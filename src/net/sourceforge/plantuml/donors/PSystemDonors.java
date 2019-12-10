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

	public static final String DONORS = "6ym902mFR3fSuLzO9ciEU-UTVLaMenKnfVRDznXLdPaQKLLXAao0mEOTctOFBc-_C0Zx6pfwpEqxFUut"
			+ "qbz4LxhtJJEYBLjJd6SKnOAWnzlacc6RUtjIPKW9Ogde_il4xUdMSI4sJgWGoY75FJja_dVfXBnNLM6M"
			+ "xbPr_dI5saaRsR46R3M7opy5BaNbN0u56eGQqwaxr-5GHQEyUk1h2qqoA5pv2dnBAcR5n0wHugiRH-2J"
			+ "pWnghTCwnoVIQlzI5z1Hvt9DYPI4yZnA95gtbVQE0tHVm6GIYKSTiC4ve9B3VeAftPw88N6hGOFAd8p6"
			+ "fcgsFB70H02Qs8u_bA9cyvJCd8PE91vBe8JFht42i5UggOZPaSs0afxHegT9GfVNA-7cUB4qHUgWuJOm"
			+ "uE6aJT1AGVu7Ek7NxgikcInZLzHCUGhprMmUYeSKNQnFPlJjh8M9yd-p8JFLyuhQvYcTnxhQ-9_DeTWq"
			+ "PTNmcz76FEVWFhqjfD1neG3RIw3dUaV5_9cdZOLq2CzZwOLic8jNbb44pXgdYiLVnPRMoBzrXHfR5ylD"
			+ "ZGLzpD-GMyb09rOX5aCVqqfcPUSJ9KrJu_EGHqF7Pn9hacRr3kbRYdCj__UsiAAPPXZeUXTpcgwDA1L6"
			+ "GTMTR9e8J0kz6WeBo1selKnkrPVLG_phV43chOaa8hSvWNwcUaAzoyKgp1rhkjJ4363nb6Wd4gyRYI2h"
			+ "BkXrG9b5fYFqsqrwfYsEXQTERtZSj1c3-g9ELqx2Lmk1TpTCohoP76pVuPo2n_7bUVLz7-5Z5S1Qt7lR"
			+ "HMe-Wu5sImj6H5gEACTHTaburq0PMOmbBnhYpnxMk7ZePl0-aqNxc7ctEGvP0YhFceTMkiMuTKs6lYCc"
			+ "cx1NiMXh8FYIc0Mg8-DzmEM2v84gBY5ncy4AE7W1mgyFNh4H5kxMGTKj8oT7rodCrdrr6cW9VjpMWQMo"
			+ "3pnLk7Nth3Y3D4gTLynxOSsRnZwa-5X0kHmKpAI9ie-Kefgsbf5HOmnwzhg3vtQgsmAxZMyrZ1KARrDq"
			+ "qs83jYbgkehjHXFpH5HHgH6NFXP6mCyy_2OyDDOXnj_qiXbnfhaXJ4c54vvVd2qDsnujibwZrstRzhPo"
			+ "_lo_rsG7ftfSvrK5q1M4_DzCcTo81TSrFON-tkMBFfWpMIsgrJY9bUUMdWMO32AtRFQcVPgHtQapokrg"
			+ "WpxhyQ6IlwaS9wZh4vPVp59KXoznDjlU6auLvhxvHZZ9eV3-fwYoAJ-uAYKrCO9DxEGepkDuhgCKTrrS"
			+ "GGJq3HS5ndxpisBDbum-NpJdC1mPyjCHTZdStRMW2casfmKMwQplhTkwP7R_IqRmXL6JTbOFQurZX5IB" + "G57DXFeu3000";

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
