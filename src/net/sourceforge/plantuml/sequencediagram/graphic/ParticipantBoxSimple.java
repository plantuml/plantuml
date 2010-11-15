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
 * Revision $Revision: 5249 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.graphic.StringBounder;

class ParticipantBoxSimple implements Pushable {

	private double pos = 0;
	private final String name;

	public ParticipantBoxSimple(double pos) {
		this(pos, null);
	}

	public ParticipantBoxSimple(double pos, String name) {
		this.pos = pos;
		this.name = name;
	}

	@Override
	public String toString() {
		return name == null ? super.toString() : name;
	}

	public double getCenterX(StringBounder stringBounder) {
		return pos;
	}

	public void pushToLeft(double deltaX) {
		pos += deltaX;
	}
	
	public double getPreferredWidth(StringBounder stringBounder) {
		return 0;
	}


}
