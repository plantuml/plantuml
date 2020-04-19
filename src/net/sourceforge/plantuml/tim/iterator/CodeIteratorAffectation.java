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
 */
package net.sourceforge.plantuml.tim.iterator;

import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.json.ParseException;
import net.sourceforge.plantuml.tim.EaterAffectation;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;

public class CodeIteratorAffectation extends AbstractCodeIterator {

	private final TContext context;
	private final TMemory memory;
	private final List<StringLocated> logs;

	public CodeIteratorAffectation(CodeIterator source, TContext context, TMemory memory, List<StringLocated> log) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.logs = log;
	}

	public StringLocated peek() throws EaterException, EaterExceptionLocated {
		while (true) {
			final StringLocated result = source.peek();
			if (result == null) {
				return null;
			}
			if (result.getType() == TLineType.AFFECTATION) {
				logs.add(result);
				doAffectation(result);
				next();
				continue;
			}
			return result;
		}
	}

	private void doAffectation(StringLocated result) throws EaterException, EaterExceptionLocated {
		int lastLocation = -1;
		for (int i = 0; i < 100; i++)
			try {
				this.executeAffectation(context, memory, result);
				return;
			} catch (ParseException e) {
				if (e.getColumn() <= lastLocation) {
					throw EaterException.located("Error in JSON format", result);
				}
				lastLocation = e.getColumn();
				next();
				final StringLocated forward = source.peek();
				result = result.append(forward.getString());
			}
	}

	private void executeAffectation(TContext context, TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		new EaterAffectation(s).analyze(context, memory);
	}

}
