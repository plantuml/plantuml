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
package net.sourceforge.plantuml.project.command;

import com.plantuml.ubrex.TextNavigator;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.ulang.GanttParseResult;
import net.sourceforge.plantuml.project.ulang.UbrexSentence;
import net.sourceforge.plantuml.utils.BlocLines;

public class NaturalCommandUbrex<D extends Diagram> implements Command<D> {

	private final UbrexSentence<D> sentence;

	public NaturalCommandUbrex(UbrexSentence<D> sentence) {
		this.sentence = sentence;
	}

	@Override
	public String toString() {
		return sentence.toString();
	}

//	@Override
//	final protected CommandExecutionResult executeArg(D system, LineLocation location, RegexResult arg,
//			ParserPass currentPass) {
//		// System.err.println("sentence=" + sentence.getClass());
//		return sentence.execute(system, arg);
//	}

	public static <D extends Diagram> NaturalCommandUbrex<D> create(UbrexSentence<D> sentence) {
		return new NaturalCommandUbrex<D>(sentence);

	}

	@Override
	public CommandExecutionResult execute(D diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {

		System.out.println("==============================");
		System.out.println("--> " + sentence.getClass());
		final GanttParseResult args = sentence.parse(lines.getFirst().getString());
		System.out.println("args=" + args);

		final Failable<? extends Object> subject = sentence.getSubject(diagram, args);
		final Failable<? extends Object> complement = sentence.getComplement(diagram, args);

		return sentence.execute(diagram, subject.get(), complement.get());
		// return CommandExecutionResult.ok();
	}

	@Override
	public CommandControl isValid(BlocLines lines) {
		System.out.println("------------------------------");
		final TextNavigator tn = TextNavigator.build(lines.getFirst().getString());
		System.out.println("NaturalCommandUbrex::isValid " + tn);
		final boolean match = sentence.check(tn);
		if (match)
			return CommandControl.OK;

		return CommandControl.NOT_OK;
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE;
	}

}
