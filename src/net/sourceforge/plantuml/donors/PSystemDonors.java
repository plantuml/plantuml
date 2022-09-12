/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.log.Logme;
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

	public static final String DONORS = "6miF03mSRygAr4-2sU91UhPs3uiVqvmG9uBbUK9nOX6dku3u0zmxFUutqbz4LtRlcsP4hTScvGoY-Y3e"
			+ "0Uzk2-r3wNzz4EOiUH7ScH9SG0g8ukWI-P5kP3jzKcL82TvyX-vlcQmzsJek5ZMV4u6h02gHaD6SOhkN"
			+ "YNczGYgb-FoLZz1Fu6tww3nt6aMZ_XEuUTJsKUnzpVzDdMNdeH1cwMu5Hc1KD49rth77-FyKGOKes3bK"
			+ "mOubntGghLeELNICUL_N0dLCkbPDauygDQjYN5MaM4Od3Oj89jJbOl8nyg9F6h_Kc_3XkYeauzu1dMrr"
			+ "T7tJspRFnFppHH-2ZC4t53Km0jOZQHFDpqCF6aOE66fsuSOM5OuyYOT46hIGN_vGc48loTaLSDCmgG24"
			+ "nJjGWIG7rQdXRCTXG8KbjcYpyAYB_S7Ik2uhfp9TQNWYiJ4nKGkeKkH_q0tTKUjuD7NtC3WYFIKQOSjH"
			+ "FWjifHrMzFbPIWGBWCwOnVehH3cLr7GSpzg_Soq0kwNbLGzZ2dzEe2bBA9JnYGNgLw7Rve8jUkzoi906"
			+ "KH-7R_g8S-MXkWX7P4fPhQENiPrhcJzqsOBrINWABEGzxakn1HniYgigDO0lj0ffgVC992tpxwN7ot_V"
			+ "4wufb6IlNxGHhf3qquznCaCXGFkcCHkwYGABiIfMhSKMHB17wT00hOLa4CwHTjb7_99-vIS0tbn2T9kD"
			+ "G3tuzvUc31svI0C9NPN62c3rQKNTDCmUmQ1KKz7rHCMNtiQFhqnhx6EbiMfPjCdpHeCnyfjqcc9ZkK95"
			+ "XXaMwc7DcU_KbcYNucxfWrUz9tjT1RYKUG-vSUyN6V1p3nce59pkUCz7Rbc62OjPF7EzJEcPdqnk7hhn"
			+ "m5qiciQCClMGPS1WVoAgzP6eRnOZl-sGpSXh9Re7iBuA90SFCoh1woScLrHpJCDeaqLqR14YsToyufHT"
			+ "kRPWCozFagEYHChDLWz3Gdv7-YAYLT96gSFcV9VW6PAZ82_dtR7gCN-xOUe2AbCdWKzQi6PBQD-pBf21"
			+ "jOkUgPR4wVy-iI3lVJampL1y9Oas7jn0CGLcXd0V65WY6SXAnKkR617ulZ_kZXxnEjJZRvzECGwIRf8O"
			+ "g8A4arzGJTHPHuGJwpN-_wPFIrpuywyzRPDJlJtx7e9eCuWBG9WNw8gS5q8y1RusDam1dC8I5vqaNBbi"
			+ "xPL0b8ADzC4kwlPbHld6JvORaPlMNZzKuKzJZ0h7_K2s2r_HtkUIAjCNqwW6-yEx3XSv6FubgWo8vB8t"
			+ "CoiZKIR1nN4VfxKDpLWwG0tcG9q4djtzsMAXeR7sAvGgmmcGhpSabHfbMbWTqzBO2AoK4_jxQqxA6VNP"
			+ "6SJF7klCULKFTPCKrrECA7o7ISXJmiJC8d4YWjtYEIb13z_i8rzK2oMnjAdDpX2iaF--7yCR2GVia0SQ"
			+ "zOHIdcb13jkaUziiy4U2zn0ba8N5Cjr5B9JAU215KZ2NMyOpfA-q6QVs1j0f2iR_zmQKoqmqFwBPfNTr"
			+ "ahd2rdRY82h3_PubjYZPQf61VLVDPUkNvRRDJMROaV-17plV_AYwVX41DKHN9NZDQUJ3ZSrgQjYY_cOE"
			+ "zt6PhPl9p-DIj7DK0vlqPAE6iXaev2I4I0T2XVHL-5Nz2LlEEVBfVQeK1cfu7hiDFNYk1kR8LXfU9Gse"
			+ "NcHnR7s_uIPF3XHEJBR-LQUnqyEO7gqmrAQRDcl8e1AvIyLNZc1sSqcLjoecbJAyy-FUKBCajFQTxUZq"
			+ "JcuXbzTpFBqnWqlzakWpmJa6ch9U7CY41cGHMK4ZYAwI_Q8LPyYeBAl4HwfWc3ZQ4kxGQ3DOk3lMfw5i"
			+ "NZ5RuziaFZ126vR-nWNSUuP4pmKD8zkS06nO52cKX6OetMCGEs57p2vkUISVW_BMtW00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	public PSystemDonors(UmlSource source) {
		super(source);
	}

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
					final XDimension2D dim = tb.calculateDimension(stringBounder);
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
		final List<TextBlock> result = new ArrayList<>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<>();
		final Transcoder t = TranscoderImpl.utf8(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
		try {
			final String s = t.decode(DONORS).replace('*', '.');
			final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
			while (st.hasMoreTokens()) {
				lines.add(st.nextToken());
			}
		} catch (NoPlantumlCompressionException e) {
			Logme.error(e);
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Donors)");
	}

	public static PSystemDonors create(UmlSource source) {
		return new PSystemDonors(source);
	}

}
