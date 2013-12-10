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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

import java.util.List;

public class Jalon implements Item {

	private Instant begin;
	private final String code;
	private final Item parent;

	public Jalon(String code, Item parent) {
		this.code = code;
		this.parent = parent;
	}

	public Instant getBegin() {
		return begin;
	}

	public Instant getCompleted() {
		return begin;
	}

	public Duration getDuration() {
		return new Duration(0);
	}

	public Load getLoad() {
		return new Load(0);
	}

	public NumericNumber getWork() {
		return new NumericNumber(1);
	}

	public boolean isLeaf() {
		return true;
	}

	public Item getParent() {
		return parent;
	}

	public List<Item> getChildren() {
		return null;
	}

	public String getCode() {
		return code;
	}

	public boolean isValid() {
		return begin != null;
	}

	public void setInstant(Numeric value) {
		this.begin = (Instant) value;
	}

}
