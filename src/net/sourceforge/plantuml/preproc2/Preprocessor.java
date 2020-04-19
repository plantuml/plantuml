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
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineNumbered;

public class Preprocessor implements ReadLineNumbered {

	private final ReadLine source;

	public Preprocessor(List<String> config, ReadLine reader) throws IOException {
		final ReadFilterAnd filters = new ReadFilterAnd();
		// filters.add(new ReadLineQuoteComment(true));
		filters.add(new ReadFilterAddConfig(config));
		filters.add(new ReadFilterMergeLines());
		this.source = filters.applyFilter(reader);
	}

	public StringLocated readLine() throws IOException {
		return source.readLine();
	}

	public void close() throws IOException {
		this.source.close();
	}

//	public Set<FileWithSuffix> getFilesUsedTOBEREMOVED() {
//		// System.err.println("************************** WARNING **************************");
//		return Collections.emptySet();
//		// return Collections.unmodifiableSet(include.getFilesUsedGlobal());
//	}
}