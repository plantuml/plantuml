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
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.timingdiagram.graphic.TimeArrow;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimingDiagram extends UmlDiagram implements Clocks {

	public static final double marginX1 = 5;
	private final double marginX2 = 5;

	private final Map<String, TimeTick> codes = new HashMap<String, TimeTick>();
	private final Map<String, Player> players = new LinkedHashMap<String, Player>();
	private final Map<String, PlayerClock> clocks = new HashMap<String, PlayerClock>();
	private final List<TimeMessage> messages = new ArrayList<TimeMessage>();
	private final List<Highlight> highlights = new ArrayList<Highlight>();
	private final TimingRuler ruler = new TimingRuler(getSkinParam());
	private TimeTick now;
	private Player lastPlayer;
	private boolean drawTimeAxis = true;
	private boolean compactByDefault = false;

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
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildD(getSkinParam(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2), getAnimation(), fileFormatOption.isWithMetadata() ? getMetadata() : null,
		getWarningOrError(), dpiFactor);

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
				final double withBeforeRuler = getPart1MaxWidth(stringBounder);
				final double totalWith = withBeforeRuler + ruler.getWidth() + marginX1 + marginX2;
				return new Dimension2DDouble(totalWith, getHeightTotal(stringBounder));
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawInternal(UGraphic ug) {
		ruler.ensureNotEmpty();
		final StringBounder stringBounder = ug.getStringBounder();
		final double part1MaxWidth = getPart1MaxWidth(stringBounder);
		final UTranslate widthPart1 = UTranslate.dx(part1MaxWidth);
		if (compactByDefault == false) {
			drawBorder(ug);
		}
		ug = ug.apply(UTranslate.dx(marginX1));

		drawHighlightsBack(ug.apply(widthPart1));
		ruler.drawVlines(ug.apply(widthPart1), getHeightInner(stringBounder));
		boolean first = true;

		for (Player player : players.values()) {
			final UGraphic ugPlayer = ug.apply(getUTranslateForPlayer(player, stringBounder));
			final double caption = getHeightForCaptions(stringBounder);
			if (first) {
				if (player.isCompact() == false) {
					drawHorizontalSeparator(ugPlayer);
				}
				player.getPart1(part1MaxWidth, caption).drawU(ugPlayer);
				player.getPart2().drawU(ugPlayer.apply(widthPart1).apply(UTranslate.dy(caption)));
			} else {
				if (player.isCompact() == false) {
					drawHorizontalSeparator(ugPlayer.apply(UTranslate.dy(caption)));
				}
				player.getPart1(part1MaxWidth, 0).drawU(ugPlayer.apply(UTranslate.dy(caption)));
				player.getPart2().drawU(ugPlayer.apply(widthPart1).apply(UTranslate.dy(caption)));
			}
			first = false;
		}
		ug = ug.apply(widthPart1);
		if (this.drawTimeAxis) {
			ruler.drawTimeAxis(ug.apply(getLastTranslate(stringBounder)));
		}
		for (TimeMessage timeMessage : messages) {
			drawMessages(ug, timeMessage);
		}
		drawHighlightsLines(ug);
	}

	private void drawHorizontalSeparator(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(HColorUtils.BLACK);
		ug = ug.apply(getBorderStroke());
		ug = ug.apply(UTranslate.dx(-marginX1));
		ug.draw(ULine.hline(getWidthTotal(stringBounder)));
	}

	private void drawBorder(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final ULine border = ULine.vline(getLastTranslate(stringBounder).getDy());
		ug = ug.apply(HColorUtils.BLACK).apply(getBorderStroke());
		ug.draw(border);
		ug.apply(UTranslate.dx(getWidthTotal(stringBounder))).draw(border);
	}

	private UStroke getBorderStroke() {
		return new UStroke(1.7);
	}

	private UTranslate getLastTranslate(final StringBounder stringBounder) {
		return getUTranslateForPlayer(null, stringBounder).compose(UTranslate.dy(getHeightForCaptions(stringBounder)));
	}

	private void drawHighlightsBack(UGraphic ug) {
		final double height = getHeightInner(ug.getStringBounder());
		for (Highlight highlight : highlights) {
			highlight.drawHighlightsBack(ug, ruler, height);
		}
	}

	private void drawHighlightsLines(UGraphic ug) {
		final double height = getHeightInner(ug.getStringBounder());
		for (Highlight highlight : highlights) {
			highlight.drawHighlightsLines(ug, ruler, height);
			final double start = ruler.getPosInPixel(highlight.getTickFrom());
			highlight.getCaption(getSkinParam()).drawU(ug.apply(new UTranslate(start + 3, 2)));
		}
	}

	private double getHeightTotal(StringBounder stringBounder) {
		return getHeightInner(stringBounder) + ruler.getHeight(stringBounder);
	}

	private double getHeightInner(StringBounder stringBounder) {
		return getLastTranslate(stringBounder).getDy();
	}

	private double getHeightForCaptions(StringBounder stringBounder) {
		double result = 0;
		for (Highlight highlight : highlights) {
			final TextBlock caption = highlight.getCaption(getSkinParam());
			result = Math.max(result, caption.calculateDimension(stringBounder).getHeight());
		}
		return result;
	}

	private double getWidthTotal(final StringBounder stringBounder) {
		return getPart1MaxWidth(stringBounder) + ruler.getWidth() + marginX1 + marginX2;
	}

	private double getPart1MaxWidth(StringBounder stringBounder) {
		double width = 0;
		for (Player player : players.values()) {
			width = Math.max(width, player.getPart1(0, 0).calculateDimension(stringBounder).getWidth());

		}
		return width;
	}

	private void drawMessages(UGraphic ug, TimeMessage message) {
		final Player player1 = message.getPlayer1();
		final Player player2 = message.getPlayer2();

		final StringBounder stringBounder = ug.getStringBounder();
		final UTranslate translate1 = getUTranslateForPlayer(player1, stringBounder)
				.compose(UTranslate.dy(getHeightForCaptions(stringBounder)));
		final UTranslate translate2 = getUTranslateForPlayer(player2, stringBounder)
				.compose(UTranslate.dy(getHeightForCaptions(stringBounder)));

		final IntricatedPoint pt1 = player1.getTimeProjection(stringBounder, message.getTick1());
		final IntricatedPoint pt2 = player2.getTimeProjection(stringBounder, message.getTick2());

		if (pt1 == null || pt2 == null) {
			return;
		}

		final TimeArrow timeArrow = TimeArrow.create(pt1.translated(translate1), pt2.translated(translate2),
				message.getLabel(), getSkinParam(), message);
		timeArrow.drawU(ug);

	}

	private UTranslate getUTranslateForPlayer(Player candidat, StringBounder stringBounder) {
		double y = 0;
		for (Player player : players.values()) {
			if (candidat == player) {
				return UTranslate.dy(y);
			}
//			if (y == 0) {
//				y += getHeightHighlights(stringBounder);
//			}
			y += player.getFullHeight(stringBounder);
		}
		if (candidat == null) {
			return UTranslate.dy(y);
		}
		throw new IllegalArgumentException();
	}

	public CommandExecutionResult createRobustConcise(String code, String full, TimingStyle type, boolean compact) {
		final Player player = new PlayerRobustConcise(type, full, getSkinParam(), ruler, compactByDefault || compact);
		players.put(code, player);
		lastPlayer = player;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult createClock(String code, String full, int period, int pulse, boolean compact) {
		final PlayerClock player = new PlayerClock(getSkinParam(), ruler, period, pulse, compactByDefault);
		players.put(code, player);
		clocks.put(code, player);
		final TimeTick tick = new TimeTick(new BigDecimal(period), TimingFormat.DECIMAL);
		ruler.addTime(tick);
		return CommandExecutionResult.ok();
	}

	public PlayerAnalog createAnalog(String code, String full, boolean compact) {
		final PlayerAnalog player = new PlayerAnalog(full, getSkinParam(), ruler, compactByDefault);
		players.put(code, player);
		return player;
	}

	public CommandExecutionResult createBinary(String code, String full, boolean compact) {
		final Player player = new PlayerBinary(full, getSkinParam(), ruler, compactByDefault);
		players.put(code, player);
		return CommandExecutionResult.ok();
	}

	public TimeMessage createTimeMessage(Player player1, TimeTick time1, Player player2, TimeTick time2, String label) {
		final TimeMessage message = new TimeMessage(new TickInPlayer(player1, time1), new TickInPlayer(player2, time2),
				label);
		messages.add(message);
		return message;
	}

	public void addTime(TimeTick time, String code) {
		this.now = time;
		ruler.addTime(time);
		if (code != null) {
			this.codes.put(code, time);
		}
	}

	public TimeTick getCodeValue(String code) {
		return codes.get(code);
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

	public CommandExecutionResult highlight(TimeTick tickFrom, TimeTick tickTo, Display caption, Colors colors) {
		this.highlights.add(new Highlight(tickFrom, tickTo, caption, colors));
		return CommandExecutionResult.ok();

	}

	public void goCompactMode() {
		this.compactByDefault = true;
	}

}
