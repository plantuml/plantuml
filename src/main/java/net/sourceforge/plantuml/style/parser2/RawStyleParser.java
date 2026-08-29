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
package net.sourceforge.plantuml.style.parser2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.StyleScheme;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.CharInspector;

/**
 * Turns the raw text of a .skin file into a {@link RawStyleSheet}: a literal tree of
 * {@code selector { ... }} blocks, with no attempt yet at merging duplicate selectors,
 * expanding comma lists, or resolving {@code @media} into a cascade -- see
 * {@link RawStyleRule}. That second pass, building the actual in-memory, queryable style
 * model, is left for later.
 *
 * Grammar handled here, ignoring whitespace and comments ({@code //}, {@code /* *}{@code
 * /} and {@code /' '/}) between tokens:
 *
 * <pre>
 *   sheet        ::= statement*
 *   statement    ::= rule | atMediaRule | variableDecl | propertyDecl
 *   rule         ::= ':'? selectorList '*'? '{' statement* '}'
 *   atMediaRule  ::= '@' anyCharExceptBraceOrSemicolon* '{' statement* '}'
 *   selectorList ::= name (',' name)*
 *   variableDecl ::= '--' name ':'? value (';' | end-of-line)
 *   propertyDecl ::= name ':'? value (';' | end-of-line)
 * </pre>
 */
public final class RawStyleParser {

	// Matches var(--name), var(-name) or var(name), the way CssVariables already tolerated it.
	private static final Pattern VAR_REF = Pattern.compile("var\\(\\s*-*([A-Za-z_][-\\w]*)\\s*\\)");

	private final CharInspector ins;
	private final Map<String, String> variables = new LinkedHashMap<String, String>();
	private final Deque<RawStyleRule> stack = new ArrayDeque<RawStyleRule>();
	private final List<RawStyleRule> topLevel = new ArrayList<RawStyleRule>();

	private RawStyleParser(CharInspector ins) {
		this.ins = ins;
	}

	public static RawStyleSheet parse(BlocLines lines) throws StyleParsingException {
		if (lines.size() == 0)
			return new RawStyleSheet(new LinkedHashMap<String, String>(), new ArrayList<RawStyleRule>());

		return new RawStyleParser(lines.inspectorWithNewlines()).run();
	}

	private RawStyleSheet run() throws StyleParsingException {
		while (true) {
			skipWhitespaceAndComments();
			final char c = ins.peek(0);
			if (c == '\0')
				break;

			if (c == '}') {
				ins.jump();
				closeCurrentRule();
			} else if (c == '@') {
				parseAtRule();
			} else if (c == ':') {
				// A selector may be written with a leading colon, e.g. ":foo {".
				ins.jump();
				parseSelectorHead(readIdentifier());
			} else {
				parseStatementStartingWithIdentifier();
			}
		}

		if (stack.isEmpty() == false)
			throw new StyleParsingException(stack.size() + " block(s) never closed with a '}'");

		return new RawStyleSheet(variables, topLevel);
	}

	// -----------------------------------------------------------------------
	// Statements
	// -----------------------------------------------------------------------

	private void parseStatementStartingWithIdentifier() throws StyleParsingException {
		final String head = readIdentifier();
		if (head.length() == 0)
			throw new StyleParsingException("Unexpected character '" + ins.peek(0) + "'");

		if (head.startsWith("--")) {
			parseVariableDeclaration(head);
			return;
		}

		skipWhitespaceAndComments();
		final char next = ins.peek(0);
		if (next == ',' || next == '*' || next == '{')
			parseSelectorHead(head);
		else
			parsePropertyDeclaration(head);
	}

	private void parseSelectorHead(String firstName) throws StyleParsingException {
		final List<String> names = new ArrayList<String>();
		names.add(firstName);

		skipWhitespaceAndComments();
		while (ins.peek(0) == ',') {
			ins.jump();
			skipWhitespaceAndComments();
			names.add(readIdentifier());
			skipWhitespaceAndComments();
		}

		boolean star = false;
		if (ins.peek(0) == '*') {
			star = true;
			ins.jump();
			skipWhitespaceAndComments();
		}

		if (ins.peek(0) != '{')
			throw new StyleParsingException("Expected '{' after selector " + names);
		ins.jump();

		final List<RawSelector> selectors = new ArrayList<RawSelector>();
		for (String name : names)
			selectors.add(RawSelector.classify(name));

		stack.push(RawStyleRule.forSelectors(selectors, star));
	}

