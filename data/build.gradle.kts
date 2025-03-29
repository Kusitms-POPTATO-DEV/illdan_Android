plugins {
    id("poptato.android.feature")
    id("poptato.retrofit")
    id("poptato.android.hilt")
}

android {
    namespace = "com.poptato.data"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core)

    implementation(libs.androidx.datastore)

    implementation(libs.gson)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.urlconnection)
}