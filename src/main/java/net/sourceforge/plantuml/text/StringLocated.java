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
package net.sourceforge.plantuml.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.utils.LineLocation;

final public class StringLocated {

	private final String s;
	private final LineLocation location;
	private final String preprocessorError;

	private StringLocated trimmed;
	private long fox = -1;
	private TLineType type;

	public int findMultilineTripleSeparator() {
		final Matcher matcher = TRIPLE_PATTERN.matcher(s);
		if (matcher.find())
			return matcher.start();
		return -1;
	}

	public StringLocated[] splitAtTripleSeparator(int x) {
		final String s1 = s.substring(0, x);
		final String s2 = s.substring(x + 3);
		return new StringLocated[] { new StringLocated(s1, location, preprocessorError).jawsHideBackslash(),
				new StringLocated(s2, location, preprocessorError).jawsHideBackslash() };
	}

	private static final Pattern TRIPLE_PATTERN = Pattern.compile("!!!|'''|\"\"\"");

	public List<StringLocated> expandsNewline() {
		final List<StringLocated> copy = new ArrayList<>();
		for (String s : Arrays.asList(s.split("" + Jaws.BLOCK_E1_NEWLINE)))
			copy.add(new StringLocated(s, location, preprocessorError));
		return copy;
	}

//	public List<StringLocated> expandsJaws51() {
//		final List<StringLocated> copy = new ArrayList<>();
//		for (String s : expandsJaws31())
//			copy.add(new StringLocated(s, location, preprocessorError));
//		return copy;
//	}

//	public List<StringLocated> expandsJaws51() {
//		final List<StringLocated> copy = new ArrayList<>();
//		for (String s : expandsJaws31())
//			copy.add(new StringLocated(s, location, preprocessorError));
//		return copy;
//	}
//

//	public List<String> expandsJaws31() {
//		final List<String> result = new ArrayList<>();
//		boolean inGuillement = false;
//		StringBuilder pending = new StringBuilder();
//		for (char ch : s.toCharArray()) {
//			if (ch == '"')
//				inGuillement = !inGuillement;
//			if (inGuillement) {
//				pending.append(ch);
//			} else if (ch == Jaws.BLOCK_E1_NEWLINE) {
//				result.add(pending.toString());
//				pending.setLength(0);
//			} else {
//				pending.append(ch);
//			}
//		}
//		result.add(pending.toString());
//
//		return result;
//	}
//
//	public static String expandsJaws32(String s) {
//		boolean inGuillement = false;
//		final StringBuilder pending = new StringBuilder();
//		for (char ch : s.toCharArray()) {
//			if (ch == '"')
//				inGuillement = !inGuillement;
//			if (inGuillement)
//				pending.append(ch);
//			else if (ch == Jaws.BLOCK_E1_NEWLINE)
//				pending.append('\n');
//			else
//				pending.append(ch);
//
//		}
//		return pending.toString();
//	}

	public StringLocated jawsHideBackslash() {
		return new StringLocated(StringUtils.replaceChar(s, '\\', Jaws.BLOCK_E1_REAL_BACKSLASH), location, preprocessorError);
	}

	public static List<String> expandsNewline(String s) {
		return Arrays.asList(s.split("" + Jaws.BLOCK_E1_NEWLINE));
	}

	public StringLocated(String s, LineLocation location) {
		this(s, location, null);
	}

	public StringLocated(String s, LineLocation location, String preprocessorError) {
		this.s = Objects.requireNonNull(s);
		this.location = location;
		this.preprocessorError = preprocessorError;
	}

	@Override
	public String toString() {
		if (s.length() == 0)
			return "<<<EMPTY STRING>>>";
		return "(SL) " + s;
	}

	public StringLocated append(String endOfLine) {
		return new StringLocated(s + endOfLine, location, preprocessorError);
	}

	public StringLocated append(char endOfLine) {
		return new StringLocated(s + endOfLine, location, preprocessorError);
	}

	public StringLocated mergeEndBackslash(StringLocated next) {
		if (StringUtils.endsWithBackslash(s) == false)
			throw new IllegalArgumentException();

		return new StringLocated(s.substring(0, s.length() - 1) + next.s, location, preprocessorError);
	}

	public StringLocated withErrorPreprocessor(String preprocessorError) {
		return new StringLocated(s, location, preprocessorError);
	}

	public StringLocated substring(int start, int end) {
		return new StringLocated(this.getString().substring(start, end), this.getLocation(),
				this.getPreprocessorError());
	}

	public StringLocated substring(int start) {
		return new StringLocated(this.getString().substring(start), this.getLocation(), this.getPreprocessorError());
	}

	public StringLocated getTrimmed() {
		if (s.length() == 0)
			return this;

		if (trimmed == null) {
			final String tmp = StringUtils.trin(s);
			// tmp==s when no trim is needed
			if (tmp == s) {
				this.trimmed = this;
			} else {
				this.trimmed = new StringLocated(tmp, location, preprocessorError);
				trimmed.fox = this.fox;
				trimmed.trimmed = trimmed;
			}
		}
		return trimmed;
	}

