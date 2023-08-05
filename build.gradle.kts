plugins {
    id("java")
    id("maven-publish")
}

group = "club.someoneice.imgencrypt"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // implementation("com.google.zxing:core:3.5.1")
    // implementation("com.google.zxing:javase:3.5.1")
}

tasks.jar.configure {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Main-Class"] = "club.someoneice.imgencrypt.Main"
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}

publishing {
    repositories {
        maven {
            name = "ImgEncrypt"
            url = uri("https://maven.pkg.github.com/amarokice/imgencrypt")
            credentials {
                username = "AmarokIce"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}