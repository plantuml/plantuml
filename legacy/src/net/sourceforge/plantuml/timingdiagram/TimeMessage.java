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
 */
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.decoration.WithLinkType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class TimeMessage extends WithLinkType {
    // ::remove folder when __HAXE__

	private final TickInPlayer tickInPlayer1;
	private final TickInPlayer tickInPlayer2;
	private final Display label;
	private final ISkinParam skinParam;
	private final StyleBuilder styleBuilder;

	public TimeMessage(TickInPlayer tickInPlayer1, TickInPlayer tickInPlayer2, String label, ISkinParam skinParam) {
		this.skinParam = skinParam;
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
		this.tickInPlayer1 = tickInPlayer1;
		this.tickInPlayer2 = tickInPlayer2;
		this.label = Display.getWithNewlines(label);
		this.setSpecificColor(getColor());
		this.type = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
	}

	@Override
	public UStroke getUStroke() {
		if (styleBuilder == null) {
			return UStroke.withThickness(1.5);
		}
		return getStyle().getStroke();
	}

	private HColor getColor() {
//		return HColorUtils.BLUE;
		if (styleBuilder == null) {
			return HColors.MY_RED;
		}
		return getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(styleBuilder);
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.arrow);
	}

	public final Player getPlayer1() {
		return tickInPlayer1.getPlayer();
	}

	public final Player getPlayer2() {
		return tickInPlayer2.getPlayer();
	}

	public final TimeTick getTick1() {
		return tickInPlayer1.getTick();
	}

	public final TimeTick getTick2() {
		return tickInPlayer2.getTick();
	}

	public final Display getLabel() {
		return label;
	}

	@Override
	public void goNorank() {
		// Nothing to do
	}

}
