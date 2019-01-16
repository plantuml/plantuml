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
package net.sourceforge.plantuml.evalex;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Snipset1 {

	public static void main(String[] args) {
		Number result = null;

		Expression expression = new Expression("1+1/3");
		result = expression.eval();
		expression.setPrecision(2);
		result = expression.eval();

		result = new Expression("(3.4 + -4.1)/2").eval();

		result = new Expression("SQRT(a^2 + b^2)").with("a", "2.4").and("b", "9.253").eval();

		BigDecimal a = new BigDecimal("2.4");
		BigDecimal b = new BigDecimal("9.235");
		result = new Expression("SQRT(a^2 + b^2)").with("a", a).and("b", b).eval();

		result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();

		result = new Expression("random() > 0.5").eval();

		result = new Expression("not(x<7 || sqrt(max(x,9,3,min(4,3))) <= 3)").with("x", "22.9").eval();
		System.err.println("foo1=" + result);

		result = new Expression("log10(100)").eval();
	}
}
