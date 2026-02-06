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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.timingdiagram.graphic.Panels;
import net.sourceforge.plantuml.timingdiagram.graphic.TimeArrow;

public class TimingDiagram extends UmlDiagram implements Clocks {

	public static final double marginX1 = 5;
	private final double marginX2 = 5;

	private final Map<String, TimeTick> codes = new HashMap<String, TimeTick>();
	private final Map<String, Player> players = new LinkedHashMap<String, Player>();
	private final Map<String, PlayerClock> clocks = new HashMap<String, PlayerClock>();
	private final List<TimeMessage> messages = new ArrayList<>();
	private final List<Highlight> highlights = new ArrayList<>();
	private final TimingRuler ruler = new TimingRuler(getSkinParam());
	private TimeTick now;
	private Player lastPlayer;
	private TimeAxisStategy timeAxisStategy = TimeAxisStategy.AUTOMATIC;
	private boolean compactByDefault = false;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Timing Diagram)");
	}

	public TimingDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.TIMING, null, preprocessing);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final double withBeforeRuler = getPart1MaxWidth(stringBounder);
				final double totalWith = withBeforeRuler + ruler.getWidth() + marginX1 + marginX2;
				return new XDimension2D(totalWith, getHeightTotal(stringBounder));
			}

		};
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram);
	}

	private HColor black() {
		final Style style = getStyleSignature().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		return style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());

	}

	private void drawInternal(UGraphic ug) {
		ruler.ensureNotEmpty();
		final StringBounder stringBounder = ug.getStringBounder();
		final double part1MaxWidth = getPart1MaxWidth(stringBounder);

		for (Player player : players.values()) {
			final HColor generalBackgroundColor = player.getGeneralBackgroundColor();
			if (generalBackgroundColor != null) {
				UGraphic ugPlayer = ug.apply(getUTranslateForPlayerFrame(player, stringBounder));
				final double fullHeight = player.getFrameHeight(stringBounder)
						+ player.panels().getFullHeight(stringBounder);
				ugPlayer = ugPlayer.apply(generalBackgroundColor).apply(generalBackgroundColor.bg());
				ugPlayer.draw(URectangle.build(getWidthTotal(stringBounder), fullHeight));
			}
		}

		final UTranslate widthPart1 = UTranslate.dx(part1MaxWidth);
		if (compactByDefault == false)
			drawBorder(ug);

		ug = ug.apply(UTranslate.dx(marginX1));

		drawHighlightsBack(ug.apply(widthPart1));
		ruler.drawVlines(ug.apply(widthPart1), getHeightInner(stringBounder));

		for (Player player : players.values()) {
			final UGraphic ugPlayer = ug.apply(getUTranslateForPlayer(player, stringBounder));
			final UGraphic ugFrame = ug.apply(getUTranslateForPlayerFrame(player, stringBounder));
			if (player.isCompact() == false)
				drawHorizontalSeparator(ugFrame);

			player.drawFrameTitle(ugFrame);
			final Panels panels = player.panels();
			panels.drawLeftPanel(ugPlayer, part1MaxWidth);
			panels.drawRightPanel(ugPlayer.apply(widthPart1));
		}

		ug = ug.apply(widthPart1);
		ruler.drawTimeAxis(ug.apply(getLastTranslate(stringBounder)), this.timeAxisStategy, codes);

		for (TimeMessage timeMessage : messages)
			drawMessages(ug, timeMessage);

		drawHighlightsLines(ug);
	}

	private void drawHorizontalSeparator(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(black());
		ug = ug.apply(getBorderStroke());
		ug = ug.apply(UTranslate.dx(-marginX1));
		ug.draw(ULine.hline(getWidthTotal(stringBounder)));
	}

	private void drawBorder(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final ULine border = ULine.vline(getLastTranslate(stringBounder).getDy());
		ug = ug.apply(black()).apply(getBorderStroke());
		ug.draw(border);
		ug.apply(UTranslate.dx(getWidthTotal(stringBounder))).draw(border);
	}

	private UStroke getBorderStroke() {
		return getStyleSignature().getMergedStyle(getCurrentStyleBuilder()).getStroke();
	}

	private UTranslate getLastTranslate(final StringBounder stringBounder) {
		return getUTranslateForPlayer(null, stringBounder);
	}

	private void drawHighlightsBack(UGraphic ug) {
		final double height = getHeightInner(ug.getStringBounder());
		for (Highlight highlight : highlights)
			highlight.drawHighlightsBack(ug, ruler, height);

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

	private double getWidthTotal(final StringBounder stringBounder) {
		return getPart1MaxWidth(stringBounder) + ruler.getWidth() + marginX1 + marginX2;
	}

	private double getPart1MaxWidth(StringBounder stringBounder) {
		double width = 0;
		for (Player player : players.values()) {
			final double widthLeftPanel = player.panels().getLeftPanelWidth(stringBounder);
			width = Math.max(width, widthLeftPanel);
		}

		return width;
	}

	private void drawMessages(UGraphic ug, TimeMessage message) {
		final Player player1 = message.getPlayer1();
		final Player player2 = message.getPlayer2();

		final StringBounder stringBounder = ug.getStringBounder();

		final UTranslate translate1 = getUTranslateForPlayer(player1, stringBounder);
		final UTranslate translate2 = getUTranslateForPlayer(player2, stringBounder);

		final IntricatedPoint pt1 = player1.panels().getTimeProjection(stringBounder, message.getTick1());
		final IntricatedPoint pt2 = player2.panels().getTimeProjection(stringBounder, message.getTick2());

		if (pt1 == null || pt2 == null)
			return;

		final TimeArrow timeArrow = TimeArrow.create(pt1.translated(translate1), pt2.translated(translate2),
				message.getLabel(), getSkinParam(), message);
		timeArrow.drawU(ug);

	}

	private UTranslate getUTranslateForPlayer(Player candidat, StringBounder stringBounder) {
		double y = 0;
		for (Player player : players.values()) {
			y += player.getFrameHeight(stringBounder);
			if (candidat == player)
				return UTranslate.dy(y);

//			if (y == 0) {
//				y += getHeightHighlights(stringBounder);
//			}
			y += player.panels().getFullHeight(stringBounder);
		}
		if (candidat == null)
			return UTranslate.dy(y);

		throw new IllegalArgumentException();
	}

	private UTranslate getUTranslateForPlayerFrame(Player candidat, StringBounder stringBounder) {
		double y = 0;
		for (Player player : players.values()) {
			if (candidat == player)
				return UTranslate.dy(y);

//			if (y == 0) {
//				y += getHeightHighlights(stringBounder);
//			}
			y += player.getFrameHeight(stringBounder);
			y += player.panels().getFullHeight(stringBounder);
		}
		if (candidat == null)
			return UTranslate.dy(y);

		throw new IllegalArgumentException();
	}

	public Player createPlayerRobust(String code, String full, boolean compact, Stereotype stereotype,
			HColor backColor) {
		final Player player = new PlayerRobust(full, getSkinParam(), ruler, compactByDefault || compact, stereotype,
				backColor);
		players.put(code, player);
		lastPlayer = player;
		return player;
	}

	public Player createPlayerConcise(String code, String full, boolean compact, Stereotype stereotype,
			HColor backColor) {
		final Player player = new PlayerConcise(full, getSkinParam(), ruler, compactByDefault || compact, stereotype,
				backColor);
		players.put(code, player);
		lastPlayer = player;
		return player;
	}

	public Player createPlayerRectangle(String code, String full, boolean compact, Stereotype stereotype,
			HColor backColor) {
		final Player player = new PlayerRectangle(full, getSkinParam(), ruler, compactByDefault || compact, stereotype,
				backColor);
		players.put(code, player);
		lastPlayer = player;
		return player;
	}

	public PlayerClock createPlayerClock(String code, String full, int period, int pulse, int offset, boolean compact,
			Stereotype stereotype) {
		final PlayerClock player = new PlayerClock(full, getSkinParam(), ruler, period, pulse, offset, compactByDefault,
				stereotype);
		players.put(code, player);
		clocks.put(code, player);
		final TimeTick tick = new TimeTick(new BigDecimal(period), TimingFormat.DECIMAL);
		ruler.addTime(tick);
		return player;
	}

	public PlayerAnalog createPlayerAnalog(String code, String full, boolean compact, Stereotype stereotype) {
		final PlayerAnalog player = new PlayerAnalog(full, getSkinParam(), ruler, compactByDefault, stereotype);
		players.put(code, player);
		return player;
	}

	public Player createPlayerBinary(String code, String full, boolean compact, Stereotype stereotype) {
		final Player player = new PlayerBinary(full, getSkinParam(), ruler, compactByDefault, stereotype);
		players.put(code, player);
		return player;
	}

	public PlayerDigital createPlayerAnalogDigital(TimingType type, String code, String full, boolean compact, Stereotype stereotype) {
		final PlayerAnalogDigital player = new PlayerAnalogDigital(type, full, getSkinParam(), ruler, compactByDefault, stereotype);
		players.put(code, player);
		return player;
	}

	public TimeMessage createTimeMessage(Player player1, TimeTick time1, Player player2, TimeTick time2, String label) {
		final TimeMessage message = new TimeMessage(new TickInPlayer(player1, time1), new TickInPlayer(player2, time2),
				label, getSkinParam());
		messages.add(message);
		return message;
	}

	public void addTime(TimeTick time, String code) {
		this.now = time;
		ruler.addTime(time);
		if (code != null)
			this.codes.put(code, time);

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
		if (clock == null)
			return null;

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

	public CommandExecutionResult setTimeAxisStategy(TimeAxisStategy newStrategy) {
		this.timeAxisStategy = newStrategy;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult highlight(TimeTick tickFrom, TimeTick tickTo, Display caption, Colors colors) {
		this.highlights.add(new Highlight(getSkinParam(), tickFrom, tickTo, caption, colors));
		return CommandExecutionResult.ok();

	}

	public void goCompactMode() {
		this.compactByDefault = true;
	}

	private SimpleDateFormat sdf;

	public CommandExecutionResult useDateFormat(String dateFormat) {
		try {
			this.sdf = new SimpleDateFormat(dateFormat, Locale.US);
		} catch (Exception e) {
			return CommandExecutionResult.error("Bad date format");
		}

		return CommandExecutionResult.ok();
	}

	@Override
	public TimingFormat getTimingFormatDate() {
		if (sdf == null)
			return TimingFormat.DATE;
		return TimingFormat.create(sdf);
	}

	public void setStopAt(TimeTick timeTick) {
		ruler.setStopAt(timeTick);
	}

}
