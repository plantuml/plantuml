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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.PaddingParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.teoz.LivingSpace;
import net.sourceforge.plantuml.sequencediagram.teoz.TileArguments;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Doll extends DollAbstract {

	final private List<Participant> participants = new ArrayList<>();
	private final List<Doll> leafs = new ArrayList<>();

	final private Real core1;
	final private Real core2;
	private double marginX = 0;

	public static Doll createGroup(ParticipantEnglober englober, TileArguments tileArguments, StyleBuilder styleBuilder,
			boolean isTeoz) {
		return new Doll(englober, tileArguments, styleBuilder, isTeoz);
	}

	public static Doll createPuma(ParticipantEnglober englober, Participant first, ISkinParam skinParam, Rose skin,
			StringBounder stringBounder, StyleBuilder styleBuilder) {
		return new Doll(englober, convertFunctionToBeRemoved(skinParam, skin, stringBounder), styleBuilder, false,
				first);
	}

	public static Doll createTeoz(ParticipantEnglober englober, Participant first, TileArguments tileArguments,
			StyleBuilder styleBuilder) {
		final Doll result = new Doll(englober, tileArguments, styleBuilder, true, first);
		return result;
	}

	private static TileArguments convertFunctionToBeRemoved(ISkinParam skinParam, Rose skin,
			StringBounder stringBounder) {
		return new TileArguments(stringBounder, null, skin, skinParam, null);
	}

	private Doll(ParticipantEnglober englober, TileArguments tileArguments, StyleBuilder styleBuilder, boolean isTeoz,
			Participant first) {
		super(englober, tileArguments, styleBuilder, isTeoz);
		this.participants.add(first);
		final double preferredWidth = getPreferredWidth();
		if (tileArguments.getLivingSpaces() == null) {
			this.core1 = null;
			this.core2 = null;
		} else {
			this.core1 = getMiddle().addFixed(-preferredWidth / 2);
			this.core2 = getMiddle().addFixed(preferredWidth / 2);
		}
	}

	private Doll(ParticipantEnglober englober, TileArguments tileArguments, StyleBuilder styleBuilder, boolean isTeoz) {
		super(englober, tileArguments, styleBuilder, isTeoz);
		this.core1 = null;
		this.core2 = null;
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

	public boolean contains(Participant p) {
		return participants.contains(p);
	}

	public void addParticipant(Participant p) {
		if (participants.contains(p))
			throw new IllegalArgumentException();

		participants.add(p);
	}

	public void addDoll(Doll doll) {
		this.leafs.add(doll);
	}

	@Override
	public String toString() {
		return "DollLeaf:" + englober.getTitle().toString() + " " + participants;
	}

	private double getPreferredWidth() {
		if (isTeoz) {
			return 10;
		}
		return getTitleWidth();
	}

	private double getTitleWidth() {
		return getComponent().getPreferredWidth(tileArguments.getStringBounder());
	}

	public void drawGroup(UGraphic ug, double height, Context2D context) {
		if (leafs.size() == 0)
			throw new IllegalArgumentException();
		final double x1 = getGroupX1();
		final double x2 = getGroupX2();
		final Dimension2DDouble dim = new Dimension2DDouble(x2 - x1, height);
		getComponent().drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
	}

	private double getGroupX1() {
		double result = leafs.get(0).getX1().getCurrentValue() - 6;
		if (participants.size() > 0)
			result = Math.min(result, getX1().getCurrentValue() - 6);
		return result;
	}

	private double getGroupX2() {
		double result = leafs.get(leafs.size() - 1).getX2().getCurrentValue() + 6;
		if (participants.size() > 0)
			result = Math.max(result, getX2().getCurrentValue() + 6);
		return result;
	}

	public void drawMe(UGraphic ug, double height, Context2D context, Doll group) {
		final double x1 = getX1().getCurrentValue() - 4;
		final double x2 = getX2().getCurrentValue() + 4;

		if (group != null) {
			final double titlePreferredHeight = group.getTitlePreferredHeight();
			ug = ug.apply(UTranslate.dy(titlePreferredHeight));
			height -= titlePreferredHeight;
		}

		final Dimension2DDouble dim = new Dimension2DDouble(x2 - x1, height);
		getComponent().drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
	}

	private Real getX1() {
		if (core1 == null)
			return getPosB().addFixed(-marginX);
		return RealUtils.min(getPosB(), core1).addFixed(-marginX);
	}

	private Real getX2() {
		if (core2 == null)
			return getPosD().addFixed(marginX);
		return RealUtils.max(getPosD(), core2).addFixed(marginX);
	}

	public void addInternalConstraints() {
		assert isTeoz;
		final double titleWidth = getTitleWidth();
		final double x1 = getX1().getCurrentValue();
		final double x2 = getX2().getCurrentValue();
		final double actualWidth = x2 - x1;
		if (titleWidth > actualWidth + 20)
			this.marginX = (titleWidth - actualWidth - 20) / 2;

		getX1().ensureBiggerThan(getPosAA().addFixed(10 + padding()));
		final Real posZZ = getPosZZ();
		final Real limit = getX2().addFixed(10 + padding());
		if (posZZ != null)
			posZZ.ensureBiggerThan(limit);

	}

	private double padding() {
		return tileArguments.getSkinParam().getPadding(PaddingParam.BOX);
	}

	public void addConstraintAfter(Doll current) {
		current.getX1().ensureBiggerThan(getX2().addFixed(10 + 2 * padding()));
	}

	public Real getMinX(StringBounder stringBounder) {
		return getX1();
	}

	public Real getMaxX(StringBounder stringBounder) {
		return getX2().addFixed(10);
	}

}
