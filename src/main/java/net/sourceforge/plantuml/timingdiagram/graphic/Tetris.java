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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.math.BigDecimal;

/**
 * Manages vertical stacking of rectangular pieces, inspired by the Tetris game.
 * 
 * <p>This class handles a collection of pieces that "fall" vertically and stack
 * on top of each other. Each piece is defined by:
 * <ul>
 *   <li>A default Y position (where it would land if there were no obstacles)</li>
 *   <li>A horizontal span [x1, x2]</li>
 *   <li>A thickness (height of the piece)</li>
 * </ul>
 * 
 * <p><b>Coordinate system:</b> The Y-axis points downward, meaning the "top"
 * of the screen corresponds to lower (possibly negative) Y values.
 * When a piece is blocked by another, its final Y position will be
 * <em>lower</em> than its default position.
 * 
 * <p><b>Responsibilities:</b>
 * <ul>
 *   <li>Store added pieces with their dimensions</li>
 *   <li>Compute the actual Y position of each piece considering collisions</li>
 *   <li>Allow retrieval of each piece's final position by index</li>
 * </ul>
 */
public class Tetris {

	/**
	 * Adds a new piece and computes its final vertical position.
	 * 
	 * <p>The piece is characterized by:
	 * <ul>
	 *   <li>{@code defaultY}: desired Y position if no collision occurs</li>
	 *   <li>{@code x1}, {@code x2}: horizontal bounds of the piece</li>
	 *   <li>{@code thickness}: height of the piece</li>
	 * </ul>
	 * 
	 * <p><b>Expected algorithm:</b>
	 * <ol>
	 *   <li>Create an internal representation of the piece</li>
	 *   <li>Find collisions with already placed pieces whose horizontal
	 *       span overlaps [x1, x2]</li>
	 *   <li>Compute the lowest possible Y position without overlap</li>
	 *   <li>Store the computed actual Y position for this piece</li>
	 * </ol>
	 * 
	 * @param defaultY   default Y position (when no obstacle)
	 * @param x1         left horizontal bound of the piece
	 * @param x2         right horizontal bound of the piece
	 * @param thickness  height of the piece
	 */
	public void dropPiece(double defaultY, BigDecimal x1, BigDecimal x2, double thickness) {
		// TODO: Implement piece addition and position calculation
	}

	/**
	 * Returns the actual Y position (after stacking) of the piece at the given index.
	 * 
	 * @param idx  piece index (in insertion order, starting at 0)
	 * @return     the final Y position computed when the piece was added
	 */
	public double getY(int idx) {
		// TODO: Implement position retrieval
		return 0;
	}

}
