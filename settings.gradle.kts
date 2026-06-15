pluginManagement {
  includeBuild("build-logic")

  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "storage"

sequenceOf(
  "bom",
  "core",
).forEach {
  include(":${rootProject.name}-$it")
  project(":${rootProject.name}-$it").projectDir = file(it)
}

sequenceOf(
  "caffeine",
  "gson"
).forEach {
  include(":${rootProject.name}-$it-provider")
  project(":${rootProject.name}-$it-provider").projectDir = file("providers/$it")
}
