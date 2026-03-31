package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.opentest4j.TestAbortedException;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.yaml.parser.Monomorph;
import net.sourceforge.plantuml.yaml.parser.MonomorphType;
import net.sourceforge.plantuml.yaml.parser.YamlParser;

/**
 * Couples the YAML header (parsed as a Monomorph) with the PlantUML source
 * extracted from a single {@code .puml} test file.
 *
 * <p>
 * The expected file format is:
 * 
 * <pre>
 * ----
 * output: svg
 * ----
 * &#64;startuml
 * ...
 * @enduml
 * </pre>
 *
 * <p>
 * The YAML block between the two {@code ----} delimiters holds expected values
 * for assertions. The {@code output} key controls which {@link FileFormat} is
 * used for generation.
 */
public class VegaInputFile {

	private static final String YAML_DELIMITER = "---";

	private final static Map<FileFormat, VegaChecker> CHECKERS = new EnumMap<>(FileFormat.class);

	static {
		CHECKERS.put(FileFormat.DEBUG, new VegaCheckerDebug());
		CHECKERS.put(FileFormat.LATEX_FIXED, new VegaCheckerLatex());
		CHECKERS.put(FileFormat.SVG_FIXED, new VegaCheckerSvg());
		CHECKERS.put(FileFormat.SCXML, new VegaCheckerScxml());
		CHECKERS.put(FileFormat.GRAPHML, new VegaCheckerGraphml());
		CHECKERS.put(FileFormat.PREPROC, new VegaCheckerPreproc());
		CHECKERS.put(FileFormat.XMI_STANDARD, new VegaCheckerXmi());
		CHECKERS.put(FileFormat.XMI_STAR, new VegaCheckerXmi());
		CHECKERS.put(FileFormat.XMI_ARGO, new VegaCheckerXmi());
		CHECKERS.put(FileFormat.XMI_CUSTOM, new VegaCheckerXmi());
		CHECKERS.put(FileFormat.XMI_SCRIPT, new VegaCheckerXmi());
	}

	private final Path path;
	private final Monomorph yaml;
	private final List<String> pumlSource;

	static public VegaInputFile parse(Path path) throws IOException {
		final List<String> allLines = Files.readAllLines(path);

		final List<String> yamlLines = new ArrayList<>();
		final List<String> pumlLines = new ArrayList<>();

		boolean insideYaml = false;
		boolean yamlDone = false;

		for (final String line : allLines) {
			if (yamlDone == false && line.trim().equals(YAML_DELIMITER)) {
				if (insideYaml == false) {
					insideYaml = true;
					continue;
				} else {
					insideYaml = false;
					yamlDone = true;
					continue;
				}
			}

			if (insideYaml)
				yamlLines.add(line);
			else if (yamlDone)
				pumlLines.add(line);
			else
				pumlLines.add(line);

		}

		final Monomorph yaml;
		if (yamlLines.isEmpty()) {
			yaml = new Monomorph();
			yaml.putInMap("allow-failure", Monomorph.scalar("true"));
			yaml.putInMap("output", Monomorph.scalar("svg"));
			yaml.putInMap("tag", Monomorph.scalar("specification"));
		} else {
			yaml = new YamlParser().parse(yamlLines);
		}
		return new VegaInputFile(path, yaml, pumlLines);
	}

	private VegaInputFile(Path path, Monomorph yaml, List<String> pumlSource) {
		this.path = path;
		this.yaml = yaml;
		this.pumlSource = pumlSource;
	}

	public Path getPath() {
		return path;
	}

	public Monomorph getYaml() {
		return yaml;
	}

	public List<String> getPumlSource() {
		return pumlSource;
	}

	/**
	 * Returns the full PlantUML source as a single string (lines joined with
	 * newlines).
	 */
	public String getPumlSourceAsString() {
		return String.join("\n", pumlSource);
	}

	/**
	 * Returns the scalar value for the given key in the YAML header, or
	 * {@code null} if the key is absent or the header is not a map.
	 */
	public String getYamlString(String key) {
		if (yaml.getType() != MonomorphType.MAP)
			return null;

		final Monomorph value = yaml.getMapValue(key);
		if (value == null || value.getType() != MonomorphType.SCALAR)
			return null;

		return value.getValue();
	}

	public List<String> getYamlList(String key) {
		if (yaml.getType() != MonomorphType.MAP)
			return new ArrayList<>();

		final Monomorph value = yaml.getMapValue(key);
		return monomorphToList(value);
	}

