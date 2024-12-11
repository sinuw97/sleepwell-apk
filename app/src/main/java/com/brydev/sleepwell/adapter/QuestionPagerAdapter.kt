package com.brydev.sleepwell.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brydev.sleepwell.R
import com.brydev.sleepwell.model.InputType
import com.brydev.sleepwell.model.Question

class QuestionPagerAdapter(
    private val questions: List<Question>,
    private val answers: MutableMap<String, Any>,
    private val onAnswerCompleted: (Int) -> Unit
) : RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, answers)

        holder.nextButton.setOnClickListener {
            when (question.inputType) {
                InputType.NUMBER -> {
                    val answer = holder.answerInput.text.toString()
                    if (answer.isEmpty()) {
                        Toast.makeText(holder.itemView.context, "Masukkan jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    answers[question.key] = answer.toFloatOrNull() ?: 0f
                }
                InputType.RADIO -> {
                    val selectedRadioId = holder.radioGroup.checkedRadioButtonId
                    if (selectedRadioId == -1) {
                        Toast.makeText(holder.itemView.context, "Pilih salah satu opsi terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val selectedOptionIndex = holder.radioGroup.indexOfChild(holder.itemView.findViewById(selectedRadioId))
                    answers[question.key] = question.options?.get(selectedOptionIndex) ?: ""
                }
            }

            onAnswerCompleted(position)
        }

        holder.backButton.setOnClickListener {
            onAnswerCompleted(position - 1)
        }
    }

    override fun getItemCount(): Int = questions.size

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.question_text)
        val answerInput: EditText = view.findViewById(R.id.answer_input)
        val radioGroup: RadioGroup = view.findViewById(R.id.answer_radio_group)
        val nextButton: Button = view.findViewById(R.id.btn_next)
        val backButton: Button = view.findViewById(R.id.btn_back)

        fun bind(question: Question, answers: MutableMap<String, Any>) {
            questionText.text = question.text
            answerInput.visibility = if (question.inputType == InputType.NUMBER) View.VISIBLE else View.GONE
            radioGroup.visibility = if (question.inputType == InputType.RADIO) View.VISIBLE else View.GONE

            if (question.inputType == InputType.NUMBER) {
                val existingAnswer = answers[question.key] as? Float
                answerInput.setText(existingAnswer?.toString() ?: "")
            }

            if (question.inputType == InputType.RADIO && question.options != null) {
                radioGroup.removeAllViews()
                question.options.forEachIndexed { index, option ->
                    val radioButton = RadioButton(itemView.context).apply { text = option }
                    radioGroup.addView(radioButton)
                    if (answers[question.key] == option) {
                        radioButton.isChecked = true
                    }
                }
            }
        }
    }
}
