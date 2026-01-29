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
 * Contribution: The-Lum
 *
 */
package net.sourceforge.plantuml.timingdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.timingdiagram.graphic.Panels;
import net.sourceforge.plantuml.timingdiagram.graphic.PanelsDigital;

public class PlayerDigital extends Player {

	private final TimeSeries timeSeries = new TimeSeries();

	private final List<TimeConstraint> constraints = new ArrayList<>();

	private Double initialState;
	private Integer ticksEvery;

	public PlayerDigital(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype) {
		super(code, skinParam, ruler, compact, stereotype, null, SName.digital, 100);
	}

	@Override
	protected Panels buildPlayerPanels() {
		return new PanelsDigital(getRuler(), getSkinParam(), getSuggestedHeight(), getStyle(), timeSeries, constraints,
				initialState, ticksEvery);

	}

	@Override
	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(TimeTick now, String comment, Colors color, String... valueString) {
		final double value = getState(valueString[0]);
		if (now == null)
			this.initialState = value;
		else
			this.timeSeries.put(now, value);

		if (this.initialState == null)
			this.initialState = value;

	}

	private double getState(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			Logme.error(e);
			return 0;
		}
	}

	@Override
	public void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(1, tick1, tick2, message, getSkinParam(), config));
	}

	public void setBounds(String min, String max) {
		timeSeries.setBounds(min, max);
	}

	public void setTicks(int ticksEvery) {
		this.ticksEvery = ticksEvery;
	}

}
