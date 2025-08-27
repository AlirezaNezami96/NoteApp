pluginManagement {
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
    }
}

rootProject.name = "NoteApp"
include(":app")
include(":core")
include(":core:data")
include(":core:domain")
include(":core:designsystem")
include(":core:model")
include(":core:database")
include(":core:common")
include(":feature")
include(":feature:home")
include(":feature:note")
