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
 */
package net.sourceforge.plantuml.tim;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc2.PreprocessorIncludeStrategy;

public class EaterInclude extends Eater {

	private String location;
	private PreprocessorIncludeStrategy strategy = PreprocessorIncludeStrategy.DEFAULT;

	public EaterInclude(StringLocated s) {
		super(s);
	}

	@Override
	public void analyze(TContext context, TMemory memory) throws EaterException, EaterExceptionLocated {
		skipSpaces();
		checkAndEatChar("!include");
		final char peekChar = peekChar();
		if (peekChar == 'u') {
			checkAndEatChar("url");
		} else if (peekChar == '_') {
			checkAndEatChar("_");
			final char peekChar2 = peekChar();
			if (peekChar2 == 'm') {
				checkAndEatChar("many");
				this.strategy = PreprocessorIncludeStrategy.MANY;
			} else {
				checkAndEatChar("once");
				this.strategy = PreprocessorIncludeStrategy.ONCE;
			}
		}
		skipSpaces();
		this.location = context.applyFunctionsAndVariables(memory, getLineLocation(), this.eatAllToEnd());

	}

	public final String getLocation() {
		return location;
	}

	public final PreprocessorIncludeStrategy getPreprocessorIncludeStrategy() {
		return strategy;
	}

}
