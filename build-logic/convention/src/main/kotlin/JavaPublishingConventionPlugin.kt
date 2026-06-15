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
          ci(false)
        }
        mitLicense()

        configurePublications {
          pom {
            name.set("storage")
            description.set("The groundbreaking library that breathes life into your data and models with consistent elegance. Effortlessly manage storage while implementing the powerful repository design pattern.")
            url.set("https://github.com/emptyte/storage")

            developers {
              developer {
                id.set("srvenient")
                name.set("Nelson Rodriguez Roa")
                url.set("https://github.com/srvenient")
                email.set("srvenient@gmail.com")
              }
            }
          }
        }
      }
    }
  }
}
