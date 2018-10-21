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
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.StringCompressorNone;
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
	public static final String DONORS = "6oq809m7nctnXhJewjSHZX8n3MVAYw086_hm054IjRdwqUp4_JWT81c0qvE5N5zqDfkGDMFIZSqPpZEA"
			+ "Oa7GUDPF7RQvsHzYgMp_PU9sNGTigUPAY0UnNlhpz-w1eq0MjocFC0vilaMDlMmEkoSg6tJxTDy7_Hze"
			+ "JAENBVkKjNs_Z-F8-XAoLMPVamaYHTyS9QnaASP-H8gkRcm1Ekvq8zutkcDy8VtZFse1JLgpQKXu3IYM"
			+ "aIBMlErj_ZZ0-0v6CQ0QwWu72GY74eVy0eE_Fv40AhLgr8YEOXhhpjvv8e494rHOmJzoafrRoF8eGmSg"
			+ "ZQkXWCvFu0HWRuehuMphDY1N2xR8Qx1KDwuN8bBxY-4fBWyDbu4T6X6K0ge0yYReWb-bpLpIkOvTKSEV"
			+ "t9NnbWr0tcJWiVHaeQyphmoKh_LJCr5T7jIt5b8vvJh7lyu6UTQGaNkxQJskMOp7_36NnF2b1SWlG2zj"
			+ "6oh6yulpPY41mcDANKZMENHydI9WrbaA2x-8AQzn_-WoYzN5V6l1GZt4NpSEPHb6w4h4DQ1lQBcShFE5"
			+ "vAwX_dt8rQxZMt2DoyAz9MfssfpFVo-xfPbHUe3MDz57wCg8SiE-QfKh9aC4hW3z6F8Ga3jGVviygc_B"
			+ "X_ZR_e32cnSEH6apflr5TKQZo64LPi6amrqi09ExHBbGQTr863vreAjGvH8PuQw_Rb8zRDgOE6_KZciz"
			+ "mXZKb-a4-IBYOYw8aeOkMJPpXxLtE0euWuBp-h6NvQm2cDVqRbiiZHSPyFSmfndIVKG-ZhGb4KKT9RES"
			+ "jtXM5tzkRYT_7ZhrXsD-5lMySVp1uyyXOz5yJPzAwXPZxXeqVrj2bhKxb9G5LiDr-q0DPZm6dC-QF5F9"
			+ "4IgruGo7Gan0w6qxhnq52xYjasgiSxQdfYNCtdFr4EWcFonl8539Tznnn3dk59wXF8hi5-UzCkgclWz9"
			+ "SHQGFuO9BbAXt4P8KWsRCyYGVV_gwOuEdldI5WkvRVnIUCoXy0aJhHitq365z6YHtwZh05pFJPrw2MDY"
			+ "1F0zJh-X7ZNh8AEDbbQC0xYb90IeWfBR_-7iL2gxDOtr5hrksxQFPUEl_ynP9ZabuZ9M2v3g82h_nHJ-"
			+ "J5qKuE2k_lU2Zwgq5Z82J-facoifz000";

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
		final List<TextBlock> cols = getCols(getDonors(), 6, 5);
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
		final Transcoder t = new TranscoderImpl(new AsciiEncoder(), new StringCompressorNone(), new CompressionBrotli());
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
