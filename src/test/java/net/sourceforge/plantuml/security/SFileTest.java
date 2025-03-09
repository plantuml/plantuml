package net.sourceforge.plantuml.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests some features of {@link SFile}.
 */
class SFileTest {

	private static String oldSecurity;

	@TempDir
	Path tempDir;

	@BeforeAll
	static void storeSecurityProperty() {
		oldSecurity = System.getProperty(SecurityUtils.PATHS_SECURITY);
	}

	@AfterAll
	static void loadSecurityProperty() {
		if (oldSecurity != null) {
			System.setProperty(SecurityUtils.PATHS_SECURITY, oldSecurity);
		} else {
			System.getProperties().remove(SecurityUtils.PATHS_SECURITY);
		}
	}

	/**
	 * Checks, if we cannot see a secret file in a security folder.
	 *
	 * @throws Exception Hopefully not
	 */
	@Test
	void testFileDenied() throws Exception {
		File secureFolder = tempDir.toFile();
		System.setProperty(SecurityUtils.PATHS_SECURITY, secureFolder.getCanonicalPath());

		// A file is needed:
		File secretFile = File.createTempFile("user", ".credentials", secureFolder);

		assertThat(secretFile).describedAs("File should be visible with standard java.io.File").exists();

		SFile file = new SFile(secretFile.getAbsolutePath());

		assertThat(file.exists()).describedAs("File should be invisible for SFile").isFalse();
	}
}
