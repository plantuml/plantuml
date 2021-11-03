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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import static net.sourceforge.plantuml.utils.ObjectUtils.instanceOfAny;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UBackground;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicNo;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ZadBuilder extends UGraphicNo {

	@Override
	public UGraphic apply(UChange change) {
		return new ZadBuilder(this, change);
	}

	private final Context context;

	static class Context {
		private final Zad zad = new Zad();
	}

	public ZadBuilder(StringBounder stringBounder) {
		super(stringBounder);
		this.context = new Context();
	}

	private ZadBuilder(ZadBuilder other, UChange change) {
		super(other, change);
		if (!instanceOfAny(change,
				UBackground.class,
				HColor.class,
				UStroke.class,
				UTranslate.class
		)) {
			throw new UnsupportedOperationException(change.getClass().toString());
		}
		this.context = other.context;
	}

	public void draw(UShape shape) {
		if (shape instanceof URectangle) {
			drawRectangle((URectangle) shape);
		}
	}

	private void drawRectangle(URectangle shape) {
		final MinMax area = shape.getMinMax().translate(getTranslate());
		// System.err.println("ZadBuilder " + shape + " " + area);
		context.zad.add(area);
	}

	public Zad getZad() {
		return context.zad;
	}

}
