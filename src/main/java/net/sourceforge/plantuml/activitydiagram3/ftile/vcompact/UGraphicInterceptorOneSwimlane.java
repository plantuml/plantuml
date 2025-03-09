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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.gtile.GConnection;
import net.sourceforge.plantuml.activitydiagram3.gtile.GPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicDelegator;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class UGraphicInterceptorOneSwimlane extends UGraphicDelegator {

	private final Swimlane swimlane;
	private final List<Swimlane> orderedList;

	public UGraphicInterceptorOneSwimlane(UGraphic ug, Swimlane swimlane, List<Swimlane> orderedList) {
		super(ug);
		this.swimlane = swimlane;
		this.orderedList = orderedList;
	}

	public void draw(UShape shape) {
		// System.err.println("inter=" + shape.getClass());
		if (shape instanceof Ftile) {
			final Ftile tile = (Ftile) shape;
			final Set<Swimlane> swimlanes = tile.getSwimlanes();
			final boolean contained = swimlanes.contains(swimlane);
			if (contained) {
				tile.drawU(this);
				// drawGoto();
			}
			// ::comment when __CORE__
		} else if (shape instanceof Gtile) {
			final Gtile tile = (Gtile) shape;
			final Set<Swimlane> swimlanes = tile.getSwimlanes();
			final boolean contained = swimlanes.contains(swimlane);
			if (contained)
				tile.drawU(this);

		} else if (shape instanceof GConnection) {
			final GConnection connection = (GConnection) shape;
			final List<GPoint> hooks = connection.getHooks();
			final GPoint point0 = hooks.get(0);
			final GPoint point1 = hooks.get(1);

			if (point0.match(swimlane) && point1.match(swimlane))
				connection.drawU(this);
			// ::done
		} else if (shape instanceof Connection) {
			final Connection connection = (Connection) shape;
			final Ftile tile1 = connection.getFtile1();
			final Ftile tile2 = connection.getFtile2();
			final boolean contained1 = tile1 == null || tile1.getSwimlaneOut() == null
					|| tile1.getSwimlaneOut() == swimlane;
			final boolean contained2 = tile2 == null || tile2.getSwimlaneIn() == null
					|| tile2.getSwimlaneIn() == swimlane;

			if (contained1 && contained2) {
				connection.drawU(this);
			}
		} else {
			getUg().draw(shape);
			// System.err.println("Drawing " + shape);
		}

	}

	private void drawGoto() {
		final UGraphic ugGoto = getUg().apply(HColors.GREEN).apply(HColors.GREEN.bg());
		ugGoto.draw(new ULine(100, 100));
	}

	public UGraphic apply(UChange change) {
		return new UGraphicInterceptorOneSwimlane(getUg().apply(change), swimlane, orderedList);
	}

	public final Swimlane getSwimlane() {
		return swimlane;
	}

	public final List<Swimlane> getOrderedListOfAllSwimlanes() {
		return Collections.unmodifiableList(orderedList);
	}

}