rootProject.name = "actionbar-api"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

  repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  }
}
