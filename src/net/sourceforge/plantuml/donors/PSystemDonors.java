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

	public static final String DONORS = "6w8602mIZ6r6h8Vz5iSz4U4e4OD_QwRy4VHnsX9GMiAKM7PtB6kpHqv0K9vJl6yGnOAW0UkI7k_btCRE"
			+ "s8BWkQAuWWgGLz6DShx6dP852fGRwsVRI0vl_tyk5zYzA93UUPlo1CFZW6viQeo14x1jzwEoGVz5Tu4M"
			+ "y3IwiuP5bpLRFaGClgArQvZPYpGGGYk_WL9IPoX6M28br2viuHIIWn7KMkTrn3FSldvIBrABZLaivJaJ"
			+ "oArsSJJV_h6thcJxSMQ9e1_gWAm3E803vKvE64T-i57vrI11PGwwiQcIcITkY8X10xtnG-cKb9AApE78"
			+ "H1Lm1KY4_1aZ08z7DNLoDZSDA5aoM_Gi2UhKUk1kZfTfARKl6awI0m5Ewe1M2UG_q1rwxpBxIuDdNGmn"
			+ "Kz_9t6JpK6n4TF7zCT2db1lwv0b0xSoSTREfNKggSUcllJx6Ne0ebffzb3VzZNUsO5x_FOHql5Y0yW8w"
			+ "rpiYviyVryb29aHpwVQ22-VYoTL5GDeu31O-n9BLr8TtCM98cgSuYt2utg3QY40dLrrS1gwkbSnNErTW"
			+ "cgN5zLkVXjNh8DSarUPzYrRIYwNiVuHtDMiP3jhlOapN9ObmpJXPTGUBBWB3eEs1i99SIBNDzLFTl7_B"
			+ "Flj9S6z396653P4wKDVJb6yTLX5lLBhKn02m_f2eTrH13_4WgmFeCgYymjCHTZjSBTEdZl7QQKqUmcuC"
			+ "wgiwKxdZFZoKliCmAOzcTTNs5kS0BgDWBZ_RsBwAO_KCxx4NeNbE1iOjFUY1z1rMrhNxI-xR0c5ciHRS"
			+ "BMI_7hxpvw6RVv_-B1gvkl-v_WzpLJn-9zIkjvJbiAJRctLPnVSYgWZGEq6xG7LcF0JiEvCJrJb2vJU3"
			+ "6V3m98cmFhYuFh7WDX2rPMLdAJP4R4UixWCjoG-LJXfLrC2IzTxkf3mZMbIptvntOOx7Ogb2hmMKowg2"
			+ "QpGGzff2rVGvMLHSSW00";

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
