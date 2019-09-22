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

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6uq902mFw6aNBnoRoJQNUtErFonYo206_-9C-IFeunriW0n1BL5IhOvS7xq622gWQ-ZeCRVlzBZVI7yH"
			+ "NUdUDys8jcnDSPvH50k27c-JQuPjxkqnSsBnGt1dZe133yW3iacsZHfPkfVj92sddof3IY7pywS6s9OA"
			+ "ihYRQXukK7S6TknnUzX_ePwbHp_sKxhZEw0nZHBhiAEQb5SFVAsgP8B2sM_7DwrYLapW42cEjd043jcO"
			+ "YNnrMiUu85NxZte3BJPcM2foJoYssYMf-UVP-hQ1xXCOJ2AmYG4RC7Eew32UO2vGRyGGM4MYYUpiZCQ6"
			+ "QsvveeC95zInXd-KHKsFLEOSZau4Dg-40Ww_XXE0hIYlmcVxDWCAHNgBkYN9EZMUX9N6XoKfhXyqt006"
			+ "R2ne7QY2a3T19_nfCVjJatjT3GLUzEpClTaPo4Ms0y3nPaRVPDsPAF_t3yuqiMsfbaXrBlrvhlTn3iZT"
			+ "Kgj6_37QM6LBtDQJ5nLQUN81_4FGIxsZOdxETMCX6UAvT7iYpdFgojSe0cR5OMJXJx4bDUxIBazZVLcU"
			+ "eIp1Z_RhL2EPq89N8bu6_QfMA0zqlb2oBArSFlHeMBqCV4t8JBmjkcV7NCh_lrAymupI5RJVnPogQo1A"
			+ "dh5pLUjigWXC8texv2QGAr3jhbknU_-G_vmFIBqbaO78RCvnlrIig3LT334RrdIwn0nWyfHe9mAxaqGY"
			+ "faReNR2oZ4mhnQw7g-VqYJ5nxVJ2uxwfCOWFTBBo9EpfHR1cXY79chaDxGUSWt06NvnNN_VXFLO1hBjv"
			+ "Zxr9pHzd85tfXP4aRKQKszRzgR1c1QLdKPPyM96_U_4p5mzT2bvdikW8Cwysno5ZeNZPf9N-PFJ3aY5l"
			+ "dygo_aeLUGFkbug5G9XdV0nuF3XPKCKb15RJMGuJ6u1mweDNx3SlSJEekgNav18snAp3LkU1IkU7UGT6"
			+ "bN7WcOCTtRt4pz0IHSx7vxso-cbbhIfsBA1uLWYMKaVPQweeqkTKCcICGSzXQ-3jDmjT42xrsXkc1Gfl"
			+ "W-Y-V0EjKyYq4kC7JPybqeLyHRxaiJ02_iwltsGlEDP1nhyPtOV7cUUA8AKgEi0vSRRKQbjQ1-ij-lpv"
			+ "ifoy1t_-xpLjvrI8vtoM0Bg0WlQlfS2SQDRLF7liJwEF3kmsiW9Y7PpqbLUcNWWm40oQPQcNrcb6uSuO"
			+ "Ncwg1dt6yQ52No4L9uZZ3SikbYcA6bFEykLanQYys9VU22SvU5x_55M6KbnbkPeOeLNi_2ZEqtWVjHjk"
			+ "TUa24j0T5mJAVlExtIntZJvVW7qO7b3avoDvwB25QK4Lfx4yDJ15OtrlnjjJPwP-4m00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

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
		final List<TextBlock> cols = getCols(getDonors(), COLS, FREE_LINES);
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
