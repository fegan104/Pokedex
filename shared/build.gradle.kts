import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"

val coroutinesVersion = "1.6.1"
val serializationVersion = "1.0.0-RC"
val ktorVersion = "2.0.0"
val koinVersion = "3.1.6"
val sqlDelightVersion: String by project

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
            }
        }
        // Workaround for:
        //
        // The Kotlin source set androidAndroidTestRelease was configured but not added to any
        // Kotlin compilation. You can add a source set to a target's compilation by connecting it
        // with the compilation's default source set using 'dependsOn'.
        // See https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#connecting-source-sets
        //
        // This workaround includes `dependsOn(androidAndroidTestRelease)` in the `androidTest` sourceSet.
        val androidAndroidTestRelease by getting

        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.frankegan.pokedex.data.local"
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    val targetDir = File(buildDir, "xcode-frameworks")

    group = "build"
    dependsOn(framework.linkTask)
    inputs.property("mode", mode)

    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)