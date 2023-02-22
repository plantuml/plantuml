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
package net.sourceforge.plantuml.security.authentication.basicauth;

import java.net.URLConnection;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.security.authentication.SecurityAccessInterceptor;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.utils.Base64Coder;

/**
 * Applies from {@link SecurityAuthentication} data a BasicAuth authentication
 * access header.
 *
 * @author Aljoscha Rittner
 */
public class BasicAuthAccessInterceptor implements SecurityAccessInterceptor {

	/**
	 * Applies from {@link SecurityAuthentication} data a BasicAuth authentication
	 * access header.
	 * <p>
	 * Expects "identifier" and "secret" to build a Authorization header.
	 *
	 * @param authentication the determined authentication data to authorize for the
	 *                       endpoint access
	 * @param connection     the connection to the endpoint
	 */
	@Override
	public void apply(SecurityAuthentication authentication, URLConnection connection) {
		String auth = getAuth(authentication);
		String authorization = Base64Coder.encodeString(auth);
		String authHeaderValue = "Basic " + authorization;
		connection.setRequestProperty("Authorization", authHeaderValue);
	}

	private String getAuth(SecurityAuthentication authentication) {
		String id = (String) authentication.getTokens().get("identifier");
		char[] secret = (char[]) authentication.getTokens().get("secret");
		StringBuilder auth = new StringBuilder();
		if (StringUtils.isNotEmpty(id)) {
			auth.append(id);
			if (secret != null && secret.length > 0) {
				auth.append(':').append(secret);
			}
		}
		return auth.toString();
	}
}
