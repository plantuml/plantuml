/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
	public static final int END_OF_INPUT = 256;
	public static final int INVALID_TOKEN = 257;
	public static final int IDENTIFIER = 258;
	public static final int FLOAT = 259;
	public static final int INTEGER = 270;

	public static final int FORWARD = 260;
	public static final int BACK = 261;
	public static final int LEFT = 262;
	public static final int RIGHT = 263;
	public static final int PENUP = 264;
	public static final int PENDOWN = 265;
	public static final int HIDETURTLE = 266;
	public static final int SHOWTURTLE = 267;
	public static final int CLEARSCREEN = 268;
	public static final int REPEAT = 269;
	public static final int TO = 271;
	public static final int SETPC = 272;

	public int kind;
	public String lexeme;
	public float value;
	public int intValue;
}
