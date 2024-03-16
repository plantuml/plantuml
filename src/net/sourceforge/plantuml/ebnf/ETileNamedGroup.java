/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.style.ISkinParam;

public class ETileNamedGroup extends ETile {

	private final ETile orig;
	private final ISkinParam skinParam;
	private String commentAbove;
	private String commentBelow;
	private final HColorSet colorSet;
	private final double deltax = 10;
	private final double deltay1 = 10;
	private final double deltay2 = 10;
	private final UText groupName;

	private final FontConfiguration fc;

	public ETileNamedGroup(ETile orig, FontConfiguration fc, HColorSet colorSet, ISkinParam skinParam, String name) {
		this.skinParam = skinParam;
		this.orig = orig;
		this.fc = fc;
		this.colorSet = colorSet;
		this.groupName = UText.build(name, fc);

	}

	@Override
	public double getH1(StringBounder stringBounder) {
		// final TextBlock note = getNoteAbove(stringBounder);
		return deltay1 + orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		// final TextBlock note = getNoteBelow(stringBounder);
		return orig.getH2(stringBounder) + deltay2;
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);

		try {
			final HColor background = colorSet.getColor("#E8E8FF");
			final UGraphic ugBack = ug.apply(background).apply(background.bg());
			ugBack.draw(URectangle.build(dim));
			final XDimension2D dimText = stringBounder.calculateDimension(fc.getFont(), groupName.getText());
			ugBack.apply(UTranslate.dy(-dimText.getHeight())).draw(URectangle.build(dimText.delta(10, 0)));

		} catch (NoSuchColorException e) {
			e.printStackTrace();
		}
		final double linePos = getH1(stringBounder);
		drawHline(ug, linePos, 0, deltax);
		drawHline(ug, linePos, dim.getWidth() - deltax, dim.getWidth());

		orig.drawU(ug.apply(new UTranslate(deltax, deltay1)));
		ug.apply(UTranslate.dx(5)).draw(groupName);

	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

//	@Override
//	protected void addCommentAbove(String comment) {
//		this.commentAbove = comment;
//	}
//
//	@Override
//	protected void addCommentBelow(String comment) {
//		this.commentBelow = comment;
//	}

//	private TextBlock getNoteAbove(StringBounder stringBounder) {
//		if (commentAbove == null)
//			return TextBlockUtils.EMPTY_TEXT_BLOCK;
//		final FloatingNote note = FloatingNote.create(Display.getWithNewlines(commentAbove), skinParam, SName.ebnf);
//		return TextBlockUtils.withMargin(note, 0, 0, 0, 10);
//	}
//
//	private TextBlock getNoteBelow(StringBounder stringBounder) {
//		if (commentBelow == null)
//			return TextBlockUtils.EMPTY_TEXT_BLOCK;
//		final FloatingNote note = FloatingNote.create(Display.getWithNewlines(commentBelow), skinParam, SName.ebnf);
//		return TextBlockUtils.withMargin(note, 0, 0, 10, 0);
//	}

}
