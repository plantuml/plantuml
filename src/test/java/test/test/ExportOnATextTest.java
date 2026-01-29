package test.test;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Pipe;
import net.sourceforge.plantuml.cli.AbstractCliTest;
import net.sourceforge.plantuml.cli.CliFlag;
import net.sourceforge.plantuml.cli.CliOptions;
import net.sourceforge.plantuml.cli.CliParser;
import net.sourceforge.plantuml.cli.ExitStatus;

class ExportOnATextTest extends AbstractCliTest {

	@Test
	void shouldRenderBasicSequenceDiagram() throws Exception {
		final String input = String.join("\n", //
				"@startuml", //
				"a -> b", //
				"@enduml");
		final String expected = String.join("\n", //
				"     ,-.          ,-.", //
				"     |a|          |b|", //
				"     `+'          `+'", //
				"      |            | ", //
				"      |----------->| ", //
				"     ,+.          ,+.", //
				"     |a|          |b|", //
				"     `-'          `-'", //
				"");

		final String rendered = renderViaPipe(input);
		assertEqualsButControlChars(expected, rendered);
	}

	@Test
	void shouldRenderSingleClassDiagram() throws Exception {
		final String input = String.join("\n", //
				"@startuml", //
				"class a", //
				"@enduml");
		final String expected = String.join("\n", //
				",-.", //
				"|a|", //
				"|-|", //
				"`-'", "");

		final String rendered = renderViaPipe(input);
		assertEqualsButControlChars(expected, rendered);
	}

	@Test
	void shouldRenderSingleClassDiagramWithSmetanaLayout() throws Exception {
		final String input = String.join("\n", //
				"@startuml", //
				"!pragma layout smetana", //
				"class a", //
				"@enduml");
		final String expected = String.join("\n", //
				",-.", //
				"|a|", //
				"|-|", //
				"`-'", "");

		final String rendered = renderViaPipe(input);
		assertEqualsButControlChars(expected, rendered);
	}

	private static final String[] COMMON_OPTIONS = { "-txt" };

	private String renderViaPipe(String diagram) throws Exception {
		final CliOptions option = CliParser.parse(COMMON_OPTIONS);
		final ByteArrayInputStream bais = new ByteArrayInputStream(diagram.getBytes(UTF_8));
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final Pipe pipe = new Pipe(option, new PrintStream(baos), bais, option.getString(CliFlag.CHARSET));
		pipe.managePipe(ExitStatus.init());
		return new String(baos.toByteArray(), UTF_8);
	}
}
