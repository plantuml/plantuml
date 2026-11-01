/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;

/**
 * Decides which URL a diagram may read, working on the string of the URL only
 * (no java.net, no DNS), for the C build.
 * <p>
 * It applies the rules of {@link SURL#isUrlOk()} and of {@link URLCheck} on the
 * JVM. What needs the network is left to the native code: the refusal of the
 * hosts that resolve to an inner address (loopback, link-local, private), which is
 * told to it through {@link #isInnerAddressAllowed(String)}.
 */
public final class UrlPolicy {

	/**
	 * Regex to remove the UserInfo part from a URL.
	 */
	private static final Pattern PATTERN_USERINFO = Pattern.compile("(^https?://)([-_0-9a-zA-Z]+@)([^@]*)$");

	private UrlPolicy() {
	}

	/**
	 * Whether the security profile lets a diagram read this URL.
	 */
	public static boolean isAllowed(String url) {
		if (url == null)
			return false;

		final SecurityProfile profile = SecurityUtils.getSecurityProfile();
		if (profile == SecurityProfile.SANDBOX)
			// In SANDBOX, we cannot read any URL
			return false;

		if (isInAllowList(url))
			return true;

		if (profile == SecurityProfile.LEGACY)
			return isForbidden(cleanPath(url)) == false;

		if (profile == SecurityProfile.INSECURE)
			return true;

		if (profile == SecurityProfile.INTERNET || profile == SecurityProfile.INTERNET_WITH_DOTSVG) {
			if (isForbidden(cleanPath(url)))
				return false;

			final int port = getPort(url);
			// Using INTERNET profile, port 80 and 443 are ok
			return port == 80 || port == 443 || port == -1;
		}
		return false;
	}

	/**
	 * Whether the host may resolve to an inner address: only in INSECURE, and for
	 * the URL of the allowlist, as on the JVM.
	 */
	public static boolean isInnerAddressAllowed(String url) {
		return SecurityUtils.getSecurityProfile() == SecurityProfile.INSECURE || isInAllowList(url);
	}

	private static boolean isInAllowList(String url) {
		final String full = cleanPath(url);
		// Thanks to Agasthya Kasturi
		if (full.contains("@"))
			return false;

		final String env = SecurityUtils.getenv(SecurityUtils.ALLOWLIST_URL);
		if (env == null)
			return false;

		for (String allow : StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env).split(";")) {
			// An empty entry would allow every URL
			if (allow.trim().isEmpty())
				continue;

			if (full.startsWith(cleanPath(allow)))
				return true;
		}
		return false;
	}

	/**
	 * Same as the one of SURL.
	 */
	private static String cleanPath(String path) {
		// Remove user information, because we don't like to store user/password or
		// userTokens in allow-list
		path = removeUserInfoFromUrlPath(path);
		path = path.trim().toLowerCase();
		// We simplify/normalize the url, removing default ports
		path = path.replace(":80/", "");
		path = path.replace(":443/", "");
		return path;
	}

	private static String removeUserInfoFromUrlPath(String url) {
		final Matcher matcher = PATTERN_USERINFO.matcher(url);
		if (matcher.find())
			return matcher.replaceFirst("$1$3");

		return url;
	}

	/**
	 * Same checks as {@link URLCheck#isURLforbidden(String)}, without the one that
	 * needs a DNS lookup.
	 */
	private static boolean isForbidden(String full) {
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

		final String host = getHost(full);
		if (host == null || host.isEmpty() || host.contains(".") == false)
			return true;

		// Additional check for IP addresses or invalid host patterns
		if (host.matches("^[-#.0-9:\\[\\]+]+$"))
			return true;

		// The JVM refuses a host that changes when it is URL decoded
		return host.contains("%");
	}

	/**
	 * The part of the URL between {@code ://} and the first {@code /}, {@code ?} or
	 * {@code #}, without its user information.
	 */
	private static String getAuthority(String url) {
		final int start = url.indexOf("://");
		if (start == -1)
			return null;

		int end = start + 3;
		while (end < url.length()) {
			final char c = url.charAt(end);
			if (c == '/' || c == '?' || c == '#')
				break;
			end++;
		}
		final String authority = url.substring(start + 3, end);
		final int at = authority.lastIndexOf('@');
		return at == -1 ? authority : authority.substring(at + 1);
	}

	private static String getHost(String url) {
		final String authority = getAuthority(url);
		if (authority == null)
			return null;

		if (authority.startsWith("[")) {
			final int close = authority.indexOf(']');
			return close == -1 ? null : authority.substring(0, close + 1);
		}
		final int colon = authority.indexOf(':');
		return colon == -1 ? authority : authority.substring(0, colon);
	}

	/**
	 * The port of the URL, -1 if it is not written, -2 if it is not a number.
	 */
	private static int getPort(String url) {
		final String authority = getAuthority(url);
		if (authority == null)
			return -2;

		final int colon = authority.startsWith("[") ? authority.indexOf("]:") + 1 : authority.indexOf(':');
		if (colon <= 0)
			return -1;

		final String port = authority.substring(colon + 1);
		if (port.isEmpty())
			return -1;

		try {
			return Integer.parseInt(port);
		} catch (NumberFormatException e) {
			return -2;
		}
	}

}
