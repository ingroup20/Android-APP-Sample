plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.myfirstapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myfirstapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //-------------------------------------------
    // ImagePicker - 用於簡單的圖片選擇
    implementation("com.github.dhaval2404:imagepicker:2.1.0") // 使用最新版本

    // Retrofit - 用於與 API 進行網絡通信
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit 核心庫
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // 用於解析 JSON 格式的轉換器

    // OkHttp - 用於 Retrofit 的網絡請求
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // 核心庫
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0") // 用於網絡請求的日誌拦截器（可選）

    // Kotlin Coroutines - 用於協程支持（viewModelScope 等功能）
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0") // 協程 Android 支持

    // Lifecycle - 用於 ViewModel 支援
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0") // 支援 ViewModel

    // Gson - 用於 JSON 轉換
    implementation("com.google.code.gson:gson:2.8.8") // Gson 庫，用於 JSON 解析
}
