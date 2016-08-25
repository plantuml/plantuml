/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.io.IOException;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.command.regex.Matcher2;

class IfManagerNegatif extends IfManager {

	private boolean skippingDone = false;

	public IfManagerNegatif(ReadLine source, Defines defines) {
		super(source, defines);
	}

	@Override
	protected CharSequence2 readLineInternal() throws IOException {
		if (skippingDone == false) {
			skippingDone = true;
			do {
				final CharSequence2 s = readLine();
				if (s == null) {
					return null;
				}
				Matcher2 m = endifPattern.matcher(s);
				if (m.find()) {
					return null;
				}
				m = elsePattern.matcher(s);
				if (m.find()) {
					break;
				}
			} while (true);
		}

		final CharSequence2 s = super.readLineInternal();
		if (s == null) {
			return null;
		}
		final Matcher2 m = endifPattern.matcher(s);
		if (m.find()) {
			return null;
		}
		return s;

	}

}
