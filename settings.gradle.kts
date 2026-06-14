pluginManagement {
  includeBuild("build-logic")

  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "storage"

sequenceOf(
  "core"
).forEach {
  include(":${rootProject.name}-$it")
  project(":${rootProject.name}-$it").projectDir = file(it)
}
