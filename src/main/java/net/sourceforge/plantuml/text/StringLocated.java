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
import net.sourceforge.plantuml.jaws.JawsFlags;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.utils.LineLocation;

final public class StringLocated {
	// ::remove folder when __HAXE__

	private final String s;
	private final LineLocation location;
	private final String preprocessorError;

	private StringLocated trimmed;
	private long fox = -1;
	private TLineType type;

//	public StringLocated jawsPatchNewlines() {
//	return new StringLocated(s.replace("\\n", "%n()"), location, preprocessorError);
//}
//
//public BlocLines expandsJaws() {
//	BlocLines result = BlocLines.create();
//	for (String part : s.split("" + Jaws.BLOCK_E1_NEWLINE))
//		result = result.add(new StringLocated(part, location, preprocessorError));
//	return result;
//}

	public List<StringLocated> expandsJawsForPreprocessor() {
		if (JawsFlags.PARSE_NEW_MULTILINE_TRIPLE_MARKS) {
			final int x = searchMultilineTripleSeparators();
			if (x == -1)
				return Arrays.asList(this);
			final String s1 = s.substring(0, x);
			final String s2 = s.substring(x + 3);
			return Arrays.asList(new StringLocated(s1, location, preprocessorError).jawsHideBackslash(),
					new StringLocated(s2, location, preprocessorError).jawsHideBackslash());
		}
		return Arrays.asList(this);
	}

	private static final Pattern TRIPLE_PATTERN = Pattern.compile("!!!|'''|\"\"\"");

	private int searchMultilineTripleSeparators() {
		final Matcher matcher = TRIPLE_PATTERN.matcher(s);

		if (matcher.find())
			return matcher.start();

		return -1;
	}

	public List<StringLocated> expandsNewline() {
		final List<StringLocated> copy = new ArrayList<>();
		for (String s : Arrays.asList(s.split("" + Jaws.BLOCK_E1_NEWLINE)))
			copy.add(new StringLocated(s, location, preprocessorError));
		return copy;
	}

	public List<StringLocated> expandsBreaklineButEmbedded() {
		final List<StringLocated> copy = new ArrayList<>();
		int level = 0;
		StringBuilder pending = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);

			if (ch == '{' && i + 1 < s.length() && s.charAt(i + 1) == '{')
				level++;
			else if (ch == '}' && i + 1 < s.length() && s.charAt(i + 1) == '}')
				level--;

			if (level > 0) {
				pending.append(ch);
			} else if (ch == Jaws.BLOCK_E1_BREAKLINE) {
				copy.add(new StringLocated(pending.toString(), location, preprocessorError));
				pending.setLength(0);
			} else {
				pending.append(ch);
			}
		}
		copy.add(new StringLocated(pending.toString(), location, preprocessorError));

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
		return new StringLocated(s.replace('\\', Jaws.BLOCK_E1_REAL_BACKSLASH), location, preprocessorError);
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
		return s;
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
		if (trimmed == null) {
			this.trimmed = new StringLocated(StringUtils.trin(this.getString()), location, preprocessorError);
			trimmed.fox = this.fox;
			trimmed.trimmed = trimmed;
		}
		return trimmed;
	}

	@JawsStrange
	public StringLocated removeInnerComment() {
		final String string = s.toString();
		final String trim = string.replace('\t', ' ').trim();
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

}
