package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.TestAbortedException;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.WriterConfig;
import net.sourceforge.plantuml.yaml.parser.Monomorph;
import net.sourceforge.plantuml.yaml.parser.YamlParser;

class VegaTest {

	private static final String YAML_DELIMITER = "---";

	private static final Path VEGA_RESOURCES = Paths.get("src", "test", "resources", "vega");

	private static final List<JsonObject> results = new ArrayList<>();

	static {
		TitledDiagram.FORCE_SMETANA = true;
	}

	@AfterAll
	static void writeJsonReport() throws IOException {
		results.sort((a, b) -> a.getString("file", "").compareTo(b.getString("file", "")));

		final JsonObject report = new JsonObject();
		final JsonArray tests = new JsonArray();
		for (final JsonObject result : results)
			tests.add(result);
		report.add("tests", tests);

		final int passed = (int) results.stream().filter(r -> "pass".equals(r.getString("status", ""))).count();
		final int failed = (int) results.stream().filter(r -> "fail".equals(r.getString("status", ""))).count();
		final int skipped = (int) results.stream().filter(r -> "skipped".equals(r.getString("status", ""))).count();
		report.add("summary", new JsonObject()
				.add("total", results.size())
				.add("passed", passed)
				.add("failed", failed)
				.add("skipped", skipped));

		final Path jsonFile = VEGA_RESOURCES.resolve("vega.json");
		try (Writer writer = Files.newBufferedWriter(jsonFile, UTF_8)) {
			report.writeTo(writer, WriterConfig.PRETTY_PRINT);
		}

		// Write a plain-text summary for Gradle console output
		final Path summaryFile = VEGA_RESOURCES.resolve("vega-summary.txt");
		final StringBuilder sb = new StringBuilder();
		sb.append("===========================================\n");
		sb.append(" VEGA TEST SUMMARY\n");
		sb.append("===========================================\n");
		sb.append(String.format(" Total:    %d%n", results.size()));
		sb.append(String.format(" Passed:   %d%n", passed));
		sb.append(String.format(" Failed:   %d%n", failed));
		sb.append(String.format(" Skipped:  %d%n", skipped));
		sb.append("===========================================");
		if (failed > 0) {
			sb.append("\n Failed tests:");
			for (final JsonObject r : results)
				if ("fail".equals(r.getString("status", "")))
					sb.append("\n   - " + r.getString("file", ""));
			sb.append("\n===========================================");
		}
		if (skipped > 0) {
			sb.append("\n Skipped tests:");
			for (final JsonObject r : results)
				if ("skipped".equals(r.getString("status", "")))
					sb.append("\n   - " + r.getString("file", ""));
			sb.append("\n===========================================");
		}
		Files.write(summaryFile, sb.toString().getBytes(UTF_8));
	}

	@TestFactory
	List<DynamicNode> testAllPumlFiles() throws IOException {
		final List<Path> pumlFiles;
		try (Stream<Path> walk = Files.walk(VEGA_RESOURCES)) {
			pumlFiles = walk.filter(p -> p.toString().endsWith(".puml")).sorted().collect(Collectors.toList());
		}

		assertFalse(pumlFiles.isEmpty(), "No .puml files found in " + VEGA_RESOURCES);

		// Group .puml files by their parent directory
		final Map<String, List<Path>> byDirectory = new LinkedHashMap<>();
		for (final Path path : pumlFiles) {
			final Path relative = VEGA_RESOURCES.relativize(path);
			final String dirName = relative.getParent() == null ? "" : relative.getParent().toString();
			byDirectory.computeIfAbsent(dirName, k -> new ArrayList<>()).add(path);
		}

		final List<DynamicNode> containers = new ArrayList<>();
		for (final Map.Entry<String, List<Path>> entry : byDirectory.entrySet()) {
			final String dirName = entry.getKey();
			final List<DynamicTest> tests = entry.getValue().stream().map(path -> {
				final String fileName = path.getFileName().toString();
				return DynamicTest.dynamicTest(fileName, () -> runSingleFile(path));
			}).collect(Collectors.toList());

			if (dirName.isEmpty()) {
				containers.addAll(tests);
			} else {
				containers.add(DynamicContainer.dynamicContainer(dirName, tests));
			}
		}
		return containers;
	}

	private void runSingleFile(Path path) throws IOException {
		final VegaTestData data = parse(path);
		final boolean allowFailure = "true".equals(data.getYamlString("allow-failure"));
		final String relativePath = VEGA_RESOURCES.relativize(path).toString();
		final long startTime = System.currentTimeMillis();
		final String[] diagramClass = { null };

		try {
			diagramClass[0] = doRunSingleFile(path, data);
			recordResult(relativePath, "pass", System.currentTimeMillis() - startTime, null, diagramClass[0]);
		} catch (TestAbortedException e) {
			recordResult(relativePath, "skipped", System.currentTimeMillis() - startTime, e.getMessage(), diagramClass[0]);
			throw e;
		} catch (AssertionError | RuntimeException e) {
			if (allowFailure) {
				recordResult(relativePath, "skipped", System.currentTimeMillis() - startTime, e.getMessage(), diagramClass[0]);
				assumeTrue(false, "Known failure (allow-failure: true): " + path + " - " + e.getMessage());
			} else {
				recordResult(relativePath, "fail", System.currentTimeMillis() - startTime, e.getMessage(), diagramClass[0]);
				throw e;
			}
		}
	}

