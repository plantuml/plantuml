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
package net.sourceforge.plantuml.gitlog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GitTextArea {

	private final List<String> lines = new ArrayList<>();
	private final List<Commit> commits = new ArrayList<>();

	public void add(String s) {
		lines.add(s);
	}

	public List<Commit> getAllCommits() {
		if (commits.size() == 0)
			for (int y = 0; y < lines.size(); y++) {
				String s = lines.get(y);
				final String name = CursorPosition.getCommitNameInLine(s);
				final int x = s.indexOf("*");
				assert (name == null) == (x == -1);
				if (x == -1)
					continue;

				commits.add(new Commit(name, new CursorPosition(this, x, y)));
			}

		return Collections.unmodifiableList(commits);
	}

	public char charAt(int x, int y) {
		return lines.get(y).charAt(x);
	}

	public String getLine(int y) {
		if (y >= lines.size())
			return "";

		return lines.get(y);
	}

	public Commit getCommitByName(String name) {
		for (Commit commit : getAllCommits())
			if (commit.getName().equals(name))
				return commit;

		return null;
	}

}
