import java.util.Properties

plugins {
    id("poptato.android.feature")
    id("poptato.android.compose")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.poptato.mypage"

    defaultConfig {
        buildConfigField(type = "String", name = "VERSION_NAME", value = "\"${project.version}\"")

        val noticeUrl = properties.getProperty("NOTICE_URL")
        buildConfigField("String", "NOTICE_URL", "\"$noticeUrl\"")

        val faqUrl = properties.getProperty("FAQ_URL")
        buildConfigField("String", "FAQ_URL", "\"$faqUrl\"")
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.ui)
    implementation(projects.designSystem)

    // 웹뷰
    implementation(libs.web.view)

    // pdf viewer
    implementation(libs.pdf.viewer)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}