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

/**
 * A player whose states are numbers (a time series), as opposed to
 * {@link AbstractStatePlayer} whose states are labels.
 * <p>
 * Subclasses only choose how two consecutive samples are joined, through the
 * {@link net.sourceforge.plantuml.timingdiagram.graphic.PanelsNumeric} they
 * build.
 */
public abstract class AbstractNumericPlayer extends Player {

	private final TimeSeries timeSeries = new TimeSeries();
	private final List<TimeConstraint> constraints = new ArrayList<>();

	private Double initialState;
	private Integer ticksEvery;

	public AbstractNumericPlayer(String full, ISkinParam skinParam, TimingRuler ruler, boolean compact,
			Stereotype stereotype, SName sname) {
		super(full, skinParam, ruler, compact, stereotype, null, sname, 100);
	}

	@Override
	public final void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void setState(TimeTick now, String comment, Colors color, String... valueString) {
		final double value = parseValue(valueString[0]);
		if (now == null)
			this.initialState = value;
		else
			this.timeSeries.put(now, value);

		if (this.initialState == null)
			this.initialState = value;
	}

	private static double parseValue(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			Logme.error(e);
			return 0;
		}
	}

	@Override
	public final void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(1, tick1, tick2, message, getSkinParam(), config));
	}

	public final void setBounds(String min, String max) {
		timeSeries.setBounds(min, max);
	}

	public final void setTicks(int ticksEvery) {
		this.ticksEvery = ticksEvery;
	}

	protected final TimeSeries getTimeSeries() {
		return timeSeries;
	}

	protected final List<TimeConstraint> getConstraints() {
		return constraints;
	}

	protected final Double getInitialState() {
		return initialState;
	}

	protected final Integer getTicksEvery() {
		return ticksEvery;
	}

}
