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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.style.ISkinParam;

public abstract class AbstractGtileRoot extends AbstractTextBlock implements Gtile {

	protected final StringBounder stringBounder;
	private final ISkinParam skinParam;

	public AbstractGtileRoot(StringBounder stringBounder, ISkinParam skinParam) {
		this.stringBounder = stringBounder;
		this.skinParam = skinParam;
	}

	@Override
	final public StringBounder getStringBounder() {
		return stringBounder;
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

	@Override
	final public GPoint getGPoint(String name) {
		if (name.equals(GPoint.NORTH_HOOK) || name.equals(GPoint.SOUTH_HOOK) || name.equals(GPoint.WEST_HOOK)
				|| name.equals(GPoint.EAST_HOOK) || name.equals(GPoint.NORTH_BORDER) || name.equals(GPoint.SOUTH_BORDER)
				|| name.equals(GPoint.WEST_BORDER) || name.equals(GPoint.EAST_BORDER))
			return new GPoint(this, name);
		throw new UnsupportedOperationException();
	}

	@Override
	public final UTranslate getCoord(String name) {
		if (name.equals(GPoint.NORTH_BORDER)) {
			final UTranslate tmp = getCoordImpl(GPoint.NORTH_HOOK);
			return new UTranslate(tmp.getDx(), 0);
		}
		if (name.equals(GPoint.SOUTH_BORDER)) {
			final UTranslate tmp = getCoordImpl(GPoint.SOUTH_HOOK);
			return new UTranslate(tmp.getDx(), calculateDimension(stringBounder).getHeight());
		}
		if (name.equals(GPoint.WEST_BORDER)) {
			final UTranslate tmp = getCoordImpl(GPoint.WEST_HOOK);
			return new UTranslate(0, tmp.getDy());
		}
		if (name.equals(GPoint.EAST_BORDER)) {
			final UTranslate tmp = getCoordImpl(GPoint.EAST_HOOK);
			return new UTranslate(calculateDimension(stringBounder).getWidth(), tmp.getDy());
		}
		return getCoordImpl(name);
	}

	abstract protected UTranslate getCoordImpl(String name);

	@Override
	final public void drawU(UGraphic ug) {
		drawUInternal(ug);
		for (GConnection connection : getInnerConnections()) {
			ug.draw(connection);
		}

	}

	abstract protected void drawUInternal(UGraphic ug);

}
