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
package net.sourceforge.plantuml.tim;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.TokenStack;
import net.sourceforge.plantuml.tim.iterator.CodePosition;

public class ExecutionContextWhile {

	private final TokenStack whileExpression;
	private final CodePosition codePosition;
	private boolean skipMe;

	private ExecutionContextWhile(TokenStack whileExpression, CodePosition codePosition) {
		this.whileExpression = whileExpression;
		this.codePosition = codePosition;
	}

	@Override
	public String toString() {
		return whileExpression.toString() + " " + codePosition;
	}

	public static ExecutionContextWhile fromValue(TokenStack whileExpression, CodePosition codePosition) {
		return new ExecutionContextWhile(whileExpression, codePosition);
	}

	public TValue conditionValue(LineLocation location, TContext context, TMemory memory) throws EaterException, EaterExceptionLocated {
		return whileExpression.getResult(location, context, memory);
	}

	public void skipMe() {
		skipMe = true;
	}

	public final boolean isSkipMe() {
		return skipMe;
	}

	public CodePosition getStartWhile() {
		return codePosition;
	}

}
