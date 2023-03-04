/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.cucadiagram;

import java.io.IOException;
import java.util.Collection;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.Annotated;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.abel.EntityFactory;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.StyleBuilder;

public interface ICucaDiagram extends GroupHierarchy, PortionShower, Annotated {

	ISkinParam getSkinParam();

	UmlDiagramType getUmlDiagramType();

	EntityFactory getEntityFactory();

	StyleBuilder getCurrentStyleBuilder();

	boolean isHideEmptyDescriptionForState();

	Collection<Link> getLinks();

	Pragma getPragma();

	long seed();

	String getMetadata();

	String getFlashData();

	ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException;

	String getNamespaceSeparator();

	UmlSource getSource();

	String[] getDotStringSkek();

	// boolean isAutarkic(Entity g);

	int getUniqueSequence();

}
