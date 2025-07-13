package com.poptato.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(plugins) {
            apply("com.google.devtools.ksp")
            apply("com.google.dagger.hilt.android")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            "implementation"(libs.findLibrary("hilt.android").get())
            "ksp"(libs.findLibrary("hilt.compiler").get())
            "kspTest"(libs.findLibrary("hilt.testing.compiler").get())
        }
    }
}