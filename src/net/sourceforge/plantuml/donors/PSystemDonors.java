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

	public static final String DONORS = "6vOD0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSaPHV13q"
			+ "W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6jIptfDXjcof3Il3lEr_v9UWTth51eZbAgyMHwq-_8GFlEsFp"
			+ "x3GsNZsQe2y2FmRsrR-VEbkNgqAOfRiL685HqGYNCPbxlHVLei9NiFDHmqu_fbCfgiEXMfNOR7jJH2jf"
			+ "xfWLwjlvgDNaeXWfHKgW5T8869q9ZJcPVP9iqA3nI_qGtcoki9Hn9X176o9qVRstSz-9vHT9V33oXbXW"
			+ "hC0SI3ao5b7qt53Gy0fWg9z5DRQqSi-JL53aG0jvyuz6Kl58ENCwN3Hy7W11m3EeGD87DMh2sSBTW4eY"
			+ "sQBJ4bUT7JUMbkkmFAMFfE7fnCB7WbP03K6IWoxmz5ZZewcpRGP7Ka-Jl4vRXVepunsqpGLzOskJXFMr"
			+ "lMdcZ2j1MIBLjOtFqLxp2C3TKYkl-3BcYEO0pNxpgYAh7osG7u4UgWbRycVPxonaGrZRi0_KTQuvFAe8"
			+ "9sJ8DB5Etissr_GNkhZ91YMi08lydl9rjWaEjj3LaaU09xHAQDRvH24jhTulUdnPkq-ufaJYtgZeVOom"
			+ "bFxxLHzcXk807Imp2xhfWcLmDQqwYYs8O8_GYG4tXMGZd5VNvRZwk4WV-Gc0DvKHaCE6eGdY-MKhNKSX"
			+ "L4Ucharp0R3rjD8M3Ryb32gq2xeLLIAXibhjSRewqvUDBVT97pnlDnYZEAQpDHQMkrT5UXYMg4Qplbix"
			+ "H7CAhl9nabUzPpvN0Qujl4TzSCy_Cg1dUcKWKdp8Oz-7VsJfLc4nItYkcrNfhke71m_TSk1k5irXHfay"
			+ "P1eCZYQYfNubyjEIuMyUifg_rXJx15lJTMQ8C2C9RYzDRgZ9CGslwIp7nIv2PAh5AqxnmWK5UrdX95lO"
			+ "52hFM3qE2VcTy4b4geo5qeYrvQx2Cx97GHxEUsJLiherbUG2gincWeYqO5LB5KVwMY03rofwPbidjZyj"
			+ "i60kxJ4cLXoAhvXektA3WYamCuJoXK4aAWEDKdhPaqG4_kVFNUh9jGxh-CuOFXa7oHwI2AX2cSuyKStK"
			+ "quvXmrYlyVVhfTny17_-jKURd9hidfyXYDP1L016U06NSdymWgU1lyw6MO13hA2YZOGhromz2IGis89t"
			+ "RgBERpEQDp5PRbmkUaL-g9BVvdQuc0wnGST59CNHOacLNcvMWugtVSkx3ZSv3FnTL1aMoMMldib8o2XK"
			+ "SMwFtTemD6EL0LPG7RK9mhlhjzCihnfplWAfCbm1yjs6d4hr22sigMd5M0Yi4UFsthRzbDFZFndenskj"
			+ "7tlrq7YGg-lVJJHyEuBfvumaXelO7H76BJKd4dZu9eYyg1PAnzAE6NQUO8Ny1b_62mb7xE03crfUfQno"
			+ "GuwnXUoTrIW_8KGD8O5SMApKLSH2ASS3APA2EQjbD4FAGLzmTay0Jb5mlB-t8PoUQ7v1i_RsZ48obsFk"
			+ "9HL4CfoVHMAJagq1ER0lPIxRkYAtinRfi1FVZkyut_c8klKY0YKGfIHuRIxqtOtzPcdOb9xhDJYEoxmy"
			+ "cFn2TaKxNndO6YTRaELi1YgU121sKAXGUML-jHznpJb5_htVA8M19ZnFdKOtdkl0Ah8rHgIa0IqtMOHh"
			+ "t--voSc1med41XUMcXizJABez3hLZfCswycW4eLBnMJtCAlPb2eV6NRGN3PDdVO4YoM1LTrvjQ8J";

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
