plugins {
    id("java")
}

group = "me.gorbunov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")

    tasks.test {
        useJUnitPlatform()
    }
}
