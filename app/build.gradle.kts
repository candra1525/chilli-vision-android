plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.candra.chillivision"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.candra.chillivision"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.0"

        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://chilli-vision-laravel.vercel.app/api/api/\""
        )

        buildConfigField(
            "String",
            "BASE_URL_CHAT_AI",
            "\"https://chilli-ai-chat-with-api-production.up.railway.app/\""
        )

        buildConfigField(
            "String",
            "BASE_URL_MODEL",
            "\"https://hq816h0p-5000.asse.devtunnels.ms/\""
        )

        buildConfigField(
            "String",
            "BASE_URL_MODEL_NGROK",
            "\"https://6899-114-10-99-66.ngrok-free.app/\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
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
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation)
    implementation(libs.coil)
    implementation(libs.glide)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.volley)
    annotationProcessor(libs.lifecycle.compiler)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.livedata.ktx)
    implementation(libs.viewmodel.ktx)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.datastore)
    implementation(libs.swipe.to.refresh)
    implementation(libs.sweet.alert)
    implementation(libs.nav.animation)
    implementation(libs.lottie.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}