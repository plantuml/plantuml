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
 * With assistance from ChatGPT (OpenAI)
 *
 */
package net.sourceforge.plantuml.png.quant;

/**
 * Represents an RGB555 color cube.
 * 
 * Each cube corresponds to a 18-bit color (6 bits per channel, 64 × 64 × 64 = 262,144 possible cubes).
 * Inside a cube, we track the frequency of up to 64 "sub-colors" (for example,
 * finer subdivisions or variations of colors mapped to this cube).
 *
 * This structure is mainly used during color quantization: it allows us to
 * count occurrences of colors belonging to the same coarse cube, and later
 * select the most representative sub-color.
 */
public final class Cube666 {

    /** Frequency table for all possible sub-colors inside this cube (2 bits per channel => 4*4*4 = 64). */
	final int[] counts = new int[64];
	
	/** The RGB555 index of this cube (18-bit compressed representation of a color). */
	final int rgb666;

	/**
	 * Creates a cube corresponding to the given RGB555 color index.
	 * 
	 * @param rgb555 the compressed 18-bit RGB index of this cube
	 */
	public Cube666(int rgb666) {
		this.rgb666 = rgb666;
	}

	/**
	 * Increments the count of a specific sub-color.
	 * 
	 * @param subIndex index of the sub-color (0..63)
	 */
	void increment(int subIndex) {
		counts[subIndex]++;
	}


	/**
	 * Finds the sub-color with the highest frequency inside this cube.
	 *
	 * @return the index (0..63) of the most frequent sub-color
	 */
	int best() {
		int best = 0, besti = 0;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > best) {
				best = counts[i];
				besti = i;
			}

		}
		return besti;
	}
}