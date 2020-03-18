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
package net.sourceforge.plantuml.graph;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

abstract class AbstractEntityImage {

	private final IEntity entity;

	final private HColor red = HColorUtils.MY_RED;

	final private HColor yellow = HColorUtils.MY_YELLOW;
	private final HColor yellowNote = HColorUtils.COL_FBFB77;

	final private UFont font14 = UFont.sansSerif(14);
	final private UFont font17 = UFont.courier(17).bold();
	final private HColor green = HColorUtils.COL_ADD1B2;
	final private HColor violet = HColorUtils.COL_B4A7E5;
	final private HColor blue = HColorUtils.COL_A9DCDF;
	final private HColor rose = HColorUtils.COL_EB937F;

	public AbstractEntityImage(IEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity null");
		}
		this.entity = entity;
	}

	public abstract Dimension2D getDimension(StringBounder stringBounder);

	public abstract void draw(ColorMapper colorMapper, Graphics2D g2d);

	protected final IEntity getEntity() {
		return entity;
	}

	protected final HColor getRed() {
		return red;
	}

	protected final HColor getYellow() {
		return yellow;
	}

	protected final UFont getFont17() {
		return font17;
	}

	protected final UFont getFont14() {
		return font14;
	}

	protected final HColor getGreen() {
		return green;
	}

	protected final HColor getViolet() {
		return violet;
	}

	protected final HColor getBlue() {
		return blue;
	}

	protected final HColor getRose() {
		return rose;
	}

	protected final HColor getYellowNote() {
		return yellowNote;
	}
}
