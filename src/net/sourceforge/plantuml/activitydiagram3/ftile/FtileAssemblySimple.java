/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileAssemblySimple implements Ftile {

	private final Ftile tile1;
	private final Ftile tile2;

	@Override
	public String toString() {
		return "FtileAssemblySimple " + tile1 + " && " + tile2;
	}

	public FtileAssemblySimple(Ftile tile1, Ftile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}
	
	public Swimlane getSwimlaneIn() {
		return tile1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile2.getSwimlaneOut();
	}


	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile1) {
			return getTranslated1(stringBounder);
		}
		if (child == tile2) {
			return getTranslated2(stringBounder);
		}
		UTranslate tmp = tile1.getTranslateFor(child, stringBounder);
		if (tmp != null) {
			return tmp.compose(getTranslated1(stringBounder));
		}
		tmp = tile2.getTranslateFor(child, stringBounder);
		if (tmp != null) {
			return tmp.compose(getTranslated2(stringBounder));
		}
		throw new UnsupportedOperationException();
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				// final TextBlock textBlock1 = tile1.asTextBlock();
				// final TextBlock textBlock2 = tile2.asTextBlock();
				// textBlock1.drawUNewWayINLINED(ug.apply(getTranslated1(stringBounder)));
				// textBlock2.drawUNewWayINLINED(ug.apply(getTranslated2(stringBounder)));
				ug.apply(getTranslated1(stringBounder)).draw(tile1);
				ug.apply(getTranslated2(stringBounder)).draw(tile2);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim1 = tile1.asTextBlock().calculateDimension(stringBounder);
				final Dimension2D dim2 = tile2.asTextBlock().calculateDimension(stringBounder);
				return Dimension2DDouble.mergeTB(dim1, dim2);
			}

		};
	}

	public boolean isKilled() {
		return tile1.isKilled() || tile2.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return tile1.getInLinkRendering();
	}

	public LinkRendering getOutLinkRendering() {
		return null;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final UTranslate dx1 = getTranslated1(stringBounder);
		final Point2D pt = tile1.getPointIn(stringBounder);
		return dx1.getTranslated(pt);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final UTranslate dx2 = getTranslated2(stringBounder);
		final Point2D pt = tile2.getPointOut(stringBounder);
		if (pt == null) {
			return null;
		}
		return dx2.getTranslated(pt);
	}

	private UTranslate getTranslated1(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		final TextBlock textBlock1 = tile1.asTextBlock();
		final Dimension2D dim1 = textBlock1.calculateDimension(stringBounder);
		final double dx1 = dimTotal.getWidth() - dim1.getWidth();
		return new UTranslate(dx1 / 2, 0);
	}

	private UTranslate getTranslated2(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		final TextBlock textBlock1 = tile1.asTextBlock();
		final TextBlock textBlock2 = tile2.asTextBlock();
		final Dimension2D dim1 = textBlock1.calculateDimension(stringBounder);
		final Dimension2D dim2 = textBlock2.calculateDimension(stringBounder);
		final double dx2 = dimTotal.getWidth() - dim2.getWidth();
		return new UTranslate(dx2 / 2, dim1.getHeight());
	}

	public Collection<Connection> getInnerConnections() {
		return Collections.emptyList();
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		result.addAll(tile1.getSwimlanes());
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public boolean shadowing() {
		return tile1.shadowing() || tile2.shadowing();
	}

}
