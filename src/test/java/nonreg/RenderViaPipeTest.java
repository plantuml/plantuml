package nonreg;

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
 * Tests the Render
 */
class RenderViaPipeTest {

    @ParameterizedTest(name = "[{index}] {1}")
	@CsvSource(nullValues = "null", value = {
        "'@startuml\n!$a={\"k\": \"exampleValue\"}\ntitle $a.k\na -> b\n@enduml\n'                       , exampleValue",
        "'@startuml\n!$a=[1, 2, 3]\ntitle xx $a[2] yy\na -> a\n'                                         , xx 3 yy",
        "'@startuml\ntitle\n!foreach $i in [1, 2, 3]\nxx $i yy\n!endfor\nendtitle\na -> a\n'             , xx 2 yy",
        "'@startuml\ntitle\n!foreach $i in [\"a\", \"b\", \"c\"]\nxx $i yy\n!endfor\nendtitle\na -> a\n' , xx b yy",     
	})
	void RenderTest(String input, String expected) throws Exception {
		assertRenderExpectedOutput(input, expected);
	}

    // TODO: to Factorize on a specific test package...
    private static final String[] COMMON_OPTIONS = {"-preproc"};
    // OK with "-tutxt"
    // private static final String[] COMMON_OPTIONS = {"-tutxt"};

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
        // System.out.println(rendered);
        return rendered;
    }

    public void assertRenderExpectedOutput(String input, String expected) throws Exception {
        final String rendered = renderViaPipe(input);
        assertThat(rendered).doesNotContain("syntax").contains(expected);
    }   
}
