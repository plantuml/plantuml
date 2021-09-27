package net.sourceforge.plantuml.test;

import static java.nio.file.Files.deleteIfExists;
import static org.assertj.swing.assertions.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.approvaltests.core.Options;
import org.approvaltests.namer.ApprovalNamer;

public class ApprovalTesting {

	public static void approveImage(BufferedImage image) {
		final ApprovalNamer namer = new Options().forFile().getNamer();
		final File approvedFile = namer.getApprovedFile(".png");
		final File receivedFile = namer.getReceivedFile(".png");

		try {
			try {
				assertThat(approvedFile)
						.exists()
						.isFile();

				final BufferedImage approvedImage = ImageIO.read(approvedFile);

				assertThat(image)
						.isEqualTo(approvedImage);

				deleteIfExists(receivedFile.toPath());

			} catch (AssertionError e) {
				ImageIO.write(image, "png", receivedFile);
				throw e;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
