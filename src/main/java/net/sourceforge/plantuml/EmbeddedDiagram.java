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
package net.sourceforge.plantuml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Neutron;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.Line;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.UImageTikz;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.teavm.PSystemBuilder2;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.text.StringLocated;

public class EmbeddedDiagram extends TextBlockMemoized implements Line, Atom {

	public static final String EMBEDDED_START = "{{";
	public static final String EMBEDDED_END = "}}";

	private PortableImage image;
	private String svg;
	private UImageTikz imageTikz;
	private TextBlock textBlock;

	private final Diagram diagram;

	private EmbeddedDiagram(ISkinSimple skinParam, List<StringLocated> strings) {

		final Previous previous = skinParam == null ? Previous.createEmpty() : Previous.createFrom(skinParam.values());
		final BlockUml blockUml = new BlockUml(null, PathSystem.fetch(), strings, Defines.createEmpty(), previous,
				null);

		this.diagram = blockUml.getDiagram();

	}

	public static EmbeddedDiagram createAndSkip(String type, Iterator<CharSequence> it, ISkinSimple skinParam) {
		final List<String> result = new ArrayList<String>();
		result.add("@start" + type);
		int nested = 1;
		while (it.hasNext()) {
			final CharSequence s2 = it.next();
			if (EmbeddedDiagram.getEmbeddedType(s2) != null)
				nested++;
			else if (StringUtils.trim2(s2.toString()).equals(EmbeddedDiagram.EMBEDDED_END)) {
				nested--;
				if (nested == 0)
					break;
			}
			result.add(s2.toString());
		}
		result.add("@end" + type);
		return EmbeddedDiagram.from(skinParam, result);

	}

	public static EmbeddedDiagram from(ISkinSimple skinParam, List<String> strings) {
		return new EmbeddedDiagram(skinParam, BlockUml.convert(strings));
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		try {
			if (!TeaVM.isTeaVM()) {
				if (stringBounder.matchesProperty("SVG")) {
					final String imageSvg = getImageSvg(stringBounder.getFileFormat());
					final UImageSvg svg = new UImageSvg(imageSvg, 1);
					return new XDimension2D(svg.getWidth(), svg.getHeight());
				}
				if (stringBounder.matchesProperty("TIKZ")) {
					final UImageTikz tikz = getImageTikz(stringBounder.getFileFormat());
					return new XDimension2D(tikz.getWidth(), tikz.getHeight());
				}
				final PortableImage im = getImage(stringBounder.getFileFormat());
				return new XDimension2D(im.getWidth(), im.getHeight());
			} else {
				// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
				final TextBlock tb = getInternalTextBlock(stringBounder.getFileFormat());
				final XDimension2D result = tb.calculateDimension(stringBounder);
				PSystemBuilder2.getInstance().reset();
				return result;
				// ::done
			}
		} catch (Exception e) {
			Logme.error(e);
		}
		return new XDimension2D(42, 42);
	}

	public TextBlock getInternalTextBlock(FileFormat fileFormat) throws Exception {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
		if (textBlock == null) {
			PSystemBuilder2.getInstance().reset();
			final UgDiagram ugDiagram = (UgDiagram) diagram;
			textBlock = ugDiagram.getTextBlock12026(0, new FileFormatOption(fileFormat));
		}
		// ::done
		return textBlock;
	}

	public void drawU(UGraphic ug) {
		try {
			final StringBounder stringBounder = ug.getStringBounder();
			if (!TeaVM.isTeaVM()) {
				final boolean isSvg = ug.matchesProperty("SVG");
				if (isSvg) {
					final String imageSvg = getImageSvg(stringBounder.getFileFormat());
					final UImageSvg svg = new UImageSvg(imageSvg, 1);
					ug.draw(svg);
					return;
				}
				if (ug.matchesProperty("TIKZ")) {
					ug.draw(getImageTikz(stringBounder.getFileFormat()));
					return;
				}
				final PortableImage im = getImage(stringBounder.getFileFormat());
				final UShape image = new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
				ug.draw(image);
			} else {
				// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
				final TextBlock tb = getInternalTextBlock(stringBounder.getFileFormat());
				ug = ug.apply(HColors.transparent().bg());
				tb.drawU(ug);
				PSystemBuilder2.getInstance().reset();
				// ::done
			}
		} catch (Exception e) {
			Logme.error(e);
		}

	}

	private String getImageSvg(FileFormat fileFormat) throws IOException, InterruptedException {
		if (svg == null)
			svg = getImageSvgSlow(fileFormat).replaceAll("<\\?plantuml.+?\\?>", "");

		return svg;

	}

