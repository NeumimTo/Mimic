plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    val kotlinVersion = "1.6.10"
    implementation(kotlin("gradle-plugin", version = kotlinVersion))
    implementation(kotlin("serialization", version = kotlinVersion))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:0.8.0")
    implementation("de.undercouch:gradle-download-task:4.1.2")
}

repositories {
    mavenCentral()
}
