plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
}

android {
    namespace = "com.poptato.setting"

    defaultConfig {
        buildConfigField(type = "String", name = "VERSION_NAME", value = "\"${project.version}\"")
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.designSystem)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}