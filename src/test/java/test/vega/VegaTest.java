package test.vega;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.WriterConfig;
import net.sourceforge.plantuml.log.Logme;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class VegaTest {

	public static final Path VEGA_RESOURCES = Paths.get("src", "test", "resources", "vega");

	public static final List<JsonObject> results = new ArrayList<>();

	static {
		TitledDiagram.FORCE_SMETANA = true;
	}

	@BeforeEach
	void setUp() {
		Logme.MODE_VEGA = true;
	}

	@AfterEach
	void tearDown() {
		Logme.MODE_VEGA = false;
	}

	@AfterAll
	static void writeJsonReport() throws IOException {
		results.sort((a, b) -> a.getString("file", "").compareTo(b.getString("file", "")));

		final JsonObject report = new JsonObject();
		final JsonArray tests = new JsonArray();
		for (final JsonObject result : results)
			tests.add(result);
		report.add("tests", tests);

		final int total = results.size();
		final int passed = (int) results.stream().filter(r -> "pass".equals(r.getString("status", ""))).count();
		final int failed = (int) results.stream().filter(r -> "fail".equals(r.getString("status", ""))).count();
		final int skipped = (int) results.stream().filter(r -> "skipped".equals(r.getString("status", ""))).count();
		report.add("summary", new JsonObject().add("total", total).add("passed", passed).add("failed", failed)
				.add("skipped", skipped));

		final Path jsonFile = VEGA_RESOURCES.resolve("vega.json");
		try (Writer writer = Files.newBufferedWriter(jsonFile, UTF_8)) {
			report.writeTo(writer, WriterConfig.PRETTY_PRINT);
		}

		final List<String> failedFiles = results.stream()
											.filter(r -> "fail".equals(r.getString("status", "")))
											.map(r -> r.getString("file", ""))
											.collect(Collectors.toList());

		final List<String> skippedFiles = results.stream()
											.filter(r -> "skipped".equals(r.getString("status", "")))
											.map(r -> r.getString("file", ""))
											.collect(Collectors.toList());
	
		// Write a plain-text summary for Gradle console output
		final Path summaryFile = VEGA_RESOURCES.resolve("vega-summary.txt");
		Files.write(summaryFile, textSummary(total, passed, failed, skipped, failedFiles, skippedFiles).getBytes(UTF_8));

		// Write a Markdown summary (useful for CI artifacts / GitHub display)
		final Path mdSummaryFile = VEGA_RESOURCES.resolve("vega-summary.md");
		Files.write(mdSummaryFile, mdSummary(total, passed, failed, skipped, failedFiles, skippedFiles).getBytes(UTF_8));

	}

	private static String textSummary(int total, int passed, int failed, int skipped, List<String> failedFiles, List<String> skippedFiles) {
		final StringBuilder sb = new StringBuilder();
		sb.append("===========================================\n");
		sb.append(" VEGA TEST SUMMARY\n");
		sb.append("===========================================\n");
		sb.append(String.format(" Total:    %d%n", total));
		sb.append(String.format(" Passed:   %d%n", passed));
		sb.append(String.format(" Failed:   %d%n", failed));
		sb.append(String.format(" Skipped:  %d%n", skipped));
		sb.append("===========================================");
		if (failed > 0) {
			sb.append("\n Failed tests:");
			for (final String file : skippedFiles)
				sb.append("\n   - " + file);
			sb.append("\n===========================================");
		}
		if (skipped > 0) {
			sb.append("\n Skipped tests:");
			for (final String file : skippedFiles)
				sb.append("\n   - " + file);
			sb.append("\n===========================================");
		}
		sb.append("\n");
		return sb.toString();
	}

	private static String mdSummary(int total, int passed, int failed, int skipped, List<String> failedFiles,
			List<String> skippedFiles) {
		final StringBuilder md = new StringBuilder();
		md.append("## Vega Test Summary\n\n");
		md.append("| Metric | Icon | Count |\n");
		md.append("|---|:---:|---:|\n");
		md.append(String.format("| Total   | = | %d |%n", total));
		md.append(String.format("| Passed  | :white_check_mark: | %d |%n", passed));
		md.append(String.format("| Failed  | :x: | %d |%n", failed));
		md.append(String.format("| Skipped | :fast_forward: | %d |%n", skipped));

		if (failed > 0) {
			md.append("\n<details>\n<summary><h3>Failed tests</h3></summary>\n\n");
			for (final String file : failedFiles)
				md.append("- `").append(file).append("`\n");
			md.append("\n</details>");
		}
		if (skipped > 0) {
			md.append("\n<details>\n<summary><h3>Skipped tests</h3></summary>\n\n");
			for (final String file : skippedFiles)
				md.append("- `").append(file).append("`\n");
			md.append("\n</details>");
		}
		md.append("\n");
		return md.toString();
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
				return DynamicTest.dynamicTest(fileName, () -> VegaInputFile.parse(path).runSingleFile());
			}).collect(Collectors.toList());

			if (dirName.isEmpty())
				containers.addAll(tests);
			else
				containers.add(DynamicContainer.dynamicContainer(dirName, tests));

		}
		return containers;
	}

}
