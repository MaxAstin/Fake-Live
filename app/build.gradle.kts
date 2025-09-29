plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bunbeauty.tiptoplive"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bunbeauty.tiptoplive"
        minSdk = 27
        targetSdk = 36
        versionCode = 440
        versionName = "4.4.0"
        multiDexEnabled = true
        setProperty("archivesBaseName", "FakeLive-$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions.add("default")
    productFlavors {
        create("video") {
            buildConfigField("Boolean", "SHOW_CAMERA", "false")
        }
        create("camera") {
            buildConfigField("Boolean", "SHOW_CAMERA", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
        cmake {
            path = File("cpp","CMakeLists.txt")
            version = "4.1.1"
        }
    }
    ndkVersion = "29.0.14033849"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.core.splashscreen)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.compose.animation)

    // Camera
    implementation(libs.bundles.camera2)

    // Dagger/Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)

    // Media3
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)

    // In-App Review
    implementation(libs.review)

    // Image Cropping
    implementation(libs.image.cropper)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)

    // Billing
    implementation(libs.billing.ktx)

    // Ktor
    implementation(libs.bundles.ktor)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Lottie
    implementation(libs.lottie)

    // Work manager
    implementation(libs.work)

    // Datastore
    implementation(libs.bundles.datastore)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
}