	@JawsStrange
	public StringLocated removeInnerComment() {
		final String string = s.toString();
		final String trim = StringUtils.replaceChar(string, '\t', ' ').trim();
		if (trim.startsWith("/'")) {
			final int idx = string.indexOf("'/");
			if (idx != -1)
				return new StringLocated(removeSpecialInnerComment(s.substring(idx + 2, s.length())), location,
						preprocessorError);

		}
		if (trim.endsWith("'/")) {
			final int idx = string.lastIndexOf("/'");
			if (idx != -1)
				return new StringLocated(removeSpecialInnerComment(s.substring(0, idx)), location, preprocessorError);

		}
		if (trim.contains("/'''") && trim.contains("'''/"))
			return new StringLocated(removeSpecialInnerComment(s), location, preprocessorError);

		return this;
	}

	private String removeSpecialInnerComment(String s) {
		if (s.contains("/'''") && s.contains("'''/"))
			return s.replaceAll("/'''[-\\w]*'''/", "");

		return s;
	}

	public String getString() {
		return s;
	}

	public LineLocation getLocation() {
		return location;
	}

	public String getPreprocessorError() {
		return preprocessorError;
	}

	public long getFoxSignature() {
		if (fox == -1)
			fox = FoxSignature.getFoxSignatureFromRealString(getString());

		return fox;
	}

	public TLineType getType() {
		if (type == null)
			type = TLineType.getFromLineInternal(this);

		return type;
	}

	public int length() {
		return s.length();
	}

	public char charAt(int i) {
		return s.charAt(i);
	}

	private static final long EXCLAMATION_MARK = FoxSignature.getFoxSignatureFromRealString("!");

	public boolean containsExclamationMark() {
		return (getFoxSignature() & EXCLAMATION_MARK) != 0L;
	}

	private String firstToken = null;

	/**
	 * The token the command index files this line under: {@code PSystemCommandFactory} only tries,
	 * on a line, the commands that declared its first token (see
	 * {@code Command#mandatoryFirstTokens}), plus those that declared nothing.
	 *
	 * <p>
	 * Once the blanks the line opens with are skipped -- the very ones {@link #getTrimmed()}
	 * removes, so that trimming a line never changes its token -- the token is:
	 * <ul>
	 * <li>for a line starting with an ASCII letter, the whole run of ASCII letters it starts with,
	 * lower-cased: {@code "Title foo"} gives {@code "title"}, {@code "endif"} gives
	 * {@code "endif"}, {@code "note2"} gives {@code "note"};</li>
	 * <li>for a line starting with any other character, that character alone, as it is:
	 * {@code "}"} gives {@code "}"}, {@code "-> B"} gives {@code "-"}, {@code "@0"} gives
	 * {@code "@"}, {@code "12:00"} gives {@code "1"} -- every digit is a token of its own --
	 * and a line starting with an accented letter has that letter for token;</li>
	 * <li>for a blank line, {@code ""}.</li>
	 * </ul>
	 *
	 * <p>
	 * Why a single character rather than {@code ""} for every line that does not start with a
	 * letter: those lines -- closing braces, arrows, dividers, activities, colors... -- used to
	 * share one bucket, holding every command that could start that way. Measured on the Vega
	 * corpus, they were 28% of the lines but 54% of the regex attempts. Their first character is
	 * almost always enough to tell those commands apart.
	 *
	 * <p>
	 * Letters are folded because the command patterns are compiled with {@code CASE_INSENSITIVE};
	 * other characters are not, because that flag, without {@code UNICODE_CASE}, folds nothing
	 * else. {@code net.sourceforge.plantuml.regex.FirstTokens} reads the tokens a pattern accepts
	 * with this same definition: the two must change together.
	 */
	public String getFirstToken() {
		if (firstToken == null) {
			int start = 0;
			while (start < s.length() && StringUtils.isTrimmable(s.charAt(start)))
				start++;

			if (start == s.length()) {
				firstToken = "";
			} else if (isLetter(s.charAt(start))) {
				final StringBuilder sb = new StringBuilder();
				int end = start;
				while (end < s.length() && isLetter(s.charAt(end))) {
					sb.append(toLowerCase(s.charAt(end)));
					end++;
				}
				firstToken = sb.toString();
			} else {
				firstToken = String.valueOf(s.charAt(start));
			}
		}
		return firstToken;
	}

	/**
	 * Only ever called on a letter, as the loop above guarantees and the assert repeats, which is
	 * what makes the fold a single bit: 'A'-'Z' become 'a'-'z' by setting bit 5, and 'a'-'z'
	 * already have it. That is also exactly the folding Pattern.CASE_INSENSITIVE does without
	 * UNICODE_CASE -- the flag the command patterns are compiled with -- so a token finds an
	 * indexed command here whenever the pattern itself would have matched.
	 */
	private static char toLowerCase(char ch) {
		if (TeaVM.a())
			assert (isLetter(ch));
		return (char) (ch | 0x20);
	}

	private static boolean isLetter(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}

}
