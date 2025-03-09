package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;

class YamlParserWithJsonTest {

	@Test
	void testYamlWithChristmasData() {
		List<String> yaml = Arrays.asList( //
				"#highlight \"french-hens\"", "#highlight \"xmas-fifth-day\" / \"partridges\"", "",
				"doe: \"a deer, a female deer\"", //
				"ray: \"a drop of golden sun\"", //
				"pi: 3.14159", //
				"xmas: true", //
				"french-hens: 3", //
				"calling-birds:", //
				"  - huey", //
				"  - dewey", //
				"  - louie", //
				"  - fred", //
				"xmas-fifth-day:", //
				"  calling-birds: four", //
				"  french-hens: 3", //
				"  golden-rings: 5", //
				"  partridges:", //
				"    count: 1", //
				"    location: \"a pear tree\"", //
				"  turtle-doves: two" //
		);

		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		assertEquals("a deer, a female deer", result.get("doe").asString());
		assertEquals("a drop of golden sun", result.get("ray").asString());
		assertEquals("3.14159", result.get("pi").asString());
		assertEquals("true", result.get("xmas").asString());
		assertEquals("3", result.get("french-hens").asString());

		JsonArray callingBirds = result.get("calling-birds").asArray();
		assertEquals(4, callingBirds.size());
		assertEquals("huey", callingBirds.get(0).asString());
		assertEquals("dewey", callingBirds.get(1).asString());
		assertEquals("louie", callingBirds.get(2).asString());
		assertEquals("fred", callingBirds.get(3).asString());

		JsonObject xmasFifthDay = result.get("xmas-fifth-day").asObject();
		assertEquals("four", xmasFifthDay.get("calling-birds").asString());
		assertEquals("3", xmasFifthDay.get("french-hens").asString());
		assertEquals("5", xmasFifthDay.get("golden-rings").asString());

		JsonObject partridges = xmasFifthDay.get("partridges").asObject();
		assertEquals("1", partridges.get("count").asString());
		assertEquals("a pear tree", partridges.get("location").asString());

		assertEquals("two", xmasFifthDay.get("turtle-doves").asString());
	}

	@Test
	void testFullLibraryYaml() {
		List<String> yaml = Arrays.asList("# here is a full test for the library!", //
				"# let's start form empty string value :)", //
				"test:", //
				"", //
				"# key contains space is ok", //
				"the key: the value", //
				"", //
				"# we have 10 types natively supported", //
				"# string/multiline/text are take as string", //
				"types:", //
				"  # bool", //
				"  bool: true", //
				"  # byte", //
				"  byte: -1", //
				"  # short", //
				"  short: 3200", //
				"  # int", //
				"  int: -2100000000", //
				"  # long", //
				"  long: 1234321425321", //
				"  # float", //
				"  float: 3.1415926", //
				"  # double", //
				"  double: 123413.4567654567654", //
				"  # char", //
				"  char: c", //
				"  # string", //
				"  string: this is a string", //
				"  # multiline string is one string joined by space (\" \")", //
				"  multiline:", //
				"    # value is also commentable 1", //
				"    line 1", //
				"    line 2", //
				"    # value is also commentable 2 (will move up)", //
				"    line 3", //
				"    # value is also commentable 3 (will move up)", //
				"  # text string is one kept literal originally (but cannot comment the value)", //
				"  text: |", //
				"    def func(x) do", //
				"      # do something", //
				"      print x = x * 2", //
				"    end", //
				"  # datetime", //
				"  datetime: 1997-01-06 23:12:10", //
				"", //
				"# finally, this is an orphan comment (will be removed)");

		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		assertEquals("", result.get("test").asString());

		assertEquals("the value", result.get("the key").asString());

		JsonObject types = result.get("types").asObject();
		assertEquals("true", types.get("bool").asString());
		assertEquals("-1", types.get("byte").asString());
		assertEquals("3200", types.get("short").asString());
		assertEquals("-2100000000", types.get("int").asString());
		assertEquals("1234321425321", types.get("long").asString());
		assertEquals("3.1415926", types.get("float").asString());
		assertEquals("123413.4567654567654", types.get("double").asString());
		assertEquals("c", types.get("char").asString());
		assertEquals("this is a string", types.get("string").asString());

		assertEquals("line 1 line 2 line 3", types.get("multiline").asString());

		String expectedText = "def func(x) do\n  # do something\n  print x = x * 2\nend";
		// assertEquals(expectedText, types.get("text").asString());

		assertEquals("1997-01-06 23:12:10", types.get("datetime").asString());
	}

