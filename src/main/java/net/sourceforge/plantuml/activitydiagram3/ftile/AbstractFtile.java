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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.style.ISkinParam;

public abstract class AbstractFtile extends AbstractTextBlock implements Ftile {

	protected final boolean TRACE = false;

	private final ISkinParam skinParam;

	public AbstractFtile(ISkinParam skinParam) {
		this.skinParam = skinParam;

		if (TRACE)
			System.err.println("TRACE Building " + this);
	}

	final public ISkinParam skinParam() {
		if (skinParam == null)
			throw new IllegalStateException();

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

	public List<WeldingPoint> getWeldingPoints() {
		return Collections.emptyList();
	}

	public Collection<Ftile> getMyChildren() {
		throw new UnsupportedOperationException("" + getClass());
	}

	public HorizontalAlignment arrowHorizontalAlignment() {
		return skinParam.getHorizontalAlignment(AlignmentParam.arrowMessageAlignment, null, false, null);
	}

	private FtileGeometry cachedGeometry;

	final public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (cachedGeometry == null)
			cachedGeometry = calculateDimensionFtile(stringBounder);

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
