package net.sourceforge.plantuml.test;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static net.sourceforge.plantuml.test.TestUtils.readUtf8File;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
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

	//
	// Public methods
	//

	public void approve(BufferedImage value) {
		approve(value, "");
	}

	public void approve(BufferedImage value, String suffix) {
		approveImpl(IMAGE_STRATEGY, value, suffix, ".png");
	}

	public void approve(String value) {
		approve(value, "");
	}

	public void approve(String value, String suffix) {
		approveImpl(STRING_STRATEGY, value, suffix, ".txt");
	}

	//
	// Internals
	//

	String baseName;
	Path dir;
	final Set<String> approvedFilesUsed = new HashSet<>();

	@Override
	public void beforeEach(ExtensionContext context) {
		final Class<?> klass = context.getRequiredTestClass();

		dir = Paths.get("test", klass.getPackage().getName().split("\\."));

		baseName = klass.getSimpleName() + "." + createTestName(context);
	}

	private String createTestName(ExtensionContext context) {
		final String displayName = context.getDisplayName();
		final int index = displayName.indexOf('(');
		return index == -1 ? displayName : displayName.substring(0, index);
	}

	private <T> void approveImpl(Strategy<T> strategy, T value, String suffix, String extensionWithDot) {
		final String approvedFilename = baseName + suffix + ".approved" + extensionWithDot;
		final String failedFilename = baseName + suffix + ".failed" + extensionWithDot;

		if (!approvedFilesUsed.add(approvedFilename)) {
			throw new IllegalStateException("Already used '" + approvedFilename + "'");  // todo better message when suffix empty
		}

		final Path approvedFile = dir.resolve(approvedFilename);
		final Path failedFile = dir.resolve(failedFilename);

		try {
			try {
				assertThat(approvedFile).isNotEmptyFile();
				strategy.compare(value, approvedFile, failedFile);
				deleteIfExists(failedFile);
			} catch (AssertionError e) {
				createDirectories(dir);
				strategy.writeFailedFile(value, failedFile);
				throw e;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private interface Strategy<T> {
		void compare(T value, Path approvedFile, Path failedFile) throws IOException, AssertionError;

		void writeFailedFile(T value, Path failedFile) throws IOException;
	}

	private final Strategy<BufferedImage> IMAGE_STRATEGY = new Strategy<BufferedImage>() {
		@Override
		public void compare(BufferedImage value, Path approvedFile, Path failedFile) throws IOException, AssertionError {
			final BufferedImage approved = ImageIO.read(approvedFile.toFile());
			assertThat(value).isEqualTo(approved);
		}

		@Override
		public void writeFailedFile(BufferedImage value, Path failedFile) throws IOException {
			ImageIO.write(value, "png", failedFile.toFile());
		}
	};

	private final Strategy<String> STRING_STRATEGY = new Strategy<String>() {
		@Override
		public void compare(String value, Path approvedFile, Path failedFile) throws IOException, AssertionError {
			final String approved = readUtf8File(approvedFile);
			assertThat(value).isEqualTo(approved);
		}

		@Override
		public void writeFailedFile(String value, Path failedFile) throws IOException {
			writeUtf8File(failedFile, value);
		}
	};
}
