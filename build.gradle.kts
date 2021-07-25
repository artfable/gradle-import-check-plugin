group = "com.artfable.gradle"
version = "0.0.3"

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    kotlin("jvm") version "1.5.21"
    id("com.jfrog.artifactory") version "4.24.14"
    `maven-publish`
    id("artfable.artifact") version "0.0.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(gradleApi())
    implementation(localGroovy())
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }
}

val user = if (project.hasProperty("artifactoryUser")) {
    project.ext["artifactoryUser"]
} else System.getenv("ARTIFACTORY_USER")
val key = if (project.hasProperty("artifactoryKey")) {
    project.ext["artifactoryKey"]
} else System.getenv("ARTIFACTORY_KEY")

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourceJar"])
            artifact(tasks["javadocJar"])

            pom {
                description.set("Check imports in java classes")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://raw.githubusercontent.com/artfable/gradle-import-check-plugin/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("artfable")
                        name.set("Artem Veselov")
                        email.set("art-fable@mail.ru")
                    }
                }
            }
        }
    }
}

artifactory {
    setContextUrl("https://artfable.jfrog.io/artifactory/")
    publish {
        repository {
            setRepoKey("default-maven-local")
            setUsername(user)
            setPassword(key)
        }
        defaults {
            publications ("mavenJava")

            setPublishArtifacts(true)
            setPublishPom(true)
            setPublishIvy(false)
        }
    }
}