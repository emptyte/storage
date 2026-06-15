import net.kyori.indra.IndraExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories

class JavaLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "storage.java.publishing")

      apply(plugin = "net.kyori.indra")

      extensions.configure<IndraExtension> {
        javaVersions {
          target(21)
          minimumToolchain(21)
          strictVersions(true)
        }
      }

      repositories {
        mavenCentral()
      }
    }
  }
}
