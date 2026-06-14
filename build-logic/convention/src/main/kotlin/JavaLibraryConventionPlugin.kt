import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import ext.libs
import java.util.Date
import net.kyori.indra.IndraExtension
import net.kyori.indra.crossdoc.CrossdocExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

class JavaLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "storage.java.publishing")

      apply(plugin = "net.kyori.indra")

      extensions.configure<IndraExtension> {
        javaVersions {
          target(21)
          minimumToolchain(21)
        }
      }

      repositories {
        mavenCentral()
      }
    }
  }
}
