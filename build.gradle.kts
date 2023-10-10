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

    tasks.test {
        useJUnitPlatform()
    }
}
