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
 */
package net.sourceforge.plantuml.yaml.parser;

import java.util.List;
import java.util.Optional;

import net.sourceforge.plantuml.json.JsonObject;

// https://yaml.org/spec/1.2.2/
public class YamlParser {

	public JsonObject parse(List<String> lines) {
		final IndentationStack indentationStack = new IndentationStack();
		final YamlBuilder yamlBuilder = new YamlBuilder();

		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			final Optional<YamlLine> optionalYamlLine = YamlLine.build(line);
			// System.err.println(line + " --> " + optionalYamlLine);
			if (optionalYamlLine.isPresent()) {
				final YamlLine yamlLine = optionalYamlLine.get();

				if (indentationStack.size() == 0)
					indentationStack.push(yamlLine.getIndent());

				if (yamlLine.getIndent() > indentationStack.peek()) {
					yamlBuilder.increaseIndentation();
					indentationStack.push(yamlLine.getIndent());
				} else
					while (yamlLine.getIndent() < indentationStack.peek()) {
						yamlBuilder.indentationDecrease();
						indentationStack.pop();
					}

				if (yamlLine.getIndent() == indentationStack.peek()) {

					if (yamlLine.isListItem())
						if (yamlLine.getType() == YamlValueType.ABSENT)
							yamlBuilder.onListItemOnlyKey(yamlLine.getKey());
						else if (yamlLine.getType() == YamlValueType.PLAIN_ELEMENT_LIST)
							yamlBuilder.onListItemOnlyValue(yamlLine.getValue());
						else
							yamlBuilder.onListItemKeyAndValue(yamlLine.getKey(), yamlLine.getValue());
					else if (yamlLine.getType() == YamlValueType.ABSENT)
						yamlBuilder.onOnlyKey(yamlLine.getKey());
					else if (yamlLine.getType() == YamlValueType.FLOW_SEQUENCE)
						throw new IllegalArgumentException("wip");
						// yamlBuilder.onKeyAndValue(yamlLine.getKey(), yamlLine.getValue());
					else
						yamlBuilder.onKeyAndValue(yamlLine.getKey(), yamlLine.getValue());

				} else
					throw new YamlSyntaxException("wip");

			}
		}

		return yamlBuilder.getResult();

	}

}
