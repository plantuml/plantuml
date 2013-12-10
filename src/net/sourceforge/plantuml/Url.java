/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 6396 $
 *
 */
package net.sourceforge.plantuml;

import java.util.Comparator;

public class Url implements EnsureVisible {

	private final String url;
	private final String tooltip;
	private final String label;
	private boolean member;

	public Url(String url, String tooltip) {
		this(url, tooltip, null);
	}

	public Url(String url, String tooltip, String label) {
		if (url.contains("{")) {
			throw new IllegalArgumentException(url);
		}
		this.url = url;
		if (tooltip == null) {
			this.tooltip = url;
		} else {
			this.tooltip = tooltip;
		}
		if (label == null) {
			this.label = url;
		} else {
			this.label = label;
		}
	}

	public final String getUrl() {
		return url;
	}

	public final String getTooltip() {
		return tooltip;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return super.toString() + " " + url + " " + visible.getCoords(1.0);
	}

	public String getCoords(double scale) {
		return visible.getCoords(scale);
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public final boolean isMember() {
		return member;
	}

	private final BasicEnsureVisible visible = new BasicEnsureVisible();

	public void ensureVisible(double x, double y) {
		visible.ensureVisible(x, y);
	}

	public static final Comparator<Url> SURFACE_COMPARATOR = new Comparator<Url>() {
		public int compare(Url url1, Url url2) {
			final double surface1 = url1.visible.getSurface();
			final double surface2 = url2.visible.getSurface();
			if (surface1 > surface2) {
				return 1;
			} else if (surface1 < surface2) {
				return -1;
			}
			return 0;
		}
	};

}
