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
package net.sourceforge.plantuml.salt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.factory.AbstractElementFactoryComplex;
import net.sourceforge.plantuml.salt.factory.ElementFactory;
import net.sourceforge.plantuml.salt.factory.ElementFactoryBorder;
import net.sourceforge.plantuml.salt.factory.ElementFactoryButton;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryDroplist;
import net.sourceforge.plantuml.salt.factory.ElementFactoryImage;
import net.sourceforge.plantuml.salt.factory.ElementFactoryLine;
import net.sourceforge.plantuml.salt.factory.ElementFactoryMenu;
import net.sourceforge.plantuml.salt.factory.ElementFactoryPyramid;
import net.sourceforge.plantuml.salt.factory.ElementFactoryPyramid2;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRetrieveFromDictonnary;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTab;
import net.sourceforge.plantuml.salt.factory.ElementFactoryText;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTextField;

public class SaltUtils {

	public static Element createElement(List<String> data) {
		final DataSourceImpl source = new DataSourceImpl(data);

		final Collection<AbstractElementFactoryComplex> cpx = new ArrayList<AbstractElementFactoryComplex>();
		
		final Dictionary dictionnary = new Dictionary();

		// cpx.add(new ElementFactorySimpleFrame(source, dictionnary));
		cpx.add(new ElementFactoryPyramid2(source, dictionnary));
		cpx.add(new ElementFactoryBorder(source, dictionnary));

		for (AbstractElementFactoryComplex f : cpx) {
			addSimpleFactory(f, source, dictionnary);
		}
		for (AbstractElementFactoryComplex f1 : cpx) {
			for (AbstractElementFactoryComplex f2 : cpx) {
				f1.addFactory(f2);
			}
		}

		for (ElementFactory f : cpx) {
			if (f.ready()) {
				return f.create().getElement();
			}
		}

		System.err.println("data="+data);
		throw new IllegalArgumentException();

	}

	private static void addSimpleFactory(final AbstractElementFactoryComplex cpxFactory, final DataSource source,
			Dictionary dictionnary) {
		cpxFactory.addFactory(new ElementFactoryMenu(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryTab(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryLine(source));
		cpxFactory.addFactory(new ElementFactoryTextField(source));
		cpxFactory.addFactory(new ElementFactoryButton(source));
		cpxFactory.addFactory(new ElementFactoryDroplist(source));
		cpxFactory.addFactory(new ElementFactoryRadioOn(source));
		cpxFactory.addFactory(new ElementFactoryRadioOff(source));
		cpxFactory.addFactory(new ElementFactoryCheckboxOn(source));
		cpxFactory.addFactory(new ElementFactoryCheckboxOff(source));
		cpxFactory.addFactory(new ElementFactoryImage(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryRetrieveFromDictonnary(source, dictionnary));
		cpxFactory.addFactory(new ElementFactoryText(source));
	}

}
