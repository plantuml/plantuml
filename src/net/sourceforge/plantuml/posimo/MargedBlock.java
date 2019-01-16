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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.StringBounder;

public class MargedBlock {

	private final Block block;
	private final IEntityImageBlock imageBlock;
	private final double marginDecorator;
	private final Dimension2D imageDimension;

	static private int uid = 1;

	public MargedBlock(StringBounder stringBounder, IEntityImageBlock imageBlock, double marginDecorator, Cluster parent) {
		this.imageBlock = imageBlock;
		this.marginDecorator = marginDecorator;
		this.imageDimension = imageBlock.getDimension(stringBounder);
		this.block = new Block(uid++, imageDimension.getWidth() + 2 * marginDecorator, imageDimension.getHeight() + 2
				* marginDecorator, parent);
	}

	public Block getBlock() {
		return block;
	}

	public double getMarginDecorator() {
		return marginDecorator;
	}

	public IEntityImageBlock getImageBlock() {
		return imageBlock;
	}

	public Positionable getImagePosition() {
		return new Positionable() {

			public Dimension2D getSize() {
				return imageDimension;
			}

			public Point2D getPosition() {
				final Point2D pos = block.getPosition();
				return new Point2D.Double(pos.getX() + marginDecorator, pos.getY() + marginDecorator);
			}

			public void moveSvek(double deltaX, double deltaY) {
				throw new UnsupportedOperationException();
			}
		};
	}

}
