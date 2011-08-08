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
 * Revision $Revision: 6918 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateEntityClass2;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateEntityClassMultilines2;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandEndNamespace;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow3;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass2;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop2;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesClassNote;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespace;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandCreateNote;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.command.CommandNoteEntity;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPage;

public class ClassDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ClassDiagram system;

	public ClassDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ClassDiagram();

		addCommonCommands(system);

		addCommand(new CommandPage(system));
		addCommand(new CommandAddMethod(system));

		//addCommand(new CommandCreateEntityClass(system));
		addCommand(new CommandCreateEntityClass2(system));
		addCommand(new CommandCreateNote(system));
		
		addCommand(new CommandPackage(system));
		addCommand(new CommandEndPackage(system));
		addCommand(new CommandNamespace(system));
		addCommand(new CommandEndNamespace(system));
		addCommand(new CommandStereotype(system));

		//addCommand(new CommandLinkClass(system));
		addCommand(new CommandLinkClass2(system));
		addCommand(new CommandLinkLollipop2(system));

		addCommand(new CommandImport(system));
		addCommand(new CommandNoteEntity(system));
		addCommand(new CommandUrl(system));

		addCommand(new CommandMultilinesClassNote(system));
		addCommand(new CommandMultilinesStandaloneNote(system));
//		addCommand(new CommandCreateEntityClassMultilines(system));
		addCommand(new CommandCreateEntityClassMultilines2(system));

		addCommand(new CommandDiamondAssociation(system));

		addCommand(new CommandHideShow3(system));
		addCommand(new CommandHideShow(system));

	}
}
