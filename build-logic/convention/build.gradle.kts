plugins {
  `kotlin-dsl`
}

group = "team.emptyte.storage.buildlogic"

kotlin {
  jvmToolchain(21)
}

gradlePlugin {
  plugins {
    register("java-publishing") {
      id = libs.plugins.storage.java.publishing.get().pluginId
      implementationClass = "JavaPublishingConventionPlugin"
    }
    register("java-library") {
      id = libs.plugins.storage.java.library.get().pluginId
      implementationClass = "JavaLibraryConventionPlugin"
    }
  }
}

repositories {
  gradlePluginPortal()

  maven(url = "https://repo.stellardrift.ca/repository/internal/") {
    name = "stellardriftReleases"
    mavenContent { releasesOnly() }
  }
  maven(url = "https://repo.stellardrift.ca/repository/snapshots/") {
    name = "stellardriftSnapshots"
    mavenContent { snapshotsOnly() }
  }
}

dependencies {
  implementation(libs.bundles.indra)
}

tasks {
  validatePlugins {
    enableStricterValidation.set(true)
    failOnWarning.set(true)
  }
}
