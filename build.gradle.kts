// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)

    alias(libs.plugins.googleServices) apply false // Needed for Crashlytics
    alias(libs.plugins.crashlytics) apply false
}
