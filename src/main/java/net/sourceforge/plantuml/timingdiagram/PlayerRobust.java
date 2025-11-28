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

import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.timingdiagram.graphic.FooHistogram;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.utils.Position;

public final class PlayerRobust extends Player {

	private final Set<ChangeState> changes = new TreeSet<>();
	private final List<TimeConstraint> constraints = new ArrayList<>();
	private final List<TimingNote> notes = new ArrayList<>();
	private final Map<String, String> statesLabel = new LinkedHashMap<String, String>();

	private String initialState;
	private Colors initialColors;

	public PlayerRobust(String full, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype,
			HColor generalBackgroundColor) {
		super(full, skinParam, ruler, compact, stereotype, generalBackgroundColor);
		this.suggestedHeight = 0;
	}

	@Override
	public final void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(2.5, tick1, tick2, message, skinParam, config));
	}

	@Override
	protected StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.robust)
				.withTOBECHANGED(stereotype);
	}

	protected PlayerPanels getPlayerPanelsSlow() {
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final Style style0 = StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FooHistogram result = new FooHistogram(ruler, skinParam, statesLabel.values(), suggestedHeight, style,
				style0);
		result.setInitialState(initialState, initialColors);
		for (ChangeState change : changes)
			result.addChange(change);

		for (TimeConstraint constraint : constraints)
			result.addConstraint(constraint);

		return result;
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
