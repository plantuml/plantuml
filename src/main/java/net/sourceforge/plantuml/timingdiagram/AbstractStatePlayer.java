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
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;

public abstract class AbstractStatePlayer extends Player {

	private final Set<ChangeState> changes = new TreeSet<>();
	private final List<TimeConstraint> constraints = new ArrayList<>();
	private final Map<String, String> statesLabel = new LinkedHashMap<>();

	private String initialState;
	private Colors initialColors;

	public AbstractStatePlayer(String full, ISkinParam skinParam, TimingRuler ruler, boolean compact,
			Stereotype stereotype, HColor generalBackgroundColor) {
		super(full, skinParam, ruler, compact, stereotype, generalBackgroundColor);
		this.suggestedHeight = 0;
	}

	@Override
	public final void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(getConstraintOffset(), tick1, tick2, message, skinParam, config));
	}

	protected abstract double getConstraintOffset();

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
	public final void defineState(String stateCode, String label) {
		statesLabel.put(stateCode, label);
	}

	protected final Set<ChangeState> getChanges() {
		return changes;
	}

	protected final List<TimeConstraint> getConstraints() {
		return constraints;
	}

	protected final Map<String, String> getStatesLabel() {
		return statesLabel;
	}

	protected final String getInitialState() {
		return initialState;
	}

	protected final Colors getInitialColors() {
		return initialColors;
	}

}
