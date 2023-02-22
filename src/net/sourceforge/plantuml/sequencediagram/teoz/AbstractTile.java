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
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;

public abstract class AbstractTile extends CommonTile implements Tile {

	public AbstractTile(StringBounder stringBounder, YGauge currentY) {
		super(stringBounder);
		if (YGauge.USE_ME)
			System.err.println("CREATING " + getClass());
	}

	public AbstractTile(StringBounder stringBounder) {
		super(stringBounder);
	}

	@Override
	public YGauge getYGauge() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public double getContactPointRelative() {
		return -1;
	}

	final public double getZZZ() {
		final double result = getPreferredHeight() - getContactPointRelative();
		assert result >= 0;
		return result;
	}

	public boolean matchAnchor(String anchor) {
		final Event event = this.getEvent();
		if (event instanceof AbstractMessage) {
			final AbstractMessage msg = (AbstractMessage) event;
			if (anchor.equals(msg.getAnchor()))
				return true;

		}
		return false;
	}

}
