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

	public static final String DONORS = "6_eA02mFU3XMJYceJdotTlPGzS6YgH0ylZFbdo3X7Mo0347DLH8jYjmdkzS0GG5q3HqzvlQT7lSRwI-Y"
			+ "AztxfXdHbh_c-7d6K0m4FPw_hePj_hS7Zwa_zqxZk-ioVNvpXqL918Z8CYV51XeoyFvcECytZOzUUWOi"
			+ "eUgdwBxsVvUkikvULOLPkbkrJf-kK3UsOexfygF0XLEm4rJHe91ZEfLp7GvLZVBgWQyjDCYaSEUtystN"
			+ "P5QCVeYKJ6riGKPlZD2TazOloOACsd_K9z2YOxgcH2u9nTPB93h_h7HuMK3tQoI-0iaG2pODpA6K8pf1"
			+ "iNCZXYGfWeREKOp6rX_PyYG4GnvQs8T_r9CZxYcPCGmjafHhe23ZPXo0j4hTcd2skXbGdITR36kIgelZ"
			+ "8Tnt7AQdd3feU0GiKYoe1QeXo9lG2P_ciruokFIBeeQ__3R9jLa2r5ss1DWtC_eM7aqIvNzwromGTvMo"
			+ "UKfZgTVU7sDto5rJPXx-Hs-Zb2suryr22XOlBO0VWPwhYKhoyrVMZOKuX6ifVI5TvxOhrr64IpPag5YN"
			+ "psB9QyuTNN9ZqsAP3XRZ4_ovs8GCjD2LojNG3LfrsQtpXPBKrBX-oAj3-tN0DNLczuxHUY-Ri_p_8gpC"
			+ "Z8e3TBh4AEX6GC4SJQkXOW4Hc1QwEkGcG2v0kPhSYjNgG_pQ9mZUJ7eW2XiXFH2lFvURN8Gm3MkwFSKC"
			+ "OFgGKYT9seG8JTe4FOagnKXPXDuEavPXqyRKTT86ZzC6On37T5nz4ZvR2Dp2i4urcft5skSOGt2BxsK-"
			+ "FUz36LVndMxXFUz9QloG0JlJWXwHAYFgorBsad3JG64MRyb9GtnNsCx5G_S5vvaisi3CsUOuP3PKBYMf"
			+ "gNzY-A59zxS2MTjURKNUWFGIc8GmCxh1wQt98pMvH98qdEd4s04WgnQVs6-AS6TGTSlBpk8wXCaZLYz2"
			+ "Xlmk-GIMAhhmT65xT_VZ6SEZo3wSzyAigN7fKdmieERi9YXA2wLwIb6apGaov5Y3dlNMmFQxEXR8rc_L"
			+ "iEbGUDj4fvKRJ4mXmrMY_sYqI3GguXAz-2GnWB_nyfHwnbW77T_XPODnWAmX2O4gZER8WTFIMDswbh7U"
			+ "ely_QsspSlxytskooAaddl6g0kWECk8_I57dw6BM3RmDyzNOC0VOO0OJtTdXoWDBxmOI5kp3EwkeAzEC"
			+ "v4eCbbkKYrPnVAYwhtGFBcP3R5XnKKnHT9_YfBLD8wWKs_Tz4KvoOCv_YYgpKhWgTMfYn8JOxb6SmjXx"
			+ "ByLTrwn0W-guJE3jgTUtYsckqkTuGQbXk0RafoDooAPvQ45DJPlwH62R6EjxSziXcUQ_IOz-YjREVbOF"
			+ "GplRlybLeE7R7qAxeISOBX8t3F2xnvpSmCqt5FB6r1rbkPYsSz4zmouHhB3Vz5ra8U6LPQqo4SRSt5ek"
			+ "F2t4Czk67Bo5xDFLAFg2g2fk80SMctKEOb4fnoDI98Nc72sR4v6s0m00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
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
		final Transcoder t = new TranscoderImpl(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
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
