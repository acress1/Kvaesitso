import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/NOTICE.md")
    }

    compileSdk = sdk.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "de.mm20.launcher2"
        minSdk = sdk.versions.minSdk.get().toInt()
        targetSdk = sdk.versions.targetSdk.get().toInt()
        @SuppressLint("HighAppVersionCode")
        versionCode = 2023022101
        versionName = "1.22.1"
        signingConfig = signingConfigs.getByName("debug")
    }

    signingConfigs {
        create("gh-actions") {
            storeFile = file("${System.getenv("RUNNER_TEMP")}/keystore/keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            applicationIdSuffix = ".release"

            postprocessing {
                isRemoveUnusedCode = true
                isObfuscate = false
                isOptimizeCode = true
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            // Jetpack Compose is unusably laggy in debug builds, it's ridiculous
            // This somehow seems to resolve that issue.
            isDebuggable = false
        }
        create("nightly") {
            initWith(getByName("release"))
            matchingFallbacks += "release"
            applicationIdSuffix = ".nightly"
            versionNameSuffix = "-${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))}-nightly"
            signingConfig = signingConfigs.findByName("gh-actions")
        }

        flavorDimensions += "variant"
        productFlavors {
            create("default") {
                dimension = "variant"
            }
            create("fdroid") {
                dimension = "variant"
                versionNameSuffix = "-fdroid"
            }
        }
    }
    configurations.all {
        //Fixes Error: Duplicate class: com.google.common.util.concurrent.ListenableFuture
        exclude(group = "com.google.guava", module = "listenablefuture")
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lint {
        abortOnError = false
    }
    namespace = "de.mm20.launcher2"
}


dependencies {
    implementation(libs.bundles.kotlin)

    //Android Jetpack
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.palette)
    implementation(libs.androidx.core)
    implementation(libs.androidx.exifinterface)
    implementation(libs.materialcomponents.core)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.androidx.work)

    implementation(libs.coil.core)
    implementation(libs.coil.svg)


    implementation(libs.koin.android)

    implementation(project(":services:accounts"))
    implementation(project(":data:applications"))
    implementation(project(":data:appshortcuts"))
    implementation(project(":services:backup"))
    implementation(project(":services:badges"))
    implementation(project(":core:base"))
    implementation(project(":data:calculator"))
    implementation(project(":data:calendar"))
    implementation(project(":data:contacts"))
    implementation(project(":core:crashreporter"))
    implementation(project(":data:currencies"))
    implementation(project(":data:customattrs"))
    implementation(project(":data:favorites"))
    implementation(project(":data:files"))
    implementation(project(":libs:g-services"))
    implementation(project(":core:i18n"))
    implementation(project(":services:icons"))
    implementation(project(":core:ktx"))
    implementation(project(":libs:ms-services"))
    implementation(project(":services:music"))
    implementation(project(":libs:nextcloud"))
    implementation(project(":data:notifications"))
    implementation(project(":libs:owncloud"))
    implementation(project(":core:permissions"))
    implementation(project(":core:preferences"))
    implementation(project(":services:search"))
    implementation(project(":services:tags"))
    implementation(project(":data:unitconverter"))
    implementation(project(":app:ui"))
    implementation(project(":data:weather"))
    implementation(project(":data:websites"))
    implementation(project(":data:widgets"))
    implementation(project(":data:wikipedia"))
    implementation(project(":core:database"))
    implementation(project(":data:search-actions"))
    implementation(project(":services:global-actions"))

    // Uncomment this if you want annoying notifications in your debug builds yelling at you how terrible your code is
    //debugImplementation(libs.leakcanary)
}