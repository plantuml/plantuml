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

	public static final String DONORS = "6qmB02mFUBXRGOc9nbfsvvsjZ8ybZ2OHBCyJYiF08fxk1iGVuDxfSR-H_YAwqhrlcX5jsPhYF6E8M13q"
			+ "U9fSCsnpRmyyvfxff-Ixcz4JPiBaVmA4fYcNj0L8bI4b-FUTh_mIz0xls23HNANHuiWzilk5zpV3UUuq"
			+ "Fdhf6V08LJz5zxN_Dvr8N1uAOPPcAup1g6Y4sXSuHUTOxlzJgKkZsdrNHNMhAiTqwk8OhWeRqDK9ZmMh"
			+ "cLguQvnNXoQZOhARI8cqHWaHepD3JqvgNo47CaZyKJz0XyqLbehS4uhBA4bqVJrtPDj0ymiIBuLWY0MR"
			+ "1-PGok7TeCIv4ICH5453rofwOnkEB7dYWY4EBUpX7utaY4SgiwQZHIBa4IY8U1e79BrGZoOnExqOK7ra"
			+ "j1Yn95LNnqsuVnuMKhGs6ku1Yn0BpW9g2F8cTCEdnwgF9lTnDTJmLvucUQjMXpfaou2s6T6t-6YIA9-p"
			+ "JJD7N3NA5gdEKPzZlEOHaBkbLixuCkeevnBSv-vT3UiVBP0VWPwhYKhoPyw5XIJ4TPHzaAvp-vL7By8b"
			+ "6p9DhCitiOKQVR4hMTcCB3l8GfxEhwjDo60BNRay33sfLPzcdI-qfANr-qMFfetx9DRKc-4Tf_THDXVv"
			+ "_ztkPMOeJj2P4hEewm65CPjMmyM68ZWjT6t8JK2kG7bptOgRrqN-gW-8ta96878R9RqGxu-Kkov2JCUg"
			+ "xaWn0fW_fPoRHDY62CtQ1JsDAgL8MU_sE5qzovUDkUUa3wvdZSOeZkcanYPyyowuks6zgZVJpThdM5Fm"
			+ "7A_BlEfUK_qgjhFDlHTyIAs_O23EzCw8IDKHZMqh_oZShQD45cl9rRfyL_U3WuVkNt06YpOneyrsoF9G"
			+ "k9oCC_KhmqzB1jufokRztjxa5IJk2XiX71dDuFhHv96QN28Hj9pb-DW445MBL-nA2QSBLREiL8wmAIPF"
			+ "Nxq8cU1toIUmLDw5fnjhpJqepX14GPsOzyAgRVqwrBmje9OS9YX92wL6gYdIro8PcAlJiz4wsFwqu8An"
			+ "ZKTLh06ARvlehCO6ZLDWkeZmYmQUI0go4hta9370V_tyIZtIry77TqNvZ3X8JfV442f8TSQ5KzFuUHbM"
			+ "ndgBlbzlRVSA_lnx7UtWr1lt-XH1z00Y_k-a08vo4YDu6lXxv2F1MmUhHeysYiJAGqfl1X8MxCSxiwYd"
			+ "XXcDEp6Zt99Sz8hjGzN_TdAVM2o6sB3yefIc6B7afBVTkbEbRD__nk8W1yFVHPKvcLpbqXeOqIYsjnUd"
			+ "E7ORPRZBQWLuK1sN2QpzwhTcbZUDkUvOgXkU8VBJ4JbLz70jhAbfyzWCR1TZVczfFvNfyP-Cw9VIcXvx"
			+ "zJ1yK7vFdGADt_PG-WqZmO18tJ5WD_SvcO4xtd382rDpKjF5CYiNtTVj0i4A-ysyH1OJNjdMAfUmrXvU"
			+ "S_7jNPxTTkQmBMIVgKRJ2qKLCGEPN6pHtOc5Ki03gI05S_QMYIBI3dg3Opq3y105n_jx1k5eiNczPftZ" + "6Gq0";

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
