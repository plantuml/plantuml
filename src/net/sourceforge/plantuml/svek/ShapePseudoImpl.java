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
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.graphic.StringBounder;

public class ShapePseudoImpl implements IShapePseudo {

	private final String uid;
	private final double width;
	private final double height;

	public ShapePseudoImpl(String uid, double width, double height) {
		this.uid = uid;
		this.width = width;
		this.height = height;
	}

	public String getUid() {
		return uid;
	}

	public void appendShape(StringBuilder sb, StringBounder stringBounder) {
		sb.append(uid + " [shape=rect,label=\"\"");
		sb.append(",width=" + SvekUtils.pixelToInches(width));
		sb.append(",height=" + SvekUtils.pixelToInches(height));
		sb.append("];");
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
