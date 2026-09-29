import org.gradle.api.provider.Property

/**
 * POM data of the Maven publication named "maven", see plantuml.publishing.gradle.kts.
 *
 * Coordinates are not configurable here: groupId, artifactId and version
 * are the project's own (`project.group`, `project.name`, `project.version`).
 */
abstract class PlantumlPublishingExtension {
	/** POM <name>. */
	abstract val pomName: Property<String>

	/** POM <description>. */
	abstract val pomDescription: Property<String>

	/** License name written in the published POM. */
	abstract val pomLicenseName: Property<String>

	/** License URL written in the published POM. */
	abstract val pomLicenseUrl: Property<String>

	internal val extraDevelopers = mutableListOf<Triple<String, String, String>>()

	/** Adds a developer to the POM, after the default one (Arnaud Roques). */
	fun developer(id: String, name: String, email: String) {
		extraDevelopers += Triple(id, name, email)
	}
}
