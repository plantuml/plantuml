/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 */
package net.sourceforge.plantuml.version;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.PreprocessorInclude;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

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
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
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
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage getTime() {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream("time00.png");
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
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
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}

	private static BufferedImage transparentIcon;

	public static BufferedImage getPlantumlSmallIcon2() {
		if (transparentIcon != null) {
			return transparentIcon;
		}
		final BufferedImage ico = getPlantumlSmallIcon();
		if (ico == null) {
			return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
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

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final TextBlockBackcolored result = GraphicStrings.createBlackOnWhite(strings, image,
				GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT);
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, result.getBackcolor(),
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public static PSystemVersion createShowVersion() {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
		strings.add("Loaded from " + Version.getJarPath());
		if (OptionFlags.getInstance().isWord()) {
			strings.add("Word Mode");
			strings.add("Command Line: " + Run.getCommandLine());
			strings.add("Current Dir: " + FileSystem.getInstance().getCurrentDir().getAbsolutePath());
			strings.add("plantuml.include.path: " + PreprocessorInclude.getenv("plantuml.include.path"));
		}
		strings.add(" ");
		strings.add("<b>Stdlib:");
		Stdlib.addInfoVersion(strings);
		strings.add(" ");

		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		strings.add(" ");
		for (String name : OptionPrint.interestingProperties()) {
			strings.add(name);
		}
		for (String v : OptionPrint.interestingValues()) {
			strings.add(v);
		}

		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createShowAuthors() {
		// Duplicate in OptionPrint
		final List<String> strings = getAuthorsStrings(true);
		return new PSystemVersion(true, strings);
	}

	public static List<String> getAuthorsStrings(boolean withTag) {
		final List<String> strings = new ArrayList<String>();
		add(strings, "<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")",
				withTag);
		add(strings, "(" + License.getCurrent() + " source distribution)", withTag);
		add(strings, " ", withTag);
		add(strings, "<u>Original idea</u>: Arnaud Roques", withTag);
		add(strings, "<u>Word Macro</u>: Alain Bertucat & Matthieu Sabatier", withTag);
		add(strings, "<u>Word Add-in</u>: Adriaan van den Brand", withTag);
		add(strings, "<u>J2V8 & viz.js integration</u>: Andreas Studer", withTag);
		add(strings, "<u>Official Eclipse Plugin</u>: Hallvard Tr\u00E6tteberg", withTag);
		add(strings, "<u>Original Eclipse Plugin</u>: Claude Durif & Anne Pecoil", withTag);
		add(strings, "<u>Servlet & XWiki</u>: Maxime Sinclair", withTag);
		add(strings, "<u>Docker</u>: David Ducatel", withTag);
		add(strings, "<u>AWS lib</u>: Chris Passarello", withTag);
		add(strings, "<u>Stdlib Icons</u>: tupadr3", withTag);
		add(strings, "<u>Site design</u>: Raphael Cotisson", withTag);
		add(strings, "<u>Logo</u>: Benjamin Croizet", withTag);

		add(strings, " ", withTag);
		add(strings, "http://plantuml.com", withTag);
		add(strings, " ", withTag);
		return strings;
	}

	private static void add(List<String> result, String s, boolean withTag) {
		if (withTag == false) {
			s = s.replaceAll("\\</?\\w+\\>", "");
		}
		result.add(s);

	}

	public static PSystemVersion createCheckVersions(String host, String port) {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");

		final int lastversion = extractDownloadableVersion(host, port);

		int lim = 7;
		if (lastversion == -1) {
			strings.add("<b><color:red>Error");
			strings.add("<color:red>Cannot connect to http://plantuml.com/");
			strings.add("Maybe you should set your proxy ?");
			strings.add("@startuml");
			strings.add("checkversion(proxy=myproxy.com,port=8080)");
			strings.add("@enduml");
			lim = 9;
		} else if (lastversion == 0) {
			strings.add("<b><color:red>Error</b>");
			strings.add("Cannot retrieve last version from http://plantuml.com/");
		} else {
			strings.add("<b>Last available version for download</b> : " + lastversion);
			strings.add(" ");
			if (Version.version() >= lastversion) {
				strings.add("<b><color:green>Your version is up to date.");
			} else {
				strings.add("<b><color:red>A newer version is available for download.");
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
			final URL url = new URL("http://plantuml.com/download");
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
		final Pattern p = Pattern.compile(".*\\.([1-9]\\d?)\\.(20\\d\\d)\\.([1-9]?\\d)\\..*");
		while ((s = in.readLine()) != null) {
			final Matcher m = p.matcher(s);
			if (m.matches()) {
				final String a = m.group(1);
				final String b = m.group(2);
				final String c = m.group(3);
				return Integer.parseInt(a) * 1000000 + Integer.parseInt(b) * 100 + Integer.parseInt(c);
			}
		}
		return 0;
	}

	public static PSystemVersion createTestDot() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		return new PSystemVersion(false, strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Version)");
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(strings);
	}

}
