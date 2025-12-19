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
 */
package net.sourceforge.plantuml.tim.iterator;

import java.util.List;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;
import net.sourceforge.plantuml.tim.EaterElseIf;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterIf;
import net.sourceforge.plantuml.tim.EaterIfdef;
import net.sourceforge.plantuml.tim.EaterIfndef;
import net.sourceforge.plantuml.tim.ExecutionContextIf;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TMemory;

public class CodeIteratorIf extends AbstractCodeIterator {

	private final TContext context;
	private final TMemory memory;
	private final List<StringLocated> logs;

	public CodeIteratorIf(CodeIterator source, TContext context, TMemory memory, List<StringLocated> logs) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.logs = logs;
	}

	public StringLocated peek() throws EaterException {
		while (true) {
			final StringLocated result = source.peek();
			if (result == null)
				return null;

			final TLineType type = result.getType();

			if (isConditionalDirective(type)) {
				logs.add(result);
				processConditionalDirective(result, type);
				next();
				continue;
			}

			if (shouldSkipLine()) {
				logs.add(result);
				next();
				continue;
			}

			return result;
		}
	}

	private boolean isConditionalDirective(TLineType type) {
		return type == TLineType.IF || type == TLineType.IFDEF || type == TLineType.IFNDEF || type == TLineType.ELSE
				|| type == TLineType.ELSEIF || type == TLineType.ENDIF;
	}

	private void processConditionalDirective(StringLocated line, TLineType type) throws EaterException {
		switch (type) {
		case IF:
			executeIf(line);
			break;
		case IFDEF:
			executeIfdef(line);
			break;
		case IFNDEF:
			executeIfndef(line);
			break;
		case ELSE:
			executeElse(line);
			break;
		case ELSEIF:
			executeElseIf(line);
			break;
		case ENDIF:
			executeEndif(line);
			break;
		}
	}

	private boolean shouldSkipLine() throws EaterException {
		return memory.peekIf() != null && areAllIfOk() == false;
	}

	private boolean areAllIfOk() throws EaterException {
		return memory.areAllIfOk(context, memory);
	}

	// --- Execution methods ---

	private void executeIf(StringLocated s) throws EaterException {
		final boolean isTrue;
		if (areAllIfOk()) {
			final EaterIf condition = new EaterIf(s);
			condition.analyze(context, memory);
			isTrue = condition.isTrue();
		} else
			isTrue = false;

		memory.addIf(ExecutionContextIf.fromValue(isTrue));
	}

	private void executeIfdef(StringLocated s) throws EaterException {
		final EaterIfdef condition = new EaterIfdef(s);
		condition.analyze(context, memory);
		final boolean isTrue = condition.isTrue(context, memory);
		memory.addIf(ExecutionContextIf.fromValue(isTrue));
	}

	private void executeIfndef(StringLocated s) throws EaterException {
		final EaterIfndef condition = new EaterIfndef(s);
		condition.analyze(context, memory);
		final boolean isTrue = condition.isTrue(context, memory);
		memory.addIf(ExecutionContextIf.fromValue(isTrue));
	}

	private void executeElseIf(StringLocated s) throws EaterException {
		final ExecutionContextIf poll = getRequiredIfContext(s, "elseif");
		poll.enteringElseIf();

		if (poll.hasBeenBurn() == false) {
			final EaterElseIf condition = new EaterElseIf(s);
			condition.analyze(context, memory);
			if (condition.isTrue())
				poll.nowInSomeElseIf();
		}
	}

	private void executeElse(StringLocated s) throws EaterException {
		final ExecutionContextIf poll = getRequiredIfContext(s, "else");
		poll.nowInElse();
	}

	private void executeEndif(StringLocated s) throws EaterException {
		final ExecutionContextIf poll = (ExecutionContextIf) memory.pollIf();
		if (poll == null)
			throw new EaterException("No if related to this endif", s);
	}

	// --- Helper methods ---

	private ExecutionContextIf getRequiredIfContext(StringLocated s, String directive) throws EaterException {
		final ExecutionContextIf poll = (ExecutionContextIf) memory.peekIf();
		if (poll == null)
			throw new EaterException("No if related to this " + directive, s);
		return poll;
	}

}