plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")

}

android {

    namespace = "com.example.weatherforecast"
    compileSdk = 34

    defaultConfig {
      // buildConfigField ("String", "MAPTILER_API_KEY", "${project.findProperty("MAPTILER_API_KEY") ?: "t"}")
        applicationId = "com.example.weatherforecast"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        // Set the API key correctly
     //   buildConfigField("String", "MAPTILER_API_KEY", "${project.findProperty("MAPTILER_API_KEY") ?: "default_key"}")

        // buildConfigField ("String", "MAPTILER_API_KEY", "${MAPTILER_API_KEY}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

//    buildFeatures {
//        buildConfig = true
//    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation( "androidx.navigation:navigation-fragment:2.5.3")
//    implementation ("androidx.navigation:navigation-ui:2.5.3")
//glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
//retrofit
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation ("androidx.work:work-runtime-ktx:2.7.1")

//location
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
   // implementation ("org.maplibre.gl:android-sdk:10.0.2")

    //Room
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
  kapt ("androidx.room:room-compiler:2.6.1")

  //  lifecycle,stateflow
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    //nav
//
//    implementation ("androidx.navigation:navigation-fragment:2.7.6")
//    implementation ("androidx.navigation:navigation-ui:2.7.6")

    // Picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    // ViewModel & LiveData
//    3
    // worker
    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation ("com.squareup.picasso:picasso:2.8")


    val androidXTestCoreVersion = ("1.4.0")
    val androidXTestExtKotlinRunnerVersion = ("1.1.3")

    val archTestingVersion = ("2.1.0")

    val coroutinesVersion = ("1.5.0")

    val espressoVersion = ("3.4.0")

    val hamcrestVersion = ("1.3")
    val junitVersion = ("4.13.2")

    val robolectricVersion = ("4.5.1")


    // AndroidX and Robolectric
    testImplementation("androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion")
    testImplementation("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation("org.robolectric:robolectric:4.8")

// InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")

// kotlinx-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

// hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")

// Dependencies for local unit tests
    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation("androidx.arch.core:core-testing:$archTestingVersion")
    testImplementation("org.robolectric:robolectric:$robolectricVersion")

// AndroidX Test - JVM testing
    testImplementation("androidx.test:core-ktx:$androidXTestCoreVersion")
//testImplementation("androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion")

// AndroidX Test - Instrumented testing
    androidTestImplementation("androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation ("androidx.arch.core:core-testing:$archTestingVersion")

//    // Dependencies for local unit tests
//    testImplementation ("junit:junit:$junitVersion")
//    testImplementation ("org.hamcrest:hamcrest-all:$hamcrestVersion")
//    testImplementation ("androidx.arch.core:core-testing:$archTestingVersion")
//    testImplementation "org.robolectric:robolectric:$robolectricVersion"
//
//    // AndroidX Test - JVM testing
//    testImplementation "androidx.test:core-ktx:$androidXTestCoreVersion"
//    //testImplementation "androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion"
//
//    // AndroidX Test - Instrumented testing
//    androidTestImplementation "androidx.test.:$androidXTestExtKotlinRunnerVersion"
//    androidTestImplementation "androidx.test.espresso:espresso-core:$espressoVersion"

}