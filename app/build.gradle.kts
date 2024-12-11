plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.brydev.sleepwell"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.brydev.sleepwell"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }

        buildFeatures {
            dataBinding = true
            viewBinding= true
        }
    }

    dependencies {
        implementation ("androidx.viewpager2:viewpager2:1.1.0")
        implementation (libs.lottie)
        implementation (libs.play.services.auth.v2050)
        implementation(libs.androidx.espresso.core)
        testImplementation(libs.junit.jupiter)
        implementation(libs.gson)
        implementation(libs.glide)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)
        implementation(libs.androidx.room.runtime)
        implementation(libs.play.services.auth.v2070)
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.okhttp)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }

