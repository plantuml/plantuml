/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.security.authentication;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * Defines a configuration for credentials.
 *
 * @author Aljoscha Rittner
 */
public class SecurityCredentials implements SecurityCredentialsContainer {

	private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

	/**
	 * No credentials given.
	 */
	public static final SecurityCredentials NONE = new SecurityCredentials("<NONE>", "public", null, null);

	/**
	 * Name of the configuration.
	 */
	private final String name;
	/**
	 * The type of authorization and access process (e.g. "basicauth" or "oauth2").
	 */
	private final String type;
	/**
	 * Username or client identifier.
	 */
	private final String identifier;
	/**
	 * User/Client secret information.
	 */
	private final char[] secret;
	/**
	 * Properties defined for a specific authorization and access process.
	 */
	private final Map<String, Object> properties = new HashMap<String, Object>();
	/**
	 * Proxy configuration.
	 * <p>
	 * <p>
	 * {@link Proxy#NO_PROXY} means, we want direct access. null means, we use the
	 * system proxy configuration.
	 */
	private final Proxy proxy;

	/**
	 * Creates BasicAuth credentials without a proxy.
	 *
	 * @param name       Name of the credentials
	 * @param type       The type of authentication and access process (e.g.
	 *                   "basicauth" or "oauth2")
	 * @param identifier username, clientId, ...
	 * @param secret     the secret information to authenticate the client or user
	 */
	public SecurityCredentials(String name, String type, String identifier, char[] secret) {
		this(name, type, identifier, secret, EMPTY_MAP, null);
	}

	/**
	 * Creates BasicAuth credentials with a proxy.
	 *
	 * @param name       Name of the credentials
	 * @param type       The type of authentication and access process (e.g.
	 *                   "basicauth" or "oauth2")
	 * @param identifier username, clientId, ...
	 * @param secret     the secret information to authenticate the client or user
	 * @param proxy      proxy configuration
	 */
	public SecurityCredentials(String name, String type, String identifier, char[] secret,
			Map<String, Object> properties, Proxy proxy) {
		if (name == null) {
			throw new NullPointerException("Credential name should not be null");
		}
		this.name = name;
		this.type = type;
		this.identifier = identifier;
		this.secret = secret;
		this.proxy = proxy;
		this.properties.putAll(properties);
	}

	/**
	 * Creates BasicAuth credentials.
	 *
	 * @param identifier the basic auth user name.
	 * @param secret     password
	 * @return credential object
	 */
	public static SecurityCredentials basicAuth(String identifier, char[] secret) {
		return new SecurityCredentials(identifier, "basicauth", identifier, secret);
	}

	/**
	 * Creates a SecurityCredentials from a JSON.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 *     {
	 *         "name": "jenkins",
	 *         "identifier": "alice",
	 *         "secret": "secret",
	 *         "proxy": {
	 *             "type": "socket",
	 *             "address": "192.168.1.250",
	 *             "port": 8080
	 *         }
	 *     }
	 * </pre>
	 *
	 * @param jsonValue a JSON structure
	 * @return the created SecurityCredentials
	 */
	public static SecurityCredentials fromJson(JsonValue jsonValue) {
		try {
			JsonObject securityObject = jsonValue.asObject();
			JsonValue name = securityObject.get("name");
			JsonValue type = securityObject.get("type");
			JsonValue identifier = securityObject.get("identifier");
			JsonValue secret = securityObject.get("secret");

			Map<String, Object> map = new HashMap<String, Object>();
			buildProperties("", securityObject.get("properties"), map);

			if (type != null && !type.isNull() && "tokenauth".equals(type.asString())) {
				return new SecurityCredentials(name.asString(), "tokenauth", null, null, map,
						proxyFromJson(securityObject.get("proxy")));
			} else if (StringUtils.isNotEmpty(name.asString()) && StringUtils.isNotEmpty(identifier.asString())) {
				String authType = type != null && !type.isNull() ? type.asString() : "basicauth";
				return new SecurityCredentials(name.asString(), authType, identifier.asString(), extractSecret(secret),
						map, proxyFromJson(securityObject.get("proxy")));
			}

		} catch (UnsupportedOperationException use) {
			// We catch UnsupportedOperationException to stop parsing on unexpected elements
		}
		return NONE;
	}

	/**
	 * Creates a Proxy object from a JSON value.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 *     {
	 *         "type": "socket",
	 *         "address": "192.168.1.250",
	 *         "port": 8080
	 *     }
	 * </pre>
	 *
	 * @param proxyValue JSON, that represents a Proxy object
	 * @return Proxy object or null
	 */
	private static Proxy proxyFromJson(JsonValue proxyValue) {
		if (proxyValue != null && !proxyValue.isNull() && proxyValue.isObject()) {
			Proxy.Type type = Proxy.Type.DIRECT;

			JsonObject proxyObject = proxyValue.asObject();
			JsonValue proxyType = proxyObject.get("type");
			if (proxyType != null && !proxyType.isNull()) {
				type = Proxy.Type.valueOf(proxyType.asString().toUpperCase());
			}
			if (type == Proxy.Type.DIRECT) {
				return Proxy.NO_PROXY;
			}
			JsonValue proxyAddress = proxyObject.get("address");
			JsonValue proxyPort = proxyObject.get("port");
			if (proxyAddress != null && !proxyAddress.isNull() && !proxyPort.isNull() && proxyPort.isNumber()) {
				InetSocketAddress address = new InetSocketAddress(proxyAddress.asString(), proxyPort.asInt());
				return new Proxy(type, address);
			}
		}
		return null;
	}

