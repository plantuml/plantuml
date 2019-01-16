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
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.preproc.DefinesGet;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.utils.StartUtils;

public class PreprocessorDefine3Learner implements ReadFilter {

	private static final String END_DEFINE_LONG = "!enddefinelong";
	private static final String ID = "[A-Za-z_][A-Za-z_0-9]*";
	private static final String ID_ARG = "\\s*[A-Za-z_][A-Za-z_0-9]*\\s*(?:=\\s*(?:\"[^\"]*\"|'[^']*')\\s*)?";
	private static final String ARG = "(?:\\(" + ID_ARG + "(?:," + ID_ARG + ")*?\\))?";
	private static final Pattern2 defineShortPattern = MyPattern.cmpile("^[%s]*!define[%s]+(" + ID + ARG + ")"
			+ "(?:[%s]+(.*))?$");
	private static final Pattern2 filenamePattern = MyPattern.cmpile("^[%s]*!filename[%s]+(.+)$");
	private static final Pattern2 undefPattern = MyPattern.cmpile("^[%s]*!undef[%s]+(" + ID + ")$");
	private static final Pattern2 definelongPattern = MyPattern.cmpile("^[%s]*!definelong[%s]+(" + ID + ARG + ")");
	private static final Pattern2 enddefinelongPattern = MyPattern.cmpile("^[%s]*" + END_DEFINE_LONG + "[%s]*$");

	private final DefinesGet defines;
	private final AParentFolder currentDir;

	public PreprocessorDefine3Learner(DefinesGet defines, AParentFolder currentDir) {
		this.defines = defines;
		this.currentDir = currentDir;
	}

	public static boolean isLearningLine(CharSequence2 s) {
		Matcher2 m = defineShortPattern.matcher(s);
		if (m.find()) {
			return true;
		}
		m = definelongPattern.matcher(s);
		if (m.find()) {
			return true;
		}
		m = undefPattern.matcher(s);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public ReadLine applyFilter(final ReadLine source) {
		return new ReadLine() {

			public void close() throws IOException {
				source.close();
			}

			public CharSequence2 readLine() throws IOException {
				while (true) {
					final CharSequence2 s = source.readLine();
					if (s == null || s.getPreprocessorError() != null) {
						return s;
					}
					if (StartUtils.isArobaseStartDiagram(s)) {
						defines.restoreState();
						return s;
					}

					Matcher2 m = filenamePattern.matcher(s);
					if (m.find()) {
						manageFilename(m);
						continue;
					}
					m = defineShortPattern.matcher(s);
					if (m.find()) {
						manageDefineShort(source, m, s.toString().trim().endsWith("()"));
						continue;
					}
					m = definelongPattern.matcher(s);
					if (m.find()) {
						manageDefineLong(source, m, s.toString().trim().endsWith("()"));
						continue;
					}

					m = undefPattern.matcher(s);
					if (m.find()) {
						manageUndef(m);
						continue;
					}
					return s;
				}
			}
		};
	}

	private void manageUndef(Matcher2 m) throws IOException {
		defines.get().undefine(m.group(1));
	}

	private void manageDefineLong(ReadLine source, Matcher2 m, boolean emptyParentheses) throws IOException {
		final String group1 = m.group(1);
		final List<String> def = new ArrayList<String>();
		while (true) {
			final CharSequence2 read = source.readLine();
			if (read == null) {
				return;
			}
			if (enddefinelongPattern.matcher(read).find()) {
				defines.get().define(group1, def, emptyParentheses, currentDir);
				return;
			}
			def.add(read.toString2());
		}
	}

	private void manageFilename(Matcher2 m) {
		final String group1 = m.group(1);
		this.defines.get().overrideFilename(group1);
	}

	private void manageDefineShort(ReadLine source, Matcher2 m, boolean emptyParentheses) throws IOException {
		final String group1 = m.group(1);
		final String group2 = m.group(2);
		if (group2 == null) {
			defines.get().define(group1, null, emptyParentheses, null);
		} else {
			final List<String> strings = defines.get().applyDefines(group2);
			if (strings.size() > 1) {
				defines.get().define(group1, strings, emptyParentheses, null);
			} else {
				final StringBuilder value = new StringBuilder(strings.get(0));
				while (StringUtils.endsWithBackslash(value.toString())) {
					value.setLength(value.length() - 1);
					final CharSequence2 read = source.readLine();
					value.append(read.toString2());
				}
				final List<String> li = new ArrayList<String>();
				li.add(value.toString());
				defines.get().define(group1, li, emptyParentheses, null);
			}
		}
	}

}