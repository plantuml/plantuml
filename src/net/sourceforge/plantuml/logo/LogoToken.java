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


class LogoToken {
	public final static int END_OF_INPUT = 256;
	public final static int INVALID_TOKEN = 257;
	public final static int IDENTIFIER = 258;
	public final static int FLOAT = 259;
	public final static int INTEGER = 270;

	public final static int FORWARD = 260;
	public final static int BACK = 261;
	public final static int LEFT = 262;
	public final static int RIGHT = 263;
	public final static int PENUP = 264;
	public final static int PENDOWN = 265;
	public final static int HIDETURTLE = 266;
	public final static int SHOWTURTLE = 267;
	public final static int CLEARSCREEN = 268;
	public final static int REPEAT = 269;
	public final static int TO = 271;
	public final static int SETPC = 272;

	public int kind;
	public String lexeme;
	public float value;
	public int intValue;
}
