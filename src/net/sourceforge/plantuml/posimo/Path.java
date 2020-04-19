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
package net.sourceforge.plantuml.posimo;

public class Path {

	private final Label label;
	private final Block start;
	private final Block end;
	private final int length;
	private DotPath dotPath;
	private final boolean invis;

	public Path(Block start, Block end, Label label) {
		this(start, end, label, 2, false);
	}

	public Path(Block start, Block end, Label label, int length, boolean invis) {
		if (start == null || end == null) {
			throw new IllegalArgumentException();
		}
		if (length < 1) {
			throw new IllegalArgumentException("length=" + length);
		}
		this.invis = invis;
		this.start = start;
		this.end = end;
		this.label = label;
		this.length = length;
	}

	public final Label getLabel() {
		return label;
	}

	public final Block getStart() {
		return start;
	}

	public final Block getEnd() {
		return end;
	}

	public void setLabelPositionCenter(double labelX, double labelY) {
		label.setCenterX(labelX);
		label.setCenterY(labelY);
	}

	public void setLabelPosition(double x, double y) {
		label.setX(x);
		label.setY(y);
	}

	public void setDotPath(DotPath dotPath) {
		this.dotPath = dotPath;

	}

	public final DotPath getDotPath() {
		return dotPath;
	}

	public int getLength() {
		return length;
	}

	public final boolean isInvis() {
		return invis;
	}

}
