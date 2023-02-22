/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.xmi;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import net.sourceforge.plantuml.baraye.Entity;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.plasma.Quark;

public class XmiClassDiagramStandard extends XmiClassDiagramAbstract implements XmlDiagramTransformer {
	/*
     * <UML:Package xmi.id='pkg001' name='name'>
     *   <UML:Namespace.ownedElement>
     */
	private int renderPackage(String name) {
		s.push(document.createElement("UML:Package"));
		s.peek().setAttribute("xmi.id", "pkg" + classDiagram.getUniqueSequence());
		s.peek().setAttribute("name", name);
		s.pushNamespace();
		return 2;
	}

	private int renderEntity(Entity entity) {
		LeafType leafType = entity.getLeafType();
		int levels = 0;

		if (leafType == null || leafType == LeafType.EMPTY_PACKAGE) {
			levels += renderPackage(entity.getName());
			return levels;
		}

		switch (leafType) {
		default:
			break;
		}

		levels += addEntityNode(entity);
		return levels;
	}

	private void renderQuark(Quark<Entity> quark) {
		int levels = 0;

		// FIXME: Maybe move UML:Model generation here?
		if (quark.isRoot()) {
			// parent = "";
		} else {
			Entity entity = quark.getData();
			if (entity == null) {
				throw new RuntimeException("oops");//levels += renderPackage()
			}
			levels = renderEntity(quark.getData());
			//parent += "." + "s";
			done.add(entity);
		}

		for (final Quark<Entity> child : quark.getChildren()) {
			Entity entity = child.getData();

			// UGLY: Stealing logic from EntityFactory.leafs(), breaking encapsulation.
			if (entity == null /* && entity.isGroup()*/)
				continue;

			renderQuark(child);
		}

		for (int i = 0; i < levels; ++i)
			s.pop();
	}

	public XmiClassDiagramStandard(ClassDiagram classDiagram) throws ParserConfigurationException {
		super(classDiagram);
		renderQuark(classDiagram.getEntityFactory().getRootGroup().getQuark());
	}
}
