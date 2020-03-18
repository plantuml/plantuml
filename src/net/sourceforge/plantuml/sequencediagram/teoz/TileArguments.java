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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.SkinParamBackcoloredReference;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TileArguments implements Bordered {
	private final StringBounder stringBounder;
	private final Real origin;
	private final LivingSpaces livingSpaces;
	private final Rose skin;
	private final ISkinParam skinParam;

	public TileArguments(StringBounder stringBounder, LivingSpaces livingSpaces, Rose skin, ISkinParam skinParam,
			Real origin) {
		this.stringBounder = stringBounder;
		this.origin = origin;
		this.livingSpaces = livingSpaces;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	public TileArguments withBackColorGeneral(HColor backColorElement, HColor backColorGeneral) {
		return new TileArguments(stringBounder, livingSpaces, skin, new SkinParamBackcolored(skinParam,
				backColorElement, backColorGeneral), origin);
	}

	public TileArguments withBackColor(Reference reference) {
		final ISkinParam newSkinParam = new SkinParamBackcoloredReference(skinParam, reference.getBackColorElement(),
				reference.getBackColorGeneral());
		return new TileArguments(stringBounder, livingSpaces, skin, newSkinParam, origin);
	}

	public final StringBounder getStringBounder() {
		return stringBounder;
	}

	// public final Real getMaxAbsolute() {
	// return origin.getMaxAbsolute();
	// }

	public final Real getOrigin() {
		return origin;
	}

	public final LivingSpaces getLivingSpaces() {
		return livingSpaces;
	}

	public final Rose getSkin() {
		return skin;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public LivingSpace getLivingSpace(Participant p) {
		return livingSpaces.get(p);
	}

	public LivingSpace getFirstLivingSpace() {
		return livingSpaces.values().iterator().next();
	}

	public LivingSpace getLastLivingSpace() {
		LivingSpace result = null;
		for (LivingSpace v : livingSpaces.values()) {
			result = v;
		}
		return result;
	}

	private Bordered bordered;

	public void setBordered(Bordered bordered) {
		this.bordered = bordered;
	}

	public double getBorder1() {
		return bordered.getBorder1();
	}

	public double getBorder2() {
		return bordered.getBorder2();
	}

	// public double getAbsoluteMin() {
	// return line.getAbsoluteMin();
	// }
	//
	// public double getAbsoluteMax() {
	// return line.getAbsoluteMax();
	// }

	// public void ensure(Tile tile) {
	// getAlpha().ensureLowerThan(tile.getMinX(getStringBounder()));
	// getOmega().ensureBiggerThan(tile.getMaxX(getStringBounder()));
	// }

}
