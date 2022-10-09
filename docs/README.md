# DOCUMENTATION

Here you can find the documentation of the main stack used in this project.

## Ktlint

> Ktlint is an anti-bikeshedding Kotlin linter with built-in formatter.

[Ktlint] is an open source library from Pinterest which have several [integrations/wrappers]. This project make use of the [jlleitschuh/ktlint-gradle] Gradle plugin which automatically creates check and format tasks for project Kotlin sources. It supports different kotlin plugins and Gradle build caching.

<details>
<summary>Show ktlint gradle configuration</summary>

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
</details>

Once the project is synced then check these ktlint gradle tasks are available:

- `ktlintCheck`
- `ktlintFormat`

## Detekt

> Detekt is a static code analyzer for Kotlin.

[Detekt] helps you write cleaner Kotlin code so you can focus on what matters the most building amazing software.

Check [Detekt docs] for more info. Some interesting entries are:

- [Configuration for Compose]
- [Complexity rules]

<details>
<summary>Show detekt gradle configuration</summary>

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
</details>

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

## Timber

> [Timber] is a logger with a small, extensible API which provides utility on top of Android's normal Log class.

<details>
<summary>Show Timber configuration</summary>

Gradle _app/build.gradle_ configuration:

```
dependencies {
  implementation 'com.jakewharton.timber:timber:5.0.1'
}
```

Then initialize it Application class:

```
package com.handysparksoft.valenciabustracker

import android.app.Application
import timber.log.Timber

class ValenciaBusTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber logger initialized")
        }
    }
}
```
</details>

## Foreground services
> [Foreground services] perform operations that are noticeable to the user. A status bar notification is shown, so that users are actively aware that your app is performing a task in the foreground and is consuming system resources.

Apps that target Android 9 (API level 28) or higher and use foreground services must request the `FOREGROUND_SERVICE` permission in the Android Manifest.

Apps that target Android 13 (API level 33) have notifications turned off by default so they need to be enabled either manually or by the new [notification runtime permission]. Also, the app must request the `POST_NOTIFICATIONS` permission in the Android Manifest.

### Start foreground service on Boot Completed

If it's needed to start the foreground service after a system reboot this can be achieved with a Broadcast Receiver listening to `BOOT_COMPLETED` intent.

<details>
<summary>Show receiver configuration</summary>

_AndroidManifest_ configuration:

```
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
...
<application>
        ...
        <receiver
            android:name=".BusTrackerBootReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.USER_PRESENT" />
                <action android:name="android.intent.action.REBOOT" />
            </intent-filter>
        </receiver>
</application>        
        
```

_BusTrackerBootReceiver_ class:

```
class BusTrackerBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        BusStopTrackerService.startTheService(
            context = context,
            notificationData = NotificationData(
                contentTitle = context.getString(R.string.app_name),
                contentText = context.getString(R.string.foreground_notification_on_boot_info),
                subText = context.getString(R.string.foreground_notification_on_boot_completed)
            )
        )
        Timber.d("BroadcastReceiver triggered by $action")
    }
}
```

**Note:** If you want to test the functionality you could do it like so:

1. Add a "fake" intent action in the manifest along with the boot ones:


```
            <intent-filter>
                <action android:name="com.handysparksoft.valenciabustracker.action.TEST" />
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.USER_PRESENT" />
                <action android:name="android.intent.action.REBOOT" />
            </intent-filter>

```

2. Broadcast the action via **adb tool**

```
adb shell am broadcast -a com.handysparksoft.valenciabustracker.action.TEST -p com.handysparksoft.valenciabustracker
```
</details>
## Notification runtime permission
> Android 13 (API level 33) Tiramisú and higher supports a [notification runtime permission] for sending non-exempt (including Foreground Services (FGS)) notifications from an app: POST_NOTIFICATIONS. This change helps users focus on the notifications that are most important to them.

In this App a `@Composable PermissionUI` is in charge of taking care about the permissions logic. In order to test this part you might need to uninstall or revoke permissions:

```
# Adb commands for uninstall the app or revoke permissions:

adb shell pm uninstall com.handysparksoft.valenciabustracker
adb shell pm revoke com.handysparksoft.valenciabustracker android.permission.POST_NOTIFICATIONS
```


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
[Timber]: <https://github.com/JakeWharton/timber>
[Foreground services]: <https://developer.android.com/guide/components/foreground-services>
[notification runtime permission]: <https://developer.android.com/develop/ui/views/notifications/notification-permission>
