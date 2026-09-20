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
package net.sourceforge.plantuml.regex;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;

/**
 * Reads, from a command pattern alone, the first tokens a line must start with for that pattern to
 * have any chance of matching it -- so that the regex stays the only place a command says what it
 * accepts, instead of repeating it in a hand-written declaration that nothing keeps in sync.
 *
 * The token is the one {@code StringLocated#getFirstToken} computes, once the blanks the line may
 * open with are skipped:
 * <ul>
 * <li>the leading run of ASCII letters, lower-cased, for a line starting with a letter;</li>
 * <li>the first character alone, for a line starting with anything else -- {@code "}"},
 * {@code "-"}, {@code "#"}, each digit on its own...;</li>
 * <li>{@code ""} for a blank line.</li>
 * </ul>
 * Case is not an issue: the patterns are compiled with {@code CASE_INSENSITIVE} and without
 * {@code UNICODE_CASE}, which folds exactly the ASCII letters the token itself folds, and nothing
 * else.
 *
 * <p>
 * Everything here leans the same way on purpose. Returning a token too many only costs the regex
 * attempt it could have saved, while returning one too few makes a command unreachable for the
 * lines it left out, so every step that cannot be followed exactly is either widened or given up
 * on: {@link #from(String)} answers {@code null} -- "I cannot tell" -- and the command then
 * declares nothing and stays in the list tried for every line.
 *
 * <p>
 * Two things a reader will want to have in mind, because they are what most of the care below is
 * about:
 * <ul>
 * <li>a token is not over until a non-letter arrives, so {@code ^end[%s]*split$} does not describe
 * a line starting with {@code end}: {@code [%s]*} matches nothing at all, and {@code endsplit} is a
 * line this pattern accepts. An element that may be empty therefore leaves the token open for
 * whatever follows to extend;</li>
 * <li>a blank the line opens with is skipped by the token reader -- the blanks being those of
 * {@code StringUtils#isTrimmable}, U+00A0 included, which is exactly what {@code %s} stands for.
 * A blank therefore contributes nothing while the line may still be in its leading blanks, and
 * only closes the token once one has started. A character class that may be a blank <em>or</em>
 * something else, like {@code [%s,]}, is read both ways;</li>
 * <li>any other character, met where the line may still be in its leading blanks, is a token of
 * its own. That is why a character class, {@code \d} included, must be read as the exact set of
 * characters it holds: each of them is a token the command may start with. The ASCII letters of
 * a class, as in {@code [hx]-axis}, are read one by one like any other letter. A class that cannot
 * be enumerated -- negated, {@code %pLN}, {@code \w}... -- makes the whole pattern unknown;</li>
 * <li>a lookahead or a lookbehind only rules lines out, so it is read past as if it were not
 * there: the worst it can cost is a token too many.</li>
 * </ul>
 */
public final class FirstTokens {

	public static final Collection<String> ANYTHING = null;
	
	// A pattern whose alternatives multiply is a pattern we have no business describing token by
	// token: past this many, giving up costs one wasted regex attempt per line and nothing else.
	private static final int MAX_TOKENS = 64;

	private final String pattern;
	private int pos;
	private boolean unknown;

