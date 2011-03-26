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
 * Revision $Revision: 4246 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;

import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizLayoutStrategy;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class SkinParamBackcolored implements ISkinParam {

	final private ISkinParam skinParam;
	final private HtmlColor backColorElement;
	final private HtmlColor backColorGeneral;

	public SkinParamBackcolored(ISkinParam skinParam, HtmlColor backColorElement) {
		this(skinParam, backColorElement, null);
	}

	public SkinParamBackcolored(ISkinParam skinParam,
			HtmlColor backColorElement, HtmlColor backColorGeneral) {
		this.skinParam = skinParam;
		this.backColorElement = backColorElement;
		this.backColorGeneral = backColorGeneral;
	}

	public HtmlColor getBackgroundColor() {
		if (backColorGeneral != null) {
			return backColorGeneral;
		}
		return skinParam.getBackgroundColor();
	}

	public int getCircledCharacterRadius() {
		return skinParam.getCircledCharacterRadius();
	}

	public Font getFont(FontParam fontParam, String stereotype) {
		return skinParam.getFont(fontParam, stereotype);
	}

	public String getFontFamily(FontParam param, String stereotype) {
		return skinParam.getFontFamily(param, stereotype);
	}

	public HtmlColor getFontHtmlColor(FontParam param, String stereotype) {
		return skinParam.getFontHtmlColor(param, stereotype);
	}

	public int getFontSize(FontParam param, String stereotype) {
		return skinParam.getFontSize(param, stereotype);
	}

	public int getFontStyle(FontParam param, String stereotype) {
		return skinParam.getFontStyle(param, stereotype);
	}

	public HtmlColor getHtmlColor(ColorParam param, String stereotype) {
		if (param.isBackground() && backColorElement != null) {
			return backColorElement;
		}
		return skinParam.getHtmlColor(param, stereotype);
	}

	public String getValue(String key) {
		return skinParam.getValue(key);
	}

	public boolean isClassCollapse() {
		return skinParam.isClassCollapse();
	}

	public int classAttributeIconSize() {
		return skinParam.classAttributeIconSize();
	}

	public boolean isMonochrome() {
		return skinParam.isMonochrome();
	}

	public int getDpi() {
		return skinParam.getDpi();
	}

	public boolean useOctagonForActivity() {
		return skinParam.useOctagonForActivity();
	}

	public DotSplines getDotSplines() {
		return skinParam.getDotSplines();
	}

	public GraphvizLayoutStrategy getStrategy() {
		return skinParam.getStrategy();
	}
}
