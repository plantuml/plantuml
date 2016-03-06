/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3834 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;

class SvgAttributesChange implements FontChange {

	static final Pattern pattern = MyPattern.cmpile(Splitter.svgAttributePattern);
	private final SvgAttributes attributes;

	SvgAttributesChange(String s) {
		final Matcher m = pattern.matcher(s);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		attributes = new SvgAttributes(m.group(1));
	}

	public FontConfiguration apply(FontConfiguration initial) {
		return initial.changeAttributes(attributes);
	}

}
