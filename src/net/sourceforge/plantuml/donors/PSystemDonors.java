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

	public static final String DONORS = "UDfjL4jksp0GtSyfk1RIGoXsxDI_17QQ0jcDvR75c29TKd8ghOBUe421hdk3eaKMHOjiUW7bHXqvbKcb"
			+ "9M3058V3zsRUcrX5VnTPkAISh8XrRhJ41AueyijaC35-Qnt4xLLSmj3w8ofXGKjqkKGjPYRL4EkLiM9e"
			+ "AA-e-RjUSqGRZXec63oaS-ZBTYCLn3wrsSEYy11A6CgekxI1KOkoSB38QPUI5ZrZTioltxv33iRPYwl6"
			+ "GiyegSNS58vWLBmuZsuD4satEmdJ03LcuPQVYXMzHxtYSlPIoLatQ4jcEZ_IrXkOXUZp2YwKtFE5laos"
			+ "ZFnu8ZMSkMDg7O28Q0_zAfEvBSK4KxD263L5lqcHqmTn4IdVBbxRaikaCUSuniUuHi_WQTjXfLUyjGdN"
			+ "P81BMsxDdTjAJgJuseNDPGNdFdfbO2gJb9GuHQdAa86gGZ3EmOXGlnfXyguW9_h5qYBtKdc-r-OsfoGL"
			+ "OuiEFQGDiKI3Zjqrok8GCJPx6Ghb89Ktdh14c0VYRPu8UKQtV7iNoCficygS6eiQrUv4t52EPkTWpWJH"
			+ "hiHODgs5WJAsgVVqzo-b3attWb21E5bEawESBBRLRhgUTXavkCHM7dFP0HmhAHQ8AxQEtP1uItf3zaHC"
			+ "uwlNYzXBEp8QXjInJCXS9ooHJ5mpwZwBTkmZBJMTPRDCYE1i7of_Zp3pIIXhGKO1ZM4XMHT5Hc9W2ifz"
			+ "VHC7Wx0p1ovU3ReAoyPmPtB36bh8p5kG0R6JrqsbaLqTW8wKWlfJ_H2ahn_g7z1BEzOksNZ5yZ08h6Ol"
			+ "d9iEd26PzTfIAMOouyQDyD-fCiSrAZ4jdMAFr5_gXwVx-dlpbAW_ru_rp_hnwGF_xldJDpYpL8angeoT"
			+ "THxskf4hiLvFIJM7EcWRloA3gHX5F2IxB5XWr_HoA4M5uhdHKibXSKZeSgW_rb-F9M5Wx3cVvyei9SFe"
			+ "eyr2dx88J6VmAHwXNeLhxcfOkAtF-PyPyzpDFxMzjnO0";

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
