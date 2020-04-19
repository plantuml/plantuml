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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.Token;
import net.sourceforge.plantuml.tim.expression.TokenStack;
import net.sourceforge.plantuml.tim.expression.TokenType;

public abstract class Eater {

	private int i = 0;
	private final String s;
	private final StringLocated sl;

	public Eater(StringLocated sl) {
		this.s = sl.getString();
		this.sl = sl;
	}

	public final StringLocated getStringLocated() {
		if (sl == null) {
			throw new UnsupportedOperationException();
		}
		return sl;
	}

	public final LineLocation getLineLocation() {
		if (sl == null) {
			throw new UnsupportedOperationException();
		}
		return sl.getLocation();
	}

	public abstract void analyze(TContext context, TMemory memory) throws EaterException, EaterExceptionLocated;

	public int getCurrentPosition() {
		return i;
	}

	final protected String eatAllToEnd() throws EaterException {
		final String result = s.substring(i);
		i = s.length();
		return result;
	}

	final protected TValue eatExpression(TContext context, TMemory memory)
			throws EaterException, EaterExceptionLocated {
		if (peekChar() == '{') {
			String data = eatAllToEnd();
			// System.err.println("data=" + data);
			final JsonValue json = Json.parse(data);
			// System.err.println("json=" + json);
			return TValue.fromJson(json);
		}
		final TokenStack tokenStack = eatTokenStack();
		return tokenStack.getResult(getLineLocation(), context, memory);
	}

	final protected TokenStack eatTokenStack() throws EaterException {
		final TokenStack tokenStack = new TokenStack();
		addIntoTokenStack(tokenStack, false);
		if (tokenStack.size() == 0) {
			throw EaterException.located("Missing expression", getStringLocated());
		}
		return tokenStack;
	}

	final protected TValue eatExpressionStopAtColon(TContext context, TMemory memory)
			throws EaterException, EaterExceptionLocated {
		final TokenStack tokenStack = new TokenStack();
		addIntoTokenStack(tokenStack, true);
		return tokenStack.getResult(getLineLocation(), context, memory);
	}

	final protected void addIntoTokenStack(TokenStack tokenStack, boolean stopAtColon) throws EaterException {
		while (true) {
			final Token token = TokenType.eatOneToken(this, stopAtColon);
			// System.err.println("token=" + token);
			if (token == null) {
				return;
			}
			tokenStack.add(token);
		}
	}

	final public String eatAndGetQuotedString() throws EaterException {
		final char separator = peekChar();
		if (TLineType.isQuote(separator) == false) {
			throw EaterException.located("quote10", getStringLocated());
		}
		checkAndEatChar(separator);
		final StringBuilder value = new StringBuilder();
		addUpTo(separator, value);
		checkAndEatChar(separator);
		return value.toString();
	}

	final protected String eatAndGetOptionalQuotedString() throws EaterException {
		final char quote = peekChar();
		if (TLineType.isQuote(quote)) {
			return eatAndGetQuotedString();
		}
		final StringBuilder value = new StringBuilder();
		// DEPLICATE eatUntilCloseParenthesisOrComma

		int level = 0;
		while (true) {
			char ch = peekChar();
			if (ch == 0) {
				throw EaterException.located("until001", getStringLocated());
			}
			if (level == 0 && (ch == ',' || ch == ')')) {
				return value.toString().trim();
			}
			ch = eatOneChar();
			if (ch == '(') {
				level++;
			} else if (ch == ')') {
				level--;
			}
			value.append(ch);
		}

		// addUpTo(',', ')', value);
		// return value.toString();
	}

	final public String eatAndGetNumber() throws EaterException {
		final StringBuilder result = new StringBuilder();
		while (true) {
			final char ch = peekChar();
			if (ch == 0 || TLineType.isLatinDigit(ch) == false) {
				return result.toString();
			}
			result.append(eatOneChar());
		}
	}

	final public String eatAndGetSpaces() throws EaterException {
		final StringBuilder result = new StringBuilder();
		while (true) {
			final char ch = peekChar();
			if (ch == 0 || TLineType.isSpaceChar(ch) == false) {
				return result.toString();
			}
			result.append(eatOneChar());
		}
	}

	final protected String eatAndGetVarname() throws EaterException {
		final StringBuilder varname = new StringBuilder("" + eatOneChar());
		if (TLineType.isLetterOrUnderscoreOrDollar(varname.charAt(0)) == false) {
			throw EaterException.located("a002", getStringLocated());
		}
		addUpToLastLetterOrUnderscoreOrDigit(varname);
		return varname.toString();
	}

	final protected String eatAndGetFunctionName() throws EaterException {
		final StringBuilder varname = new StringBuilder("" + eatOneChar());
		if (TLineType.isLetterOrUnderscoreOrDollar(varname.charAt(0)) == false) {
			throw EaterException.located("a003", getStringLocated());
		}
		addUpToLastLetterOrUnderscoreOrDigit(varname);
		return varname.toString();
	}

