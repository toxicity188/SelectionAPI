dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly(project(":api"))
}

tasks {
    jar {
        archiveFileName.set("SelectionAPI-ExamplePlugin.jar")
    }
}