/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.salt.factory;

import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementLine;

public class ElementFactoryLine implements ElementFactory {

	final private DataSource dataSource;

	public ElementFactoryLine(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final Terminated<String> next = dataSource.next();
		final String text = next.getElement();
		return new Terminated<Element>(new ElementLine(text.charAt(0)), next.getTerminator());
	}

	public boolean ready() {
		final String text = dataSource.peek(0).getElement();
		if (isLine(text, '-')) {
			return true;
		}
		if (isLine(text, '=')) {
			return true;
		}
		if (isLine(text, '~')) {
			return true;
		}
		if (isLine(text, '.')) {
			return true;
		}
		return false;
	}

	private boolean isLine(String text, char c) {
		final String s = "" + c + c;
		return text.startsWith(s) && text.endsWith(s);
	}
}
