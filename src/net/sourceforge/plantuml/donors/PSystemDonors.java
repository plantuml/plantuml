/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

	public static final String DONORS = "UDfbL4jksp0GtSyfk1RIGoXsxDI_17QQ0jcDfR75c29TKd8ghOAUeK3NlKABB8eMsVG2oeqwSYkJIWa8"
			+ "q8Z3uNipxobMqRz55gwf82jYNHWjCO4RYlmoESoC3ws3k3kAQnXRlqKLh6YDhf2enS9a6cAT6Yl6XegQ"
			+ "sjVDXZEwTDGmm-0YNS1GTe5K4FlIvW2hqaEeO2oZ_j869XtAqi6ge7r6MWoCsJE_ONSFEPZchuuQ2mEZ"
			+ "f1PBKpg2IVbgFxetJAGBzXBc0MhCmv2lYXLzH9roEmUPvAfRj1KpNPvewoqiGlH53LTA7lZ0K2OxHdxQ"
			+ "aHektAcq3a14T81XdSl2LcA6cKaH9ctJxpBaya5UHCgFYzUkuZPfB3YFyJ6koL_mjEknqYc7Dk6UZ71j"
			+ "gvrvS3l9XHGVkxA5hE7IPwS6vZB9I8bpbAeA6QWw1ECSJ0Zrcmac7qfoObYkBV8ibUTxQ-uBIZ8njUZG"
			+ "GzeIIpIOs4EhB4uHKtEGeL2EKddd2Kk4PI3UzegGPtJFf_U1hCokxSoviQXHxS_47HLezWwMJ11jAgQo"
			+ "7IsCbB5jl-U_Vobr7BeNX0h4oNAQdUHaiUjsE_MipyZ1DNRoMCeUu5X9iK9CsJfsI-8zwItPCp6FRzwk"
			+ "OY_jo6WOKyyo8NETi4HoSSkeXopQgS-qr4wMpJ8ZWej3gFm3mi8NeRm36GKqngLaNPGvYP4fgV3zJHoC"
			+ "miaSkNWrw3fi6yDTo2rhQ2LpRq46n4xUj9r6TdK0Eb8Aci_DKr2-UMf-m23hMRjYuvNhuu_0QlR2fUd1"
			+ "6PFPR2nLOY5p7jm4__-hB763IimhfzWZpTVcwUMn-TvU9PelpNFpitb--SJF8t_w1XUMQZ55bREpBiDP"
			+ "jt8brkivgNPJmny9r86N";

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, os);
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
