package nonreg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

/**
 * Tests the Render
 */
class RenderViaApiTest {

    @ParameterizedTest(name = "[{index}] {1}")
	@CsvSource(nullValues = "null", value = {
        "'@startuml\n!$a={\"k\": \"exampleValue\"}\ntitle $a.k\na -> b\n@enduml\n'                              , exampleValue",
        "'@startuml\n!$a=[1, 2, 3]\ntitle xx $a[2] yy\na -> a\n@enduml'                                         , xx 3 yy",
        "'@startuml\ntitle\n!foreach $i in [1, 2, 3]\nxx $i yy\n!endfor\nendtitle\na -> a\n@enduml'             , xx 2 yy",
        "'@startuml\ntitle\n!foreach $i in [\"a\", \"b\", \"c\"]\nxx $i yy\n!endfor\nendtitle\na -> a\n@enduml' , xx b yy",     
	})
	void RenderTest(String input, String expected) throws Exception {
		assertRenderExpectedOutput(input, expected);
	}

    // TODO: to Factorize on a specific test package...

	private String RenderViaApiTest(String diagram, FileFormat format) throws IOException, UnsupportedEncodingException {
		final SourceStringReader ssr = new SourceStringReader(diagram, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(format));
        final String rendered = new String(baos.toByteArray(), UTF_8);
        // System.out.println(rendered);
        return rendered;
	}

    public void assertRenderExpectedOutput(String input, String expected) throws Exception {
        final String rendered = RenderViaApiTest(input, FileFormat.PREPROC);
        // OK with:
        // final String rendered = RenderViaApiTest(input, FileFormat.UTXT);
        assertThat(rendered).doesNotContain("syntax").contains(expected);
    }   
}
