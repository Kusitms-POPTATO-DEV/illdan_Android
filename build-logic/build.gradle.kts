plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.agp)
    implementation(libs.kotlin.gradleplugin)
    compileOnly(libs.compose.compiler.extension)
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = "poptato.android.application"
            implementationClass = "com.poptato.plugin.AndroidApplicationPlugin"
        }
        register("android-compose") {
            id = "poptato.android.compose"
            implementationClass = "com.poptato.plugin.AndroidComposePlugin"
        }
        register("android-feature") {
            id = "poptato.android.feature"
            implementationClass = "com.poptato.plugin.AndroidFeaturePlugin"
        }
        register("android-hilt") {
            id = "poptato.android.hilt"
            implementationClass = "com.poptato.plugin.AndroidHiltPlugin"
        }
        register("android-kotlin") {
            id = "poptato.android.kotlin"
            implementationClass = "com.poptato.plugin.AndroidKotlinPlugin"
        }
        register("kotlin-jvm") {
            id = "poptato.kotlin.jvm"
            implementationClass = "com.poptato.plugin.KotlinJvmPlugin"
        }
        register("kotlin-serialization") {
            id = "poptato.kotlin.serialization"
            implementationClass = "com.poptato.plugin.KotlinSerializationPlugin"
        }
        register("retrofit") {
            id = "poptato.retrofit"
            implementationClass = "com.poptato.plugin.RetrofitPlugin"
        }
    }
}