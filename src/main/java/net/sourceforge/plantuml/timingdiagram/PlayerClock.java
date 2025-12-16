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

import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.graphic.FooClock;

public class PlayerClock extends Player {

	private final int period;
	private final int pulse;
	private final int offset;

	public PlayerClock(String title, ISkinParam skinParam, TimingRuler ruler, int period, int pulse, int offset,
			boolean compact, Stereotype stereotype) {
		super(title, skinParam, ruler, compact, stereotype, null, SName.clock);
		this.period = period;
		this.pulse = pulse;
		this.offset = offset;
		this.suggestedHeight = 30;
	}

	@Override
	protected PlayerPanels buildPlayerPanels() {
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());

		return new FooClock(ruler, skinParam, suggestedHeight, style, period, pulse, offset, null);

	}

	@Override
	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(TimeTick now, String comment, Colors color, String... states) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		throw new UnsupportedOperationException();
	}

	public final int getPeriod() {
		return period;
	}

}
