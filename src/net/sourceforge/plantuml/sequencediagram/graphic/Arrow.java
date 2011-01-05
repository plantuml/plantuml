/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5872 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Skin;

abstract class Arrow extends GraphicalElement implements InGroupable {

	private final Skin skin;
	private final Component arrowComponent;
	private double paddingArrowHead = 0;
	private double maxX;

	public void setMaxX(double m) {
		if (maxX != 0) {
			throw new IllegalStateException();
		}
		this.maxX = m;
	}

	final protected double getMaxX() {
		if (maxX == 0) {
			throw new IllegalStateException();
		}
		return maxX;
	}
	
	public abstract double getActualWidth(StringBounder stringBounder);

	Arrow(double startingY, Skin skin, Component arrowComponent) {
		super(startingY);
		this.skin = skin;
		this.arrowComponent = arrowComponent;
	}

	public abstract int getDirection(StringBounder stringBounder);

	protected Skin getSkin() {
		return skin;
	}

	protected final Component getArrowComponent() {
		return arrowComponent;
	}

	public double getArrowOnlyWidth(StringBounder stringBounder) {
		return getPreferredWidth(stringBounder);
	}

	public abstract double getArrowYStartLevel(StringBounder stringBounder);

	public abstract double getArrowYEndLevel(StringBounder stringBounder);

	public abstract LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position);

	protected final double getPaddingArrowHead() {
		return paddingArrowHead;
	}

	protected final void setPaddingArrowHead(double paddingArrowHead) {
		this.paddingArrowHead = paddingArrowHead;
	}
	
	final public double getMargin() {
		return 5;
	}


}
