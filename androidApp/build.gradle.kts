plugins {
    id("com.android.application")
    kotlin("android")
}

val koinVersion= "3.0.2"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.fragment:fragment-ktx:1.3.4")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("androidx.paging:paging-runtime:3.0.0")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-android-ext:$koinVersion")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.frankegan.pokedex.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}