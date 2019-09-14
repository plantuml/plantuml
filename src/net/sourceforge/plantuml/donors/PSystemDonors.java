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

	public static final String DONORS = "6ty902mFR3fSuLzO9ciEU-UTVLaMenKn_Poll0GDwsu450sG0Q8aQloSnDhywO_J0H90rz3HOsxVwN6_"
			+ "aVuYkjAzRveHRVcs-MeT28Jb1yTZxQt2wPrlgWmfGKmLss-PqDxVVbneGbeh65VWfd92vl-cq-9uA8HP"
			+ "cgrmFXgXChPaZuwvpzsFeYvKNLo2gc-56jEfn35S5JQ6wpFKDHonwDhLyQWe6HGZh28bKJUE8C4TDngr"
			+ "KJd19r8B_r4BQDAQJKFIAQ1OHefOyxRC-UC0zJTXu4GGKpeS8CAXf2DuWJ54VI618AeODMgZw8pDLCzv"
			+ "ueE955JOWN_af7fl8TkZ31q8OhAXGC9F40HWpzLGuhEvsO3IBRp5l0QRTMfPoMZtDrDIQguqF00wOY9e"
			+ "1Ln1o9kW2tuVRUOIPngxei0RdiHubTi7kSZs0DXU3EZRMHC3vGizEVEKPKPrQo6TKwvp_EfiaEyQChEt"
			+ "cr75E8k9cD_cKXY-k03xcw2NMaF5-9lpp48sXCUKkf2Tv_Bbt8b0M6KfMUGgbgIc_hIBAMrTpFUcBEBT"
			+ "-tLgADimWbTJv80-gfNsXiuNYgk6-kSXZxgEJ-5hqXRYNQBlJiojuk_jRWapJbVGzIPyg6w0A3Lwucgk"
			+ "M5GHk8FesP27WDo1wilaLdbPFYIkvmDIRqiu49aDOYDnpRRYa8j5c0TMTgMA0S0MBj6kt_gTf5zvTK8V"
			+ "WfLHP8IxVhj9zx1vDioDNYj0Gur1UKDdonSPgxaW3ZDCownPFT1-n564otYGl7hUCxkk0bYJUMzRR8sd"
			+ "Cx2kOSumITCH-JZIbcGSwYWzYxFDcxh4gxcTx3nqEpXFRz49Piuswo5J8FUeUMcfMunxrs38crDPnQxb"
			+ "ehq4-kfcFL32Cpu5t6wS3Ada4eAeuKn7I4n09BNEApPOulekgdevEPwQbZ1hjzL1eDhv4Ni3GLMm4suW"
			+ "plfUusT4YMBdxVDUcNLNtuUKM1TG8GO48wbGjX5ILCBcL3AizrRFyUZWjL_QT46_ZM-MB0aKtW4HQhv1"
			+ "qYdC8q_mHirPefHJgv8lUqoCWEzn-GtsnAuES_oEbbOUPv0RWfGxfO3abs15Isst9bXlqVksRNjRDlxy"
			+ "tskod5Cprx7o0Bg0k02rUH4xKHVrF2d_TT0ddUCMCWD6dHpxoXjJhmSO28fcsUvLUp-ZVhcKbzjLEtsK"
			+ "zKEb_qCgGJ2M5oox6AMeZ3ObM6rxgniLYttxZR2NG-FycwYoAJ-uobIhPqHIiSkTEC_ZjOrK8efAESo0"
			+ "jXE2mDblV9P2uoMZZIjWjSC5HJwVPfV3evcMs12Kbf7rm2_T0000";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

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
