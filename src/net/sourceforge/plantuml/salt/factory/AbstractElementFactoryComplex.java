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

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Dictionary;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.element.Element;

public abstract class AbstractElementFactoryComplex implements ElementFactory {

	final private DataSource dataSource;
	final private Collection<ElementFactory> factories = new ArrayList<ElementFactory>();
	final private Dictionary dictionary;
	

	public AbstractElementFactoryComplex(DataSource dataSource, Dictionary dictionary) {
		this.dataSource = dataSource;
		this.dictionary = dictionary;
	}

	final public void addFactory(ElementFactory factory) {
		factories.add(factory);
	}

	protected Terminated<Element> getNextElement() {
		for (ElementFactory factory : factories) {
			if (factory.ready()) {
				return factory.create();
			}
		}
		throw new IllegalStateException(dataSource.peek(0).getElement());
	}

	protected final DataSource getDataSource() {
		return dataSource;
	}

	protected final Dictionary getDictionary() {
		return dictionary;
	}

}
