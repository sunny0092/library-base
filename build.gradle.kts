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
}

dependencies {
    //LAYOUT
    kapt(Dependencies.DATABINDING)
    implementation(Dependencies.CONSTRAINT)
    implementation(Dependencies.ACTIVITY)
    implementation(Dependencies.FRAGMENT)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.MATERIAL_DIALOG)
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
    implementation(Dependencies.GOOGLE_SERVICE_MAP)
    implementation(Dependencies.GOOGLE_SERVICE_LOCATION)
    implementation(Dependencies.GOOGLE_API_PHONE)

    //ROOM
    kapt(Dependencies.ROOM_COMPILER)
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_KTX)
    implementation(Dependencies.MOSHI_CONVERTER)
    implementation(Dependencies.MOSHI_KTX)

    //UTILS
    implementation(Dependencies.LOCALIZATION)
    implementation(Dependencies.COMMONS)

    //LOGGER
    implementation(Dependencies.LOGGER_ERROR)
}
