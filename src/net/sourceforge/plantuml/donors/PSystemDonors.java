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
import net.sourceforge.plantuml.FileFormatOption;
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

	public static final String DONORS = "UDfbL4jksp0GtSyfk1RIGoXsxDI_17QQ0jcDfR75c29TKd8ghOAUeK3NlKABB8eMsVG2oeqwSYkJIWaO"
			+ "CCNXp7lptZ1MqRz55gwf82jYNHWjCO4RYlmoESoCtre7SNSLrp2s_eWgMD4QNI5HYuN9DCGwDLQC3HKr"
			+ "jNyt6uxemb7338D2ke2Xx3PIGUnJcmEiIW-XWh6C-aiRc7GeImUhWlOPQJ4mPi_yXbqTSZ3DNrqr5WP6"
			+ "IIsMfdG4a_BLUNHlc4YtsKkO1wWn3xTSAbRq4NNAxHnaahDkqLRCT7cYhRUm2D4NDLmfUU0BGvdi6Fdf"
			+ "H6guSAVKEW0HqG66TIuBMuaPPYP5cBHDlykGqmTn4Ial5wzTnMtIM70SusDSax_WQTTZfLFUseHxCiAr"
			+ "hNRcmUqa9r9yxSeMiePB7vqQcCia8oNEKQegPA3g48npC27KRoQOV2Z9YM6vjiXQAi_trjmNb6HYQj6X"
			+ "XxGbjcYWs4FhB0uHKtEGeL6ELddd2Kk4PM3UjaJ8Cxhds_l0LcRNTkRSMDIezcVYZWeqUmTB9eWs5LFP"
			+ "IWiZPMpRx_dlNqeTHVU6K84vsKwpavqiTjrkLS_wZ1nSOsUFfUm1ZfKKAyIKHyTkIRmdlILx9kRnpTjL"
			+ "xAqT6GrZwerCo5mdR95St3Bg8PjswYCjjShoiCo8uE8GEl-0iF19AEz0HW6DSIdP5sLEOcHAAdn_4mUZ"
			+ "K9aZ5-y6NOTjOxWBkMKFhMJkHv01yIHlsauZJtK0Eb8Aci_DKv2-UMf-m23hZNR5WrUkZm-1rJmBbwO7"
			+ "Pqncix5KYONCMRW9_l-gB763IimhftX6cg_DqyjZyxqj9PelpNFpitb--SI_H_xq3IuirMAAAkV9kWor"
			+ "RkrAxDTpKksX3cJZAZ9u5QE87ya-2pROBRr-bA9ImH_rNe_d";

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
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
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
		final Transcoder t = new TranscoderImpl();
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, "\n");
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
