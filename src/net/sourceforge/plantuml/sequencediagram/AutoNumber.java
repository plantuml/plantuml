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
 */
package net.sourceforge.plantuml.sequencediagram;

import java.text.DecimalFormat;

public class AutoNumber {

	private boolean running = false;
	private DottedNumber current;
	private int increment;

	private DecimalFormat format;
	private String last = "";

	public final void go(DottedNumber startingNumber, int increment, DecimalFormat format) {
		this.running = true;
		this.current = startingNumber;
		this.increment = increment;
		this.format = format;
	}

	public final void stop() {
		this.running = false;
	}

	public final void resume(DecimalFormat format) {
		this.running = true;
		if (format != null) {
			this.format = format;
		}
	}

	public final void resume(int increment, DecimalFormat format) {
		this.running = true;
		this.increment = increment;
		if (format != null) {
			this.format = format;
		}
	}

	public void incrementIntermediate() {
		current.incrementIntermediate();
	}

	public void incrementIntermediate(int position) {
		current.incrementIntermediate(position);
	}

	public String getNextMessageNumber() {
		if (running == false) {
			return null;
		}
		last = current.format(format);
		current.incrementMinor(increment);
		return last;
	}

	public String getCurrentMessageNumber(boolean formatted) {
		if (formatted) {
			return last;
		}
		return last.replace("<b>", "").replace("</b>", "");
	}
}
