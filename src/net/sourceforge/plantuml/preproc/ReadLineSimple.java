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
 *
 */
package net.sourceforge.plantuml.preproc;

import net.sourceforge.plantuml.StringLocated;

public class ReadLineSimple implements ReadLine {

	private final StringLocated data;
	private final String error;
	private int current = 0;

	public ReadLineSimple(StringLocated s2, String error) {
		this.data = s2;
		this.error = error;
	}

	public void close() {
	}

	public StringLocated readLine() {
		if (current > 0) {
			return null;
		}
		current++;
		// return new CharSequence2Impl(line, null);
		// return CharSequence2Impl.errorPreprocessor(data, error);
		return data.withErrorPreprocessor(error);
	}

}
