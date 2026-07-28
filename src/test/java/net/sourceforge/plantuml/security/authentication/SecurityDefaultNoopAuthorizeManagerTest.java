package net.sourceforge.plantuml.security.authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

class SecurityDefaultNoopAuthorizeManagerTest {

	/**
	 * Tests the creation of SecurityAuthentication via SecurityDefaultNoopAuthenticationInterceptor.
	 */
	@Test
	void createTest() {
		SecurityAuthorizeManager cut = new SecurityDefaultNoopAuthorizeManager();

		SecurityAuthentication securityAuthentication = cut.create(null);

		assertNotNull(securityAuthentication);

		assertTrue(securityAuthentication.isPublic());
	}
}