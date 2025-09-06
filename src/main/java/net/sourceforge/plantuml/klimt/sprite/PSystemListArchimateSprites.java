/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.klimt.sprite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.donors.PSystemDonors;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockHorizontal;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class PSystemListArchimateSprites extends PlainDiagram {
	// ::remove file when __CORE__

	public PSystemListArchimateSprites(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
		final List<String> lines = new ArrayList<>();
		lines.add("<b>List Current Sprites");
		lines.add("<i>Credit to");
		lines.add("http://www.archimatetool.com");
		lines.add(" ");
		for (String folder : ResourcesUtils.getJarFile("sprites", true)) {
			lines.add("<u>" + folder + "</u> :");
			lines.add(" ");
			for (String file : ResourcesUtils.getJarFile("sprites/" + folder, false))
				if (file.endsWith(".png") || file.endsWith(".svg")) {
					final String format = file.substring(file.length() - 3);
					final String spriteName = file.substring(0, file.length() - 4);
					lines.add("<$archimate/" + spriteName + "> " + spriteName + " (" + format + ")");
				}

		}
		final List<TextBlock> cols = PSystemDonors.getCols(lines, 4, 0);
		return new TextBlockHorizontal(cols, VerticalAlignment.TOP);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Sprites)");
	}

}
