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
 * Original Author:  Vjekoslav Leonard Prcic
 * 
 *
 */
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

public class ActorHollow extends AbstractTextBlock implements TextBlock {

	private final double headDiam = 9;
	private final double bodyWidth = 25;
	private final double bodyHeight = 21;

	private final double neckHeight = 2;

	private final double armThickness = 5;
	private final double bodyThickness = 6;
	private final double legThickness = 6;

	private final Fashion fashion;

	public ActorHollow(Fashion fashion) {
		this.fashion = fashion;
	}

	public void drawU(UGraphic ug) {

		final UEllipse head = UEllipse.build(headDiam, headDiam);
		final double centerX = getPreferredWidth() / 2;

		final UPath path = UPath.none();
		path.moveTo(-bodyWidth / 2, 0);
		path.lineTo(-bodyWidth / 2, armThickness);
		path.lineTo(-bodyThickness / 2, armThickness);
		path.lineTo(-bodyThickness / 2, bodyHeight - (bodyWidth + legThickness * Math.sqrt(2) - bodyThickness) / 2);
		path.lineTo(-bodyWidth / 2, bodyHeight - legThickness * Math.sqrt(2) / 2);
		path.lineTo(-(bodyWidth / 2 - legThickness * Math.sqrt(2) / 2), bodyHeight);

		path.lineTo(0, bodyHeight - (bodyWidth / 2 - legThickness * Math.sqrt(2) / 2));

		path.lineTo(+(bodyWidth / 2 - legThickness * Math.sqrt(2) / 2), bodyHeight);
		path.lineTo(+bodyWidth / 2, bodyHeight - legThickness * Math.sqrt(2) / 2);
		path.lineTo(+bodyThickness / 2, bodyHeight - (bodyWidth + legThickness * Math.sqrt(2) - bodyThickness) / 2);
		path.lineTo(+bodyThickness / 2, armThickness);
		path.lineTo(+bodyWidth / 2, armThickness);
		path.lineTo(+bodyWidth / 2, 0);
		path.lineTo(-bodyWidth / 2, 0);
		path.closePath();

		if (fashion.getDeltaShadow() != 0) {
			head.setDeltaShadow(fashion.getDeltaShadow());
			path.setDeltaShadow(fashion.getDeltaShadow());
		}
		ug = fashion.apply(ug);
		ug.apply(new UTranslate(centerX - head.getWidth() / 2, thickness())).draw(head);
		ug.apply(new UTranslate(centerX, head.getHeight() + thickness() + neckHeight)).draw(path);
	}

	private double thickness() {
		return fashion.getStroke().getThickness();
	}

	public double getPreferredWidth() {
		return bodyWidth + thickness() * 2;
	}

	public double getPreferredHeight() {
		return headDiam + neckHeight + bodyHeight + thickness() * 2 + fashion.getDeltaShadow();
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(getPreferredWidth(), getPreferredHeight());
	}
}