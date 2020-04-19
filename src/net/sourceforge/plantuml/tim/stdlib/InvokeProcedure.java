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
package net.sourceforge.plantuml.tim.stdlib;

import java.util.List;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterFunctionCall;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TFunctionType;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class InvokeProcedure implements TFunction {

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%invoke_procedure", 1);
	}

	public boolean canCover(int nbArg) {
		return nbArg > 0;
	}

	public TFunctionType getFunctionType() {
		return TFunctionType.PROCEDURE;
	}

	public void executeProcedure(TContext context, TMemory memory, LineLocation location, String s) throws EaterException, EaterExceptionLocated {
		final EaterFunctionCall call = new EaterFunctionCall(new StringLocated(s, location), false, isUnquoted());
		call.analyze((TContext) context, memory);
		final List<TValue> values = call.getValues();
		final String fname = values.get(0).toString();
		final List<TValue> args = values.subList(1, values.size());
		final TFunctionSignature signature = new TFunctionSignature(fname, args.size());
		final TFunction func = context.getFunctionSmart(signature);
		if (func == null) {
			throw EaterException.located("Cannot find void function " + fname, new StringLocated(s, location));
		}
		func.executeProcedureInternal(context, memory, args);
	}

	public void executeProcedureInternal(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		throw new UnsupportedOperationException();
	}

	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> args)
			throws EaterException {
		throw new UnsupportedOperationException();
	}

	public boolean isUnquoted() {
		return false;
	}

}
