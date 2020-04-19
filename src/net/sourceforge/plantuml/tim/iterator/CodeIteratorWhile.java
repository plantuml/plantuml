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
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterWhile;
import net.sourceforge.plantuml.tim.ExecutionContextWhile;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.TokenStack;

public class CodeIteratorWhile extends AbstractCodeIterator {

	private final TContext context;
	private final TMemory memory;
	private final List<StringLocated> logs;

	public CodeIteratorWhile(CodeIterator source, TContext context, TMemory memory, List<StringLocated> logs) {
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

			final ExecutionContextWhile currentWhile = memory.peekWhile();
			if (currentWhile != null && currentWhile.isSkipMe()) {
				if (result.getType() == TLineType.WHILE) {
					level++;
				} else if (result.getType() == TLineType.ENDWHILE) {
					level--;
					if (level == -1) {
						memory.pollWhile();
						level = 0;
					}
				}
				next();
				continue;
			}

			if (result.getType() == TLineType.WHILE) {
				logs.add(result);
				executeWhile(memory, result.getTrimmed());
				next();
				continue;
			} else if (result.getType() == TLineType.ENDWHILE) {
				logs.add(result);
				if (currentWhile == null) {
					throw EaterException.located("No while related to this endwhile", result);
				}
				final TValue value = currentWhile.conditionValue(result.getLocation(), context, memory);
				if (value.toBoolean()) {
					source.jumpToCodePosition(currentWhile.getStartWhile());
				} else {
					memory.pollWhile();
				}
				next();
				continue;
			}

			return result;
		}
	}

	private void executeWhile(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterWhile condition = new EaterWhile(s);
		condition.analyze(context, memory);
		final TokenStack whileExpression = condition.getWhileExpression();
		final ExecutionContextWhile theWhile = ExecutionContextWhile.fromValue(whileExpression,
				source.getCodePosition());
		final TValue value = theWhile.conditionValue(s.getLocation(), context, memory);
		if (value.toBoolean() == false) {
			theWhile.skipMe();
		}
		memory.addWhile(theWhile);
	}

}
