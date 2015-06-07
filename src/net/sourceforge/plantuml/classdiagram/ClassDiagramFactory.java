/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 10006 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandAllowMixing;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClass;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2.Mode;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowSpecificClass;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLayoutNewLine;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandNamespace;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPackageEmpty;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.note.FactoryTipOnEntityCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.cucadiagram.Link;

public class ClassDiagramFactory extends UmlDiagramFactory {

	@Override
	public ClassDiagram createEmptyDiagram() {
		return new ClassDiagram();

	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());
		addCommonCommands(cmds);

		cmds.add(new CommandRankDir());
		cmds.add(new CommandPage());
		cmds.add(new CommandAddMethod());

		cmds.add(new CommandCreateClass());
		cmds.add(new CommandAllowMixing());
		cmds.add(new CommandLayoutNewLine());

		cmds.add(new CommandCreateElementFull2(Mode.NORMAL_KEYWORD));
		cmds.add(new CommandCreateElementFull2(Mode.WITH_MIX_PREFIX));
		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		cmds.add(factoryNoteCommand.createSingleLine());

		cmds.add(new CommandPackage());
		cmds.add(new CommandEndPackage());
		cmds.add(new CommandPackageEmpty());

		cmds.add(new CommandNamespace());
		cmds.add(new CommandStereotype());

		cmds.add(new CommandLinkClass(UmlDiagramType.CLASS));
		cmds.add(new CommandLinkLollipop(UmlDiagramType.CLASS));

		cmds.add(new CommandImport());

		final FactoryTipOnEntityCommand factoryTipOnEntityCommand = new FactoryTipOnEntityCommand(new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE_NO_DOTDOT + "|[%g][^%g]+[%g])::([^%s]+)"));
		cmds.add(factoryTipOnEntityCommand.createMultiLine());

		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE + "|[%g][^%g]+[%g])"));
		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		cmds.add(new CommandUrl());

		cmds.add(factoryNoteOnEntityCommand.createMultiLine());
		cmds.add(factoryNoteCommand.createMultiLine());

		cmds.add(new CommandCreateClassMultilines());

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine());

		cmds.add(new CommandDiamondAssociation());

		cmds.add(new CommandHideShowSpecificClass());

		cmds.add(new CommandNamespaceSeparator());

		return cmds;
	}

	@Override
	public String checkFinalError(AbstractPSystem sys) {
		final ClassDiagram system = (ClassDiagram) sys;

		for (Link link : system.getLinks()) {
			final int len = link.getLength();
			if (len == 1) {
				for (Link link2 : system.getLinks()) {
					if (link2.sameConnections(link) && link2.getLength() != 1) {
						link2.setLength(1);
					}
				}
			}
		}

		system.applySingleStrategy();

		// for (IGroup g : system.getGroups(true)) {
		// final List<ILeaf> standalones = new ArrayList<ILeaf>();
		// for (ILeaf ent : g.getLeafsDirect()) {
		// if (system.isStandalone(ent)) {
		// standalones.add(ent);
		// }
		// }
		// if (standalones.size() < 3) {
		// continue;
		// }
		// final Magma magma = new Magma(system, standalones);
		// magma.putInSquare();
		// }
		return super.checkFinalError(system);
	}

}