	private static synchronized void recordResult(String path, String status, long durationMs, String message,
			String diagramClass) {
		final JsonObject entry = new JsonObject()
				.add("file", path)
				.add("status", status)
				.add("duration_ms", durationMs);
		if (diagramClass != null)
			entry.add("diagram_class", diagramClass);
		if (message != null)
			entry.add("message", message);
		results.add(entry);
	}

	private String doRunSingleFile(Path path, VegaTestData data) throws IOException {
		assertNotNull(data.getYaml(), "YAML header in " + path);
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

				final String suffix = nbImages == 1 ? "" : "-" + (imageIndex + 1);

				if (fileFormat == FileFormat.DEBUG) {
					checkDebugOutput(path, data, baos, suffix, nbImages, imageIndex, generatedFiles);
				} else if (fileFormat == FileFormat.SVG) {
					checkSvgOutput(path, baos, suffix, generatedFiles);
				} else if (fileFormat == FileFormat.SCXML) {
					checkScxmlOutput(path, baos, suffix, generatedFiles);
				} else if (fileFormat.name().startsWith("XMI")) {
					checkXmiOutput(path, baos, suffix, generatedFiles);
				} else if (fileFormat == FileFormat.PREPROC) {
					checkPreprocOutput(path, baos, suffix, generatedFiles);
				}
			}
		}

		if (generatedFiles.isEmpty() == false) {
			assumeTrue(false, "Expected files created: " + generatedFiles + " - please review and re-run");
		}
		return diagramClass;
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
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(pumlPath, suffix, ".txt");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(expectedOutput, actualOutput, "DEBUG output mismatch for " + pumlPath);

		checkDebugContains(pumlPath, data, actualOutput, nbImages, imageIndex);
	}

	/**
	 * Checks {@code expected-debug} (single image) or
	 * {@code expected-debug-N} (multi-image) sub-keys {@code contains}
	 * and {@code not-contains} against the actual DEBUG output.
	 */
	private void checkDebugContains(Path pumlPath, VegaTestData data, String actualOutput, int nbImages,
			int imageIndex) {
		// "expected-debug" for single image, "expected-debug-N" for multi-image
		final String yamlKey = nbImages == 1 ? "expected-debug" : "expected-debug-" + (imageIndex + 1);
		final String label = pumlPath + " [image " + (imageIndex + 1) + "]";

		for (final String needle : data.getYamlSubList(yamlKey, "contains"))
			assertTrue(actualOutput.contains(needle),
					"DEBUG output should contain '" + needle + "' for " + label);

		for (final String needle : data.getYamlSubList(yamlKey, "not-contains"))
			assertFalse(actualOutput.contains(needle),
					"DEBUG output should not contain '" + needle + "' for " + label);
	}

	// ----------------------------------------------------------
	// PREPROC output: compare with .preproc reference file
	// ----------------------------------------------------------

	private void checkPreprocOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix,
			List<Path> generatedFiles) throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(pumlPath, suffix, ".preproc");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(expectedOutput, actualOutput, "PREPROC output mismatch for " + pumlPath);
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
	// SCXML output: compare with .scxml reference file
	// ----------------------------------------------------------

	private void checkScxmlOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix,
			List<Path> generatedFiles) throws IOException {
		final String actualOutput = normalizeLineEndings(new String(baos.toByteArray(), UTF_8));
		final Path expectedFile = getExpectedFile(pumlPath, suffix, ".scxml");

		if (Files.exists(expectedFile) == false) {
			Files.write(expectedFile, actualOutput.getBytes(UTF_8));
			generatedFiles.add(expectedFile);
			return;
		}

		final String expectedOutput = normalizeLineEndings(new String(Files.readAllBytes(expectedFile), UTF_8));
		assertEquals(expectedOutput, actualOutput, "SCXML output mismatch for " + pumlPath);
	}

	// ----------------------------------------------------------
	// XMI output: compare with .xmi reference file
	// ----------------------------------------------------------

	private void checkXmiOutput(Path pumlPath, ByteArrayOutputStream baos, String suffix,
			List<Path> generatedFiles) throws IOException {
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
	 * version-dependent content (like {@code <XMI.exporterVersion>}) does
	 * not cause spurious comparison failures.
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

	// ----------------------------------------------------------
	// Parsing: split .puml file into YAML header + PlantUML body
	// ----------------------------------------------------------

	static VegaTestData parse(Path path) throws IOException {
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

			if (insideYaml) {
				yamlLines.add(line);
			} else if (yamlDone) {
				pumlLines.add(line);
			} else {
				pumlLines.add(line);
			}
		}

		final Monomorph yaml;
		if (yamlLines.isEmpty()) {
			yaml = new Monomorph();
		} else {
			yaml = new YamlParser().parse(yamlLines);
		}

		return new VegaTestData(path, yaml, pumlLines);
	}

}
