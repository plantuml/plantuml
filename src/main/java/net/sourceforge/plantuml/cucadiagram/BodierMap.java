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
package net.sourceforge.plantuml.cucadiagram;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class BodierMap extends BodierAbstract {

	private final Map<String, String> map = new LinkedHashMap<String, String>();

	@Override
	public void muteClassToObject() {
		throw new UnsupportedOperationException();
	}

	public BodierMap() {
	}

	private static final Pattern p = Pattern.compile("(\\*-+_?\\>)");

	public static String getLinkedEntry(String s) {
		final Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);

		return null;
	}

	@Override
	public boolean addFieldOrMethod(String s) {
		final int x = s.indexOf("=>");
		if (x != -1) {
			map.put(s.substring(0, x).trim(), s.substring(x + 2).trim());
			return true;
		} else if (getLinkedEntry(s) != null) {
			final String link = getLinkedEntry(s);
			final int pos = s.indexOf(link);
			map.put(s.substring(0, pos).trim(), "\0");
			return true;
		}
		return false;
	}

	@Override
	public Display getMethodsToDisplay() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Display getFieldsToDisplay() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasUrl() {
		return false;
	}

	@Override
	public TextBlock getBody(ISkinParam skinParam, final boolean showMethods, final boolean showFields,
			Stereotype stereotype, Style style, FontConfiguration fontConfiguration) {
		final LineBreakStrategy wordWrap = style.wrapWidth();
		return new TextBlockMap(fontConfiguration, skinParam, map, wordWrap);
	}

}
