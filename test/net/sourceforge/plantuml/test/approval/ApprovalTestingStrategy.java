package net.sourceforge.plantuml.test.approval;

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

import javax.imageio.ImageIO;

class ApprovalTestingStrategy<T> {

	private static final String APPROVED_FILE_ALREADY_USED = "The '%s' file is already part of this test, " +
			"please use withSuffix() to make this approve() unique";

	interface Comparison<T> {
		void compare(T value, Path approvedFile) throws IOException, AssertionError;
	}

	interface FileWriter<T> {
		void write(T value, Path path) throws IOException;
	}

	private final Comparison<T> comparison;
	private final String defaultExtensionWithDot;
	private final FileWriter<T> fileWriter;

	ApprovalTestingStrategy(String defaultExtensionWithDot, Comparison<T> comparison, FileWriter<T> fileWriter) {
		this.comparison = comparison;
		this.defaultExtensionWithDot = defaultExtensionWithDot;
		this.fileWriter = fileWriter;
	}

	void approve(ApprovalTestingDsl dsl, T value) {
		final StringBuilder baseName = new StringBuilder()
				.append(simplifyTestName(substringAfterLast(dsl.getClassName(), '.')))
				.append('.')
				.append(simplifyTestName(dsl.getMethodName()));

		if (!dsl.getDisplayName().equals(dsl.getMethodName() + "()")) {
			baseName.append('.').append(simplifyTestName(dsl.getDisplayName()));
		}

		baseName.append(dsl.getSuffix());

		final String extension = dsl.getExtensionWithDot().orElse(defaultExtensionWithDot);
		final String approvedFilename = baseName + ".approved" + extension;
		final String failedFilename = baseName + ".failed" + extension;

		if (!dsl.getApprovedFilesUsed().add(approvedFilename)) {
			throw new AssertionError(String.format(APPROVED_FILE_ALREADY_USED, approvedFilename));
		}

		final Path dir = Paths.get("test", dsl.getClassName().split("\\.")).getParent();
		final Path approvedFile = dir.resolve(approvedFilename);
		final Path failedFile = dir.resolve(failedFilename);

		try {
			try {
				if (notExists(approvedFile)) {
					fileWriter.write(value, approvedFile);
					return;
				}
				comparison.compare(value, approvedFile);
				deleteIfExists(failedFile);
			} catch (Throwable e) {
				createDirectories(dir);
				fileWriter.write(value, failedFile);
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

	static final ApprovalTestingStrategy<BufferedImage> BUFFERED_IMAGE = new ApprovalTestingStrategy<>(
			".png",
			(value, approvedFile) ->
					org.assertj.swing.assertions.Assertions.assertThat(value).isEqualTo(ImageIO.read(approvedFile.toFile())),
			(value, path) -> {
				final String format = substringAfterLast(path.toString(), '.');
				boolean failed = !ImageIO.write(value, format, path.toFile());
				if (failed) throw new AssertionError(String.format("Failed to write image file '%s'", path));
			}
	);

	static final ApprovalTestingStrategy<String> STRING = new ApprovalTestingStrategy<>(
			".txt",
			(value, approvedFile) ->
					assertThat(value).isEqualTo(readUtf8File(approvedFile)),
			(value, path) ->
					writeUtf8File(path, value)
	);
}
