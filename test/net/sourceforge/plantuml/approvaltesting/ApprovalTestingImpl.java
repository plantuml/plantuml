package net.sourceforge.plantuml.approvaltesting;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.notExists;
import static net.sourceforge.plantuml.StringUtils.substringAfterLast;
import static net.sourceforge.plantuml.test.Assertions.assertImagesEqual;
import static net.sourceforge.plantuml.test.TestUtils.readUtf8File;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.util.ExceptionUtils.throwAsUncheckedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.AssertionFailedError;

class ApprovalTestingImpl implements ApprovalTesting {

	private String className;
	private String displayName;
	private String extensionWithDot;
	private int maxFailures;
	private String methodName;
	private final SharedState sharedState;
	private String suffix;

	// Computed state
	private Path dir;
	private String baseName;

	private static class SharedState {
		final Set<String> approvedFilesUsed = new HashSet<>();
		final Map<String, Integer> failuresPerMethod = new HashMap<>();

		int getFailureCount(String methodName) {
			return failuresPerMethod.getOrDefault(methodName, 0);
		}

		int bumpFailureCount(String methodName) {
			return failuresPerMethod.compute(methodName, (k, v) -> (v == null) ? 1 : v + 1);
		}
	}

	ApprovalTestingImpl() {
		this.maxFailures = 10;
		this.sharedState = new SharedState();
		this.suffix = "";
	}

	private ApprovalTestingImpl(ApprovalTestingImpl other) {
		this.className = other.className;
		this.displayName = other.displayName;
		this.extensionWithDot = other.extensionWithDot;
		this.maxFailures = other.maxFailures;
		this.methodName = other.methodName;
		this.sharedState = other.sharedState;
		this.suffix = other.suffix;

		// nulling computed state here to avoid lint warnings
		this.baseName = null;
		this.dir = null;
	}

	ApprovalTestingImpl forExtensionContext(ExtensionContext context) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.className = context.getRequiredTestClass().getName();
		copy.displayName = context.getDisplayName();
		copy.methodName = context.getRequiredTestMethod().getName();
		return copy;
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
	public ApprovalTesting fail(FailCallback callback) {
		if (sharedState.bumpFailureCount(methodName) <= maxFailures) {
			try {
				callback.call(this);
			} catch (Exception e) {
				throwAsUncheckedException(e);
			}
		}
		return this;
	}

	@Override
	public void rethrow(Throwable t) {
		if (sharedState.getFailureCount(methodName) <= maxFailures) {
			throwAsUncheckedException(t);
		}

		final String message = "** APPROVAL FAILURE FILE(S) WERE SUPPRESSED ** " + t.getMessage();

		if (t instanceof AssertionFailedError) {
			final AssertionFailedError assertionFailedError = (AssertionFailedError) t;
			throw new AssertionFailedError(message, assertionFailedError.getExpected(), assertionFailedError.getActual(), t);
		}

		throwAsUncheckedException(new Throwable(message, t));
	}

	@Override
	public Path getDir() {
		if (dir == null) {
			dir = Paths.get("test", className.split("\\.")).getParent();
		}
		return dir;
	}

	@Override
	public Path getPathForApproved(String extraSuffix, String extensionWithDot) {
		return getDir().resolve(getBaseName() + extraSuffix + ".approved" + extensionWithDot);
	}

	@Override
	public Path getPathForFailed(String extraSuffix, String extensionWithDot) {
		return getDir().resolve(getBaseName() + extraSuffix + ".failed" + extensionWithDot);
	}

	@Override
	public ApprovalTestingImpl withExtension(String extensionWithDot) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.extensionWithDot = extensionWithDot;
		return copy;
	}

	@Override
	public ApprovalTesting withMaxFailures(int maxFailures) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.maxFailures = maxFailures;
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
		final String extension = extensionWithDot != null ? extensionWithDot : strategy.defaultExtensionWithDot();
		final Path approvedFile = getPathForApproved("", extension);
		final Path failedFile = getPathForFailed("", extension);

		final String approvedFilename = approvedFile.getFileName().toString();

		if (!sharedState.approvedFilesUsed.add(approvedFilename)) {
			throw new AssertionError(String.format(
					"The file '%s' is already used by this test class, please use withSuffix() to make a unique approval",
					approvedFilename
			));
		}

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
				fail(unused -> {
					createDirectories(failedFile.getParent());
					strategy.writeFile(value, failedFile);
				});
				rethrow(t);
			}
		} catch (IOException e) {
			throwAsUncheckedException(e);
		}
	}

	private String getBaseName() {
		if (baseName == null) {
			final StringBuilder b = new StringBuilder()
					.append(simplifyTestName(substringAfterLast(className, '.')))
					.append('.')
					.append(simplifyTestName(methodName));

			if (!displayName.equals(methodName + "()")) {
				b.append('.').append(simplifyTestName(displayName));
			}

			b.append(suffix);
			baseName = b.toString();
		}
		return baseName;
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
			final BufferedImage expected = ImageIO.read(approvedFile.toFile());
			assertImagesEqual(expected, value);
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
