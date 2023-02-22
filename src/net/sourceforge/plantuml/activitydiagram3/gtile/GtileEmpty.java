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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UEmpty;
import net.sourceforge.plantuml.style.ISkinParam;

public class GtileEmpty extends AbstractGtile {

	private final double width;
	private final double height;

	public GtileEmpty(StringBounder stringBounder, ISkinParam skinParam, double width, double height) {
		this(stringBounder, skinParam, width, height, null);
	}

	public GtileEmpty(StringBounder stringBounder, ISkinParam skinParam) {
		this(stringBounder, skinParam, 0, 0, null);
	}

	public GtileEmpty(StringBounder stringBounder, ISkinParam skinParam, Swimlane swimlane) {
		this(stringBounder, skinParam, 0, 0, swimlane);
	}

	public GtileEmpty(StringBounder stringBounder, ISkinParam skinParam, double width, double height,
			Swimlane swimlane) {
		super(stringBounder, skinParam, swimlane);
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "GtileEmpty";
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		if (width > 0 && height > 0)
			ug.draw(new UEmpty(width, height));

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(width, height);
	}

//	@Override
//	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
//		return calculateDimensionEmpty();
//	}
//
//	final protected FtileGeometry calculateDimensionEmpty() {
//		return new FtileGeometry(width, height, width / 2, 0, height);
//	}
//
//	public Swimlane getSwimlaneIn() {
//		return swimlane;
//	}
//
//	public Swimlane getSwimlaneOut() {
//		return swimlane;
//	}
//
//	public Set<Swimlane> getSwimlanes() {
//		final Set<Swimlane> result = new HashSet<>();
//		if (swimlane != null) {
//			result.add(swimlane);
//		}
//		return Collections.unmodifiableSet(result);
//	}

}
