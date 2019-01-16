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
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.preproc.ReadLine;

public abstract class ReadLineInsertable implements ReadLine {

	private final List<ReadLine> sources = new ArrayList<ReadLine>();

	final protected void insert(ReadLine inserted) throws IOException {
		sources.add(0, inserted);
	}

	abstract CharSequence2 readLineInternal() throws IOException;

	final public CharSequence2 readLine() throws IOException {
		while (sources.size() > 0) {
			final ReadLine tmp = sources.get(0);
			final CharSequence2 result = tmp.readLine();
			if (result != null) {
				return result;
			}
			tmp.close();
			sources.remove(0);
		}
		return readLineInternal();
	}

	abstract void closeInternal() throws IOException;

	final public void close() throws IOException {
		for (ReadLine s : sources) {
			s.close();
		}
		closeInternal();
	}

}
