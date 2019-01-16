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
 *
 */
package net.sourceforge.plantuml.openiconic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;

public class SvgPath {

	// http://www.w3.org/TR/SVG11/paths.html#PathDataEllipticalArcCommands
	// https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths
	// http://tutorials.jenkov.com/svg/path-element.html

	private List<Movement> movements = new ArrayList<Movement>();
	private List<SvgCommand> commands = new ArrayList<SvgCommand>();

	public SvgPath(String path) {
		path = StringDecipher.decipher(path);
		// List<SvgCommand> commands = new ArrayList<SvgCommand>();
		for (final StringTokenizer st = new StringTokenizer(path); st.hasMoreTokens();) {
			final String token = st.nextToken();
			// System.err.println("token=" + token);
			if (token.matches("[a-zA-Z]")) {
				commands.add(new SvgCommandLetter(token));
			} else {
				commands.add(new SvgCommandNumber(token));
			}
		}
		commands = manageHV(commands);
		commands = insertMissingLetter(commands);
		checkArguments(commands);
		SvgPosition last = new SvgPosition();
		SvgPosition mirrorControlPoint = null;
		final Iterator<SvgCommand> iterator = commands.iterator();
		while (iterator.hasNext()) {
			Movement movement = new Movement(iterator);
			// System.err.println("before=" + movement.toSvg());
			movement = movement.toAbsoluteUpperCase(last);
			// System.err.println("after=" + movement.toSvg());
			if (movement.is('S')) {
				// System.err.println(" before " + movement.toSvg());
				movement = movement.mutoToC(mirrorControlPoint);
				// System.err.println(" after " + movement.toSvg());
			}
			movements.add(movement);
			if (movement.lastPosition() != null) {
				last = movement.lastPosition();
			}
			mirrorControlPoint = movement.getMirrorControlPoint();
		}
	}

	private List<SvgCommand> insertMissingLetter(List<SvgCommand> commands) {
		final List<SvgCommand> result = new ArrayList<SvgCommand>();
		final Iterator<SvgCommand> it = commands.iterator();
		SvgCommandLetter lastLetter = null;
		while (it.hasNext()) {
			final SvgCommand cmd = it.next();
			// System.err.println("cmd=" + cmd);
			final int nb;
			if (cmd instanceof SvgCommandNumber) {
				// System.err.println("INSERTING " + lastLetter);
				result.add(lastLetter);
				result.add(cmd);
				nb = lastLetter.argumentNumber() - 1;
			} else {
				result.add(cmd);
				lastLetter = (SvgCommandLetter) cmd;
				nb = lastLetter.argumentNumber();
			}
			for (int i = 0; i < nb; i++) {
				final SvgCommandNumber number = (SvgCommandNumber) it.next();
				result.add(number);
			}
		}
		return result;
	}

	private void checkArguments(List<SvgCommand> commands) {
		final Iterator<SvgCommand> it = commands.iterator();
		while (it.hasNext()) {
			final SvgCommandLetter cmd = (SvgCommandLetter) it.next();
			final int nb = cmd.argumentNumber();
			for (int i = 0; i < nb; i++) {
				final SvgCommandNumber number = (SvgCommandNumber) it.next();
			}
		}
	}

	private List<SvgCommand> manageHV(List<SvgCommand> commands) {
		final List<SvgCommand> result = new ArrayList<SvgCommand>();
		SvgCommandNumber lastX = null;
		SvgCommandNumber lastY = null;
		final Iterator<SvgCommand> it = commands.iterator();
		while (it.hasNext()) {
			final SvgCommand cmd = it.next();
			if (cmd instanceof SvgCommandNumber) {
				lastX = lastY;
				lastY = (SvgCommandNumber) cmd;
				result.add(cmd);
				continue;
			}
			final String letter = cmd.toSvg();
			if (letter.equals("h")) {
				result.add(new SvgCommandLetter("l"));
				result.add(it.next());
				result.add(new SvgCommandNumber("0"));
			} else if (letter.equals("v")) {
				result.add(new SvgCommandLetter("l"));
				result.add(new SvgCommandNumber("0"));
				result.add(it.next());
			} else {
				result.add(cmd);
			}
		}
		return result;
	}

	public String toSvg() {
		final StringBuilder result = new StringBuilder("<path d=\"");
		for (Movement move : movements) {
			result.append(move.toSvg());
			result.append(' ');
		}
		result.append("\"/>");
		return result.toString();
	}

	private String toSvgNew() {
		final StringBuilder result = new StringBuilder("<path d=\"");
		for (SvgCommand cmd : commands) {
			result.append(cmd.toSvg());
			result.append(' ');
		}
		result.append("\"/>");
		return result.toString();
	}

	private UPath toUPath(double factor) {
		final UPath result = new UPath();
		for (Movement move : movements) {
			final char letter = move.getLetter();
			final SvgPosition lastPosition = move.lastPosition();
			if (letter == 'M') {
				result.moveTo(lastPosition.getXDouble() * factor, lastPosition.getYDouble() * factor);
			} else if (letter == 'C') {
				final SvgPosition ctl1 = move.getSvgPosition(0);
				final SvgPosition ctl2 = move.getSvgPosition(2);
				result.cubicTo(ctl1.getXDouble() * factor, ctl1.getYDouble() * factor, ctl2.getXDouble() * factor,
						ctl2.getYDouble() * factor, lastPosition.getXDouble() * factor, lastPosition.getYDouble()
								* factor);
			} else if (letter == 'L') {
				result.lineTo(lastPosition.getXDouble() * factor, lastPosition.getYDouble() * factor);
			} else if (letter == 'A') {

				final double rx = move.getArgument(0);
				final double ry = move.getArgument(1);
				final double x_axis_rotation = move.getArgument(2);
				final double large_arc_flag = move.getArgument(3);
				final double sweep_flag = move.getArgument(4);
				result.arcTo(rx * factor, ry * factor, x_axis_rotation, large_arc_flag, sweep_flag,
						lastPosition.getXDouble() * factor, lastPosition.getYDouble() * factor);
			} else if (letter == 'Z') {
				result.closePath();
			} else {
				throw new UnsupportedOperationException("letter " + letter);
			}

		}
		result.setOpenIconic(true);
		return result;
	}

	public void drawMe(UGraphic ug, double factor) {
		final UPath path = toUPath(factor);
		ug.draw(path);
	}
}
