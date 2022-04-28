plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

val koinVersion= "3.1.6"

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    implementation("io.coil-kt:coil:1.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.ui:ui-tooling:1.1.1")
    implementation("androidx.compose.foundation:foundation:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.material:material-icons-core:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0-beta01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.accompanist:accompanist-coil:0.12.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.12.0")
    implementation("com.google.accompanist:accompanist-insets-ui:0.12.0")
    implementation("androidx.navigation:navigation-compose:2.5.0-beta01")


    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.frankegan.pokedex.android"
        minSdk = 21
        targetSdk = 30
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
        kotlinCompilerExtensionVersion = "1.2.0-alpha08"
    }
}