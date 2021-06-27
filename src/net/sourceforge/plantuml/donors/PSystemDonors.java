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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
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

	public static final String DONORS = "6zeD0AmFjCllA6nYeh4EstErPvQF9MScYM1vdb2SMCJfBY3-0FTEZ_kDz1VHbUvzqunHwxL9EOEeFWXw"
			+ "mBlRGljG-byVX3bBNWHtfWGNa0AYEBf4VgGRsKwVL1bIWcUfwEzlcRYztJekL2Q9lX7AWU9-EMJWRVfi"
			+ "dft6XqOlK0t-zf1sN_tVfRDrULKLfWNThS08Z3gDSAJiDVwFQb575Em4DUpqOpgLgreErQh4PjkR8bf9"
			+ "SyUiqD3EHvqc5yLaf4ZnraOANZ6OeIadiq-HN_f5uu-w2HrQgsk8vGuWpfdIe-kstgx7WrX-iSG7mHLy"
			+ "GHQ1EP1o95v4otFJZOQB1muDkwZ6bfNSyyGAGIgqa3V_A9AItaCykmnk6curGE3rDWf8ng0cJJZRTpUW"
			+ "qXviqQM1gwxr5-RMTLYUoisauGd4mis2Ma0DGUA3Nj0PVUMMoJqFXIqXJqayfwr6_Pbu9srpGT_uKX8m"
			+ "NpjFpSp-Y56s7j8uSf_phyuEUDUGbQlVZ3d2EOEwlCm5CTzQe7u4UcnEM1BVK-uif23KH-gc3vrhJseL"
			+ "uPHSKYhMwKjiUItt3rrfams9vEbWoNotV5rhYWTRw2ga6dX3gpJDEb_0f8Rw-qiUBPtlWcjAnUuDYpOY"
			+ "kuRelvzRcXcA0tHeYjdGJG26iKghpc834UmMEZRW9e4SXF9fSicVbb-Yj_m4m5jA15rXGz8CVlzIyf23"
			+ "2p6GgUjIN00idbBgHSFZ0GkLEWJTZYgXz3Pt_GoJbctBni8oQHVFEmt6o8_fbEB2V9bBU8HXgLeqvpwi"
			+ "7S0kmRFvECshxZFDLO1BXVUOZMl_ae4ymvmHaQBJ5Oz7sXRpK8TX5cFEIrrqJxUJWuTkFN0sY-OneqmU"
			+ "iXmO74v6JRr5w4E3uMyAsHnxhKNr2RQ7w_JWPaOUl9wQt5AJ8neksrcEdIq2aip5AtPHuVo2jImZcQDh"
			+ "2P4ZLaz2XlnEzKbOgiI5fWaxbxi6psHw5EJXl2TMFUh757GkeAgO9WX92sRE49I7hGKoi5RHCzS6iVkb"
			+ "7GlwDZyvSPP3yMKJxJHkoC8Kc1uYlU4W4ZC7iXAzr2GHm7T_tArUPgr37D_TqufnaBIGn40LcTFfFoWp"
			+ "zTOZD8tr5ltVrjhBilFdNsjaarEplVOUWcWT90KWmWjqb0j214-1hpSR9WDEM255Jp9SkMxfUI1XmLRw"
			+ "u2RgT9jHl-F5SXDoqThpGvN-DBKeEEiRKbo4WlBPboXfxQKULDJshMySRd8m_5rKcPJ9lRsCfib8-0Ig"
			+ "E7V7RZCLzTYi0IAO0vK9ZDtzMPd6KyRShr2g3Cy4ylqM6PAC7bfOKzFAk11OAvtRUolR4C_e_u8H_QZM"
			+ "kUTUFJITvEe6YeZ2jod8k22Ik5oa7GYmcsjEFEZc6p_oGRLG1fPspR1p1YlaD_GZl95mp3O-KEGn5ytJ"
			+ "NiXXMyZUDGly2S1x20zaOR5Aho8MIZaUI94Kp5ajarcadRG5f_GEe5CKZF_VEn0kDz7yYimky-gDP2xJ"
			+ "t8yhY7ouFul4fYLR8t9WFoDpsVOLkLrpaGuxzJ_m7UUx7r6tBoB82iIC97Zj9gIx6tjKczZCU6xGu3Wk"
			+ "y-DWyelO5Uq2oS3ML6n1Lh8PAF4a1BL39XLclP9VtHzSSowHV-_TIs6W2Sypjz2Dv_fW5TbQYwUa0JKt"
			+ "MNpNlZzm9uiJ2YSU2rnO9cxqCALRotogNJTjDf169NALYl4UPdRJ4YK_qVMakXmokVM2cYMXTTsPeZCu"
			+ "jWLpwTgvvuKPNEetKtqcfCT0m_Mb1W00";

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
			e.printStackTrace();
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
