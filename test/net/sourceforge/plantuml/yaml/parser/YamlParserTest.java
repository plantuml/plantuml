package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.json.*;

class YamlParserTest {

	@Test
	void testSimple01() {
		List<String> list = Arrays.asList( //
				"name: vibrant", //
				"type: 'dark'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(list);
		assertEquals("vibrant", result.get("name").asString());
		assertEquals("dark", result.get("type").asString());
	}

	@Test
	void testSimple02() {
		List<String> list = Arrays.asList( //
				"metadata:", //
				"  name: vibrant", //
				"  type: 'dark'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(list);

		JsonObject metadata = result.get("metadata").asObject();
		assertEquals("vibrant", metadata.get("name").asString());
		assertEquals("dark", metadata.get("type").asString());
	}

	@Test
	void testComplexYaml01() {
		List<String> yaml = Arrays.asList( //
				"root:", //
				"  metadata:", //
				"    name: vibrant", //
				"    type: 'dark'", //
				"  details:", //
				"    version: 1.0", //
				"    info:", //
				"      description: 'This is a complex YAML'", //
				"      author: 'John Doe'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Extract nested objects
		JsonObject root = result.get("root").asObject();
		JsonObject metadata = root.get("metadata").asObject();
		JsonObject details = root.get("details").asObject();
		JsonObject info = details.get("info").asObject();

		assertEquals("vibrant", metadata.get("name").asString());
		assertEquals("dark", metadata.get("type").asString());
		assertEquals("1.0", details.get("version").asString());
		assertEquals("This is a complex YAML", info.get("description").asString());
		assertEquals("John Doe", info.get("author").asString());
	}

	@Test
	void testComplexYaml02() {
		List<String> yaml = Arrays.asList( //
				"root:", //
				"  metadata:", //
				"    name: vibrant", //
				"    type: 'dark'", //
				"  details:", //
				"    version: 1.0", //
				"    info:", //
				"      description: 'This is a complex YAML'", //
				"      author: 'John Doe'", //
				"  address:", //
				"    street: '123 Main St'", //
				"    city: 'Paris'", //
				"    zipcode: '75000'", //
				"    country: 'France'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Extract nested objects
		JsonObject root = result.get("root").asObject();
		JsonObject metadata = root.get("metadata").asObject();
		JsonObject details = root.get("details").asObject();
		JsonObject info = details.get("info").asObject();
		JsonObject address = root.get("address").asObject();

		// Assertions for metadata
		assertEquals("vibrant", metadata.get("name").asString());
		assertEquals("dark", metadata.get("type").asString());
		// Assertions for metadata
		assertEquals("1.0", details.get("version").asString());
		assertEquals("This is a complex YAML", info.get("description").asString());
		assertEquals("John Doe", info.get("author").asString());
		// Assertions for address
		assertEquals("123 Main St", address.get("street").asString());
		assertEquals("Paris", address.get("city").asString());
		assertEquals("75000", address.get("zipcode").asString());
		assertEquals("France", address.get("country").asString());
	}

	@Test
	void testComplexYaml03() {
		List<String> yaml = Arrays.asList( //
				"header:", //
				"  id: 123", //
				"  info: 'dummy info'", //
				"root:", //
				"  metadata:", //
				"    name: vibrant", //
				"    type: 'dark'", //
				"  details:", //
				"    version: 1.0", //
				"    info:", //
				"      description: 'This is a complex YAML'", //
				"      author: 'John Doe'", //
				"  address:", //
				"    street: '123 Main St'", //
				"    city: 'Paris'", //
				"    zipcode: '75000'", //
				"    country: 'France'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Extract nested objects
		JsonObject header = result.get("header").asObject();
		JsonObject root = result.get("root").asObject();
		JsonObject metadata = root.get("metadata").asObject();
		JsonObject details = root.get("details").asObject();
		JsonObject info = details.get("info").asObject();
		JsonObject address = root.get("address").asObject();

		// Assertions for metadata
		assertEquals("vibrant", metadata.get("name").asString());
		assertEquals("dark", metadata.get("type").asString());
		// Assertions for details
		assertEquals("1.0", details.get("version").asString());
		assertEquals("This is a complex YAML", info.get("description").asString());
		assertEquals("John Doe", info.get("author").asString());
		// Assertions for address
		assertEquals("123 Main St", address.get("street").asString());
		assertEquals("Paris", address.get("city").asString());
		assertEquals("75000", address.get("zipcode").asString());
		assertEquals("France", address.get("country").asString());
		// Assertions for header
		assertEquals("123", header.get("id").asString());
		assertEquals("dummy info", header.get("info").asString());
	}

	@Test
	void testYamlWithLists01() {
		List<String> yaml = Arrays.asList( //
				"meta:", //
				"    countries:", //
				"      - country: USA", //
				"        capital: 'Washington D.C.'", //
				"      - country: France", //
				"        capital: Paris", //
				"      - country: Japan", //
				"        capital: Tokyo" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		JsonArray countries = result.get("meta").asObject().get("countries").asArray();
		assertEquals(3, countries.size());

		JsonObject country1 = countries.get(0).asObject();
		assertEquals("USA", country1.get("country").asString());
		assertEquals("Washington D.C.", country1.get("capital").asString());

		JsonObject country2 = countries.get(1).asObject();
		assertEquals("France", country2.get("country").asString());
		assertEquals("Paris", country2.get("capital").asString());

		JsonObject country3 = countries.get(2).asObject();
		assertEquals("Japan", country3.get("country").asString());
		assertEquals("Tokyo", country3.get("capital").asString());
	}

	@Test
	void testYamlWithSimpleLists01() {
		List<String> yaml = Arrays.asList( //
				"colors:", //
				"- red", //
				"- blue", //
				"- green" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Checking the list of colors
		JsonArray colors = result.get("colors").asArray();
		assertEquals(3, colors.size());
		assertEquals("red", colors.get(0).asString());
		assertEquals("blue", colors.get(1).asString());
		assertEquals("green", colors.get(2).asString());

	}

	// @Test
	void testYamlWithSimpleLists02() {
		List<String> yaml = Arrays.asList( //
				"colors: ['red, 'blue', 'green']" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Checking the list of colors
		JsonArray colors = result.get("colors").asArray();
		assertEquals(3, colors.size());
		assertEquals("red", colors.get(0).asString());
		assertEquals("blue", colors.get(1).asString());
		assertEquals("green", colors.get(2).asString());

	}

	@Test
	void testYamlWithLists() {
		List<String> yaml = Arrays.asList( //
				"colors:", //
				"- red", //
				"- blue", //
				"- green", //
				"", //
				"countries:", //
				"- country: USA", //
				"  capital: 'Washington D.C.'", //
				"- country: France", //
				"  capital: Paris", //
				"- country: Japan", //
				"  capital: Tokyo" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Checking the list of colors
		JsonArray colors = result.get("colors").asArray();
		assertEquals(3, colors.size());
		assertEquals("red", colors.get(0).asString());
		assertEquals("blue", colors.get(1).asString());
		assertEquals("green", colors.get(2).asString());

		// Checking the list of countries and capitals
		JsonArray countries = result.get("countries").asArray();
		assertEquals(3, countries.size());

		JsonObject country1 = countries.get(0).asObject();
		assertEquals("USA", country1.get("country").asString());
		assertEquals("Washington D.C.", country1.get("capital").asString());

		JsonObject country2 = countries.get(1).asObject();
		assertEquals("France", country2.get("country").asString());
		assertEquals("Paris", country2.get("capital").asString());

		JsonObject country3 = countries.get(2).asObject();
		assertEquals("Japan", country3.get("country").asString());
		assertEquals("Tokyo", country3.get("capital").asString());
	}

	@Test
	void testYamlWithListsAndMultipleHeaders01() {
		List<String> yaml = Arrays.asList( //
				"header:", //
				"  id: 123", //
				"  name: sample1", //
				"  description: example2", //
				"header2:", //
				"   id: 123", //
				"   name: sample3", //
				"   description: example4", //
				"   details:", //
				"    error: sample5" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Checking the header section
		JsonObject header = result.get("header").asObject();
		assertEquals("123", header.get("id").asString());
		assertEquals("sample1", header.get("name").asString());
		assertEquals("example2", header.get("description").asString());

		// Checking the header2 section and its details
		JsonObject header2 = result.get("header2").asObject();
		assertEquals("123", header2.get("id").asString());
		assertEquals("sample3", header2.get("name").asString());
		assertEquals("example4", header2.get("description").asString());
		JsonObject details = header2.get("details").asObject();
		assertEquals("sample5", details.get("error").asString());

	}

	@Test
	void testYamlWithListsAndMultipleHeaders02() {
		List<String> yaml = Arrays.asList( //
				"header:", //
				"  id: 123", //
				"  name: sample1", //
				"  description: example2", //
				"header2:", //
				"   id: 123", //
				"   name: sample3", //
				"   description: example4", //
				"   details:", //
				"    error: sample5", //
				"colors:", //
				"   - red:", //
				"      value: sample6", //
				"   - blue:", //
				"       shade: sample7", //
				"   - green:", //
				"         intensity: sample8" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = parser.parse(yaml);

		// Checking the header section
		JsonObject header = result.get("header").asObject();
		assertEquals("123", header.get("id").asString());
		assertEquals("sample1", header.get("name").asString());
		assertEquals("example2", header.get("description").asString());

		// Checking the header2 section and its details
		JsonObject header2 = result.get("header2").asObject();
		assertEquals("123", header2.get("id").asString());
		assertEquals("sample3", header2.get("name").asString());
		assertEquals("example4", header2.get("description").asString());
		JsonObject details = header2.get("details").asObject();
		assertEquals("sample5", details.get("error").asString());

		// Checking the colors section (list)
		JsonArray colors = result.get("colors").asArray();
		assertEquals(3, colors.size());

		// First element: red
		JsonObject redEntry = colors.get(0).asObject();
		JsonObject redMapping = redEntry.get("red").asObject();
		assertEquals("sample6", redMapping.get("value").asString());

		// Second element: blue
		JsonObject blueEntry = colors.get(1).asObject();
		JsonObject blueMapping = blueEntry.get("blue").asObject();
		assertEquals("sample7", blueMapping.get("shade").asString());

		// Third element: green
		JsonObject greenEntry = colors.get(2).asObject();
		JsonObject greenMapping = greenEntry.get("green").asObject();
		assertEquals("sample8", greenMapping.get("intensity").asString());
	}

}
