plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

val koinVersion= "3.0.2"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0")
    implementation("androidx.paging:paging-compose:1.0.0-alpha10")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-android-ext:$koinVersion")
    implementation("androidx.compose.ui:ui:1.0.0-beta09")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta09")
    implementation("androidx.compose.foundation:foundation:1.0.0-beta09")
    implementation("androidx.compose.material:material:1.0.0-beta09")
    implementation("androidx.compose.material:material-icons-core:1.0.0-beta09")
    implementation("androidx.compose.material:material-icons-extended:1.0.0-beta09")
    implementation("androidx.compose.runtime:runtime-livedata:1.0.0-beta09")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha08")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-beta02")
    implementation("com.google.accompanist:accompanist-coil:0.12.0")


    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-beta09")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.frankegan.pokedex.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerVersion = "1.5.10"
        kotlinCompilerExtensionVersion = "1.0.0-beta08"
    }
}