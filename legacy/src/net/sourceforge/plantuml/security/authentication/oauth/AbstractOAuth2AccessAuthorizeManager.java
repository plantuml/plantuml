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
package net.sourceforge.plantuml.security.authentication.oauth;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityAuthorizeManager;

/**
 * Default abstract OAuth2 AccessAuthorizeManager for OAuth2 managers.
 *
 * @author Aljoscha Rittner
 */
public abstract class AbstractOAuth2AccessAuthorizeManager implements SecurityAuthorizeManager {
    // ::remove folder when __HAXE__

	/**
	 * Default headers for token service access.
	 * <p>
	 * Initialize with:
	 * 
	 * <pre>
	 * "Content-Type"="application/x-www-form-urlencoded; charset=UTF-8"
	 * "Accept"="application/json"
	 * </pre>
	 *
	 * @return headers
	 */
	protected Map<String, Object> headers() {
		Map<String, Object> map = new HashMap<>();
		map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		map.put("Accept", "application/json");
		return map;
	}

	/**
	 * Builds the access parameter map.
	 *
	 * @param tokenResponse the JSOn object with the response data
	 * @param tokenType     token type to use instead of token_type from response
	 * @return data-map
	 */
	protected Map<String, Object> buildAccessDataFromResponse(JsonObject tokenResponse, String tokenType) {
		Map<String, Object> map = new HashMap<>();

		toMap(map, tokenResponse, OAuth2Tokens.ACCESS_TOKEN);
		toMap(map, tokenResponse, OAuth2Tokens.SCOPE);
		toMap(map, tokenResponse, OAuth2Tokens.EXPIRES_IN);

		if (tokenType == null) {
			toMap(map, tokenResponse, OAuth2Tokens.TOKEN_TYPE);
			if (!map.isEmpty() && !map.containsKey(OAuth2Tokens.TOKEN_TYPE.key())) {
				// default token type is bearer
				map.put(OAuth2Tokens.TOKEN_TYPE.key(), "bearer");
			}
		} else {
			// Caller don't belief in the token_type response
			if (!map.isEmpty()) {
				map.put(OAuth2Tokens.TOKEN_TYPE.key(), tokenType);
			}
		}

		return map;
	}

	/**
	 * Translates the JSON value to a map key/value.
	 *
	 * @param map      collection to store
	 * @param response values from response
	 * @param name     name of the value
	 */
	private void toMap(Map<String, Object> map, JsonObject response, OAuth2Tokens name) {
		JsonValue jsonValue = response.get(name.key());
		if (jsonValue != null && !jsonValue.isNull()) {
			if (jsonValue.isString()) {
				map.put(name.key(), jsonValue.asString());
			} else if (jsonValue.isNumber()) {
				map.put(name.key(), jsonValue.asInt());
			} else if (jsonValue.isBoolean()) {
				map.put(name.key(), jsonValue.asBoolean());
			}
		}
	}

	/**
	 * Encodes the data to UTF-8 into {@code application/x-www-form-urlencoded}.
	 *
	 * @param data data to encode
	 * @return the encoded data
	 */
	protected String urlEncode(String data) {
		try {
			return URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
			return data;
		}
	}

	/**
	 * Calls the endpoint to load the token response and create a
	 * SecurityAuthentication.
	 *
	 * @param proxy        Proxy for the access
	 * @param grantType    grant type
	 * @param tokenType    token type to use instead of token_type from response
	 * @param tokenService URL to token service
	 * @param content      body content
	 * @param basicAuth    principal basicAuth
	 * @return the authentication object to access resources (or null)
	 */
	protected SecurityAuthentication requestAndCreateAuthFromResponse(Proxy proxy, String grantType, String tokenType,
			SURL tokenService, String content, SecurityAuthentication basicAuth) {
		byte[] bytes = tokenService.getBytesOnPost(proxy, basicAuth, content, headers());
		if (bytes != null) {
			JsonValue tokenResponse = Json.parse(new String(bytes, StandardCharsets.UTF_8));
			if (tokenResponse != null && !tokenResponse.isNull()) {
				return new SecurityAuthentication("oauth2", null, grantType,
						buildAccessDataFromResponse(tokenResponse.asObject(), tokenType));
			}
		}
		return null;
	}
}
