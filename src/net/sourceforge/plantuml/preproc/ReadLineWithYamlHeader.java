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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.text.StringLocated;

public class ReadLineWithYamlHeader implements ReadLine {

	private final ReadLine source;
	// Right now, the header is not used
	// It will be used in next releases
	private List<String> yamlHeader;
	private final Map<String, String> metadata = new LinkedHashMap<>();

	public ReadLineWithYamlHeader(ReadLine source) {
		this.source = source;
	}

	@Override
	public void close() throws IOException {
		source.close();
	}

	@Override
	public StringLocated readLine() throws IOException {
		StringLocated line = source.readLine();
		if (yamlHeader == null) {
			// At this point, "line" is the very first line of the file
			yamlHeader = new ArrayList<String>();
			if (isSeparator(line)) {
				do {
					line = source.readLine();
					if (line == null || isSeparator(line))
						break;
					final String tmp = line.getString();
					yamlHeader.add(tmp);
					final int idx = tmp.indexOf(':');
					if (idx > 0)
						metadata.put(tmp.substring(0, idx).trim(), tmp.substring(idx + 1).trim());

				} while (true);
				// Skip the second separator
				line = source.readLine();
			}

		}
		return line;
	}

	private static boolean isSeparator(StringLocated line) {
		return line.getString().equals("---");
	}

	public Map<String, String> getMetadata() {
		return Collections.unmodifiableMap(metadata);
	}

	public static List<StringLocated> removeYamlHeader(List<StringLocated> input) {
		if (input.size() > 1 && isSeparator(input.get(1))) {
			final List<StringLocated> result = new ArrayList<>();
			result.add(input.get(0));
			for (int i = 2; i < input.size(); i++)
				if (isSeparator(input.get(i))) {
					result.addAll(input.subList(i + 1, input.size()));
					return result;
				}
		}

		return input;
	}

}
