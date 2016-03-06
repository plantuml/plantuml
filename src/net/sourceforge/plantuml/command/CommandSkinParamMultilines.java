/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5727 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class CommandSkinParamMultilines extends CommandMultilinesBracket<UmlDiagram> {

	static class Context {
		private List<String> strings = new ArrayList<String>();

		public void push(String s) {
			strings.add(s);
		}

		public void pop() {
			strings.remove(strings.size() - 1);
		}

		public String getFullParam() {
			final StringBuilder sb = new StringBuilder();
			for (String s : strings) {
				sb.append(s);
			}
			return sb.toString();
		}
	}

	private final static Pattern p1 = MyPattern
			.cmpile("^([\\w.]*(?:\\<\\<.*\\>\\>)?[\\w.]*)[%s]+(?:(\\{)|(.*))$|^\\}?$");

	public CommandSkinParamMultilines() {
		super("(?i)^skinparam[%s]*(?:[%s]+([\\w.]*(?:\\<\\<.*\\>\\>)?[\\w.]*))?[%s]*\\{$");
	}

	@Override
	protected boolean isLineConsistent(String line, int level) {
		line = StringUtils.trin(line);
		if (hasStartingQuote(line)) {
			return true;
		}
		return p1.matcher(line).matches();
	}

	private boolean hasStartingQuote(CharSequence line) {
		// return MyPattern.mtches(line, "[%s]*[%q].*");
		return MyPattern.mtches(line, CommandMultilinesComment.COMMENT_SINGLE_LINE);
	}

	public CommandExecutionResult execute(UmlDiagram diagram, BlocLines lines) {
		final Context context = new Context();
		final Matcher mStart = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		if (mStart.find() == false) {
			throw new IllegalStateException();
		}
		if (mStart.group(1) != null) {
			context.push(mStart.group(1));
		}

		lines = lines.subExtract(1, 1);
		lines = lines.removeComments();
		lines = lines.trim(true);

		for (CharSequence s : lines) {
			assert s.length() > 0;
//			if (hasStartingQuote(s)) {
//				continue;
//			}
			if (s.toString().equals("}")) {
				context.pop();
				continue;
			}
			final Matcher m = p1.matcher(s);
			if (m.find() == false) {
				throw new IllegalStateException();
			}
			if (m.group(2) != null) {
				context.push(m.group(1));
			} else if (m.group(3) != null) {
				final String key = context.getFullParam() + m.group(1);
				diagram.setParam(key, m.group(3));
			} else {
				throw new IllegalStateException("." + s.toString() + ".");
			}
		}

		return CommandExecutionResult.ok();
	}

}
