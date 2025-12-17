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

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.graphic.PanelsConcise;
import net.sourceforge.plantuml.timingdiagram.graphic.Panels;

public final class PlayerConcise extends AbstractStatePlayer {

	public PlayerConcise(String full, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype,
			HColor generalBackgroundColor) {
		super(full, skinParam, ruler, compact, stereotype, generalBackgroundColor, SName.concise, 1.0);
	}

	@Override
	protected Panels buildPlayerPanels() {
		final PanelsConcise result = new PanelsConcise(getRuler(), getSkinParam(), getNotes(), getSuggestedHeight(),
				getStyle(), getConstraints());
		result.setInitialState(getInitialState(), getInitialColors());
		for (ChangeState change : getChanges())
			result.addChange(change);

		return result;
	}

}
