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
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.StartUtils;

public class ReadFilterMergeLines implements ReadFilter {

	public ReadLine applyFilter(final ReadLine source) {
		return new ReadLine() {

			private boolean manageEndingBackslash = true;

			public void close() throws IOException {
				source.close();
			}

			public StringLocated readLine() throws IOException {
				StringLocated result = source.readLine();
				if (result != null && StartUtils.isArobaseStartDiagram(result.getString())
						&& isDitaa(result.getString())) {
					this.manageEndingBackslash = false;
				}
				if (result != null && StartUtils.isArobaseEndDiagram(result.getString())) {
					this.manageEndingBackslash = true;
				}

				ReadLine sourceWithoutComment = null;

				while (result != null && manageEndingBackslash && StringUtils.endsWithBackslash(result.getString())) {
					if (sourceWithoutComment == null) {
						sourceWithoutComment = new ReadFilterQuoteComment().applyFilter(source);
					}
					final StringLocated next = sourceWithoutComment.readLine();
					if (next == null) {
						break;
					} else {
						result = result.mergeEndBackslash(next);
					}
				}
				return result;
			}

			private boolean isDitaa(String string) {
				return DiagramType.getTypeFromArobaseStart(StringUtils.trinNoTrace((string))) == DiagramType.DITAA;
			}
		};
	}

}
