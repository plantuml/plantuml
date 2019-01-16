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
package net.sourceforge.plantuml.sequencediagram.graphic;

import net.sourceforge.plantuml.cucadiagram.Display;

public final class Page {

	private final double headerHeight;
	private final double newpage1;
	private final double newpage2;
	private final double tailHeight;
	private final double signatureHeight;
	private final Display title;

	@Override
	public String toString() {
		return "headerHeight=" + headerHeight + " newpage1=" + newpage1 + " newpage2=" + newpage2;
	}

	public Page(double headerHeight, double newpage1, double newpage2, double tailHeight,
			double signatureHeight, Display title) {
		if (headerHeight < 0) {
			throw new IllegalArgumentException();
		}
		if (tailHeight < 0) {
			throw new IllegalArgumentException();
		}
		if (signatureHeight < 0) {
			throw new IllegalArgumentException();
		}
		if (newpage1 > newpage2) {
			throw new IllegalArgumentException();
		}
		this.headerHeight = headerHeight;
		this.newpage1 = newpage1;
		this.newpage2 = newpage2;
		this.tailHeight = tailHeight;
		this.signatureHeight = signatureHeight;
		this.title = title;
	}

	public double getHeight() {
		return headerHeight + getBodyHeight() + tailHeight + signatureHeight;
	}

	public double getHeaderRelativePosition() {
		return 0;
	}

	public double getBodyRelativePosition() {
		return getHeaderRelativePosition() + headerHeight;
	}

	public double getBodyHeight() {
		return newpage2 - newpage1;
	}

	public double getTailRelativePosition() {
		return getBodyRelativePosition() + getBodyHeight();
	}

	public double getSignatureRelativePosition() {
		if (displaySignature() == false) {
			return -1;
		}
		return getTailRelativePosition() + tailHeight;
	}

	public boolean displaySignature() {
		return signatureHeight > 0;
	}

	public double getNewpage1() {
		return newpage1;
	}

	public double getNewpage2() {
		return newpage2;
	}

	public double getHeaderHeight() {
		return headerHeight;
	}

	public final Display getTitle() {
		return title;
	}

}
