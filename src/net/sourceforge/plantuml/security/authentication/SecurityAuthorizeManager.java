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

/**
 * Creates from credentials a {@link SecurityAuthentication} object or authorize
 * as principal to retrieve an authentication object.
 *
 * @author Aljoscha Rittner
 */
public interface SecurityAuthorizeManager {
	/**
	 * Creates from the credentials the authentication object to access an endpoint.
	 * If the credentials defines a principal (e.g. in OAuth2), the create method
	 * should authorize the principal and get the final authentication data to
	 * access an endpoint.
	 *
	 * @param credentials the configured credentials
	 * @return the authentication object.
	 */
	SecurityAuthentication create(SecurityCredentials credentials);
}
