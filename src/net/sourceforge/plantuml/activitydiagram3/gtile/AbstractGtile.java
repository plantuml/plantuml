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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class AbstractGtile extends AbstractTextBlock implements Gtile {

	protected final StringBounder stringBounder;
	private final ISkinParam skinParam;
	private final Swimlane singleSwimlane;

	public AbstractGtile(StringBounder stringBounder, ISkinParam skinParam, Swimlane singleSwimlane) {
		this.stringBounder = stringBounder;
		this.skinParam = skinParam;
		this.singleSwimlane = singleSwimlane;
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public AbstractGtile(StringBounder stringBounder, ISkinParam skinParam) {
		this(stringBounder, skinParam, null);
	}

	final public ISkinParam skinParam() {
		if (skinParam == null) {
			throw new IllegalStateException();
		}
		return skinParam;
	}

	final public HColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
	}

//	@Override
//	public UTranslate getTranslateFor(Gtile child) {
//		throw new UnsupportedOperationException();
//	}

//	@Override
//	public Collection<Gtile> getMyChildren() {
//		return Collections.emptySet();
//	}

	@Override
	public UTranslate getCoord(String name) {
		final Dimension2D dim = calculateDimension(stringBounder);
		if (name.equals(GPoint.NORTH))
			return new UTranslate(dim.getWidth() / 2, 0);
		if (name.equals(GPoint.SOUTH))
			return new UTranslate(dim.getWidth() / 2, dim.getHeight());
		if (name.equals(GPoint.WEST))
			return new UTranslate(0, dim.getHeight() / 2);
		if (name.equals(GPoint.EAST))
			return new UTranslate(dim.getWidth(), dim.getHeight() / 2);
		throw new UnsupportedOperationException();
	}

	@Override
	public GPoint getGPoint(String name) {
		if (name.equals(GPoint.NORTH) || name.equals(GPoint.SOUTH) || name.equals(GPoint.WEST)
				|| name.equals(GPoint.EAST))
			return new GPoint(this, name);
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		if (singleSwimlane == null)
			return Collections.emptySet();
		return Collections.singleton(singleSwimlane);
	}

	@Override
	public Swimlane getSwimlane(String point) {
		return singleSwimlane;
	}

//	public LinkRendering getInLinkRendering() {
//		return LinkRendering.none();
//	}
//
//	public LinkRendering getOutLinkRendering() {
//		return LinkRendering.none();
//	}

	public Collection<GConnection> getInnerConnections() {
		return Collections.emptyList();
	}

//	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
//		throw new UnsupportedOperationException("" + getClass());
//	}

	public final UStroke getThickness() {
		UStroke thickness = skinParam.getThickness(LineParam.activityBorder, null);
		if (thickness == null) {
			thickness = new UStroke(1.5);
		}
		return thickness;
	}

	private final Rose rose = new Rose();

	protected final Rose getRose() {
		return rose;
	}

//	public List<WeldingPoint> getWeldingPoints() {
//		return Collections.emptyList();
//	}
//
//	public Collection<Ftile> getMyChildren() {
//		throw new UnsupportedOperationException("" + getClass());
//	}
//
//	public HorizontalAlignment arrowHorizontalAlignment() {
//		return skinParam.getHorizontalAlignment(AlignmentParam.arrowMessageAlignment, null, false, null);
//	}
//
//	private FtileGeometry cachedGeometry;
//
//	final public FtileGeometry calculateDimension(StringBounder stringBounder) {
//		if (cachedGeometry == null) {
//			cachedGeometry = calculateDimensionFtile(stringBounder);
//		}
//		return cachedGeometry;
//	}
//
//	abstract protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder);
//
//	@Override
//	final public MinMax getMinMax(StringBounder stringBounder) {
//		throw new UnsupportedOperationException();
//		// return getMinMaxFtile(stringBounder);
//	}
//
//	// protected MinMax getMinMaxFtile(StringBounder stringBounder) {
//	// throw new UnsupportedOperationException();
//	// }

}
