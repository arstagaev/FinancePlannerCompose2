pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(
            //extra["kotlin.version"] as String
            "1.9.0"
        )
        kotlin("android").version(
//            extra["kotlin.version"] as String
            "1.9.0"
        )
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(
            //extra["compose.version"] as String
            "1.4.3"
        )
    }
}

rootProject.name = "MultiplatformsDesktop1"

include(":android", ":desktop", ":common")
