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

public class TFunctionSignature {

	private final String functionName;
	private final int nbArg;

	public TFunctionSignature(String functionName, int nbArg) {
		if (functionName == null) {
			throw new IllegalArgumentException();
		}
		this.functionName = functionName;
		this.nbArg = nbArg;
	}
	
	public boolean sameNameAs(TFunctionSignature other) {
		return getFunctionName().equals(other.getFunctionName());
	}

	@Override
	public String toString() {
		return functionName + "/" + nbArg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + functionName.hashCode();
		result = prime * result + nbArg;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		final TFunctionSignature other = (TFunctionSignature) obj;
		return functionName.equals(other.functionName) && nbArg == other.nbArg;
	}

	public final String getFunctionName() {
		return functionName;
	}

	public final int getNbArg() {
		return nbArg;
	}
}
