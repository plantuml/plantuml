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
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;

public class EntityImageTransitionLabel extends AbstractEntityImage {

	private final TextBlock textBlock;
	private static final double MARGIN = 4;

	public EntityImageTransitionLabel(Entity entity) {
		super(entity);

		final Stereotype stereotype = entity.getStereotype();

		// Use same font configuration as arrow labels
		final FontConfiguration fontConfiguration = FontConfiguration.create(getSkinParam(), FontParam.ARROW, stereotype);

		// Create text block from entity display
		final Display display = getEntity().getDisplay();

		// Ensure we have a valid display to render
		if (display != null && !Display.isNull(display)) {
			this.textBlock = display.create(fontConfiguration, HorizontalAlignment.CENTER, getSkinParam());
		} else {
			// Fallback to entity name if display is not available
			this.textBlock = Display.create(getEntity().getName()).create(fontConfiguration, HorizontalAlignment.CENTER, getSkinParam());
		}
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		try {
			// Return dimensions based on the text content plus margins
			XDimension2D textDim = textBlock.calculateDimension(stringBounder);
			return textDim.delta(MARGIN * 2, MARGIN * 2);
		} catch (Exception e) {
			// Fallback to safe default dimensions if calculation fails
			return new XDimension2D(20, 20);
		}
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dimText = textBlock.calculateDimension(stringBounder);

		// Draw a rounded rectangle background for the label
		final URectangle rect = URectangle.build(dimTotal.getWidth(), dimTotal.getHeight()).rounded(5);
		ug.draw(rect);

		// Center the text within the rectangle
		final double xText = (dimTotal.getWidth() - dimText.getWidth()) / 2;
		final double yText = (dimTotal.getHeight() - dimText.getHeight()) / 2;
		textBlock.drawU(ug.apply(new UTranslate(xText, yText)));
	}

	public ShapeType getShapeType() {
		// Use RECTANGLE for a proper node shape
		return ShapeType.RECTANGLE;
	}
}
