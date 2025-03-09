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

import java.util.Arrays;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;
import net.sourceforge.plantuml.security.authentication.basicauth.BasicAuthAuthorizeManager;

/**
 * Authorize via principal a resource owner (from {@link SecurityCredentials}
 * and creates a {@link SecurityAuthentication} object with a bearer token
 * secret.
 * <p>
 * Because a pass through of username/password is an anti-pattern in OAuth2,
 * this authorization method should be avoided. However, it may be necessary in
 * some environments to gain access with the ROPC flow.
 *
 * @author Aljoscha Rittner
 */
public class OAuth2ResourceOwnerAccessAuthorizeManager extends AbstractOAuth2AccessAuthorizeManager {

	/**
	 * Basic Auth manager to access the token service with authorization.
	 */
	private final BasicAuthAuthorizeManager basicAuthManager = new BasicAuthAuthorizeManager();

	@Override
	public SecurityAuthentication create(SecurityCredentials credentials) {
		String grantType = credentials.getPropertyStr("grantType");
		String requestScope = credentials.getPropertyStr("scope");
		String accessTokenUri = credentials.getPropertyStr("accessTokenUri");
		String tokenType = credentials.getPropertyStr("tokenType");

		// Resource owner
		String username = credentials.getPropertyStr("resourceOwner.identifier");
		char[] password = credentials.getPropertyChars("resourceOwner.secret");

		try {
			SURL tokenService = SURL.create(accessTokenUri);

			StringBuilder content = new StringBuilder().append("grant_type=").append(urlEncode(grantType));
			if (StringUtils.isNotEmpty(requestScope)) {
				content.append("&scope=").append(urlEncode(requestScope));
			}

			// OAuth2 with BasicAuth via principal (standard)
			SecurityAuthentication basicAuth = basicAuthManager
					.create(SecurityCredentials.basicAuth(credentials.getIdentifier(), credentials.getSecret()));
			// We need to add the principal to the form
			content.append("&username=").append(urlEncode(username)).append("&password=")
					.append(urlEncode(new String(password)));

			return requestAndCreateAuthFromResponse(credentials.getProxy(), grantType, tokenType, tokenService,
					content.toString(), basicAuth);
		} finally {
			if (password != null && password.length > 0) {
				Arrays.fill(password, '*');
			}
		}
	}

}
