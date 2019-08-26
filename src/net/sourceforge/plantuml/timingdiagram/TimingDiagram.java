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
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimingDiagram extends UmlDiagram implements Clocks {

	private TitleStrategy getTitleStrategy() {
		// return TitleStrategy.IN_LEFT_HEADER;
		return TitleStrategy.IN_FRAME;
	}

	private final Map<String, Player> players = new LinkedHashMap<String, Player>();
	private final Map<String, PlayerClock> clocks = new HashMap<String, PlayerClock>();
	private final List<TimeMessage> messages = new ArrayList<TimeMessage>();
	private final TimingRuler ruler = new TimingRuler(getSkinParam());
	private TimeTick now;
	private Player lastPlayer;
	private boolean drawTimeAxis = true;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Timing Diagram)");
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.TIMING;
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final double dpiFactor = 1;
		final double margin = 10;
		final ImageBuilder imageBuilder = new ImageBuilder(getSkinParam(), dpiFactor,
				fileFormatOption.isWithMetadata() ? getMetadata() : null, getWarningOrError(), margin, margin,
				getAnimation());

		TextBlock result = getTextBlock();
		final ISkinParam skinParam = getSkinParam();
		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder()).addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final UTranslate lastTranslate = getUTranslateForPlayer(null, stringBounder);
				final double withBeforeRuler = getWithBeforeRuler(stringBounder);
				final double totalWith = withBeforeRuler + ruler.getWidth() + marginX1 + marginX2;
				return new Dimension2DDouble(totalWith, lastTranslate.getDy() + ruler.getHeight(stringBounder));
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HtmlColor getBackcolor() {
				return null;
			}
		};
	}

	public static final double marginX1 = 5;
	private final double marginX2 = 5;

	private void drawInternal(UGraphic ug) {
		ruler.ensureNotEmpty();
		final StringBounder stringBounder = ug.getStringBounder();
		final UTranslate lastTranslate = getUTranslateForPlayer(null, stringBounder);
		final double withBeforeRuler = getWithBeforeRuler(stringBounder);
		final double totalWith = withBeforeRuler + ruler.getWidth() + marginX1 + marginX2;

		final ULine border = new ULine(0, lastTranslate.getDy());
		final UStroke borderStroke = new UStroke(1.7);
		ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(borderStroke).draw(border);
		ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(borderStroke).apply(new UTranslate(totalWith, 0))
				.draw(border);

		ug = ug.apply(new UTranslate(marginX1, 0));

		ruler.draw0(ug.apply(new UTranslate(withBeforeRuler, 0)), getUTranslateForPlayer(null, stringBounder).getDy());
		for (Player player : players.values()) {
			final UGraphic playerUg = ug.apply(getUTranslateForPlayer(player, stringBounder));
			player.drawFrameTitle(playerUg);
			player.drawContent(playerUg.apply(new UTranslate(withBeforeRuler, 0)));
			player.drawLeftHeader(playerUg);
			playerUg.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(borderStroke)
					.apply(new UTranslate(-marginX1, 0)).draw(new ULine(totalWith, 0));
		}
		ug = ug.apply(new UTranslate(withBeforeRuler, 0));
		if (this.drawTimeAxis) {
			ruler.drawTimeAxis(ug.apply(lastTranslate));
		}
		for (TimeMessage timeMessage : messages) {
			drawMessages(ug, timeMessage);
		}
	}

	private double getWithBeforeRuler(StringBounder stringBounder) {
		double width = 0;
		for (Player player : players.values()) {
			width = Math.max(width, player.getWidthHeader(stringBounder));

		}
		return width;
	}

	private double getFirstColumnWidth(StringBounder stringBounder) {
		double width = 0;
		for (Player player : players.values()) {
			width = Math.max(width, player.getFirstColumnWidth(stringBounder));

		}
		return width;
	}

	private void drawMessages(UGraphic ug, TimeMessage message) {
		final Player player1 = message.getPlayer1();
		final Player player2 = message.getPlayer2();

		final UTranslate translate1 = getUTranslateForPlayer(player1, ug.getStringBounder());
		final UTranslate translate2 = getUTranslateForPlayer(player2, ug.getStringBounder());

		final IntricatedPoint pt1 = player1.getTimeProjection(ug.getStringBounder(), message.getTick1());
		final IntricatedPoint pt2 = player2.getTimeProjection(ug.getStringBounder(), message.getTick2());

		if (pt1 == null || pt2 == null) {
			return;
		}

		final TimeArrow timeArrow = TimeArrow.create(pt1.translated(translate1), pt2.translated(translate2),
				message.getLabel(), getSkinParam(), message);
		timeArrow.drawU(ug);

	}

	public UTranslate getUTranslateForPlayer(Player candidat, StringBounder stringBounder) {
		double y = 0;
		for (Player player : players.values()) {
			if (candidat == player) {
				return new UTranslate(0, y);
			}
			y += player.getHeight(stringBounder);
		}
		if (candidat == null) {
			return new UTranslate(0, y);
		}
		throw new IllegalArgumentException();
	}

	public CommandExecutionResult createRobustConcise(String code, String full, TimingStyle type) {
		final Player player = type.createPlayer(getTitleStrategy(), full, getSkinParam(), ruler);
		players.put(code, player);
		lastPlayer = player;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult createClock(String code, String full, int period, int pulse) {
		final PlayerClock player = new PlayerClock(getTitleStrategy(), getSkinParam(), ruler, period, pulse);
		players.put(code, player);
		clocks.put(code, player);
		final TimeTick tick = new TimeTick(new BigDecimal(period), TimingFormat.DECIMAL);
		ruler.addTime(tick);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult createBinary(String code, String full) {
		final Player player = new PlayerBinary(getTitleStrategy(), code, getSkinParam(), ruler);
		players.put(code, player);
		return CommandExecutionResult.ok();
	}

	public TimeMessage createTimeMessage(Player player1, TimeTick time1, Player player2, TimeTick time2, String label) {
		final TimeMessage message = new TimeMessage(new TickInPlayer(player1, time1), new TickInPlayer(player2, time2),
				label);
		messages.add(message);
		return message;
	}

	public void addTime(TimeTick time) {
		this.now = time;
		ruler.addTime(time);
	}

	public void updateNow(TimeTick time) {
		this.now = time;
	}

	public Player getPlayer(String code) {
		return players.get(code);
	}

	public TimeTick getNow() {
		return now;
	}

	public TimeTick getClockValue(String clockName, int nb) {
		final PlayerClock clock = clocks.get(clockName);
		if (clock == null) {
			return null;
		}
		return new TimeTick(new BigDecimal(nb * clock.getPeriod()), TimingFormat.DECIMAL);
	}

	public void setLastPlayer(Player player) {
		this.lastPlayer = player;
	}

	public Player getLastPlayer() {
		return lastPlayer;
	}

	public void scaleInPixels(long tick, long pixel) {
		ruler.scaleInPixels(tick, pixel);
	}

	public CommandExecutionResult hideTimeAxis() {
		this.drawTimeAxis = false;
		return CommandExecutionResult.ok();
	}

}
