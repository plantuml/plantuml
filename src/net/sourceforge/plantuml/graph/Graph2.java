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
 * Revision $Revision: 3833 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.StringBounderUtils;

public class Graph2 {

	final private static Graphics2D dummyGraphics2D;

	private final Elastane elastane;
	private int widthCell;
	private int heightCell;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		dummyGraphics2D = builder.getGraphics2D();
	}

	public Graph2(Board board) {
		board.normalize();

		for (ANode n : board.getNodes()) {
			final Dimension2D dim = images(n).getDimension(StringBounderUtils.asStringBounder(dummyGraphics2D));
			widthCell = Math.max(widthCell, (int) dim.getWidth());
			heightCell = Math.max(heightCell, (int) dim.getHeight());
		}
		final Galaxy4 galaxy = new Galaxy4(board, widthCell, heightCell);
		elastane = new Elastane(galaxy);

		for (ANode n : board.getNodes()) {
			final Dimension2D dim = images(n).getDimension(StringBounderUtils.asStringBounder(dummyGraphics2D));
			elastane.addBox(n, (int) dim.getWidth(), (int) dim.getHeight());
		}

		final List<ALink> links = new ArrayList<ALink>(board.getLinks());
		Collections.sort(links, board.getLinkComparator());
		for (ALink link : links) {
			galaxy.addLink(link);
		}

		elastane.init();

	}

	private AbstractEntityImage images(ANode n) {
		return new EntityImageFactory().createEntityImage(((Entity) n.getUserData()));
	}

	public Dimension2D getDimension() {
		return elastane.getDimension();

	}

	public void draw(final Graphics2D g2d) {
		elastane.draw(g2d);
	}

}
