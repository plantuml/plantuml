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

import net.sourceforge.plantuml.utils.Peeker;
import net.sourceforge.plantuml.utils.PeekerUtils;

public class YamlParser {

	public Monomorph parse(List<String> lines) {
		final IndentationStack indentationStack = new IndentationStack();
		final YamlBuilder yamlBuilder = new YamlBuilder();

		for (Peeker<String> peeker = PeekerUtils.peeker(lines); peeker.peek(0) != null; peeker.jump()) {

			final YamlLine yamlLine = YamlLine.build(peeker.peek(0));
			if (yamlLine.getType() == YamlLineType.EMPTY_LINE)
				continue;

			if (yamlLine.getType() == YamlLineType.NO_KEY_ONLY_TEXT)
				throw new YamlSyntaxException("YamlLineType.NO_KEY_ONLY_TEXT");

			if (yamlLine.getType() == YamlLineType.PLAIN_DASH) {
				yamlBuilder.onListItemPlainDash();
				continue;
			}

			if (indentationStack.size() == 0)
				indentationStack.push(yamlLine.getIndent());

			if (yamlLine.getIndent() > indentationStack.peek()) {
				yamlBuilder.increaseIndentation();
				indentationStack.push(yamlLine.getIndent());
			} else
				while (yamlLine.getIndent() < indentationStack.peek()) {
					yamlBuilder.decreaseIndentation();
					indentationStack.pop();
				}

			if (yamlLine.getIndent() == indentationStack.peek()) {
				if (yamlLine.isListItem()) {
					if (yamlLine.getType() == YamlLineType.KEY_ONLY)
						yamlBuilder.onListItemOnlyKey(yamlLine.getKey());
					else if (yamlLine.getType() == YamlLineType.PLAIN_ELEMENT_LIST)
						yamlBuilder.onListItemOnlyValue(yamlLine.getValue());
					else if (yamlLine.getType() == YamlLineType.KEY_AND_VALUE)
						yamlBuilder.onListItemKeyAndValue(yamlLine.getKey(), yamlLine.getValue());
					else if (yamlLine.getType() == YamlLineType.KEY_AND_FLOW_SEQUENCE)
						yamlBuilder.onListItemKeyAndFlowSequence(yamlLine.getKey(), yamlLine.getValues());
					else
						throw new UnsupportedOperationException("wip3 " + yamlLine);
				} else if (yamlLine.getType() == YamlLineType.KEY_ONLY) {
					final YamlLine next = peekNext(peeker);
					if (next == null || next.getIndent() <= yamlLine.getIndent())
						yamlBuilder.onKeyAndValue(yamlLine.getKey(), "");
					else if (next.getType() == YamlLineType.NO_KEY_ONLY_TEXT)
						yamlBuilder.onKeyAndValue(yamlLine.getKey(), peekNextOnlyText(peeker));
					else
						yamlBuilder.onOnlyKey(yamlLine.getKey());
				} else if (yamlLine.getType() == YamlLineType.KEY_AND_VALUE)
					yamlBuilder.onKeyAndValue(yamlLine.getKey(), yamlLine.getValue());
				else if (yamlLine.getType() == YamlLineType.KEY_AND_BLOCK_STYLE)
					yamlBuilder.onKeyAndValue(yamlLine.getKey(), getBlockStyleString(yamlLine.getIndent(), peeker));
				else if (yamlLine.getType() == YamlLineType.KEY_AND_FLOW_SEQUENCE)
					yamlBuilder.onKeyAndFlowSequence(yamlLine.getKey(), yamlLine.getValues());
				else
					throw new UnsupportedOperationException("wip4 " + yamlLine);

			} else
				throw new UnsupportedOperationException("wip5 " + yamlLine);
		}

		return yamlBuilder.getResult();

	}

	private String peekNextOnlyText(Peeker<String> peeker) {
		final StringBuilder result = new StringBuilder();
		do {
			final String peek = peeker.peek(1);
			if (peek == null)
				return result.toString();
			final YamlLine next = YamlLine.build(peek);
			if (next != null && next.getType() == YamlLineType.EMPTY_LINE) {
				peeker.jump();
				continue;
			}
			if (next.getType() == YamlLineType.NO_KEY_ONLY_TEXT) {
				if (result.length() > 0)
					result.append(" ");
				result.append(next.getValue());
				peeker.jump();
				continue;
			}
			return result.toString();
		} while (true);
	}

	private YamlLine peekNext(Peeker<String> peeker) {
		int i = 1;
		do {
			final String peek = peeker.peek(i);
			if (peek == null)
				return null;
			final YamlLine next = YamlLine.build(peek);
			if (next != null && next.getType() == YamlLineType.EMPTY_LINE)
				i++;
			else
				return next;
		} while (true);
	}

	private String getBlockStyleString(int indent, Peeker<String> peeker) {
		final StringBuilder result = new StringBuilder();
		while (peeker.peek(1) != null) {
			final String line = peeker.peek(1);
			if (line == null)
				return result.toString();
			final YamlLine yamlLine = YamlLine.build(line);
			if (yamlLine.getType() != YamlLineType.NO_KEY_ONLY_TEXT && yamlLine.getType() != YamlLineType.EMPTY_LINE
					&& yamlLine.getIndent() <= indent)
				return result.toString();

			result.append(cleanBlockStyle(indent, line));
			result.append("\n");
			peeker.jump();

		}
		return result.toString();
	}

	private static String cleanBlockStyle(int indent, String s) {
		// Not finished!
		return s.trim();
	}

}
