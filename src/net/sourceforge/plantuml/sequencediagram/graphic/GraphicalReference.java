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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class GraphicalReference extends GraphicalElement implements InGroupable {

	private final Component comp;
	private final LivingParticipantBox livingParticipantBox1;
	private final LivingParticipantBox livingParticipantBox2;
	private final Url url;

	public GraphicalReference(double startingY, Component comp, LivingParticipantBox livingParticipantBox1,
			LivingParticipantBox livingParticipantBox2, Url url) {
		super(startingY);
		if (livingParticipantBox1 == null || livingParticipantBox2 == null) {
			throw new IllegalArgumentException();
		}
		this.url = url;
		this.comp = comp;
		this.livingParticipantBox1 = livingParticipantBox1;
		this.livingParticipantBox2 = livingParticipantBox2;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {

		final StringBounder stringBounder = ug.getStringBounder();
		final double posX = getMinX(stringBounder);

		ug = ug.apply(new UTranslate(posX, getStartingY()));
		final double preferredWidth = comp.getPreferredWidth(stringBounder);
		final double w = getMaxX(stringBounder) - getMinX(stringBounder);

		final double width = Math.max(preferredWidth, w);

		final Dimension2D dim = new Dimension2DDouble(width, comp.getPreferredHeight(stringBounder));
		if (url != null) {
			ug.startUrl(url);
		}
		comp.drawU(ug, new Area(dim), context);
		if (url != null) {
			ug.closeAction();
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return comp.getPreferredWidth(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getMinX(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return Math.max(livingParticipantBox1.getMaxX(stringBounder), livingParticipantBox2.getMaxX(stringBounder));
	}

	public double getMinX(StringBounder stringBounder) {
		return Math.min(livingParticipantBox1.getMinX(stringBounder), livingParticipantBox2.getMinX(stringBounder));
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
