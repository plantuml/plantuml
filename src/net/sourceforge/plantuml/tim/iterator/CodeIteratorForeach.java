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
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterForeach;
import net.sourceforge.plantuml.tim.ExecutionContextForeach;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.TVariableScope;
import net.sourceforge.plantuml.tim.expression.TValue;

public class CodeIteratorForeach extends AbstractCodeIterator {

	private final TContext context;
	private final TMemory memory;
	private final List<StringLocated> logs;

	public CodeIteratorForeach(CodeIterator source, TContext context, TMemory memory, List<StringLocated> logs) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.logs = logs;
	}

	public StringLocated peek() throws EaterException, EaterExceptionLocated {
		int level = 0;
		while (true) {
			final StringLocated result = source.peek();
			if (result == null) {
				return null;
			}

			final ExecutionContextForeach foreach = memory.peekForeach();
			if (foreach != null && foreach.isSkipMe()) {
				if (result.getType() == TLineType.FOREACH) {
					level++;
				} else if (result.getType() == TLineType.ENDFOREACH) {
					level--;
					if (level == -1) {
						memory.pollForeach();
						level = 0;
					}
				}
				next();
				continue;
			}

			if (result.getType() == TLineType.FOREACH) {
				logs.add(result);
				executeForeach(memory, result.getTrimmed());
				next();
				continue;
			} else if (result.getType() == TLineType.ENDFOREACH) {
				logs.add(result);
				if (foreach == null) {
					throw EaterException.located("No foreach related to this endforeach", result);
				}
				foreach.inc();
				if (foreach.isSkipMe()) {
					memory.pollForeach();
				} else {
					setLoopVariable(memory, foreach, result);
					source.jumpToCodePosition(foreach.getStartForeach());
				}
				next();
				continue;
			}

			return result;
		}
	}

	private void executeForeach(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterForeach condition = new EaterForeach(s);
		condition.analyze(context, memory);
		final ExecutionContextForeach foreach = ExecutionContextForeach.fromValue(condition.getVarname(),
				condition.getJsonArray(), source.getCodePosition());
		if (condition.isSkip()) {
			foreach.skipMeNow();
		} else {
			setLoopVariable(memory, foreach, s);
		}
		memory.addForeach(foreach);
	}

	private void setLoopVariable(TMemory memory, ExecutionContextForeach foreach, StringLocated position)
			throws EaterException {
		final JsonValue first = foreach.getJsonArray().get(foreach.currentIndex());
		memory.putVariable(foreach.getVarname(), TValue.fromJson(first), TVariableScope.GLOBAL);
	}

}
