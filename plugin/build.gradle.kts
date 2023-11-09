plugins {
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    maven(url = "https://repo.inventivetalent.org/repository/public/")
}

dependencies {
    implementation(project(":api"))
    implementation(project(path = ":nms:v1_19_R3", configuration = "reobf"))
    implementation(project(path = ":nms:v1_20_R1", configuration = "reobf"))
    implementation(project(path = ":nms:v1_20_R2", configuration = "reobf"))
    implementation("org.mineskin:java-client:1.2.4-SNAPSHOT") {
        exclude("junit")
    }

    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
}

val pluginFileName = "SelectionAPI.jar"

tasks {
    jar {
        finalizedBy(shadowJar)
        archiveFileName.set(pluginFileName)
    }
    shadowJar {
        fun prefix(pattern: String) {
            relocate(pattern, "${project.group}.shaded.$pattern")
        }
        prefix("org.mineskin")
        prefix("org.jsoup")
        archiveFileName.set(pluginFileName)
    }
}