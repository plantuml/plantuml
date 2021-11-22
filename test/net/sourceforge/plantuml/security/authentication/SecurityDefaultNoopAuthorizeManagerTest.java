package net.sourceforge.plantuml.security.authentication;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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