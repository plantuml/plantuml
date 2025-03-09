package net.sourceforge.plantuml.security.authentication.oauth;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(responseMap)
				.containsEntry(OAuth2Tokens.ACCESS_TOKEN.key(), "7fea8201-eebb-4101-a76f-ddc1efdd3bbd")
				.containsEntry(OAuth2Tokens.SCOPE.key(), "read write")
				.containsEntry(OAuth2Tokens.TOKEN_TYPE.key(), "bearer")
				.containsEntry(OAuth2Tokens.EXPIRES_IN.key(), 300);
	}

	@ParameterizedTest
	@ValueSource(strings = {"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\"," +
			"\"token_type\":\"bearer\",\"expires_in\":300}",
			"{\"access_token\":\"7fea8201-eebb-4101-a76f-ddc1efdd3bbd\",\"scope\":\"read write\",\"expires_in\":300}"
	})
	void accessDataOverrideTokenTypeTest(String jsonResponse) {
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), "apikey");

		assertThat(responseMap)
				.containsEntry(OAuth2Tokens.ACCESS_TOKEN.key(), "7fea8201-eebb-4101-a76f-ddc1efdd3bbd")
				.containsEntry(OAuth2Tokens.SCOPE.key(), "read write")
				.containsEntry(OAuth2Tokens.TOKEN_TYPE.key(), "apikey")
				.containsEntry(OAuth2Tokens.EXPIRES_IN.key(), 300);
	}

	@Test
	void accessDataEmptyTest() {
		String jsonResponse = "{}";
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), null);

		assertThat(responseMap).as("Empty map should not contain default token-type 'bearer'").isEmpty();
	}

	@Test
	void accessDataEmptyAndTokenOverrideTest() {
		String jsonResponse = "{}";
		JsonValue response = Json.parse(jsonResponse);
		Map<String, Object> responseMap = cut.buildAccessDataFromResponse(response.asObject(), "apikey");

		assertThat(responseMap).as("Empty map should not contain override token-type 'apikey'").isEmpty();
	}

	@Test
	void urlEncodeTest() {
		assertThat(cut.urlEncode("alice")).isEqualTo("alice");
		assertThat(cut.urlEncode("bob")).isEqualTo("bob");
		assertThat(cut.urlEncode("alice and bob")).isEqualTo("alice+and+bob");
		assertThat(cut.urlEncode("Müller")).isEqualTo("M%C3%BCller");
		assertThat(cut.urlEncode("s?ecret=-110%")).isEqualTo("s%3Fecret%3D-110%25");
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