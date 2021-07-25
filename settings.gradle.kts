rootProject.name = "gradle-import-check-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://artfable.jfrog.io/artifactory/default-maven-local")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "artfable") {
                useModule("${extra["custom.plugins.${requested.id}"]}:${requested.version}")
            }
        }
    }
}

