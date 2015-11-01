/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 4836 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.teoz.Bordered;
import net.sourceforge.plantuml.sequencediagram.teoz.LivingSpace;
import net.sourceforge.plantuml.sequencediagram.teoz.TileArguments;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Englober {

	final private ParticipantEnglober participantEnglober;
	final private List<Participant> participants = new ArrayList<Participant>();
	final private TileArguments tileArguments;
	final private Real core1;
	final private Real core2;

	@Deprecated
	public Englober(ParticipantEnglober participantEnglober, Participant first, ISkinParam skinParam, Skin skin) {
		this(participantEnglober, first, convertFunctionToBeRemoved(skinParam, skin));
	}

	private static TileArguments convertFunctionToBeRemoved(ISkinParam skinParam, Skin skin) {
		final TileArguments result = new TileArguments(TextBlockUtils.getDummyStringBounder(), null, skin, skinParam,
				null);
		return result;
	}

	public Englober(ParticipantEnglober participantEnglober, Participant first, TileArguments tileArguments) {
		if (tileArguments == null) {
			throw new IllegalArgumentException();
		}
		this.participantEnglober = participantEnglober;
		this.participants.add(first);
		this.tileArguments = tileArguments;
		final double preferredWidth = getPreferredWidth();
		if (tileArguments.getLivingSpaces() == null) {
			this.core1 = null;
			this.core2 = null;
		} else {
			this.core1 = getMiddle().addFixed(-preferredWidth / 2);
			this.core2 = getMiddle().addFixed(preferredWidth / 2);
		}
	}

	public final Participant getFirst2TOBEPRIVATE() {
		return participants.get(0);
	}

	public final Participant getLast2TOBEPRIVATE() {
		return participants.get(participants.size() - 1);
	}

	private Real getMiddle() {
		return RealUtils.middle(getPosB(), getPosD());
	}

	private Real getPosB() {
		return getFirstLivingSpace().getPosB();
	}

	private Real getPosD() {
		return getLastLivingSpace().getPosD(tileArguments.getStringBounder());
	}

	private Real getPosAA() {
		final LivingSpace previous = tileArguments.getLivingSpaces().previous(getFirstLivingSpace());
		if (previous == null) {
			return tileArguments.getOrigin();
		}
		return previous.getPosD(tileArguments.getStringBounder());
	}

	private Real getPosZZ() {
		final LivingSpace next = tileArguments.getLivingSpaces().next(getLastLivingSpace());
		if (next == null) {
			// return tileArguments.getMaxAbsolute();
			return null;
		}
		return next.getPosB();
	}

	private LivingSpace getFirstLivingSpace() {
		return tileArguments.getLivingSpace(getFirst2TOBEPRIVATE());
	}

	private LivingSpace getLastLivingSpace() {
		return tileArguments.getLivingSpace(getLast2TOBEPRIVATE());
	}

	private Component getComponent() {
		final ParticipantEnglober englober = getParticipantEnglober();
		final ISkinParam s = englober.getBoxColor() == null ? tileArguments.getSkinParam() : new SkinParamBackcolored(
				tileArguments.getSkinParam(), englober.getBoxColor());
		return tileArguments.getSkin().createComponent(ComponentType.ENGLOBER, null, s, englober.getTitle());
	}

	public final ParticipantEnglober getParticipantEnglober() {
		return participantEnglober;
	}

	public boolean contains(Participant p) {
		return participants.contains(p);
	}

	public void add(Participant p) {
		if (participants.contains(p)) {
			throw new IllegalArgumentException();
		}
		participants.add(p);
	}

	@Override
	public String toString() {
		return "ParticipantEngloberContexted:" + participantEnglober.getTitle().toString() + " " + participants;
	}

	private double getPreferredWidth() {
		return getComponent().getPreferredWidth(tileArguments.getStringBounder());
	}

	public double getPreferredHeight() {
		final Component comp = tileArguments.getSkin().createComponent(ComponentType.ENGLOBER, null,
				tileArguments.getSkinParam(), getParticipantEnglober().getTitle());
		return comp.getPreferredHeight(tileArguments.getStringBounder());
	}

	public void drawEnglober(UGraphic ug, double height, Context2D context) {
		final double x1 = getX1().getCurrentValue() - 4;
		final double x2 = getX2().getCurrentValue() + 4;

		final Dimension2DDouble dim = new Dimension2DDouble(x2 - x1, height);
		getComponent().drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
	}

	private Real getX2() {
		return RealUtils.max(getPosD(), core2);
	}

	private Real getX1() {
		return RealUtils.min(getPosB(), core1);
	}

	public void addInternalConstraints() {
		getX1().ensureBiggerThan(getPosAA().addFixed(10));
		final Real posZZ = getPosZZ();
		final Real limit = getX2().addFixed(10);
		if (posZZ != null) {
			posZZ.ensureBiggerThan(limit);
		}
	}

	public void addConstraintAfter(Englober current) {
		current.getX1().ensureBiggerThan(getX2().addFixed(10));
	}

	public Real getMinX(StringBounder stringBounder) {
		return getX1();
	}

	public Real getMaxX(StringBounder stringBounder) {
		return getX2().addFixed(10);
	}


}
