package net.sourceforge.plantuml.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Checks SecurityAuthentication.
 */
class SecurityAuthenticationTest {

	private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

	@Test
	void isPublicAsPublicTest() {
		SecurityAuthentication cut = new SecurityAuthentication("public", null, null, EMPTY_MAP);

		assertThat(cut).isNotNull();
		assertThat(cut.isPublic()).isTrue();
	}

	@Test
	void isPublicAsBasicAuthTest() {
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, EMPTY_MAP);

		assertThat(cut.isPublic()).isFalse();
	}

	@Test
	void getTokensTest() {
		Map<String, Object> tokens = new HashMap<>();
		tokens.put("identifier", "alice");
		tokens.put("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, tokens);

		assertThat(cut.getTokens())
				.containsEntry("identifier", "alice")
				.containsEntry("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
	}

	@Test
	void eraseCredentialsTest() {
		Map<String, Object> tokens = new HashMap<>();
		tokens.put("identifier", "alice");
		tokens.put("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, tokens);

		assertThat(cut.getTokens())
				.containsEntry("identifier", "alice")
				.containsEntry("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});

		cut.eraseCredentials();
		assertThat(cut.getTokens()).isEmpty();
	}
}