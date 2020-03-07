# Gradle Import Check Plugin
[ ![bintray](https://api.bintray.com/packages/artfable/gradle-plugins/gradle-import-check-plugin/images/download.svg?version=0.0.2) ](https://bintray.com/artfable/gradle-plugins/gradle-import-check-plugin/0.0.2/link)

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

