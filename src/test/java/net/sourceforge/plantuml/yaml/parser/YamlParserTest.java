package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.yaml.parser.Monomorph;
import net.sourceforge.plantuml.yaml.parser.YamlParser;

class YamlParserTest {

    @Test
    void testYamlWithChristmasData() {
        List<String> yaml = Arrays.asList(
            "#highlight \"french-hens\"",
            "#highlight \"xmas-fifth-day\" / \"partridges\"",
            "",
            "doe: \"a deer, a female deer\"",
            "ray: \"a drop of golden sun\"",
            "pi: 3.14159",
            "xmas: true",
            "french-hens: 3",
            "calling-birds:",
            "  - huey",
            "  - dewey",
            "  - louie",
            "  - fred",
            "xmas-fifth-day:",
            "  calling-birds: four",
            "  french-hens: 3",
            "  golden-rings: 5",
            "  partridges:",
            "    count: 1",
            "    location: \"a pear tree\"",
            "  turtle-doves: two"
        );

        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        assertEquals("a deer, a female deer", result.getMapValue("doe").getValue());
        assertEquals("a drop of golden sun", result.getMapValue("ray").getValue());
        assertEquals("3.14159", result.getMapValue("pi").getValue());
        assertEquals("true", result.getMapValue("xmas").getValue());
        assertEquals("3", result.getMapValue("french-hens").getValue());

        Monomorph callingBirds = result.getMapValue("calling-birds");
        assertEquals(4, callingBirds.size());
        assertEquals("huey", callingBirds.getElementAt(0).getValue());
        assertEquals("dewey", callingBirds.getElementAt(1).getValue());
        assertEquals("louie", callingBirds.getElementAt(2).getValue());
        assertEquals("fred", callingBirds.getElementAt(3).getValue());

        Monomorph xmasFifthDay = result.getMapValue("xmas-fifth-day");
        assertEquals("four", xmasFifthDay.getMapValue("calling-birds").getValue());
        assertEquals("3", xmasFifthDay.getMapValue("french-hens").getValue());
        assertEquals("5", xmasFifthDay.getMapValue("golden-rings").getValue());

        Monomorph partridges = xmasFifthDay.getMapValue("partridges");
        assertEquals("1", partridges.getMapValue("count").getValue());
        assertEquals("a pear tree", partridges.getMapValue("location").getValue());

        assertEquals("two", xmasFifthDay.getMapValue("turtle-doves").getValue());
    }

    @Test
    void testFullLibraryYaml() {
        List<String> yaml = Arrays.asList(
            "# here is a full test for the library!",
            "# let's start form empty string value :)",
            "test:",
            "",
            "# key contains space is ok",
            "the key: the value",
            "",
            "# we have 10 types natively supported",
            "# string/multiline/text are take as string",
            "types:",
            "  # bool",
            "  bool: true",
            "  # byte",
            "  byte: -1",
            "  # short",
            "  short: 3200",
            "  # int",
            "  int: -2100000000",
            "  # long",
            "  long: 1234321425321",
            "  # float",
            "  float: 3.1415926",
            "  # double",
            "  double: 123413.4567654567654",
            "  # char",
            "  char: c",
            "  # string",
            "  string: this is a string",
            "  # multiline string is one string joined by space (\" \")",
            "  multiline:",
            "    # value is also commentable 1",
            "    line 1",
            "    line 2",
            "    # value is also commentable 2 (will move up)",
            "    line 3",
            "    # value is also commentable 3 (will move up)",
            "  # text string is one kept literal originally (but cannot comment the value)",
            "  text: |",
            "    def func(x) do",
            "      # do something",
            "      print x = x * 2",
            "    end",
            "  # datetime",
            "  datetime: 1997-01-06 23:12:10",
            "",
            "# finally, this is an orphan comment (will be removed)"
        );

        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        assertEquals("", result.getMapValue("test").getValue());
        assertEquals("the value", result.getMapValue("the key").getValue());

        Monomorph types = result.getMapValue("types");
        assertEquals("true", types.getMapValue("bool").getValue());
        assertEquals("-1", types.getMapValue("byte").getValue());
        assertEquals("3200", types.getMapValue("short").getValue());
        assertEquals("-2100000000", types.getMapValue("int").getValue());
        assertEquals("1234321425321", types.getMapValue("long").getValue());
        assertEquals("3.1415926", types.getMapValue("float").getValue());
        assertEquals("123413.4567654567654", types.getMapValue("double").getValue());
        assertEquals("c", types.getMapValue("char").getValue());
        assertEquals("this is a string", types.getMapValue("string").getValue());
        assertEquals("line 1 line 2 line 3", types.getMapValue("multiline").getValue());

        String expectedText = "def func(x) do\n  # do something\n  print x = x * 2\nend";
        // Uncomment if needed:
        // assertEquals(expectedText, types.getMapValue("text").getValue());

        assertEquals("1997-01-06 23:12:10", types.getMapValue("datetime").getValue());
    }

