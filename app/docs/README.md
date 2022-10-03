# DOCUMENTATION

Here you can find the documentation of the main stack used in this project.

## Ktlint

> Ktlint is an anti-bikeshedding Kotlin linter with built-in formatter.

[Ktlint] is an open source library from Pinterest which have several [integrations/wrappers]. This project make use of the [jlleitschuh/ktlint-gradle] Gradle plugin which automatically creates check and format tasks for project Kotlin sources. It supports different kotlin plugins and Gradle build caching.


Gradle _root/build.gradle_ configuration:

```
buildscript {
    ext {
        ...
        ktlint_version = "11.0.0"
    }
}

plugins {
    ...
    id "org.jlleitschuh.gradle.ktlint" version "$ktlint_version"
}

allprojects {
    apply plugin: "org.jlleitschuh.gradle.ktlint"
}
```

Once the project is synced then check these ktlint gradle tasks are available:

- `ktlintCheck`
- `ktlintFormat

[//]: # (Document links)

[Ktlint]: <https://pinterest.github.io/ktlint/>
[integrations/wrappers]: <https://pinterest.github.io/ktlint/install/integrations/>
[jlleitschuh/ktlint-gradle]: <https://github.com/jlleitschuh/ktlint-gradle>
