pluginManagement {
    repositories {
        google()
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

rootProject.name = "AStep"
include(":app")
include(":core:network")
include(":core:database")
include(":feature:gear")
include(":feature:planner")
include(":feature:explore")
include(":feature:map")
include(":feature:profile")
include(":data:explore")
include(":data:planner")
include(":data:gear")
include(":data:map")
include(":data:profile")
include(":core:notification")
include(":data:mylocation")
include(":core:permission")

include(":core:entity")
