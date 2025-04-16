plugins {
    id("application")
    id("checkstyle")
    id("jacoco")
    id("io.freefair.lombok") version "8.13.1"
    id("org.sonarqube") version "6.1.0.5360"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

checkstyle {
    toolVersion = "10.23.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-bundle:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("gg.jte:jte:3.2.0")

    testImplementation(platform("org.junit:junit-bom:5.12.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    reports {
        html.required = false
    }
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
    }
}

sonar {
    properties {
        property("sonar.projectKey", "AdalyatNazirov_java-project-72")
        property("sonar.organization", "adalyatnazirov")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}