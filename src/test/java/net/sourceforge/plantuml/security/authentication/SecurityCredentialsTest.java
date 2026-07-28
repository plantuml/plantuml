package net.sourceforge.plantuml.security.authentication;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("jenkins", credentials.getName());
		assertEquals("basicauth", credentials.getType());
		assertEquals("alice", credentials.getIdentifier());
		assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, credentials.getSecret());
		assertTrue(credentials.getProperties().isEmpty());

		assertNull(credentials.getProxy());
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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("jenkins", credentials.getName());
		assertEquals("basicauth", credentials.getType());
		assertEquals("alice", credentials.getIdentifier());
		assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, credentials.getSecret());
		assertTrue(credentials.getProperties().isEmpty());

		assertNotNull(credentials.getProxy());
		Proxy proxy = credentials.getProxy();
		assertEquals(Proxy.Type.DIRECT, proxy.type());
		assertNull(proxy.address());
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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("jenkins", credentials.getName());
		assertEquals("basicauth", credentials.getType());
		assertEquals("alice", credentials.getIdentifier());
		assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, credentials.getSecret());
		assertTrue(credentials.getProperties().isEmpty());

		assertNotNull(credentials.getProxy());
		Proxy proxy = credentials.getProxy();
		assertEquals(Proxy.Type.SOCKS, proxy.type());

		assertNotNull(proxy.address());
		assertInstanceOf(InetSocketAddress.class, proxy.address());

		InetSocketAddress address = (InetSocketAddress) proxy.address();
		assertEquals(8080, address.getPort());
		assertEquals("192.168.92.250", address.getHostString());
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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("jenkins", credentials.getName());
		assertEquals("basicauth", credentials.getType());
		assertEquals("alice", credentials.getIdentifier());
		assertArrayEquals(new char[]{'s', 'e', 'c', 'r', 'e', 't'}, credentials.getSecret());
		assertTrue(credentials.getProperties().isEmpty());

		assertNotNull(credentials.getProxy());
		Proxy proxy = credentials.getProxy();
		assertEquals(Proxy.Type.HTTP, proxy.type());

		assertNotNull(proxy.address());
		assertInstanceOf(InetSocketAddress.class, proxy.address());

		InetSocketAddress address = (InetSocketAddress) proxy.address();
		assertEquals(8080, address.getPort());
		assertEquals("proxy.example.com", address.getHostString());
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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("github", credentials.getName());
		assertEquals("tokenauth", credentials.getType());
		assertFalse(credentials.getProperties().isEmpty()); assertEquals("ApiKey a4db08b7-5729-4ba9-8c08-f2df493465a1", credentials.getProperties().get("headers.Authorization"));

		assertNull(credentials.getProxy());
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

		assertNotNull(credentials); assertNotEquals(SecurityCredentials.NONE, credentials);

		assertEquals("jenkins", credentials.getName());
		assertEquals("alice", credentials.getIdentifier());
		assertNull(credentials.getSecret());
		assertTrue(credentials.getProperties().isEmpty());

		assertNull(credentials.getProxy());
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

		assertFalse(credentials.getProperties().isEmpty()); assertEquals("client_credentials", credentials.getProperties().get("grantType")); assertEquals(Boolean.TRUE, credentials.getProperties().get("test")); assertEquals(1.0d, credentials.getProperties().get("number"));
		assertFalse(credentials.getProperties().containsKey("x"));
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

		assertFalse(credentials.getProperties().isEmpty()); assertEquals("client_credentials", credentials.getProperties().get("grantType")); assertEquals("serviceId", credentials.getProperties().get("nested.identifier")); assertEquals("ServiceSecret", credentials.getProperties().get("nested.secret"));
	}
}
