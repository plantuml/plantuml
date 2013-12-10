/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.turing;

public class BFMachine {

	private final char cells[] = new char[30000];
	private final BFToken prg[];
	private int prgPointer;
	private int pointer;
	private final StringBuilder output = new StringBuilder();
	private final String input;
	private int inputPointer;

	public BFMachine(String program, String input) {
		prg = new BFToken[program.length() + 1];
		int i = 0;
		for (int n = 0; n < program.length(); n++) {
			final BFToken token = BFToken.getToken(program.charAt(n));
			if (token != null) {
				prg[i++] = token;
			}
		}
		this.input = input;
	}

	public boolean run() {
		for (int i = 0; i < 10000000; i++) {
			if (prg[prgPointer] == null) {
				return true;
			}
			interpret(prg[prgPointer]);
			prgPointer++;
		}
		return false;
	}

	private void interpret(BFToken token) {
		switch (token) {
		case NEXT:
			pointer++;
			break;

		case PREVIOUS:
			pointer--;
			break;

		case PLUS:
			cells[pointer]++;
			break;

		case MINUS:
			cells[pointer]--;
			break;

		case OUTPUT:
			output(cells[pointer]);
			break;

		case INPUT:
			final int read = input();
			if (read != -1) {
				cells[pointer] = (char) read;
			}
			break;

		case BRACKET_LEFT:
			if (cells[pointer] == 0) {
				int i = 1;
				while (i > 0) {
					BFToken c2 = prg[++prgPointer];
					if (c2 == BFToken.BRACKET_LEFT)
						i++;
					else if (c2 == BFToken.BRACKET_RIGHT)
						i--;
				}
			}
			break;
		case BRACKET_RIGHT:
			int i = 1;
			while (i > 0) {
				BFToken c2 = prg[--prgPointer];
				if (c2 == BFToken.BRACKET_LEFT)
					i--;
				else if (c2 == BFToken.BRACKET_RIGHT)
					i++;
			}
			prgPointer--;
			break;
		}

	}

	private int input() {
		if (inputPointer < input.length()) {
			return input.charAt(inputPointer++);
		}
		return -1;
	}

	private void output(char c) {
		output.append(c);
	}

	public String getOutput() {
		return output.toString();
	}
}
