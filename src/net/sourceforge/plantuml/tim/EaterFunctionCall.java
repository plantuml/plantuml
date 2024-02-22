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
package net.sourceforge.plantuml.tim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.TokenStack;

public class EaterFunctionCall extends Eater {

	private final List<TValue> values = new ArrayList<>();
	private final Map<String, TValue> namedArguments = new HashMap<String, TValue>();
	private final boolean isLegacyDefine;
	private final boolean unquoted;

	public EaterFunctionCall(StringLocated s, boolean isLegacyDefine, boolean unquoted) {
		super(s);
		this.isLegacyDefine = isLegacyDefine;
		this.unquoted = unquoted;
	}

	@Override
	public void analyze(TContext context, TMemory memory) throws EaterException {
		skipUntilChar('(');
		checkAndEatChar('(');
		skipSpaces();
		if (peekChar() == ')') {
			checkAndEatChar(')');
			return;
		}
		while (true) {
			skipSpaces();
			if (isLegacyDefine) {
				final String read = eatAndGetOptionalQuotedString();
				final String value = context.applyFunctionsAndVariables(memory,
						new StringLocated(read, getLineLocation()));
				final TValue result = TValue.fromString(value);
				values.add(result);
			} else if (unquoted) {
				if (matchAffectation()) {
					final String varname = eatAndGetVarname();
					skipSpaces();
					checkAndEatChar('=');
					skipSpaces();
					final String read = eatAndGetOptionalQuotedString();
					final String value = context.applyFunctionsAndVariables(memory,
							new StringLocated(read, getLineLocation()));
					final TValue result = TValue.fromString(value);
					namedArguments.put(varname, result);
				} else {
					final String read = eatAndGetOptionalQuotedString();
					final String value = context.applyFunctionsAndVariables(memory,
							new StringLocated(read, getLineLocation()));
					final TValue result = TValue.fromString(value);
					values.add(result);
				}
//				}
			} else {
				if (matchAffectation()) {
					final String varname = eatAndGetVarname();
					skipSpaces();
					checkAndEatChar('=');
					skipSpaces();
					final TokenStack tokens = TokenStack.eatUntilCloseParenthesisOrComma(this).withoutSpace();
					tokens.guessFunctions(getStringLocated());
					final TValue result = tokens.getResult(getStringLocated(), context, memory);
					namedArguments.put(varname, result);
				} else {
					final TokenStack tokens = TokenStack.eatUntilCloseParenthesisOrComma(this).withoutSpace();
					tokens.guessFunctions(getStringLocated());
					final TValue result = tokens.getResult(getStringLocated(), context, memory);
					values.add(result);
				}
			}
			skipSpaces();
			final char ch = eatOneChar();
			if (ch == ',') {
				continue;
			}
			if (ch == ')') {
				break;
			}
			if (unquoted) {
				throw new EaterException("unquoted function/procedure cannot use expression.", getStringLocated());
			}
			throw new EaterException("call001", getStringLocated());
		}
	}

	public final List<TValue> getValues() {
		return Collections.unmodifiableList(values);
	}

	public final Map<String, TValue> getNamedArguments() {
		return Collections.unmodifiableMap(namedArguments);
	}

	public final String getEndOfLine() throws EaterException {
		return this.eatAllToEnd();
	}

}
