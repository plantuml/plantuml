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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.security.authentication.SecurityAuthentication;
import net.sourceforge.plantuml.security.authentication.SecurityAuthorizeManager;
import net.sourceforge.plantuml.security.authentication.SecurityCredentials;

/**
 * The {@link BasicAuthAuthorizeManager} creates the authentication on the fly
 * from the credentials without any access to other services.
 *
 * @author Aljoscha Rittner
 */
public class BasicAuthAuthorizeManager implements SecurityAuthorizeManager {
    // ::remove folder when __HAXE__
	@Override
	public SecurityAuthentication create(SecurityCredentials credentials) {
		String type = credentials.getType();
		String identifier = credentials.getIdentifier();
		char[] secret = credentials.getSecret();
		Map<String, Object> tokens = new HashMap<String, Object>();
		tokens.put("identifier", identifier);
		if (secret != null) {
			tokens.put("secret", secret.clone());
		}
		return new SecurityAuthentication(type, tokens);
	}
}
