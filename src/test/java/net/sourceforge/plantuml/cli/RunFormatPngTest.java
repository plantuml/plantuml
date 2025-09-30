package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatPngTest extends AbstractCliTest {

	@Test
	void testPng() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-tpng", file.toAbsolutePath().toString() });

		assertLs("[test.png, test.txt]", tempDir);

		final Path pngFile = tempDir.resolve("test.png");
		assertTrue(Files.exists(pngFile));

		final BufferedImage bufferedImage = ImageIO.read(pngFile.toFile());
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		assertTrue(width > 10);
		assertTrue(height > 10);

	}

}
