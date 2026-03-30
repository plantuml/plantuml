package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
public class VegaTestData {

	private final Path path;
	private final Monomorph yaml;
	private final List<String> pumlSource;

	public VegaTestData(Path path, Monomorph yaml, List<String> pumlSource) {
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
	private List<String> getYamlSubList(String key, String subKey) {
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
			return new ArrayList<>();

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
		final VegaTestData data = this;
		if (data.getYaml().getType() == MonomorphType.UNDETERMINATE) {
			final Monomorph yaml = data.getYaml();
			yaml.putInMap("allow-failure", Monomorph.scalar("true"));
			yaml.putInMap("output", Monomorph.scalar("svg"));
		}

		final boolean allowFailure = "true".equals(data.getYamlString("allow-failure"));
		final String relativePath = VegaTest.VEGA_RESOURCES.relativize(path).toString();
		final long startTime = System.currentTimeMillis();
		final String[] diagramClass = { null };

		try {
			diagramClass[0] = doRunSingleFile(path, data);
			recordResult(relativePath, "pass", System.currentTimeMillis() - startTime, null, diagramClass[0]);
		} catch (TestAbortedException e) {
			recordResult(relativePath, "skipped", System.currentTimeMillis() - startTime, e.getMessage(),
					diagramClass[0]);
			throw e;
		} catch (AssertionError | RuntimeException e) {
			e.printStackTrace();
			if (allowFailure) {
				recordResult(relativePath, "skipped", System.currentTimeMillis() - startTime, e.getMessage(),
						diagramClass[0]);
				assumeTrue(false, "Known failure (allow-failure: true): " + path + " - " + e.getMessage());
			} else {
				recordResult(relativePath, "fail", System.currentTimeMillis() - startTime, e.getMessage(),
						diagramClass[0]);
				throw e;
			}
		}
	}

	private static synchronized void recordResult(String path, String status, long durationMs, String message,
			String diagramClass) {
		final JsonObject entry = new JsonObject().add("file", path).add("status", status).add("duration_ms",
				durationMs);
		if (diagramClass != null)
			entry.add("diagram_class", diagramClass);
		if (message != null)
			entry.add("message", message);
		VegaTest.results.add(entry);
	}

	private String doRunSingleFile(Path path, VegaTestData data) throws IOException {
		assertFalse(data.getPumlSource().isEmpty(), "PlantUML source in " + path);

		final String source = data.getPumlSourceAsString();
		final SourceStringReader ssr = new SourceStringReader(source, UTF_8);
		final Diagram diagram = ssr.getBlocks().get(0).getDiagram();
		final String diagramClass = diagram.getClass().getSimpleName();

		// Check expectations on the parsed diagram
		checkErrorExpectations(path, data, ssr);
		checkDescriptionExpectation(path, data, ssr);
		checkImageCountExpectation(path, data, ssr);

		// Render in each requested output format (if any)
		final List<FileFormat> fileFormats = data.getFileFormats();
		if (fileFormats.isEmpty())
			return diagramClass;

		final int nbImages = ssr.getBlocks().get(0).getDiagram().getNbImages();
		final List<Path> generatedFiles = new ArrayList<>();

		final String expectedException = data.getYamlString("expected-exception");

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
					if (status != 0) {
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

				if (expectedException == null) {
					final String suffix = nbImages == 1 ? "" : "-" + (imageIndex + 1);

					if (fileFormat == FileFormat.DEBUG)
						checkDebugOutput(path, data, baos, suffix, nbImages, imageIndex, generatedFiles);
					else if (fileFormat == FileFormat.LATEX_FIXED)
						checkTextOutput(path, baos, suffix, ".tex", "LATEX", generatedFiles);
					else if (fileFormat == FileFormat.SVG_FIXED)
						checkSvgOutput(path, baos, suffix, generatedFiles);
					else if (fileFormat == FileFormat.SCXML)
						checkTextOutput(path, baos, suffix, ".scxml", "SCXML", generatedFiles);
					else if (fileFormat == FileFormat.GRAPHML)
						checkTextOutput(path, baos, suffix, ".graphml", "GRAPHML", generatedFiles);
					else if (fileFormat.name().startsWith("XMI"))
						checkXmiOutput(path, baos, suffix, generatedFiles);
					else if (fileFormat == FileFormat.PREPROC)
						checkTextOutput(path, baos, suffix, ".preproc", "PREPROC", generatedFiles);

				}
			}
		}

		if (expectedException != null && generatedFiles.isEmpty() == false)
			assumeTrue(false, "Expected files created: " + generatedFiles + " - please review and re-run");

		return diagramClass;
	}

	// ----------------------------------------------------------
	// Generic text output checking
	// ----------------------------------------------------------

