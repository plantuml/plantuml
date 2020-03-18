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
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.hector2.MinMax;
import net.sourceforge.plantuml.hector2.layering.Layer;
import net.sourceforge.plantuml.hector2.mpos.Distribution;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Foo2 extends AbstractTextBlock implements TextBlock {

	private final Distribution distribution;
	private final CucaDiagram diagram;

	public Foo2(Distribution distribution, CucaDiagram diagram) {
		this.distribution = distribution;
		this.diagram = diagram;
	}

	public Dimension2D getMaxCellDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (Layer layer : distribution.getLayers()) {
			final Dimension2D dim = Foo1.getMaxCellDimension(stringBounder, layer, diagram);
			result = Dimension2DDouble.max(result, dim);
		}
		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D cell = getMaxCellDimension(stringBounder);
		final MinMax longitudes = distribution.getMinMaxLongitudes();
		final double width = (longitudes.getDiff() + 2) * cell.getWidth() / 2;
		final double height = cell.getHeight() * distribution.getNbLayers();
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D cell = getMaxCellDimension(stringBounder);
		for (Layer layer : distribution.getLayers()) {
			drawLayer(ug, layer, cell.getWidth(), cell.getHeight());
			ug = ug.apply(UTranslate.dy(cell.getHeight()));
		}
	}

	private void drawLayer(UGraphic ug, Layer layer, double w, double h) {
		for (IEntity ent : layer.entities()) {
			final IEntityImage image = computeImage((ILeaf) ent);
			final int longitude = layer.getLongitude(ent);
			final Dimension2D dimImage = image.calculateDimension(ug.getStringBounder());
			final double diffx = w - dimImage.getWidth();
			final double diffy = h - dimImage.getHeight();
			image.drawU(ug.apply(new UTranslate(w * longitude / 2 + diffx / 2, diffy / 2)));
		}
	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = GeneralImageBuilder.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null, diagram.getLinks());
		return image;
	}

}
