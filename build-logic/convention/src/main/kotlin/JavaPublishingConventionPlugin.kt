import net.kyori.indra.IndraExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class JavaPublishingConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "net.kyori.indra.publishing")

      extensions.configure<IndraExtension> {
        github("emptyte", "storage") {
          ci(true)
        }
        mitLicense()
      }
    }
  }
}
