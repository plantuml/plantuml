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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends PlainDiagram {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6sWD0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSaPHV13q"
			+ "W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6jIptfDXjcof3Il3lEr_v9UWTth51eZbAgyMHwq-_8GFlEsFp"
			+ "x3GsNZsQe2y2FmRsrVzTJelBGo7Cqcm5Hc3KQOHRtMF_7mLLrwXs9rN3JZ-cKxagXgkYAh7PzgQ8LjBS"
			+ "CIlKjVDHeyb5CMafKjoja8RRw4neoCdi4sI31eqVweVmPdE5fOmpWJZQ4A7hwxqT-quilr3YWyWHOe7X"
			+ "WJcGSbWMKUBSKD1m262ecqKrjgHopnCZAFpGGjxyeP5KF5BEd0uN3QS7GE3s32gGz44Dcd1sw6v0nP5i"
			+ "qM69gwwC6qlpTLYUoXv9myt4mic2La0DGVu7NU3fiSP7KsVR38wadYPvdROIzMVYxRJD1NsZQvE4zRMr"
			+ "QQQFAqLP8jMrZS_HNlC8mDrIAw_uCkQ8vm3DVlEg8giVBP0VWUwh2LloPzdlB2GXh6tO1-ewLnyULOHZ"
			+ "CcIQMCVlPjjhsIzq1IORaB02BEdlXgyhJN2mXQvS7W6Uq2gcMUS3XRIqUh_eyMNjFk6QOebxuwBtCS9I"
			+ "-kzNVPWPYWDqeCmiw9e5Yt8rhTgBBOXWZz2f0JU5P2ESLzVbkFguI1_v2O2ta16GmuRG2F7yaahNKGZL"
			+ "aQbhqbm0p9zMsgBXieH1kNP1DwDAB5HMgprELsVwiZ5td_J1yrQ3COfZEgcniDozAjB3i5WrchDtQqTf"
			+ "Ji4p-JZEgzvpC5S1hYsyHtnmppyo46TwPI1ISiXZtuT_PEbMOJ6BDjVDgdHNzOE3Xsu-S3UBPZ6ZJ1wo"
			+ "B1WSJqHB_KhafoN3tq78PlbhTR5Fi3bTfuO8CxBWzjBacfeSqt2OpdAix2A4HorUSIeeN4ImbwKbsSAc"
			+ "K7d6wb58oE-OJo9IMHPA0zQMkmBFf7iKvE6y1rOjgrjJIIwWYcmcY4aBLhLKSQIl1J9mhQ9dfdLY-zC2"
			+ "2-hI7cFY38VYAoRQgjoGWIamCqJuXK4afW-afVIIaqG4_kVFNUgbrphuyPqnV38EaZqa4L15ndJgYMgc"
			+ "xdOC6yPwYRzVB-Nc8_ZphptQvDHez_mCGRGE4W8mmWkubFk54JmD_7Ksz08EiKA8DXAkNBNqB92mO3FU"
			+ "kObwS9jHlOd9ScDoqIloGnN_DfMeS38swBY8fJXQBAdIoyrgK0Ljt_okmqqEYh-BAaCYkUpDpcGaR1Ig"
			+ "E3V7RcqOch4P0tXG7RK9jDtrsscMLuqvNv5K6Pu6vDiDEPNg4LfOKzFoiH1OBSRjlQNzbDFZFnde5zGw"
			+ "-zYhXuQ3N5tVRgBXMnUadp6I62vITaJGMsfEBF3mJGHvKIsKZgLjC-pimGhv3RoC5n8EsS47abfUfQno"
			+ "GmxTGlQEQfIVAAA6i40kB5ReAcAXbEE154d1dDKoaI7be2_OrnE0KnGSx-yj29UTQ7v6i_RsZ48obsxk"
			+ "9XL4DfoVHMAJagqUER2VbhbiyuhSZbgamuvzExxZVFiZwkQB2382CIq9NgiBVNTZbraQDjEUwpGuZilS"
			+ "F9Zy0dP5EqyPs1ecMv2bR0OgdWGWTiYWmenNyglvY6lE1VBl_KOf33JXUU8qkl5S2rQHhJOY9GrekSaI"
			+ "klRzZfkS72YSM6nmOOcvqSC4YawlKsq_QRCiWqeKBXLVsCEiPbEgV6JOGNFPQkVLzWG0";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
		final List<TextBlock> cols = getCols(getDonors(), COLS, FREE_LINES);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings
						.createBlackOnWhite(Arrays.asList("<b>Special thanks to our sponsors and donors !"));
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(UTranslate.dy(header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(UTranslate.dx(x)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(
						new PixelImage(PSystemVersion.getPlantumlImage(), AffineTransformType.TYPE_BILINEAR));
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
		final Transcoder t = TranscoderImpl.utf8(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
		try {
			final String s = t.decode(DONORS).replace('*', '.');
			final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
			while (st.hasMoreTokens()) {
				lines.add(st.nextToken());
			}
		} catch (NoPlantumlCompressionException e) {
			e.printStackTrace();
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
