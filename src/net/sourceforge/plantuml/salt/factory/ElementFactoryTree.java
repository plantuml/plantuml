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

import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Dictionary;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.Terminator;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementTree;
import net.sourceforge.plantuml.salt.element.TableStrategy;
import net.sourceforge.plantuml.ugraphic.UFont;

public class ElementFactoryTree extends AbstractElementFactoryComplex {

	public ElementFactoryTree(DataSource dataSource, Dictionary dictionary) {
		super(dataSource, dictionary);
	}

	public Terminated<Element> create() {
		if (ready() == false) {
			throw new IllegalStateException();
		}
		final String header = getDataSource().next().getElement();
		final String textT = getDataSource().next().getElement();
		TableStrategy strategy = TableStrategy.DRAW_NONE;
		if (textT.length() == 2) {
			strategy = TableStrategy.fromChar(textT.charAt(1));
		}

		final UFont font = UFont.byDefault(12);
		final ElementTree result = new ElementTree(font, getDictionary(), strategy);

		boolean takeMe = true;
		while (getDataSource().peek(0).getElement().equals("}") == false) {
			final Terminated<String> t = getDataSource().next();
			final Terminator terminator = t.getTerminator();
			final String s = t.getElement();
			if (takeMe) {
				result.addEntry(s);
			} else {
				result.addCellToEntry(s);
			}
			takeMe = terminator == Terminator.NEWLINE;

		}
		final Terminated<String> next = getDataSource().next();
		return new Terminated<Element>(result, next.getTerminator());
	}

	public boolean ready() {
		final String text = getDataSource().peek(0).getElement();
		if (text.equals("{")) {
			final String text1 = getDataSource().peek(1).getElement();
			if (text1.equals("T")) {
				return true;
			}
			if (text1.length() == 2 && text1.startsWith("T")) {
				final char c = text1.charAt(1);
				return TableStrategy.fromChar(c) != null;

			}
			return false;
		}
		return false;
	}
}
