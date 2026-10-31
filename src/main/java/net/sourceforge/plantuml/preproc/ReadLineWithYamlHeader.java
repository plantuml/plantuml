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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.MyCollections;

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
			if (line != null && isSeparator(line)) {
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
		return MyCollections.unmodifiableMap(metadata);
	}

	/**
	 * The lines of the YAML header of a diagram (between the two "---" lines that
	 * follow the @start line, separators excluded), or an empty list.
	 */
	public static List<StringLocated> getYamlHeader(List<StringLocated> input) {
		final int end = yamlHeaderEnd(input);
		if (end == -1)
			return new ArrayList<>();
		return new ArrayList<>(input.subList(2, end));
	}

	public static List<StringLocated> removeYamlHeader(List<StringLocated> input) {
		final int end = yamlHeaderEnd(input);
		if (end == -1)
			return input;
		final List<StringLocated> result = new ArrayList<>();
		result.add(input.get(0));
		result.addAll(input.subList(end + 1, input.size()));
		return result;
	}

	/** The index of the closing "---" of the header that follows the first line, or -1. */
	private static int yamlHeaderEnd(List<StringLocated> input) {
		if (input.size() > 1 && isSeparator(input.get(1)))
			for (int i = 2; i < input.size(); i++)
				if (isSeparator(input.get(i)))
					return i;
		return -1;
	}

}
