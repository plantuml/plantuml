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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Styleable;

abstract class FtileDiamondWIP extends AbstractFtile implements Styleable {

	protected final HColor backColor;
	protected final HColor borderColor;
	protected final Swimlane swimlane;

	protected final TextBlock label;

	protected final TextBlock north;
	protected final TextBlock south;
	protected /* final */ TextBlock west;
	protected /* final */ TextBlock east;

	protected final double shadowing;

	public void swapEastWest() {
		final TextBlock tmp = this.west;
		this.west = this.east;
		this.east = tmp;

	}

	final public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public Style getStyle() {
		return getStyleSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
	}

	@Override
	final public Collection<Ftile> getMyChildren() {
		return Collections.emptyList();
	}

	protected FtileDiamondWIP(TextBlock label, ISkinParam skinParam, HColor backColor, HColor borderColor,
			Swimlane swimlane, TextBlock north, TextBlock south, TextBlock east, TextBlock west) {
		super(skinParam);
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.shadowing = style.getShadowing();

		this.swimlane = swimlane;

		this.label = label;
		this.north = north;
		this.west = west;
		this.east = east;
		this.south = south;
	}

	final public Set<Swimlane> getSwimlanes() {
		if (swimlane == null)
			return Collections.emptySet();

		return Collections.singleton(swimlane);
	}

	final public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	final public Swimlane getSwimlaneOut() {
		return swimlane;
	}

}
