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
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.graphic.PanelsBinary;
import net.sourceforge.plantuml.timingdiagram.graphic.Panels;

public class PlayerBinary extends Player {

	private static final String LOW_STRING = "0";
	private static final String HIGH_STRING = "1";

	private final List<TimeConstraint> constraints = new ArrayList<>();
	private final SortedMap<TimeTick, ChangeState> values = new TreeMap<>();
	private ChangeState initialState;

	public PlayerBinary(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype) {
		super(code, skinParam, ruler, compact, stereotype, null, SName.binary, 30);
	}

	@Override
	protected Panels buildPlayerPanels() {
		return new PanelsBinary(getRuler(), getSkinParam(), getSuggestedHeight(), getStyle(), values, constraints,
				initialState, getNotes());

	}

	@Override
	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(TimeTick now, String comment, Colors color, String... states) {
		final ChangeState cs = new ChangeState(now, comment, color, convert(states));
		if (now == null)
			this.initialState = cs;
		else
			this.values.put(now, cs);

	}

	private String[] convert(String[] states) {
		if (states.length == 1)
			return new String[] { convert(states[0]) };
		return new String[] { convert(states[0]), convert(states[1]) };
	}

	private String convert(String value) {
		if ("1".equals(value) || "high".equalsIgnoreCase(value))
			return HIGH_STRING;
		return LOW_STRING;
	}

	@Override
	public void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(2.5, tick1, tick2, message, getSkinParam(), config));
	}

}
