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

public class SuggestEngineResult {

	private final SuggestEngineStatus status;
	private final String suggestedLine;
	private final int numLine;

	public static final SuggestEngineResult CANNOT_CORRECT = new SuggestEngineResult(SuggestEngineStatus.CANNOT_CORRECT);
	public static final SuggestEngineResult SYNTAX_OK = new SuggestEngineResult(SuggestEngineStatus.SYNTAX_OK);

	private SuggestEngineResult(SuggestEngineStatus status) {
		if (status == SuggestEngineStatus.ONE_SUGGESTION) {
			throw new IllegalArgumentException();
		}
		this.status = status;
		this.suggestedLine = null;
		this.numLine = -1;
	}

	public SuggestEngineResult(String suggestedLine, int numLine) {
		this.status = SuggestEngineStatus.ONE_SUGGESTION;
		this.suggestedLine = suggestedLine;
		this.numLine = numLine;
	}

	public final SuggestEngineStatus getStatus() {
		return status;
	}

	public final String getSuggestedLine() {
		return suggestedLine;
	}

	public final int getNumLine() {
		return numLine;
	}

}
