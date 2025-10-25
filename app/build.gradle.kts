plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "br.thiago.habitflowapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.thiago.habitflowapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.material3)

    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")


    // https://developer.android.com/jetpack/compose/navigation?hl=pt-br
    implementation ("androidx.navigation:navigation-compose:2.5.3")


    //https://developer.android.com/training/dependency-injection/hilt-android?hl=pt-br#kts

    implementation(libs.hilt.android)
    implementation ("androidx.hilt:hilt-navigation-compose:1.3.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    //splash screen
    implementation(libs.androidx.core.splashscreen)

    kapt(libs.hilt.android.compiler)

    //  Firebase store configuration
    implementation (libs.firebase.bom)
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")

    //Firebase Auth configuration

    implementation (libs.firebase.auth.ktx)

    //
    implementation (libs.firebase.config)
    // https://firebase.google.com/docs/storage/android/start?hl=pt

    implementation ("com.google.firebase:firebase-storage-ktx:21.0.2")

    //JSON

    implementation ("com.google.code.gson:gson:2.11.0")

    //https://coil-kt.github.io/coil/compose/
    //Async Image
    implementation("io.coil-kt:coil-compose:2.6.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}