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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class AbstractFtile extends AbstractTextBlock implements Ftile {

	private final boolean shadowing;
	private final ISkinParam skinParam;

	private AbstractFtile(boolean shadowing) {
		this.shadowing = shadowing;
		this.skinParam = null;
	}

	public AbstractFtile(ISkinParam skinParam) {
		this.shadowing = skinParam.shadowing();
		this.skinParam = skinParam;
	}

	final public ISkinParam skinParam() {
		if (skinParam == null) {
			throw new IllegalStateException();
		}
		return skinParam;
	}

	public LinkRendering getInLinkRendering() {
		return LinkRendering.none();
	}

	public LinkRendering getOutLinkRendering() {
		return LinkRendering.none();
	}

	public Collection<Connection> getInnerConnections() {
		return Collections.emptyList();
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public final UStroke getThickness() {
		UStroke thickness = skinParam.getThickness(LineParam.activityBorder, null);
		if (thickness == null) {
			thickness = new UStroke(1.5);
		}
		return thickness;
	}

}
