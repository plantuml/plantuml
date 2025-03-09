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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.utils.Position;

public class PlayerBinary extends Player {

	private static final String LOW_STRING = "0";
	private static final String HIGH_STRING = "1";

	private final List<TimeConstraint> constraints = new ArrayList<>();
	private final SortedMap<TimeTick, ChangeState> values = new TreeMap<>();
	private ChangeState initialState;

	private final List<TimingNote> notes = new ArrayList<>();

	public PlayerBinary(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype) {
		super(code, skinParam, ruler, compact, stereotype, null);
		this.suggestedHeight = 30;
	}

	private double getHeightForConstraints(StringBounder stringBounder) {
		return TimeConstraint.getHeightForConstraints(stringBounder, constraints);
	}

	@Override
	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + getHeightForNotes(stringBounder, Position.TOP) + suggestedHeight
				+ getHeightForNotes(stringBounder, Position.BOTTOM);
	}

	@Override
	protected StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.binary)
				.withTOBECHANGED(stereotype);
	}

	@Override
	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		return new IntricatedPoint(new XPoint2D(x, getYpos(stringBounder, HIGH_STRING)),
				new XPoint2D(x, getYpos(stringBounder, HIGH_STRING)));
	}

	@Override
	public void addNote(TimeTick now, Display note, Position position, Stereotype stereotype) {
		final StyleSignature signature = StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram,
				SName.note);
		final Style style = signature.withTOBECHANGED(stereotype).getMergedStyle(skinParam.getCurrentStyleBuilder());

		this.notes.add(new TimingNote(now, this, note, position, skinParam, style));
	}

	@Override
	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(TimeTick now, String comment, Colors color, String... states) {
		final ChangeState cs = new ChangeState(now, comment, color, convert(states));
		if (now == null)
			this.initialState = cs;
		else
			this.values.put(now, cs);

	}

	private String[] convert(String[] states) {
		if (states.length == 1)
			return new String[] { convert(states[0]) };
		return new String[] { convert(states[0]), convert(states[1]) };
	}

	private String convert(String value) {
		if ("1".equals(value) || "high".equalsIgnoreCase(value))
			return HIGH_STRING;
		return LOW_STRING;
	}

	@Override
	public void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(2.5, tick1, tick2, message, skinParam, config));
	}

	private final double ymargin = 8;

	private double getYpos(StringBounder stringBounder, String state) {
		if (state.equalsIgnoreCase(LOW_STRING))
			return getYlow(stringBounder);
		return getYhigh(stringBounder);
	}

	private double getYhigh(StringBounder stringBounder) {
		return ymargin + getHeightForConstraints(stringBounder) + getHeightForNotes(stringBounder, Position.TOP);
	}

	private double getYlow(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + getHeightForNotes(stringBounder, Position.TOP) + suggestedHeight
				- ymargin;
	}

	@Override
	public TextBlock getPart1(double fullAvailableWidth, double specialVSpace) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final TextBlock title = getTitle();
				final XDimension2D dim = title.calculateDimension(stringBounder);
				final double y = (getFullHeight(stringBounder) - dim.getHeight()) / 2;
				title.drawU(ug.apply(UTranslate.dy(y)));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dim = getTitle().calculateDimension(stringBounder);
				return dim.delta(5, 0);
			}
		};
	}

	@Override
	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = getContext().apply(ug);
				double lastx = 0;
				List<String> lastValues = initialState == null ? Collections.singletonList(LOW_STRING)
						: initialState.getStates();
				final StringBounder stringBounder = ug.getStringBounder();
				final double yhigh = getYhigh(stringBounder);
				final double ylow = getYlow(stringBounder);
				final ULine vline = ULine.vline(ylow - yhigh);
				for (Map.Entry<TimeTick, ChangeState> ent : values.entrySet()) {
					final ChangeState value = ent.getValue();

					final double x = ruler.getPosInPixel(ent.getKey());

					if (lastValues.size() == 1)
						ug.apply(new UTranslate(lastx, getYpos(stringBounder, lastValues.get(0))))
								.draw(ULine.hline(x - lastx));
					else
						for (double tmpx = lastx; tmpx < x; tmpx += 5)
							ug.apply(new UTranslate(tmpx, yhigh)).draw(vline);

					if (lastValues.equals(value.getStates()) == false)
						ug.apply(new UTranslate(x, yhigh)).draw(vline);

					if (value.getComment() != null) {
						final TextBlock label = getTextBlock(value.getComment());
						label.drawU(ug.apply(new UTranslate(x + 2, yhigh)));
					}

					lastx = x;
					lastValues = value.getStates();
				}
				ug.apply(new UTranslate(lastx, getYpos(stringBounder, lastValues.get(0))))
						.draw(ULine.hline(ruler.getWidth() - lastx));

				drawConstraints(ug.apply(UTranslate.dy(getHeightForConstraints(ug.getStringBounder()))));

				drawNotes(ug.apply(UTranslate.dy(ymargin)), Position.TOP);
				drawNotes(
						ug.apply(UTranslate.dy(getHeightForConstraints(stringBounder)
								+ getHeightForNotes(stringBounder, Position.TOP) + suggestedHeight - ymargin / 2)),
						Position.BOTTOM);

			}
		};
	}

	final protected FontConfiguration getCommentFontConfiguration() {
		return FontConfiguration.create(skinParam,
				getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder()));
	}

	private TextBlock getTextBlock(String value) {
		final Display display = Display.getWithNewlines(skinParam.getPragma(), value);
		return display.create(getCommentFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private void drawConstraints(final UGraphic ug) {
		for (TimeConstraint constraint : constraints)
			constraint.drawU(ug, ruler);
	}

	private void drawNotes(UGraphic ug, final Position position) {
		for (TimingNote note : notes)
			if (note.getPosition() == position) {
				final TimeTick when = note.getWhen();
				final double x = when == null ? 0 : ruler.getPosInPixel(when);
				note.drawU(ug.apply(UTranslate.dx(x)));
			}
	}

	private double getHeightForNotes(StringBounder stringBounder, Position position) {
		double height = 0;
		for (TimingNote note : notes)
			if (note.getPosition() == position)
				height = Math.max(height, note.getHeight(stringBounder));

		return height;
	}

}
