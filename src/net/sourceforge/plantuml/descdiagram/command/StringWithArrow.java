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
 * Contribution :  Hisashi Miyashita
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockArrow2;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.svek.GuideLine;

public class StringWithArrow {

	private final String label;
	private final LinkArrow linkArrow;

	public StringWithArrow(String completeLabel) {
		if (completeLabel == null) {
			this.linkArrow = LinkArrow.NONE_OR_SEVERAL;
			this.label = null;
			return;
		}
		completeLabel = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(completeLabel, "\"");
		if (Display.hasSeveralGuideLines(completeLabel)) {
			this.linkArrow = LinkArrow.NONE_OR_SEVERAL;
			this.label = completeLabel;
		} else {

			if ("<".equals(completeLabel)) {
				this.linkArrow = LinkArrow.BACKWARD;
				this.label = null;
			} else if (">".equals(completeLabel)) {
				this.linkArrow = LinkArrow.DIRECT_NORMAL;
				this.label = null;
			} else if (completeLabel.startsWith("< ")) {
				this.linkArrow = LinkArrow.BACKWARD;
				this.label = StringUtils.trin(completeLabel.substring(2));
			} else if (completeLabel.startsWith("> ")) {
				this.linkArrow = LinkArrow.DIRECT_NORMAL;
				this.label = StringUtils.trin(completeLabel.substring(2));
			} else if (completeLabel.endsWith(" >")) {
				this.linkArrow = LinkArrow.DIRECT_NORMAL;
				this.label = StringUtils.trin(completeLabel.substring(0, completeLabel.length() - 2));
			} else if (completeLabel.endsWith(" <")) {
				this.linkArrow = LinkArrow.BACKWARD;
				this.label = StringUtils.trin(completeLabel.substring(0, completeLabel.length() - 2));
			} else {
				this.linkArrow = LinkArrow.NONE_OR_SEVERAL;
				this.label = completeLabel;
			}
		}
	}

	public final String getLabel() {
		return label;
	}

	public final LinkArrow getLinkArrow() {
		return linkArrow;
	}

	public final Display getDisplay() {
		return Display.getWithNewlines(label);
	}

	static public TextBlock addMagicArrow(TextBlock label, GuideLine guide, FontConfiguration font) {
		final TextBlock arrow = new TextBlockArrow2(guide, font);
		return TextBlockUtils.mergeLR(arrow, label, VerticalAlignment.CENTER);
	}

	static private TextBlock addMagicArrow2(TextBlock label, GuideLine guide, FontConfiguration font) {
		final TextBlock arrow = new TextBlockArrow2(guide, font);
		return TextBlockUtils.mergeLR(arrow, label, VerticalAlignment.CENTER);
	}
	
	/*
	static public TextBlock addMagicArrow2(TextBlock label, GuideLine guide, FontConfiguration font) {
		final TextBlock arrowRight = new TextBlockArrow(Direction.RIGHT, font);
		final TextBlock arrowLeft = new TextBlockArrow(Direction.LEFT, font);
		final TextBlock arrowUp = new TextBlockArrow(Direction.UP, font);
		final TextBlock arrowDown = new TextBlockArrow(Direction.DOWN, font);
		final TextBlock right = TextBlockUtils.mergeLR(label, arrowRight, VerticalAlignment.CENTER);
		final TextBlock left = TextBlockUtils.mergeLR(arrowLeft, label, VerticalAlignment.CENTER);
		final TextBlock up = TextBlockUtils.mergeLR(arrowUp, label, VerticalAlignment.CENTER);
		final TextBlock down = TextBlockUtils.mergeLR(label, arrowDown, VerticalAlignment.CENTER);
		return new DirectionalTextBlock(guide, right, left, up, down);
	}

	static public TextBlock addMagicArrow(TextBlock label, GuideLine guide, FontConfiguration font) {
		final TextBlock arrow = new TextBlockArrow2(guide, font);
		return TextBlockUtils.mergeLR(label, arrow, VerticalAlignment.CENTER);
	}

	public static TextBlock addSeveralMagicArrows(Display label, GuideLine guide, FontConfiguration font,
			HorizontalAlignment alignment, ISkinParam skinParam) {
		TextBlock result = TextBlockUtils.EMPTY_TEXT_BLOCK;
		for (CharSequence cs : label) {
			StringWithArrow tmp = new StringWithArrow(cs.toString());
			TextBlock block = tmp.getDisplay().create9(font, alignment, skinParam, skinParam.maxMessageSize());
			if (tmp.getLinkArrow() != LinkArrow.NONE_OR_SEVERAL) {
				block = StringWithArrow.addMagicArrow(block, tmp.getLinkArrow().mute(guide), font);
			}
			result = TextBlockUtils.mergeTB(result, block, alignment);
		}
		return result;
	}

	 */

	public static TextBlock addSeveralMagicArrows(Display label, GuideLine guide, FontConfiguration font,
			HorizontalAlignment alignment, ISkinParam skinParam) {
		TextBlock result = TextBlockUtils.EMPTY_TEXT_BLOCK;
		for (CharSequence cs : label) {
			StringWithArrow tmp = new StringWithArrow(cs.toString());
			TextBlock block = tmp.getDisplay().create9(font, alignment, skinParam, skinParam.maxMessageSize());
			if (tmp.getLinkArrow() != LinkArrow.NONE_OR_SEVERAL) {
				block = StringWithArrow.addMagicArrow2(block, tmp.getLinkArrow().mute(guide), font);
			}
			result = TextBlockUtils.mergeTB(result, block, alignment);
		}
		return result;
	}

}
