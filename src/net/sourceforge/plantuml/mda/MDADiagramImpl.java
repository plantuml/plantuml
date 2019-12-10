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
package net.sourceforge.plantuml.mda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.api.mda.option2.MDADiagram;
import net.sourceforge.plantuml.api.mda.option2.MDAPackage;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;

public class MDADiagramImpl implements MDADiagram {

	public static MDADiagram create(String uml) {
		List<BlockUml> blocks = new SourceStringReader(uml).getBlocks();
		if (blocks.size() == 0) {
			uml = "@startuml\n" + uml + "\n@enduml";
			blocks = new SourceStringReader(uml).getBlocks();
			if (blocks.size() == 0) {
				return null;
			}
		}
		final BlockUml block = blocks.get(0);
		final Diagram diagram = block.getDiagram();
		if (diagram instanceof ClassDiagram) {
			return new MDADiagramImpl((ClassDiagram) diagram);
		}
		return null;
	}

	private final Collection<MDAPackage> packages = new ArrayList<MDAPackage>();

	private MDADiagramImpl(ClassDiagram classDiagram) {
		final EntityFactory entityFactory = classDiagram.getEntityFactory();
		packages.add(new MDAPackageImpl(entityFactory.getRootGroup()));
		for (IGroup group : entityFactory.groups()) {
			packages.add(new MDAPackageImpl(group));
		}
	}

	public Collection<MDAPackage> getPackages() {
		return Collections.unmodifiableCollection(packages);
	}

}
