package net.sourceforge.plantuml.security.authentication;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

		assertNotNull(cut);
		assertTrue(cut.isPublic());
	}

	@Test
	void isPublicAsBasicAuthTest() {
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, EMPTY_MAP);

		assertFalse(cut.isPublic());
	}

	@Test
	void getTokensTest() {
		Map<String, Object> tokens = new HashMap<>();
		tokens.put("identifier", "alice");
		tokens.put("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, tokens);

		assertEquals("alice", cut.getTokens().get("identifier")); assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, (char[]) cut.getTokens().get("secret"));
	}

	@Test
	void eraseCredentialsTest() {
		Map<String, Object> tokens = new HashMap<>();
		tokens.put("identifier", "alice");
		tokens.put("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		SecurityAuthentication cut = new SecurityAuthentication("basicauth", null, null, tokens);

		assertEquals("alice", cut.getTokens().get("identifier")); assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, (char[]) cut.getTokens().get("secret"));

		cut.eraseCredentials();
		assertTrue(cut.getTokens().isEmpty());
	}
}