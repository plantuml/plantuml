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
 */
package net.sourceforge.plantuml.version;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
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
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.dedication.Dedication;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class PSystemVersion extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private BufferedImage image;

	PSystemVersion(boolean withImage, List<String> args) {
		this.strings.addAll(args);
		if (withImage) {
			this.image = getPlantumlImage();
		}
	}

	private PSystemVersion(List<String> args, BufferedImage image) {
		this.strings.addAll(args);
		this.image = image;
	}

	public static BufferedImage getPlantumlImage() {
		return getImage("logo.png");
	}

	public static BufferedImage getCharlieImage() {
		return getImage("charlie.png");
	}

	public static BufferedImage getTime01() {
		return getImage("time01.png");
	}

	public static BufferedImage getTime15() {
		return getImage("time15.png");
	}

	public static BufferedImage getPlantumlSmallIcon() {
		return getImage("favicon.png");
	}

	public static BufferedImage getArecibo() {
		return getImage("arecibo.png");
	}

	public static BufferedImage getApple2Image() {
		return getImageWebp("apple2.png");
	}

	private static BufferedImage getImage(final String name) {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream(name);
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}

	private static BufferedImage getImageWebp(final String name) {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream(name);
			final BufferedImage image = Dedication.getBufferedImage(is);
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
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(),
				false, null, getMetadata(), null, 1.0, result.getBackcolor());
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public static PSystemVersion createShowVersion() {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
		if (OptionFlags.ALLOW_INCLUDE) {
			strings.add("Loaded from " + Version.getJarPath());
			if (OptionFlags.getInstance().isWord()) {
				strings.add("Word Mode");
				strings.add("Command Line: " + Run.getCommandLine());
				strings.add("Current Dir: " + new File(".").getAbsolutePath());
				strings.add("plantuml.include.path: " + PreprocessorUtils.getenv("plantuml.include.path"));
			}
		}
		strings.add(" ");
		// strings.add("<b>Stdlib:");
		// Stdlib.addInfoVersion(strings, false);
		// strings.add(" ");

		GraphvizUtils.addDotStatus(strings, true);
		strings.add(" ");
		for (String name : OptionPrint.interestingProperties()) {
			strings.add(name);
		}
		for (String v : OptionPrint.interestingValues()) {
			strings.add(v);
		}

		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createStdLib() {
		final List<String> strings = new ArrayList<String>();
		Stdlib.addInfoVersion(strings, true);
		strings.add(" ");

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
		strings.add(Version.fullDescription());
		GraphvizUtils.addDotStatus(strings, true);
		return new PSystemVersion(false, strings);
	}

	public static PSystemVersion createDumpStackTrace() throws IOException {
		final List<String> strings = new ArrayList<String>();
		final Throwable creationPoint = new Throwable();
		creationPoint.fillInStackTrace();
		for (StackTraceElement ste : creationPoint.getStackTrace()) {
			strings.add(ste.toString());
		}
		return new PSystemVersion(false, strings);
	}

	public static PSystemVersion createKeyDistributor() throws IOException {
		final LicenseInfo license = LicenseInfo.retrieveDistributor();
		BufferedImage im = null;
		final List<String> strings = new ArrayList<String>();
		if (license == null) {
			strings.add("No license found");
		} else {
			strings.add(license.getOwner());
			strings.add(license.getContext());
			strings.add(license.getGenerationDate().toString());
			strings.add(license.getExpirationDate().toString());
			im = LicenseInfo.retrieveDistributorImage(license);
		}
		return new PSystemVersion(strings, im);
	}

	public static PSystemVersion createPath() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.add("<u>Current Dir</u>: " + new File(".").getAbsolutePath());
		strings.add(" ");
		strings.add("<u>Default path</u>:");
		for (File f : ImportedFiles.createImportedFiles(null).getPath()) {
			strings.add(f.getAbsolutePath());
		}
		return new PSystemVersion(true, strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Version)");
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(strings);
	}

}
