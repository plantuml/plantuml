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

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6zm80AmEEBmtNeGqQ-xME_ioXj6A6CylpxzGd5Z4qzs0-0MuTthSRwI_YAvqxvjcH5klBSmPH700wedp"
			+ "RuQMT_qrVlFrLJB6LIrTOG84tZ4kxHj_ULk8gMBxBHFQ-zkF2tL8QuNW2jnKJiZyTAy2XmAoE9jY_ZbW"
			+ "O-FQkxLxdrgxbYvsFw0jq9X6FxQibADsTU1heSR1aN5yGqfcnPWFaQAfZIr0moTkn6gZEi4dKhF_g4Lg"
			+ "qffTGxfB0B6C5B7cRT7SlfFjKnWuuQXaXm94E91mI2zo752VIC1HIgD6P1IZiQxYBKz44A632hlmZvmK"
			+ "dpraUPIX0uxARY2WyHi40NWcnPJOREDcGCuUrYAdmKATcHizalt5u9GZ3nek8JjK8gW5L078cw0BUbrz"
			+ "vX9d6PkYX2TlORpAOe2SPLq0ZYSp-jfBOQ3ytpym4-Hoezfw27FATOnV7HtmKqD6zdxJQfopc8ZvRIx8"
			+ "y8K5E5y0lTGQAiRVD-UDXNW8ZIbr8IVEmUDz586mobAmo4rCSStnaovziPgORrrOB1_XrvcXBCC8NAdO"
			+ "0_Y8bjC7TRw0hXhgduTSTHsVmjOuBTXRX4ydpRdY_rst93DA5z1Q4pv4LqAKBBrWbIqcHGIk0Fesu259"
			+ "RgJgo-HHlYmV4k_xao3Ud70WKfi5x4_A6OrB3bE85rOwLCKCOBfBr0kEserqmwiJqBj0vHGPuQxVquGz"
			+ "RDAOEZUuud4_n1ZaYtIyVD5ZCvUCImsTCcrsxDH-n560s_0bUTLz68vL04jHxvR5qdWQ0-uQvZI3T1sH"
			+ "zprjIOzB7QLbEMvnLPTuDhU3YuTEBZYJHMFCQ7CSCZiadvEfgLkCvwb1uxT1PEts2gBE0dU3TJYGOHcV"
			+ "0zOLamEL7C5H6ivmoCGa2QtMUSLk0t0FechhcEsfQmcndhhw5AWsViJPo81AsWd744UxA-oCXQF8Vjcy"
			+ "LqRTrBSX6CS2yiKWmKagb6s4e39iJY53UX_qZCP9FizBkonas_XcuGM6mYT8TDziWrMc48yKwOsw1i3r"
			+ "Owl4YnyJ8u2VyVORw5d6EkZvDtfQMHp0ff8GC1MMSlYNOALYtQn9hBVeTLjhRylEdt-l8zCunVisLWgW"
			+ "no2B_WiXY9EeSmMte1yNV2P7RI4ZiA-3RLkvDlGumA2WHIsJNBLDC-BJMLQketPuvF6XdFykao2mdqz8"
			+ "Ud4AAaUV4gpMtkfAPQ1zyet5IGuM_nHL6hAuYZfgOiGH";

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
		final List<TextBlock> cols = getCols(getDonors(), COLS, FREE_LINES);
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