	private FirstTokens(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * The first tokens {@code regex} can start a line with, or {@code null} when they cannot be
	 * determined.
	 */
	public static Set<String> from(IRegex regex) {
		return from(regex.getPatternAsString());
	}

	/**
	 * The first tokens {@code pattern} can start a line with, or {@code null} when they cannot be
	 * determined.
	 */
	public static Set<String> from(String pattern) {
		// Commands are matched with find(), not matches(): a pattern that does not anchor its
		// start can match from anywhere in the line, which constrains the first token not at all.
		if (pattern.startsWith("^") == false)
			return null;

		final FirstTokens reader = new FirstTokens(pattern);
		final State state = reader.alternation(State.atLineStart());
		if (reader.unknown || reader.pos != pattern.length())
			return null;

		return state.tokens();
	}

	/**
	 * Whether two declarations of first tokens say the same thing: both {@code null} -- "any
	 * line" -- or the same tokens, whatever their order and whatever kind of collection holds
	 * them. A {@code Set} and a {@code List} of the same tokens are not {@code equals()}, yet they
	 * declare exactly the same: this is what a hand-written list is to be compared with.
	 */
	public static boolean same(Collection<String> a, Collection<String> b) {
		if (a == null || b == null)
			return a == b;

		return new HashSet<>(a).equals(new HashSet<>(b));
	}

	/**
	 * What the analysis knows once some prefix of the pattern has been read: the possible first
	 * tokens so far, held in three parts because what a token still allows depends on how far it
	 * has got.
	 */
	private static final class State {

		// The line may still be in the blanks it opens with, so a letter arriving now would be
		// starting the token rather than continuing one.
		private boolean leading;
		// Tokens under construction: whatever comes next may still add letters to them.
		private final Set<String> open = new HashSet<>();
		// Tokens a non-letter has already closed: nothing can change them any more.
		private final Set<String> done = new HashSet<>();

		private static State atLineStart() {
			final State result = new State();
			result.leading = true;
			return result;
		}

		private State copy() {
			final State result = new State();
			result.leading = this.leading;
			result.open.addAll(this.open);
			result.done.addAll(this.done);
			return result;
		}

		/**
		 * Nothing that follows can change the answer any more: no token is open and the line is
		 * past its leading blanks. Reading on would only risk giving up on a pattern we have
		 * already read enough of.
		 */
		private boolean isSettled() {
			return leading == false && open.isEmpty();
		}

		private void letters(String s) {
			final Set<String> extended = new HashSet<>();
			for (String prefix : open)
				extended.add(prefix + s);

			if (leading)
				extended.add(s);

			open.clear();
			open.addAll(extended);
			leading = false;
		}

		/**
		 * A character that is one of the blanks the line opens with -- {@code [%s]}, {@code \s},
		 * a literal space or U+00A0. It closes whatever token was open, but it does not end the
		 * leading blanks, and it contributes nothing of its own: the token reader skips it there.
		 */
		private void blank() {
			done.addAll(open);
			open.clear();
		}

		/**
		 * A character that is neither a letter nor a blank -- a digit, {@code #}, {@code !},
		 * {@code <}... -- known to be one of {@code chars}. The token reader stops right there:
		 * it ends the token that was open, if any, and if the line was still in its leading
		 * blanks, that character is the token itself. Either way the leading blanks are over, and
		 * the answer is now final.
		 */
		private void separator(Collection<Character> chars) {
			done.addAll(open);
			open.clear();
			if (leading)
				for (char ch : chars)
					done.add(String.valueOf(ch));

			leading = false;
		}

		private void union(State other) {
			this.leading = this.leading || other.leading;
			this.open.addAll(other.open);
			this.done.addAll(other.done);
		}

		private int size() {
			return open.size() + done.size();
		}

		private Set<String> tokens() {
			final Set<String> result = new HashSet<>(done);
			// The pattern is over, so nothing is going to extend these any further.
			result.addAll(open);
			// Still in the leading blanks at the end: the pattern accepts a blank line, whose
			// token is "".
			if (leading)
				result.add("");

			return result;
		}
	}

	private State alternation(State in) {
		State result = sequence(in.copy());
		while (unknown == false && pos < pattern.length() && pattern.charAt(pos) == '|') {
			pos++;
			result.union(sequence(in.copy()));
		}
		return result;
	}

	private State sequence(State state) {
		while (unknown == false && pos < pattern.length()) {
			final char ch = pattern.charAt(pos);
			if (ch == '|' || ch == ')')
				break;

			if (state.isSettled()) {
				// The answer is already fixed: read past the rest without asking anything of it,
				// which is also what keeps a trailing "(.*)" from making a perfectly clear
				// pattern undecidable.
				skipElement();
				continue;
			}

			state = element(state);
			if (state.size() > MAX_TOKENS)
				unknown = true;
		}
		return state;
	}

	private State element(State state) {
		final char ch = pattern.charAt(pos);

		// Zero-width: they say where we are, not what is there.
		if (ch == '^' || ch == '$') {
			pos++;
			return state;
		}

		if (ch == '(') {
			if (isLookaround()) {
				// A lookahead or a lookbehind only ever rules lines out: reading past it as if it
				// were not there may give a token too many, never one too few -- which is the
				// safe way to be wrong here.
				skipGroup();
				if (unknown == false)
					skipQuantifier();

				return state;
			}
			pos++;
			if (pattern.startsWith("?:", pos))
				pos += 2;
			else if (pos < pattern.length() && pattern.charAt(pos) == '?') {
				// A flag, a named group: not worth reading.
				unknown = true;
				return state;
			}

			final State inner = alternation(state.copy());
			if (unknown)
				return state;

			if (pos >= pattern.length() || pattern.charAt(pos) != ')') {
				unknown = true;
				return state;
			}
			pos++;
			return quantified(state, inner);
		}

		if (ch == '[') {
			final int end = endOfCharClass();
			if (end < 0) {
				unknown = true;
				return state;
			}
			final CharClass charClass = CharClass.read(pattern.substring(pos + 1, end));
			if (charClass == null) {
				unknown = true;
				return state;
			}
			pos = end + 1;
			// Each thing the class may be is one way to read on, and the answer is all of them
			// together: a blank, one of the other characters, or one of the letters.
			State after = null;
			if (charClass.blank) {
				after = state.copy();
				after.blank();
			}
			if (charClass.chars.isEmpty() == false) {
				final State asSeparator = state.copy();
				asSeparator.separator(charClass.chars);
				after = union(after, asSeparator);
			}
			for (char letter : charClass.letters) {
				final State asLetter = state.copy();
				asLetter.letters(String.valueOf(letter));
				after = union(after, asLetter);
			}
			return quantified(state, after);
		}

		if (ch == '\\') {
			pos++;
			if (pos >= pattern.length()) {
				unknown = true;
				return state;
			}
			final char escaped = pattern.charAt(pos);
			pos++;
			// \s, \d and the escaped control characters are the ones we are sure keep out of the
			// letters; \w, \p{L}, \S and the rest may well be one, and \b is not a character at all.
			if (isAsciiLetter(escaped) && escaped != 's' && escaped != 'd' && escaped != 'n'
					&& escaped != 't' && escaped != 'r' && escaped != 'f') {
				unknown = true;
				return state;
			}
			final State after = state.copy();
			if (escaped == 'd')
				after.separator(DIGITS);
			else if (isAsciiLetter(escaped))
				after.blank();
			else
				// An escaped punctuation sign: the character itself, and no blank.
				after.separator(Collections.singleton(escaped));

			return quantified(state, after);
		}

		if (isAsciiLetter(ch)) {
			pos++;
			final State after = state.copy();
			after.letters(String.valueOf(toLowerCase(ch)));
			return quantified(state, after);
		}

		// '.' is any character, so it may be a letter; '%' opens a macro whose expansion we would
		// have to read as a sequence rather than as one character.
		if (ch == '.' || ch == '%') {
			unknown = true;
			return state;
		}

		// A quantifier with nothing in front of it means we have lost track of the syntax.
		if (ch == '*' || ch == '+' || ch == '?' || ch == '{') {
			unknown = true;
			return state;
		}

		// Any other literal character: a digit, a punctuation sign, a non-ASCII character. Not an
		// ASCII letter, at any rate, and the pattern is not folding its case.
		pos++;
		final State after = state.copy();
		if (StringUtils.isTrimmable(ch))
			after.blank();
		else
			after.separator(Collections.singleton(ch));

		return quantified(state, after);
	}

	private static State union(State a, State b) {
		if (a == null)
			return b;

		a.union(b);
		return a;
	}

	/**
	 * Whether the group opening at {@code pos} is a lookahead or a lookbehind: {@code (?=},
	 * {@code (?!}, {@code (?<=} or {@code (?<!} -- but not {@code (?<name>}, a named group.
	 */
	private boolean isLookaround() {
		return pattern.startsWith("(?=", pos) || pattern.startsWith("(?!", pos) || pattern.startsWith("(?<=", pos)
				|| pattern.startsWith("(?<!", pos);
	}

	/**
	 * Applies to {@code after} -- the state the element just read leads to -- the quantifier that
	 * element carries, {@code before} being where it started from.
	 */
	private State quantified(State before, State after) {
		if (pos >= pattern.length())
			return after;

		final char ch = pattern.charAt(pos);
		int min = 1;
		int max = 1;
		if (ch == '?') {
			pos++;
			min = 0;
		} else if (ch == '*') {
			pos++;
			min = 0;
			max = Integer.MAX_VALUE;
		} else if (ch == '+') {
			pos++;
			max = Integer.MAX_VALUE;
		} else if (ch == '{') {
			final int end = pattern.indexOf('}', pos);
			if (end < 0) {
				unknown = true;
				return after;
			}
			final String bounds = pattern.substring(pos + 1, end);
			pos = end + 1;
			min = 1;
			max = Integer.MAX_VALUE;
			final int comma = bounds.indexOf(',');
			if (comma < 0)
				max = min = parseCount(bounds);
			else {
				min = parseCount(bounds.substring(0, comma));
				final String upper = bounds.substring(comma + 1);
				max = upper.length() == 0 ? Integer.MAX_VALUE : parseCount(upper);
			}
			if (min < 0 || max < 0) {
				unknown = true;
				return after;
			}
		} else
			return after;

		// A lazy or possessive quantifier matches the same language; only the search order
		// differs, and the language is all we are reading here.
		if (pos < pattern.length() && (pattern.charAt(pos) == '?' || pattern.charAt(pos) == '+'))
			pos++;

		if (max > 1 && before.open.containsAll(after.open) == false) {
			// The element can start or lengthen a token, and repeating it would lengthen it again
			// -- "(ab)+" also matches "abab" -- which is more than one pass has told us.
			unknown = true;
			return after;
		}

		if (min == 0)
			after.union(before);

		return after;
	}

	private static int parseCount(String s) {
		int result = 0;
		if (s.length() == 0)
			return -1;

		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			if (ch < '0' || ch > '9')
				return -1;

			result = result * 10 + (ch - '0');
		}
		return result;
	}

