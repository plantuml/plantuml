/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageStateEmptyDescription extends EntityImageStateCommon {

	final private static int MIN_WIDTH = 50;
	final private static int MIN_HEIGHT = 40;

	public EntityImageStateEmptyDescription(EntityImp entity, ISkinParam skinParam) {
		super(entity, skinParam);

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim = title.calculateDimension(stringBounder);
		final XDimension2D result = dim.delta(MARGIN * 2);
		return result.atLeast(MIN_WIDTH, MIN_HEIGHT);
	}

	final public void drawU(UGraphic ug) {
		if (url != null)
			ug.startUrl(url);

		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dimDesc = title.calculateDimension(stringBounder);

		final UStroke stroke = getStyleState().getStroke(lineConfig.getColors());

		ug = applyColor(ug);
		ug = ug.apply(stroke);

		ug.draw(getShape(dimTotal));

		final double xDesc = (dimTotal.getWidth() - dimDesc.getWidth()) / 2;
		final double yDesc = (dimTotal.getHeight() - dimDesc.getHeight()) / 2;
		title.drawU(ug.apply(new UTranslate(xDesc, yDesc)));

		if (url != null)
			ug.closeUrl();

	}

}
