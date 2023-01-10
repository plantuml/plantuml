/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.yaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositioned;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.jsondiagram.JsonDiagram;
import net.sourceforge.plantuml.jsondiagram.StyleExtractor;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.style.parser.StyleParsingException;

public class YamlDiagramFactory extends PSystemAbstractFactory {

	public YamlDiagramFactory() {
		super(DiagramType.YAML);
	}

	@Override
	public Diagram createSystem(UmlSource source, Map<String, String> skinParam) {
		final List<Highlighted> highlighted = new ArrayList<>();
		JsonValue yaml = null;
		StyleExtractor styleExtractor = null;
		try {
			final List<String> list = new ArrayList<>();
			styleExtractor = new StyleExtractor(source.iterator2());
			final Iterator<String> it = styleExtractor.getIterator();
			it.next();
			while (true) {
				final String line = it.next();
				if (it.hasNext() == false)
					break;

				if (Highlighted.matchesDefinition(line)) {
					highlighted.add(Highlighted.build(line));
					continue;
				}
				list.add(line);
			}
			yaml = new SimpleYamlParser().parse(list);
		} catch (Exception e) {
			Logme.error(e);
		}
		final JsonDiagram result = new JsonDiagram(source, UmlDiagramType.YAML, yaml, highlighted, styleExtractor);
		if (styleExtractor != null) {
			try {
				styleExtractor.applyStyles(result.getSkinParam());
			} catch (StyleParsingException e) {
				Logme.error(e);
			}
			final String title = styleExtractor.getTitle();
			if (title != null)
				result.setTitle(DisplayPositioned.single(Display.getWithNewlines(title), HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER));
		}
		return result;
	}

}
