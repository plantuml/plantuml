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

	public static final String DONORS = "6vaB02mFUBXRGOc9nbfsvvsjZ9-86KqYM9ud58U1HJpT3OW_mBtJutuZ_KLqfNlVD2FQiZN5USOGiI3e"
			+ "yJIvPjZctXu8Sw9y2FxT21uW0qH9r4QrBFUas6sRAaDAyEyxN_abw1tUiK6YkKgZnP5xPFSBxs-6ytpJ"
			+ "-EYbPy0IgdwAxcl_Tqqhw_AcAWpfifenC-hqX2hcrgWHaIB-_rJhRgSsJgOw3IG1dSgvWflgLC2h4nu3"
			+ "YpI3MyPvDMaoAYQ-YPHWQvGGD3epjEMazZco8OF4B_K3jDbSOQd8cL0SeoJHzVNSlcq3pI-NU15y8HPi"
			+ "7fX3AOThWHBdHeo4JmGDBRFeZQqwikI97OPIQ64V_j18ZdYaCdCwMYJy7e62eATnGD8BjMd2sT75W2eZ"
			+ "sQBFabHT6G_XpkkmFCLge-5EiF2pu2oWXY3_Woxuz5ZrqUGSLqCjgQT9tggjH_rAl9Us6T4t_MYIA9_j"
			+ "JREYk0kKBLAThJx7UCqZ87TBhRpYowWZcoEuzzsx6jO_MY0_0jrM4vNapx8lB2GXhgtiWtIT6wyyUX6E"
			+ "sP1fOXs_YosliHUwmfMDoR82BFHf-xhIX0osq9NBmz0JMfLfrlb4IrfQlr_qwB9ndt1DfTczW_G-okOY"
			+ "_x_RlSmCnG4wLc8MT9sWQ5nDgzvY0n6S5hemv2OWLw2ykMx5pUiYl-e3WhUG4OZ2XgGTulMHSjT5YEbO"
			+ "rKqfHW3pfvHpaGXO4A5Mkw1RKIKJAUlT7gUhP_goCNUVzC5rky4OnJ6Tr5Y4xzu5zp2iL8rcjbaxXJa5"
			+ "B-Drd5VTQwHVnRAzXVUK3wdvHmRYJE-CY9GVqTYsye_2tJeAivXBRjRbkxeV73nqvu8pMBIC6MssGvQ0"
			+ "mjbadAbV6NvQClmTWMp2xurDNa7eJbWFOMOqWjjRasjgSec4ptIMusONW6ArUCMkY719eMgMcSeMDiNa"
			+ "6Qj78KD-ntm2I-LQUDgmrjoLUKRrAAZ3UNUiMjQlGytR0ghBOOACj51gf9eYVIsGWMjrUfPQ1zkV5bZG"
			+ "j_6eYbM7ujicsgxSe68AJ1SHVj4W4cb1hKGlEKaCy3z_VgKUxUlWupkZV38EaZqaCL05hKu_SMeQF8zX"
			+ "PQpNwFlrrdQluC-_rz4cf_PuvgS8ekSW6lys2k0e9p62fu6_BXybxoHODJgrKPGhroYz4KXOi1FlpA8U"
			+ "6MRKxyHakHcvw1NRXwhyxUGAN2o6sB3Y8fEc6B7afBVTUb15RD_vnk8WXuQ_YoeJChdAPJKmGY6sGYzE"
			+ "Ukmsod2NLGlme3ek4vZxrM-dMTuqvhnZgMvu3CWtH-HKqIEqiAQcjMCXi56C-Rsb_LIcntyoebz4QtRi"
			+ "rKFlGVaziWiqV5k3z3D6Wk4YPST0kxdFoGZSVCCXRqpDYKmNYwrSTCMmymOhx3Vs4bb8U65Rgrp2N7ju"
			+ "pUcNTtbMsv6ZRI7xM3MQNYYgY178XSKwNPrOAELu84LIC6TlcIWYxQ0tkAHd04vHSBo_sn2E5PeVqQnz"
			+ "FADHp6MTUt4FOYgSt4zxKhQRdfK7";

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
