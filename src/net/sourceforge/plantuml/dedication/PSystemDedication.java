/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.util.Objects;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.klimt.UImage;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class PSystemDedication extends PlainDiagram {

	private final BufferedImage img;

	public PSystemDedication(UmlSource source, BufferedImage img) {
		super(source);
		this.img = Objects.requireNonNull(img);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		// return ug -> ug.draw(new UImage(new PixelImage(img, AffineTransformType.TYPE_BILINEAR)));
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug.draw(new UImage(new PixelImage(img, AffineTransformType.TYPE_BILINEAR)));
			}
		};
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Dedication)");
	}

}
