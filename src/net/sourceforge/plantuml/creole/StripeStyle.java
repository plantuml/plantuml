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
package net.sourceforge.plantuml.creole;

import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.atom.AtomText;
import net.sourceforge.plantuml.creole.atom.Bullet;
import net.sourceforge.plantuml.graphic.FontConfiguration;

public class StripeStyle {

	private final StripeStyleType type;
	private final int order;
	private final char style;

	public StripeStyle(StripeStyleType type, int order, char style) {
		this.type = type;
		this.order = order;
		this.style = style;
	}

	public final StripeStyleType getType() {
		return type;
	}

	public Atom getHeader(FontConfiguration fontConfiguration, CreoleContext context) {
		if (type == StripeStyleType.LIST_WITHOUT_NUMBER) {
			return new Bullet(fontConfiguration, order);
		}
		if (type == StripeStyleType.LIST_WITH_NUMBER) {
			final int localNumber = context.getLocalNumber(order);
			return AtomText.createListNumber(fontConfiguration, order, localNumber);
		}
		return null;
	}

	public final int getOrder() {
		return order;
	}

	public char getStyle() {
		return style;
	}
}
