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
 *
 */
package net.sourceforge.plantuml.logo;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class TinyJavaLogo {
	private final LogoScanner scanner = new LogoScanner();

	private final TurtleGraphicsPane turtleGraphicsPane;

	private final Map<String, String> dictionary = new HashMap<String, String>();
	private HColor penColor;

	public TinyJavaLogo(TurtleGraphicsPane turtleGraphicsPane) {
		this.turtleGraphicsPane = turtleGraphicsPane;
	}

	private void message(String messageText) {
		//turtleGraphicsPane.message(messageText);
	}

	private void error(String messageText) {
		turtleGraphicsPane.message("Error: " + messageText);
	}

	private void parseCommandBlock(int nestLevel) {
		String commandName;
		String code;
		LogoToken token = scanner.getToken();
		while (token.kind != LogoToken.END_OF_INPUT && token.kind != LogoToken.INVALID_TOKEN) {
			switch (token.kind) {
			case LogoToken.FORWARD:
				token = scanner.getToken();
				if (token.kind == LogoToken.FLOAT || token.kind == LogoToken.INTEGER) {
					turtleGraphicsPane.forward(token.value);
					token = scanner.getToken();
				} else {
					error("FORWARD requires distance");
					return;
				}
				break;

			case LogoToken.BACK:
				token = scanner.getToken();
				if (token.kind == LogoToken.FLOAT || token.kind == LogoToken.INTEGER) {
					turtleGraphicsPane.back(token.value);
					token = scanner.getToken();
				} else {
					error("BACK requires distance");
					return;
				}
				break;

			case LogoToken.LEFT:
				token = scanner.getToken();
				if (token.kind == LogoToken.FLOAT || token.kind == LogoToken.INTEGER) {
					turtleGraphicsPane.left(token.value);
					token = scanner.getToken();
				} else {
					error("LEFT requires turn angle");
					return;
				}
				break;

			case LogoToken.RIGHT:
				token = scanner.getToken();
				if (token.kind == LogoToken.FLOAT || token.kind == LogoToken.INTEGER) {
					turtleGraphicsPane.right(token.value);
					token = scanner.getToken();
				} else {
					error("RIGHT requires turn angle");
					return;
				}
				break;

			case LogoToken.PENUP:
				turtleGraphicsPane.penUp();
				token = scanner.getToken();
				break;

			case LogoToken.PENDOWN:
				turtleGraphicsPane.penDown();
				token = scanner.getToken();
				break;

			case LogoToken.HIDETURTLE:
				turtleGraphicsPane.hideTurtle();
				token = scanner.getToken();
				break;

			case LogoToken.SHOWTURTLE:
				turtleGraphicsPane.showTurtle();
				token = scanner.getToken();
				break;

			case LogoToken.CLEARSCREEN:
				turtleGraphicsPane.clearScreen();
				token = scanner.getToken();
				break;

			case LogoToken.REPEAT:
				token = scanner.getToken();
				if (token.kind != LogoToken.INTEGER) {
					error("REPEAT requires positive integer count");
					return;
				}
				int count = token.intValue;
				token = scanner.getToken();
				if (token.kind != '[') {
					error("REPEAT requires block in []");
					return;
				}
				final int blockStart = scanner.getPosition();
				while (count-- > 0) {
					scanner.setPosition(blockStart);
					parseCommandBlock(nestLevel + 1);
				}
				token = scanner.getToken();
				break;

			case LogoToken.TO:
				token = scanner.getToken();
				if (token.kind != LogoToken.IDENTIFIER) {
					error("TO requires name for new definition");
					return;
				}
				commandName = token.lexeme;
				if (dictionary.get(commandName) == null) {
					message("Defining new command " + commandName);
				} else {
					message("Redefining command " + commandName);
				}
				code = scanner.getRestAsString();
				dictionary.put(commandName, code);
				token = scanner.getToken();
				break;

			case LogoToken.IDENTIFIER:
				commandName = token.lexeme;
				code = dictionary.get(commandName);
				if (code == null) {
					error("Undefined command " + commandName);
					return;
				}
				final String savedCommand = scanner.getSourceString();
				final int savedPosition = scanner.getPosition();
				scanner.setSourceString(code);
				parseCommandBlock(0);
				scanner.setSourceString(savedCommand);
				scanner.setPosition(savedPosition);
				token = scanner.getToken();
				break;

			case LogoToken.SETPC:
				token = scanner.getToken();
				final HColor newPenColor = HColorSet.instance().getColorIfValid(token.lexeme);
				if (newPenColor == null) {
					error("Unrecognized color name");
					return;
				}
				penColor = newPenColor;
				turtleGraphicsPane.setPenColor(penColor);
				token = scanner.getToken();
				break;

			case '[':
				token = scanner.getToken();
				break;

			case ']':
				if (nestLevel == 0) {
					error("] without matching [");
					token = scanner.getToken();
					return;
				}
				return;

			default:
				error("Unrecognized symbol in input");
				return;
			}
		}
	}

	public void doCommandLine(String commandLine) {
		message(commandLine);
		scanner.setSourceString(commandLine);
		parseCommandBlock(0);
	}
}
