package net.sourceforge.plantuml.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SecurityDefaultNoopAuthorizeManagerTest {

	/**
	 * Tests the creation of SecurityAuthentication via SecurityDefaultNoopAuthenticationInterceptor.
	 */
	@Test
	void createTest() {
		SecurityAuthorizeManager cut = new SecurityDefaultNoopAuthorizeManager();

		SecurityAuthentication securityAuthentication = cut.create(null);

		assertThat(securityAuthentication).isNotNull();

		assertThat(securityAuthentication.isPublic()).isTrue();
	}
}