	private static final Collection<Character> DIGITS = new TreeSet<>();
	static {
		for (char ch = '0'; ch <= '9'; ch++)
			DIGITS.add(ch);
	}

	// What %q and %g stand for, as Pattern2 expands them.
	private static final String QUOTES = "'\u2018\u2019";
	private static final String DOUBLE_QUOTES = "\"\u201c\u201d" + Jaws.BLOCK_E1_INVISIBLE_QUOTE;

	/**
	 * The content of a character class, read as the exact set of characters it may match -- since
	 * every one of them that is not a blank may be a token of its own.
	 */
	private static final class CharClass {

		// Whether the class may match one of the blanks a line opens with.
		private boolean blank;
		// The ASCII letters it may match, lower-cased: the patterns fold their case, so "[hx]"
		// matches "H" as well, whose token is "h".
		private final Set<Character> letters = new TreeSet<>();
		// The other characters it may match: never a blank, never an ASCII letter.
		private final Set<Character> chars = new TreeSet<>();

		/**
		 * Reads {@code content}, the text between the brackets, or answers {@code null} when the
		 * class cannot be enumerated: a negated class, {@code %pLN}, {@code \w}, {@code \p{...}},
		 * a nested class, a range too wide to be worth listing...
		 */
		private static CharClass read(String content) {
			// A negated class holds everything it does not name, letters included.
			if (content.startsWith("^"))
				return null;

			final CharClass result = new CharClass();
			int i = 0;
			while (i < content.length()) {
				final char ch = content.charAt(i);
				if (ch == '%') {
					if (content.startsWith("%s", i))
						result.blank = true;
					else if (content.startsWith("%q", i))
						result.addAll(QUOTES);
					else if (content.startsWith("%g", i))
						result.addAll(DOUBLE_QUOTES);
					else
						// %pLN: the Unicode letters and digits.
						return null;

					i += 2;
					continue;
				}
				// A nested class or an intersection: not worth following.
				if (ch == '[' || content.startsWith("&&", i))
					return null;

				final char first;
				if (ch == '\\') {
					if (i + 1 >= content.length())
						return null;

					final char escaped = content.charAt(i + 1);
					i += 2;
					if (escaped == 's' || escaped == 'n' || escaped == 't' || escaped == 'r' || escaped == 'f'
							|| escaped == '0') {
						result.blank = true;
						continue;
					}
					if (escaped == 'd') {
						result.chars.addAll(DIGITS);
						continue;
					}
					// \w, \p{L}, \S...: letters, or anything.
					if (isAsciiLetter(escaped))
						return null;

					first = escaped;
				} else {
					first = ch;
					i++;
				}

				// A range, unless the '-' is the last character of the class, where it is a
				// literal one.
				if (i + 1 < content.length() && content.charAt(i) == '-') {
					char last = content.charAt(i + 1);
					i += 2;
					if (last == '\\') {
						if (i >= content.length())
							return null;

						last = content.charAt(i);
						i++;
						if (isAsciiLetter(last))
							return null;
					}
					if (last < first || last - first > MAX_TOKENS)
						return null;

					for (char c = first; c <= last; c++)
						result.add(c);

					continue;
				}
				result.add(first);
			}
			// A class matching nothing at all is not something to reason about.
			if (result.blank == false && result.chars.isEmpty() && result.letters.isEmpty())
				return null;

			return result;
		}

