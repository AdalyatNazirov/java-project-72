plugins {
    id("java")
    id("checkstyle")
    id("jacoco")
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
    testImplementation(platform("org.junit:junit-bom:5.12.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
        html.required = false
    }
}

sonar {
    properties {
        property("sonar.projectKey", "AdalyatNazirov_java-project-72")
        property("sonar.organization", "adalyatnazirov")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}