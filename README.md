# Gradle Import Check Plugin
[ ![artifactory](https://img.shields.io/badge/Artifactory-v0.1.0-green) ](https://artfable.jfrog.io/ui/packages/gav:%2F%2Fcom.artfable.gradle:gradle-import-check-plugin)

## Overview
Allows finding classes with specific imports 
 

## Install
```kotlin
buildscript {
    repositories {
        maven(url = "https://artfable.jfrog.io/artifactory/default-maven-local")
    }
    dependencies {
        classpath("com.artfable.gradle:gradle-import-check-plugin:0.1.0")
    }
}

apply(plugin = "artfable.import.check")
```

**_NOTE!_** `org.gradle.jvmargs=--illegal-access=permit` required in `gradle.properties`

For use in `plugins {}` see [Gradle resolution strategy](https://docs.gradle.org/current/userguide/custom_plugins.html#note_for_plugins_published_without_java_gradle_plugin) 

It'll add a task `importCheck`

## Usage

For groovy DSL:

```groovy
importCheck {
    // failBuild = true // default
    group {
        // warning = false // default
        source = "$projectDir/src/test/java"
        patterns = ["org\\.junit\\.(?!jupiter).*"]
    }
    group {
        // ...
    }
}
```

or for kotlin DSL:

```kotlin
configure<ImportCheckExtension> { // can be importCheck { if plugin added through plugins {}
    // failBuild = true // default
    group {
        // warning = false // default
        source = "$projectDir/src/test/java"
        patterns = setOf("org\\.junit\\.(?!jupiter).*")
    }
    group {
        // ...
    }
}
```

Put `warning = true` to put check for that group as a warning and not fail the build for that group

### Example of resolution strategy

As package group id and artifact id is different from what expected by `pugins {}` block by default, resolution strategy should be provided:

In `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven(url = "https://artfable.jfrog.io/artifactory/default-maven-local")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace?.startsWith("artfable") == true) {
                useModule("${extra["custom.plugins.${requested.id}"]}:${requested.version}")
            }
        }
    }
}
```

and in `gradle.properties`
```properties
custom.plugins.artfable.import.check=com.artfable.gradle:gradle-import-check-plugin
```