	private void parseAtRule() throws StyleParsingException {
		ins.jump(); // consume '@'
		final StringBuilder header = new StringBuilder();
		while (true) {
			final char c = ins.peek(0);
			if (c == '\0' || c == '}' || c == ';')
				throw new StyleParsingException("Unterminated @ rule: '@" + header + "'");
			if (c == '{')
				break;
			header.append(c);
			ins.jump();
		}
		ins.jump(); // consume '{'
		stack.push(RawStyleRule.forMedia(header.toString().trim()));
	}

	private void parseVariableDeclaration(String name) {
		final String value = readValue();
		variables.put(name.substring(2), resolveVariables(value));
	}

	private void parsePropertyDeclaration(String name) throws StyleParsingException {
		if (stack.isEmpty())
			throw new StyleParsingException("Property '" + name + "' declared outside of any block");

		final String rawValue = readValue();
		final PName pname = PName.getFromName(name, StyleScheme.REGULAR);
		if (pname == null)
			return; // Unknown property name: silently ignored, as the legacy parser already did.

		stack.peek().putProperty(pname, resolveVariables(rawValue));
	}

	private void closeCurrentRule() throws StyleParsingException {
		if (stack.isEmpty())
			throw new StyleParsingException("Unexpected '}' with no matching '{'");

		final RawStyleRule closed = stack.pop();
		if (stack.isEmpty())
			topLevel.add(closed);
		else
			stack.peek().addChild(closed);
	}

	// -----------------------------------------------------------------------
	// Lexical helpers
	// -----------------------------------------------------------------------

	/** Reads everything up to (not including) the next {@code ;}, end-of-line or {@code }}. */
	private String readValue() {
		skipInlineSpaces();
		while (ins.peek(0) == ':')
			ins.jump();
		skipInlineSpaces();

		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char c = ins.peek(0);
			if (c == '\0' || c == '\n' || c == '\r' || c == '}')
				break;
			if (c == ';') {
				ins.jump();
				break;
			}
			sb.append(c);
			ins.jump();
		}
		return sb.toString().trim();
	}

	/** Reads one bare identifier, or a double-quoted string; stops before any delimiter. */
	private String readIdentifier() {
		if (ins.peek(0) == '\"')
			return readQuoted();

		final StringBuilder sb = new StringBuilder();
		while (isDelimiter(ins.peek(0)) == false) {
			sb.append(ins.peek(0));
			ins.jump();
		}
		return sb.toString();
	}

	private String readQuoted() {
		ins.jump(); // opening quote
		final StringBuilder sb = new StringBuilder();
		while (ins.peek(0) != '\0' && ins.peek(0) != '\"') {
			sb.append(ins.peek(0));
			ins.jump();
		}
		if (ins.peek(0) == '\"')
			ins.jump();
		return sb.toString();
	}

	private static boolean isDelimiter(char c) {
		return c == '\0' || c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '{' || c == '}' || c == ';'
				|| c == ',' || c == ':';
	}

	private void skipInlineSpaces() {
		while (ins.peek(0) == ' ' || ins.peek(0) == '\t')
			ins.jump();
	}

	private void skipWhitespaceAndComments() {
		while (true) {
			final char c = ins.peek(0);
			if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
				ins.jump();
			} else if (c == '/' && ins.peek(1) == '/') {
				skipUntil('\n');
			} else if (c == '/' && ins.peek(1) == '*') {
				ins.jump();
				ins.jump();
				skipUntilSequence('*', '/');
			} else if (c == '/' && ins.peek(1) == '\'') {
				ins.jump();
				ins.jump();
				skipUntilSequence('\'', '/');
			} else {
				return;
			}
		}
	}

	private void skipUntil(char end) {
		while (ins.peek(0) != '\0' && ins.peek(0) != end)
			ins.jump();
	}

	private void skipUntilSequence(char first, char second) {
		while (ins.peek(0) != '\0') {
			if (ins.peek(0) == first && ins.peek(1) == second) {
				ins.jump();
				ins.jump();
				return;
			}
			ins.jump();
		}
	}

	/** Replaces every {@code var(--name)} reference with the value learnt for {@code name}. */
	private String resolveVariables(String value) {
		if (value.indexOf("var(") == -1)
			return value;

		final Matcher m = VAR_REF.matcher(value);
		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			final String varName = m.group(1);
			final String replacement = variables.containsKey(varName) ? variables.get(varName) : m.group();
			m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
