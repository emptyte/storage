plugins {
  alias(libs.plugins.storage.java.library)
}

dependencies {
  // Project dependencies
  api(project(":${rootProject.name}-core"))

  // Extra dependencies
  api(libs.caffeine)
}
