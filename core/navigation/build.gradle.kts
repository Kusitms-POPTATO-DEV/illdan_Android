plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
}

android {
    namespace = "com.poptato.navigation"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.feature.splash)
    implementation(projects.feature.login)
    implementation(projects.feature.backlog)
    implementation(projects.feature.yesterdaylist)
    implementation(projects.feature.setting)
    implementation(projects.feature.mypage)
    implementation(projects.feature.today)
    implementation(projects.feature.history)
    implementation(projects.feature.category)
    implementation(projects.feature.onboarding)
}
