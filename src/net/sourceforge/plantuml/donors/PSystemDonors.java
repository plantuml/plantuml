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

	public static final String DONORS = "6r8F0AoFktsyGOBPua5wjfPR-fZcX3WHBCyJYiF0HZpT1SGVuDxfSR-H_YAwqhrlcZ5glPofDGbvnfSS"
			+ "iFHQuIQCt_d2eyvUFWIVD0A61iW0SLF_nZdioMcw8rM6bE3DT-Z-bidQawtZOb7pEH6u2g0IqPnYkvU9"
			+ "URr2AgNu_9MFq4yWcuBESwPH96fUuEQexQVO-vhxdpexbp1BToim0gEY4QhylkPOUxarxw9QL8c2dQ0A"
			+ "TepbL5gr7AgeZ2tBoCS-CD6QGpwgrAoACHUHOXYTD0mY4QZBnUXZvCMOL7wfD-73TLL8Hjq1dMqDT7tJ"
			+ "sZRFnFJpHH-XqWRVOAJ025YEn4sqV1v6KD5oG52pYsniKUN8apXGf48B_kKFXQcCafnT0JKLdGP067m7"
			+ "AeXafperd6qw3AYmn1Pj5guwrzzO6k_BybIc2mrF83Ow9bg1LOBy3xh1EsiTZqIrXq4HwIdH6BOSvNCe"
			+ "M-c75NrkrX910g1Jit1_-IYdahgEurdxTwu5uB2qVDV37CET4sZA4gh8-6X1nXNehBKWYzwx72oaGDZ7"
			+ "uPinTAuyj2p2DfvI5clxPMnxBVE7hhJ1-iGy1HR-FVPBhWKSQj9LaXR05rg5TEjyef2M-VVIuzj_tnEk"
			+ "AUIurotw25T8-kt7sCnO2D2-PKX3Dq4W2TobLMkn1H7g4TetG6iX6OHpf5ts4R-aZ_m4m5kM4CDX4qez"
			+ "-FDBKQQENAW93rsLnWfWQR6YRIfwZs1GgYdeObHnPRJnuolJEljODfOiYpvvNcem9dvDfoWsbYjKP8PX"
			+ "eHxHdFcsjfLs2MwNCNZLVM9b5T1QuJtanhrVP22VUyX49EyCnti-SiioJKZCufZhPKhF-cHoyz2D2yxi"
			+ "fYSpofH3bW6TtSberaUiiLbCVpF8PkIridWCO7mDHLlmCACOl7uqEQQQOofQcooYOeiGITRc5RkOS1P1"
			+ "dfOZoL5HGkKMgriX8RzjOn6HAd6Zr65pkZFm3EzHb9VppbdrcB_TIie2AbERm2KjIAkbOj-pBf2XUoSz"
			+ "Kos9C_vzOi6sz-M1mQ7qYecM7jm0c4AIBZ8F128HEWgbz58cXWM-xhUxrUDp7TBnjq-d68T83qa2L073"
			+ "sLvGZTJPHeIDzHh_VzEd9Ixy-TUwRPDJlRtttONGE4WMW0WlDAIS4q8y1JutDam6dCAIB3f9k7BMqek1"
			+ "o8ADZC4kwcoPqNRZ9ykDo4rlBn-gyAUhN5JOwGTqNBXIx7qkgJ9zCOEAsRxnZSL53W7_4bL6PNBlAgDP"
			+ "OIJYA6HnxCVfRKFJ5pf8mHgeJU3OtUyj5XgQQxyYgJGSW7oo8QdKhD2YQarBOoEmKaxixwquAMVMPsUY"
			+ "7zCwSrxLGzqaPzTJZ2Xy-oaqzmWJqqNSX8LmJyv934Rune9yKIsMnDAo3ZjDi8B_-tuCRoHiieCF5Ee9"
			+ "fJpJWXnXa-nzgoZ-8DvTn40M5ijq9x5Ool58YgHmhcoPd25zfKyufMyGWw9m_7ylGRZCHFCLaYwzgvFC"
			+ "vRTMamoYEDJF9Ab5obQB3EoRIovRl2AtRMtIOTl-1txYVFaZwkAR220GpONWDQUX3pVMgwbX2_ARETp7"
			+ "PRPk9Z-FIz7EKGbirYSRDS1cgE991647e49u3VrL_GbRpcNus_DKAepKyDnH3JruhWQMaQqql4WQrBp8"
			+ "OjhwVifDdXn8d00TKAawZPqUneneXKCjlscp98DAuauL_ti6qjqbLTwgc3JAaAXwWysIs3hlpD3fdDmg"
			+ "N5xFyVJ632dr2-eBWZs6kbpF3NW20meXhW05oLNpljMACsHKbbNZmoeKHWws5JKqsWmMsHtZqr0shvYj"
			+ "yUpO5i08L0pzWmjxZuP6JgXesUQ4CXiH1ObIQ0ieiySejc6VaYvkEJMTj_PENUY7pPDDDVeMbUB0EZoZ" + "yWe1";
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
