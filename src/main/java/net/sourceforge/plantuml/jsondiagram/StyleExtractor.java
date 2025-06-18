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
package net.sourceforge.plantuml.jsondiagram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

public class StyleExtractor {

	private final List<String> list = new ArrayList<>();
	private final List<StringLocated> style = new ArrayList<>();
	private String title = null;
	private boolean handwritten = false;
	private String scale = null;
	private String newSkin;

	public StyleExtractor(Iterator<StringLocated> data) {
		while (data.hasNext()) {
			StringLocated line = data.next();
			final String s = line.getString().trim();
			if (s.length() == 0)
				continue;
			if (startStyle(s)) {
				while (data.hasNext()) {
					style.add(line);
					if (endStyle(line))
						break;
					line = data.next();
				}
			} else if (list.size() <= 1 && s.startsWith("!assume ")) {
				// Ignore
			} else if (list.size() <= 1 && s.startsWith("!pragma ")) {
				// Ignore
			} else if (list.size() <= 1 && s.startsWith("hide ")) {
				// Ignore
			} else if (list.size() <= 1 && s.startsWith("scale ")) {
				this.scale = s;
			} else if (list.size() <= 1 && s.startsWith("title ")) {
				this.title = s.substring("title ".length()).trim();
			} else if (list.size() <= 1 && s.startsWith("skin ")) {
				this.newSkin = s.substring("skin ".length()).trim();
			} else if (list.size() <= 1 && s.startsWith("skinparam ")) {
				if (s.contains("handwritten") && s.contains("true"))
					handwritten = true;
				if (s.contains("{")) {
					while (data.hasNext()) {
						if (line.getString().trim().equals("}"))
							break;
						line = data.next();
					}
				}
			} else {
				list.add(line.getString());
			}
		}

	}

	private boolean startStyle(String line) {
		return line.equals("<style>");
	}

	private boolean endStyle(StringLocated line) {
		return line.getString().trim().equals("</style>");
	}

	@DuplicateCode(reference = "TitledDiagram")
	public void applyStyles(ISkinParam skinParam) throws StyleParsingException {
		if (newSkin != null) {
			final String filename = newSkin + ".skin";
			skinParam.setDefaultSkin(filename);
		}

		if (style.size() > 0) {
			final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
			final BlocLines blocLines = BlocLines.from(style);
			skinParam.muteStyle(StyleParser.parse(blocLines, styleBuilder));
		}
	}

	public Iterator<String> getIterator() {
		return list.iterator();
	}

	public String getTitle() {
		return title;
	}

	public final boolean isHandwritten() {
		return handwritten;
	}

	public String getScale() {
		return scale;
	}

}
