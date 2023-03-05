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
package net.sourceforge.plantuml.eggs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemWelcome extends PlainDiagram {

	private final List<String> strings = new ArrayList<>();
	private final GraphicPosition position;

	public PSystemWelcome(UmlSource source, GraphicPosition position) {
		super(source);
		this.position = position;
		strings.add("<b>Welcome to PlantUML!");
		strings.add(" ");
		strings.add("You can start with a simple UML Diagram like:");
		strings.add(" ");
		strings.add("\"\"Bob->Alice: Hello\"\"");
		strings.add(" ");
		strings.add("Or");
		strings.add(" ");
		strings.add("\"\"class Example\"\"");
		strings.add(" ");
		strings.add("You will find more information about PlantUML syntax on <u>https://plantuml.com</u>");
		strings.add(" ");
		strings.add("(Details by typing \"\"license\"\" keyword)");
		strings.add(" ");
		if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
			strings.add(" ");
			strings.add(" ");
			strings.add(" ");
			strings.add(" ");
		}
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
		return getGraphicStrings();
	}

	public TextBlock getGraphicStrings() {
		if (position != null)
			return GraphicStrings.createBlackOnWhite(strings, PSystemVersion.getPlantumlImage(), position);

		return GraphicStrings.createBlackOnWhite(strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Empty)");
	}

}
