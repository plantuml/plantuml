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
package net.sourceforge.plantuml.tim.stdlib;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.color.HSLColor;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.utils.LineLocation;

public class HslColor extends SimpleReturnFunction {

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%hsl_color", 3);
	}

	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg == 3 || nbArg == 4;
	}

	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> values,
			Map<String, TValue> named) throws EaterException, EaterExceptionLocated {
		final int h = values.get(0).toInt();
		final int s = values.get(1).toInt();
		final int l = values.get(2).toInt();
		if (values.size() == 3) {
			final HSLColor color = new HSLColor(h, s, l);
			final Color rgb = color.getRGB();
			return TValue.fromString(HColors.simple(rgb).asString());
		}
		final int a = values.get(3).toInt();
		final HSLColor color = new HSLColor(h, s, l, (float) (a / 100.0));
		final Color rgb = color.getRGB();
		return TValue.fromString(HColors.simple(rgb).asString());

	}
}
