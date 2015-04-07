/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class FtileDecorate implements Ftile {

	final private Ftile ftile;

	public FtileDecorate(final Ftile ftile) {
		this.ftile = ftile;
	}

	@Override
	public String toString() {
		return "" + getClass() + " " + ftile;
	}

	public LinkRendering getOutLinkRendering() {
		return ftile.getOutLinkRendering();
	}

	public LinkRendering getInLinkRendering() {
		return ftile.getInLinkRendering();
	}
	
	public void drawU(UGraphic ug) {
		ftile.drawU(ug);
	}

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		return ftile.calculateDimension(stringBounder);
	}

	public Collection<Connection> getInnerConnections() {
		return ftile.getInnerConnections();
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		return ftile.getTranslateFor(child, stringBounder);
	}

	public Set<Swimlane> getSwimlanes() {
		return ftile.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return ftile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return ftile.getSwimlaneOut();
	}

	public boolean shadowing() {
		return ftile.shadowing();
	}
	
	protected final Ftile getFtileDelegated() {
		return ftile;
	}

}
