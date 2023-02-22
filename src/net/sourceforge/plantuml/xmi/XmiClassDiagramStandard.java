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
import net.sourceforge.plantuml.baraye.IGroup;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class XmiClassDiagramStandard extends XmiClassDiagramAbstract implements XmlDiagramTransformer {
	private int renderPackage(String name) {
		s.push(document.createElement("UML:Package"));
		s.peek().setAttribute("xmi.id", "pkg" + classDiagram.getUniqueSequence());
		s.peek().setAttribute("name", name);
		return 1;
	}

	/*
     *       <UML:Namespace.ownedElement>
     *         <UML:Package xmi.id='11A2' name='a'>
     *           <UML:Namespace.ownedElement>
     *             <UML:Class xmi.id='cl0001' name='ClassName'>
     */
	private int renderEntity(IEntity entity) {
		int levels = 0;

		//for (final IEntity ent : classDiagram.getLeafsvalues()) {
		//for (final Entity ent : classDiagram.getEntityFactory().leafs()) {
		if (entity == null) {
			throw new NullPointerException("oops, entity is null");//levels += renderPackage()
		}

		LeafType leafType = entity.getLeafType();

		// This may or may not be needed. It's possible for a logical hierarchy to not match a
		// naming hierarchy.  If that's not the case in this code then please remove this check
		// and always add the UML:Namespace
		String name = entity.getDisplay().get(0).toString();
		if (name != null && name != "") {
			s.push(document.createElement("UML:Namespace.ownedElement"));
			++levels;
		}

		switch (leafType) {
		case EMPTY_PACKAGE:
			levels += renderPackage(name);

		//	return levels;
		default:
			levels += addEntityNode(entity);
		}

		return levels;
	}

	private void renderGroup(IEntity entity, IGroup parent) {
		int levels = 0;
		IGroup group = null;

		if (entity != null)
			levels = renderEntity(entity);

		if (entity.isGroup())
			group = (IGroup) entity;

		for (final IEntity child : group.getLeafsDirect()) {

			// UGLY: Stealing logic from EntityFactory.leafs(), breaking encapsulation.
			//if (entity == null && entity.isGroup())
			//	continue;
			renderGroup(child, group);
		}

		for (int i = 0; i < levels; ++i)
			s.pop();
	}

	public XmiClassDiagramStandard(ClassDiagram classDiagram) throws ParserConfigurationException {
		super(classDiagram);
		IGroup rootGroup = classDiagram.getEntityFactory().getRootGroup();
		for (ILeaf leaf : rootGroup.getLeafsDirect()) {
			renderGroup(leaf, rootGroup);
		}
		//classDiagram.getRootGroup()classDiagram.leaf

		//for (final IEntity ent : classDiagram.getLeafsvalues()) {}
		//getLeafsDirect
		//renderQuark(.getQuark());

		// if (fileFormat != FileFormat.XMI_STANDARD) {
		// for (final Link link : classDiagram.getLinks()) {
		// addLink(link);
		// }
		// }
	}

	/*
	for (final IEntity ent : classDiagram.getLeafsvalues()) {
		-			// if (fileFormat == FileFormat.XMI_ARGO && isStandalone(ent) == false) {
		-			// continue;
		-			// }
		 			final Element cla = createEntityNode(ent);
		 			if (cla == null) {
		 				continue;

final String parentCode = entity.getQuark().getParent().toStringPoint();
-
-					if (parentCode.length() == 0)
-						cla.setAttribute("namespace", CucaDiagramXmiMaker.getModel(classDiagram));
-					else
-						cla.setAttribute("namespace", parentCode);
-
-					break;
-				default:
-					continue;
-				}
-
-			final Element cla = createEntityNode(ent);
-			if (cla == null) {
-				continue;
-			}
-			ownedElement.appendChild(cla);
-			done.add(ent);
*/
	// private boolean isStandalone(IEntity ent) {
	// for (final Link link : classDiagram.getLinks()) {
	// if (link.getEntity1() == ent || link.getEntity2() == ent) {
	// return false;
	// }
	// }
	// return true;
	// }

}
