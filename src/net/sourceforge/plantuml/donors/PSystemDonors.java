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

	public static final String DONORS = "UDfbL4jksp0GtSyfk1RIGoXsujI_17QQ0jcDvR75c29Tad8ghuBUe421hdk3eaKMHOjiUW7bHXsvbKcb"
			+ "10oOud3clPdtcAhato8BL-J9YbHxeoLcS4r9MAQ0iGbRwo1jh-821ZOSKGLpMg3p4hMOcbn3gfV6Ye4X"
			+ "lwVcxsh54MquQXXZL4XxwCjs8nMa8RNPmRmC42eOoAIxj85XYx9qCFUqpKcBdZ5RvjTlwv23KV7Ygh7G"
			+ "CqfgCJEb8nYMBywJEyD4sirMmYH2ZKMyvLAfelUebzpENYuvwnfjnKndHzfw3TCOlT_3fP8xlj2NsOQH"
			+ "7q-aXZDtJAqZ40djeBylfBUL66DkbWZ3fkctENBwA2uX5SR5QrDncpHwZcDyZ6lu5pnjEgpqahSsuvwS"
			+ "uy9M6tFlDf8JARvsQRtSmqM8NXgOo2md9KvHgYfceFOn6ETWIAXV3J5xLv8J_N9XaMUf0jyRS-SfoyN8"
			+ "eiC0QKqiqMXYzusoE4ICp4x6GZb8vKqWB15caNYR4Z7Fv8vlRoDPcKtJcLDZKQFQdeXRycYs3cPC4ErI"
			+ "Z6GpMZXNnZRzdlp-fTHXw44GAX8dosbyb9F5jjlDrFCk8mTNsCfZ9Zk0KoN57779rh5h4cz9hycUY4bw"
			+ "_NgU1cadHiE0EfQ9cUkC9LA86qRTPz6EGgIbPh9ibZ4HdErYvUyGfY495It89A8nA2NheYn8d9kIVEXl"
			+ "vk0ydio1Ir23tiTjOxXJkMODpMKHBCY0sCchfjF8hev09qf1_Qb-ZDBNZ_KFwEKTQrTil79nU0YiPYzS"
			+ "c0wS0PdLob8bfhBWmGtn_rTbXYjKOb8vnHwflzIFpm_rzwQKg3_NJ_NF-kdv0_yU-DCtEBEq5oDK1Jlh"
			+ "8fvr8rTYlPwIQWvrD3Qk8gDNCKduaUooO87TqCj7AIaLlnij5G_pGqBdeVvOVpssX86nvqAUFuVhdim0";

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
