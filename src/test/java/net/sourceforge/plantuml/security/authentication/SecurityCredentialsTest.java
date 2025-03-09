package net.sourceforge.plantuml.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * Tests for {@link SecurityCredentials}.
 */
class SecurityCredentialsTest {

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON.
	 *
	 * @throws Exception hopefully not
	 */
	@Test
	void fromJsonTestComplete() throws Exception {
		JsonValue jsonValue =
				Json.parse("{\"name\": \"jenkins\", \"type\": \"basicauth\", " +
						"\"identifier\": \"alice\", \"secret\": \"secret\"}");
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getType()).isEqualTo("basicauth");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isEqualTo(new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		assertThat(credentials.getProperties()).isEmpty();

		assertThat(credentials.getProxy()).isNull();
	}

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON with direct access.
	 *
	 * @throws Exception hopefully not
	 */
	@Test
	void fromJsonTestCompleteWithProxyDirect() throws Exception {
		String jsonProxy = "\"proxy\": {\"type\": \"direct\"}";
		JsonValue jsonValue =
				Json.parse("{\"name\": \"jenkins\", \"type\": \"basicauth\", " +
						"\"identifier\": \"alice\", \"secret\": \"secret\"" +
						", " + jsonProxy + "}");
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getType()).isEqualTo("basicauth");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isEqualTo(new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		assertThat(credentials.getProperties()).isEmpty();

		assertThat(credentials.getProxy()).isNotNull();
		Proxy proxy = credentials.getProxy();
		assertThat(proxy.type()).isEqualTo(Proxy.Type.DIRECT);
		assertThat(proxy.address()).isNull();
	}

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON with socket proxy.
	 *
	 * @throws Exception hopefully not
	 */
	@Test
	void fromJsonTestCompleteWithProxySocksAddress() throws Exception {
		String jsonProxy = "\"proxy\": {\"type\": \"socks\", \"address\": \"192.168.92.250\", \"port\":8080}";
		JsonValue jsonValue =
				Json.parse("{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"secret\"" +
						", " + jsonProxy + "}");
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getType()).as("basicauth should be the default").isEqualTo("basicauth");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isEqualTo(new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		assertThat(credentials.getProperties()).isEmpty();

		assertThat(credentials.getProxy()).isNotNull();
		Proxy proxy = credentials.getProxy();
		assertThat(proxy.type()).isEqualTo(Proxy.Type.SOCKS);

		assertThat(proxy.address()).isNotNull();
		assertThat(proxy.address()).isInstanceOf(InetSocketAddress.class);

		InetSocketAddress address = (InetSocketAddress) proxy.address();
		assertThat(address.getPort()).isEqualTo(8080);
		assertThat(address.getHostString()).isEqualTo("192.168.92.250");
	}

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON with http-high-level proxy.
	 *
	 * @throws Exception hopefully not
	 */
	@Test
	void fromJsonTestCompleteWithProxyHttpAddress() throws Exception {
		String jsonProxy = "\"proxy\": {\"type\": \"http\", \"address\": \"proxy.example.com\", \"port\":8080}";
		JsonValue jsonValue =
				Json.parse("{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"secret\"" +
						", " + jsonProxy + "}");
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getType()).as("basicauth should be the default").isEqualTo("basicauth");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isEqualTo(new char[]{'s', 'e', 'c', 'r', 'e', 't'});
		assertThat(credentials.getProperties()).isEmpty();

		assertThat(credentials.getProxy()).isNotNull();
		Proxy proxy = credentials.getProxy();
		assertThat(proxy.type()).isEqualTo(Proxy.Type.HTTP);

		assertThat(proxy.address()).isNotNull();
		assertThat(proxy.address()).isInstanceOf(InetSocketAddress.class);

		InetSocketAddress address = (InetSocketAddress) proxy.address();
		assertThat(address.getPort()).isEqualTo(8080);
		assertThat(address.getHostString()).isEqualTo("proxy.example.com");
	}

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON.
	 *
	 * @throws Exception hopefully not
	 */
	@Test
	void fromJsonTokenTest() throws Exception {

		String headers = "{\"Authorization\": \"ApiKey a4db08b7-5729-4ba9-8c08-f2df493465a1\"}";
		String properties = "{\"headers\": " + headers + "}";
		JsonValue jsonValue =
				Json.parse("{\"name\": \"github\", \"type\": \"tokenauth\", " +
						"\"properties\": " + properties + "}");
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("github");
		assertThat(credentials.getType()).isEqualTo("tokenauth");
		assertThat(credentials.getProperties())
				.isNotEmpty().containsEntry("headers.Authorization", "ApiKey a4db08b7-5729-4ba9-8c08-f2df493465a1");

		assertThat(credentials.getProxy()).isNull();
	}

	/**
	 * Tests, if the {@link SecurityCredentials} can be created from JSON with empty password.
	 *
	 * @throws Exception hopefully not
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": null}", // null password
			"{\"name\": \"jenkins\", \"identifier\": \"alice\", \"secret\": \"\"}", // empty password
			"{\"name\": \"jenkins\", \"identifier\": \"alice\"}", // no password
			"{\"name\": \"jenkins\", \"identifier\": \"alice\", \"pwd\": \"Xyz\"}" // pwd ignored
	})
	void fromJsonTestNoPassword(String json) throws Exception {
		JsonValue jsonValue = Json.parse(json);
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials).isNotNull().isNotEqualTo(SecurityCredentials.NONE);

		assertThat(credentials.getName()).isEqualTo("jenkins");
		assertThat(credentials.getIdentifier()).isEqualTo("alice");
		assertThat(credentials.getSecret()).isNull();
		assertThat(credentials.getProperties()).isEmpty();

		assertThat(credentials.getProxy()).isNull();
	}

	/**
	 * Checks, if the property parser can read simple values.
	 */
	@Test
	void fromJsonWithSimpleProperties() {
		String props = "{\"grantType\": \"client_credentials\", \"test\": true, \"number\": 1.0, \"x\": null}";
		String json = "{\"name\": \"jenkins\", \"identifier\": \"alice\", \"properties\": " + props + " }";
		JsonValue jsonValue = Json.parse(json);
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials.getProperties())
				.isNotEmpty()
				.containsEntry("grantType", "client_credentials")
				.containsEntry("test", Boolean.TRUE)
				.containsEntry("number", 1.0d)
				.doesNotContainKey("x");
	}

	/**
	 * Checks, if the property parser can read nested values.
	 */
	@Test
	void fromJsonWithNestedProperties() {
		String nested = "{\"identifier\": \"serviceId\",\"secret\": \"ServiceSecret\"}";
		String props = "{\"grantType\": \"client_credentials\", \"nested\": " + nested + "}";
		String json = "{\"name\": \"jenkins\", \"identifier\": \"alice\", \"properties\": " + props + " }";
		JsonValue jsonValue = Json.parse(json);
		SecurityCredentials credentials = SecurityCredentials.fromJson(jsonValue);

		assertThat(credentials.getProperties())
				.isNotEmpty()
				.containsEntry("grantType", "client_credentials")
				.containsEntry("nested.identifier", "serviceId")
				.containsEntry("nested.secret", "ServiceSecret");
	}
}
