// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.devtools.ksp") version "1.7.20-1.0.8" apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:2.0.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
    }
}