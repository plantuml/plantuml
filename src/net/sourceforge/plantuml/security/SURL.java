/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.StringUtils;

/**
 * Secure replacement for java.net.URL.
 * <p>
 * This class should be used instead of java.net.URL.
 * <p>
 * This class does some control access.
 *
 */
public class SURL {

	private final URL internal;

	private SURL(String src) throws MalformedURLException {
		this(new URL(src));
	}

	private SURL(URL url) {
		this.internal = url;
	}

	public static SURL create(String url) {
		if (url == null) {
			return null;
		}
		if (url.startsWith("http://") || url.startsWith("https://"))
			try {
				return new SURL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static SURL create(URL url) {
		if (url == null) {
			return null;
		}
		return new SURL(url);
	}

	@Override
	public String toString() {
		return internal.toString();
	}

	/**
	 * Check SecurityProfile to see if this URL can be open.
	 */
	private boolean isUrlOk() {
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.SANDBOX) {
			// In SANDBOX, we cannot read any URL
			return false;
		}
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.LEGACY) {
			return true;
		}
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
			// We are UNSECURE anyway
			return true;
		}
		if (isInWhiteList()) {
			return true;
		}
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.INTERNET) {
			final int port = internal.getPort();
			// Using INTERNET profile, port 80 and 443 are ok
			if (port == 80 || port == 443) {
				return true;
			}
		}
		return false;
	}

	private boolean isInWhiteList() {
		final String full = cleanPath(internal.toString());
		for (String white : getWhiteList()) {
			if (full.startsWith(cleanPath(white))) {
				return true;
			}
		}
		return false;
	}

	private String cleanPath(String path) {
		path = path.trim().toLowerCase(Locale.US);
		// We simplify/normalize the url, removing default ports
		path = path.replace(":80/", "");
		path = path.replace(":443/", "");
		return path;
	}

	private List<String> getWhiteList() {
		final String env = SecurityUtils.getenv("plantuml.whitelist.url");
		if (env == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env).split(";"));
	}

	public URLConnection openConnection() {
		if (isUrlOk())
			try {
				return internal.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	public InputStream openStream() {
		if (isUrlOk())
			try {
				return internal.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	public BufferedImage readRasterImageFromURL() {
		if (isUrlOk())
			try {
				final ImageIcon tmp = new ImageIcon(internal);
				return SecurityUtils.readRasterImage(tmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

}