	/**
	 * Returns the values for a sub-key inside a YAML map entry. For example, with:
	 * 
	 * <pre>
	 * expected-debug:
	 *   contains:
	 *     - Alice
	 *     - Bob
	 *   not-contains: ERROR
	 * </pre>
	 * 
	 * calling {@code getYamlSubList("expected-debug", "contains")} returns
	 * {@code ["Alice", "Bob"]}.
	 */
	protected List<String> getYamlSubList(String key, String subKey) {
		if (yaml.getType() != MonomorphType.MAP)
			return new ArrayList<>();

		final Monomorph mapValue = yaml.getMapValue(key);
		if (mapValue == null || mapValue.getType() != MonomorphType.MAP)
			return new ArrayList<>();

		final Monomorph subValue = mapValue.getMapValue(subKey);
		return monomorphToList(subValue);
	}

	private static List<String> monomorphToList(Monomorph value) {
		if (value == null)
			return new ArrayList<>();

		if (value.getType() == MonomorphType.SCALAR) {
			final List<String> result = new ArrayList<>();
			result.add(value.getValue());
			return result;
		}

		if (value.getType() == MonomorphType.LIST) {
			final List<String> result = new ArrayList<>();
			for (int i = 0; i < value.size(); i++)
				result.add(value.getElementAt(i).getValue());
			return result;
		}

		return new ArrayList<>();
	}

	/**
	 * Returns all {@link FileFormat}s specified by the {@code output} key in the
	 * YAML header. The value can be a single format (e.g. {@code svg}) or a
	 * comma-separated list (e.g. {@code svg, debug}).
	 *
	 * @return a non-empty list, or an empty list if {@code output} is absent.
	 */
	private List<FileFormat> getFileFormats() {
		final String value = getYamlString("output");
		if (value == null)
			return Collections.singletonList(FileFormat.SVG_FIXED);

		final List<FileFormat> result = new ArrayList<>();
		for (final String token : value.split(",")) {
			final String trimmed = token.trim().toUpperCase();
			if (trimmed.equals("SVG"))
				result.add(FileFormat.SVG_FIXED);
			else if (trimmed.equals("LATEX"))
				result.add(FileFormat.LATEX_FIXED);
			else
				result.add(FileFormat.valueOf(trimmed));
		}
		return result;
	}

	@Override
	public String toString() {
		return path.getFileName().toString();
	}

	public void runSingleFile() throws IOException {
		final boolean allowFailure = "true".equals(this.getYamlString("allow-failure"));
		final long startTime = System.currentTimeMillis();
		Class<?> diagramClass = null;

		try {
			diagramClass = doRunSingleFile();
			recordResult(VegaStatus.PASS, System.currentTimeMillis() - startTime, diagramClass, null);
		} catch (TestAbortedException e) {
			recordResult(VegaStatus.SKIPPED, System.currentTimeMillis() - startTime, diagramClass, e);
			throw e;
		} catch (Throwable e) {
			if (allowFailure) {
				recordResult(VegaStatus.SKIPPED, System.currentTimeMillis() - startTime, diagramClass, e);
				assumeTrue(false, "Known failure (allow-failure: true): " + path + " - " + e.getMessage());
			} else {
				recordResult(VegaStatus.FAIL, System.currentTimeMillis() - startTime, diagramClass, e);
				throw e;
			}
		}
	}

	private void recordResult(VegaStatus status, long durationMs, Class<?> diagramClass, Throwable e) {
		final String relativePath = VegaTest.VEGA_RESOURCES.relativize(path).toString();
		final String message = e == null ? null : e.getMessage();

		final JsonObject json = new VegaResult(relativePath, status, durationMs, message, diagramClass, e)
				.toJsonObject();

		synchronized (VegaTest.results) {
			VegaTest.results.add(json);
		}
	}

