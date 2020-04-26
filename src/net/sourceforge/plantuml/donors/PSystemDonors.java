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

	public static final String DONORS = "6nmB0AmFkBcp0hHmgCOwx9qzUsNuYPYXIV3upfH_WeHti04o13LNIRGeSf_iNWC41T0rTFIOstTwt6-a"
			+ "luYkTE-RPaHR_fhZvnb3CH3qUFww6hRvtnuyvfxff-Ixcz4JPiBaVmA4fYcNj0L8bI6ba87tDyDvlcaC"
			+ "lVGCM1vLFqNtjV-xT9LrzwfEcBReReNpfn6gCqUZc6Ew_wF0XIZO2QfeKCWndSgv3eUg3dAs4trL54X2"
			+ "nJk_dTzsJLR5P2wH4gkDRSICpe3kdFH-IGvajFwZVe0F7TCr9RWd59TU8kZyizhXPG73hv7uDHGEiU38"
			+ "m1nAEoesA6Lk91J22L3eNCLiRFt1aITUM5H12_lmZqPou9wIh65eKOXu00Knp-02IM_KhKdEJXq6LEUP"
			+ "BOOrITUjyH3fcuU95AsBXll18iA2Ma0DGzw4RlXqd_MIeNk_A1huos-JNBlbG1so9K3RhEXRU3H9LF-d"
			+ "csUUkCkKpLEQZdhj_J5sXzmrPURqFteVXLp0SsuMKcJnyWBvCUYvcgWbFvVpO24DuJfANqZtkUtYCOjm"
			+ "oOOCDUlopIoZHb_iIdGsBSPsif0Vy-TM4n9iuQkKHw0djUfeqVb28JKrhX_oUcg_3d9DdGNUCLeV5NEL"
			+ "_r-4LPYXE46douoYhb-e6BDvDLGiKGHdZ2uFkGaa5-1SJMx7QlKXVkqJH6yb8n3Bng07ulMlbIwNGbWT"
			+ "wxezpGfW-R1IDuMeBGAQj1jw4bVoaR88lHqcZMKpnj9pqWOVfut6A4xejCOcShCGfCkmJbNDT8dsEQrX"
			+ "U3M_b_dql1_9UHNNwIRlUH_IwoSPg3Cjw9XK6l5ObhANHBe6gipYB7NL4DyLjlFmq7t1UHQBDZ1pjhaE"
			+ "MHOQ5vBKr3yn-Q59npj9sQnzjHLx1_4lWtc8P4OHND-UF5AJonJ2bZET09j0LAh59_PRqhWpg8VbP7Eu"
			+ "3c7beTMBa2I_Ipv1hAfOE5rO6-v-UOOW2lR9UI_CsXgNBYNN0chE7eAyj51LJocEj3b91kQQzAot3hRV"
			+ "rRd0jtwhZcr0yPqGdTRSO7CAZ3HHVb6WHDAGbV9BJn8J-5y_Jxb7vZha-2ul6na7oIb93Ac2TCSlN3sD"
			+ "rSlG2jPR_FsprZOhvy__lIOhEVN4irvD41qWed-J0hYSqnZ1sy1VjuyaRqxCi57GsEJAWrel0ea8zk6T"
			+ "NTIJoupy8aQMMvIBLd5zgBgluXwvM0snOUiYd-BeFiMLrZODKILZt_T5kCZ1EFyjginAuQgMriA8JR7T"
			+ "ipYviLilnTFMRY23whX3uEoft-wckKgVwmjBJSFJH7uwa4EskaiBQthQD3O3syJOtrlR3acw_oOT_v9U"
			+ "q_sk7ePsTlucJuF6RtSO_OQHO41axPXWjymvYS2JRoJaZNauoUrYwPEBxYcxKB32VjClayM4LrQqoaIq"
			+ "vkRMS-vj8Svk6tFO5hPFLwFu5qKr4O5CBZRhdiH2AU21L9A2EMVBL86qdIPDymc9Inq0";

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
