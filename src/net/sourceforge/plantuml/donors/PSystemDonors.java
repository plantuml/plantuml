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

	public static final String DONORS = "UDfTKyjosp0ClEChUDQa7w5OYLlvaOoTfZEvmH9YqQ88bPIKoj_MMt-iK5gPj7aYXdZiBfQfIVuVSl1C7JcLsewjnW9UA0c76rXp29s7T2x50onTUA8HzdH0tscqQikLXTIMxDIIgJlJT7r_bumv7IsiCHfaEyZq76W3QMZD0-px0664fKwkZsN8PvIzXtr7RKLMBPXRuPVDSyZ3ghafPGSBDjgg7VUU8EzltfCJ2v4vQ3Li8jJOnA6CIWtzGbkAd8jAIzSZkb6OxYwqxH6sCVhk34z63rAGwQ8Mv9SNRU71NrhR24H20sJdHdTkL6kik4J89z7VAfJsKLv29gnBJZsAJ1OxoHDyWYl_1vxgArQsbD0LeiaZ7jnOyuUljJGoKlRaEds6ho6xPDZeeYAZxb6RCMPWpZ4Oxs-XvOJsIux5pvwyolg3Grcj2VHV-THHKQcLGuy1uP74iT42FoQZIOfQyQ1ZttmwvtlWhn5saPUd4J7jv2JLRUGohYUXxjcXHTFUgJVga5iFE-6BhbGhFMqQ7WsxIVxDdz_6V7eW-CD4NXLthI_kSZYBFvcWkcRa8yDCGZtZx9sTleAV6gtsYANyAtSazOFiaToTsgGlt_Pfy7t25fPqzPzY7MmX_cdKgt38nD6haEbeMhlyf3KHR4CDDV0mn9zaGFWBXv0VcW00";

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
