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
package net.sourceforge.plantuml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.command.ProtectedCommand;

final public class PSystemSingleBuilder {

	private final Iterator<String> it;
	private final UmlSource source;

	private int nb = 0;
	private AbstractPSystem sys;

	private boolean hasNext() {
		return it.hasNext();
	}

	private String next() {
		nb++;
		return it.next();
	}

	public PSystem getPSystem() {
		return sys;
	}

	public PSystemSingleBuilder(List<String> strings, PSystemFactory systemFactory) throws IOException {
		source = new UmlSource(strings);
		it = strings.iterator();
		if (next().startsWith("@startuml") == false) {
			throw new UnsupportedOperationException();
		}

		if (systemFactory instanceof PSystemCommandFactory) {
			executeUmlCommand((PSystemCommandFactory) systemFactory);
		} else if (systemFactory instanceof PSystemBasicFactory) {
			executeUmlBasic((PSystemBasicFactory) systemFactory);
		}
	}

	private void executeUmlBasic(PSystemBasicFactory systemFactory) throws IOException {
		systemFactory.reset();
		while (hasNext()) {
			final String s = next();
			if (s.equals("@enduml")) {
				if (source.getSize() == 2) {
					sys = buildEmptyError(source);
				} else {
					sys = (AbstractPSystem) systemFactory.getSystem();
				}
				if (sys == null) {
					return;
				}
				sys.setSource(source);
				return;
			}
			final boolean ok = systemFactory.executeLine(s);
			if (ok == false) {
				sys = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", nb - 1));
				return;
			}
		}
		sys = (AbstractPSystem) systemFactory.getSystem();
		sys.setSource(source);
	}

	private PSystemError buildEmptyError(UmlSource source) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR,
				"Empty description", 1));
		return result;
	}

	private void executeUmlCommand(PSystemCommandFactory systemFactory) throws IOException {
		systemFactory.reset();
		while (hasNext()) {
			final String s = next();
			if (s.equals("@enduml")) {
				if (source.getSize() == 2) {
					sys = buildEmptyError(source);
				} else {
					sys = (AbstractPSystem) systemFactory.getSystem();
				}
				if (sys == null) {
					return;
				}
				sys.setSource(source);
				return;
			}
			final CommandControl commandControl = systemFactory.isValid(Arrays.asList(s));
			if (commandControl == CommandControl.NOT_OK) {
				sys = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", nb - 1));
				return;
			} else if (commandControl == CommandControl.OK_PARTIAL) {
				final boolean ok = manageMultiline(systemFactory, s);
				if (ok == false) {
					sys = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, "Syntax Error?", nb - 1));
					return;
				}
			} else if (commandControl == CommandControl.OK) {
				final Command cmd = new ProtectedCommand(systemFactory.createCommand(Arrays.asList(s)));
				final CommandExecutionResult result = cmd.execute(Arrays.asList(s));
				if (result.isOk() == false) {
					sys = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, result.getError(),
							nb - 1));
					return;
				}
				testDeprecated(Arrays.asList(s), cmd);
			} else {
				assert false;
			}
		}
		sys = (AbstractPSystem) systemFactory.getSystem();
		sys.setSource(source);
	}

	private void testDeprecated(final List<String> lines, final Command cmd) {
		if (cmd.isDeprecated(lines)) {
			Log.error("The following syntax is deprecated :");
			for (String s : lines) {
				Log.error(s);
			}
			final String msg = cmd.getHelpMessageForDeprecated(lines);
			if (msg != null) {
				Log.error("Use instead :");
				Log.error(msg);
			}
		}
	}

	private boolean manageMultiline(PSystemCommandFactory systemFactory, final String init) throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(init);
		while (hasNext()) {
			final String s = next();
			if (s.equals("@enduml")) {
				return false;
			}
			lines.add(s);
			final CommandControl commandControl = systemFactory.isValid(lines);
			if (commandControl == CommandControl.NOT_OK) {
				throw new IllegalStateException();
			}
			if (commandControl == CommandControl.OK) {
				final Command cmd = systemFactory.createCommand(lines);
				testDeprecated(lines, cmd);
				return cmd.execute(lines).isOk();
			}
		}
		return false;

	}

}
