import com.jfrog.bintray.gradle.BintrayExtension

group = "com.github.artfable.gradle"
version = "0.0.1"

buildscript {
    val bintray_version = "1.8.4"
    val artifact_plugin_version = "0.0.1"

    repositories {
        jcenter()
        mavenCentral()
        maven(url = "http://dl.bintray.com/artfable/gradle-plugins")
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:$bintray_version")
        classpath("com.github.artfable.gradle:gradle-artifact-plugin:$artifact_plugin_version")
    }
}

plugins {
    java
    kotlin("jvm") version "1.3.61"
}

apply(plugin = "maven-publish")
apply(plugin = "com.jfrog.bintray")
apply(plugin = "artfable.artifact")

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    // FIXME: it part of jdk, should be there
    implementation(files("${System.getProperty("java.home")}/../lib/tools.jar"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation(localGroovy())
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

configure<PublishingExtension> {
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
                        url.set("https://github.com/artfable/gradle-import-check-plugin/master/LICENSE")
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

configure<BintrayExtension> {
    user = if (project.hasProperty("bintrayUser")) {
        project.ext["bintrayUser"] as String
    } else System.getenv("BINTRAY_USER")
    key = if (project.hasProperty("bintrayKey")) {
        project.ext["bintrayKey"] as String
    } else System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "gradle-plugins"
        name = "gradle-import-check-plugin"
        setLicenses("MIT")
        vcsUrl = "https://github.com/artfable/gradle-import-check-plugin.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            attributes =
                mapOf(Pair("gradle-plugin", "artfable.import.check:com.github.artfable.gradle:gradle-import-check-plugin"))
        })
    })
}