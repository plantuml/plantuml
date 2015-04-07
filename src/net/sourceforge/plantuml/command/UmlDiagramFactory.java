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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow3;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.suggest.SuggestEngine;
import net.sourceforge.plantuml.suggest.SuggestEngineResult;
import net.sourceforge.plantuml.suggest.SuggestEngineStatus;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter;

public abstract class UmlDiagramFactory extends PSystemAbstractFactory {

	private final List<Command> cmds;

	protected UmlDiagramFactory() {
		this(DiagramType.UML);
	}

	protected UmlDiagramFactory(DiagramType type) {
		super(type);
		cmds = createCommands();
	}

	final public Diagram createSystem(UmlSource source) {
		final IteratorCounter it = source.iterator();
		final String startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}

		if (source.isEmpty()) {
			return buildEmptyError(source);
		}
		AbstractPSystem sys = createEmptyDiagram();

		while (it.hasNext()) {
			final String line = it.next();
			if (StartUtils.isArobaseEndDiagram(line)) {
				final String err = checkFinalError(sys);
				if (err != null) {
					return buildEmptyError(source, err);
				}
				if (source.getTotalLineCount() == 2) {
					return buildEmptyError(source);
				}
				if (sys == null) {
					return null;
				}
				sys.makeDiagramReady();
				if (sys.isOk() == false) {
					return null;
				}
				sys.setSource(source);
				return sys;
			}
			sys = executeOneLine(sys, source, it, line);
			if (sys instanceof PSystemError) {
				return sys;
			}
		}
		sys.setSource(source);
		return sys;

	}

	private AbstractPSystem executeOneLine(AbstractPSystem sys, UmlSource source, final IteratorCounter it,
			final String line) {
		final CommandControl commandControl = isValid(Arrays.asList(line));
		if (commandControl == CommandControl.NOT_OK) {
			final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", it.currentNum() - 1);
			if (OptionFlags.getInstance().isUseSuggestEngine()) {
				final SuggestEngine engine = new SuggestEngine(source, this);
				final SuggestEngineResult result = engine.tryToSuggest(sys);
				if (result.getStatus() == SuggestEngineStatus.ONE_SUGGESTION) {
					err.setSuggest(result);
				}
			}
			sys = new PSystemError(source, err);
		} else if (commandControl == CommandControl.OK_PARTIAL) {
			final boolean ok = manageMultiline(sys, line, it);
			if (ok == false) {
				sys = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, "Syntax Error?",
						it.currentNum() - 1));

			}
		} else if (commandControl == CommandControl.OK) {
			Command cmd = createCommand(Arrays.asList(line));
			final CommandExecutionResult result = sys.executeCommand(cmd, Arrays.asList(line));
			if (result.isOk() == false) {
				sys = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, result.getError(),
						it.currentNum() - 1));
			}
			if (result.getNewDiagram() != null) {
				sys = result.getNewDiagram();
			}
		} else {
			assert false;
		}
		return sys;
	}

	private boolean manageMultiline(AbstractPSystem system, final String init, IteratorCounter it) {
		final List<String> lines = new ArrayList<String>();
		addOneSingleLineManageEmbedded(lines, init, it);
		while (it.hasNext()) {
			final String s = it.next();
			if (StartUtils.isArobaseEndDiagram(s)) {
				return false;
			}
			addOneSingleLineManageEmbedded(lines, s, it);
			final CommandControl commandControl = isValid(lines);
			if (commandControl == CommandControl.NOT_OK) {
				// throw new IllegalStateException();
				return false;
			}
			if (commandControl == CommandControl.OK) {
				final Command cmd = createCommand(lines);
				final CommandExecutionResult result = system.executeCommand(cmd, lines);
				return result.isOk();
			}
		}
		return false;

	}

	private void addOneSingleLineManageEmbedded(final List<String> lines, final String linetoBeAdded, IteratorCounter it) {
		lines.add(linetoBeAdded);
		if (linetoBeAdded.trim().equals("{{")) {
			while (it.hasNext()) {
				final String s = it.next();
				lines.add(s);
				if (s.trim().equals("}}")) {
					return;
				}
			}
		}
	}

	// -----------------------------------

	public String checkFinalError(AbstractPSystem system) {
		return null;
	}

	final public CommandControl isValid(List<String> lines) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK || result == CommandControl.OK_PARTIAL) {
				return result;
			}
		}
		return CommandControl.NOT_OK;

	}

	final public Command createCommand(List<String> lines) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK) {
				return cmd;
			} else if (result == CommandControl.OK_PARTIAL) {
				throw new IllegalArgumentException();
			}
		}
		throw new IllegalArgumentException();
	}

	protected abstract List<Command> createCommands();

	public abstract AbstractPSystem createEmptyDiagram();

	final protected void addCommonCommands(List<Command> cmds) {
		cmds.add(new CommandNope());
		cmds.add(new CommandComment());
		cmds.add(new CommandMultilinesComment());
		cmds.add(new CommandPragma());
		cmds.add(new CommandTitle());
		cmds.add(new CommandMultilinesTitle());
		cmds.add(new CommandMultilinesLegend());

		cmds.add(new CommandFooter());
		cmds.add(new CommandMultilinesFooter());

		cmds.add(new CommandHeader());
		cmds.add(new CommandMultilinesHeader());

		cmds.add(new CommandSkinParam());
		cmds.add(new CommandSkinParamMultilines());
		cmds.add(new CommandMinwidth());
		cmds.add(new CommandRotate());
		cmds.add(new CommandScale());
		cmds.add(new CommandScaleWidthAndHeight());
		cmds.add(new CommandScaleWidthOrHeight());
		cmds.add(new CommandAffineTransform());
		cmds.add(new CommandAffineTransformMultiline());
		cmds.add(new CommandHideUnlinked());
		final FactorySpriteCommand factorySpriteCommand = new FactorySpriteCommand();
		cmds.add(factorySpriteCommand.createMultiLine());
		cmds.add(factorySpriteCommand.createSingleLine());
		cmds.add(new CommandSpriteFile());
		
		cmds.add(new CommandHideShow3());
		cmds.add(new CommandHideShow());


	}

	final public List<String> getDescription() {
		final List<String> result = new ArrayList<String>();
		for (Command cmd : createCommands()) {
			result.addAll(Arrays.asList(cmd.getDescription()));
		}
		return Collections.unmodifiableList(result);

	}

}
