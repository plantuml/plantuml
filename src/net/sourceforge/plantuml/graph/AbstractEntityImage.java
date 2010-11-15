/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3831 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.StringBounder;

abstract class AbstractEntityImage {

	private final Entity entity;

	final private Color red = new Color(Integer.parseInt("A80036", 16));
	final private Color yellow = new Color(Integer.parseInt("FEFECE", 16));
	private final Color yellowNote = new Color(Integer.parseInt("FBFB77", 16));

	final private Font font14 = new Font("SansSerif", Font.PLAIN, 14);
	final private Font font17 = new Font("Courier", Font.BOLD, 17);
	final private Color green = new Color(Integer.parseInt("ADD1B2", 16));
	final private Color violet = new Color(Integer.parseInt("B4A7E5", 16));
	final private Color blue = new Color(Integer.parseInt("A9DCDF", 16));
	final private Color rose = new Color(Integer.parseInt("EB937F", 16));

	public AbstractEntityImage(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity null");
		}
		this.entity = entity;
	}

	public abstract Dimension2D getDimension(StringBounder stringBounder);

	public abstract void draw(Graphics2D g2d);

	protected final Entity getEntity() {
		return entity;
	}

	protected final Color getRed() {
		return red;
	}

	protected final Color getYellow() {
		return yellow;
	}

	protected final Font getFont17() {
		return font17;
	}

	protected final Font getFont14() {
		return font14;
	}

	protected final Color getGreen() {
		return green;
	}

	protected final Color getViolet() {
		return violet;
	}

	protected final Color getBlue() {
		return blue;
	}

	protected final Color getRose() {
		return rose;
	}

	protected final Color getYellowNote() {
		return yellowNote;
	}
}