	/**
	 * Compares the actual output (from {@code baos}) against a reference file.
	 * If the reference file does not exist yet, it is created and the test is
	 * recorded as "generated".
	 *
	 * @param pumlPath       the {@code .puml} test file
	 * @param baos           the rendered output
	 * @param suffix         image-index suffix ({@code ""} or {@code "-1"}, …)
	 * @param extension      the reference-file extension (e.g. {@code ".txt"})
	 * @param label          a human-readable format label for assertion messages
	 * @param generatedFiles collects newly created reference files
	 */
	private void checkTextOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix, String extension,
			String label, List<Path> generatedFiles) throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(pumlPath, suffix, extension);

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(expectedOutput, actualOutput, label + " output mismatch for " + pumlPath);
	}

	// ----------------------------------------------------------
	// Description checking
	// ----------------------------------------------------------

	private void checkDescriptionExpectation(Path path, VegaTestData data, SourceStringReader ssr) {
		final String expectedDescription = data.getYamlString("expected-description");
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

	private void checkImageCountExpectation(Path path, VegaTestData data, SourceStringReader ssr) {
		String expectedImageCount = data.getYamlString("expected-image-count");
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

	private void checkErrorExpectations(Path path, VegaTestData data, SourceStringReader ssr) {
		final String expectedErrorLine = data.getYamlString("expected-error-line");
		final String expectedErrorMessage = data.getYamlString("expected-error-message");

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

	// ----------------------------------------------------------
	// DEBUG output: compare with .txt reference file
	// ----------------------------------------------------------

	private void checkDebugOutput(Path pumlPath, VegaTestData data, ByteArrayOutputStream baos, String suffix,
			int nbImages, int imageIndex, List<Path> generatedFiles) throws IOException {
		checkTextOutput(pumlPath, baos, suffix, ".txt", "DEBUG", generatedFiles);

		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		checkDebugContains(pumlPath, data, actualOutput, nbImages, imageIndex);
	}

	/**
	 * Checks {@code expected-debug} (single image) or {@code expected-debug-N}
	 * (multi-image) sub-keys {@code contains} and {@code not-contains} against the
	 * actual DEBUG output.
	 */
	private void checkDebugContains(Path pumlPath, VegaTestData data, String actualOutput, int nbImages,
			int imageIndex) {
		// "expected-debug" for single image, "expected-debug-N" for multi-image
		final String yamlKey = nbImages == 1 ? "expected-debug" : "expected-debug-" + (imageIndex + 1);
		final String label = pumlPath + " [image " + (imageIndex + 1) + "]";

		for (final String needle : data.getYamlSubList(yamlKey, "contains"))
			assertTrue(actualOutput.contains(needle), "DEBUG output should contain '" + needle + "' for " + label);

		for (final String needle : data.getYamlSubList(yamlKey, "not-contains"))
			assertFalse(actualOutput.contains(needle), "DEBUG output should not contain '" + needle + "' for " + label);
	}


	// ----------------------------------------------------------
	// SVG output: compare cleaned SVG with .svg reference file
	// ----------------------------------------------------------

	private void checkSvgOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix, List<Path> generatedFiles)
			throws IOException {
		final String rawSvg = new String(baos.toByteArray(), UTF_8);
		final String cleanedSvg = SvgCleaner.clean(rawSvg);
		final Path expectedFile = getExpectedFile(pumlPath, suffix, ".svg");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, cleanedSvg.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedSvg = new String(Files.readAllBytes(expectedFile), UTF_8);
		assertEquals(SvgCleaner.normalise(expectedSvg), SvgCleaner.normalise(cleanedSvg),
				"SVG output mismatch for " + pumlPath);
	}


	// ----------------------------------------------------------
	// XMI output: compare with .xmi reference file
	// ----------------------------------------------------------

	private void checkXmiOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix, List<Path> generatedFiles)
			throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(pumlPath, suffix, ".xmi");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(cleanXmi(expectedOutput), cleanXmi(actualOutput), "XMI output mismatch for " + pumlPath);
	}

	/**
	 * Strips the {@code <XMI.documentation>} block from XMI output so that
	 * version-dependent content (like {@code <XMI.exporterVersion>}) does not cause
	 * spurious comparison failures.
	 */
	private static String cleanXmi(String xmi) {
		return xmi.replaceAll("(?s)<XMI\\.documentation>.*?</XMI\\.documentation>", "");
	}

	/**
	 * Normalizes line endings to {@code \n} so that comparisons are not affected by
	 * platform differences or git autocrlf settings.
	 */
	private static String normalizeLineEndings(String s) {
		return s.replace("\r\n", "\n").replace("\r", "\n");
	}

	/**
	 * Returns the expected-output file path by replacing the {@code .puml}
	 * extension with the given suffix and extension.
	 *
	 * <p>
	 * For a single image, suffix is {@code ""} so {@code hello.puml} becomes
	 * {@code hello.txt}. For multiple images, suffix is {@code "-1"}, {@code "-2"},
	 * etc. so {@code newpage.puml} becomes {@code newpage-1.txt},
	 * {@code newpage-2.txt}.
	 */
	private static Path getExpectedFile(Path pumlPath, String suffix, String extension) {
		final String name = pumlPath.getFileName().toString();
		final String baseName = name.substring(0, name.lastIndexOf('.'));
		return pumlPath.resolveSibling(baseName + suffix + extension);
	}

}
