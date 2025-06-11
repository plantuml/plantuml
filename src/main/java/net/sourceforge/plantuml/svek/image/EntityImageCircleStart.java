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
 *
 */
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;

public class EntityImageCircleStart extends AbstractEntityImage {

	private final CircleStart circle;

	public StyleSignatureBasic getDefaultStyleDefinitionCircle() {
		return StyleSignatureBasic.of(SName.root, SName.element, getSkinParam().getUmlDiagramType().getStyleName(),
				SName.circle, SName.start);
	}

	public EntityImageCircleStart(Entity entity) {
		super(entity);
		final Style style = getDefaultStyleDefinitionCircle().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		this.circle = new CircleStart(getSkinParam(), style, entity.getColors());
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return circle.calculateDimension(stringBounder);
	}

	final public void drawU(UGraphic ug) {
		circle.drawU(ug);
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
