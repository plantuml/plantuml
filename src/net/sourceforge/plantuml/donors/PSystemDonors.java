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

	public static final String DONORS = "6tOB0AmEUFDMA37HMBjrivhoM7AcIGJByuJYCB28fxi1yGUuTthSRwI_YAvqxvjcH5ksfZZF628M13sU"
			+ "9jSCszpR0s7EZ1V1dJc2dWyV88ghDl8ZTUGuzq0i5Tr_cRWztJekLC6nQeHoeBYruNrdR9wTnXUl7atG"
			+ "Hk37mBxwVrE7hCj3LPYbknNFOp4g6g4wB_GKSwnp_qSL5w9Wtw7gOSindRgu3eSgAk3L2O-1HPh1RUEy"
			+ "MZGP5PFOH4geDaeG4eCPEd9I-mBvGGQ9N-e5j6cjhY6HFQ2upXI9hjjo1xODDBzIugNW3xvWyy0SIZdS"
			+ "9oXnRiIGyFEWeMqLlR4rHvOySS1G3YtipH-Av4Zl8PbLXXQ9W1ee27aO1oIZK8Scd1rR3AYw1riCQ92g"
			+ "g_qct7qSbgTeRZJS1HO15fm5r13aVz0JFgEll6IopwUYbf2dIRogRKJzgdW_RJEYR_RH950-xA5PiLy5"
			+ "ojP3EbjUn_oheqFUDMJbqMz67TbSWxlivj8mlxH0_GXqsvoe9Bw-A0iBIGZNbdeXNUVcAxKNuOGDAHNh"
			+ "v5riUOt-eIjIsOHOpWuMyhH_NMrA1bleogKQUaEhEiqwdsX93VNtGnuTEjy5rrIRtJjCxv4s5_d_NRSq"
			+ "CnG7wCGKiw3h0aNdQ5hr5nk8uBJGXI4t0V84ofV9BNzPFiGVzG71MqW4eh1HqWd-_4Xvo8KBCx2gMrAC"
			+ "0EPFAUT98kY42FKw13sCAfb8cRj-XadFicLZRfdqmPKr62C_ffEAa_3TNE0HXjLgqKmtQnzXbU0Ll4xo"
			+ "gdkDzgiKiIsyProarWyPY3FC6H4fFwBOjhHBmaCTXLci9JTryJtT3WuUkdl16InQnOmsso5BGk5iCixK"
			+ "Mum-D1Z-Za8sQVVcfbv1-Awm4TpCQ0QtboOtrEGI2R_bBCV35e0IS_6ANL7XKaBLB2kLhMi9aoDMZqE6"
			+ "_4xq2IoLU-5fnuxcxiGpnAEW3kVTiUfKVqvftXPGGqmJPAI5CcT8auUj1J9mh4xFIXjWzrCx5enjVh9Y"
			+ "pQ7ujebOumODKs2wYF160v582kebUib9OW3VTVcLUgIlWu_lwcd5E4XQI6AW2bATyODJq_3v34rZlKR_"
			+ "jxMsMtR-_7iTsUJK6VUwYY1w3YBnRwS07UKa8dWQyFwMZv1t2gmQNNfGX2jtARqPI5YmczwPHJqwp6ZS"
			+ "yMAvERdevTk7glxkv3PSB0RViE8YaoQVVOcJrdRre8hQllkDnK4E7NyNLQPaSfLBQs14WzZMNfpqs6sc"
			+ "uYwh5U31xBXCOErNJy_DD5uovhfZgMvu7CWM8ZAa67ryiQQczN4Yi5awytjBsv3CwB_1Y7vHhUcnLm_z"
			+ "1-NtM15e-BQ5Ciw91CD5we61VdEVanbqyms7l30r9pfTBBvoqMt3pXUiiD_4ZyX2mWlRccaYhBLtgpdz"
			+ "QIwUi-t8qRUGVM8scbx8guWPoC9YWvwTM2ZbU215KZ1dRvae8ksWDxYiHm2Tec3yVnc4euNcHsHMcMTl" + "57FP9cC_bFBc_GC0";

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
