plugins {
  id("java-platform")
  alias(libs.plugins.storage.java.publishing)
}

indra {
  configurePublications {
    from(components["javaPlatform"])
  }
}

dependencies {
  constraints {
    sequenceOf(
      "core",
      "caffeine-provider",
      "gson-provider"
    ).forEach {
      api(project(":${rootProject.name}-$it"))
    }
  }
}
