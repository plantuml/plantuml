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

import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.url.Url;

import java.util.HashMap;
import java.util.Map;

abstract class Arrow extends GraphicalElement implements InGroupable {

	private final Rose skin;
	private final ArrowComponent arrowComponent;
	private double paddingArrowHead;
	private double maxX;
	private final Url url;

	public void setMaxX(double m) {
		if (maxX != 0) {
			throw new IllegalStateException();
		}
		this.maxX = m;
	}

	final protected double getMaxX() {
		if (maxX == 0) {
			// throw new IllegalStateException();
		}
		return maxX;
	}

	public abstract double getActualWidth(StringBounder stringBounder);

	Arrow(double startingY, Rose skin, ArrowComponent arrowComponent, Url url) {
		super(startingY);
		this.skin = skin;
		this.arrowComponent = arrowComponent;
		this.url = url;
	}

	protected Url getUrl() {
		return url;
	}

	protected final void startGroup(UGraphic ug) {
		Map<UGroupType, String> typeIdents = new HashMap<>();
		typeIdents.put(UGroupType.CLASS, "message");
		typeIdents.put(UGroupType.PARTICIPANT_1_NAME, getParticipant1Code());
		typeIdents.put(UGroupType.PARTICIPANT_2_NAME, getParticipant2Code());
		ug.startGroup(typeIdents);
	}

	protected final void endGroup(UGraphic ug) {
		ug.closeGroup();
	}

	protected final void startUrl(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
	}

	protected final void endUrl(UGraphic ug) {
		if (url != null) {
			ug.closeUrl();
		}
	}

	public abstract int getDirection(StringBounder stringBounder);

	protected Rose getSkin() {
		return skin;
	}

	protected final ArrowComponent getArrowComponent() {
		return arrowComponent;
	}

	public double getArrowOnlyWidth(StringBounder stringBounder) {
		return getPreferredWidth(stringBounder);
	}

	public abstract double getArrowYStartLevel(StringBounder stringBounder);

	public abstract double getArrowYEndLevel(StringBounder stringBounder);

	public abstract LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position);

	protected abstract String getParticipant1Code();

	protected abstract String getParticipant2Code();

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