	private String getImageSvgSlow(FileFormat fileFormat) throws IOException, InterruptedException {
		if (!TeaVM.isTeaVM()) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			diagram.exportDiagram(os, 0, new FileFormatOption(fileFormat));
			os.close();
			return new String(os.toByteArray());
		}
		throw new UnsupportedOperationException("TEAVM4586");
	}

	private UImageTikz getImageTikz(FileFormat fileFormat) throws IOException, InterruptedException {
		if (imageTikz == null)
			imageTikz = getImageTikzSlow(fileFormat);

		return imageTikz;
	}

	private UImageTikz getImageTikzSlow(FileFormat fileFormat) throws IOException, InterruptedException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final ImageData imageData = diagram.exportDiagram(os, 0, new FileFormatOption(fileFormat));
		os.close();
		return new UImageTikz(new String(os.toByteArray(), StandardCharsets.UTF_8), imageData.getWidth(),
				imageData.getHeight());
	}

	private PortableImage getImage(FileFormat fileFormat) throws IOException, InterruptedException {
		BrowserLog.consoleLog(getClass(), "=======getImage");
		if (image == null)
			image = getImageSlow(fileFormat);
		return image;
	}

	private PortableImage getImageSlow(FileFormat fileFormat) throws IOException, InterruptedException {
		BrowserLog.consoleLog(getClass(), "=======getImageSlow");
		if (!TeaVM.isTeaVM()) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			diagram.exportDiagram(os, 0, new FileFormatOption(fileFormat));
			os.close();
			return SImageIO.read(os.toByteArray());
		}
		throw new UnsupportedOperationException("TEAVM4585");
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return HorizontalAlignment.LEFT;
	}

	@Override
	public List<Neutron> getNeutrons() {
		return Arrays.asList(Neutron.create(this));
	}

	public static String getEmbeddedType(CharSequence cs) {
		if (cs == null)
			return null;

		final int len = cs.length();

		// Skip leading whitespace
		int p = 0;
		while (p < len && Character.isWhitespace(cs.charAt(p)))
			p++;

		// Must start with "{{"
		if (p + 2 > len || cs.charAt(p) != '{' || cs.charAt(p + 1) != '{')
			return null;
		p += 2;

		// Find end (skip trailing whitespace)
		int end = len;
		while (end > p && Character.isWhitespace(cs.charAt(end - 1)))
			end--;

		final int suffixLen = end - p;

		// "{{" alone -> uml
		if (suffixLen == 0)
			return "uml";

		// Dispatch on first char to avoid scanning every keyword
		switch (cs.charAt(p)) {
		case 'b':
			if (match(cs, p, end, "board"))
				return "board";
			break;
		case 'c':
			if (match(cs, p, end, "creole"))
				return "creole";
			if (match(cs, p, end, "chronology"))
				return "chronology";
			if (match(cs, p, end, "chen"))
				return "chen";
			if (match(cs, p, end, "chart"))
				return "chart";
			break;
		case 'd':
			if (match(cs, p, end, "ditaa"))
				return "ditaa";
			break;
		case 'e':
			if (match(cs, p, end, "ebnf"))
				return "ebnf";
			break;
		case 'f':
			if (match(cs, p, end, "files"))
				return "files";
			break;
		case 'g':
			if (match(cs, p, end, "gantt"))
				return "gantt";
			break;
		case 'j':
			if (match(cs, p, end, "json"))
				return "json";
			break;
		case 'm':
			if (match(cs, p, end, "mindmap"))
				return "mindmap";
			break;
		case 'n':
			if (match(cs, p, end, "nwdiag"))
				return "nwdiag";
			break;
		case 'p':
			if (match(cs, p, end, "packetdiag"))
				return "packetdiag";
			break;
		case 'r':
			if (match(cs, p, end, "regex"))
				return "regex";
			break;
		case 's':
			if (match(cs, p, end, "salt"))
				return "salt";
			break;
		case 'u':
			if (match(cs, p, end, "uml"))
				return "uml";
			break;
		case 'w':
			if (match(cs, p, end, "wbs"))
				return "wbs";
			if (match(cs, p, end, "wire"))
				return "wire";
			break;
		case 'y':
			if (match(cs, p, end, "yaml"))
				return "yaml";
			break;
		}
		return null;
	}

	private static boolean match(CharSequence cs, int from, int end, String key) {
		final int klen = key.length();
		if (end - from != klen)
			return false;
		for (int i = 0; i < klen; i++)
			if (cs.charAt(from + i) != key.charAt(i))
				return false;
		return true;
	}

}
