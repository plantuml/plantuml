package net.sourceforge.plantuml.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.security.authentication.SecurityCredentials;

/**
 * Checks some aspects in {@link SecurityUtils}.
 */
class SecurityUtilsTest {

	private static final String EXT = ".credential";

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
	 * Checks, if SecurityUtils can loadSecurityCredentials.
	 *
	 * @throws Exception nobody wants an exception
	 */
	@Test
	void testLoadOfSecurityCredentials() throws Exception {
		File secureFolder = tempDir.toFile();
		System.setProperty(SecurityUtils.PATHS_SECURITY, secureFolder.getCanonicalPath());

		// A file is needed:
		File secretFile = File.createTempFile("user", EXT, secureFolder);

		String jsonProxy = "\"proxy\": {\"type\": \"socks\", \"address\": \"192.168.92.250\", \"port\":8080}";
		String jsonCredentials = "{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"secret\"" +
				", " + jsonProxy + "}";

		Files.write(secretFile.toPath(), jsonCredentials.getBytes(StandardCharsets.UTF_8));

		assertThat(secretFile).describedAs("File should be existing with content")
				.exists().isNotEmpty();

		assertThat(SecurityUtils.getSecurityPath()).isNotNull();

		String secretFileName = secretFile.getName();

		SecurityCredentials credentials = SecurityUtils.loadSecurityCredentials(
				secretFileName.substring(0, secretFileName.length() - EXT.length()));

		assertThat(credentials).isNotNull();

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isEqualTo(new char[]{'s', 'e', 'c', 'r', 'e', 't'});

		assertThat(credentials.getProxy()).isNotNull();
		Proxy proxy = credentials.getProxy();
		assertThat(proxy.type()).isEqualTo(Proxy.Type.SOCKS);

		assertThat(proxy.address()).isNotNull();
		assertThat(proxy.address()).isInstanceOf(InetSocketAddress.class);

		InetSocketAddress address = (InetSocketAddress) proxy.address();
		assertThat(address.getPort()).isEqualTo(8080);
		assertThat(address.getHostString()).isEqualTo("192.168.92.250");
	}

	/**
	 * Tests unsecure names.
	 *
	 * @param name name of filepart
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"_unsecure", "unse%cure", "  unsecure04343  ",
			"tooLong012345678901234567890123456789012345678901234567890123456789unsecure"
	})
	void testUnsecureNames(String name) throws IOException {
		File secureFolder = tempDir.toFile();
		System.setProperty(SecurityUtils.PATHS_SECURITY, secureFolder.getCanonicalPath());

		// A file is needed:
		File secretFile = File.createTempFile(name, EXT, secureFolder);

		String jsonProxy = "\"proxy\": {\"type\": \"socks\", \"address\": \"192.168.92.250\", \"port\":8080}";
		String jsonCredentials = "{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"secret\"" +
				", " + jsonProxy + "}";

		Files.write(secretFile.toPath(), jsonCredentials.getBytes(StandardCharsets.UTF_8));

		assertThat(secretFile).describedAs("File should be existing with content")
				.exists().isNotEmpty();

		String secretFileName = secretFile.getName();

		SecurityCredentials credentials = SecurityUtils.loadSecurityCredentials(
				secretFileName.substring(0, secretFileName.length() - EXT.length()));

		assertThat(credentials).isEqualTo(SecurityCredentials.NONE);
	}


	/**
	 * Tests secure names.
	 *
	 * @param name name of filepart
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"secure", "Secure", "123secure", "secure123", "1290565234", "45435-543534-fdgfdg"
	})
	void testSecureNames(String name) throws IOException {
		File secureFolder = tempDir.toFile();
		System.setProperty(SecurityUtils.PATHS_SECURITY, secureFolder.getCanonicalPath());

		// A file is needed:
		File secretFile = File.createTempFile(name, EXT, secureFolder);

		String jsonProxy = "\"proxy\": {\"type\": \"socks\", \"address\": \"192.168.92.250\", \"port\":8080}";
		String jsonCredentials = "{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"secret\"" +
				", " + jsonProxy + "}";

		Files.write(secretFile.toPath(), jsonCredentials.getBytes(StandardCharsets.UTF_8));

		assertThat(secretFile).describedAs("File should be existing with content")
				.exists().isNotEmpty();

		String secretFileName = secretFile.getName();

		SecurityCredentials credentials = SecurityUtils.loadSecurityCredentials(
				secretFileName.substring(0, secretFileName.length() - EXT.length()));

		assertThat(credentials).isNotEqualTo(SecurityCredentials.NONE);
	}


}
