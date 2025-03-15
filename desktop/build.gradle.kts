import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "com.rayliu"
version = "1.0.0"


kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.apache.commons:commons-lang3:3.12.0")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.uiTestJUnit4)
                implementation("io.mockk:mockk:1.13.5")
                implementation("junit:junit:4.12")
                implementation("com.google.truth:truth:1.1.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "IntentSender"
            packageVersion = version.toString()
        }

        buildTypes.release.proguard {
            obfuscate.set(true)
        }
    }
}
