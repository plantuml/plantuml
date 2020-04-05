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
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandAllowMixing;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClass;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateElementFull2.Mode;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow2;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLayoutNewLine;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.classdiagram.command.CommandRemoveRestore;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandNamespace;
import net.sourceforge.plantuml.command.CommandNamespace2;
import net.sourceforge.plantuml.command.CommandNamespaceEmpty;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPackageEmpty;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.CommandConstraintOnLinks;
import net.sourceforge.plantuml.command.note.CommandFactoryNote;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteOnEntity;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteOnLink;
import net.sourceforge.plantuml.command.note.CommandFactoryTipOnEntity;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementMultilines;
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementParenthesis;
import net.sourceforge.plantuml.descdiagram.command.CommandNewpage;
import net.sourceforge.plantuml.descdiagram.command.CommandPackageWithUSymbol;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObject;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateEntityObjectMultilines;
import net.sourceforge.plantuml.objectdiagram.command.CommandCreateMap;

public class ClassDiagramFactory extends UmlDiagramFactory {

	private final ISkinSimple skinParam;

	public ClassDiagramFactory(ISkinSimple skinParam) {
		this.skinParam = skinParam;
	}

	@Override
	public ClassDiagram createEmptyDiagram() {
		return new ClassDiagram(skinParam);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());

		cmds.add(new CommandRankDir());
		cmds.add(new CommandNewpage(this));

		cmds.add(new CommandPage());
		cmds.add(new CommandAddMethod());

		addCommonHides(cmds);
		cmds.add(new CommandHideShow2());

		cmds.add(new CommandRemoveRestore());
		cmds.add(new CommandCreateClassMultilines());
		cmds.add(new CommandCreateEntityObjectMultilines());
		cmds.add(new CommandCreateMap());
		cmds.add(new CommandCreateClass());
		cmds.add(new CommandCreateEntityObject());

		cmds.add(new CommandAllowMixing());
		cmds.add(new CommandCreateElementParenthesis());
		cmds.add(new CommandLayoutNewLine());

		cmds.add(new CommandPackage());
		cmds.add(new CommandEndPackage());
		cmds.add(new CommandPackageEmpty());
		cmds.add(new CommandPackageWithUSymbol());

		cmds.add(new CommandCreateElementFull2(Mode.NORMAL_KEYWORD));
		cmds.add(new CommandCreateElementFull2(Mode.WITH_MIX_PREFIX));
		final CommandFactoryNote factoryNoteCommand = new CommandFactoryNote();
		cmds.add(factoryNoteCommand.createSingleLine());

		cmds.add(new CommandNamespace());
		cmds.add(new CommandNamespace2());
		cmds.add(new CommandNamespaceEmpty());
		cmds.add(new CommandStereotype());

		cmds.add(new CommandLinkClass(UmlDiagramType.CLASS));
		cmds.add(new CommandLinkLollipop(UmlDiagramType.CLASS));

		cmds.add(new CommandImport());

		final CommandFactoryTipOnEntity factoryTipOnEntityCommand = new CommandFactoryTipOnEntity("a", new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE_NO_DOTDOT + "|[%g][^%g]+[%g])::([%g][^%g]+[%g]|[^%s]+)"));
		cmds.add(factoryTipOnEntityCommand.createMultiLine(true));
		cmds.add(factoryTipOnEntityCommand.createMultiLine(false));

		final CommandFactoryNoteOnEntity factoryNoteOnEntityCommand = new CommandFactoryNoteOnEntity("class",
				new RegexLeaf("ENTITY", "(" + CommandCreateClass.CODE + "|[%g][^%g]+[%g])"));
		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		cmds.add(new CommandUrl());

		cmds.add(factoryNoteOnEntityCommand.createMultiLine(true));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(false));
		cmds.add(factoryNoteCommand.createMultiLine(false));

		final CommandFactoryNoteOnLink factoryNoteOnLinkCommand = new CommandFactoryNoteOnLink();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));
		cmds.add(new CommandConstraintOnLinks());

		cmds.add(new CommandDiamondAssociation());

		cmds.add(new CommandNamespaceSeparator());

		cmds.add(new CommandCreateElementMultilines(0));
		cmds.add(new CommandCreateElementMultilines(1));
		addTitleCommands(cmds);
		addCommonCommands2(cmds);

		return cmds;
	}
}
