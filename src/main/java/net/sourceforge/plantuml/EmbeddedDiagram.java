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
			if (EmbeddedDiagram.getEmbeddedType(StringUtils.trinNoTrace(s2)) != null)
				// if (StringUtils.trinNoTrace(s2).startsWith(EmbeddedDiagram.EMBEDDED_START))
				nested++;
			else if (StringUtils.trinNoTrace(s2).equals(EmbeddedDiagram.EMBEDDED_END)) {
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
					final String imageSvg = getImageSvg();
					final UImageSvg svg = new UImageSvg(imageSvg, 1);
					return new XDimension2D(svg.getWidth(), svg.getHeight());
				}
				if (stringBounder.matchesProperty("TIKZ")) {
					final UImageTikz tikz = getImageTikz();
					return new XDimension2D(tikz.getWidth(), tikz.getHeight());
				}
				final PortableImage im = getImage();
				return new XDimension2D(im.getWidth(), im.getHeight());
			} else {
				// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
				final TextBlock tb = getInternalTextBlock();
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

	public TextBlock getInternalTextBlock() throws Exception {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
		if (textBlock == null) {
			PSystemBuilder2.getInstance().reset();
			final UgDiagram ugDiagram = (UgDiagram) diagram;
			textBlock = ugDiagram.getTextBlock12026(0, new FileFormatOption(FileFormat.SVG));
		}
		// ::done
		return textBlock;
	}

	public void drawU(UGraphic ug) {
		try {
			if (!TeaVM.isTeaVM()) {
				final boolean isSvg = ug.matchesProperty("SVG");
				if (isSvg) {
					final String imageSvg = getImageSvg();
					final UImageSvg svg = new UImageSvg(imageSvg, 1);
					ug.draw(svg);
					return;
				}
				if (ug.matchesProperty("TIKZ")) {
					ug.draw(getImageTikz());
					return;
				}
				final PortableImage im = getImage();
				final UShape image = new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
				ug.draw(image);
			} else {
				// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
				final TextBlock tb = getInternalTextBlock();
				ug = ug.apply(HColors.transparent().bg());
				tb.drawU(ug);
				PSystemBuilder2.getInstance().reset();
				// ::done
			}
		} catch (Exception e) {
			Logme.error(e);
		}

	}

	private String getImageSvg() throws IOException, InterruptedException {
		if (svg == null)
			svg = getImageSvgSlow().replaceAll("<\\?plantuml.+?\\?>", "");

		return svg;

	}

	private String getImageSvgSlow() throws IOException, InterruptedException {
		if (!TeaVM.isTeaVM()) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			diagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.SVG));
			os.close();
			return new String(os.toByteArray());
		}
		throw new UnsupportedOperationException("TEAVM4586");
	}

	private UImageTikz getImageTikz() throws IOException, InterruptedException {
		if (imageTikz == null)
			imageTikz = getImageTikzSlow();

		return imageTikz;
	}

	private UImageTikz getImageTikzSlow() throws IOException, InterruptedException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final ImageData imageData = diagram.exportDiagram(os, 0,
				new FileFormatOption(FileFormat.LATEX_NO_PREAMBLE));
		os.close();
		return new UImageTikz(new String(os.toByteArray(), StandardCharsets.UTF_8),
				imageData.getWidth(), imageData.getHeight());
	}

	private PortableImage getImage() throws IOException, InterruptedException {
		BrowserLog.consoleLog(getClass(), "=======getImage");
		if (image == null)
			image = getImageSlow();
		return image;
	}

	private PortableImage getImageSlow() throws IOException, InterruptedException {
		BrowserLog.consoleLog(getClass(), "=======getImageSlow");
		if (!TeaVM.isTeaVM()) {
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			diagram.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
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

		final String s = StringUtils.trin(cs.toString());
		if (s.startsWith(EMBEDDED_START) == false)
			return null;

		switch (s) {
		case EMBEDDED_START:
			return "uml";
		case EMBEDDED_START + "ditaa":
			return "ditaa";
		case EMBEDDED_START + "salt":
			return "salt";
		case EMBEDDED_START + "uml":
			return "uml";
		case EMBEDDED_START + "wbs":
			return "wbs";
		case EMBEDDED_START + "mindmap":
			return "mindmap";
		case EMBEDDED_START + "gantt":
			return "gantt";
		case EMBEDDED_START + "json":
			return "json";
		case EMBEDDED_START + "yaml":
			return "yaml";
		case EMBEDDED_START + "wire":
			return "wire";
		case EMBEDDED_START + "creole":
			return "creole";
		case EMBEDDED_START + "board":
			return "board";
		case EMBEDDED_START + "ebnf":
			return "ebnf";
		case EMBEDDED_START + "regex":
			return "regex";
		case EMBEDDED_START + "files":
			return "files";
		case EMBEDDED_START + "chronology":
			return "chronology";
		case EMBEDDED_START + "chen":
			return "chen";
		case EMBEDDED_START + "chart":
			return "chart";
		case EMBEDDED_START + "nwdiag":
			return "nwdiag";
		case EMBEDDED_START + "packetdiag":
			return "packetdiag";
		default:
			return null;
		}
	}

}
