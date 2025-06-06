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
package net.sourceforge.plantuml.url;

import java.util.Comparator;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.geom.BasicEnsureVisible;
import net.sourceforge.plantuml.klimt.geom.EnsureVisible;

public class Url implements EnsureVisible {
	private final String url;
	private final String tooltip;
	private final String label;
	private boolean member;

	public Url(String url, String tooltip, String label) {
		url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(url, "\"");
		this.url = url;
		// ::revert when __HAXE__
		if (tooltip == null)
			this.tooltip = url;
		else
			this.tooltip = tooltip;
		// this.tooltip = url;
		// ::done

		if (label == null || label.length() == 0)
			this.label = url;
		else
			this.label = label;

	}

	public static boolean isLatex(String pendingUrl) {
		return pendingUrl.startsWith("latex://");
	}

	public boolean isLatex() {
		return isLatex(url);
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

	// ::comment when __HAXE__
	@Override
	public String toString() {
		return super.toString() + " " + url + " " + visible.getCoords(1.0);
	}
	// ::done

	public String getCoords(double scale) {
		// ::comment when __CORE__ or __HAXE__
		if (Check.isJunit() && visible.getCoords(1.0).contains("0,0,0,0"))
			throw new IllegalStateException(toString());
		// ::done

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

	public boolean hasData() {
		return visible.hasData();
	}

	public static final Comparator<Url> SURFACE_COMPARATOR = new Comparator<Url>() {
		public int compare(Url url1, Url url2) {
			final double surface1 = url1.visible.getSurface();
			final double surface2 = url2.visible.getSurface();
			if (surface1 > surface2)
				return 1;
			else if (surface1 < surface2)
				return -1;

			return 0;
		}
	};

}
