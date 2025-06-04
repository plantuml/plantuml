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
package net.sourceforge.plantuml.tim.expression;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;

public class ReversePolishInterpretor {

	private final TValue result;
	private final boolean trace = false;

	public ReversePolishInterpretor(StringLocated location, TokenStack queue, Knowledge knowledge, TMemory memory,
			TContext context) throws EaterException {

		final Map<String, TValue> named = new HashMap<>();
		final Deque<TValue> stack = new ArrayDeque<>();
		if (trace)
			System.err.println("ReversePolishInterpretor::queue=" + queue);
		for (TokenIterator it = queue.tokenIterator(); it.hasMoreTokens();) {
			final Token token = it.nextToken();
			if (trace)
				System.err.println("rpn " + token);
			if (token.getTokenType() == TokenType.NUMBER) {
				stack.addFirst(TValue.fromNumber(token));
			} else if (token.getTokenType() == TokenType.QUOTED_STRING) {
				stack.addFirst(TValue.fromString(token));
			} else if (token.getTokenType() == TokenType.JSON_DATA) {
				stack.addFirst(TValue.fromJson(token.getJson()));
			} else if (token.getTokenType() == TokenType.AFFECTATION) {
				final TValue v2 = stack.removeFirst();
				final TValue v1 = stack.removeFirst();
				named.put(v1.toString(), v2);
			} else if (token.getTokenType() == TokenType.OPERATOR) {
				final TValue v2 = stack.removeFirst();
				final TValue v1 = stack.removeFirst();
				final TokenOperator op = token.getTokenOperator();
				if (op == null)
					throw new EaterException("bad op", location);

				final TValue tmp = op.operate(v1, v2);
				stack.addFirst(tmp);
			} else if (token.getTokenType() == TokenType.OPEN_PAREN_FUNC) {
				final int nb = Integer.parseInt(token.getSurface()) - named.size();
				final Token token2 = it.nextToken();
				if (token2.getTokenType() != TokenType.FUNCTION_NAME)
					throw new EaterException("rpn43", location);

				if (trace)
					System.err.println("token2=" + token2);
				final TFunction function = knowledge.getFunction(new TFunctionSignature(token2.getSurface(), nb));
				if (trace)
					System.err.println("function=" + function);
				if (function == null)
					throw new EaterException("Unknown built-in function " + token2.getSurface(), location);

				if (function.canCover(nb, Collections.<String>emptySet()) == false)
					throw new EaterException("Bad number of arguments for " + function.getSignature().getFunctionName(),
							location);

				final List<TValue> args = new ArrayList<>();
				for (int i = 0; i < nb; i++)
					args.add(0, stack.removeFirst());

				if (trace)
					System.err.println("args=" + args);
				if (location == null)
					throw new EaterException("rpn44", location);

				final TValue r = function.executeReturnFunction(context, memory, location, args, named);
				named.clear();
				if (trace)
					System.err.println("r=" + r);
				stack.addFirst(r);
			} else {
				throw new EaterException("rpn41", location);
			}
		}
		result = stack.removeFirst();
	}

	public final TValue getResult() {
		return result;
	}
}
