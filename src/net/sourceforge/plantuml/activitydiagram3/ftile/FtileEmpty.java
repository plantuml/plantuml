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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class FtileEmpty extends AbstractFtile {

	private final double width;
	private final double height;
	private final Swimlane swimlaneIn;
	private final Swimlane swimlaneOut;

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.emptyList();
	}

	public FtileEmpty(ISkinParam skinParam, double width, double height) {
		this(skinParam, width, height, null, null);
	}

	public FtileEmpty(ISkinParam skinParam, double width, double height, Swimlane swimlaneIn, Swimlane swimlaneOut) {
		super(skinParam);
		this.width = width;
		this.height = height;
		this.swimlaneIn = swimlaneIn;
		this.swimlaneOut = swimlaneOut;

	}

	public FtileEmpty(ISkinParam skinParam) {
		this(skinParam, 0, 0, null, null);
	}

	public FtileEmpty(ISkinParam skinParam, Swimlane swimlane) {
		this(skinParam, 0, 0, swimlane, swimlane);
	}

	@Override
	public String toString() {
		return "FtileEmpty";
	}

	public void drawU(UGraphic ug) {
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		return calculateDimensionEmpty();
	}

	final protected FtileGeometry calculateDimensionEmpty() {
		return new FtileGeometry(width, height, width / 2, 0, height);
	}

	public Swimlane getSwimlaneIn() {
		return swimlaneIn;
	}

	public Swimlane getSwimlaneOut() {
		return swimlaneOut;
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (swimlaneIn != null) {
			result.add(swimlaneIn);
		}
		if (swimlaneOut != null) {
			result.add(swimlaneOut);
		}
		return Collections.unmodifiableSet(result);
	}

}
