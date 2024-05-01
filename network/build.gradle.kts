plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "ballpark.buddy.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField( "Integer", "PIN", "0000")
            buildConfigField("String", "APP_BASE_URL", "\"https://breakdown-staging-77007924a07c.herokuapp.com/\"")
        }
        release {
            buildConfigField( "Integer", "PIN", "0000")
            buildConfigField("String", "APP_BASE_URL", "\"https://breakdown-staging-77007924a07c.herokuapp.com/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        generateStubs = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3Interceptor)
    implementation(libs.coroutines)
    implementation(libs.dagger.hilt)
    implementation(libs.timber)
    kapt(libs.hilt.android.compiler)
    implementation(libs.coroutines.android)
}