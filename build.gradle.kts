import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

group = "com.rayliu"
version = "1.0.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.compose") apply false
    id("org.jetbrains.compose") apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    disabledRules.set(setOf("import-ordering"))
    reporters {
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    config = files("config/detekt/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(false) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(false) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(false) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}
