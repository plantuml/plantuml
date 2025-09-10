package test.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.ErrorStatus;
import net.sourceforge.plantuml.Pipe;
import net.sourceforge.plantuml.cli.CliOptions;
import net.sourceforge.plantuml.cli.CliParser;

/**
 * Tests the Render UText
 */
class ExportOnUTextTest {

    @ParameterizedTest(name = "[{index}] {1}")
	@CsvSource(value = {
        "'@startuml\na -> b\n@enduml\n', a",
        "'@startuml\nclass a\n@enduml\n', a",
        "'@startuml\n!pragma layout smetana\nclass a\n@enduml\n', a",
	})
	void RenderTest(String input, String expected) throws Exception {
		assertRenderExpectedOutput(input, expected);
	}

    // TODO: to Factorize on a specific test package...
    //
    private static final String[] COMMON_OPTIONS = {"-tutxt"};

    private String[] optionArray(String... extraOptions) {
        final List<String> list = newArrayList(COMMON_OPTIONS);
        Collections.addAll(list, extraOptions);
        return list.toArray(new String[0]);
    }

    private String renderViaPipe(String diagram, String... extraOptions) throws Exception {
        final CliOptions option = CliParser.parse(optionArray(extraOptions));
        final ByteArrayInputStream bais = new ByteArrayInputStream(diagram.getBytes(UTF_8));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Pipe pipe = new Pipe(option, new PrintStream(baos), bais, option.getCharset());
        pipe.managePipe(ErrorStatus.init());
        final String rendered = new String(baos.toByteArray(), UTF_8);
        // println actived
        System.out.println(rendered);
        return rendered;
    }

    public void assertRenderExpectedOutput(String input, String expected) throws Exception {
        final String rendered = renderViaPipe(input);
        assertThat(rendered).doesNotContain("syntax").contains(expected);
    }   
}
