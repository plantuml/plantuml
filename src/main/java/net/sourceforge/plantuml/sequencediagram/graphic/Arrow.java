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

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.url.Url;

abstract class Arrow extends GraphicalElement implements InGroupable {

	private final Rose skin;
	private final ArrowComponent arrowComponent;
	private double paddingArrowHead;
	private double maxX;
	private final Url url;
	private final Pragma pragma;
	private final String uid;

	public void setMaxX(double m) {
		if (maxX != 0)
			throw new IllegalStateException();

		this.maxX = m;
	}

	final protected double getMaxX() {
		if (maxX == 0) {
			// throw new IllegalStateException();
		}
		return maxX;
	}

	public abstract double getActualWidth(StringBounder stringBounder);

	Arrow(AtomicInteger counter, Pragma pragma, double startingY, Rose skin, ArrowComponent arrowComponent, Url url) {
		super(startingY);
		this.pragma = pragma;
		this.skin = skin;
		this.arrowComponent = arrowComponent;
		this.url = url;
		this.uid = "msg" + counter.getAndAdd(1);
	}

	protected Url getUrl() {
		return url;
	}

	protected final void startGroup(UGraphic ug) {
		final UGroup typeIdents = new UGroup();
		typeIdents.put(UGroupType.CLASS, "message");
		// Please also remove Pragma from constructor when this will be removed
		if (pragma.isTrue(PragmaKey.SVGNEWDATA)) {
			typeIdents.put(UGroupType.DATA_PARTICIPANT_1, getParticipant1().getUid());
			typeIdents.put(UGroupType.DATA_PARTICIPANT_2, getParticipant2().getUid());
			typeIdents.put(UGroupType.DATA_UID, uid);
		} else {
			typeIdents.put(UGroupType.DATA_PARTICIPANT_1, getParticipant1().getCode());
			typeIdents.put(UGroupType.DATA_PARTICIPANT_2, getParticipant2().getCode());
		}
		ug.startGroup(typeIdents);
	}

	protected final void endGroup(UGraphic ug) {
		ug.closeGroup();
	}

	protected final void startUrl(UGraphic ug) {
		if (url != null)
			ug.startUrl(url);

	}

	protected final void endUrl(UGraphic ug) {
		if (url != null)
			ug.closeUrl();

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

	protected abstract Participant getParticipant1();

	protected abstract Participant getParticipant2();

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
