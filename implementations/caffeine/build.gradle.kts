plugins {
  alias(libs.plugins.storage.java.library)
}

dependencies {
  // Project dependencies
  implementation(project(":${rootProject.name}-api"))

  // Extra dependencies
  implementation(libs.caffeine)
}
