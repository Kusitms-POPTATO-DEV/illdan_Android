plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
    id("poptato.android.hilt")
    id("poptato.android.kotlin")
}

android {
    namespace = "com.poptato.feature"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core)
    implementation(projects.core.ui)
    implementation(projects.designSystem)
    implementation(projects.core.navigation)

    implementation(libs.firebase.messaging)
    implementation(libs.permissions)
}