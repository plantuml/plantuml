/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 4246 $
 *
 */
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class SkinParamBackcolored extends SkinParamDelegator {

	final private HtmlColor backColorElement;
	final private HtmlColor backColorGeneral;
	final private boolean forceClickage;

	public SkinParamBackcolored(ISkinParam skinParam, HtmlColor backColorElement) {
		this(skinParam, backColorElement, null, false);
	}

	public SkinParamBackcolored(ISkinParam skinParam, HtmlColor backColorElement, boolean forceClickage) {
		this(skinParam, backColorElement, null, forceClickage);
	}

	public SkinParamBackcolored(ISkinParam skinParam, HtmlColor backColorElement, HtmlColor backColorGeneral) {
		this(skinParam, backColorElement, backColorGeneral, false);
	}

	public SkinParamBackcolored(ISkinParam skinParam, HtmlColor backColorElement, HtmlColor backColorGeneral,
			boolean forceClickage) {
		super(skinParam);
		this.forceClickage = forceClickage;
		this.backColorElement = backColorElement;
		this.backColorGeneral = backColorGeneral;
	}

	@Override
	public HtmlColor getBackgroundColor() {
		if (backColorGeneral != null) {
			return backColorGeneral;
		}
		return super.getBackgroundColor();
	}

	@Override
	public HtmlColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		if (param.isBackground() && backColorElement != null) {
			return backColorElement;
		}
		if (forceClickage) {
			final HtmlColor c1 = super.getHtmlColor(param, stereotype, true);
			if (c1 != null) {
				return c1;
			}
			// clickable = true;
		}
		return super.getHtmlColor(param, stereotype, clickable);
	}

}
