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
package net.sourceforge.plantuml.stats;

import java.util.prefs.Preferences;

import net.sourceforge.plantuml.api.NumberAnalyzed;
import net.sourceforge.plantuml.api.NumberAnalyzedDated;

public class ParsedGenerated {

	private final NumberAnalyzed parsed;
	private final NumberAnalyzed generated;

	private ParsedGenerated(NumberAnalyzed parsed, NumberAnalyzed generated) {
		if (parsed == null || generated == null) {
			throw new IllegalArgumentException();
		}
		this.parsed = parsed;
		this.generated = generated;

	}

	public void reset() {
		parsed.reset();
		generated.reset();
	}

	public static ParsedGenerated createVolatile() {
		return new ParsedGenerated(new NumberAnalyzed(), new NumberAnalyzed());
	}

	public static ParsedGenerated createVolatileDated() {
		return new ParsedGenerated(new NumberAnalyzedDated(), new NumberAnalyzedDated());
	}

	public static ParsedGenerated loadDated(Preferences prefs, String name) {
		NumberAnalyzedDated parsed = NumberAnalyzedDated.load(name + ".p", prefs);
		if (parsed == null) {
			parsed = new NumberAnalyzedDated(name + ".p");
		}
		NumberAnalyzedDated generated = NumberAnalyzedDated.load(name + ".g", prefs);
		if (generated == null) {
			generated = new NumberAnalyzedDated(name + ".g");
		}
		return new ParsedGenerated(parsed, generated);
	}

	public NumberAnalyzed parsed() {
		return parsed;
	}

	public NumberAnalyzed generated() {
		return generated;
	}

	public NumberAnalyzedDated parsedDated() {
		return (NumberAnalyzedDated) parsed;
	}

	public NumberAnalyzedDated generatedDated() {
		return (NumberAnalyzedDated) generated;
	}

	public long getId() {
		final String comment = parsedDated().getComment();
		final int x = comment.indexOf('/');
		if (x == -1) {
			return -1;
		}
		return Long.parseLong(comment.substring(0, x), 36);
	}

	public String getVersion() {
		final String comment = parsedDated().getComment();
		final int x = comment.indexOf('/');
		if (x == -1) {
			return " ";
		}
		return comment.substring(x + 1);
	}

}
