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
package net.sourceforge.plantuml.timingdiagram;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class AbstractPlayer extends ReallyAbstractPlayer implements Player {

	private String initialState;

	private final Set<ChangeState> changes = new TreeSet<ChangeState>();
	private final List<TimeConstraint> constraints = new ArrayList<TimeConstraint>();
	protected final List<TimingNote> notes = new ArrayList<TimingNote>();
	protected final Map<String, String> statesLabel = new LinkedHashMap<String, String>();

	public AbstractPlayer(TitleStrategy titleStrategy, String full, ISkinParam skinParam, TimingRuler ruler) {
		super(titleStrategy, full, skinParam, ruler);
	}

	protected abstract TimeDrawing buildDrawing();

	public PlayerFrame getPlayerFrame() {
		return new PlayerFrame2(titleStrategy, getTitle());
	}

	public void drawContent(UGraphic ug) {
		ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder()));
		getTimeDrawing().drawU(ug);
	}

	public void drawLeftHeader(UGraphic ug) {
		if (titleStrategy == TitleStrategy.IN_LEFT_HEADER) {
			final StringBounder stringBounder = ug.getStringBounder();
			final TextBlock title = getTitle();
			final Dimension2D dim = title.calculateDimension(stringBounder);
			final double y = (getHeight(stringBounder) - dim.getHeight()) / 2;
			title.drawU(ug.apply(new UTranslate(0, y)));
			ug = ug.apply(new UTranslate(dim.getWidth() + 5, 0));
		}
		ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder()));
		getTimeDrawing().getWidthHeader(ug.getStringBounder()).drawU(ug);
	}

	public double getWidthHeader(StringBounder stringBounder) {
		final Dimension2D dimHeader1 = getTimeDrawing().getWidthHeader(stringBounder).calculateDimension(stringBounder);
		if (titleStrategy == TitleStrategy.IN_LEFT_HEADER) {
			final Dimension2D dimTitle = getTitle().calculateDimension(stringBounder);
			return dimTitle.getWidth() + 5 + dimHeader1.getWidth();
		}
		return dimHeader1.getWidth();
	}

	private UTranslate getTranslateForTimeDrawing(StringBounder stringBounder) {
		if (titleStrategy == TitleStrategy.IN_FRAME) {
			final TextBlock title = getTitle();
			return new UTranslate(0, title.calculateDimension(stringBounder).getHeight() * 2);
		}
		return new UTranslate(0, 12);
	}

	private TimeDrawing cached;
	private Colors initialColors;

	private TimeDrawing getTimeDrawing() {
		if (cached == null) {
			cached = computeTimeDrawing();
		}
		return cached;
	}

	private TimeDrawing computeTimeDrawing() {
		final TimeDrawing result = buildDrawing();
		result.setInitialState(initialState, initialColors);
		for (ChangeState change : changes) {
			result.addChange(change);
		}
		for (TimeConstraint constraint : constraints) {
			result.addConstraint(constraint);
		}
		return result;
	}

	public double getHeight(StringBounder stringBounder) {
		final double zoneHeight = getZoneHeight(stringBounder);
		if (titleStrategy == TitleStrategy.IN_LEFT_HEADER) {
			return zoneHeight;
		}
		final TextBlock title = getTitle();
		return title.calculateDimension(stringBounder).getHeight() * 2 + zoneHeight;
	}

	private double getZoneHeight(StringBounder stringBounder) {
		return getTimeDrawing().getHeight(stringBounder);
	}

	public void setState(TimeTick now, String comment, Colors color, String... states) {
		for (int i = 0; i < states.length; i++) {
			states[i] = decodeState(states[i]);
		}
		if (now == null) {
			this.initialState = states[0];
			this.initialColors = color;
		} else {
			this.changes.add(new ChangeState(now, comment, color, states));
		}

	}

	private String decodeState(String code) {
		final String label = statesLabel.get(code);
		if (label == null) {
			return code;
		}
		return label;
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final IntricatedPoint point = getTimeDrawing().getTimeProjection(stringBounder, tick);
		if (point == null) {
			return null;
		}
		final UTranslate translation = getTranslateForTimeDrawing(stringBounder);
		return point.translated(translation);
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		this.constraints.add(new TimeConstraint(tick1, tick2, message));
	}

	public void addNote(TimeTick now, Display note, Position position) {
		this.notes.add(new TimingNote(now, this, note, position, skinParam));
	}

	public void defineState(String stateCode, String label) {
		statesLabel.put(stateCode, label);
	}

}
