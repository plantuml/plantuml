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
 */
package net.sourceforge.plantuml.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.utils.Log;

public class PSystemVersionFactory extends PSystemSingleLineFactory {

	@Override
	protected AbstractPSystem executeLine(UmlSource source, String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$"))
				return PSystemVersion.createShowAuthors2(source);

			if (line.matches("(?i)^version\\s*$"))
				return PSystemVersion.createShowVersion2(source);

			// ::comment when __CORE__
			if (line.matches("(?i)^stdlib\\s*$"))
				return PSystemVersion.createStdLib(source);

			if (line.matches("(?i)^testdot\\s*$"))
				return PSystemVersion.createTestDot(source);

			if (line.matches("(?i)^keydistributor\\s*$"))
				return PSystemVersion.createKeyDistributor(source);

			if (line.matches("(?i)^keygen\\s*$")) {
				line = line.trim();
				return new PSystemKeygen(source, "");
			}
			if (line.matches("(?i)^keyimport(\\s+[0-9a-z]+)?\\s*$")) {
				line = line.trim();
				final String key = line.substring("keyimport".length()).trim();
				return new PSystemKeygen(source, key);
			}
			if (line.matches("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$")) {
				final Pattern p = Pattern.compile("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$");
				final Matcher m = p.matcher(line);
				if (m.find()) {
					return new PSystemKeycheck(source, m.group(1), m.group(2));
				}
			}
			// ::done
		} catch (Exception e) {
			Log.error("Error " + e);

		}
		return null;
	}
}
