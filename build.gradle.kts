plugins {
    id("java")
    id("maven-publish")
}

val adventureVersion = "4.14.0"
val platformVersion = "4.3.1"

val targetJavaVersion = 17

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = "kor.toxicity.selection"
    version = "1.0"

    repositories {
        mavenCentral()
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://libraries.minecraft.net/")
    }
    dependencies {
        compileOnly("net.kyori:adventure-api:$adventureVersion")
        compileOnly("net.kyori:adventure-platform-bukkit:$platformVersion")
        compileOnly("com.mojang:authlib:5.0.47")

        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    }
    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(targetJavaVersion)
        }
        test {
            useJUnitPlatform()
        }
        processResources {
            val props = mapOf(
                "version" to version.toString(),
                "adventure" to adventureVersion,
                "platform" to platformVersion
            )
            filteringCharset = Charsets.UTF_8.name()
            inputs.properties(props)
            filesMatching("plugin.yml") {
                expand(props)
            }
        }
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}