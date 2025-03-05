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
import java.util.List;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.expression.Token;
import net.sourceforge.plantuml.tim.expression.TokenStack;
import net.sourceforge.plantuml.tim.expression.TokenType;
import net.sourceforge.plantuml.utils.LineLocation;

public abstract class Eater {

	private int i = 0;
	private final StringLocated stringLocated;

	public Eater(StringLocated stringLocated) {
		this.stringLocated = stringLocated;
	}

	public final LineLocation getLineLocation() {
		return stringLocated.getLocation();
	}

	public final StringLocated getStringLocated() {
		return stringLocated;
	}

	public abstract void analyze(TContext context, TMemory memory) throws EaterException;

	public int getCurrentPosition() {
		return i;
	}

	final protected String eatAllToEnd() throws EaterException {
		final String result = stringLocated.getString().substring(i);
		i = stringLocated.length();
		return result;
	}

	final public TValue eatExpression(TContext context, TMemory memory) throws EaterException {
		final char ch = peekChar();
		if (ch == '{' || ch == '[') {
			final String data = eatAllToEnd();
			// System.err.println("data=" + data);
			final JsonValue json = Json.parse(data);
			// System.err.println("json=" + json);
			return TValue.fromJson(json);
		}
		final TokenStack tokenStack = eatTokenStack();
		return tokenStack.getResult(getStringLocated(), context, memory);
	}

	final protected TokenStack eatTokenStack() throws EaterException {
		final TokenStack tokenStack = new TokenStack();
		addIntoTokenStack(tokenStack, false);
		if (tokenStack.size() == 0)
			throw new EaterException("Missing expression", stringLocated);

		return tokenStack;
	}

	final protected TValue eatExpressionStopAtColon(TContext context, TMemory memory) throws EaterException {
		final TokenStack tokenStack = new TokenStack();
		addIntoTokenStack(tokenStack, true);
		return tokenStack.getResult(getStringLocated(), context, memory);
	}

	final protected void addIntoTokenStack(TokenStack tokenStack, boolean stopAtColon) throws EaterException {
		Token lastToken = null;
		while (true) {
			final Token token = TokenType.eatOneToken(lastToken, this, stopAtColon);
			if (token == null)
				return;

			tokenStack.add(token);
			if (token.getTokenType() != TokenType.SPACES)
				lastToken = token;
		}
	}

	final public String eatAndGetQuotedString() throws EaterException {
		final char separator = peekChar();
		if (TLineType.isQuote(separator) == false)
			throw new EaterException("quote10", stringLocated);

		checkAndEatChar(separator);
		final StringBuilder value = new StringBuilder();
		addUpTo(separator, value);
		checkAndEatChar(separator);
		return value.toString();
	}

	final protected String eatAndGetOptionalQuotedString() throws EaterException {
		final char quote = peekChar();
		if (TLineType.isQuote(quote))
			return eatAndGetQuotedString();

		final StringBuilder value = new StringBuilder();
		// DEPLICATE eatUntilCloseParenthesisOrComma

		int level = 0;
		while (true) {
			char ch = peekChar();
			if (ch == 0)
				throw new EaterException("until001", stringLocated);

			if (level == 0 && (ch == ',' || ch == ')'))
				return value.toString().trim();

			ch = eatOneChar();
			if (ch == '(')
				level++;
			else if (ch == ')')
				level--;

			value.append(ch);
		}

		// addUpTo(',', ')', value);
		// return value.toString();
	}

	final public String eatAndGetNumber() throws EaterException {
		final StringBuilder result = new StringBuilder();
		while (true) {
			final char ch = peekChar();
			if (result.length() == 0 && ch == '-') {
				result.append(eatOneChar());
				continue;
			}

			if (ch == 0 || TLineType.isLatinDigit(ch) == false)
				return result.toString();

			result.append(eatOneChar());
		}
	}

	final public String eatAndGetSpaces() throws EaterException {
		final StringBuilder result = new StringBuilder();
		while (true) {
			final char ch = peekChar();
			if (ch == 0 || TLineType.isSpaceChar(ch) == false)
				return result.toString();

			result.append(eatOneChar());
		}
	}

	final protected String eatAndGetVarname() throws EaterException {
		final StringBuilder varname = new StringBuilder("" + eatOneChar());
		if (TLineType.isLetterOrEmojiOrUnderscoreOrDollar(varname.charAt(0)) == false)
			throw new EaterException("a002", stringLocated);

		addUpToLastLetterOrEmojiOrUnderscoreOrDigit(varname);
		return varname.toString();
	}

	final protected String eatAndGetFunctionName() throws EaterException {
		final StringBuilder varname = new StringBuilder("" + eatOneChar());
		if (TLineType.isLetterOrEmojiOrUnderscoreOrDollar(varname.charAt(0)) == false)
			throw new EaterException("a003", stringLocated);

		addUpToLastLetterOrEmojiOrUnderscoreOrDigit(varname);
		return varname.toString();
	}

