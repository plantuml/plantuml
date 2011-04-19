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
 * Revision $Revision: 4975 $
 *
 */
package net.sourceforge.plantuml.suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.StartUtils;
import net.sourceforge.plantuml.UmlSource;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.command.ProtectedCommand;

final public class SuggestEngine {

	private final PSystemCommandFactory systemFactory;
	private final Iterator<String> it;
	// private int nb = 0;
	private String current = "";
	private String previous = "";

	public SuggestEngine(UmlSource source, PSystemCommandFactory systemFactory) {
		this.systemFactory = systemFactory;
		this.it = source.iterator();
		if (StartUtils.isArobaseStartDiagram(next()) == false) {
			throw new UnsupportedOperationException();
		}
	}

	private boolean hasNext() {
		return it.hasNext();
	}

	private String next() {
		// nb++;
		this.previous = this.current;
		this.current = it.next();
		return current;
	}

	public SuggestEngineResult tryToSuggest() throws IOException {
		return executeUmlCommand();
	}

	private SuggestEngineResult executeUmlCommand() throws IOException {
		systemFactory.reset();
		while (hasNext()) {
			final String s = next();
			if (StartUtils.isArobaseEndDiagram(s)) {
				return SuggestEngineResult.SYNTAX_OK;
			}
			final SuggestEngineResult check = checkAndCorrect(s);
			if (check.getStatus() != SuggestEngineStatus.SYNTAX_OK) {
				return check;
			}
			final CommandControl commandControl = systemFactory.isValid(Arrays.asList(s));
			if (commandControl == CommandControl.OK_PARTIAL) {
				final boolean ok = manageMultiline(s);
				if (ok == false) {
					return SuggestEngineResult.CANNOT_CORRECT;
				}
			} else if (commandControl == CommandControl.OK) {
				final Command cmd = new ProtectedCommand(systemFactory.createCommand(Arrays.asList(s)));
				final CommandExecutionResult result = cmd.execute(Arrays.asList(s));
				if (result.isOk() == false) {
					return SuggestEngineResult.CANNOT_CORRECT;
				}
			} else {
				assert false;
			}
		}
		throw new IllegalStateException();
	}

	private boolean manageMultiline(final String init) throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(init);
		while (hasNext()) {
			final String s = next();
			if (StartUtils.isArobaseEndDiagram(s)) {
				return false;
			}
			lines.add(s);
			final CommandControl commandControl = systemFactory.isValid(lines);
			if (commandControl == CommandControl.NOT_OK) {
				throw new IllegalStateException();
			}
			if (commandControl == CommandControl.OK) {
				// final Command cmd = systemFactory.createCommand(lines);
				// return cmd.execute(lines).isOk();
				return true;
			}
		}
		return false;

	}

	SuggestEngineResult checkAndCorrect(final String incorrectLine) {
		final CommandControl commandControl = systemFactory.isValid(Arrays.asList(incorrectLine));
		if (commandControl != CommandControl.NOT_OK) {
			return SuggestEngineResult.SYNTAX_OK;
		}

		if (incorrectLine.trim().startsWith("{")
				&& systemFactory.isValid(Arrays.asList(previous + " {")) != CommandControl.NOT_OK) {
			return new SuggestEngineResult(previous + " {");
		}

		final Collection<Iterator<String>> all = new ArrayList<Iterator<String>>();
		all.add(new VariatorRemoveOneChar(incorrectLine));
		all.add(new VariatorSwapLetter(incorrectLine));
		// all.add(new VariatorAddOneCharBetweenWords(incorrectLine, ':'));
		all.add(new VariatorAddOneCharBetweenWords(incorrectLine, '-'));
		all.add(new VariatorAddOneCharBetweenWords(incorrectLine, ' '));
		// all.add(new VariatorAddTwoChar(incorrectLine, '\"'));

		for (Iterator<String> it : all) {
			final SuggestEngineResult result = tryThis(it);
			if (result != null) {
				return result;
			}
		}
		return SuggestEngineResult.CANNOT_CORRECT;
	}

	private SuggestEngineResult tryThis(Iterator<String> it) {
		while (it.hasNext()) {
			final String newS = it.next();
			if (newS.trim().length() == 0) {
				continue;
			}
			final CommandControl commandControl = systemFactory.isValid(Arrays.asList(newS));
			if (commandControl != CommandControl.NOT_OK) {
				return new SuggestEngineResult(newS);
			}
		}
		return null;

	}
}
