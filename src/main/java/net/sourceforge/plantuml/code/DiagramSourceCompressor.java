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
package net.sourceforge.plantuml.code;

import java.io.IOException;
import java.io.StringReader;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.text.StringLocated;

public class DiagramSourceCompressor implements StringCompressor {

	private static final String START_UML = "@startuml";
	private static final String END_UML = "@enduml";

	public String compress(final String data) throws IOException {
		final ReadLine r = new UncommentReadLine(ReadLineReader.create(new StringReader(data), "COMPRESS"));
		final StringBuilder sb = new StringBuilder();
		final StringBuilder full = new StringBuilder();
		StringLocated s = null;
		boolean startDone = false;
		while ((s = r.readLine()) != null) {
			append(full, s);
			if (s.getString().startsWith("@startuml")) {
				startDone = true;
			} else if (s.getString().startsWith("@enduml")) {
				return sb.toString();
			} else if (startDone) {
				append(sb, s);
			}
		}
		if (startDone == false) {
			return compressOld(full.toString());
		}
		return sb.toString();
	}

	private void append(final StringBuilder sb, StringLocated s) {
		if (sb.length() > 0) {
			sb.append('\n');
		}
		sb.append(s.getString());
	}

	// Package-private, as the other cleaning methods below, so that they can be unit tested
	String compressOld(String s) throws IOException {
		return clean(stripStartEnd(s, true));
	}

	public String decompress(String s) {
		String result = clean(s);
		if (result.startsWith("@start")) {
			return result;
		}
		result = "@startuml\n" + result;
		if (result.endsWith("\n") == false) {
			result += "\n";
		}
		result += "@enduml";
		return result;
	}

	String clean(String s) {
		// s = s.replace("\0", "");
		s = StringUtils.trin(s);
		s = clean1(s);
		s = removeFromToken(s, END_UML);
		s = removeFromToken(s, START_UML);
		s = StringUtils.trin(s);
		return s;
	}

	/**
	 * Same as {@code s.replaceAll(token + "[^\\n\\r]*", "")}, with the token taken
	 * literally: removes every occurrence of the token, and the rest of its
	 * line (up to, and not including, the next {@code \n} or {@code \r}).
	 * <p>
	 * Plain loop, like {@link #stripStartEnd}, so that the cost does not depend
	 * on the regex engine of the TeaVM build when the source is big (issue
	 * #2934).
	 */
	static String removeFromToken(String s, String token) {
		int pos = s.indexOf(token);
		if (pos == -1)
			return s;

		final int len = s.length();
		final StringBuilder sb = new StringBuilder(len);
		int from = 0;
		while (pos != -1) {
			sb.append(s, from, pos);
			from = pos + token.length();
			while (from < len && s.charAt(from) != '\n' && s.charAt(from) != '\r')
				from++;
			pos = s.indexOf(token, from);
		}
		sb.append(s, from, len);
		return sb.toString();
	}

	private String clean1(String s) {
		return stripStartEnd(s, false);
	}

	/**
	 * What {@code [%s]} stands for in a PlantUML regex: {@code \s} and the
	 * non-breaking space. This is deliberately not {@link StringUtils#isTrimmable},
	 * which is wider (every char up to the space, control chars included).
	 */
	static boolean isBlank(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\u000B' || c == '\f' || c == '\r' || c == '\u00A0';
	}

	/**
	 * The line terminators, besides {@code \n} and {@code \r} which are blanks, that
	 * a regex {@code $} accepts just before the end of the input.
	 */
	private static boolean isOtherLineTerminator(char c) {
		return c == '\u0085' || c == '\u2028' || c == '\u2029';
	}

	/**
	 * Returns what group 2 captures in the regex
	 * <pre>(?s)^[%s]*(@startuml[^\n\r]*)?[%s]*(.*?)[%s]*(@enduml)?[%s]*$</pre>
	 * that is, the text without its leading blanks, without a first
	 * {@code @startuml} line, without its trailing blanks and without a final
	 * {@code @enduml}.
	 * <p>
	 * It is computed with plain loops because that regex, applied to a whole
	 * diagram source, overflows the JavaScript call stack in the TeaVM build as
	 * soon as the source is a few KB long (issue #2934): the lazy {@code (.*?)}
	 * costs one stack frame per character there.
	 *
	 * @param dollarBeforeFinalTerminator {@code true} for {@code Matcher.find()},
	 *                                    where {@code $} also matches before a
	 *                                    final U+0085, U+2028 or U+2029;
	 *                                    {@code false} for {@code Matcher.matches()},
	 *                                    where the match must reach the very end
	 */
	static String stripStartEnd(String s, boolean dollarBeforeFinalTerminator) {
		final int len = s.length();
		int start = 0;
		while (start < len && isBlank(s.charAt(start)))
			start++;

		if (s.startsWith(START_UML, start)) {
			start += START_UML.length();
			while (start < len && s.charAt(start) != '\n' && s.charAt(start) != '\r')
				start++;
			while (start < len && isBlank(s.charAt(start)))
				start++;
		}

		int end = len;
		if (dollarBeforeFinalTerminator && end > start && isOtherLineTerminator(s.charAt(end - 1)))
			end--;
		while (end > start && isBlank(s.charAt(end - 1)))
			end--;

		final int endUml = end - END_UML.length();
		if (endUml >= start && s.startsWith(END_UML, endUml)) {
			end = endUml;
			while (end > start && isBlank(s.charAt(end - 1)))
				end--;
		}
		return s.substring(start, end);
	}

}