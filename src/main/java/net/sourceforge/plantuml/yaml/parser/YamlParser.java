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

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the subset of YAML that PlantUML uses: nested maps, lists (of
 * scalars and maps), inline lists <code>[a, b]</code>, quoted scalars,
 * block scalars (<code>|</code> and <code>&gt;</code>), plain scalars on
 * several lines, and comments. Every scalar stays a String.
 * <p>
 * What is not supported is an error (flow mappings, a list inside a list) or
 * left as plain text (anchors, tags, several documents). Every error is a
 * {@link YamlSyntaxException} that carries the line (counted from 1).
 */
public class YamlParser {

	/** The lines to read. */
	private static class Lines {
		private final List<String> text;
		private int pos = 0;

		Lines(List<String> text) {
			this.text = text;
		}

		String peek(int ahead) {
			final int tmp = pos + ahead;
			return tmp < text.size() ? text.get(tmp) : null;
		}

		void jump() {
			pos++;
		}

		/** The line being read, counted from 1. */
		int lineNumber() {
			return pos + 1;
		}
	}

	private static final String LIST_IN_LIST = "a list inside a list is not supported";

	/** "- - a": the first element of a list is a list. */
	private static boolean isListInList(String line) {
		final String rest = line.trim();
		return rest.startsWith("- ") && rest.substring(2).trim().startsWith("- ");
	}

	public Monomorph parse(List<String> input) {
		final YamlBuilder yamlBuilder = new YamlBuilder();
		final Lines lines = new Lines(input);
		// An indentation deeper than the current level is only legal right after a line
		// that opens a level (a key without value, a dash).
		boolean opened = false;

		for (; lines.peek(0) != null; lines.jump()) {
			final int lineNumber = lines.lineNumber();
			try {
				final YamlLine yamlLine = YamlLine.build(lines.peek(0));
				if (yamlLine.getType() == YamlLineType.EMPTY_LINE)
					continue;

				if (yamlLine.getType() == YamlLineType.NO_KEY_ONLY_TEXT)
					throw new YamlSyntaxException("text without a key: " + yamlLine.getValue());

				if (isListInList(lines.peek(0)))
					throw new YamlSyntaxException(LIST_IN_LIST);

				if (yamlLine.getType() == YamlLineType.PLAIN_DASH) {
					final YamlLine next = peekNext(lines);
					if (next != null && next.isListItem() && next.getIndent() - 2 > yamlLine.getIndent() - 1)
						throw new YamlSyntaxException(LIST_IN_LIST);
					yamlBuilder.onListItemPlainDash();
					opened = true;
					continue;
				}

				if (yamlLine.getIndent() > yamlBuilder.currentIndent() && yamlBuilder.currentIndent() != -1
						&& opened == false)
					throw new YamlSyntaxException("unexpected indentation");

				yamlBuilder.adjustIndentation(yamlLine.getIndent());
				opened = onLine(yamlBuilder, yamlLine, lines);
			} catch (YamlSyntaxException e) {
				if (e.getLine() != 0)
					throw e;
				throw new YamlSyntaxException(e.getMessage(), lineNumber);
			} catch (RuntimeException e) {
				// the builder was given a structure that makes no sense (a list among the keys of a map...)
				throw new YamlSyntaxException("inconsistent structure", lineNumber);
			}
		}

		return yamlBuilder.getResult();

	}

	/** Handles a line that is not empty, nor a plain dash. True if it opens a deeper level. */
	private boolean onLine(YamlBuilder yamlBuilder, YamlLine yamlLine, Lines lines) {
		if (yamlLine.isListItem()) {
			if (yamlLine.getType() == YamlLineType.KEY_ONLY) {
				yamlBuilder.onListItemOnlyKey(yamlLine.getKey());
				return true;
			}
			if (yamlLine.getType() == YamlLineType.PLAIN_ELEMENT_LIST)
				yamlBuilder.onListItemOnlyValue(yamlLine.getValue());
			else if (yamlLine.getType() == YamlLineType.KEY_AND_VALUE)
				yamlBuilder.onListItemKeyAndValue(yamlLine.getKey(), plainScalar(yamlLine, lines));
			else if (yamlLine.getType() == YamlLineType.KEY_AND_BLOCK_STYLE)
				yamlBuilder.onListItemKeyAndValue(yamlLine.getKey(), getBlockStyleString(yamlLine.getIndent(), lines));
			else if (yamlLine.getType() == YamlLineType.KEY_AND_FOLDED_STYLE)
				yamlBuilder.onListItemKeyAndValue(yamlLine.getKey(), fold(getBlockStyleString(yamlLine.getIndent(), lines)));
			else if (yamlLine.getType() == YamlLineType.KEY_AND_FLOW_SEQUENCE)
				yamlBuilder.onListItemKeyAndFlowSequence(yamlLine.getKey(), yamlLine.getValues());
			else
				throw new YamlSyntaxException("unsupported list item");
			return false;
		}
		if (yamlLine.getType() == YamlLineType.KEY_ONLY) {
			final YamlLine next = peekNext(lines);
			if (next == null || next.getIndent() <= yamlLine.getIndent()) {
				yamlBuilder.onKeyAndValue(yamlLine.getKey(), "");
				return false;
			}
			if (next.getType() == YamlLineType.NO_KEY_ONLY_TEXT) {
				yamlBuilder.onKeyAndValue(yamlLine.getKey(), peekNextOnlyText(lines));
				return false;
			}
			yamlBuilder.onOnlyKey(yamlLine.getKey());
			return true;
		}
		if (yamlLine.getType() == YamlLineType.KEY_AND_VALUE)
			yamlBuilder.onKeyAndValue(yamlLine.getKey(), plainScalar(yamlLine, lines));
		else if (yamlLine.getType() == YamlLineType.KEY_AND_BLOCK_STYLE)
			yamlBuilder.onKeyAndValue(yamlLine.getKey(), getBlockStyleString(yamlLine.getIndent(), lines));
		else if (yamlLine.getType() == YamlLineType.KEY_AND_FOLDED_STYLE)
			yamlBuilder.onKeyAndValue(yamlLine.getKey(), fold(getBlockStyleString(yamlLine.getIndent(), lines)));
		else if (yamlLine.getType() == YamlLineType.KEY_AND_FLOW_SEQUENCE)
			yamlBuilder.onKeyAndFlowSequence(yamlLine.getKey(), yamlLine.getValues());
		else
			throw new YamlSyntaxException("unsupported line");
		return false;
	}

