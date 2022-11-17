
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package net.sourceforge.plantuml.genericdiagram.cucaprocessing.impl;

import net.sourceforge.plantuml.baraye.CucaDiagram;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.Factory;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaDiagramVisitor;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaDiagramWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaGroupWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaLeafWrapper;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.ICucaLinkWrapper;

import java.util.List;
import java.util.stream.Collectors;

public class CucaDiagramWrapper implements ICucaDiagramWrapper {

	public CucaDiagram getDiagram() {
		return diagram;
	}

	CucaDiagram diagram;

	public CucaDiagramWrapper(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	@Override
	public List<ICucaLinkWrapper> getLinks(){
		return diagram.getLinks().stream()
						.map(l -> Factory.getInstance().createLinkWrapper(l))
						.collect(Collectors.toList());
	}

	@Override
	public List<ICucaLeafWrapper> getLeafs(){
		return diagram.getLeafsvalues().stream()
						.map(l -> Factory.getInstance().createLeafWrapper(l))
						.collect(Collectors.toList());
	}

	@Override
	public List<ICucaGroupWrapper> getGroups(){
		return diagram.getGroups(false).stream()
						.map(g -> Factory.getInstance().createGroupWrapper(g))
						.collect(Collectors.toList());
	}

	@Override
	public void acceptVisitor(ICucaDiagramVisitor visitor) {
		visitor.visitCucaDiagram(this);
	}


}
