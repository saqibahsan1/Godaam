// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ktLint) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false

}
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.hilt.classpath)
    }
}

tasks.create<Delete>("cleanRP") {
    group = "rp"
    delete = setOf(
        "rp-out",
        "rp-zip"
    )
}
true // Needed to make the Suppress annotation work for the plugins block