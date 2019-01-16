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
package net.sourceforge.plantuml.hector2.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.hector2.layering.Layer;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.IEntityImage;

public class Foo1 {

	public static Dimension2D getMaxCellDimension(StringBounder stringBounder, Layer layer, CucaDiagram diagram) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (IEntity ent : layer.entities()) {
			final IEntityImage image = computeImage((ILeaf) ent, diagram);
			final Dimension2D dim = image.calculateDimension(stringBounder);
			result = Dimension2DDouble.max(result, dim);
		}
		return result;
	}

	private static IEntityImage computeImage(final ILeaf leaf, CucaDiagram diagram) {
		final IEntityImage image = GeneralImageBuilder.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null, diagram.getLinks());
		return image;
	}

}
