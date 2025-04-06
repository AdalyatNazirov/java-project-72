plugins {
    id("java")
    id("checkstyle")
    id("org.sonarqube") version "6.1.0.5360"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

checkstyle {
    toolVersion = "10.23.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

sonar {
    properties {
        property("sonar.projectKey", "AdalyatNazirov_java-project-72")
        property("sonar.organization", "adalyatnazirov")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}