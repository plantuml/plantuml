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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleLoader;
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
				// Neither the opening "<style>" (already consumed as "line" above, never
				// added) nor the closing "</style>" join the collected content: a diagram can
				// carry more than one such block back to back -- "!theme xxx" is itself
				// expanded into its own inline "<style>...</style>" ahead of the source, so a
				// diagram with its own explicit <style> block accumulates two occurrences here,
				// one straight after the other. Keeping a boundary tag in the collected content
				// would leave it sitting mid-stream once every block's tags are concatenated
				// together below, which RawStyleParser (unlike the legacy tokenizer) does not
				// tolerate wherever it appears -- see applyStyles.
				while (data.hasNext()) {
					line = data.next();
					if (endStyle(line))
						break;
					style.add(line);
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
			// Check the file right now, so that an invalid style is reported here
			// instead of crashing later. StyleLoader caches, so this costs nothing.
			final StyleBuilder loaded;
			try {
				loaded = StyleLoader.loadSkin(filename);
			} catch (IOException | NoStyleAvailableException e) {
				throw new StyleParsingException("Cannot find style " + newSkin);
			}
			// As in TitledDiagram: a fragment cannot replace the whole style sheet.
			final List<PName> missing = StyleLoader.getMissingRootProperties(loaded);
			if (missing.size() > 0)
				throw new StyleParsingException("Incomplete style " + newSkin + ": root does not define " + missing);

			skinParam.setDefaultSkin(filename);
		}

		if (style.size() > 0) {
			// "style" never carries a "<style>"/"</style>" boundary line -- see the comment
			// where it is collected above -- so there is nothing left to strip here, unlike
			// every other muteStyle caller (CommandStyleMultilinesCSS and friends), which strips
			// its own opening/closing marker line with subExtract(1, 1) instead.
			final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
			final BlocLines blocLines = BlocLines.from(style);
			skinParam.muteStyle(StyleLoader.parseStyleText(blocLines, styleBuilder));
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
