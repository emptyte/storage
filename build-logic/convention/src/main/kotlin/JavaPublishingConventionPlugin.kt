import java.nio.charset.StandardCharsets
import net.kyori.indra.IndraExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class JavaPublishingConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply(plugin = "net.kyori.indra.publishing")

        extensions.configure<IndraExtension> {
          val repoName = providers.gradleProperty("githubRepo").getOrElse("storage")
          github("emptyte", repoName) {
            ci(true)
          }

          mitLicense()
        }

        tasks.withType<JavaCompile>().configureEach {
          options.encoding = StandardCharsets.UTF_8.name()
          options.compilerArgs.addAll(listOf("-Xlint:all", "-parameters"))
        }

        tasks.withType<Javadoc>().configureEach {
          options.encoding = StandardCharsets.UTF_8.name()
        }
      }
    }
  }
}
