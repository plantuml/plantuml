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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;

public class SecurityUtils {

	static private SecurityProfile current = null;

	public static synchronized SecurityProfile getSecurityProfile() {
		if (current == null) {
			current = SecurityProfile.init();
		}
		return current;
	}

	public static boolean getJavascriptUnsecure() {
		final String env = getenv("PLANTUML_JAVASCRIPT_UNSECURE");
		if ("true".equalsIgnoreCase(env)) {
			return true;
		}
		return OptionFlags.ALLOW_INCLUDE;
	}

	public static String getenv(String name) {
		final String env = System.getProperty(name);
		if (StringUtils.isNotEmpty(env)) {
			return env;
		}
		return System.getenv(name);
	}

	public static List<SFile> getPath(String prop) {
		final List<SFile> result = new ArrayList<>();
		String paths = getenv(prop);
		if (paths == null) {
			return Collections.unmodifiableList(result);
		}
		paths = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(paths);
		final StringTokenizer st = new StringTokenizer(paths, System.getProperty("path.separator"));
		while (st.hasMoreTokens()) {
			try {
				final SFile f = new SFile(st.nextToken()).getCanonicalFile();
				if (f.isDirectory()) {
					result.add(f);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Collections.unmodifiableList(result);
	}

	public static boolean allowSvgText() {
		return true;
	}

	public static java.io.PrintWriter createPrintWriter(OutputStream os) {
		return new PrintWriter(os);
	}

	public static java.io.PrintWriter createPrintWriter(OutputStream os, boolean append) {
		return new PrintWriter(os, append);
	}

	public static PrintStream createPrintStream(OutputStream os) {
		return new PrintStream(os);
	}

	public static PrintStream createPrintStream(OutputStream os, boolean autoFlush, String charset)
			throws UnsupportedEncodingException {
		return new PrintStream(os, autoFlush, charset);
	}

	public static PrintStream createPrintStream(OutputStream os, boolean autoFlush, Charset charset)
			throws UnsupportedEncodingException {
		return new PrintStream(os, autoFlush, charset.name());
	}

	public synchronized static BufferedImage readRasterImage(final ImageIcon imageIcon) {
		final Image tmpImage = imageIcon.getImage();
		if (imageIcon.getIconWidth() == -1) {
			return null;
		}
		final BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(tmpImage, 0, 0, null);
		tmpImage.flush();
		return image;
	}

	// ----
	public static FileReader createFileReader(String path) throws FileNotFoundException {
		return new FileReader(path);
	}

	public static java.io.PrintWriter createPrintWriter(String path) throws FileNotFoundException {
		return new PrintWriter(path);
	}

	public static FileOutputStream createFileOutputStream(String path) throws FileNotFoundException {
		return new FileOutputStream(path);
	}

}
