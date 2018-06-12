/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.suggest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;
import net.sourceforge.plantuml.version.IteratorCounter2Impl;

final public class SuggestEngine {

	private static final int LIMIT = 120;

	private final UmlDiagramFactory systemFactory;

	private final IteratorCounter2 it99;

	public SuggestEngine(UmlSource source, Object foo) {
		throw new UnsupportedOperationException();
	}

	public SuggestEngine(UmlSource source, UmlDiagramFactory systemFactory) {
		this.systemFactory = systemFactory;
		this.it99 = source.iterator2();
		final CharSequence startLine = it99.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}
	}

	public SuggestEngineResult tryToSuggest(AbstractPSystem system) {
		return executeUmlCommand(system);
	}

	private SuggestEngineResult executeUmlCommand(AbstractPSystem system) {
		throw new UnsupportedOperationException();
//		while (it99.hasNext()) {
//			if (StartUtils.isArobaseEndDiagram(it99.peek())) {
//				return SuggestEngineResult.SYNTAX_OK;
//			}
//			final SuggestEngineResult check = checkAndCorrect();
//			if (check.getStatus() != SuggestEngineStatus.SYNTAX_OK) {
//				return check;
//			}
//			final CommandControl commandControl = systemFactory.isValid2(it99);
//			if (commandControl == CommandControl.OK_PARTIAL) {
//				systemFactory.goForwardMultiline(it99);
//				// if (ok == false) {
//				// return SuggestEngineResult.CANNOT_CORRECT;
//				// }
//			} else if (commandControl == CommandControl.OK) {
//				it99.next();
//				// final Command cmd = new ProtectedCommand(systemFactory.createCommand(Arrays.asList(s)));
//				// final CommandExecutionResult result = cmd.execute(system, Arrays.asList(s));
//				// if (result.isOk() == false) {
//				// return SuggestEngineResult.CANNOT_CORRECT;
//				// }
//			} else {
//				return SuggestEngineResult.CANNOT_CORRECT;
//			}
//		}
//		return SuggestEngineResult.CANNOT_CORRECT;
	}

	SuggestEngineResult checkAndCorrect() {
		throw new UnsupportedOperationException();
//		final String incorrectLine = it99.peek().toString();
//		if (incorrectLine.length() > LIMIT) {
//			return SuggestEngineResult.CANNOT_CORRECT;
//		}
//		final CommandControl commandControl = systemFactory.isValid2(it99);
//		if (commandControl != CommandControl.NOT_OK) {
//			return SuggestEngineResult.SYNTAX_OK;
//		}
//
//		if (StringUtils.trin(incorrectLine).startsWith("{")
//				&& systemFactory.isValid(BlocLines.single(it99.peekPrevious() + " {")) != CommandControl.NOT_OK) {
//			return new SuggestEngineResult(it99.peekPrevious() + " {");
//		}
//
//		final Collection<Iterator<String>> all = new ArrayList<Iterator<String>>();
//		all.add(new VariatorRemoveOneChar(incorrectLine));
//		all.add(new VariatorSwapLetter(incorrectLine));
//		// all.add(new VariatorAddOneCharBetweenWords(incorrectLine, ':'));
//		all.add(new VariatorAddOneCharBetweenWords(incorrectLine, '-'));
//		all.add(new VariatorAddOneCharBetweenWords(incorrectLine, ' '));
//		// all.add(new VariatorAddTwoChar(incorrectLine, '\"'));
//
//		for (Iterator<String> it2 : all) {
//			final SuggestEngineResult result = tryThis(it2);
//			if (result != null) {
//				return result;
//			}
//		}
//		return SuggestEngineResult.CANNOT_CORRECT;
	}

	private SuggestEngineResult tryThis(Iterator<String> it2) {
		throw new UnsupportedOperationException();
//		while (it2.hasNext()) {
//			final String newS = it2.next();
//			if (StringUtils.trin(newS).length() == 0) {
//				continue;
//			}
//			final CommandControl commandControl = systemFactory.isValid2(replaceFirstLine(newS));
//			if (commandControl == CommandControl.OK) {
//				return new SuggestEngineResult(newS);
//			}
//		}
//		return null;
	}

	private IteratorCounter2 replaceFirstLine(String s) {
		final List<CharSequence2> tmp = new ArrayList<CharSequence2>();
		tmp.add(new CharSequence2Impl(s, null));
		final Iterator<? extends CharSequence> it3 = it99.cloneMe();
		if (it3.hasNext()) {
			it3.next();
		}
		while (it3.hasNext()) {
			tmp.add(new CharSequence2Impl(it3.next(), null));
		}
		return new IteratorCounter2Impl(tmp);
	}
}
