package net.sourceforge.plantuml.test.approval;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static net.sourceforge.plantuml.test.TestUtils.readUtf8File;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.assertj.swing.assertions.Assertions;

abstract class ApprovalTestingImpl<T> {

	void approve(ApprovalTestingDsl dsl, T value) {
		final String approvedFilename = dsl.getBaseName() + dsl.getSuffix() + ".approved" + extensionWithDot();
		final String failedFilename = dsl.getBaseName() + dsl.getSuffix() + ".failed" + extensionWithDot();

		if (!dsl.getApprovedFilesUsed().add(approvedFilename)) {
			throw new IllegalStateException("Already used '" + approvedFilename + "'");  // todo better message
		}

		final Path approvedFile = dsl.getDir().resolve(approvedFilename);
		final Path failedFile = dsl.getDir().resolve(failedFilename);

		try {
			try {
				assertThat(approvedFile).isNotEmptyFile();
				compare(value, approvedFile, failedFile);
				deleteIfExists(failedFile);
			} catch (AssertionError e) {
				createDirectories(dsl.getDir());
				writeFailedFile(value, failedFile);
				throw e;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
