plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
}

android {
    namespace = "com.poptato.backlog"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.designSystem)

    implementation(libs.lazycolumn.scrollbar)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    implementation(libs.firebase.messaging)
    implementation(libs.permissions)
}