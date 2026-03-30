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
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.WriterConfig;
import net.sourceforge.plantuml.yaml.parser.Monomorph;
import net.sourceforge.plantuml.yaml.parser.YamlParser;

class VegaTest {

	private static final String YAML_DELIMITER = "---";

	public static final Path VEGA_RESOURCES = Paths.get("src", "test", "resources", "vega");

	public static final List<JsonObject> results = new ArrayList<>();

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
		report.add("summary", new JsonObject().add("total", results.size()).add("passed", passed).add("failed", failed)
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
				return DynamicTest.dynamicTest(fileName, () -> parse(path).runSingleFile());
			}).collect(Collectors.toList());

			if (dirName.isEmpty())
				containers.addAll(tests);
			else
				containers.add(DynamicContainer.dynamicContainer(dirName, tests));

		}
		return containers;
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

			if (insideYaml)
				yamlLines.add(line);
			else if (yamlDone)
				pumlLines.add(line);
			else
				pumlLines.add(line);

		}

		final Monomorph yaml;
		if (yamlLines.isEmpty())
			yaml = new Monomorph();
		else
			yaml = new YamlParser().parse(yamlLines);

		return new VegaTestData(path, yaml, pumlLines);
	}

}
