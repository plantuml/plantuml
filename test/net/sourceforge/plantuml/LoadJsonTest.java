package net.sourceforge.plantuml;

import net.sourceforge.plantuml.security.SFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * Tests the load of a JSON file.
 * <p>
 * Current limitation: Only local file tests, not tests to a http rest endpoint
 */
class LoadJsonTest {

    private static final String[] COMMON_OPTIONS = {"-tutxt"};

    private static final String JSON = "{\"jsonTestKey\": \"exampleValue\"}";

    private static final String DEF_JSON = "{\"jsonTestKey\": \"exampleDefaultValue\"}";

    // ************ Test DSL data
    private static final String DIAGRAM = "" +
            "@startuml\n" +
            "!$JSON_DATA=%loadJSON(test.json)\n" +

            // title should have the value from the JSON file
            "title $JSON_DATA.jsonTestKey\n" +
            "a -> b\n" +
            "@enduml\n";

    private static final String DIAGRAM_DEF = "" +
            "@startuml\n" +
            "!$DEF_JSON=" + DEF_JSON + "\n" +
            "!$JSON_DATA=%loadJSON(\"test-notfound.json\", $DEF_JSON)\n" +

            // title should have the value from the default (because the file doesn't exist)
            "title $JSON_DATA.jsonTestKey\n" +
            "a -> b\n" +
            "@enduml\n";

    private static final String DIAGRAM_DEF_EMPTY = "" +
            "@startuml\n" +
            "!$JSON_DATA=%loadJSON(\"test-notfound.json\")\n" +
            // JSON_DATA is defined, but empty (loadJSON default). So, title contains only  "xx  yy".
            "title xx $JSON_DATA.jsonTestKey yy\n" +
            "a -> b\n" +
            "@enduml\n";


    @TempDir
    Path tempDir;

    /**
     * Resets the current directory.
     */
    @AfterAll
    static void cleanUp() {
        FileSystem.getInstance().reset();
    }

    /**
     * Prepares test JSON file and sets the current directory for each test.
     *
     * @throws Exception hopefully not
     */
    @BeforeEach
    public void beforeEach() throws Exception {
        writeUtf8File(tempDir.resolve("test.json"), JSON);
        FileSystem.getInstance().setCurrentDir(new SFile(tempDir.toFile().getAbsolutePath()));
    }

    /**
     * Tests, if the loadJSON is loading the JSON file from test tmp folder.
     *
     * @throws Exception if something went wrong in this test
     */
    @Test
    void testLoadJsonSimple() throws Exception {
        String rendered = render(DIAGRAM);
        assertThat(rendered).doesNotContain("syntax").contains("exampleValue");

    }

    /**
     * Tests, if the loadJSON is using the default JSON given as parameter.
     *
     * @throws Exception if something went wrong in this test
     */
    @Test
    void testLoadJsonNotFoundWithDefaultParameter() throws Exception {
        String rendered = render(DIAGRAM_DEF);
        assertThat(rendered).doesNotContain("syntax").contains("exampleDefaultValue");
    }

    /**
     * Tests, if the loadJSON is using the default JSON.
     *
     * @throws Exception if something went wrong in this test
     */
    @Test
    void testLoadJsonNotFoundWithDefaultEmpty() throws Exception {
        String rendered = render(DIAGRAM_DEF_EMPTY);
        assertThat(rendered).doesNotContain("syntax").contains("xx  yy");
    }

    private String[] optionArray(String... extraOptions) {
        final List<String> list = newArrayList(COMMON_OPTIONS);
        Collections.addAll(list, extraOptions);
        return list.toArray(new String[0]);
    }

    private String render(String diagram, String... extraOptions) throws Exception {

        final Option option = new Option(optionArray(extraOptions));

        final ByteArrayInputStream bais = new ByteArrayInputStream(diagram.getBytes(UTF_8));

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final Pipe pipe = new Pipe(option, new PrintStream(baos), bais, option.getCharset());

        pipe.managePipe(ErrorStatus.init());

        return new String(baos.toByteArray(), UTF_8);
    }

}
