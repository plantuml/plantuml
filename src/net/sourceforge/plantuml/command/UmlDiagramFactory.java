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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByGender;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByVisibility;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.statediagram.command.CommandHideEmptyDescription;
import net.sourceforge.plantuml.style.CommandStyleImport;
import net.sourceforge.plantuml.style.CommandStyleMultilinesCSS;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class UmlDiagramFactory extends PSystemAbstractFactory {

	private List<Command> cmds;

	protected UmlDiagramFactory() {
		this(DiagramType.UML);
	}

	protected UmlDiagramFactory(DiagramType type) {
		super(type);
	}

	final public Diagram createSystem(UmlSource source) {
		final IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine.getString()) == false) {
			throw new UnsupportedOperationException();
		}

		if (source.isEmpty()) {
			if (it.hasNext()) {
				it.next();
			}
			return buildEmptyError(source, startLine.getLocation(), it.getTrace());
		}
		AbstractPSystem sys = createEmptyDiagram();

		while (it.hasNext()) {
			if (StartUtils.isArobaseEndDiagram(it.peek().getString())) {
				if (sys == null) {
					return null;
				}
				final String err = sys.checkFinalError();
				if (err != null) {
					final LineLocation location = it.next().getLocation();
					return buildExecutionError(source, err, location, it.getTrace());
				}
				if (source.getTotalLineCount() == 2) {
					final LineLocation location = it.next().getLocation();
					return buildEmptyError(source, location, it.getTrace());
				}
				sys.makeDiagramReady();
				if (sys.isOk() == false) {
					return null;
				}
				sys.setSource(source);
				return sys;
			}
			sys = executeFewLines(sys, source, it);
			if (sys instanceof PSystemError) {
				return sys;
			}
		}
		sys.setSource(source);
		return sys;

	}

	private AbstractPSystem executeFewLines(AbstractPSystem sys, UmlSource source, final IteratorCounter2 it) {
		final Step step = getCandidate(it);
		if (step == null) {
			final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", it.peek().getLocation());
			it.next();
			return PSystemErrorUtils.buildV2(source, err, null, it.getTrace());
		}

		final CommandExecutionResult result = sys.executeCommand(step.command, step.blocLines);
		if (result.isOk() == false) {
			final ErrorUml err = new ErrorUml(ErrorUmlType.EXECUTION_ERROR, result.getError(),
					((StringLocated) step.blocLines.getFirst()).getLocation());
			sys = PSystemErrorUtils.buildV2(source, err, result.getDebugLines(), it.getTrace());
		}
		if (result.getNewDiagram() != null) {
			sys = result.getNewDiagram();
		}
		return sys;

	}

	static class Step {
		final Command command;
		final BlocLines blocLines;

		Step(Command command, BlocLines blocLines) {
			this.command = command;
			this.blocLines = blocLines;
		}

	}

	private Step getCandidate(final IteratorCounter2 it) {
		final BlocLines single = BlocLines.single(it.peek());
		if (cmds == null) {
			cmds = createCommands();
		}
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(single);
			if (result == CommandControl.OK) {
				it.next();
				return new Step(cmd, single);
			}
			if (result == CommandControl.OK_PARTIAL) {
				final IteratorCounter2 cloned = it.cloneMe();
				final BlocLines lines = isMultilineCommandOk(cloned, cmd);
				if (lines == null) {
					continue;
				}
				it.copyStateFrom(cloned);
				assert lines != null;
				return new Step(cmd, lines);
			}
		}
		return null;
	}

	private BlocLines isMultilineCommandOk(IteratorCounter2 it, Command cmd) {
		BlocLines lines = new BlocLines();
		int nb = 0;
		while (it.hasNext()) {
			lines = addOneSingleLineManageEmbedded2(it, lines);
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.NOT_OK) {
				return null;
			}
			if (result == CommandControl.OK) {
				return lines;
			}
			nb++;
			if (cmd instanceof CommandDecoratorMultine && nb > ((CommandDecoratorMultine) cmd).getNbMaxLines()) {
				return null;
			}
		}
		return null;
	}

	private BlocLines addOneSingleLineManageEmbedded2(IteratorCounter2 it, BlocLines lines) {
		final StringLocated linetoBeAdded = it.next();
		lines = lines.add(linetoBeAdded);
		if (linetoBeAdded.getTrimmed().getString().equals("{{")) {
			while (it.hasNext()) {
				final StringLocated s = it.next();
				lines = lines.add(s);
				if (s.getTrimmed().getString().equals("}}")) {
					return lines;
				}
			}
		}
		return lines;
	}

	// -----------------------------------

	protected abstract List<Command> createCommands();

	public abstract AbstractPSystem createEmptyDiagram();

	final protected void addCommonCommands1(List<Command> cmds) {
		addTitleCommands(cmds);
		addCommonCommands2(cmds);
		addCommonHides(cmds);
		cmds.add(new CommandStyleMultilinesCSS());
		cmds.add(new CommandStyleImport());
	}

	final protected void addCommonCommands2(List<Command> cmds) {
		// cmds.add(new CommandListSprite());
		cmds.add(new CommandNope());
		cmds.add(new CommandPragma());

		cmds.add(new CommandSkinParam());
		cmds.add(new CommandSkinParamMultilines());
		cmds.add(new CommandSkin());
		cmds.add(new CommandMinwidth());
		cmds.add(new CommandRotate());
		cmds.add(new CommandScale());
		cmds.add(new CommandScaleWidthAndHeight());
		cmds.add(new CommandScaleWidthOrHeight());
		cmds.add(new CommandScaleMaxWidth());
		cmds.add(new CommandScaleMaxHeight());
		cmds.add(new CommandScaleMaxWidthAndHeight());
		cmds.add(new CommandAffineTransform());
		cmds.add(new CommandAffineTransformMultiline());
		final CommandFactorySprite factorySpriteCommand = new CommandFactorySprite();
		cmds.add(factorySpriteCommand.createMultiLine(false));
		cmds.add(factorySpriteCommand.createSingleLine());
		cmds.add(new CommandSpriteFile());
	}

	final protected void addCommonHides(List<Command> cmds) {
		cmds.add(new CommandHideEmptyDescription());
		cmds.add(new CommandHideUnlinked());
		cmds.add(new CommandHideShowByVisibility());
		cmds.add(new CommandHideShowByGender());
	}

	final protected void addTitleCommands(List<Command> cmds) {
		cmds.add(new CommandTitle());
		cmds.add(new CommandMainframe());
		cmds.add(new CommandCaption());
		cmds.add(new CommandMultilinesCaption());
		cmds.add(new CommandMultilinesTitle());
		cmds.add(new CommandMultilinesLegend());

		cmds.add(new CommandFooter());
		cmds.add(new CommandMultilinesFooter());

		cmds.add(new CommandHeader());
		cmds.add(new CommandMultilinesHeader());
	}

	final public List<String> getDescription() {
		final List<String> result = new ArrayList<String>();
		for (Command cmd : createCommands()) {
			result.addAll(Arrays.asList(cmd.getDescription()));
		}
		return Collections.unmodifiableList(result);

	}

}
