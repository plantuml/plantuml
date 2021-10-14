package net.sourceforge.plantuml.test;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

	public static void writeUtf8File(Path path, String string) throws IOException {

		Files.createDirectories(path.getParent());
		Files.write(path, string.getBytes(UTF_8));
	}
}
