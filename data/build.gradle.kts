plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    jacoco
}

android {
    namespace = "com.jeandealmeida_dev.itunesplayer.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 27
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

afterEvaluate {
    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")
        reports {
            xml.required = true
            html.required = false
        }
        val exclusions = listOf("**/R.class", "**/R\$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Module.*")
        classDirectories.setFrom(
            fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") { exclude(exclusions) }
        )
        sourceDirectories.setFrom(files("src/main/java"))
        executionData.setFrom(
            layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        )
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.paging.runtime)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
