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
      "api",
      "serialization",
      "caffeine",
      "gson"
    ).forEach {
      api(project(":${rootProject.name}-$it"))
    }
  }
}
