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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.creole.Neutron;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.Line;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.text.StringLocated;

public class EmbeddedDiagram extends AbstractTextBlock implements Line, Atom {
	// ::remove file when __HAXE__

	public static final String EMBEDDED_START = "{{";
	public static final String EMBEDDED_END = "}}";

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
			case EMBEDDED_START + "packetdiag":
				return "packetdiag";
			default:
				return null;
		}
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

	private final List<StringLocated> list;
	private final ISkinSimple skinParam;
	private PortableImage image;
	private String svg;

	private EmbeddedDiagram(ISkinSimple skinParam, List<StringLocated> system) {
		this.list = system;
		this.skinParam = skinParam;
	}

	public static EmbeddedDiagram from(ISkinSimple skinParam, List<String> strings) {
		return new EmbeddedDiagram(skinParam, BlockUml.convert(strings));
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		try {
			if (stringBounder.matchesProperty("SVG")) {
				final String imageSvg = getImageSvg();
				final UImageSvg svg = new UImageSvg(imageSvg, 1);
				return new XDimension2D(svg.getWidth(), svg.getHeight());
			}
			final PortableImage im = getImage();
			return new XDimension2D(im.getWidth(), im.getHeight());
		} catch (IOException e) {
			Logme.error(e);
		} catch (InterruptedException e) {
			Logme.error(e);
		}
		return new XDimension2D(42, 42);
	}

	public void drawU(UGraphic ug) {
		try {
			final boolean isSvg = ug.matchesProperty("SVG");
			if (isSvg) {
				final String imageSvg = getImageSvg();
				final UImageSvg svg = new UImageSvg(imageSvg, 1);
				ug.draw(svg);
				return;
			}
			final PortableImage im = getImage();
			final UShape image = new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
			ug.draw(image);
		} catch (IOException e) {
			Logme.error(e);
		} catch (InterruptedException e) {
			Logme.error(e);
		}

	}

	private String getImageSvg() throws IOException, InterruptedException {
		if (svg == null)
			svg = getImageSvgSlow().replaceAll("<\\?plantuml.+?\\?>", "");

		return svg;

	}

	private String getImageSvgSlow() throws IOException, InterruptedException {
		final Diagram system = getSystem();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		system.exportDiagram(os, 0, new FileFormatOption(FileFormat.SVG));
		os.close();
		return new String(os.toByteArray());
	}

	private PortableImage getImage() throws IOException, InterruptedException {
		if (image == null)
			image = getImageSlow();
		return image;
	}

	private PortableImage getImageSlow() throws IOException, InterruptedException {
		final Diagram system = getSystem();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		system.exportDiagram(os, 0, new FileFormatOption(FileFormat.PNG));
		os.close();
		return SImageIO.read(os.toByteArray());
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return HorizontalAlignment.LEFT;
	}

	private Diagram getSystem() throws IOException, InterruptedException {
		final Previous previous = skinParam == null ? Previous.createEmpty() : Previous.createFrom(skinParam.values());
		final BlockUml blockUml = new BlockUml(PathSystem.fetch(), list, Defines.createEmpty(), previous, null, null);
		return blockUml.getDiagram();

	}

	@Override
	public List<Neutron> getNeutrons() {
		return Arrays.asList(Neutron.create(this));
	}

}
