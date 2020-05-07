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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class SkinLoader {

	public final static Pattern2 p1 = MyPattern
			.cmpile("^([\\w.]*(?:\\<\\<.*\\>\\>)?[\\w.]*)[%s]+(?:(\\{)|(.*))$|^\\}?$");

	final private List<String> context = new ArrayList<String>();
	final private UmlDiagram diagram;

	public SkinLoader(UmlDiagram diagram) {
		this.diagram = diagram;
	}

	private void push(String s) {
		context.add(s);
	}

	private void pop() {
		context.remove(context.size() - 1);
	}

	private String getFullParam() {
		final StringBuilder sb = new StringBuilder();
		for (String s : context) {
			sb.append(s);
		}
		return sb.toString();
	}

	public CommandExecutionResult execute(BlocLines lines, final String group1) {

		if (group1 != null) {
			this.push(group1);
		}

		lines = lines.subExtract(1, 1);
		lines = lines.trim().removeEmptyLines();

		for (StringLocated s : lines) {
			assert s.getString().length() > 0;

			if (s.getString().equals("}")) {
				this.pop();
				continue;
			}
			final Matcher2 m = p1.matcher(s.getString());
			if (m.find() == false) {
				throw new IllegalStateException();
			}
			if (m.group(2) != null) {
				this.push(m.group(1));
			} else if (m.group(3) != null) {
				final String key = this.getFullParam() + m.group(1);
				diagram.setParam(key, m.group(3));
			} else {
				throw new IllegalStateException("." + s.getString() + ".");
			}
		}

		return CommandExecutionResult.ok();
	}

}
