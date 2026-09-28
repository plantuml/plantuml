import org.gradle.api.provider.Property

/**
 * Configuration of a PlantUML license variant.
 *
 * `id` drives the SJPP preprocessing: the define is `__<ID>__` (e.g. `__MIT__`)
 * and the header file is `<id>-license.txt` in the variant directory.
 */
abstract class LicenseVariantExtension {
	/** Short license id, e.g. "mit", "asl", "gplv2". */
	abstract val id: Property<String>

	/** License name written in the published POM. */
	abstract val pomLicenseName: Property<String>

	/** License URL written in the published POM. */
	abstract val pomLicenseUrl: Property<String>
}
