package net.sourceforge.plantuml.security.authentication.basicauth;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(securityAuthentication).isNotNull();

		assertThat(securityAuthentication.isPublic()).isFalse();
		assertThat(securityAuthentication.getTokens())
				.containsEntry("identifier", "alice")
				.containsEntry("secret", new char[]{'s', 'e', 'c', 'r', 'e', 't'});
	}
}