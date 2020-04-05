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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.TextBlock;

public class BodierMap implements Bodier {

	private final List<String> rawBody = new ArrayList<String>();
	private final Map<String, String> map = new LinkedHashMap<String, String>();
	private ILeaf leaf;

	public void muteClassToObject() {
		throw new UnsupportedOperationException();
	}

	public BodierMap() {
	}

	public void setLeaf(ILeaf leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		this.leaf = leaf;

	}

	public static String getLinkedEntry(String s) {
		final Pattern p = Pattern.compile("(\\*-+\\>)");
		final Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	public void addFieldOrMethod(String s) {
		if (s.contains("=>")) {
			final int x = s.indexOf("=>");
			map.put(s.substring(0, x).trim(), s.substring(x + 2).trim());
		} else if (getLinkedEntry(s) != null) {
			final String link = getLinkedEntry(s);
			final int x = s.indexOf(link);
			map.put(s.substring(0, x).trim(), "\0");
		}
	}

	public List<Member> getMethodsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public List<Member> getFieldsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUrl() {
		return false;
	}

	public TextBlock getBody(FontParam fontParam, ISkinParam skinParam, final boolean showMethods,
			final boolean showFields, Stereotype stereotype) {
		return new TextBlockMap(fontParam, skinParam, map);
	}

	public List<String> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

}
