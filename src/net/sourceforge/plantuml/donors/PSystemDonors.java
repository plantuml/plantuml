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
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6peC02mFU3XMJbc44wzsvvsjcZxOY0eHBCyJYiF08fxk1iGVuDxfSR-H_YAwqhrlcX5jsPhYF6DGBGXw"
			+ "F3NN3DlSsmFFQdxwAlbcQdI5gJ0auIuaB6JOJp-fCgG4ptAz-MSKutlkfA5Pjis5a1j8l_2-CpRFJkEB"
			+ "LuycQ2FmOs1V_TyrhQoxLrLXI9TLZQbHfoiSmeZ84VuVTPkwHMsSJ6qQFQ1JELTmKvsAj0LlUgALD1Da"
			+ "NEiu7ysQLCL4caZnr4OA7ZcPeRqcgy-JAttG-QL-W3QjtJN4SmOKnqhfqVsrqVxp1DklaFYGS8POi5bW"
			+ "3fKSrYSeVcswqN6A8A5r5jdOwYktF371u8SM-kOFPNB5UqXabY55n-KA57WynW39dDIk2MU7RWPKrOCj"
			+ "-ZK8rULnu-uQX-MfP3OQRW4B5mjg1se8yZ_e4pwjx_p8v5o78fQGfqdSbcr0_LdoFkvDHT_CgqaWV6qV"
			+ "cXdYAbEr7jBOyZFMgqQ7t3LarUXVnXg57C6ytysbOFxH0laHwA4-4KdyBFkDXGI4QqlzqBlEJITN5k6K"
			+ "3YaLw_HRR7aj-K9NOB5nqQmEbkYxyUjoKoBOGbScbw47MbMvhVE9aj9G_x_8ew7hl-2QAhLxXsWzq-Oy"
			+ "-x_hbcQ6uW0Txs8MT2qW44RJgg_OG0I7XIuFkGcG0wXk9hVYPdaHU-G716yk6NI5ZPGEYEUNa9iSGfY6"
			+ "BjrSeWAOFQNKHSTjWOSwdO9kHPLyUfcxFgP9oxHjO-O-QOFdxGPZ5CTqGld4_JONk8LXbLeq9qzh7s6M"
			+ "u74-JlEg-npCg-Ysj_2UzY4qhsJ0hp1dHAJITELvfdt4tTGHc4LRy58NszrjSF3GNGjEiMW5CzgqGvQ0"
			+ "oOLarDAlA3ur6FuEGZPXhqr5li3MS9qQmin8WzTBarjgSaJ7QJhBiR4DmAilNh4hA9mYHDlIajcwbZ0r"
			+ "O_KaLCZlb4zWgQmBInisjhiApyGy2l9mtYEhB_LZIN5RGFMnJ92N5a9V8KKHjZV8m5X9ptAROFjJEnRK"
			+ "RJrouPX3yMqJRJTkY64AJ4_XVf4W4gCJIOLUOf8eu7j-_4gzQAw3ZU_iQSKuI5f88g2As7Bg1yPAG_L8"
			+ "JMEzHlzVQ-rksVdpxtLaarE7t_Cp1T5pK3J-ZHJ0KKuWWwU1hoPVbBoLi98LoY33bMkLdWOI5kp2ErTH"
			+ "3vOPrJjHB3Uh5gsYVQYgt-xACN0sDgouoASeLjzYbBMxUb05RT_rncAHW-0_H9L9cLoD9gin4Zg3BCxT"
			+ "sFTAnJmtDe45lMCoWRPVVNQQnat6dAz8AWsF0Fb-Zqn9MWyjhAbfNJuHs2ITwtiBsvH8wt_1Y7v1hPFZ"
			+ "KJsq3t9texj0mxS-f7P19XWkaayKoBTpJZHmyus2kgWMoiJIZXbsZc25_iP_H1GIZjd61u8iSx6iTaSE"
			+ "sKBsyTue-a1HKsWW6ulr-anYePJZWHH9mPhPCfv7sg1jsDSJW5CKpFVtDeHB9ffVa5dxUlP6CfT-xiKP"
			+ "H3gSdqrYqvIj3Jcm7scvREkEtFw-I8UT-k_4ZlETZwXR1n740Z7I25wF2UlcncxL5ZuZpwqQ7CTbEWuc" + "FuoJc9O0";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, getMetadata(),
				null, 1.0, HColorUtils.WHITE);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private UDrawable getGraphicStrings() throws IOException {
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