	@Test
	void testSimple01() {
		List<String> yaml = Arrays.asList( //
				"name: vibrant", //
				"type: 'dark'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));
		
		assertEquals("vibrant", result.get("name").asString());
		assertEquals("dark", result.get("type").asString());
	}

	@Test
	void testSimple02() {
		List<String> yaml = Arrays.asList( //
				"metadata:", //
				"  name: vibrant", //
				"  type: 'dark'" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

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

	@Test
	void testString01() {
		List<String> yaml = Arrays.asList( //
				"metadata:", //
				"  desc: |", //
				"    line0", //
				"    line1", //
				"    line2", //
				"  end: 42" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		JsonObject metadata = result.get("metadata").asObject();
		assertEquals("line0\nline1\nline2\n", metadata.get("desc").asString());
		assertEquals("42", metadata.get("end").asString());
	}

	@Test
	void testYamlListDash() {
		List<String> yaml = Arrays.asList( //
				"root:", //
				" -", //
				"   name: Mark McGwire", //
				"   hr:   65", //
				"   avg:  0.278", //
				" - ", //
				"   name: Sammy Sosa", //
				"   hr:   63", //
				"   avg:  0.288", //
				"", //
				"", //
				"" //
		);
		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		JsonArray root = result.get("root").asArray();
		assertEquals(2, root.size());
		JsonObject element1 = root.get(0).asObject();
		assertEquals("Mark McGwire", element1.get("name").asString());
		assertEquals("65", element1.get("hr").asString());
		assertEquals("0.278", element1.get("avg").asString());

		JsonObject element2 = root.get(1).asObject();
		assertEquals("Sammy Sosa", element2.get("name").asString());
		assertEquals("63", element2.get("hr").asString());
		assertEquals("0.288", element2.get("avg").asString());

	}

	@Test
	void testYamlWithEmptyStringAndSpaceKey() {

		List<String> yaml = Arrays.asList( //
				"# let's start form empty string value :)", //
				"test:", //
				"# key contains space is ok", //
				"the key: the value" //
		);

		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		assertEquals("", result.get("test").asString());
		assertEquals("the value", result.get("the key").asString());
	}

	@Test
	void testYamlWithPlayers01() {

		List<String> yaml = Arrays.asList( //
				"-", //
				"  name: Mark McGwire", //
				"  hr:   65", //
				"  avg:  0.278", //
				"-", //
				"  name: Sammy Sosa", //
				"  hr:   63", //
				"  avg:  0.288" //
		);

		YamlParser parser = new YamlParser();
		JsonArray players = (JsonArray) MonomorphToJson.convert(parser.parse(yaml));
		
		assertEquals(2, players.size());

		JsonObject player1 = players.get(0).asObject();
		assertEquals("Mark McGwire", player1.get("name").asString());
		assertEquals("65", player1.get("hr").asString());
		assertEquals("0.278", player1.get("avg").asString());

		JsonObject player2 = players.get(1).asObject();
		assertEquals("Sammy Sosa", player2.get("name").asString());
		assertEquals("63", player2.get("hr").asString());
		assertEquals("0.288", player2.get("avg").asString());
	}

	@Test
	void testYamlWithPlayers02() {

		List<String> yaml = Arrays.asList( //
				" - name: Mark McGwire", //
				"   hr:   65", //
				"   avg:  0.278", //
				" - name: Sammy Sosa", //
				"   hr:   63", //
				"   avg:  0.288" //
		);

		YamlParser parser = new YamlParser();
		JsonArray players = (JsonArray) MonomorphToJson.convert(parser.parse(yaml));

		assertEquals(2, players.size());

		JsonObject player1 = players.get(0).asObject();
		assertEquals("Mark McGwire", player1.get("name").asString());
		assertEquals("65", player1.get("hr").asString());
		assertEquals("0.278", player1.get("avg").asString());

		JsonObject player2 = players.get(1).asObject();
		assertEquals("Sammy Sosa", player2.get("name").asString());
		assertEquals("63", player2.get("hr").asString());
		assertEquals("0.288", player2.get("avg").asString());
	}

	@Test
	void testYamlWithEmptyStringAndSpaceKey2() {

		List<String> yaml = Arrays.asList( //
				"the key: the value", //
				"test:" //
		);

		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		assertEquals("", result.get("test").asString());
		assertEquals("the value", result.get("the key").asString());
	}

	@Test
	void testYamlList2() {
		List<String> yaml = Arrays.asList( //
				"hash:", //
				"  name1:", //
				"    Text1", //
				"  name2: Text2");
		YamlParser parser = new YamlParser();
		JsonObject result = (JsonObject) MonomorphToJson.convert(parser.parse(yaml));

		JsonObject hash = result.get("hash").asObject();
		assertEquals("Text1", hash.get("name1").asString());
		assertEquals("Text2", hash.get("name2").asString());

	}

}
