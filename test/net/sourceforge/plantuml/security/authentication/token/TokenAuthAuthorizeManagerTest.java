package net.sourceforge.plantuml.security.authentication.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;

class TokenAuthAuthorizeManagerTest {

	/**
	 * Tests the creation of SecurityAuthentication via {@link TokenAuthAuthorizeManager}.
	 */
	@Test
	void createSimpleTest() {
		SecurityAuthorizeManager cut = new TokenAuthAuthorizeManager();

		Map<String, Object> properties = new HashMap<>();
		properties.put("headers.Authorization", "ApiKey a4db08b7-5729-4ba9-8c08-f2df493465a1");
		SecurityCredentials credentials = new SecurityCredentials("test", "token", null, null,
				properties, Proxy.NO_PROXY);

		SecurityAuthentication securityAuthentication = cut.create(credentials);

		assertThat(securityAuthentication).isNotNull();

		assertThat(securityAuthentication.isPublic()).isFalse();
		assertThat(securityAuthentication.getTokens())
				.containsEntry("headers.Authorization", "ApiKey a4db08b7-5729-4ba9-8c08-f2df493465a1");
	}
}