/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class Grouping implements Event {

	private final String title;
	private final GroupingType type;
	private final String comment;
	private final HtmlColor backColorElement;

	public Grouping(String title, String comment, GroupingType type,
			HtmlColor backColorElement) {
		this.title = title;
		this.comment = comment;
		this.type = type;
		this.backColorElement = backColorElement;
	}

	@Override
	public final String toString() {
		return super.toString() + " " + type + " " + title;
	}

	final public String getTitle() {
		return title;
	}

	final public GroupingType getType() {
		return type;
	}

	public abstract int getLevel();

	public abstract HtmlColor getBackColorGeneral();
	
	final public String getComment() {
		return comment;
	}

	public final HtmlColor getBackColorElement() {
		return backColorElement;
	}
	
	public abstract boolean isParallel(); 

}
