import java.util.Properties

plugins {
    id("poptato.android.application")
    id("poptato.android.hilt")
    id("poptato.android.kotlin")
    id("poptato.retrofit")
    id("com.google.gms.google-services")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.poptato.app"

    defaultConfig {
        val kakaoAppKey = properties.getProperty("KAKAO_APP_KEY")
        buildConfigField("String", "KAKAO_APP_KEY", "\"${properties.getProperty("KAKAO_APP_KEY")}\"")
        manifestPlaceholders["KAKAO_HOST_SCHEME"] = "kakao$kakaoAppKey"
        versionCode = project.properties["version_code"]?.toString()?.toInt() ?: 1
        versionName = project.properties["version"]?.toString() ?: "1.0.0"

        val baseUrl = properties.getProperty("BASE_URL")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        val clarityId = properties.getProperty("CLARITY_ID")
        buildConfigField("String", "CLARITY_ID", "\"$clarityId\"")
    }
}

dependencies {
    implementation(projects.feature)
    implementation(projects.domain)
    implementation(projects.core)
    implementation(projects.data)
    implementation(projects.core.ui)

    implementation(libs.gson)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.urlconnection)

    // 카카오 로그인
    implementation(libs.kakao.auth)

    // ThreeTen
    implementation(libs.threeten)

    implementation(libs.hilt.core)

    // FCM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    // Clarity
    implementation(libs.clarity)
}