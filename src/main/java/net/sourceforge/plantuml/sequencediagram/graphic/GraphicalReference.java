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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.url.Url;

class GraphicalReference extends GraphicalElement implements InGroupable {

	private final Component comp;
	private final LivingParticipantBox livingParticipantBox1;
	private final LivingParticipantBox livingParticipantBox2;
	private final Url url;
	private final Component noteLeft;
	private final Component noteRight;

	public GraphicalReference(double startingY, Component comp, LivingParticipantBox livingParticipantBox1,
			LivingParticipantBox livingParticipantBox2, Url url, Component noteLeft, Component noteRight) {
		super(startingY);
		this.noteLeft = noteLeft;
		this.noteRight = noteRight;
		this.url = url;
		this.comp = comp;
		this.livingParticipantBox1 = Objects.requireNonNull(livingParticipantBox1);
		this.livingParticipantBox2 = Objects.requireNonNull(livingParticipantBox2);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {

		final StringBounder stringBounder = ug.getStringBounder();
		// final double posX = getMinX(stringBounder);

		ug = ug.apply(UTranslate.dy(getStartingY()));

		final double r1 = getR1(stringBounder);
		final double r2 = getR2(stringBounder);
		final XDimension2D dim = new XDimension2D(r2 - r1, comp.getPreferredHeight(stringBounder));
		if (url != null)
			ug.startUrl(url);

		comp.drawU(ug.apply(UTranslate.dx(r1)), new Area(dim), context);

		if (noteLeft != null) {
			final double wn = noteLeft.getPreferredWidth(stringBounder);
			final double hn = noteLeft.getPreferredHeight(stringBounder);
			noteLeft.drawU(ug, new Area(new XDimension2D(wn, hn)), context);
		}

		if (noteRight != null) {
			final double wn = noteRight.getPreferredWidth(stringBounder);
			final double hn = noteRight.getPreferredHeight(stringBounder);
			noteRight.drawU(ug.apply(UTranslate.dx(r2)), new Area(new XDimension2D(wn, hn)), context);
		}

		if (url != null)
			ug.closeUrl();

	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		double result = comp.getPreferredWidth(stringBounder);
		if (noteLeft != null)
			result += noteLeft.getPreferredWidth(stringBounder);
		if (noteRight != null)
			result += noteRight.getPreferredWidth(stringBounder);
		return result;
	}

	private double getR1(StringBounder stringBounder) {
		return Math.min(livingParticipantBox1.getMinX(stringBounder), livingParticipantBox2.getMinX(stringBounder));
	}

	private double getR2(StringBounder stringBounder) {
		final double diff = Math.max(livingParticipantBox1.getMaxX(stringBounder),
				livingParticipantBox2.getMaxX(stringBounder)) - getR1(stringBounder);

		final double preferredWidth = comp.getPreferredWidth(stringBounder);
		final double width = Math.max(diff, preferredWidth);

		return getR1(stringBounder) + width;
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getMinX(stringBounder);
	}

	@Override
	public double getMinX(StringBounder stringBounder) {
		double result = getR1(stringBounder);
		if (noteLeft != null)
			result -= noteLeft.getPreferredWidth(stringBounder);
		return result;
	}

	@Override
	public double getMaxX(StringBounder stringBounder) {
		double result = getR2(stringBounder);
		if (noteRight != null)
			result += noteRight.getPreferredWidth(stringBounder);
		return result;
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
