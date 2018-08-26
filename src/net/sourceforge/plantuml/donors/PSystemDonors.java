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

	public static final String DONORS = "6-e702mEU9F9PHN1hXQZLurLW-3nVtlrrwZeRZDYIgOGkdS8eIVXBunYGbPF5d9zqTbiGjQCIJSsPpZF"
			+ "A8e5KEDPF_rXcvjTUFPkr2_5d-Bb2uKfAihW7jk4J2TYN0HFczUtrCbQaWS9fxkw5F0d590ld4unVmxG"
			+ "btJi7RlLOhTFYHKwl0ze0boDFjOibEWOhUzKgS55ft-TKrAJLZ6kIQJOQiSMsEI4XsnDicFoHCRVT-e9"
			+ "rBIMfY4zLq2npoaINIxxMXq6K3v10eSNYrYmmSoXv8FRGAlNHWnhimWQqgkeZGtc37b2m-205lRbYpB9"
			+ "yHQoiihGviNY3XJO_4q0aEcdh2OnMnuCAD0YMbGrMABpuwQxC0_3KlwSQ3WB5XOMb0BLOEG_w0QVtfOy"
			+ "nRdcNb5Bo5iIhxGnrBUKLtWsBVhQSqcWN5P7oNJn_b5rBQJIyXpdlSO6kMl8oDakMevNzX3U_imbONvA"
			+ "0VmmT6nCL18_NxQ1XJG8TqizaKVcnCbTYu3Pa5Asy5DCMSs_eqjCLXFBsy3YFkg_pZTb1Lh8IkGsz8PM"
			+ "88zspaWf3VNtjxoQEh-BM1Cmc_SbwfFPD2J-vswfPbGSeBKXlA7BWg8mIwfACO48N6RwD-GcG7gWUfay"
			+ "YfVbjyIrFo1ugvA1o6r6f7ygNgYx74AOZgLEbcW1J7OfPUF5weKU8ZenkemepqYRkwwJISsoHSV4RL87"
			+ "srM3COYZEasvqNsQ2zpTCA3cpVv1snkiOjpB2UVwlLjfMWKmbtktd2ojPp1Ghp2dXQNFY78Oqaxfxkee"
			+ "aUMnnQ4kiOVRoC935_vnJsDHrutxRzu_7J8VVdwbAQcN63vfqEmjGTPd-qII9n3s56z3E3BkW-CZoO6Q"
			+ "7F5YjfpX6DW2m6uihjtra9XRGrNM8pkjQGbJaw_kGYNuOJw1kU8MaImsbZiXphWjYkmJyqwCUjNxMPAd"
			+ "1HIDMGLFQQ6CEIGfmfO5PCXSbPvEcy3sJHiMv2ggC_6eGU5Z5QrQRF1W2d7_mbreQa3MMQcGIvia1l3h"
			+ "pw_5SzTraF5RBQqY3f3E9I642dUwxPKWarBf7_yiyzSFivDP4jBlTNue0SpBQhfNr955-XS0";

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