	private Class<?> doRunSingleFile() throws IOException {
		assertFalse(getPumlSource().isEmpty(), "PlantUML source in " + path);

		final String source = getPumlSourceAsString();
		final SourceStringReader ssr = new SourceStringReader(source, UTF_8);
		final Diagram diagram = ssr.getBlocks().get(0).getDiagram();
		final Class<?> diagramClass = diagram.getClass();

		// Check expectations on the parsed diagram
		checkErrorExpectations(ssr);
		checkDescriptionExpectation(ssr);
		checkImageCountExpectation(ssr);

		// Render in each requested output format (if any)
		final List<FileFormat> fileFormats = getFileFormats();
		if (fileFormats.isEmpty())
			return diagramClass;

		final int nbImages = ssr.getBlocks().get(0).getDiagram().getNbImages();
		final List<Path> generatedFiles = new ArrayList<>();

		final String expectedException = getYamlString("expected-exception");

		for (final FileFormat fileFormat : fileFormats) {
			for (int imageIndex = 0; imageIndex < nbImages; imageIndex++) {
				final SourceStringReader ssrForFormat = new SourceStringReader(source, UTF_8);
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final DiagramDescription description = ssrForFormat.outputImage(baos, imageIndex,
						new FileFormatOption(fileFormat));

				assertNotNull(description,
						"No diagram generated for " + path + " [" + fileFormat + " image " + (imageIndex + 1) + "]");
				assertFalse(baos.size() == 0,
						"Empty output for " + path + " [" + fileFormat + " image " + (imageIndex + 1) + "]");

				if (fileFormat != FileFormat.PREPROC) {
					final ImageData imageData = description.getImageData();
					assertNotNull(imageData);
					final int status = imageData.getStatus();
					if (status != 0 && imageData.getRootCause() != null) {
						assertNotNull(expectedException, "Rendering failed with status " + status
								+ " but no expected-exception declared in " + path);
						final String exception = imageData.getRootCause().getClass().getSimpleName();
						assertEquals(expectedException, exception, "Exception mismatch for " + path + " [" + fileFormat
								+ " image " + (imageIndex + 1) + "]");
					} else if (expectedException != null) {
						throw new AssertionError(
								"Expected exception " + expectedException + " but rendering succeeded for " + path);
					}
				}

				if (expectedException == null && isExpectedError() == false) {
					final String suffix = nbImages == 1 ? "" : "-" + (imageIndex + 1);
					final Path newFile = CHECKERS.get(fileFormat).checkOutput(this, baos, suffix, nbImages, imageIndex);
					if (newFile != null)
						generatedFiles.add(newFile);

				}
			}
		}

		if (expectedException != null && generatedFiles.isEmpty() == false)
			assumeTrue(false, "Expected files created: " + generatedFiles + " - please review and re-run");

		return diagramClass;
	}

	// ----------------------------------------------------------
	// Description checking
	// ----------------------------------------------------------

	private void checkDescriptionExpectation(SourceStringReader ssr) {
		final String expectedDescription = getYamlString("expected-description");
		if (expectedDescription == null)
			return;

		final List<BlockUml> blocks = ssr.getBlocks();
		assertFalse(blocks.isEmpty(), "No blocks found in " + path);

		final Diagram diagram = blocks.get(0).getDiagram();
		assertEquals(expectedDescription, diagram.getDescription().getDescription(), "Bad description for " + path);
	}

	// ----------------------------------------------------------
	// Image count checking
	// ----------------------------------------------------------

	private void checkImageCountExpectation(SourceStringReader ssr) {
		String expectedImageCount = getYamlString("expected-image-count");
		if (expectedImageCount == null)
			expectedImageCount = "1";

		final List<BlockUml> blocks = ssr.getBlocks();
		assertFalse(blocks.isEmpty(), "No blocks found in " + path);

		final Diagram diagram = blocks.get(0).getDiagram();
		assertEquals(Integer.parseInt(expectedImageCount), diagram.getNbImages(), "Image count mismatch for " + path);
	}

	// ----------------------------------------------------------
	// Error checking: verify PSystemError line and message
	// ----------------------------------------------------------

	private boolean isExpectedError() {
		final String expectedErrorLine = getYamlString("expected-error-line");
		final String expectedErrorMessage = getYamlString("expected-error-message");
		if (expectedErrorLine == null && expectedErrorMessage == null)
			return false;
		return true;
	}

	private void checkErrorExpectations(SourceStringReader ssr) {
		final String expectedErrorLine = getYamlString("expected-error-line");
		final String expectedErrorMessage = getYamlString("expected-error-message");

		if (expectedErrorLine == null && expectedErrorMessage == null)
			return;

		final List<BlockUml> blocks = ssr.getBlocks();
		assertFalse(blocks.isEmpty(), "No blocks found in " + path);

		final Diagram diagram = blocks.get(0).getDiagram();
		if (diagram instanceof PSystemError == false) {
			throw new AssertionError(
					"Expected a PSystemError but got " + diagram.getClass().getSimpleName() + " for " + path);
		}

		final PSystemError error = (PSystemError) diagram;
		final ErrorUml firstError = error.getFirstError();
		assertNotNull(firstError, "No error found in PSystemError for " + path);

		if (expectedErrorLine != null) {
			assertEquals(Integer.parseInt(expectedErrorLine), firstError.getPosition(),
					"Error line mismatch for " + path);
		}

		if (expectedErrorMessage != null) {
			assertEquals(expectedErrorMessage, firstError.getError(), "Error message mismatch for " + path);
		}
	}

}
