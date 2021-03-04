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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.Style;

public class BodyFactory {

	public final static boolean BODY3 = false;

	public static Bodier createLeaf(LeafType type, Set<VisibilityModifier> hides) {
		if (type.isLikeClass() || type == LeafType.OBJECT) {
			return new BodierLikeClassOrObject(type, hides);
		}
		return new BodierSimple();
	}

	public static Bodier createGroup(Set<VisibilityModifier> hides) {
		return new BodierSimple();
	}

	public static TextBlock create1(HorizontalAlignment align, List<CharSequence> rawBody, FontParam fontParam,
			ISkinParam skinParam, Stereotype stereotype, ILeaf entity, Style style) {
		return new BodyEnhanced1(align, rawBody, fontParam, skinParam, stereotype, entity, style);
	}

	public static TextBlock create2(HorizontalAlignment align, Display display, FontParam fontParam,
			ISkinParam skinParam, Stereotype stereotype, ILeaf entity, Style style) {
		return new BodyEnhanced1(align, display, fontParam, skinParam, stereotype, entity, style);
	}

	public static TextBlock create3(Display rawBody, FontParam fontParam, ISkinSimple skinParam,
			HorizontalAlignment align, FontConfiguration titleConfig, LineBreakStrategy lineBreakStrategy) {
		return new BodyEnhanced2(rawBody, fontParam, skinParam, align, titleConfig, lineBreakStrategy);
	}

}
