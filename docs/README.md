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
- `ktlintFormat`

## Detekt

> Detekt is a static code analyzer for Kotlin.

[Detekt] helps you write cleaner Kotlin code so you can focus on what matters the most building amazing software.

Check [Detekt docs] for more info. Some interesting entries are:

- [Configuration for Compose]
- [Complexity rules]


Gradle _root/build.gradle_ configuration:

```
buildscript {
    ext {
        ...
        detekt_version = "1.22.0-RC1"
    }
}

plugins {
    ...
    id "io.gitlab.arturbosch.detekt" version "$detekt_version"
}

allprojects {
    apply plugin: "io.gitlab.arturbosch.detekt"

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detekt_version")
    }
}
```

Once the project is synced then check these detekt gradle tasks are available:

- `detektGenerateConfig` (For initial detekt.yml file creation)
- `detektBaseline` (For creating a detekt-baseline.xml if needed)
- `detekt`

### Run detekt using a Git pre-commit hook

Detekt can be integrated into your development workflow by using a Git pre-commit hook. For that reason Git supports to run custom scripts automatically, when a specific action occurs.

The shell script can be installed by copying the content over to `<<your-repo>>/.git/hooks/pre-commit`. This pre-commit hook needs to be executable, so you may need to change the permission (`chmod +x pre-commit`)

<details>
  <summary>Show pre-commit script running Detekt</summary>

```
#!/usr/bin/env bash
echo "Running detekt check..."
OUTPUT="/tmp/detekt-$(date +%s)"
./gradlew detekt > $OUTPUT
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  cat $OUTPUT
  rm $OUTPUT
  echo "***********************************************"
  echo "                 Detekt failed                 "
  echo " Please fix the above issues before committing "
  echo "***********************************************"
  exit $EXIT_CODE
fi
rm $OUTPUT
```

**Note:** The pre-commit hook verification can be skipped for a certain commit like so:

`git commit --no-verify -m "commit message"` or
`git commit -n -m "commit message"`
</details>

<details>
  <summary>Show pre-commit script running Detekt and Ktlint</summary>

```
#!/usr/bin/env bash
echo "Running detekt check..."
OUTPUT="/tmp/detekt-$(date +%s)"
./gradlew detekt > $OUTPUT
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  cat $OUTPUT
  rm $OUTPUT
  echo "***********************************************"
  echo "                 Detekt failed                 "
  echo " Please fix the above issues before committing "
  echo "***********************************************"
  exit $EXIT_CODE
else
  echo "Running ktlint check..."
  OUTPUT="/tmp/ktlint-$(date +%s)"
  ./gradlew ktlintCheck > $OUTPUT
  EXIT_CODE=$?
  if [ $EXIT_CODE -ne 0 ]; then
    cat $OUTPUT
    rm $OUTPUT
    echo "***********************************************"
    echo "                 Ktlint failed                 "
    echo " Please fix the above issues before committing "
    echo "***********************************************"
    exit $EXIT_CODE
  fi
fi
rm $OUTPUT
```
</details>


## GitHub Actions

> GitHub Actions makes it easy to automate all your software workflows, now with world-class CI/CD. Build, test, and deploy your code right from GitHub.

[GitHub Actions] automates, customizes, and executes your software development workflows right in your repository.

### QuickStart
- Create a `.github/workflows` directory in your repository on GitHub if this directory does not already exist.
- In the `.github/workflows` directory, create a file named `github-actions-android.yml`.
- Copy the following YAML contents into the `github-actions-android.yml` file:

<details>
    <summary>Show an example of GitHubs Actions `yml` file for Android</summary>


```
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
          cache: gradle

      - name: Run ktlint
        run: ./gradlew ktlintCheck

      - name: Run detekt
        run: ./gradlew detekt

      - name: Run compileDebugKotlin
        run: ./gradlew compileDebugKotlin

      - name: Run Tests
        run: ./gradlew test
    
```

</details>


## Signing Config

> In gradle based android projects, the signing configuration could be specified in the gradle build scripts with `signingConfigs` 

See official [signingConfigs] documentation.

<details>
<summary>Show `signingConfigs` example</summary>

Gradle _app/build.gradle_ configuration:

```
// Create a variable called keystorePropertiesFile, and initialize it to your keystore.properties file, in the rootProject folder.
// And load the properties. Now a release bundle could be generated like this: ./gradlew bundleRelease
def KEYSTORE_PATH = "./keystore/keystore/keystore_pkcs12.properties"
def keystorePropertiesFile = rootProject.file(KEYSTORE_PATH)
def keystoreProperties = new Properties()
try {
    keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
} catch (Exception e) {
    println("WARNING! Keystore files not found! KeystoreProperties couldn't be loaded.\nCheck filepath: $e.message")
}

android {
    signingConfigs {
        config {
            try {
                keyAlias keystoreProperties['keyAlias']
                keyPassword keystoreProperties['keyPassword']
                storeFile file(keystoreProperties['storeFile'])
                storePassword keystoreProperties['storePassword']
            } catch (Exception e) {
                println("WARNING! KeystoreProperties not loaded!")
            }
        }
    }
}

buildTypes {
    ...
}
```

**Note:** You might want to ignore keystore files in `.gitignore` to avoid commiting compromised data in your repository:

```
# Keystore files
/keystore
*.jks
*.keystore
```

</details>


[//]: # (Document links)

[Ktlint]: <https://pinterest.github.io/ktlint/>
[integrations/wrappers]: <https://pinterest.github.io/ktlint/install/integrations/>
[jlleitschuh/ktlint-gradle]: <https://github.com/jlleitschuh/ktlint-gradle>
[Detekt]: <https://detekt.dev/>
[Detekt docs]: <https://detekt.dev/docs/intro/>
[Configuration for Compose]: <https://detekt.dev/docs/introduction/compose>
[Complexity rules]: <https://detekt.dev/docs/rules/complexity>
[GitHub Actions]: <https://github.com/features/actions>
[signingConfigs]: <https://developer.android.com/studio/publish/app-signing#secure-shared-keystore>
