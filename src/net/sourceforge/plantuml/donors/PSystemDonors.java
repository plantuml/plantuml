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

	public static final String DONORS = "6tiC02mFU3XMJbc44wzsvvsjcZxOY0eHBCyJYiF08fxk1iGVuDxfSR-H_YAwqhrlcX5jsPhYFAEe5WGz"
			+ "dYRN3DlSsmF_raRwAlbcQdI5gJ0Vo1s1R9R9ihHjgWmfmRDSR_uPnVYUEsbebcrp8UG6qY-yxupDizFO"
			+ "UFLeWhu8_1ZOLm_NrDO12aEwh6fCZNfTuTeXe2EyY_fD_Q8sJZRMw06Tohk2c-fKmeiQMAGvF6vhcE2Z"
			+ "pfAgc8mcKWAraKP0donegidgKtHb33g_j0_mehMxXZXxGBcjb87jhursdIVOVh7YGojlo0M71UvGoU5y"
			+ "WkBNPWGThmI1XbbaOyigjponmI8CA_JD7-f99Nf8CCkGekDp02K1Lp21D2SrMSBPQJS3Akt15bqQXEhI"
			+ "lF6qteVjgTniDDm15ZuUj06q4EH_i2TyMj_vaQLz7Of48KzJt9QjnlnP6vsScug-yRKf47sj7PhfyqfH"
			+ "jHxIjUHdh5UD3aLeoAwxVvbhnBE1PRsRIv7xHmiKHw27naIi-5dhZOK4X6j9_J2wphKNHnRXW0wLYdNm"
			+ "ROwzblj2LsIn8L6U3fRCT-itfAu4s4DN9aUW2rgbdDRvHA4qrFylylZMzLzmJQd6lGMS4vqA3R7_ksvf"
			+ "PYW7wD0mgw3h42B2tRJgB8uWWYD2fuR21T81N3UJstcpl8WzyWE2DvSU38UDbcR8vvTGcbnAOJLqwReo"
			+ "5S3iPQCzTFmUuL1fKz3Lg1BBA7VNnpJfJDisPgwJFdXUwp16UKmdr9zutkQ2j30iDOlctDFQkpHBy6m-"
			+ "3lAg-nnjgm3N5jvZFeJdbGpu5UPCH4hES3_Fj8zucZeYPb4NUgcBxU4sE7derWMdM3IF6MtQ8Sk1oV5a"
			+ "jBAlg69gCFqTXMoElTOMnmAhmtLAo30Z3LulJIveoMKwNjFPWOjl88B6uXMxeiB9GhmjDSdIjOHLCrRt"
			+ "Gq5-1sA2IELS64tusEu2F9FfKP27ynxPur4_bfIq1LGLomGnMI4QAwGum_O66RXNqZFN9zZnr8u5PLkF"
			+ "N3ZBeVYEYTOQDsHX2gnCuNuH48cP4qablSIKgC3t_FYLUfcv3ZQ-iwSLuo2ra4H05LdBAH-OLkfjMzSq"
			+ "rcly_xNMxfQTF_zUHmuvDVizFrCGFGTbnh-L4eIK4-Z1qy1Nao-JlELY9okKGSALYvMU1WeBjXATgwX7"
			+ "oupAGnRBJQX5hsmVglItkslXOZxaXXqNiHJdwbjCPUsk7bHHsxU-iLZa8FY7g38YawlHt6gCXCv25kTk"
			+ "x7EbOfcRDO45esCsWRfVVRQxnatcdAz8bOPd0Fb-HrDIhOSMjjIqiZyHMAlEzRs5RKhG-bymqI-gTUuu"
			+ "geUcGUvkO3Le-DQ4jXtJ4mmNIISgo6ToJZI8y8sCkgWMQc9ftGmxJ_12VqC_9We9UvpW0q4zvc8yxOuS"
			+ "ieViGwDH_86ofj106ujLzanYeShnG4LIi6QsZEUe3Vg6Grq0Wg9WVt-lGRZSHFCBicfTpzv8vjBCpII3"
			+ "I8Vziqdich8reWFx6IsNhTwHspKMwR03_vtOSRx-9UhgcmYo0Z6Z4Rns4_RDZVsgBNncF3TOS1wN-N4o"
			+ "-KKSYdQ1jC1scU8qm6QWuaaBgKgJ2hDU0G00";

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
