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

import net.sourceforge.plantuml.StringUtils;


public class SuggestEngineResult {

	private final SuggestEngineStatus status;
	private final String suggestedLine;

	public static final SuggestEngineResult CANNOT_CORRECT = new SuggestEngineResult(SuggestEngineStatus.CANNOT_CORRECT);
	public static final SuggestEngineResult SYNTAX_OK = new SuggestEngineResult(SuggestEngineStatus.SYNTAX_OK);

	private SuggestEngineResult(SuggestEngineStatus status) {
		if (status == SuggestEngineStatus.ONE_SUGGESTION) {
			throw new IllegalArgumentException();
		}
		this.status = status;
		this.suggestedLine = null;
	}

	@Override
	public String toString() {
		return status + " " + suggestedLine;
	}

	@Override
	public int hashCode() {
		return status.hashCode() + (suggestedLine == null ? 0 : suggestedLine.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		final SuggestEngineResult this2 = (SuggestEngineResult) obj;
		return status.equals(this2.status) && sameString(suggestedLine, this2.suggestedLine);
	}

	private static boolean sameString(String a, String b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null || b != null) {
			return false;
		}
		return a.equals(b);
	}

	public SuggestEngineResult(String suggestedLine) {
		if (StringUtils.trin(suggestedLine).length() == 0) {
			throw new IllegalArgumentException();
		}
		this.status = SuggestEngineStatus.ONE_SUGGESTION;
		this.suggestedLine = suggestedLine;
	}

	public final SuggestEngineStatus getStatus() {
		return status;
	}

	public final String getSuggestedLine() {
		return suggestedLine;
	}

}
