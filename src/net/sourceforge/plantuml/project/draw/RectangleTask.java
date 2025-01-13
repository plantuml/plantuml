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
 *
 */
package net.sourceforge.plantuml.project.draw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.sequencediagram.graphic.Segment;

public class RectangleTask {

	private final List<Segment> segments;
	private final double round;
	private final int completion;

	public RectangleTask(double startPos, double endPos, double round, int completion, Collection<Segment> paused) {
		this.round = round;
		this.completion = completion;
		if (startPos < endPos)
			this.segments = new ArrayList<>(new Segment(startPos, endPos).cutSegmentIfNeed(paused));
		else
			this.segments = Collections.singletonList(new Segment(startPos, startPos + 1));

	}

	private void draw2hlines(UGraphic ug, double height, ULine hline) {
		ug.draw(hline);
		ug.apply(UTranslate.dy(height)).draw(hline);
	}

	public void draw(UGraphic ug, double height, HColor documentBackground, boolean oddStart, boolean oddEnd) {

		if (round == 0) {
			drawWithoutRound(ug, height, documentBackground, oddStart, oddEnd);
			return;
		}

		if (segments.size() != 1) {
			drawWithRound(ug, height, documentBackground);
			return;
		}

		assert segments.size() == 1;
		assert round > 0;
		final Segment segment = segments.get(0);

		final double width = segment.getLength();
		final URectangle partial = URectangle.build(width, height).rounded(round);
		if (completion == 100 || completion == 0) {
			if (completion == 0)
				ug = ug.apply(documentBackground.bg());
			if (oddStart && !oddEnd)
				ug.apply(UTranslate.dx(segment.getPos1())).draw(PathUtils.UtoRight(width, height, round));
			else if (!oddStart && oddEnd)
				ug.apply(UTranslate.dx(segment.getPos1())).draw(PathUtils.UtoLeft(width, height, round));
			else
				ug.apply(UTranslate.dx(segment.getPos1())).draw(partial);
		} else {
			final double x1 = width * completion / 100;
			ug.apply(HColors.none()).apply(UTranslate.dx(segment.getPos1())).draw(PathUtils.UtoLeft(x1, height, round));
			ug.apply(documentBackground.bg()).apply(HColors.none()).apply(UTranslate.dx(segment.getPos1() + x1))
					.draw(PathUtils.UtoRight(width * (100 - completion) / 100, height, round));
			ug.apply(HColors.none().bg()).apply(UTranslate.dx(segment.getPos1())).draw(partial);
		}

	}

	private void drawWithRound(UGraphic ug, double height, HColor documentBackground) {

		final Segment first = segments.get(0);
		ug.apply(UTranslate.dx(first.getPos1())).draw(PathUtils.UtoLeft(first.getLength(), height, round));

		for (int i = 1; i < segments.size() - 1; i++) {
			final Segment segment = segments.get(i);
			drawPartly(segment.getLength() * completion / 100, ug, segment, height, documentBackground, i, false,
					false);
		}

		final Segment last = segments.get(segments.size() - 1);
		ug.apply(UTranslate.dx(last.getPos1())).draw(PathUtils.UtoRight(last.getLength(), height, round));

		drawIntermediateDotted(ug, height);
	}

	private void drawWithoutRound(UGraphic ug, double height, HColor documentBackground, boolean oddStart,
			boolean oddEnd) {

		final double sumWidth = getFullSegmentsLength();
		final double lim = completion == 100 ? sumWidth : sumWidth * completion / 100;
		if (segments.size() == 1 && !oddStart && !oddEnd) {
			drawFull(completion == 100 ? -1 : lim, ug, segments.get(0), height, documentBackground);
			return;
		}
		double current = 0;

		for (int i = 0; i < segments.size(); i++) {
			final Segment segment = segments.get(i);
			final double next = current + segment.getLength();
			final double widthCompletion;
			if (lim >= next)
				widthCompletion = -1;
			else if (current >= lim)
				widthCompletion = 0;
			else {
				assert current < lim && lim < next;
				widthCompletion = lim - current;
			}

			drawPartly(widthCompletion, ug, segment, height, documentBackground, i, !oddStart && i == 0,
					!oddEnd && i == segments.size() - 1);

			current = next;
		}
		drawIntermediateDotted(ug, height);
	}

	private double getFullSegmentsLength() {
		double result = 0;
		for (Segment seg : segments)
			result += seg.getLength();
		return result;
	}

	private void drawIntermediateDotted(UGraphic ug, double height) {
		ug = ug.apply(new UStroke(2, 3, 1));
		for (int i = 0; i < segments.size() - 1; i++) {
			final double v1 = segments.get(i).getPos2() + 3;
			final double v2 = segments.get(i + 1).getPos1() - 3;
			if (v2 > v1)
				draw2hlines(ug.apply(UTranslate.dx(v1)), height, ULine.hline(v2 - v1));

		}
	}

	private void drawFull(double widthCompletion, UGraphic ug, final Segment segment, double height,
			HColor documentBackground) {
		double width = segment.getLength();
		ug = ug.apply(UTranslate.dx(segment.getPos1()));
		drawBackgroundRect(widthCompletion, ug.apply(HColors.none()), documentBackground, width, height);
		final URectangle rect = URectangle.build(width, height);
		ug.apply(HColors.none().bg()).draw(rect);
	}

	private void drawPartly(double widthCompletion, UGraphic ug, final Segment segment, double height,
			HColor documentBackground, int i, boolean withStartVline, boolean withEndVline) {

		if (withStartVline && withEndVline)
			throw new IllegalArgumentException();

		double widthBack = segment.getLength();
		if (i != segments.size() - 1)
			widthBack++;

		ug = ug.apply(UTranslate.dx(segment.getPos1()));

		if (widthBack > 0)
			drawBackgroundRect(widthCompletion, ug.apply(HColors.none()), documentBackground, widthBack, height);

		ug = ug.apply(HColors.none().bg());

		if (withStartVline)
			ug.draw(PathUtils.UtoLeft(segment.getLength(), height, 0));
		else if (withEndVline)
			ug.apply(UTranslate.dx(segment.getLength() - segment.getLength()))
					.draw(PathUtils.UtoRight(segment.getLength(), height, 0));
		else
			draw2hlines(ug, height, ULine.hline(segment.getLength()));

	}

	private void drawBackgroundRect(double widthCompletion, UGraphic ug, HColor documentBackground, double width,
			double height) {
		if (widthCompletion == -1 || widthCompletion == 0) {
			if (widthCompletion == 0)
				ug = ug.apply(documentBackground.bg());
			final URectangle rect = URectangle.build(width, height);
			ug.draw(rect);
		} else {
			final URectangle rect1 = URectangle.build(widthCompletion, height);
			ug.draw(rect1);
			final URectangle rect2 = URectangle.build(width - widthCompletion, height);
			ug.apply(documentBackground.bg()).apply(UTranslate.dx(widthCompletion)).draw(rect2);
		}
	}

}
