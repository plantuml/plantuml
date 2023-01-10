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
package net.sourceforge.plantuml.hcl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.jsondiagram.JsonDiagram;
import net.sourceforge.plantuml.jsondiagram.StyleExtractor;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.yaml.Highlighted;

public class HclDiagramFactory extends PSystemAbstractFactory {

	public HclDiagramFactory() {
		super(DiagramType.HCL);
	}

	@Override
	public Diagram createSystem(UmlSource source, Map<String, String> skinParam) {
		final List<Highlighted> highlighted = new ArrayList<>();
		JsonValue data = null;
		StyleExtractor styleExtractor = null;
		try {
			final HclSource list = new HclSource();
			styleExtractor = new StyleExtractor(source.iterator2());
			final Iterator<String> it = styleExtractor.getIterator();
			it.next();
			while (true) {
				final String line = it.next();
				if (it.hasNext() == false)
					break;

				list.add(line);
			}
			HclParser parser = new HclParser(list);
			data = parser.parseMe();
		} catch (Exception e) {
			Logme.error(e);
		}
		final JsonDiagram result = new JsonDiagram(source, UmlDiagramType.HCL, data, highlighted, styleExtractor);
//		if (styleExtractor != null) {
//			styleExtractor.applyStyles(result.getSkinParam());
//			final String title = styleExtractor.getTitle();
//			if (title != null)
//				result.setTitle(DisplayPositioned.single(Display.getWithNewlines(title), HorizontalAlignment.CENTER,
//						VerticalAlignment.CENTER));
//		}
		return result;
	}

}
