// Root project build config

plugins {
    id("org.jetbrains.dokka")
    id("com.github.ben-manes.versions") version "0.39.0"
}

// Common configurations for all Mimic projects
subprojects {
    apply(plugin = "commons")
    apply(plugin = "publish")

    version = "0.7"
    group = "ru.endlesscode.mimic"
}

repositories {
    mavenCentral()
}
