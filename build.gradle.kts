import dependencies.Dependencies

plugins {
    id("commons.android-library")
    id("kotlin-kapt")
}

android {
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //LAYOUT
    kapt(Dependencies.DATABINDING)
    implementation(Dependencies.CONSTRAINT)
    implementation(Dependencies.ACTIVITY)
    implementation(Dependencies.FRAGMENT)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.EMBEDDABLE)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.MATERIAL_DIALOG)
    implementation(Dependencies.RECYCYLER_VIEW)
    implementation(Dependencies.RECYCYLER_VIEW_ANIMATION)
    implementation(Dependencies.VIEWPAGER2)
    implementation(Dependencies.LEARN_BACK)
    implementation(Dependencies.LEARN_BACK_PAGING)
    implementation(Dependencies.SWIPE_TO_REFERESH)
    //NAVIGATION
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_UI)
    implementation(Dependencies.NAVIGATION_RUNTIME)
    implementation(Dependencies.NAVIGATION_COMPOSE)
    //----IMAGE----//
    implementation(Dependencies.GLIDE)
    kapt(Dependencies.GLIDE_COMPILER)
    implementation(Dependencies.IMAGE_VIEW_CIRCLE)
    implementation(Dependencies.CROP)
    implementation(Dependencies.RESIZE_IMAGE)

    //CORE
    implementation(Dependencies.OKHTTP)
    implementation(Dependencies.LIFECYCLE_RUNTIME)
    implementation(Dependencies.LIFECYCLE_KTX)
    implementation(Dependencies.LIFECYCLE_VIEW_MODEL)
    implementation(Dependencies.RXJAVA)
    implementation(Dependencies.CORE_KTX)

    //ROOM
    kapt(Dependencies.ROOM_COMPILER)
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_KTX)
    implementation(Dependencies.MOSHI_CONVERTER)
    implementation(Dependencies.MOSHI_KTX)

    //UTILS
    implementation(Dependencies.LOCALIZATION)
    implementation(Dependencies.COMMONS)

    //GOOGLE
    implementation(platform(Dependencies.FIREBASE_BOM))
    implementation(Dependencies.FIREBASE_MESSAGE)

    implementation(Dependencies.GOOGLE_SERVICE_MAP)
    implementation(Dependencies.GOOGLE_SERVICE_LOCATION)
    implementation(Dependencies.GOOGLE_API_PHONE)
    implementation(Dependencies.WORKMANAGER)

    //LOGGER
    implementation(Dependencies.LOGGER_ERROR)
//    implementation(project(ModulesDependency.CORE))
//    implementation(project(ModulesDependency.UTILS))
}
