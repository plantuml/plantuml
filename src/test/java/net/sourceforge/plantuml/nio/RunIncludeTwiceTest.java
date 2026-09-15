package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.cli.AbstractCliTest;

/**
 * A file reached twice through {@code !include} is only included once, even
 * when the two paths leading to it are spelled differently, while
 * {@code !include_many} still includes it every time.
 */
class RunIncludeTwiceTest extends AbstractCliTest {

	@Test
	void include_skips_a_file_already_included() throws IOException, InterruptedException {
		assertEquals(1, countLinks("!include"));
	}

	@Test
	void include_many_includes_a_file_every_time() throws IOException, InterruptedException {
		assertEquals(2, countLinks("!include_many"));
	}

	private int countLinks(String directive) throws IOException, InterruptedException {
		Files.createDirectories(tempDir.resolve("one"));
		Files.createDirectories(tempDir.resolve("two"));
		write("common.puml", "class a", "class b", "a <|-- b");
		write("one/first.puml", directive + " ../common.puml", "class c");
		write("two/second.puml", directive + " ../common.puml", "class d");
		final Path main = write("main.puml", "@startuml", "!include one/first.puml", "!include two/second.puml",
				"@enduml");

		Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

		final String svg = Files.readString(tempDir.resolve("main.svg"), StandardCharsets.UTF_8);
		return svg.split("class=\"link\"", -1).length - 1;
	}

	private Path write(String name, String... lines) throws IOException {
		final Path file = tempDir.resolve(name);
		Files.writeString(file, String.join(System.lineSeparator(), lines), StandardCharsets.UTF_8);
		return file;
	}
}
