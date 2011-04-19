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
 * Revision $Revision: 6483 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class GroupingLeaf extends Grouping {

	private final GroupingStart start;
	private final HtmlColor backColorGeneral;

	public GroupingLeaf(String title, String comment, GroupingType type, HtmlColor backColorGeneral,
			HtmlColor backColorElement, GroupingStart start) {
		super(title, comment, type, backColorElement);
		if (start == null) {
			throw new IllegalArgumentException();
		}
		this.backColorGeneral = backColorGeneral;
		this.start = start;
		start.addChildren(this);
	}

	public Grouping getJustBefore() {
		final int idx = start.getChildren().indexOf(this);
		if (idx == -1) {
			throw new IllegalStateException();
		}
		if (idx == 0) {
			return start;
		}
		return start.getChildren().get(idx - 1);
	}

	@Override
	public int getLevel() {
		return start.getLevel();
	}

	@Override
	public final HtmlColor getBackColorGeneral() {
		if (backColorGeneral == null) {
			return start.getBackColorGeneral();
		}
		return backColorGeneral;
	}

	public boolean dealWith(Participant someone) {
		return false;
	}

	public Url getUrl() {
		return null;
	}

}
