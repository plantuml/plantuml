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
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CMapData {

	private final StringBuilder stringBuilder = new StringBuilder();

	public String asString(String nameId) {
		return "<map id=\"" + nameId + "_map\" name=\"" + nameId + "_map\">\n" + stringBuilder.toString() + "</map>\n";
	}

	public boolean containsData() {
		return stringBuilder.length() > 0;
	}

	public void appendString(String s) {
		stringBuilder.append(s);
	}

	public void appendLong(long s) {
		stringBuilder.append(s);
	}

	private void appendUrl(int seq, Url url, double scale) {
		appendString("<area shape=\"rect\" id=\"id");
		appendLong(seq);
		appendString("\" href=\"");
		appendString(url.getUrl());
		appendString("\" title=\"");
		final String tooltip = url.getTooltip().replaceAll("\\\\n", BackSlash.NEWLINE).replaceAll("&", "&#38;")
				.replaceAll("\"", "&#34;").replaceAll("\'", "&#39;");
		appendString(tooltip);
		appendString("\" alt=\"\" coords=\"");
		appendString(url.getCoords(scale));
		appendString("\"/>");

		appendString(BackSlash.NEWLINE);
	}

	public static CMapData cmapString(Set<Url> allUrlEncountered, double scale) {
		final CMapData cmapdata = new CMapData();

		final List<Url> all = new ArrayList<Url>(allUrlEncountered);
		Collections.sort(all, Url.SURFACE_COMPARATOR);

		int seq = 1;
		for (Url u : all) {
			if (u.hasData() == false) {
				continue;
			}
			cmapdata.appendUrl(seq, u, scale);
			seq++;
		}
		return cmapdata;
	}

}
