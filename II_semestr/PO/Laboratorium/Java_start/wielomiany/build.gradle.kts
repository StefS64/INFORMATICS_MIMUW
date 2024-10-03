plugins {
    id("java")
    application
}

group = "pl.edu.mimuw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("pl.edu.mimuw.Main")
}
