import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val main = "com.arkivanov.decompose:decompose:2.0.0"
        val ext = "com.arkivanov.decompose:extensions-compose-jetbrains:2.0.0"
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

                val decompose = "2.0.0"
                implementation("com.arkivanov.decompose:decompose:${decompose}")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:${decompose}")

            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MultiplatformsDesktop1"
            packageVersion = "1.0.0"
        }
    }
}
