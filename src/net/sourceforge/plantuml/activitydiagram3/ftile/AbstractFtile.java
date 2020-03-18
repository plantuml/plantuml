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
import java.util.List;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class AbstractFtile extends AbstractTextBlock implements Ftile {

	private final ISkinParam skinParam;

	public AbstractFtile(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	final public ISkinParam skinParam() {
		if (skinParam == null) {
			throw new IllegalStateException();
		}
		return skinParam;
	}

	final public HColorSet getIHtmlColorSet() {
		return skinParam.getIHtmlColorSet();
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
		throw new UnsupportedOperationException("" + getClass());
	}

	public final UStroke getThickness() {
		UStroke thickness = skinParam.getThickness(LineParam.activityBorder, null);
		if (thickness == null) {
			thickness = new UStroke(1.5);
		}
		return thickness;
	}

	public List<WeldingPoint> getWeldingPoints() {
		return Collections.emptyList();
	}

	public Collection<Ftile> getMyChildren() {
		throw new UnsupportedOperationException("" + getClass());
	}

	public HorizontalAlignment arrowHorizontalAlignment() {
		return skinParam.getHorizontalAlignment(AlignmentParam.arrowMessageAlignment, null, false);
	}

	private FtileGeometry cachedGeometry;

	final public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (cachedGeometry == null) {
			cachedGeometry = calculateDimensionFtile(stringBounder);
		}
		return cachedGeometry;
	}

	abstract protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder);

	@Override
	final public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
		// return getMinMaxFtile(stringBounder);
	}

	// protected MinMax getMinMaxFtile(StringBounder stringBounder) {
	// throw new UnsupportedOperationException();
	// }

}
