package net.sourceforge.plantuml.test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static net.sourceforge.plantuml.FileFormat.BUFFERED_IMAGE;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.api.ImageDataBufferedImage;

public class TestUtils {

	public static byte[] exportOneDiagramToByteArray(String source, FileFormat fileFormat, String... options) throws Exception {

		final Option option = new Option(options);
		option.setFileFormatOption(new FileFormatOption(fileFormat));

		final SourceStringReader ssr = new SourceStringReader(option.getDefaultDefines(), source, UTF_8.name(), option.getConfig());

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		ssr.getBlocks().get(0).getDiagram().exportDiagram(os, 0, option.getFileFormatOption());

		return os.toByteArray();
	}

	public static BufferedImage renderAsImage(String... source) throws Exception {

		final Option option = new Option(new String[]{});
		option.setFileFormatOption(new FileFormatOption(BUFFERED_IMAGE));

		final SourceStringReader ssr = new SourceStringReader(option.getDefaultDefines(), String.join("\n", source), UTF_8.name(), option.getConfig());

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		ImageDataBufferedImage imageData = (ImageDataBufferedImage) ssr.getBlocks().get(0).getDiagram().exportDiagram(os, 0, option.getFileFormatOption());

		return imageData.getImage();
	}

	public static String renderAsUnicode(String source, String... options) throws Exception {

		final byte[] bytes = exportOneDiagramToByteArray(source, FileFormat.UTXT, options);
		return new String(bytes, UTF_8);
	}

	public static String renderUmlAsUnicode(String source, String... options) throws Exception {

		return renderAsUnicode("@startuml\n" + source + "\n@enduml\n", options);
	}

	public static String readUtf8File(Path path) throws IOException {

		return new String(readAllBytes(path), UTF_8);
	}

	public static void writeUtf8File(Path path, String string) throws IOException {

		Files.createDirectories(path.getParent());
		Files.write(path, string.getBytes(UTF_8));
	}
}
