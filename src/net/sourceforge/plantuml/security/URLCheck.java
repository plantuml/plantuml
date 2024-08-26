/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.security;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;

public class URLCheck {

	public static boolean isURLforbidden(String full) {
		// Thanks to Agasthya Kasturi
		if (full.contains("@"))
			return true;
		if (full.startsWith("https://") == false && full.startsWith("http://") == false)
			return true;
		if (full.matches("^https?://[-#.0-9:\\[\\]+]+/.*"))
			return true;
		if (full.matches("^https?://[^.]+/.*"))
			return true;
		if (full.matches("^https?://[^.]+$"))
			return true;

		try {
			if (isURLforbidden(new URL(full)))
				return true;
		} catch (Exception e) {
			return true;
		}

		return false;

	}

	public static boolean isURLforbidden(URL url) throws UnsupportedEncodingException, UnknownHostException {

		// Check for '@' in the authority part (user info)
		final String userInfo = url.getUserInfo();
		if (userInfo != null && !userInfo.isEmpty())
			return true;

		// Check protocol
		final String protocol = url.getProtocol();
		if (!protocol.equals("http") && !protocol.equals("https"))
			return true;

		// Check host for invalid patterns
		final String host = url.getHost();
		if (host == null || host.isEmpty() || !host.contains("."))
			return true;

		// When UNSECURE, we allow localhost
		if (SecurityUtils.getSecurityProfile() != SecurityProfile.UNSECURE) {
			// Additional check for IP addresses or invalid host patterns
			if (host.matches("^[-#.0-9:\\[\\]+]+$"))
				return true;

			final InetAddress inetAddress = InetAddress.getByName(host);
			// Check host address
			if (isInnerAddress(inetAddress))
				return true;
		}

		// Additional checks (e.g., encoding)
		final String decodedHost = URLDecoder.decode(host, "UTF-8");
		if (!host.equals(decodedHost))
			return true;

		return false;
	}

	private static boolean isInnerAddress(InetAddress inetAddress) {
		return inetAddress.isAnyLocalAddress() //
				|| inetAddress.isLoopbackAddress() //
				|| inetAddress.isLinkLocalAddress() //
				|| inetAddress.isSiteLocalAddress();
	}

}
