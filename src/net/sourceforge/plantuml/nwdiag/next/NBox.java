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
package net.sourceforge.plantuml.nwdiag.next;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NBox implements Staged {

	private final List<NBar> bars = new ArrayList<>();
	private final NTetris<NBar> tetris = new NTetris<>();

	public void add(NBar bar) {
		if (this.bars.contains(bar))
			return;

		this.bars.add(bar);
		this.tetris.add(bar);
	}

	@Override
	public NStage getStart() {
		NStage result = bars.get(0).getStart();
		for (int i = 1; i < bars.size(); i++)
			result = NStage.getMin(result, bars.get(i).getStart());

		return result;
	}

	@Override
	public NStage getEnd() {
		NStage result = bars.get(0).getEnd();
		for (int i = 1; i < bars.size(); i++)
			result = NStage.getMax(result, bars.get(i).getEnd());

		return result;
	}

	@Override
	public int getNWidth() {
		return tetris.getNWidth();
	}

	public Map<NBar, Integer> getPositions() {
		return tetris.getPositions();
	}

	@Override
	public boolean contains(NStage stage) {
		return stage.compareTo(getStart()) >= 0 && stage.compareTo(getEnd()) <= 0;
	}

}
