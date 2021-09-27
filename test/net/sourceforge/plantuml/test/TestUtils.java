package net.sourceforge.plantuml.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.SourceStringReader;

public class TestUtils {

	public static String renderAsUnicode(String source, String... options) throws Exception {

		final Option option = new Option(options);
		option.setFileFormatOption(new FileFormatOption(FileFormat.UTXT));

		final SourceStringReader ssr = new SourceStringReader(option.getDefaultDefines(), source, UTF_8.name(), option.getConfig());

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		ssr.getBlocks().get(0).getDiagram().exportDiagram(os, 0, option.getFileFormatOption());

		return new String(os.toByteArray(), UTF_8);
	}

	public static String renderUmlAsUnicode(String source, String... options) throws Exception {

		return renderAsUnicode("@startuml\n" + source + "\n@enduml\n", options);
	}

	public static Path testOutputDir(String name) throws IOException {
		return createDirectories(Paths.get("target/test-outputs").resolve(name));
	}
	
	public static String readUtf8File(Path path) throws IOException {

		return new String(readAllBytes(path), UTF_8);
	}
	
	public static void writeUtf8File(Path path, String string) throws IOException {

		Files.createDirectories(path.getParent());
		Files.write(path, string.getBytes(UTF_8));
	}
}