	final public void skipSpaces() {
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
	}

	final protected void skipUntilChar(char ch) {
		while (i < s.length() && s.charAt(i) != ch) {
			i++;
		}
	}

	final public char peekChar() {
		if (i >= s.length()) {
			return 0;
		}
		return s.charAt(i);
	}

	final public char peekCharN2() {
		if (i + 1 >= s.length()) {
			return 0;
		}
		return s.charAt(i + 1);
	}

	final protected boolean hasNextChar() {
		return i < s.length();
	}

	final public char eatOneChar() {
		final char ch = s.charAt(i);
		i++;
		return ch;
	}

	final protected void checkAndEatChar(char ch) throws EaterException {
		if (i >= s.length() || s.charAt(i) != ch) {
			throw EaterException.located("a001", getStringLocated());
		}
		i++;
	}

	final protected boolean safeCheckAndEatChar(char ch) throws EaterException {
		if (i >= s.length() || s.charAt(i) != ch) {
			return false;
		}
		i++;
		return true;
	}

	final protected void optionallyEatChar(char ch) throws EaterException {
		if (i >= s.length() || s.charAt(i) != ch) {
			return;
		}
		assert s.charAt(i) == ch;
		i++;
	}

	final protected void checkAndEatChar(String s) throws EaterException {
		for (int j = 0; j < s.length(); j++) {
			checkAndEatChar(s.charAt(j));
		}
	}

	final protected void addUpToLastLetterOrUnderscoreOrDigit(StringBuilder sb) {
		while (i < s.length()) {
			final char ch = s.charAt(i);
			if (TLineType.isLetterOrUnderscoreOrDigit(ch) == false) {
				return;
			}
			i++;
			sb.append(ch);
		}
	}

	final protected void addUpTo(char separator, StringBuilder sb) {
		while (i < s.length()) {
			final char ch = peekChar();
			if (ch == separator) {
				return;
			}
			i++;
			sb.append(ch);
		}
	}

	// final protected void addUpToUnused(char separator1, char separator2,
	// StringBuilder sb) {
	// while (i < s.length()) {
	// final char ch = peekChar();
	// if (ch == separator1 || ch == separator2) {
	// return;
	// }
	// i++;
	// sb.append(ch);
	// }
	// }

	final protected TFunctionImpl eatDeclareFunction(TContext context, TMemory memory, boolean unquoted,
			LineLocation location, boolean allowNoParenthesis, TFunctionType type)
			throws EaterException, EaterExceptionLocated {
		final List<TFunctionArgument> args = new ArrayList<TFunctionArgument>();
		final String functionName = eatAndGetFunctionName();
		skipSpaces();
		if (safeCheckAndEatChar('(') == false) {
			if (allowNoParenthesis) {
				return new TFunctionImpl(functionName, args, unquoted, type);
			}
			throw EaterException.located("Missing opening parenthesis", getStringLocated());
		}
		while (true) {
			skipSpaces();
			char ch = peekChar();
			if (TLineType.isLetterOrUnderscoreOrDollar(ch)) {
				final String varname = eatAndGetVarname();
				skipSpaces();
				final TValue defValue;
				if (peekChar() == '=') {
					eatOneChar();
					final TokenStack def = TokenStack.eatUntilCloseParenthesisOrComma(this);
					def.guessFunctions();
					defValue = def.getResult(getLineLocation(), context, memory);
					// System.err.println("result=" + defValue);
				} else {
					defValue = null;
				}
				args.add(new TFunctionArgument(varname, defValue));
			} else if (ch == ',') {
				checkAndEatChar(',');
			} else if (ch == ')') {
				checkAndEatChar(")");
				break;
			} else {
				throw EaterException.located("Error in function definition", getStringLocated());
			}
		}
		skipSpaces();
		return new TFunctionImpl(functionName, args, unquoted, type);
	}

	final protected TFunctionImpl eatDeclareReturnFunctionWithOptionalReturn(TContext context, TMemory memory,
			boolean unquoted, LineLocation location) throws EaterException, EaterExceptionLocated {
		final TFunctionImpl result = eatDeclareFunction(context, memory, unquoted, location, false, TFunctionType.RETURN_FUNCTION);
		if (peekChar() == 'r') {
			checkAndEatChar("return");
			skipSpaces();
			final String line = "!return " + eatAllToEnd();
			result.addBody(new StringLocated(line, location));
		} else if (peekChar() == '!') {
			checkAndEatChar("!return");
			skipSpaces();
			final String line = "!return " + eatAllToEnd();
			result.addBody(new StringLocated(line, location));
		}
		return result;
	}

	final protected TFunctionImpl eatDeclareProcedure(TContext context, TMemory memory, boolean unquoted,
			LineLocation location) throws EaterException, EaterExceptionLocated {
		final TFunctionImpl result = eatDeclareFunction(context, memory, unquoted, location, false, TFunctionType.PROCEDURE);
		return result;
	}

}
