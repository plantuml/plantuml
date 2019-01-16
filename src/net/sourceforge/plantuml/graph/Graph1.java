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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Graph1 {

	private final Board board;
	private final int widthCell = 40;
	private final int heightCell = 40;

	public Graph1(Board board) {
		this.board = board;
	}

	public BufferedImage createBufferedImage() {
		final BufferedImage im = new BufferedImage(widthCell * 15, heightCell * 15, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = im.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, im.getWidth(), im.getHeight());

		g2d.setColor(Color.BLACK);
		for (ANode n : board.getNodes()) {
			final int x = board.getCol(n) * widthCell;
			final int y = n.getRow() * heightCell;
			g2d.drawString(n.getCode(), x + 5, y + heightCell / 2 - 5);
			g2d.drawOval(x, y, widthCell / 2, heightCell / 2);
		}

		for (ALink link : board.getLinks()) {
			final ANode n1 = link.getNode1();
			final ANode n2 = link.getNode2();
			final int x1 = 10 + board.getCol(n1) * widthCell;
			final int y1 = 10 + n1.getRow() * heightCell;
			final int x2 = 10 + board.getCol(n2) * widthCell;
			final int y2 = 10 + n2.getRow() * heightCell;
			g2d.drawLine(x1, y1, x2, y2);

		}

		return im;

	}

}