	/**
	 * Extracts a password, if it is not empty or null.
	 *
	 * @param pwd password json value
	 * @return password or null
	 */
	private static char[] extractSecret(JsonValue pwd) {
		if (pwd == null || pwd.isNull()) {
			return null;
		}
		String pwdStr = pwd.asString();
		if (StringUtils.isEmpty(pwdStr)) {
			return null;
		}

		return pwdStr.toCharArray();
	}

	/**
	 * Creates a properties map from all given key/values.
	 * <p>
	 * Example:<br/>
	 * 
	 * <pre>
	 *     {
	 *         "grantType": "client_credentials",
	 *         "scope": "read write",
	 *         "accessTokenUri": "https://login-demo.curity.io/oauth/v2/oauth-token"
	 *         "credentials": {
	 *             "identifier": "serviceId",
	 *             "secret": "ServiceSecret"
	 *         }
	 *     }
	 *
	 *     will be transformed to:
	 *
	 *     grantType -> client_credentials
	 *     scope -> read write
	 *     accessTokenUri -> https://login-demo.curity.io/oauth/v2/oauth-token
	 *     credentials.identifier -> serviceId
	 *     credentials.secret -> ServiceSecret
	 * </pre>
	 *
	 * @param prefix    the prefix for the direct children
	 * @param fromValue parent JSON value to read from
	 * @param toMap     map to populate
	 */
	private static void buildProperties(String prefix, JsonValue fromValue, Map<String, Object> toMap) {
		if (!isJsonObjectWithMembers(fromValue)) {
			return;
		}
		JsonObject members = fromValue.asObject();
		for (String name : members.names()) {
			JsonValue child = members.get(name);
			if (child.isArray() || child.isNull()) {
				// currently, not supported or not needed
				continue;
			}
			String key = StringUtils.isEmpty(prefix) ? name : prefix + '.' + name;
			if (child.isObject()) {
				buildProperties(key, child, toMap);
			} else {
				if (child.isString()) {
					toMap.put(key, child.asString());
				} else if (child.isBoolean()) {
					toMap.put(key, child.asBoolean());
				} else if (child.isNumber()) {
					toMap.put(key, child.asDouble());
				}
			}
		}
	}

	/**
	 * Checks, if we have a JSON object with members.
	 *
	 * @param jsonValue the value to check
	 * @return true, if we have members in the JSON object
	 */
	private static boolean isJsonObjectWithMembers(JsonValue jsonValue) {
		return jsonValue != null && !jsonValue.isNull() && jsonValue.isObject() && !jsonValue.asObject().isEmpty();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public char[] getSecret() {
		return secret;
	}

	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

	/**
	 * Returns the property as String.
	 *
	 * @param key Name of the property
	 * @return String representation
	 */
	public String getPropertyStr(String key) {
		Object value = getProperties().get(key);
		if (value != null) {
			return value.toString();
		}
		return null;
	}

	/**
	 * Returns the property as characters.
	 *
	 * @param key Name of the property
	 * @return char[] representation
	 */
	public char[] getPropertyChars(String key) {
		Object value = getProperties().get(key);
		if (value != null) {
			return value.toString().toCharArray();
		}
		return null;
	}

	/**
	 * Returns the property as boolean.
	 *
	 * @param key Name of the property
	 * @return boolean representation
	 */
	public boolean getPropertyBool(String key) {
		Object value = getProperties().get(key);
		if (value != null) {
			if (value instanceof Boolean) {
				return (Boolean) value;
			} else if (value instanceof String) {
				return Boolean.parseBoolean((String) value);
			}
		}
		return false;
	}

	/**
	 * Returns the property as Number.
	 *
	 * @param key Name of the property
	 * @return boolean representation
	 */
	public Number getPropertyNum(String key) {
		Object value = getProperties().get(key);
		if (value != null) {
			if (value instanceof Number) {
				return (Number) value;
			} else if (value instanceof String) {
				return Double.parseDouble((String) value);
			}
		}
		return null;
	}

	public Proxy getProxy() {
		return proxy;
	}

	@Override
	public void eraseCredentials() {
		if (secret != null && secret.length > 0) {
			Arrays.fill(secret, '*');
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SecurityCredentials))
			return false;
		SecurityCredentials that = (SecurityCredentials) o;
		return getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

}
