/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimingDiagram extends UmlDiagram implements Clock {

	private final Map<String, Player> players = new LinkedHashMap<String, Player>();
	private final List<TimeMessage> messages = new ArrayList<TimeMessage>();
	private final TimingRuler ruler = new TimingRuler(getSkinParam());
	private TimeTick now;
	private Player lastPlayer;

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
		imageBuilder.setUDrawable(getUDrawable());

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, os);
	}

	private UDrawable getUDrawable() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}
		};
	}

	public static final double marginX1 = 5;
	private final double marginX2 = 5;

	private void drawInternal(UGraphic ug) {
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

		for (Player player : players.values()) {
			final UGraphic playerUg = ug.apply(getUTranslateForPlayer(player, stringBounder));
			player.drawU(playerUg);
			player.drawContent(playerUg.apply(new UTranslate(withBeforeRuler, 0)));
			player.drawWidthHeader(playerUg);
			playerUg.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(borderStroke)
					.apply(new UTranslate(-marginX1, 0)).draw(new ULine(totalWith, 0));
		}
		ug = ug.apply(new UTranslate(withBeforeRuler, 0));
		ruler.draw(ug.apply(lastTranslate));
		for (TimeMessage timeMessage : messages) {
			drawMessages(ug, timeMessage);
		}
	}

	private double getWithBeforeRuler(StringBounder stringBounder) {
		double width = 0;
		for (Player player : players.values()) {
			width = Math.max(width, player.getGetWidthHeader(stringBounder));

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

		final TimeArrow timeArrow = TimeArrow.create(pt1.translated(translate1), pt2.translated(translate2),
				message.getLabel(), getSkinParam());
		timeArrow.drawU(ug);

	}

	public UTranslate getUTranslateForPlayer(Player candidat, StringBounder stringBounder) {
		double y = 0;
		for (Player player : players.values()) {
			final Dimension2D dim = player.calculateDimension(stringBounder);
			if (candidat == player) {
				return new UTranslate(0, y);
			}
			y += dim.getHeight();
		}
		if (candidat == null) {
			return new UTranslate(0, y);
		}
		throw new IllegalArgumentException();
	}

	public void createLifeLine(String code, String full, TimingStyle type) {
		final Player player = new Player(code, full, type, getSkinParam(), ruler);
		players.put(code, player);
		lastPlayer = player;
	}

	public void createTimeMessage(Player player1, TimeTick time1, Player player2, TimeTick time2, String label) {
		final TimeMessage message = new TimeMessage(new TickInPlayer(player1, time1), new TickInPlayer(player2, time2),
				label);
		messages.add(message);
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
		// if (now == null) {
		// throw new IllegalStateException();
		// }
		return now;
	}

	public void setLastPlayer(Player player) {
		this.lastPlayer = player;
	}

	public Player getLastPlayer() {
		return lastPlayer;
	}

}
