# Gradle Import Check Plugin
[ ![artifactory](https://img.shields.io/badge/Artifactory-v0.0.3-green) ](https://artfable.jfrog.io/ui/packages/gav:%2F%2Fcom.artfable.gradle:gradle-import-check-plugin)

## Overview
Allows finding classes with specific imports 
 

## Install
```kotlin
buildscript {
    repositories {
        maven(url = "https://artfable.jfrog.io/artifactory/default-maven-local")
    }
    dependencies {
        classpath("com.artfable.gradle:gradle-import-check-plugin:0.0.3")
    }
}

apply(plugin = "artfable.import.check")
```

For use in `plugins {}` see [Gradle resolution strategy](https://docs.gradle.org/current/userguide/custom_plugins.html#note_for_plugins_published_without_java_gradle_plugin) 

It'll add a task `importCheck`

## Usage

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

or

```kotlin
configure<ImportCheckExtension> {
    // failBuild = true // default
    group(delegateClosureOf<ImportCheckGroup> {
        // warning = false // default
        source = "$projectDir/src/test/java"
        patterns = setOf("org\\.junit\\.(?!jupiter).*")
    })
    group(delegateClosureOf<ImportCheckGroup> {
        // ...
    })
}
```

Put `warning = true` to put check for that group as a warning and not fail the build for that group

