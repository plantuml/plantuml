package net.sourceforge.plantuml.approvaltesting;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.notExists;
import static net.sourceforge.plantuml.StringUtils.substringAfterLast;
import static net.sourceforge.plantuml.test.TestUtils.readUtf8File;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.extension.ExtensionContext;


class ApprovalTestingImpl implements ApprovalTesting {

	private final Set<String> approvedFilesUsed;
	private final String className;
	private final String displayName;
	private String extensionWithDot;
	private final String methodName;
	private String suffix;

	ApprovalTestingImpl(ExtensionContext context, Set<String> approvedFilesUsed) {
		this.approvedFilesUsed = approvedFilesUsed;
		this.className = context.getRequiredTestClass().getName();
		this.displayName = context.getDisplayName();
		this.extensionWithDot = null;
		this.methodName = context.getRequiredTestMethod().getName();
		this.suffix = "";
	}

	private ApprovalTestingImpl(ApprovalTestingImpl other) {
		this.approvedFilesUsed = other.approvedFilesUsed;
		this.className = other.className;
		this.displayName = other.displayName;
		this.extensionWithDot = other.extensionWithDot;
		this.methodName = other.methodName;
		this.suffix = other.suffix;
	}

	//
	// Implement ApprovalTesting
	//

	@Override
	public ApprovalTestingImpl approve(BufferedImage value) {
		approve(BUFFERED_IMAGE_STRATEGY, value);
		return this;
	}

	@Override
	public ApprovalTestingImpl approve(String value) {
		approve(STRING_STRATEGY, value);
		return this;
	}

	@Override
	public Path createOutputPath(String suffix) {
		return getDir().resolve(getBaseName() + suffix);
	}

	@Override
	public String getBaseName() {
		final StringBuilder b = new StringBuilder()
				.append(simplifyTestName(substringAfterLast(className, '.')))
				.append('.')
				.append(simplifyTestName(methodName));

		if (!displayName.equals(methodName + "()")) {
			b.append('.').append(simplifyTestName(displayName));
		}

		b.append(suffix);
		return b.toString();
	}

	@Override
	public Path getDir() {
		return Paths.get("test", className.split("\\.")).getParent();
	}

	@Override
	public ApprovalTestingImpl withExtension(String extensionWithDot) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.extensionWithDot = extensionWithDot;
		return copy;
	}

	@Override
	public ApprovalTestingImpl withSuffix(String suffix) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.suffix = suffix;
		return copy;
	}

	//
	// Internals
	//

	private <T> void approve(Strategy<T> strategy, T value) {
		final String baseName = getBaseName();
		final String extension = extensionWithDot == null ? strategy.defaultExtensionWithDot() : extensionWithDot;
		final String approvedFilename = baseName + ".approved" + extension;
		final String failedFilename = baseName + ".failed" + extension;

		if (!approvedFilesUsed.add(approvedFilename)) {
			throw new AssertionError(String.format(
					"The file '%s' is already used by this test class, please use withSuffix() to make a unique approval",
					approvedFilename
			));
		}

		final Path dir = getDir();
		final Path approvedFile = dir.resolve(approvedFilename);
		final Path failedFile = dir.resolve(failedFilename);

		try {
			if (notExists(approvedFile)) {
				createDirectories(approvedFile.getParent());
				strategy.writeFile(value, approvedFile);
				return;
			}

			try {
				strategy.compare(value, approvedFile);
				deleteIfExists(failedFile);
			} catch (Throwable t) {
				createDirectories(failedFile.getParent());
				strategy.writeFile(value, failedFile);
				throw t;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Visible for testing
	static String simplifyTestName(String name) {
		return name
				.replaceAll("[^A-Za-z0-9]+", "_")
				.replaceAll("(^_+|_+$)", "");
	}

	//
	// Strategies
	//

	private interface Strategy<T> {
		void compare(T value, Path approvedFile) throws IOException, AssertionError;

		String defaultExtensionWithDot();

		void writeFile(T value, Path path) throws IOException;
	}

	private static final Strategy<BufferedImage> BUFFERED_IMAGE_STRATEGY = new Strategy<BufferedImage>() {

		@Override
		public void compare(BufferedImage value, Path approvedFile) throws IOException, AssertionError {
			final BufferedImage approved = ImageIO.read(approvedFile.toFile());
			org.assertj.swing.assertions.Assertions.assertThat(value)
					.isEqualTo(approved);
		}

		@Override
		public String defaultExtensionWithDot() {
			return ".png";
		}

		@Override
		public void writeFile(BufferedImage value, Path path) throws IOException {
			final String format = substringAfterLast(path.toString(), '.');
			boolean failed = !ImageIO.write(value, format, path.toFile());
			if (failed) throw new RuntimeException(String.format("Failed to write image file '%s'", path));
		}
	};

	private static final Strategy<String> STRING_STRATEGY = new Strategy<String>() {

		@Override
		public void compare(String value, Path approvedFile) throws IOException, AssertionError {
			final String approved = readUtf8File(approvedFile);
			assertThat(value)
					.isEqualTo(approved);
		}

		@Override
		public String defaultExtensionWithDot() {
			return ".txt";
		}

		@Override
		public void writeFile(String value, Path path) throws IOException {
			writeUtf8File(path, value);
		}
	};
}
