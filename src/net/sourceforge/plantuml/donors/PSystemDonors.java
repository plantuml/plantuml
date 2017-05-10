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

	public static final String DONORS = "UDfbL4jksp0GtSyfk1RIGoXsujI_17QQ0jcDvR75c29Tad8ghuBUe421hdk3eaKMHOjiUW7bHXsvbKcb"
			+ "10oOudpUctapgKh-7R9mHPwiIBKtMc86rvI4OmeOct2r3jBM5HSmiE69AfZJ0fsNgCNKv1fIlJHM30pv"
			+ "FJL_LoksQCrHmnYZHDf3NxONgI0DeSqEvcM0KC509jsZ3GnRbAM3kQTjJbhqZDaoltwRXnoCYXUknaBF"
			+ "AAd5p9IEO5Y-U4_k31DfBri9amWr5l6LKwMAtgDUSZbxkUIeQxGLCvqTQUirJ6FqVW-NIkxOeI-p3IC_"
			+ "laWDP-uOMaSW4jf1Vrz8Roinnjmi4OPDqMzpvF2HNK8gj8lFfk8oQVHin_WOr_0lUDfqMEabNst6DJd7"
			+ "XQqsvjvj90TIx7PflTp3HR1U6fZ8B2SbJb6gAcQWzZ4Ovs18g5yDCNjNaXFzSc6HUwa2tnjpvodBnSYY"
			+ "mm1fJIpHg6FtZRBOH8pCJiP2EKZbJI0i4MQHU9iKCS_aZhstaQpCfgdCgR6eKMrFn2rvD5i7CoQ8TYb6"
			+ "icajd2jZctfFVlzIwj3q80WLYPFbD3xAoM9RxQRhUPUHWojivJ6J7S2facAEkEJHiMiIRqcloPw8INhz"
			+ "Ufu6QIT6mu0w8nCprnbBf10tZBhFeXq5IqjDPtbOnaHmjekLlqEOXY1Kj22JYCQWbAoBiY1nRahoeRwP"
			+ "W_EuCmSkGGrw7vUDuKxbcZKqbqKOGGR4axngAeqyrH7eH2ceFzMFKVZwiVu1lRmpsXKFNhauB0AhUHOk"
			+ "J0VEWCngPQaIKrbmuuRu_rQPuGgLc5HEyOpKN-h7vuVwUvDAr9_hf_fd_VJyWNyF_EaRd5dQYn6gWYVh"
			+ "8kvr8rTYlPwIQXvrr3RE8gEjc2Iy9BiiM60tz78f9QNYlT5IyJ0_17GUwe_rrsD961Zy0II4d1O0";

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
