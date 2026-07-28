package net.sourceforge.plantuml.security.authentication.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;

class AbstractOAuth2AccessAuthorizeManagerTest {

	private final MockedOAuth2AccessAuthorizeManager cut = new MockedOAuth2AccessAuthorizeManager();

	@ParameterizedTest
	@ValueSource(strings = {"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\"," +
			"\"token_type\":\"bearer\",\"expires_in\":300}",
			"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\",\"expires_in\":300}"
	})
	void accessDataTest(String jsonResponse) {
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), null);

		assertEquals("7fea8201-eebb-4101-a76f-ddc1efdd3bbd", responseMap.get(OAuth2Tokens.ACCESS_TOKEN.key())); assertEquals("read write", responseMap.get(OAuth2Tokens.SCOPE.key())); assertEquals("bearer", responseMap.get(OAuth2Tokens.TOKEN_TYPE.key())); assertEquals(300, responseMap.get(OAuth2Tokens.EXPIRES_IN.key()));
	}

	@ParameterizedTest
	@ValueSource(strings = {"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\"," +
			"\"token_type\":\"bearer\",\"expires_in\":300}",
			"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\",\"expires_in\":300}"
	})
	void accessDataOverrideTokenTypeTest(String jsonResponse) {
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), "apikey");

		assertEquals("7fea8201-eebb-4101-a76f-ddc1efdd3bbd", responseMap.get(OAuth2Tokens.ACCESS_TOKEN.key())); assertEquals("read write", responseMap.get(OAuth2Tokens.SCOPE.key())); assertEquals("apikey", responseMap.get(OAuth2Tokens.TOKEN_TYPE.key())); assertEquals(300, responseMap.get(OAuth2Tokens.EXPIRES_IN.key()));
	}

	@Test
	void accessDataEmptyTest() {
		String jsonResponse = "{}";
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), null);

		assertTrue(responseMap.isEmpty());
	}

	@Test
	void accessDataEmptyAndTokenOverrideTest() {
		String jsonResponse = "{}";
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), "apikey");

		assertTrue(responseMap.isEmpty());
	}

	@Test
	void urlEncodeTest() {
		assertEquals("alice", cut.urlEncode("alice"));
		assertEquals("bob", cut.urlEncode("bob"));
		assertEquals("alice+and+bob", cut.urlEncode("alice and bob"));
		assertEquals("M%C3%BCller", cut.urlEncode("Müller"));
		assertEquals("s%3Fecret%3D-110%25", cut.urlEncode("s?ecret=-110%"));
	}

	/**
	 * Mock to make methods public for testing.
	 */
	static class MockedOAuth2AccessAuthorizeManager extends AbstractOAuth2AccessAuthorizeManager {

		@Override
		public SecurityAuthentication create(SecurityCredentials credentials) {
			return null;
		}

		@Override
		public Map<String, Object> buildAccessDataFromResponse(JsonObject tokenResponse, String overrideTokenType) {
			return super.buildAccessDataFromResponse(tokenResponse, overrideTokenType);
		}

		@Override
		public String urlEncode(String data) {
			return super.urlEncode(data);
		}
	}
}