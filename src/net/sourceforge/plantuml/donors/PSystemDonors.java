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

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	public static final String DONORS = "UDfTKqjosp0CtUCKN6lIGoXMx5R-96EdwKnsi8HOj2d29IcbyjcwwyKAfPL9XojX183lnzna_myvUA90JkKsiDLOmJDbyN0BAuwbyv1FexY7XOk_Q80TxT47ZLPjkB6GsvgTMZ252usVRs_ICRMZXHKc5za0XPuARI2Fgxc7NHSX3B3GsUsn3EM4ilEm2tHkoAePyrdu5TCzv67PVXfb1pCssgejTvwWx3xzpuuiHARYh66TeCOsBUMgtD0ljBN8EMkqR3sW6uJfzahR7c2JeWyNU3Iwbu52LoT1VlsZBJpuwsgRWCYeX-BIwk06jSA6QuHo5FsrGLcVz6LaebroJeF8P358d-0JNEK_y7IwOMLhALqbcinnxuOJl_kJbaL6nXvTq1Vu6hjhXhMk6ZBg7hKPKWRcaeBn7af2-wN4wcT7NXNTtg5uQIBV5pu6gXgrTEWnGZgGH3Hnx7rCbhIe9VSw3Sf7LBv7mXfXcuHtl2BbcHrb-fp4Ya-ZClViqA8vtwbN2iXd3riXYAvMIprQ2tF3RjHx_UUtCH-cnq2O99mIfzKrJWudjKVNcrj6Fad8QDeJJc7PwXlukT5gXrZBGt87KZ_87iZTgNN-_6sNnw1dR651DmyerS5M4fXMlGY7GYAyZ9sEHf_bwQo8uA5Ftq6Fi8bBg9r0PWcfHQSb9LrBQiuTXQXsvM6U-ZHo-Giz_Inc";

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final GraphicStrings result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, result.getBackcolor(),
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, os);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add("<b>Special thanks to our sponsors and donors !");
		lines.add(" ");
		int i = 0;
		final List<String> donors = getDonors();
		final int maxLine = (donors.size() + 1) / 2;
		for (String d : donors) {
			lines.add(d);
			i++;
			if (i == maxLine) {
				lines.add(" ");
				lines.add(" ");
				i = 0;
			}
		}
		lines.add(" ");
		final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
		final GraphicStrings graphicStrings = new GraphicStrings(lines, font, HtmlColorUtils.BLACK,
				HtmlColorUtils.WHITE, UAntiAliasing.ANTI_ALIASING_ON, PSystemVersion.getPlantumlImage(),
				GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT);
		graphicStrings.setMaxLine(maxLine + 2);
		return graphicStrings;
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
		return new DiagramDescriptionImpl("(Donors)", getClass());
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
