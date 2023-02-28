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

import java.util.Arrays;
import java.util.Map;

/**
 * The authentication to access an endpoint. This information will be generated
 * by a SecurityAuthenticationInterceptor.
 *
 * @author Aljoscha Rittner
 */
public class SecurityAuthentication implements SecurityCredentialsContainer {
    // ::remove folder when __HAXE__

	/**
	 * Type of authentication (e.g. basicauth, oauth2)
	 */
	private final String type;

	/**
	 * Characteristic of an authentication (e.g. openId). Can be null.
	 * <p>
	 * This kind of information is typically not needed. Useful for debugging
	 * purpose.
	 */
	private final String shape;

	/**
	 * Origin authorization process (e.g. client_credentials.
	 * <p>
	 * This kind of information is typically not needed. Useful for debugging
	 * purpose.
	 */
	private final String grantType;

	/**
	 * A map of needed data tokens to authenticate access to an endpoint.
	 */
	private final Map<String, Object> tokens;

	public SecurityAuthentication(String type, Map<String, Object> tokens) {
		this(type, null, null, tokens);
	}

	public SecurityAuthentication(String type, String shape, String grantType, Map<String, Object> tokens) {
		this.type = type;
		this.shape = shape;
		this.grantType = grantType;
		this.tokens = tokens;
	}

	public String getType() {
		return type;
	}

	public String getShape() {
		return shape;
	}

	public String getGrantType() {
		return grantType;
	}

	/**
	 * Requests the state of this authentication.
	 *
	 * @return true, if we have no authentication.
	 */
	public boolean isPublic() {
		return "public".equalsIgnoreCase(type) && (tokens == null || tokens.isEmpty());
	}

	public Map<String, Object> getTokens() {
		return tokens;
	}

	@Override
	public void eraseCredentials() {
		if (tokens != null && !tokens.isEmpty()) {
			for (Object tokenVal : tokens.values()) {
				if (tokenVal instanceof char[]) {
					Arrays.fill((char[]) tokenVal, '*');
				}
			}
			tokens.clear();
		}
	}
}
