/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

abstract class AbstractEntityImage2 implements IEntityImageBlock {

	private final IEntity entity;
	private final ISkinParam skinParam;
	
	private final Rose rose = new Rose();

	public AbstractEntityImage2(IEntity entity, ISkinParam skinParam) {
		if (entity == null) {
			throw new IllegalArgumentException("entity null");
		}
		this.entity = entity;
		this.skinParam = skinParam;
	}

	public abstract Dimension2D getDimension(StringBounder stringBounder);

	protected final IEntity getEntity() {
		return entity;
	}

	protected UFont getFont(FontParam fontParam) {
		return skinParam.getFont(fontParam, null, false);
	}

	protected HtmlColor getFontColor(FontParam fontParam) {
		return skinParam.getFontHtmlColor(fontParam, null);
	}

	protected final HtmlColor getColor(ColorParam colorParam) {
		return rose.getHtmlColor(skinParam, colorParam);
	}

	protected final ISkinParam getSkinParam() {
		return skinParam;
	}
}
