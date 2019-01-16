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
package net.sourceforge.plantuml.sequencediagram.puma;

import java.util.ArrayList;
import java.util.Collection;

public class PUnivers {

	private final Collection<PSegment> all = new ArrayList<PSegment>();
	private final Collection<FixedLink> links = new ArrayList<FixedLink>();

	public PSegment createPSegment(double minsize) {
		final PSegment result = new PSegment(minsize);
		all.add(result);
		return result;
	}

	public void addFixedLink(PSegment segment1, double position1, PSegment segment2, double position2) {
		final FixedLink link = new FixedLink(new SegmentPosition(segment1, position1), new SegmentPosition(segment2,
				position2));
		links.add(link);

	}

	public void solve() {
		boolean changed = false;
		do {
			changed = false;
			for (FixedLink link : links) {
				if (link.pushIfNeed()) {
					changed = true;
				}
			}
		} while (changed);

	}
}
