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
package net.sourceforge.plantuml.project.lang;

import java.util.Collection;
import java.util.Collections;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;

public interface Subject<D extends Diagram> {

	public Collection<? extends SentenceSimple<D>> getSentences();

	public IRegex toRegex();

	public Failable<? extends Object> getMe(D project, RegexResult arg);

	default public UBrexPart toUnicodeBracketedExpressionSubject() {
		return null;
	}

	default public Failable<? extends Object> ugetMe(D diagram, UMatcher arg) {
		throw new IllegalArgumentException("wip8547 " + getClass());
	}

	default public Collection<VerbPhraseAction> getVerbPhrases() {
		return Collections.emptyList();
	}

}
