/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.version;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemVersion extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private BufferedImage image;

	PSystemVersion(boolean withImage, List<String> args) {
		strings.addAll(args);
		if (withImage) {
			image = getPlantumlImage();
		}
	}

	public static BufferedImage getPlantumlImage() {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream("logo.png");
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage getCharlieImage() {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream("charlie.png");
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage getPlantumlSmallIcon() {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream("favicon.png");
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage transparentIcon;

	public static BufferedImage getPlantumlSmallIcon2() {
		if (transparentIcon != null) {
			return transparentIcon;
		}
		final BufferedImage ico = getPlantumlSmallIcon();
		if (ico == null) {
			return null;
		}
		transparentIcon = new BufferedImage(ico.getWidth(), ico.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		for (int i = 0; i < ico.getWidth(); i++) {
			for (int j = 0; j < ico.getHeight(); j++) {
				final int col = ico.getRGB(i, j);
				if (col != ico.getRGB(0, 0)) {
					transparentIcon.setRGB(i, j, col);
				}
			}
		}
		return transparentIcon;
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final GraphicStrings result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, result.getBackcolor(),
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.addUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, os);
	}

	public static PSystemVersion createShowVersion() {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
		strings.add("Loaded from " + Version.getJarPath());
		strings.add(" ");

		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		strings.add(" ");
		final Properties p = System.getProperties();
		strings.add(p.getProperty("java.runtime.name"));
		strings.add(p.getProperty("java.vm.name"));
		strings.add(p.getProperty("java.runtime.version"));
		strings.add(p.getProperty("os.name"));
		strings.add("Processors: " + Runtime.getRuntime().availableProcessors());
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createShowAuthors() {
		// Duplicate in OptionPrint
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
		strings.add(" ");
		strings.add("<u>Original idea</u>: Arnaud Roques");
		strings.add("<u>Word Macro</u>: Alain Bertucat & Matthieu Sabatier");
		strings.add("<u>Word Add-in</u>: Adriaan van den Brand");
		strings.add("<u>Eclipse Plugin</u>: Claude Durif & Anne Pecoil");
		strings.add("<u>Servlet & XWiki</u>: Maxime Sinclair");
		strings.add("<u>Site design</u>: Raphael Cotisson");
		strings.add("<u>Logo</u>: Benjamin Croizet");

		strings.add(" ");
		strings.add("http://plantuml.sourceforge.net");
		strings.add(" ");
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createCheckVersions(String host, String port) {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");

		final int lastversion = extractDownloadableVersion(host, port);

		int lim = 7;
		if (lastversion == -1) {
			strings.add("<b><color:red>Error</b>");
			strings.add("<color:red>Cannot connect to http://plantuml.sourceforge.net/</b>");
			strings.add("Maybe you should set your proxy ?");
			strings.add("@startuml");
			strings.add("checkversion(proxy=myproxy.com,port=8080)");
			strings.add("@enduml");
			lim = 9;
		} else if (lastversion == 0) {
			strings.add("<b><color:red>Error</b>");
			strings.add("Cannot retrieve last version from http://plantuml.sourceforge.net/</b>");
		} else {
			strings.add("<b>Last available version for download</b> : " + lastversion);
			strings.add(" ");
			if (Version.version() >= lastversion) {
				strings.add("<b><color:green>Your version is up to date.</b>");
			} else {
				strings.add("<b><color:red>A newer version is available for download.</b>");
			}
		}

		while (strings.size() < lim) {
			strings.add(" ");
		}

		return new PSystemVersion(true, strings);
	}

	public static int extractDownloadableVersion(String host, String port) {
		if (host != null && port != null) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", port);
		}

		try {
			final URL url = new URL("http://plantuml.sourceforge.net/download.html");
			final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setUseCaches(false);
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				final int lastversion = extractVersion(in);
				in.close();
				urlConnection.disconnect();
				return lastversion;
			}
		} catch (IOException e) {
			Log.error(e.toString());
		}
		return -1;
	}

	private static int extractVersion(BufferedReader in) throws IOException {
		String s;
		final Pattern p = Pattern.compile(".*\\.(\\d{4,5})\\..*");
		while ((s = in.readLine()) != null) {
			final Matcher m = p.matcher(s);
			if (m.matches()) {
				final String v = m.group(1);
				return Integer.parseInt(v);
			}
		}
		return 0;
	}

	public static PSystemVersion createTestDot() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		return new PSystemVersion(false, strings);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE,
				UAntiAliasing.ANTI_ALIASING_ON, image, GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Version)", getClass());
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(strings);
	}

}
