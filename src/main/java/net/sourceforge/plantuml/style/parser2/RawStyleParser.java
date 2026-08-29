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

		// The legacy character-level tokenizer never checked for this at all: reaching
		// end-of-file with a selector block still open was simply the end of parsing, not an
		// error -- whatever was nested inside that block and DID get its own closing brace (an
		// "arrow { ... }" inside an unclosed "activityDiagram { ... ") was already linked to it
		// the moment that child's own '}' was seen (see closeCurrentRule), so nothing is lost by
		// not raising an error here, only by discarding that parent along with the file's
		// missing brace. Close every remaining open rule now instead, exactly as if the missing
		// '}' characters had actually been there.
		while (stack.isEmpty() == false)
			closeCurrentRule();

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

	/**
	 * A stray {@code }} with nothing open to close is silently ignored, rather than raising an
	 * error -- exactly like the legacy character-level tokenizer, which never even had the
	 * chance to notice one: its own {@code Context#pop()} is always guarded by
	 * {@code Context#isEmpty()} first ({@code StyleParser}'s {@code CLOSE_BRACKET} handling
	 * only pops "if (context.isEmpty() == false)"), so an extra closing brace back at the
	 * top level was always a silent no-op, never a parse error. A real-world {@code <style>}
	 * block with exactly this shape -- one closing brace too many, right before
	 * {@code </style>} -- must keep parsing exactly as it did before.
	 */
	private void closeCurrentRule() {
		if (stack.isEmpty())
			return;

		final RawStyleRule closed = stack.pop();
		if (stack.isEmpty())
			topLevel.add(closed);
		else
			stack.peek().addChild(closed);
	}

	// -----------------------------------------------------------------------
	// Lexical helpers
	// -----------------------------------------------------------------------

	/**
	 * Reads everything up to (not including) the next {@code ;}, end-of-line or {@code }} --
	 * except for a double-quoted segment (e.g. {@code FontName "Cascadia Code PL"}, needed to
	 * carry a space through as part of one value): its surrounding quotes are dropped and its
	 * content copied verbatim, exactly like the legacy character-level tokenizer's own
	 * {@code readQuotedString} does before its value-joining step ever sees the text, so a quoted
	 * value doesn't reach a caller with its literal quote characters still attached (which is
	 * indistinguishable from a font name that simply doesn't exist).
	 */
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
			if (c == '\"') {
				ins.jump();
				while (ins.peek(0) != '\0' && ins.peek(0) != '\"') {
					sb.append(ins.peek(0));
					ins.jump();
				}
				if (ins.peek(0) == '\"')
					ins.jump();
				continue;
			}
			sb.append(c);
			ins.jump();
		}
		return sb.toString().trim();
	}

	/**
	 * Reads one bare identifier, or a double-quoted string; stops before any delimiter --
	 * except that a name starting with {@code '.'} (a stereotype selector, e.g.
	 * {@code .static lib { ... }}) is allowed to carry literal embedded spaces, exactly like
	 * the legacy character-level tokenizer's own {@code StyleParser#readString}, whose only
	 * break condition on a space is {@code ch == ' ' && result.charAt(0) != '.'} -- a dot-led
	 * token never stops at a space there, only at a "harder" delimiter (tab, newline, '{',
	 * '}', ';', ',', ':', '*', or end of input), and gets {@code trim()}-ed once read. Without
	 * this, ".static lib {" is misread as the single-word selector ".static" followed by a
	 * bare "lib" that belongs to neither a selector nor a property, which is exactly the real
	 * user-visible regression this reproduces: "Property '.static' declared outside of any
	 * block", instead of the two-word stereotype name the legacy parser always accepted.
	 */
	private String readIdentifier() {
		if (ins.peek(0) == '\"')
			return readQuoted();

		final boolean stereotypeLed = ins.peek(0) == '.';
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char c = ins.peek(0);
			if (c == ' ' && stereotypeLed) {
				sb.append(c);
				ins.jump();
				continue;
			}
			if (isDelimiter(c))
				break;
			sb.append(c);
			ins.jump();
		}
		return stereotypeLed ? sb.toString().trim() : sb.toString();
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
		// '*' included: unlike the legacy character-level tokenizer (where '*' is always its
		// own token, so "name*" and "name *" tokenize identically), readIdentifier() would
		// otherwise swallow a directly-attached trailing '*' into the name itself -- silently
		// losing the star altogether, since neither the selector-list star check just below
		// nor RawSelector#classify's "depth(n)" pattern (which requires the string to end in
		// ')') would then recognize it. "depth(2)*", written without a space, is exactly the
		// real hand-written ancestor-cascade catch-all syntax this must not break.
		return c == '\0' || c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '{' || c == '}' || c == ';'
				|| c == ',' || c == ':' || c == '*';
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
