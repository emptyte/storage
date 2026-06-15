import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import ext.libs
import net.kyori.indra.IndraExtension
import net.kyori.indra.crossdoc.CrossdocExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import java.util.*

class JavaLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "storage.java.publishing")

      apply(plugin = "net.kyori.indra")
      apply(plugin = "net.kyori.indra.crossdoc")
      apply(plugin = "net.kyori.indra.licenser.spotless")

      extensions.configure<IndraExtension> {
        javaVersions {
          target(21)
          minimumToolchain(21)
          strictVersions(true)
        }
      }

      extensions.configure<SpotlessExtension> {
        fun FormatExtension.applyCommon() {
          trimTrailingWhitespace()
          endWithNewline()
          leadingTabsToSpaces(2)
        }
        java {
          importOrderFile(rootProject.file("gradle/spotless.importorder.txt"))
          applyCommon()
        }
        kotlinGradle {
          applyCommon()
        }
      }

      extensions.configure<CrossdocExtension> {
        baseUrl().set(providers.gradleProperty("javadocPublishRoot"))
        nameBasedDocumentationUrlProvider {
          projectNamePrefix.set("storage-")
        }
      }

      extensions.configure<JavaPluginExtension> {
        withJavadocJar()
      }

      tasks.named<Jar>("jar") {
        manifest {
          attributes(
            "Specification-Version" to project.version,
            "Specification-Vendor" to "emptyte-team",
            "Implementation-Build-Date" to Date()
          )
        }
      }

      tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
      }

      tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
        dependsOn("spotlessApply")
      }

      repositories {
        mavenCentral()
      }

      dependencies {
        "compileOnly"(libs.findLibrary("jetbrains.annotations").get())
      }
    }
  }
}
