# Gradle Import Check Plugin
(version: 0.0.1)

## Overview
Allow to find classes with specific imports 
 

## Install
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.github.artfable.gradle:gradle-import-check-plugin:0.0.1"
    }
}

apply plugin: 'artfable.import.check'
```

or

```kotlin
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.github.artfable.gradle:gradle-import-check-plugin:0.0.1")
    }
}
apply(plugin = "artfable.import.check")
```

It'll add a task `importCheck`

## Usage

```groovy
importCheck {
    // failBuild = true // default
    group {
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
        source = "$projectDir/src/test/java"
        patterns = setOf("org\\.junit\\.(?!jupiter).*")
    })
    group(delegateClosureOf<ImportCheckGroup> {
        // ...
    })
}
```

