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
package net.sourceforge.plantuml.salt.factory;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementRadioCheckbox;
import net.sourceforge.plantuml.ugraphic.UFont;

public class ElementFactoryCheckboxOff implements ElementFactory {

	final private DataSource dataSource;
	final private ISkinSimple spriteContainer;

	public ElementFactoryCheckboxOff(DataSource dataSource, ISkinSimple spriteContainer) {
		this.dataSource = dataSource;
		this.spriteContainer = spriteContainer;
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final Terminated<String> next = dataSource.next();
		final String text = next.getElement();
		final UFont font = UFont.byDefault(12);
		return new Terminated<Element>(new ElementRadioCheckbox(extracted(text), font, false, false, spriteContainer),
				next.getTerminator());
	}

	private List<String> extracted(final String text) {
		final int x = text.indexOf(']');
		return Arrays.asList(StringUtils.trin(text.substring(x + 1)));
	}

	public boolean ready() {
		final String text = dataSource.peek(0).getElement();
		return text.startsWith("[]") || text.startsWith("[ ]");
	}
}
