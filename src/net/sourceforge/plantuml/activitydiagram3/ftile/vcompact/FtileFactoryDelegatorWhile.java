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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.ugraphic.UFont;

public class FtileFactoryDelegatorWhile extends FtileFactoryDelegator {

	public FtileFactoryDelegatorWhile(FtileFactory factory, ISkinParam skinParam) {
		super(factory, skinParam);
	}

	private static boolean NEW = true;

	@Override
	public Ftile createWhile(Swimlane swimlane, Ftile whileBlock, Display test, Display yes, Display out, LinkRendering afterEndwhile) {
		final HtmlColor borderColor = getRose().getHtmlColor(getSkinParam(), ColorParam.activityBorder);
		final HtmlColor backColor = getRose().getHtmlColor(getSkinParam(), ColorParam.activityBackground);
		final HtmlColor arrowColor = getRose().getHtmlColor(getSkinParam(), ColorParam.activityArrow);
		final UFont font = getSkinParam().getFont(FontParam.ACTIVITY_ARROW2, null);
		final LinkRendering endInlinkRendering = whileBlock.getOutLinkRendering();
		final HtmlColor endInlinkColor = endInlinkRendering == null ? arrowColor : endInlinkRendering.getColor();
		if (NEW) {
			final ConditionStyle conditionStyle = getSkinParam().getConditionStyle();
			return FtileWhile2.create(swimlane, whileBlock, test, borderColor, backColor, arrowColor, yes, out, font,
					endInlinkColor, afterEndwhile, getFactory(), conditionStyle);
		}
		return FtileWhile.create(whileBlock, test, borderColor, backColor, arrowColor, yes, out, font, endInlinkColor,
				afterEndwhile, getFactory());
	}

}
