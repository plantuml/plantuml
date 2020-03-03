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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Moment;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;

public class ComplementBeforeOrAfterOrAtTaskStartOrEnd implements ComplementPattern {

	public IRegex toRegex(String suffix) {
		return new RegexLeaf("COMPLEMENT" + suffix,
				"(?:at|with|after|(\\d+)[%s]+days?[%s]+(before|after))[%s]+\\[([^\\[\\]]+?)\\].?s[%s]+(start|end)");
	}

	public Failable<Complement> getComplement(GanttDiagram system, RegexResult arg, String suffix) {
		final String code = arg.get("COMPLEMENT" + suffix, 2);
		final String position = arg.get("COMPLEMENT" + suffix, 3);
		final Moment task = system.getExistingMoment(code);
		if (task == null) {
			return Failable.<Complement> error("No such task " + code);
		}
		final String days = arg.get("COMPLEMENT" + suffix, 0);
		TaskInstant result = new TaskInstant(task, TaskAttribute.fromString(position));
		if (days != null) {
			int delta = Integer.parseInt(days);
			if ("before".equalsIgnoreCase(arg.get("COMPLEMENT" + suffix, 1))) {
				delta = -delta;
			}
			result = result.withDelta(delta);
		}
		return Failable.<Complement> ok(result);
	}
}
