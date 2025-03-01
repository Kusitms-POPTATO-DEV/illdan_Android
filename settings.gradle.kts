enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

rootProject.name = "Poptato"
include(":app")
include(":data")
include(":domain")
include(":feature")
include(":core")
include(":core:ui")
include(":core:navigation")
include(":design-system")
include(":feature:splash")
include(":feature:login")
include(":feature:backlog")
include(":feature:yesterdaylist")
include(":feature:today")
include(":feature:setting")
include(":feature:mypage")
include(":feature:history")
include(":feature:category")
include(":feature:onboarding")
