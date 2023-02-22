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

import java.util.Objects;

public class NBar implements Staged {

	private NBox parent;
	private NStage start;
	private NStage end;

	@Override
	public String toString() {
		return start + "->" + end;
	}

	public final NBox getParent() {
		return parent;
	}

	public final void setParent(NBox parent) {
		this.parent = parent;
	}

	@Override
	public final NStage getStart() {
		return start;
	}

	@Override
	public final NStage getEnd() {
		return end;
	}

	public void addStage(NStage stage) {
		Objects.requireNonNull(stage);
		if (start == null && end == null) {
			this.start = stage;
			this.end = stage;
		} else {
			this.start = NStage.getMin(this.start, stage);
			this.end = NStage.getMax(this.end, stage);
		}
	}

	@Override
	public int getNWidth() {
		return 1;
	}

	@Override
	public boolean contains(NStage stage) {
		return stage.compareTo(start) >= 0 && stage.compareTo(end) <= 0;
	}

}
