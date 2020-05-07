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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.ChangeState;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingNote;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Ribbon implements PDrawing {

	private final List<ChangeState> changes = new ArrayList<ChangeState>();
	private final List<TimeConstraint> constraints = new ArrayList<TimeConstraint>();

	private final ISkinParam skinParam;
	private final TimingRuler ruler;
	private String initialState;
	private Colors initialColors;
	private final List<TimingNote> notes;
	private final boolean compact;
	private final TextBlock title;
	private final int suggestedHeight;

	public Ribbon(TimingRuler ruler, ISkinParam skinParam, List<TimingNote> notes, boolean compact, TextBlock title,
			int suggestedHeight) {
		this.suggestedHeight = suggestedHeight == 0 ? 24 : suggestedHeight;
		this.compact = compact;
		this.ruler = ruler;
		this.skinParam = skinParam;
		this.notes = notes;
		this.title = title;
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final double y = getHeightForConstraints(stringBounder) + getRibbonHeight() / 2;
		for (ChangeState change : changes) {
			if (change.getWhen().compareTo(tick) == 0) {
				return new IntricatedPoint(new Point2D.Double(x, y), new Point2D.Double(x, y));
			}
		}
		return new IntricatedPoint(new Point2D.Double(x, y - getRibbonHeight() / 2),
				new Point2D.Double(x, y + getRibbonHeight() / 2));
	}

	public void addChange(ChangeState change) {
		this.changes.add(change);
	}

	private double getPosInPixel(ChangeState change) {
		return ruler.getPosInPixel(change.getWhen());
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	private TextBlock createTextBlock(String value) {
		final Display display = Display.getWithNewlines(value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public TextBlock getPart1(double fullAvailableWidth) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				if (compact) {
					final double titleHeight = title.calculateDimension(ug.getStringBounder()).getHeight();
					final double dy = (getRibbonHeight() - titleHeight) / 2;
					title.drawU(ug.apply(UTranslate.dy(dy)));
				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				double width = getInitialWidth(stringBounder);
				if (compact) {
					width += title.calculateDimension(stringBounder).getWidth() + 10;
				}
				return new Dimension2DDouble(width, getRibbonHeight());
			}
		};
	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawPart2(ug);
			}
		};
	}

	private void drawNotes(UGraphic ug, final Position position) {
		for (TimingNote note : notes) {
			if (note.getPosition() == position) {
				final double x = ruler.getPosInPixel(note.getWhen());
				note.drawU(ug.apply(UTranslate.dx(x)));
			}
		}
	}

	private double getInitialWidth(final StringBounder stringBounder) {
		if (initialState == null) {
			return 0;
		}
		return createTextBlock(initialState).calculateDimension(stringBounder).getWidth() + 24;
	}

	private void drawHexa(UGraphic ug, double len, ChangeState change) {
		final HexaShape shape = HexaShape.create(len, getRibbonHeight(), change.getContext());
		shape.drawU(ug);
	}

	private void drawFlat(UGraphic ug, double len, ChangeState change) {
		final ULine line = ULine.hline(len);
		change.getContext().apply(ug).apply(UTranslate.dy(getRibbonHeight() / 2)).draw(line);
	}

	private double getRibbonHeight() {
		return suggestedHeight;
	}

	private void drawPentaB(UGraphic ug, double len, ChangeState change) {
		final PentaBShape shape = PentaBShape.create(len, getRibbonHeight(), change.getContext());
		shape.drawU(ug);
	}

	private void drawPentaA(UGraphic ug, double len, ChangeState change) {
		SymbolContext context = change.getContext();
		final HColor back = initialColors.getColor(ColorType.BACK);
		if (back != null) {
			context = context.withBackColor(back);
		}
		final PentaAShape shape = PentaAShape.create(len, getRibbonHeight(), context);
		shape.drawU(ug);
	}

	private double getHeightForConstraints(StringBounder stringBounder) {
		return TimeConstraint.getHeightForConstraints(stringBounder, constraints);
	}

	private double getHeightForNotes(StringBounder stringBounder, Position position) {
		double height = 0;
		for (TimingNote note : notes) {
			if (note.getPosition() == position) {
				height = Math.max(height, note.getHeight(stringBounder));
			}
		}
		return height;
	}

	private double getMarginX() {
		return 12;
	}

	public void setInitialState(String initialState, Colors initialColors) {
		this.initialState = initialState;
		this.initialColors = initialColors;
	}

	public void addConstraint(TimeConstraint constraint) {
		this.constraints.add(constraint);
	}

	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + getHeightForTopComment(stringBounder)
				+ getHeightForNotes(stringBounder, Position.TOP) + getRibbonHeight()
				+ getHeightForNotes(stringBounder, Position.BOTTOM) + getBottomMargin();
	}

	private double getBottomMargin() {
		return 10;
	}

	private void drawPart2(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final UGraphic ugRibbon = ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder)
				+ getHeightForTopComment(stringBounder) + getHeightForNotes(stringBounder, Position.TOP)));

		drawBeforeZeroState(ugRibbon);
		drawBeforeZeroStateLabel(ugRibbon.apply(UTranslate.dy(getRibbonHeight() / 2)));
		drawStates(ugRibbon);
		drawStatesLabels(ugRibbon.apply(UTranslate.dy(getRibbonHeight() / 2)));

		drawConstraints(ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder) / 2)));

		drawNotes(ug, Position.TOP);
		drawNotes(ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder) + getRibbonHeight()
				+ getHeightForNotes(stringBounder, Position.TOP))), Position.BOTTOM);
	}

	private void drawBeforeZeroState(UGraphic ug) {
		if (initialState != null && changes.size() > 0) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double a = getPosInPixel(changes.get(0));
			drawPentaA(ug.apply(UTranslate.dx(-getInitialWidth(stringBounder))), getInitialWidth(stringBounder) + a,
					changes.get(0));
		}
	}

	private void drawBeforeZeroStateLabel(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		if (initialState != null) {
			final TextBlock initial = createTextBlock(initialState);
			final Dimension2D dimInital = initial.calculateDimension(stringBounder);
			initial.drawU(ug.apply(new UTranslate(-getMarginX() - dimInital.getWidth(), -dimInital.getHeight() / 2)));
		}
	}

	private void drawStates(UGraphic ug) {
		for (int i = 0; i < changes.size() - 1; i++) {
			final double a = getPosInPixel(changes.get(i));
			final double b = getPosInPixel(changes.get(i + 1));
			assert b > a;
			if (changes.get(i).isFlat()) {
				drawFlat(ug.apply(UTranslate.dx(a)), b - a, changes.get(i));
			} else if (changes.get(i).isCompletelyHidden() == false) {
				drawHexa(ug.apply(UTranslate.dx(a)), b - a, changes.get(i));
			}
		}
		if (changes.size() >= 1) {
			final ChangeState last = changes.get(changes.size() - 1);
			final double a = getPosInPixel(last);
			if (last.isFlat()) {
				drawFlat(ug.apply(UTranslate.dx(a)), ruler.getWidth() - a, last);
			} else if (last.isCompletelyHidden() == false) {
				drawPentaB(ug.apply(UTranslate.dx(a)), ruler.getWidth() - a, last);
			}
		}
	}

	private void drawStatesLabels(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (int i = 0; i < changes.size(); i++) {
			final ChangeState change = changes.get(i);
			final double x = ruler.getPosInPixel(change.getWhen());
			if (change.isBlank() == false && change.isCompletelyHidden() == false && change.isFlat() == false) {
				final TextBlock state = createTextBlock(change.getState());
				final Dimension2D dim = state.calculateDimension(stringBounder);
				final double xtext;
				if (i == changes.size() - 1) {
					xtext = x + getMarginX();
				} else {
					final double x2 = ruler.getPosInPixel(changes.get(i + 1).getWhen());
					xtext = (x + x2) / 2 - dim.getWidth() / 2;
				}
				state.drawU(ug.apply(new UTranslate(xtext, -dim.getHeight() / 2)));
			}
			final TextBlock commentTopBlock = getCommentTopBlock(change);
			final Dimension2D dimComment = commentTopBlock.calculateDimension(stringBounder);
			commentTopBlock
					.drawU(ug.apply(new UTranslate(x + getMarginX(), -getRibbonHeight() / 2 - dimComment.getHeight())));
		}
	}

	private TextBlock getCommentTopBlock(final ChangeState change) {
		if (change.getComment() == null) {
			return TextBlockUtils.empty(0, 0);
		}
		return createTextBlock(change.getComment());
	}

	private double getHeightForTopComment(StringBounder stringBounder) {
		double result = 0;
		for (ChangeState change : changes) {
			result = Math.max(result, getCommentTopBlock(change).calculateDimension(stringBounder).getHeight());
		}
		return result;
	}

	private void drawConstraints(final UGraphic ug) {
		for (TimeConstraint constraint : constraints) {
			constraint.drawU(ug, ruler);
		}
	}

}
