/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
import net.sourceforge.plantuml.salt.Dictionary;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementBorder;
import net.sourceforge.plantuml.salt.element.TableStrategy;

public class ElementFactoryBorder extends AbstractElementFactoryComplex {

	public ElementFactoryBorder(DataSource dataSource, Dictionary dictionary) {
		super(dataSource, dictionary);
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final String header = getDataSource().next().getElement();
		assert header.startsWith("{");

		TableStrategy strategy = TableStrategy.DRAW_NONE;
		if (header.length() == 2) {
			strategy = TableStrategy.fromChar(header.charAt(1));
		}

		final ElementBorder result = new ElementBorder();

		while (getDataSource().peek(0).getElement().equals("}") == false) {
			final String pos = getDataSource().next().getElement();
			switch (pos.charAt(0)) {
			case 'N':
				result.setNorth(getNextElement().getElement());
				break;
			case 'S':
				result.setSouth(getNextElement().getElement());
				break;
			case 'E':
				result.setEast(getNextElement().getElement());
				break;
			case 'W':
				result.setWest(getNextElement().getElement());
				break;
			case 'C':
				result.setCenter(getNextElement().getElement());
				break;
			default:
				throw new IllegalStateException();

			}
		}
		final Terminated<String> next = getDataSource().next();
		return new Terminated<Element>(result, next.getTerminator());
	}

	public boolean ready() {
		final String text = getDataSource().peek(0).getElement();
		if (text.equals("{") || text.equals("{+") || text.equals("{#") || text.equals("{!") || text.equals("{-")) {
			final String text1 = getDataSource().peek(1).getElement();
			if (text1.matches("[NSEW]=")) {
				return true;
			}
			return false;
		}
		return false;
	}
}
