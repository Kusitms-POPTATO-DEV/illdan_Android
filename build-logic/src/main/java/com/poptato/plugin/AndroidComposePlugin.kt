package com.poptato.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

class AndroidComposePlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(plugins) {
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<BaseExtension>().apply {
            buildFeatures.apply {
                compose = true
            }
        }
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            featureFlags.set(setOf(ComposeFeatureFlag.StrongSkipping))
            includeSourceInformation.set(true)
        }
        dependencies {
            "implementation"(platform(libs.findLibrary("androidx.compose.bom").get()))
            "implementation"(libs.findBundle("compose").get())
            "implementation"(libs.findLibrary("coil-compose").get())
            "implementation"(libs.findLibrary("androidx.navigation.compose").get())
        }
    }
}