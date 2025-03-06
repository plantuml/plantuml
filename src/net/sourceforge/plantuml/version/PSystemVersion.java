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
 */
package net.sourceforge.plantuml.version;

import static net.sourceforge.plantuml.klimt.geom.GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.PlainStringsDiagram;
import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class PSystemVersion extends PlainStringsDiagram {

	PSystemVersion(UmlSource source, boolean withImage, List<String> args, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.strings.addAll(args);
		try {
			if (withImage) {
				this.image = getPlantumlImage();
				this.imagePosition = BACKGROUND_CORNER_BOTTOM_RIGHT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PSystemVersion(UmlSource source, List<String> args, BufferedImage image, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.strings.addAll(args);
		this.image = image;
		this.imagePosition = BACKGROUND_CORNER_BOTTOM_RIGHT;
	}

	public static BufferedImage getPlantumlImage() {
		return getImage("logo.png");
	}

	public static BufferedImage getTime01() {
		return getImage("time01.png");
	}

	public static BufferedImage getTime15() {
		return getImage("time15.png");
	}

	public static BufferedImage getCharlieImage() {
		return getImage("charlie.png");
	}

	public static BufferedImage getPlantumlSmallIcon() {
		return getImage("favicon.png");
	}

	// ::comment when __CORE__
	public static BufferedImage getArecibo() {
		return getImage("arecibo.png");
	}

	public static BufferedImage getDotc() {
		return getImage("dotc.png");
	}

	public static BufferedImage getDotd() {
		return getImage("dotd.png");
	}

	public static BufferedImage getApple2Image() {
		return getImageWebp("apple2.png");
	}
	// ::done

	private static BufferedImage getImage(final String name) {
		try {
			final InputStream is = PSystemVersion.class.getResourceAsStream(name);
			final BufferedImage image = SImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			Logme.error(e);
		}
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}

	// ::comment when __CORE__
	private static BufferedImage getImageWebp(final String name) {
		try (InputStream is = PSystemVersion.class.getResourceAsStream(name)) {
			return SFile.getBufferedImageFromWebpButHeader(is);
		} catch (IOException e) {
			Logme.error(e);
		}
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}
	// ::done

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

	public static PSystemVersion createShowVersion2(UmlSource source, PreprocessingArtifact preprocessing) {
		final ReportLog strings = new ReportLog();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
		// :: uncomment when __CORE__
//		strings.add(" ");
//		strings.add("Compiled with CheerpJ 2.3");
//		strings.add("Powered by CheerpJ, a Leaning Technologies Java tool");
		// :: done
		// :: comment when __CORE__
		strings.checkOldVersionWarning();
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE) {
			strings.add("Loaded from " + Version.getJarPath());

			if (OptionFlags.getInstance().isWord()) {
				strings.add("Word Mode");
				strings.add("Command Line: " + Run.getCommandLine());
				strings.add("Current Dir: " + new SFile(".").getAbsolutePath());
				strings.add("plantuml.include.path: " + PreprocessorUtils.getenv(SecurityUtils.PATHS_INCLUDES));
			}
		}
		strings.add(" ");

		GraphvizUtils.addDotStatus(strings, true);
		strings.add(" ");
		for (String name : OptionPrint.interestingProperties())
			strings.add(name);

		for (String v : OptionPrint.interestingValues())
			strings.add(v);

		// ::done

		return new PSystemVersion(source, true, strings.asList(), preprocessing);
	}

	// :: comment when __CORE__
	public static PSystemVersion createStdLib(UmlSource source, PreprocessingArtifact preprocessing) {
		final List<String> strings = new ArrayList<>();
		Stdlib.addInfoVersion(strings, true);
		strings.add(" ");

		return new PSystemVersion(source, true, strings, preprocessing);
	}
	// ::done

	public static PSystemVersion createShowAuthors2(UmlSource source, PreprocessingArtifact preprocessing) {
		// Duplicate in OptionPrint
		final List<String> strings = getAuthorsStrings(true);
		return new PSystemVersion(source, true, strings, preprocessing);
	}

	public static List<String> getAuthorsStrings(boolean withTag) {
		final List<String> strings = new ArrayList<>();
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
		add(strings, "<u>Web Assembly</u>: Sakir Temel", withTag);

		add(strings, " ", withTag);
		add(strings, "https://plantuml.com", withTag);
		add(strings, " ", withTag);
		return strings;
	}

	private static void add(List<String> result, String s, boolean withTag) {
		if (withTag == false)
			s = s.replaceAll("\\</?\\w+\\>", "");

		result.add(s);

	}

	// ::comment when __CORE__
	public static PSystemVersion createTestDot(UmlSource source, PreprocessingArtifact preprocessing) throws IOException {
		final ReportLog strings = new ReportLog();
		strings.add(Version.fullDescription());
		GraphvizUtils.addDotStatus(strings, true);
		return new PSystemVersion(source, false, strings.asList(), preprocessing);
	}
	// ::done

//	public static PSystemVersion createDumpStackTrace() throws IOException {
//		final List<String> strings = new ArrayList<>();
//		final Throwable creationPoint = new Throwable();
//		creationPoint.fillInStackTrace();
//		for (StackTraceElement ste : creationPoint.getStackTrace()) {
//			strings.add(ste.toString());
//		}
//		return new PSystemVersion(false, strings);
//	}

	// ::comment when __CORE__
	public static PSystemVersion createKeyDistributor(UmlSource source, PreprocessingArtifact preprocessing) throws IOException {
		final LicenseInfo license = LicenseInfo.retrieveDistributor();
		BufferedImage im = null;
		final List<String> strings = new ArrayList<>();
		if (license == null) {
			strings.add("No license found");
		} else {
			strings.add(license.getOwner());
			strings.add(license.getContext());
			strings.add(license.getGenerationDate().toString());
			strings.add(license.getExpirationDate().toString());
			im = LicenseInfo.retrieveDistributorImage(license);
		}
		return new PSystemVersion(source, strings, im, preprocessing);
	}
	// ::done

//	public static PSystemVersion createPath(UmlSource source) throws IOException {
//		final List<String> strings = new ArrayList<>();
//		strings.add("<u>Current Dir</u>: " + new SFile(".").getPrintablePath());
//		strings.add(" ");
//		strings.add("<u>Default path</u>:");
//		for (SFile f : ImportedFiles.createImportedFiles(null).getPath()) {
//			strings.add(f.getPrintablePath());
//		}
//		return new PSystemVersion(source, true, strings);
//	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Version)");
	}

	public List<String> getLines() {
		return Collections.unmodifiableList(strings);
	}

}
