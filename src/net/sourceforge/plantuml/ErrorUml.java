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
 * Revision $Revision: 3824 $
 *
 */
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.suggest.SuggestEngineResult;
import net.sourceforge.plantuml.suggest.SuggestEngineStatus;

public class ErrorUml {

	private final String error;
	private final int position;
	private final ErrorUmlType type;
	private SuggestEngineResult suggest;

	public ErrorUml(ErrorUmlType type, String error, int position) {
		if (error == null || type == null || StringUtils.isEmpty(error)) {
			throw new IllegalArgumentException();
		}
		this.error = error;
		this.type = type;
		this.position = position;
	}

	@Override
	public boolean equals(Object obj) {
		final ErrorUml this2 = (ErrorUml) obj;
		return this.type == this2.type && this.position == this2.position && this.error.equals(this2.error);
	}

	@Override
	public int hashCode() {
		return error.hashCode() + type.hashCode() + position + (suggest == null ? 0 : suggest.hashCode());
	}

	@Override
	public String toString() {
		return type.toString() + " " + position + " " + error + " " + suggest;
	}

	public final String getError() {
		return error;
	}

	public final ErrorUmlType getType() {
		return type;
	}

	public int getPosition() {
		return position;
	}

	public final SuggestEngineResult getSuggest() {
		return suggest;
	}

	public final boolean hasSuggest() {
		return suggest != null && suggest.getStatus() == SuggestEngineStatus.ONE_SUGGESTION;
	}

	public void setSuggest(SuggestEngineResult suggest) {
		this.suggest = suggest;
	}

}
