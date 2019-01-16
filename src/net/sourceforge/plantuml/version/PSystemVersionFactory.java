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
package net.sourceforge.plantuml.version;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;

public class PSystemVersionFactory extends PSystemSingleLineFactory {

	@Override
	protected AbstractPSystem executeLine(String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$")) {
				return PSystemVersion.createShowAuthors();
			}
			if (line.matches("(?i)^version\\s*$")) {
				return PSystemVersion.createShowVersion();
			}
			if (line.matches("(?i)^stdlib\\s*$")) {
				return PSystemVersion.createStdLib();
			}
			if (line.matches("(?i)^path\\s*$")) {
				return PSystemVersion.createPath();
			}
			if (line.matches("(?i)^testdot\\s*$")) {
				return PSystemVersion.createTestDot();
			}
			if (line.matches("(?i)^dumpstacktrace\\s*$")) {
				return PSystemVersion.createDumpStackTrace();
			}
			if (line.matches("(?i)^keydistributor\\s*$")) {
				return PSystemVersion.createKeyDistributor();
			}
			if (line.matches("(?i)^checkversion\\s*$")) {
				return PSystemVersion.createCheckVersions(null, null);
			}
			if (line.matches("(?i)^keygen\\s*$")) {
				line = line.trim();
				return new PSystemKeygen("");
			}
			if (line.matches("(?i)^keyimport(\\s+[0-9a-z]+)?\\s*$")) {
				line = line.trim();
				final String key = line.substring("keyimport".length()).trim();
				return new PSystemKeygen(key);
			}
			if (line.matches("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$")) {
				final Pattern p = Pattern.compile("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$");
				final Matcher m = p.matcher(line);
				if (m.find()) {
					return new PSystemKeycheck(m.group(1), m.group(2));
				}
			}
			final Pattern p1 = Pattern.compile("(?i)^checkversion\\(proxy=([\\w.]+),port=(\\d+)\\)$");
			final Matcher m1 = p1.matcher(line);
			if (m1.matches()) {
				final String host = m1.group(1);
				final String port = m1.group(2);
				return PSystemVersion.createCheckVersions(host, port);
			}
			final Pattern p2 = Pattern.compile("(?i)^checkversion\\(proxy=([\\w.]+)\\)$");
			final Matcher m2 = p2.matcher(line);
			if (m2.matches()) {
				final String host = m2.group(1);
				final String port = "80";
				return PSystemVersion.createCheckVersions(host, port);
			}
		} catch (IOException e) {
			Log.error("Error " + e);

		}
		return null;
	}
}
