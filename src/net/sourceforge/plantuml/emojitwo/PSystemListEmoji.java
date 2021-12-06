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
package net.sourceforge.plantuml.emojitwo;

import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.PlainStringsDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;

public class PSystemListEmoji extends PlainStringsDiagram {

	public PSystemListEmoji(UmlSource source, String text) {
		super(source);
		strings.add("   <b><size:16>Emoji available on Unicode Block " + text);
		strings.add("   (Blocks available: 26, 1F3, 1F4, 1F5, 1F6, 1F9)");
		strings.add(" ");

		final Map<String, EmojiTwo> all = EmojiTwo.getAll();
		for (Entry<String, EmojiTwo> ent : all.entrySet()) {
			final String code = ent.getKey();
			final StringBuilder sb = new StringBuilder();
			if (code.startsWith(text) == false)
				continue;

			final String shortcut = ent.getValue().getShortcut();

			sb.append("<size:13>");
			sb.append("\"\"<U+003C>:" + code + ":<U+003E> \"\"");
			sb.append("<:" + code + ":>");
			sb.append(" ");
			sb.append("<." + code + ".>");
			if (shortcut != null) {
				sb.append(" ");
				sb.append("\"\"<U+003C>:" + shortcut + ":<U+003E> \"\"");
			}
			// strings.add(" ");
			strings.add(sb.toString());

		}

	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(List Emoji)");
	}

}
