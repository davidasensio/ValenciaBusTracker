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

## Back up user data with Auto Backup
> [Auto Backup] for Apps automatically backs up a user's data from apps that target and run on Android 6.0 (API level 23) or higher. Android preserves app data by uploading it to the user's Google Drive—where it's protected by the user's Google account credentials. The backup is end-to-end encrypted on devices running Android 9 or higher using the device's pin, pattern, or password. The amount of data is limited to 25MB per user of your app.

For enabling **Auto Backup** on your app for all Android API versions just enable it on _AndroidManifest.xml_ and link the correspondent `xml` files that will be used depending on the device Android version:


- `android:allowBackup="true"`
- `android:fullBackupContent="@xml/backup_rules"` (Needed on devices running Android 11 or lower)
- `android:dataExtractionRules="@xml/data_extraction_rules"` (Needed on devices running Android 12 or higher)

_./xml/backup\_rules.xml:_

```
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <include domain="sharedpref" path="." />
</full-backup-content>
```

_./xml/data\_extraction\_rules.xml:_

```
<?xml version="1.0" encoding="utf-8"?>
<data-extraction-rules>
    <cloud-backup disableIfNoEncryptionCapabilities="false">
        <include domain="sharedpref" path="." />
    </cloud-backup>
    <device-transfer>
        <include domain="sharedpref" path="." />
    </device-transfer>
</data-extraction-rules>
```

**Important**: Even if your app targets Android 12 or higher, you must also specify another set of XML backup rules to support devices that run Android 11 (API level 30) or lower. So overall, both files are needed.

### Test backup and restore

The Auto Backup can be forced via `adb` for an specific app by package:

```
adb shell bmgr backupnow com.handysparksoft.valenciabustracker
```

Then when the app is re-installed the restored data could be checked.

## Firebase Crashlytics

> Add [Firebase Crashlytics] to your app to help you track, prioritize, and fix stability issues that erode your app quality.

Android Studio has a **Firebase Assistant** that can help you to configure many things such Crashlytics. So just open the assistant and follow the steps:

1. Connect your app with Firbase
2. Add Crashlytics SDK and plugin to your app
3. Force a crash to finish the setup, just throwing a `RuntimeException("Test Crash")` 

<image src="./images/firebase_assistant_crashlytics_1.png" alt="Firebase assistant" width=300 style="float: left;margin-right: 20px"/>
<image src="./images/firebase_assistant_crashlytics_2.png" width=300 />

Regarding the connection the assistant will redirect you to your [Firebase Console] and will ask you to create or link a project to your app. Once your app is linked to a Firebase project you will see that a new file has been created in your project:  `google-services.json`

Regarding the **SDK** you will end up with these changes in your gradle files and that should have been added automagically:

In your **root-level (project-level)** greadle file (`<project>/build.gradle`) dependencies:

```
buildscript {
ext {
    compose_version = "1.2.1"
    ...
}
dependencies {
    classpath 'com.google.gms:google-services:4.3.14'
    classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.2'
}
}
```

In your **module (app-level)** greadle file (`<project>/<app-module>/build.gradle`) dependencies:

```
plugins {
    ...
    id 'com.google.gms.google-services'
    id 'com.google.firebase.crashlytics'
}
dependencies {
    ...
    implementation 'com.google.firebase:firebase-crashlytics-ktx:18.3.0'
    implementation 'com.google.firebase:firebase-analytics-ktx:21.2.0'
}
    
```


For next steps check the [Get Started Guide with Crashlytics] documentation to learn more about it.

## Compose Bill of Materials (BOM)

> A [BOM] is a Maven module that declares a set of libraries with their versions. It will greatly simplify the way you define Compose library versions in your Gradle dependencies block, especially now that Google moved the various Jetpack Compose libraries to independent versioning schemes.

```
dependencies {
    // Import the Compose BOM
    implementation platform('androidx.compose:compose-bom:2022.10.00')
    
    // Declare dependencies for the desired Compose libraries without versions
    implementation 'androidx.compose.foundation:foundation'
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    ...
}
``` 

Instead of defining each version separately, which can become cumbersome and prone to errors when library versions start to differ, you now only need to define one BOM version and all Compose library versions will be extracted from that. Google will publish a new version of the BOM every time a Compose artifact has a new **stable release**, so moving from stable release to stable release is going to be much simpler.

Note that you can still choose to define your dependencies using hard-coded versions. The BOM is added as a useful way to simplify dependencies and make upgrades easier.

To find out which Compose library versions are mapped to a specific BOM version, check out the [BOM to library version mapping].


## AdMob Ads

> Integrating the Google Mobile Ads SDK into an app is the first step toward displaying ads and earning revenue. Once you've integrated the SDK, you can choose an ad format (such as native or rewarded video) and follow the steps to implement it. Check the quick [AdMob Quick Start Guide] for more info.

These are the different types of ads you can add to your app:

<image src="./images/admob_ad_types.png" alt="AdMob Ad Types" width=800 />

## Multipreview Annotations

> With [multipreview annotations], you can define an annotation class that itself has multiple @Preview annotations with different configurations. Adding this annotation to a composable function will automatically render all the different previews at once. For example, you can use this annotation to preview multiple devices, font sizes, or themes at the same time without repeating those definitions for every single composable.

You can use them like this:

```
@Preview(name = "1. Light Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "2. Dark Theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreviews

@Preview(name = "3. large font", group = "font scales", fontScale = 1.5f)
@Preview(name = "4. small font", group = "font scales", fontScale = 0.5f)
annotation class FontScalePreviews

...

@ThemePreviews
@FontScalePreviews
@Composable
fun HelloWorldPreview() {
    Text("Hello World")
}
```

**Note:** notice that the number prefix in names (`1.` `2.` etc) is for the preview to show the desired previews in such order otherwise the previews will be rendered alphabetically.

<image src="./images/multipreviews.png" width=600 />

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
[Auto Backup]: <https://developer.android.com/guide/topics/data/autobackup>
[Firebase Crashlytics]: <https://firebase.google.com/products/crashlytics>
[Get Started Guide with Crashlytics]: <https://firebase.google.com/docs/crashlytics/get-started?hl=en&platform=android>
[Firebase Console]: <https://console.firebase.google.com/>
[AdMob Quick Start Guide]: <https://developers.google.com/admob/android/quick-start>
[BOM]: <https://developer.android.com/jetpack/compose/setup#using-the-bom>
[BOM to library version mapping]: <https://developer.android.com/jetpack/compose/setup#bom-version-mapping>
[multipreview annotations]: <https://developer.android.com/jetpack/compose/tooling#preview-multipreview>