	private static int leadingSpaces(String s) {
		int count = 0;
		while (count < s.length() && s.charAt(count) == ' ')
			count++;
		return count;
	}

	/**
	 * The value of "key: value", plus the lines that continue it (deeper, without a
	 * key): in a plain scalar on several lines, the line breaks are spaces.
	 */
	private String plainScalar(YamlLine yamlLine, Lines lines) {
		final StringBuilder result = new StringBuilder(yamlLine.getValue());
		while (true) {
			final YamlLine next = peekNext(lines);
			if (next == null || next.getType() != YamlLineType.NO_KEY_ONLY_TEXT
					|| next.getIndent() <= yamlLine.getIndent())
				return result.toString();
			result.append(" ").append(peekNextOnlyText(lines));
		}
	}

	private String peekNextOnlyText(Lines lines) {
		final StringBuilder result = new StringBuilder();
		do {
			final String peek = lines.peek(1);
			if (peek == null)
				return result.toString();
			final YamlLine next = YamlLine.build(peek);
			if (next != null && next.getType() == YamlLineType.EMPTY_LINE) {
				lines.jump();
				continue;
			}
			if (next.getType() == YamlLineType.NO_KEY_ONLY_TEXT) {
				if (result.length() > 0)
					result.append(" ");
				result.append(next.getValue());
				lines.jump();
				continue;
			}
			return result.toString();
		} while (true);
	}

	private YamlLine peekNext(Lines lines) {
		int i = 1;
		do {
			final String peek = lines.peek(i);
			if (peek == null)
				return null;
			final YamlLine next = YamlLine.build(peek);
			if (next != null && next.getType() == YamlLineType.EMPTY_LINE)
				i++;
			else
				return next;
		} while (true);
	}

	/**
	 * The text of a block scalar ("|"): the lines that follow, indented deeper than
	 * the key, without the indentation of the first one (a deeper indentation is
	 * kept, and so are the comments), and one line break at the end.
	 */
	private String getBlockStyleString(int indent, Lines lines) {
		final List<String> block = new ArrayList<>();
		int blockIndent = -1;
		while (lines.peek(1) != null) {
			final String line = lines.peek(1);
			if (line.trim().isEmpty()) {
				block.add("");
				lines.jump();
				continue;
			}
			final int lineIndent = leadingSpaces(line.replace("\t", "    "));
			if (lineIndent <= indent)
				break;
			if (blockIndent == -1)
				blockIndent = lineIndent;
			block.add(line.replace("\t", "    ").substring(Math.min(blockIndent, lineIndent)).replaceAll("\\s+$", ""));
			lines.jump();
		}
		// The empty lines at the end are not part of the text.
		while (block.size() > 0 && block.get(block.size() - 1).isEmpty())
			block.remove(block.size() - 1);
		final StringBuilder result = new StringBuilder();
		for (String line : block)
			result.append(line).append("\n");
		return result.toString();
	}

	/**
	 * The folded form (">") of a block: the line breaks between two lines of text are
	 * spaces, an empty line is a line break. (A line indented deeper is folded like the
	 * others: YAML keeps its line breaks, which is not done here.)
	 */
	private static String fold(String block) {
		if (block.isEmpty())
			return block;
		final StringBuilder result = new StringBuilder();
		boolean previousIsText = false;
		int empty = 0;
		for (String line : block.split("\n", -1)) {
			if (line.isEmpty()) {
				empty++;
				continue;
			}
			if (previousIsText && empty == 0)
				result.append(' ');
			else
				for (int i = 0; i < empty; i++)
					result.append('\n');
			empty = 0;
			result.append(line);
			previousIsText = true;
		}
		return result.append('\n').toString();
	}

}
