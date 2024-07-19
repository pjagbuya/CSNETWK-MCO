plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {

    mavenCentral()
}
javafx {
    version = "17.0.7"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}
dependencies {

    testImplementation("junit:junit:4.13")

    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.openjfx:javafx:17.0.7")
}
application {
    mainClass.set("org.example.Main")
}