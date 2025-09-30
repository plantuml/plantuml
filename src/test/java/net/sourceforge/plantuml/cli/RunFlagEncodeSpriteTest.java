package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagEncodeSpriteTest extends AbstractCliTest {

	@StdIo
	@Test
	void test1(StdOut out) throws Exception {
		final Path file = simplePngFile(tempDir);
		assertLs("[simple.png]", tempDir);

		Run.main(new String[] { "-encodesprite", "16", file.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("sprite $simple [40x30/16] {"));
		assertTrue(out.capturedString().contains("FFFFF0FFFFF"));

	}

	private Path simplePngFile(Path parent) throws IOException {
		final int width = 40;
		final int height = 30;
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		final Graphics2D g = image.createGraphics();

		// Fill background in black
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		// Draw white diagonal
		g.setColor(Color.WHITE);
		g.drawLine(0, 0, width - 1, height - 1);

		g.dispose();

		final Path file = parent.resolve("simple.png");
		ImageIO.write(image, "png", file.toFile());

		return file;
	}
}
