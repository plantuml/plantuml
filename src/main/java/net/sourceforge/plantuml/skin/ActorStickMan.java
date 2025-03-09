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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class ActorStickMan extends AbstractTextBlock implements TextBlock {

	private final double armsY = 8;
	private final double armsLenght = 13;
	private final double bodyLenght = 27;
	private final double legsX = 13;
	private final double legsY = 15;
	private final double headDiam = 16;

	private final Fashion fashion;
	private final boolean actorBusiness;

	ActorStickMan(Fashion fashion, boolean actorBusiness) {
		this.fashion = fashion;
		this.actorBusiness = actorBusiness;
	}

	public void drawU(UGraphic ug) {

		final double startX = Math.max(armsLenght, legsX) - headDiam / 2.0 + thickness();

		final UEllipse head = UEllipse.build(headDiam, headDiam);
		final double centerX = startX + headDiam / 2;

		final UPath path = UPath.none();
		path.moveTo(0, 0);
		path.lineTo(0, bodyLenght);
		path.moveTo(-armsLenght, armsY);
		path.lineTo(armsLenght, armsY);
		path.moveTo(0, bodyLenght);
		path.lineTo(-legsX, bodyLenght + legsY);
		path.moveTo(0, bodyLenght);
		path.lineTo(legsX, bodyLenght + legsY);
		if (fashion.getDeltaShadow() != 0) {
			head.setDeltaShadow(fashion.getDeltaShadow());
			path.setDeltaShadow(fashion.getDeltaShadow());
		}

		ug = fashion.apply(ug);
		ug.apply(new UTranslate(startX, thickness())).draw(head);
		if (actorBusiness) {
			specialBusiness(ug.apply(new UTranslate(startX + headDiam / 2, thickness() + headDiam / 2)));
		}
		ug.apply(new UTranslate(centerX, headDiam + thickness())).apply(HColors.none().bg()).draw(path);
	}

	private void specialBusiness(UGraphic ug) {
		final double alpha = 21 * Math.PI / 64;
		final XPoint2D p1 = getOnCircle(Math.PI / 4 + alpha);
		final XPoint2D p2 = getOnCircle(Math.PI / 4 - alpha);
		ug = ug.apply(UTranslate.point(p1));
		ug.draw(new ULine(p2.getX() - p1.getX(), p2.getY() - p1.getY()));
	}

	private XPoint2D getOnCircle(double alpha) {
		final double x = headDiam / 2 * Math.cos(alpha);
		final double y = headDiam / 2 * Math.sin(alpha);
		return new XPoint2D(x, y);
	}

	private double thickness() {
		return fashion.getStroke().getThickness();
	}

	public double getPreferredWidth() {
		return Math.max(armsLenght, legsX) * 2 + 2 * thickness();
	}

	public double getPreferredHeight() {
		return headDiam + bodyLenght + legsY + 2 * thickness() + fashion.getDeltaShadow() + 1;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(getPreferredWidth(), getPreferredHeight());
	}
}
