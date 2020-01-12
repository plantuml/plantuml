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
import net.sourceforge.plantuml.preproc.ReadLineList;
import net.sourceforge.plantuml.utils.StartUtils;

public class ReadFilterAddConfig implements ReadFilter {

	private final List<String> config;

	public ReadFilterAddConfig(List<String> config) {
		this.config = config;
	}

	public ReadLine applyFilter(final ReadLine raw) {

		return new ReadLine() {

			private ReadLine inserted;

			public void close() throws IOException {
				raw.close();
			}

			public StringLocated readLine() throws IOException {
				StringLocated result = null;
				if (inserted != null) {
					result = inserted.readLine();
					if (result == null) {
						inserted.close();
						inserted = null;
					} else {
						return result;
					}
				}
				result = raw.readLine();
				if (result != null && StartUtils.isArobaseStartDiagram(result.getString()) && config.size() > 0) {
					inserted = new ReadFilterQuoteComment().applyFilter(new ReadLineList(config, result.getLocation()));
				}
				return result;
			}
		};
	}

}
