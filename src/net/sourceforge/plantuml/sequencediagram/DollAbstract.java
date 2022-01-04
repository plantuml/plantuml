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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.teoz.TileArguments;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class DollAbstract implements WithStyle {

	final protected ParticipantEnglober englober;
	final protected StyleBuilder styleBuilder;
	final protected boolean isTeoz;

	DollAbstract(ParticipantEnglober englober, StyleBuilder styleBuilder, boolean isTeoz) {
		this.englober = englober;
		this.styleBuilder = styleBuilder;
		this.isTeoz = isTeoz;
	}

	final public StyleSignature getDefaultStyleDefinition() {
		return ComponentType.ENGLOBER.getDefaultStyleDefinition();
	}

	final public Style[] getUsedStyles() {
		Style tmp = getDefaultStyleDefinition().with(englober.getStereotype()).getMergedStyle(styleBuilder);
		final HColor backColor = englober.getBoxColor();
		if (tmp != null) {
			tmp = tmp.eventuallyOverride(PName.BackGroundColor, backColor);
		}
		return new Style[] { tmp };
	}

	private static TileArguments convertFunctionToBeRemoved(ISkinParam skinParam, Rose skin,
			StringBounder stringBounder) {
		final TileArguments result = new TileArguments(stringBounder, null, skin, skinParam, null);
		return result;
	}

	final public ParticipantEnglober getParticipantEnglober() {
		return englober;
	}

}
