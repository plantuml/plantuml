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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.timingdiagram.graphic.Histogram;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.timingdiagram.graphic.PDrawing;
import net.sourceforge.plantuml.timingdiagram.graphic.PlayerFrame;
import net.sourceforge.plantuml.timingdiagram.graphic.Ribbon;
import net.sourceforge.plantuml.utils.Position;

public final class PlayerRobustConcise extends Player {

	private final Set<ChangeState> changes = new TreeSet<>();
	private final List<TimeConstraint> constraints = new ArrayList<>();
	private final List<TimingNote> notes = new ArrayList<>();
	private final Map<String, String> statesLabel = new LinkedHashMap<String, String>();
	private final TimingStyle type;

	private String initialState;
	private PDrawing cached;
	private Colors initialColors;

	public PlayerRobustConcise(TimingStyle type, String full, ISkinParam skinParam, TimingRuler ruler, boolean compact,
			Stereotype stereotype, HColor generalBackgroundColor) {
		super(full, skinParam, ruler, compact, stereotype, generalBackgroundColor);
		this.type = type;
		this.suggestedHeight = 0;
	}

	@Override
	public final void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		final double margin = type == TimingStyle.ROBUST ? 2.5 : 1;
		this.constraints.add(new TimeConstraint(margin, tick1, tick2, message, skinParam, config));
	}

	@Override
	protected StyleSignature getStyleSignature() {
		if (type == TimingStyle.CONCISE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.concise)
					.withTOBECHANGED(stereotype);
		if (type == TimingStyle.ROBUST)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.robust)
					.withTOBECHANGED(stereotype);
		throw new IllegalStateException();
	}

	private PDrawing buildPDrawing() {
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		if (type == TimingStyle.CONCISE)
			return new Ribbon(ruler, skinParam, notes, isCompact(), getTitle(), suggestedHeight, style);

		if (type == TimingStyle.ROBUST) {
			final Style style0 = StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			return new Histogram(ruler, skinParam, statesLabel.values(), isCompact(), getTitle(), suggestedHeight,
					style, style0);
		}

		throw new IllegalStateException();
	}

	@Override
	public final TextBlock getPart1(final double fullAvailableWidth, final double specialVSpace) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				if (isCompact() == false)
					new PlayerFrame(getTitle(), skinParam).drawFrameTitle(ug);

				ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder())).apply(UTranslate.dy(specialVSpace));
				getTimeDrawing().getPart1(fullAvailableWidth).drawU(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getTimeDrawing().getPart1(fullAvailableWidth).calculateDimension(stringBounder);
			}
		};
	}

	@Override
	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder()));
				getTimeDrawing().getPart2().drawU(ug);
			}
		};
	}

	private UTranslate getTranslateForTimeDrawing(StringBounder stringBounder) {
		return UTranslate.dy(getTitleHeight(stringBounder));
	}

	@Override
	public final double getFullHeight(StringBounder stringBounder) {
		return getTitleHeight(stringBounder) + getZoneHeight(stringBounder);
	}

	private double getTitleHeight(StringBounder stringBounder) {
		if (isCompact())
			return 6;

		return getTitle().calculateDimension(stringBounder).getHeight() + 6;
	}

	private PDrawing getTimeDrawing() {
		if (cached == null)
			cached = computeTimeDrawing();

		return cached;
	}

	private PDrawing computeTimeDrawing() {
		final PDrawing result = buildPDrawing();
		result.setInitialState(initialState, initialColors);
		for (ChangeState change : changes)
			result.addChange(change);

		for (TimeConstraint constraint : constraints)
			result.addConstraint(constraint);

		return result;
	}

	private double getZoneHeight(StringBounder stringBounder) {
		return getTimeDrawing().getFullHeight(stringBounder);
	}

	@Override
	public final void setState(TimeTick now, String comment, Colors color, String... states) {
		for (int i = 0; i < states.length; i++)
			states[i] = decodeState(states[i]);

		if (now == null) {
			this.initialState = states[0];
			this.initialColors = color;
		} else {
			this.changes.add(new ChangeState(now, comment, color, states));
		}

	}

	private String decodeState(String code) {
		final String label = statesLabel.get(code);
		if (label == null)
			return code;

		return label;
	}

	@Override
	public final IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		if (tick == null)
			return null;
		final IntricatedPoint point = getTimeDrawing().getTimeProjection(stringBounder, tick);
		if (point == null)
			return null;

		final UTranslate translation = getTranslateForTimeDrawing(stringBounder);
		return point.translated(translation);
	}

	@Override
	public final void addNote(TimeTick now, Display note, Position position, Stereotype stereotype) {

		final StyleSignature signature = StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram,
				SName.note);
		final Style style = signature.withTOBECHANGED(stereotype).getMergedStyle(skinParam.getCurrentStyleBuilder());

		this.notes.add(new TimingNote(now, this, note, position, skinParam, style));
	}

	@Override
	public final void defineState(String stateCode, String label) {
		statesLabel.put(stateCode, label);
	}

}