	final public void skipSpaces() {
		while (i < stringLocated.length() && Character.isWhitespace(stringLocated.charAt(i)))
			i++;

	}

	final protected void skipUntilChar(char ch) {
		while (i < stringLocated.length() && stringLocated.charAt(i) != ch)
			i++;

	}

	final public char peekChar() {
		if (i >= stringLocated.length())
			return 0;

		return stringLocated.charAt(i);
	}

	final public boolean matchAffectation() {
		final String tmp = stringLocated.getString().substring(i);
		final boolean result = tmp.matches("^\\$?[_\\p{L}][_\\p{L}0-9]*\\s*=.*");
		return result;
	}

	final public char peekCharN2() {
		if (i + 1 >= stringLocated.length())
			return 0;

		return stringLocated.charAt(i + 1);
	}

	final protected boolean hasNextChar() {
		return i < stringLocated.length();
	}

	final public char eatOneChar() {
		final char ch = stringLocated.charAt(i);
		i++;
		return ch;
	}

	final protected void checkAndEatChar(char ch) throws EaterException {
		if (i >= stringLocated.length() || stringLocated.charAt(i) != ch)
			throw new EaterException("a001", stringLocated);

		i++;
	}

	final protected boolean safeCheckAndEatChar(char ch) throws EaterException {
		if (i >= stringLocated.length() || stringLocated.charAt(i) != ch)
			return false;

		i++;
		return true;
	}

	final protected void optionallyEatChar(char ch) throws EaterException {
		if (i >= stringLocated.length() || stringLocated.charAt(i) != ch)
			return;

		assert stringLocated.charAt(i) == ch;
		i++;
	}

	final protected void checkAndEatChar(String s) throws EaterException {
		for (int j = 0; j < s.length(); j++)
			checkAndEatChar(s.charAt(j));

	}

	final protected void addUpToLastLetterOrEmojiOrUnderscoreOrDigit(StringBuilder sb) {
		while (i < stringLocated.length()) {
			final char ch = stringLocated.charAt(i);
			if (TLineType.isLetterOrEmojiOrUnderscoreOrDigit(ch) == false)
				return;

			i++;
			sb.append(ch);
		}
	}

	final protected void addUpTo(char separator, StringBuilder sb) {
		while (i < stringLocated.length()) {
			final char ch = peekChar();
			if (ch == separator)
				return;

			i++;
			sb.append(ch);
		}
	}

	final protected TFunctionImpl eatDeclareFunction(TContext context, TMemory memory, boolean unquoted,
			StringLocated location, boolean allowNoParenthesis, TFunctionType type) throws EaterException {
		final List<TFunctionArgument> args = new ArrayList<>();
		final String functionName = eatAndGetFunctionName();
		skipSpaces();
		if (safeCheckAndEatChar('(') == false) {
			if (allowNoParenthesis)
				return new TFunctionImpl(functionName, args, unquoted, type);

			throw new EaterException("Missing opening parenthesis", stringLocated);
		}
		while (true) {
			skipSpaces();
			char ch = peekChar();
			if (TLineType.isLetterOrEmojiOrUnderscoreOrDollar(ch)) {
				final String varname = eatAndGetVarname();
				skipSpaces();
				final TValue defValue;
				if (peekChar() == '=') {
					eatOneChar();
					final TokenStack def = TokenStack.eatUntilCloseParenthesisOrComma(this);
					def.guessFunctions(location);
					defValue = def.getResult(getStringLocated(), context, memory);
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
				throw new EaterException("Error in function definition", stringLocated);
			}
		}
		skipSpaces();
		return new TFunctionImpl(functionName, args, unquoted, type);
	}

	final protected TFunctionImpl eatDeclareReturnFunctionWithOptionalReturn(TContext context, TMemory memory,
			boolean unquoted, StringLocated location) throws EaterException {
		final TFunctionImpl result = eatDeclareFunction(context, memory, unquoted, location, false,
				TFunctionType.RETURN_FUNCTION);
		if (peekChar() == 'r') {
			checkAndEatChar("return");
			skipSpaces();
			final String line = "!return " + eatAllToEnd();
			result.addBody(new StringLocated(line, location.getLocation()));
		} else if (peekChar() == '!') {
			checkAndEatChar("!return");
			skipSpaces();
			final String line = "!return " + eatAllToEnd();
			result.addBody(new StringLocated(line, location.getLocation()));
		}
		return result;
	}

	final protected TFunctionImpl eatDeclareProcedure(TContext context, TMemory memory, boolean unquoted,
			StringLocated location) throws EaterException {
		final TFunctionImpl result = eatDeclareFunction(context, memory, unquoted, location, false,
				TFunctionType.PROCEDURE);
		return result;
	}

}