		private void addAll(String s) {
			for (int i = 0; i < s.length(); i++)
				chars.add(s.charAt(i));
		}

		private void add(char ch) {
			if (isAsciiLetter(ch))
				letters.add(toLowerCase(ch));
			else if (StringUtils.isTrimmable(ch))
				blank = true;
			else
				chars.add(ch);
		}
	}

	private int endOfCharClass() {
		int i = pos + 1;
		if (i < pattern.length() && pattern.charAt(i) == '^')
			i++;

		// A ']' in first position is a literal one.
		if (i < pattern.length() && pattern.charAt(i) == ']')
			i++;

		while (i < pattern.length()) {
			final char ch = pattern.charAt(i);
			if (ch == '\\')
				i += 2;
			else if (ch == ']')
				return i;
			else
				i++;
		}
		return -1;
	}

	private void skipElement() {
		final char ch = pattern.charAt(pos);
		if (ch == '(') {
			skipGroup();
			if (unknown)
				return;
		} else if (ch == '[') {
			final int end = endOfCharClass();
			if (end < 0) {
				unknown = true;
				return;
			}
			pos = end + 1;
		} else if (ch == '\\')
			pos += 2;
		else
			pos++;

		if (pos > pattern.length()) {
			unknown = true;
			return;
		}
		skipQuantifier();
	}

	private void skipGroup() {
		int depth = 0;
		while (pos < pattern.length()) {
			final char ch = pattern.charAt(pos);
			if (ch == '\\') {
				pos += 2;
				continue;
			}
			if (ch == '[') {
				final int end = endOfCharClass();
				if (end < 0) {
					unknown = true;
					return;
				}
				pos = end + 1;
				continue;
			}
			pos++;
			if (ch == '(')
				depth++;
			else if (ch == ')') {
				depth--;
				if (depth == 0)
					return;
			}
		}
		unknown = true;
	}

	private void skipQuantifier() {
		if (pos >= pattern.length())
			return;

		final char ch = pattern.charAt(pos);
		if (ch == '?' || ch == '*' || ch == '+')
			pos++;
		else if (ch == '{') {
			final int end = pattern.indexOf('}', pos);
			if (end < 0) {
				unknown = true;
				return;
			}
			pos = end + 1;
		} else
			return;

		if (pos < pattern.length() && (pattern.charAt(pos) == '?' || pattern.charAt(pos) == '+'))
			pos++;
	}

	private static boolean isAsciiLetter(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}

	private static char toLowerCase(char ch) {
		return (char) (ch | 0x20);
	}

}
