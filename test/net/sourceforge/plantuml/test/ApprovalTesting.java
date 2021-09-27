package net.sourceforge.plantuml.test;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static org.assertj.swing.assertions.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;


public class ApprovalTesting implements Extension, BeforeEachCallback {

	String baseName;
	Path sourceDir;
	final Set<String> suffixUsed = new HashSet<>();

	@Override
	public void beforeEach(ExtensionContext context) {
		final Class<?> klass = context.getRequiredTestClass();

		sourceDir = Paths.get("test", klass.getPackage().getName().split("\\."));

		baseName = klass.getSimpleName() + "." + createTestName(context);
	}

	public void approve(BufferedImage image) {
		approve(image, "");
	}

	public void approve(BufferedImage image, String suffix) {
		if (!suffixUsed.add(suffix)) {
			throw new IllegalStateException("Already used suffix '" + suffix + "'");  // todo better message when suffix empty
		}

		final Path approvedFile = sourceDir.resolve(baseName + suffix + ".approved.png");
		final Path failedFile = sourceDir.resolve(baseName + suffix + ".failed.png");

		try {
			try {
				assertThat(approvedFile)
						.isNotEmptyFile();

				final BufferedImage approvedImage = ImageIO.read(approvedFile.toFile());

				assertThat(image)
						.isEqualTo(approvedImage);

				deleteIfExists(failedFile);

			} catch (AssertionError e) {
				createDirectories(sourceDir);  // in case the dir no longer exists!
				ImageIO.write(image, "png", failedFile.toFile());
				throw e;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String createTestName(ExtensionContext context) {
		final String displayName = context.getDisplayName();
		final int index = displayName.indexOf('(');
		return index == -1 ? displayName : displayName.substring(0, index);
	}
}
