plugins {
  alias(libs.plugins.storage.java.library)
}

dependencies {
  // Project dependencies
  api(project(":${rootProject.name}-core"))

  // Extra dependencies
  api(libs.gson)

  // Test dependencies
  testImplementation(platform("org.junit:junit-bom:6.0.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()

  testLogging {
    events("passed", "skipped", "failed")
  }
}
