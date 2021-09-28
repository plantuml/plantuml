package net.sourceforge.plantuml.test.approval;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.isRegularFile;
import static net.sourceforge.plantuml.StringUtils.substringAfterLast;
import static net.sourceforge.plantuml.test.TestUtils.readUtf8File;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.assertj.swing.assertions.Assertions;

abstract class ApprovalTestingImpl<T> {

	private static final String APPROVED_DOES_NOT_EXIST = "The '%s' file does not exist";

	private static final String APPROVED_FILE_ALREADY_USED = "The '%s' file is already part of this test, " +
			"please use withSuffix() to make this approve() unique";

	void approve(ApprovalTestingDsl dsl, T value) {
		final StringBuilder b = new StringBuilder()
				.append(simplifyTestName(substringAfterLast(dsl.getClassName(), '.')))
				.append('.')
				.append(simplifyTestName(dsl.getMethodName()));

		if (!dsl.getDisplayName().equals(dsl.getMethodName() + "()")) {
			b.append('.').append(simplifyTestName(dsl.getDisplayName()));
		}

		b.append(dsl.getSuffix());

		final String baseName = b.toString();
		final String approvedFilename = baseName + ".approved" + extensionWithDot();
		final String failedFilename = baseName + ".failed" + extensionWithDot();

		if (!dsl.getApprovedFilesUsed().add(approvedFilename)) {
			throw new AssertionError(String.format(APPROVED_FILE_ALREADY_USED, approvedFilename));
		}

		final Path dir = Paths.get("test", dsl.getClassName().split("\\.")).getParent();
		final Path approvedFile = dir.resolve(approvedFilename);
		final Path failedFile = dir.resolve(failedFilename);

		try {
			try {
				if (!isRegularFile(approvedFile)) {
					throw new AssertionError(String.format(APPROVED_DOES_NOT_EXIST, approvedFile));
				}
				compare(value, approvedFile, failedFile);
				deleteIfExists(failedFile);
			} catch (Throwable e) {
				createDirectories(dir);
				writeFailedFile(value, failedFile);
				throw e;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static String simplifyTestName(String name) {
		return name
				.replaceAll("[^A-Za-z0-9]+", "_")
				.replaceAll("(^_+|_+$)", "");
	}

	abstract void compare(T value, Path approvedFile, Path failedFile) throws IOException, AssertionError;

	abstract String extensionWithDot();

	abstract void writeFailedFile(T value, Path failedFile) throws IOException;

	static final ApprovalTestingImpl<BufferedImage> BUFFERED_IMAGE = new ApprovalTestingImpl<BufferedImage>() {
		@Override
		void compare(BufferedImage value, Path approvedFile, Path failedFile) throws IOException, AssertionError {
			final BufferedImage approved = ImageIO.read(approvedFile.toFile());
			Assertions.assertThat(value).isEqualTo(approved);
		}

		@Override
		String extensionWithDot() {
			return ".png";
		}

		@Override
		void writeFailedFile(BufferedImage value, Path failedFile) throws IOException {
			ImageIO.write(value, "png", failedFile.toFile());
		}
	};

	static final ApprovalTestingImpl<String> STRING = new ApprovalTestingImpl<String>() {
		@Override
		void compare(String value, Path approvedFile, Path failedFile) throws IOException, AssertionError {
			final String approved = readUtf8File(approvedFile);
			assertThat(value).isEqualTo(approved);
		}

		@Override
		String extensionWithDot() {
			return ".txt";
		}

		@Override
		void writeFailedFile(String value, Path failedFile) throws IOException {
			writeUtf8File(failedFile, value);
		}
	};
}
