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

public class SkinParamBackcoloredReference extends SkinParamDelegator {

	final private HtmlColor sequenceReferenceHeaderBackground;
	final private HtmlColor sequenceReferenceBackground;

	public SkinParamBackcoloredReference(ISkinParam skinParam, HtmlColor sequenceReferenceHeaderBackground,
			HtmlColor sequenceReferenceBackground) {
		super(skinParam);
		this.sequenceReferenceBackground = sequenceReferenceBackground;
		this.sequenceReferenceHeaderBackground = sequenceReferenceHeaderBackground;
	}

	@Override
	public HtmlColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		if (param == ColorParam.sequenceReferenceHeaderBackground && sequenceReferenceHeaderBackground != null) {
			return sequenceReferenceHeaderBackground;
		}
		if (param == ColorParam.sequenceReferenceBackground && sequenceReferenceBackground != null) {
			return sequenceReferenceBackground;
		}
		return super.getHtmlColor(param, stereotype, clickable);
	}

}
