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
package net.sourceforge.plantuml.sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.donors.PSystemDonors;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockHorizontal;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public class PSystemListInternalSprites extends PlainDiagram {

	public PSystemListInternalSprites(UmlSource source) {
		super(source);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
		final List<String> lines = new ArrayList<>();
		lines.add("<b>List Current Sprites");
		lines.add("<i>Credit to");
		lines.add("http://www.archimatetool.com");
		lines.add(" ");
		for (String folder : RessourcesUtils.getJarFile("sprites", true)) {
			lines.add("<u>" + folder + "</u> :");
			lines.add(" ");
			for (String png : RessourcesUtils.getJarFile("sprites/" + folder, false)) {
				if (png.endsWith(".png")) {
					final String spriteName = png.substring(0, png.length() - 4);
					lines.add("<$archimate/" + spriteName + "> " + spriteName);
				}
			}
		}
		final List<TextBlock> cols = PSystemDonors.getCols(lines, 4, 0);
		return new TextBlockHorizontal(cols, VerticalAlignment.TOP);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Sprites)");
	}

}
