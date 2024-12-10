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
package net.sourceforge.plantuml;

import java.util.Objects;

import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.utils.LineLocation;

public class ErrorUml {
	// ::remove file when __HAXE__

	private final String error;
	private final ErrorUmlType errorType;
	private final LineLocation lineLocation;
	private final int score;
	private final UmlDiagramType diagramType;

	public ErrorUml(ErrorUmlType type, String error, int score, LineLocation lineLocation, UmlDiagramType diagramType) {
		this.score = score;
		this.error = Objects.requireNonNull(error);
		this.errorType = Objects.requireNonNull(type);
		this.lineLocation = lineLocation;
		this.diagramType = diagramType;
	}

	public int score() {
		return score;
	}

	@Override
	public boolean equals(Object obj) {
		final ErrorUml this2 = (ErrorUml) obj;
		return this.errorType == this2.errorType && this.getPosition() == this2.getPosition()
				&& this.error.equals(this2.error);
	}

	@Override
	public int hashCode() {
		return error.hashCode() + errorType.hashCode() + getPosition();
	}

	@Override
	public String toString() {
		return errorType.toString() + " " + getPosition() + " " + error;
	}

	public final String getError() {
		if (diagramType != null)
			return error + " (Assumed diagram type: " + diagramType.humanReadableName() + ")";
		return error;
	}

	public final int getPosition() {
		return lineLocation.getPosition();
	}

	public final LineLocation getLineLocation() {
		return lineLocation;
	}

}
