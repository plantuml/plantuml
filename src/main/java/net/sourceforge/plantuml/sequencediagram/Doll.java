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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.teoz.LivingSpace;
import net.sourceforge.plantuml.sequencediagram.teoz.TileArguments;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.WithStyle;

public class Doll implements WithStyle {

	final private List<Participant> participants = new ArrayList<>();
	// alls is only used for Teoz: refactor needed after puma will be removed
	final private Map<ParticipantEnglober, Doll> alls;
	final private ParticipantEnglober englober;
	final private StyleBuilder styleBuilder;
	final private TileArguments tileArguments;

	public static Doll createPuma(ParticipantEnglober englober, Participant first, ISkinParam skinParam, Rose skin,
			StringBounder stringBounder, StyleBuilder styleBuilder) {
		return new Doll(englober, convertFunctionToBeRemoved(skinParam, skin, stringBounder), styleBuilder, first,
				null);
	}

	public static Doll createTeoz(ParticipantEnglober englober, TileArguments tileArguments,
			Map<ParticipantEnglober, Doll> alls) {
		return new Doll(englober, tileArguments, tileArguments.getSkinParam().getCurrentStyleBuilder(), null, alls);
	}

	private static TileArguments convertFunctionToBeRemoved(ISkinParam skinParam, Rose skin,
			StringBounder stringBounder) {
		return new TileArguments(stringBounder, null, skin, skinParam, null, null);
	}

	private Doll(ParticipantEnglober englober, TileArguments tileArguments, StyleBuilder styleBuilder,
			Participant first, Map<ParticipantEnglober, Doll> alls) {
		this.englober = Objects.requireNonNull(englober);
		this.styleBuilder = styleBuilder;
		this.tileArguments = Objects.requireNonNull(tileArguments);
		this.alls = alls;

		if (first != null)
			this.participants.add(first);

	}

	final public StyleSignatureBasic getStyleSignature() {
		return ComponentType.ENGLOBER.getStyleSignature();
	}

	final public Style[] getUsedStyles() {
		Style tmp = getStyleSignature().withTOBECHANGED(englober.getStereotype()).getMergedStyle(styleBuilder);
		final HColor backColor = englober.getBoxColor();
		if (tmp != null)
			tmp = tmp.eventuallyOverride(PName.BackGroundColor, backColor);

		return new Style[] { tmp };
	}

	final public ParticipantEnglober getParticipantEnglober() {
		return englober;
	}

	private Component getComponent() {
		final ParticipantEnglober englober = getParticipantEnglober();
		final ISkinParam s = englober.getBoxColor() == null ? tileArguments.getSkinParam()
				: new SkinParamBackcolored(tileArguments.getSkinParam(), englober.getBoxColor());
		return tileArguments.getSkin().createComponent(getUsedStyles(), ComponentType.ENGLOBER, null, s,
				englober.getTitle());
	}

	public double getTitlePreferredHeight() {
		final Component comp = tileArguments.getSkin().createComponent(getUsedStyles(), ComponentType.ENGLOBER, null,
				tileArguments.getSkinParam(), getParticipantEnglober().getTitle());
		return comp.getPreferredHeight(tileArguments.getStringBounder());
	}

	public final Participant getFirst2TOBEPRIVATE() {
		return participants.get(0);
	}

	public final Participant getLast2TOBEPRIVATE() {
		return participants.get(participants.size() - 1);
	}

	private Real getPosA(StringBounder stringBounder) {
		return getFirstLivingSpace().getPosA(stringBounder);
	}

	private Real getPosB(StringBounder stringBounder) {
		return getFirstLivingSpace().getPosB(stringBounder);
	}

	private Real getPosD(StringBounder stringBounder) {
		return getLastLivingSpace().getPosD(stringBounder);
	}

	private Real getPosE(StringBounder stringBounder) {
		return getLastLivingSpace().getPosE(stringBounder);
	}

	private Real getPosAA(StringBounder stringBounder) {
		final LivingSpace previous = tileArguments.getLivingSpaces().previous(getFirstLivingSpace());
		if (previous == null)
			return tileArguments.getXOrigin();

		return previous.getPosD(stringBounder);
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
		participants.add(Objects.requireNonNull(p));
	}

	@Override
	public String toString() {
		return "Doll:" + englober.getTitle().toString() + " " + participants;
	}

	private double getTitleWidth() {
		return getComponent().getPreferredWidth(tileArguments.getStringBounder());
	}

	public void drawMe(UGraphic ug, double height, Context2D context, Doll group) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getPosA(stringBounder).getCurrentValue() - 4;
		final double x2 = getPosE(stringBounder).getCurrentValue() + 4;

		for (Doll current = group; current != null; current = current.getParent()) {
			final double titlePreferredHeight = current.getTitlePreferredHeight();
			ug = ug.apply(UTranslate.dy(titlePreferredHeight));
			height -= titlePreferredHeight;
		}

		final XDimension2D dim = new XDimension2D(x2 - x1, height);
		getComponent().drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
	}

	public Doll getParent() {
		final ParticipantEnglober parent = getParticipantEnglober().getParent();
		if (parent == null)
			return null;
		return alls.get(parent);
	}

	public void addInternalConstraints(StringBounder stringBounder) {
		final double titleWidth = getTitleWidth();
		final double x1 = getPosB(stringBounder).getCurrentValue();
		final double x2 = getPosD(stringBounder).getCurrentValue();
		final double actualWidth = x2 - x1;
		final double marginX = (titleWidth + 10 - actualWidth) / 2;
		if (marginX > 0) {
			getFirstLivingSpace().ensureMarginBefore(marginX);
			getLastLivingSpace().ensureMarginAfter(marginX);
		}
		getPosA(stringBounder).ensureBiggerThan(getPosAA(stringBounder).addFixed(10 + padding()));

	}

	public void addConstraintAfter(StringBounder stringBounder) {
		final LivingSpace next = tileArguments.getLivingSpaces().next(getLastLivingSpace());
		if (next == null)
			return;

		next.getPosA(stringBounder).ensureBiggerThan(getPosE(stringBounder).addFixed(20 + 2 * padding()));
	}

	private double padding() {
		return tileArguments.getSkinParam().getPadding(PaddingParam.BOX);
	}

	public Real getMinX(StringBounder stringBounder) {
		return getPosA(stringBounder);
	}

	public Real getMaxX(StringBounder stringBounder) {
		return getPosE(stringBounder).addFixed(10);
	}

}
