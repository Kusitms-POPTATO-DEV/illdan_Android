plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
}

android {
    namespace = "com.poptato.login"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.designSystem)

    // 카카오 로그인
    implementation(libs.kakao.auth)

    implementation(libs.firebase.messaging)
}