    @Test
    void testSimple01() {
        List<String> yaml = Arrays.asList(
            "name: vibrant",
            "type: 'dark'"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);
        assertEquals("vibrant", result.getMapValue("name").getValue());
        assertEquals("dark", result.getMapValue("type").getValue());
    }

    @Test
    void testSimple02() {
        List<String> yaml = Arrays.asList(
            "metadata:",
            "  name: vibrant",
            "  type: 'dark'"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);
        Monomorph metadata = result.getMapValue("metadata");
        assertEquals("vibrant", metadata.getMapValue("name").getValue());
        assertEquals("dark", metadata.getMapValue("type").getValue());
    }

    @Test
    void testComplexYaml01() {
        List<String> yaml = Arrays.asList(
            "root:",
            "  metadata:",
            "    name: vibrant",
            "    type: 'dark'",
            "  details:",
            "    version: 1.0",
            "    info:",
            "      description: 'This is a complex YAML'",
            "      author: 'John Doe'"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph root = result.getMapValue("root");
        Monomorph metadata = root.getMapValue("metadata");
        Monomorph details = root.getMapValue("details");
        Monomorph info = details.getMapValue("info");

        assertEquals("vibrant", metadata.getMapValue("name").getValue());
        assertEquals("dark", metadata.getMapValue("type").getValue());
        assertEquals("1.0", details.getMapValue("version").getValue());
        assertEquals("This is a complex YAML", info.getMapValue("description").getValue());
        assertEquals("John Doe", info.getMapValue("author").getValue());
    }

    @Test
    void testComplexYaml02() {
        List<String> yaml = Arrays.asList(
            "root:",
            "  metadata:",
            "    name: vibrant",
            "    type: 'dark'",
            "  details:",
            "    version: 1.0",
            "    info:",
            "      description: 'This is a complex YAML'",
            "      author: 'John Doe'",
            "  address:",
            "    street: '123 Main St'",
            "    city: 'Paris'",
            "    zipcode: '75000'",
            "    country: 'France'"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph root = result.getMapValue("root");
        Monomorph metadata = root.getMapValue("metadata");
        Monomorph details = root.getMapValue("details");
        Monomorph info = details.getMapValue("info");
        Monomorph address = root.getMapValue("address");

        assertEquals("vibrant", metadata.getMapValue("name").getValue());
        assertEquals("dark", metadata.getMapValue("type").getValue());
        assertEquals("1.0", details.getMapValue("version").getValue());
        assertEquals("This is a complex YAML", info.getMapValue("description").getValue());
        assertEquals("John Doe", info.getMapValue("author").getValue());
        assertEquals("123 Main St", address.getMapValue("street").getValue());
        assertEquals("Paris", address.getMapValue("city").getValue());
        assertEquals("75000", address.getMapValue("zipcode").getValue());
        assertEquals("France", address.getMapValue("country").getValue());
    }

    @Test
    void testComplexYaml03() {
        List<String> yaml = Arrays.asList(
            "header:",
            "  id: 123",
            "  info: 'dummy info'",
            "root:",
            "  metadata:",
            "    name: vibrant",
            "    type: 'dark'",
            "  details:",
            "    version: 1.0",
            "    info:",
            "      description: 'This is a complex YAML'",
            "      author: 'John Doe'",
            "  address:",
            "    street: '123 Main St'",
            "    city: 'Paris'",
            "    zipcode: '75000'",
            "    country: 'France'"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph header = result.getMapValue("header");
        Monomorph root = result.getMapValue("root");
        Monomorph metadata = root.getMapValue("metadata");
        Monomorph details = root.getMapValue("details");
        Monomorph info = details.getMapValue("info");
        Monomorph address = root.getMapValue("address");

        assertEquals("vibrant", metadata.getMapValue("name").getValue());
        assertEquals("dark", metadata.getMapValue("type").getValue());
        assertEquals("1.0", details.getMapValue("version").getValue());
        assertEquals("This is a complex YAML", info.getMapValue("description").getValue());
        assertEquals("John Doe", info.getMapValue("author").getValue());
        assertEquals("123 Main St", address.getMapValue("street").getValue());
        assertEquals("Paris", address.getMapValue("city").getValue());
        assertEquals("75000", address.getMapValue("zipcode").getValue());
        assertEquals("France", address.getMapValue("country").getValue());
        assertEquals("123", header.getMapValue("id").getValue());
        assertEquals("dummy info", header.getMapValue("info").getValue());
    }

    @Test
    void testYamlWithLists01() {
        List<String> yaml = Arrays.asList(
            "meta:",
            "    countries:",
            "      - country: USA",
            "        capital: 'Washington D.C.'",
            "      - country: France",
            "        capital: Paris",
            "      - country: Japan",
            "        capital: Tokyo"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph meta = result.getMapValue("meta");
        Monomorph countries = meta.getMapValue("countries");
        assertEquals(3, countries.size());

        Monomorph country1 = countries.getElementAt(0);
        assertEquals("USA", country1.getMapValue("country").getValue());
        assertEquals("Washington D.C.", country1.getMapValue("capital").getValue());

        Monomorph country2 = countries.getElementAt(1);
        assertEquals("France", country2.getMapValue("country").getValue());
        assertEquals("Paris", country2.getMapValue("capital").getValue());

        Monomorph country3 = countries.getElementAt(2);
        assertEquals("Japan", country3.getMapValue("country").getValue());
        assertEquals("Tokyo", country3.getMapValue("capital").getValue());
    }

    @Test
    void testYamlWithSimpleLists01() {
        List<String> yaml = Arrays.asList(
            "colors:",
            "- red",
            "- blue",
            "- green"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph colors = result.getMapValue("colors");
        assertEquals(3, colors.size());
        assertEquals("red", colors.getElementAt(0).getValue());
        assertEquals("blue", colors.getElementAt(1).getValue());
        assertEquals("green", colors.getElementAt(2).getValue());
    }

    @Test
    void testYamlWithSimpleLists02() {
        List<String> yaml = Arrays.asList(
            "colors: ['red', 'blue', 'green']"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph colors = result.getMapValue("colors");
        assertEquals(3, colors.size());
        assertEquals("red", colors.getElementAt(0).getValue());
        assertEquals("blue", colors.getElementAt(1).getValue());
        assertEquals("green", colors.getElementAt(2).getValue());
    }

    @Test
    void testYamlWithLists() {
        List<String> yaml = Arrays.asList(
            "colors:",
            "- red",
            "- blue",
            "- green",
            "",
            "countries:",
            "- country: USA",
            "  capital: 'Washington D.C.'",
            "- country: France",
            "  capital: Paris",
            "- country: Japan",
            "  capital: Tokyo"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph colors = result.getMapValue("colors");
        assertEquals(3, colors.size());
        assertEquals("red", colors.getElementAt(0).getValue());
        assertEquals("blue", colors.getElementAt(1).getValue());
        assertEquals("green", colors.getElementAt(2).getValue());

        Monomorph countries = result.getMapValue("countries");
        assertEquals(3, countries.size());

        Monomorph country1 = countries.getElementAt(0);
        assertEquals("USA", country1.getMapValue("country").getValue());
        assertEquals("Washington D.C.", country1.getMapValue("capital").getValue());

        Monomorph country2 = countries.getElementAt(1);
        assertEquals("France", country2.getMapValue("country").getValue());
        assertEquals("Paris", country2.getMapValue("capital").getValue());

        Monomorph country3 = countries.getElementAt(2);
        assertEquals("Japan", country3.getMapValue("country").getValue());
        assertEquals("Tokyo", country3.getMapValue("capital").getValue());
    }

    @Test
    void testYamlWithListsAndMultipleHeaders01() {
        List<String> yaml = Arrays.asList(
            "header:",
            "  id: 123",
            "  name: sample1",
            "  description: example2",
            "header2:",
            "   id: 123",
            "   name: sample3",
            "   description: example4",
            "   details:",
            "    error: sample5"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph header = result.getMapValue("header");
        assertEquals("123", header.getMapValue("id").getValue());
        assertEquals("sample1", header.getMapValue("name").getValue());
        assertEquals("example2", header.getMapValue("description").getValue());

        Monomorph header2 = result.getMapValue("header2");
        assertEquals("123", header2.getMapValue("id").getValue());
        assertEquals("sample3", header2.getMapValue("name").getValue());
        assertEquals("example4", header2.getMapValue("description").getValue());
        Monomorph details = header2.getMapValue("details");
        assertEquals("sample5", details.getMapValue("error").getValue());
    }

    @Test
    void testYamlWithListsAndMultipleHeaders02() {
        List<String> yaml = Arrays.asList(
            "header:",
            "  id: 123",
            "  name: sample1",
            "  description: example2",
            "header2:",
            "   id: 123",
            "   name: sample3",
            "   description: example4",
            "   details:",
            "    error: sample5",
            "colors:",
            "   - red:",
            "      value: sample6",
            "   - blue:",
            "       shade: sample7",
            "   - green:",
            "         intensity: sample8"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph header = result.getMapValue("header");
        assertEquals("123", header.getMapValue("id").getValue());
        assertEquals("sample1", header.getMapValue("name").getValue());
        assertEquals("example2", header.getMapValue("description").getValue());

        Monomorph header2 = result.getMapValue("header2");
        assertEquals("123", header2.getMapValue("id").getValue());
        assertEquals("sample3", header2.getMapValue("name").getValue());
        assertEquals("example4", header2.getMapValue("description").getValue());
        Monomorph details = header2.getMapValue("details");
        assertEquals("sample5", details.getMapValue("error").getValue());

        Monomorph colors = result.getMapValue("colors");
        assertEquals(3, colors.size());

        Monomorph redEntry = colors.getElementAt(0);
        Monomorph redMapping = redEntry.getMapValue("red");
        assertEquals("sample6", redMapping.getMapValue("value").getValue());

        Monomorph blueEntry = colors.getElementAt(1);
        Monomorph blueMapping = blueEntry.getMapValue("blue");
        assertEquals("sample7", blueMapping.getMapValue("shade").getValue());

        Monomorph greenEntry = colors.getElementAt(2);
        Monomorph greenMapping = greenEntry.getMapValue("green");
        assertEquals("sample8", greenMapping.getMapValue("intensity").getValue());
    }

    @Test
    void testString01() {
        List<String> yaml = Arrays.asList(
            "metadata:",
            "  desc: |",
            "    line0",
            "    line1",
            "    line2",
            "  end: 42"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph metadata = result.getMapValue("metadata");
        assertEquals("line0\nline1\nline2\n", metadata.getMapValue("desc").getValue());
        assertEquals("42", metadata.getMapValue("end").getValue());
    }

    @Test
    void testYamlListDash() {
        List<String> yaml = Arrays.asList(
            "root:",
            " -",
            "   name: Mark McGwire",
            "   hr:   65",
            "   avg:  0.278",
            " - ",
            "   name: Sammy Sosa",
            "   hr:   63",
            "   avg:  0.288",
            "",
            "",
            ""
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph root = result.getMapValue("root");
        assertEquals(2, root.size());
        Monomorph element1 = root.getElementAt(0);
        assertEquals("Mark McGwire", element1.getMapValue("name").getValue());
        assertEquals("65", element1.getMapValue("hr").getValue());
        assertEquals("0.278", element1.getMapValue("avg").getValue());

        Monomorph element2 = root.getElementAt(1);
        assertEquals("Sammy Sosa", element2.getMapValue("name").getValue());
        assertEquals("63", element2.getMapValue("hr").getValue());
        assertEquals("0.288", element2.getMapValue("avg").getValue());
    }

    @Test
    void testYamlWithEmptyStringAndSpaceKey() {
        List<String> yaml = Arrays.asList(
            "# let's start form empty string value :)",
            "test:",
            "# key contains space is ok",
            "the key: the value"
        );

        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        assertEquals("", result.getMapValue("test").getValue());
        assertEquals("the value", result.getMapValue("the key").getValue());
    }

    @Test
    void testYamlWithPlayers01() {
        List<String> yaml = Arrays.asList(
            "-",
            "  name: Mark McGwire",
            "  hr:   65",
            "  avg:  0.278",
            "-",
            "  name: Sammy Sosa",
            "  hr:   63",
            "  avg:  0.288"
        );

        YamlParser parser = new YamlParser();
        Monomorph players = (Monomorph) parser.parse(yaml);
        assertEquals(2, players.size());

        Monomorph player1 = players.getElementAt(0);
        assertEquals("Mark McGwire", player1.getMapValue("name").getValue());
        assertEquals("65", player1.getMapValue("hr").getValue());
        assertEquals("0.278", player1.getMapValue("avg").getValue());

        Monomorph player2 = players.getElementAt(1);
        assertEquals("Sammy Sosa", player2.getMapValue("name").getValue());
        assertEquals("63", player2.getMapValue("hr").getValue());
        assertEquals("0.288", player2.getMapValue("avg").getValue());
    }

    @Test
    void testYamlWithPlayers02() {
        List<String> yaml = Arrays.asList(
            " - name: Mark McGwire",
            "   hr:   65",
            "   avg:  0.278",
            " - name: Sammy Sosa",
            "   hr:   63",
            "   avg:  0.288"
        );

        YamlParser parser = new YamlParser();
        Monomorph players = parser.parse(yaml);
        assertEquals(2, players.size());

        Monomorph player1 = players.getElementAt(0);
        assertEquals("Mark McGwire", player1.getMapValue("name").getValue());
        assertEquals("65", player1.getMapValue("hr").getValue());
        assertEquals("0.278", player1.getMapValue("avg").getValue());

        Monomorph player2 = players.getElementAt(1);
        assertEquals("Sammy Sosa", player2.getMapValue("name").getValue());
        assertEquals("63", player2.getMapValue("hr").getValue());
        assertEquals("0.288", player2.getMapValue("avg").getValue());
    }

    @Test
    void testYamSimple() {
        List<String> yaml = Arrays.asList(
            "the key: the value",
            "test: hello"
        );

        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        assertEquals("the value", result.getMapValue("the key").getValue());
        assertEquals("hello", result.getMapValue("test").getValue());
    }

    @Test
    void testYamlWithEmptyStringAndSpaceKey2() {
        List<String> yaml = Arrays.asList(
            "the key: the value",
            "test:"
        );

        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        assertEquals("", result.getMapValue("test").getValue());
        assertEquals("the value", result.getMapValue("the key").getValue());
    }

    @Test
    void testYamlList2() {
        List<String> yaml = Arrays.asList(
            "hash:",
            "  name1:",
            "    Text1",
            "  name2: Text2"
        );
        YamlParser parser = new YamlParser();
        Monomorph result = parser.parse(yaml);

        Monomorph hash = result.getMapValue("hash");
        assertEquals("Text1", hash.getMapValue("name1").getValue());
        assertEquals("Text2", hash.getMapValue("name2").getValue());
    }

}
