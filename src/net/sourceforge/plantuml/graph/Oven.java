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

import java.util.Random;

public class Oven {

	final private double temp;
	final private CostComputer costComputer;

	public Oven(double temp, CostComputer costComputer) {
		this.temp = temp;
		this.costComputer = costComputer;
	}

	public Board longTic(int nbTic, Board board, Random rnd) {
		double best = costComputer.getCost(board);
		Board bestBoard = board.copy();
		for (int i = 0; i < nbTic; i++) {
			final double current = tic(board, rnd);
			// Log.println("current=" + current + " best=" + best);
			if (current < best) {
				best = current;
				bestBoard = board.copy();
			}

		}
		return bestBoard;
	}

	public double tic(Board board, Random rnd) {
		// Log.println("Oven::tic");
		final double costBefore = costComputer.getCost(board);
		final Move move = null; // board.getRandomMove(rnd);
		board.applyMove(move);
		final double costAfter = costComputer.getCost(board);
		final double delta = costAfter - costBefore;
		// Log.println("delta=" + delta);
		if (delta <= 0) {
			return costAfter;
		}
		assert delta > 0;
		assert costAfter > costBefore;
		// Log.println("temp=" + temp);
		if (temp > 0) {
			final double probability = Math.exp(-delta / temp);
			final double dice = rnd.nextDouble();
			// Log.println("probability=" + probability + " dice=" +
			// dice);
			if (dice < probability) {
				// Log.println("We keep it");
				return costAfter;
			}
		}
		// Log.println("Roolback");
		board.applyMove(move.getBackMove());
		assert costBefore == costComputer.getCost(board);
		return costBefore;

	}
}
