/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4041 $
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
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		return getGraphicStrings().exportDiagram(os, fileFormat);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add("<b>Special thanks to our sponsors and donors !");
		lines.add(" ");
		final Transcoder t = new TranscoderImpl();
		final String s = t
				.decode("UDfLZb4A0Z4CHF_d5FtkBG92GHOHrWjaDOk5DO6sAwodNmLBz7rbo2GpjCGlKd2M9YMGDjFCLrmaElR0O2vB1VLJdf2AhsJ3A1FNbbd3oUuAqfkLa4pQIpxZFByTtSwAWN-2jE6GkyWBo5_R4-Fg5JQa7FzHmx6tN2js5iStZG00");
		final StringTokenizer st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		lines.add(" ");
		final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(lines, font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE,
				PSystemVersion.getPlantumlImage(), GraphicPosition.BACKGROUND_CORNER, UAntiAliasing.ANTI_ALIASING_ON);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Donors)", getClass());
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
