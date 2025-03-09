package net.sourceforge.plantuml.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks some security features
 */
class SURLTest {

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
	 * Checks a SURL without a Security context.
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"http://localhost:8080/api",
			"http://alice@localhost:8080/api",
			"http://alice:secret@localhost:8080/api",
			"https://localhost:8080/api",
			"https://alice@localhost:8080/api",
			"https://alice:secret@localhost:8080/api"})
	void urlWithoutSecurity(String url) {
		SURL surl = SURL.create(url);

		assertThat(surl).isNotNull();
		assertThat(surl.isAuthorizationConfigured()).isFalse();

		assertThat(surl).describedAs("URL should be untouched")
				.hasToString(url);
	}

	/**
	 * Checks a SURL after removing the UserInfo part.
	 *
	 * @throws MalformedURLException this should not be happened
	 * @throws URISyntaxException should not happen
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"http://localhost:8080/api",
			"http://alice@localhost:8080/api",
			"http://alice_secret@localhost:8080/api",
			"https://localhost:8080/api",
			"https://alice@localhost:8080/api",
			"https://alice_secret@localhost:8080/api"})
	void removeUserInfo(String url) throws MalformedURLException, URISyntaxException {
		SURL surl = SURL.createWithoutUser(new URI(url).toURL());

		assertThat(surl).isNotNull();
		assertThat(surl.isAuthorizationConfigured()).isFalse();
		// Check http and https and removed UserInfo part
		assertThat(surl.toString()).describedAs("User info should be removed from URL")
				.startsWith("http").endsWith("://localhost:8080/api");
	}

	/**
	 * Checks a SURL without a Security context.
	 *
	 * @throws Exception please not
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"http://bob@localhost:8080/api",
			"https://bob@localhost:8080/api"})
	void urlWithSecurity(String url) throws Exception {

		File secureFolder = tempDir.toFile();
		System.setProperty(SecurityUtils.PATHS_SECURITY, secureFolder.getCanonicalPath());

		// A credential file is needed:
		File secretFile = new File(secureFolder, "bob" + EXT);

		String jsonProxy = "\"proxy\": {\"type\": \"socks\", \"address\": \"192.168.92.250\", \"port\":8080}";
		String jsonCredentials = "{\"name\": \"bob\", \"identifier\": \"bob\", \"secret\": \"bobssecret\"" +
				", " + jsonProxy + "}";

		Files.write(secretFile.toPath(), jsonCredentials.getBytes(StandardCharsets.UTF_8));

		// pre-check, if test can start
		assertThat(secretFile).describedAs("File should be existing with content")
				.exists().isNotEmpty();

		assertThat(SecurityUtils.getSecurityPath()).isNotNull();

		// Our test goes here
		SURL surl = SURL.create(url);

		assertThat(surl).isNotNull();
		assertThat(surl.isAuthorizationConfigured()).isTrue();

		assertThat(surl.toString()).describedAs("User info should be removed from URL")
				.startsWith("http").endsWith("://localhost:8080/api");

		secretFile.delete();
	}
}
