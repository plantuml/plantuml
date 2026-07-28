package net.sourceforge.plantuml.security.authentication.basicauth;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;

class BasicAuthAuthorizeManagerTest {

	/**
	 * Tests the creation of SecurityAuthentication via BasicAuthAuthorizeManager.
	 */
	@Test
	void createTest() {
		SecurityAuthorizeManager cut = new BasicAuthAuthorizeManager();

		SecurityAuthentication securityAuthentication = cut.create(
				SecurityCredentials.basicAuth("alice", new char[]{'s', 'e', 'c', 'r', 'e', 't'}));

		assertNotNull(securityAuthentication);

		assertFalse(securityAuthentication.isPublic());
		assertEquals("alice", securityAuthentication.getTokens().get("identifier")); assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, (char[]) securityAuthentication.getTokens().get("secret"));
	}
}