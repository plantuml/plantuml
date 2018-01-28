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
 *
 */
package net.sourceforge.plantuml.donors;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {
	public static final String DONORS = "6-860AmER5DRWVz05mqtRsdC3wE5OekdFiA99Jtd00yCChUnCxOWUAuehg02v8LOSzl-wuP5HI5qgCA_"
			+ "ggpzBTFN3kjWmEMYEWvoSaK1Cfs8SH6qMc-nrEca90_odEvgROGc6Whp7KzqIYH-9gGNJuX8r4WIYxya"
			+ "Q99kIF-42Y5hIz2iaM2fNV3h4EVWF3XDLej3CUmhKhpjIJUlLtOy56inEI8Fr1nyeswQcytLB1Lv4eXp"
			+ "b1IwVXvj7WTj_Y79naykn8P70Io06icBLc3iEAZWk0HOoA_9PcoKSUU93Iw2q8dxyKLPCBobAciwjd3S"
			+ "Ne30wmz10EczYbhORFese4XBQz6qfAAEZoynbTaaJadrqd2Cjd6pH0pace3y1xtWtTkgBQzdl0mRITqX"
			+ "-HutXKQkzGRVuK2Vo094no00zSHC7y-FwjbIKgtsERxpR82xfLBDzaDFySe98SUVXr0M6mlW3T2XiR2M"
			+ "l5iTDmlT8LwjRADUd3EJTnC1SLEcWONU9fjhqbUwoD5GENOJ5gQs3iURKO6Mk2ho1lX5Ay97dHEGihJu"
			+ "t7GzRNmcjYRYmjwNa6zMirJu_zoBp7890MqDuqjqCM6rO0wi6fCi4O4rGFy6tDGaBvhhxLgEjzTDuUgc"
			+ "WNYhaecosWnW_rJSs7HDXRWwRknicGF0ojwAs3WUMqH8Q0Tq7bIUKMKHzjzTakMBnkYna7DyY8anY2RT"
			+ "NNaHStkefQjXH5sRfOFQAreJS846XKZBgMqL3HW8HC-cUZsCWNgb1vhkCyfv7ELRCTq2AyloMDLbeN16"
			+ "-CdFGrT-rDsYBX8ztVOFwOjwlv97bFxavDqIpMzAP4KlCsWVWIYrV1qYbd4RN1zCzjHY6iTb1ScniqKJ"
			+ "FcD38ByKS4kFzRmUwRHcKrpLTNKJ2kE7-A85ge1D867dV4VR6OQZijusxqWQs-8rX7aNK3872PxIYTKO"
			+ "ANIahoLaPDpiwRTEEtvPamLwZSgCFCXGk4AYY0C0";

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private UDrawable getGraphicStrings() throws IOException {
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings.createBlackOnWhite(Arrays
						.asList("<b>Special thanks to our sponsors and donors !"));
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(new UTranslate(0, header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(new UTranslate(x, 0)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(PSystemVersion.getPlantumlImage());
				ug.apply(new UTranslate(lastX, y - logo.getHeight())).draw(logo);
			}
		};
	}

	public static List<TextBlock> getCols(List<String> lines, final int nbCol, final int reserved) throws IOException {
		final List<TextBlock> result = new ArrayList<TextBlock>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<String>();
		final Transcoder t = new TranscoderImpl(new CompressionBrotli());
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Donors)");
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
