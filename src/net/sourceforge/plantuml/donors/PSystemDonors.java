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
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6rWB02mFUBXRGOc9nbfsvvsjZ9-86KqYM9ud58U1HJpT3OW_mBtJutuZ_KLqfNlVD2FQiZN5USOGiI3e"
			+ "yJIvPjZctXu8Sw9y2FxT21uW0qH9r4QrBFUas6sRAaDAyEyxN_abw1tUiK6YkKgZnP5xPFSBxs-6ytpJ"
			+ "67hf6R0ygdwAxcl_T-agw_AgAipItGgCmAZJ2DLvIp77TFz7WJjEmKvGHOybntGgvpeSggY0LoSy1XPf"
			+ "1ZUFyshKP5HCQXCfaTOe8s9q9hJcfFPPiY63n2_r0xpPN66fu4meZb4IT7qzjtVRGFFB95v4mH0B3WlC"
			+ "ePJ3kq3nSoD18YX0eNaMlR6L1qkUk61H2YtimnyQoO57AfdJqI8HyW0A8fx11P9Ug5KJpaui1bGHoHQz"
			+ "bgJgqdY8T5o7vIczDXhk0OiG2yu2QWZoF-Y2JuzL7qsTusfe9FKqoLkrTQXVYVVGDYFwfXvD4jLdVzFC"
			+ "4NU1iaMgQwlFCLxp2CZTKYkl-3BgoEO0pxvxLvFrZnR8Zq2trKGj-RFi2mi9Y6ih-o1TvuQBHo_2CHio"
			+ "JQpZTxFjDVs5hbXbep9MW4L-EhykDI715heoUGHwGgk2PfrFDDBI-lsYnvUD-uHhAYpk7Q3tKJRd-V_T"
			+ "xcLcA0xGqJApeEiHAihLjEefjY22Cq8NXjm4oHKuRwxRSVEwoA_wWE3DPGJYi26qGRm-Gkcw569rhEh6"
			+ "D2E0sTFAkKY4BH5GertGlQYIYPJrRe_JrPc-RCpS9trmtMymHd5C9pGMaRjt8JqCArIZMT-iVKXp2gxo"
			+ "EiwhxZNUhsBRty9xqeVq_2C3VgPt1YBbHposhVoZIBUEYbcifMxMvRkw7nmyTCU2CrYqX1bjjaEcGU5i"
			+ "CixKhqn-MZBy7OUivlU6PgyWUYTiX0Wpci7jhSarDRbC8cWwo_6n2uWYjNZ5hhXm1qBTB2l55ZR5oZDM"
			+ "Zq92ViTyWaZbNJYjM6lk9ZmZUnJKuRmxLYthrw6MRG7LvZ11HbgeQgIQ8tqja85hTNeMMmVRdnPOCBRn"
			+ "g8eLX-8x9jgltA1W2imKuNvH48boGLfABp59JE1__lnAFRclWutlZFB9E4ZsaCH05N9rnechfe7p65R6"
			+ "Ues_Nszjzmh-_7iTRN9gYsT-YY1w3fBZtqbnp8LIrl4qobzdmsB08LPmqKR1bLkMtWmaBDY1TsPHZmyp"
			+ "6ZSnMMvQBdh5zg4gVZkv0HVp8JRikCYaEKOikTBRRZseuhRlU6Dna8FYxwBAk4okSlKQ654WjhuN9prs"
			+ "6sLuoYe5M50Tjmci_Ugtqonl6dFUeLGtN0RopO6SAlK8BQofQSrO26mGOtvlOJ-LzFXF1lh5jTQFlNhe"
			+ "EQYyXtU3Xczxa7x6I31S9EsOeBVqEPc1XsyOv8svEV7fOicLYsuOTe5WXVr6NoOB2I_iQ9LBDDSUNdFw"
			+ "vLtEQZjow1QoZrQZ-OMYYfY1N5YirTM9XLAE1r8a1NFsbl42QGUzmIKz0JX5mV7-xaCulT3yY6RjuncD" + "OgwA2G00";

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
