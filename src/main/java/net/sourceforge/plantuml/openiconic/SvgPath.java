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
package net.sourceforge.plantuml.openiconic;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class SvgPath {

	// http://www.w3.org/TR/SVG11/paths.html#PathDataEllipticalArcCommands
	// https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths
	// http://tutorials.jenkov.com/svg/path-element.html
	// https://www.w3.org/TR/SVG/paths.html#PathDataQuadraticBezierCommands

	private final List<Movement> movements = new ArrayList<>();
	private List<SvgCommand> commands = new ArrayList<>();
	private final UTranslate translate;

	public SvgPath(String path, UTranslate translate) {
		this.translate = translate;
		// System.err.println("before=" + path);
		final List<CharSequence> decipher = StringDecipher.decipher(path);

		for (final CharSequence token : decipher) {
			if (token.length() == 1 && Character.isLetter(token.charAt(0)))
				commands.add(new SvgCommandLetter(token.charAt(0)));
			else
				commands.add(SvgCommandNumber.parse(token.toString()));

		}
		commands = insertMissingLetter(commands);
		checkArguments(commands);
		SvgPosition last = new SvgPosition();
		SvgPosition lastMove = new SvgPosition();
		SvgPosition mirrorControlPoint = null;
		final Iterator<SvgCommand> iterator = commands.iterator();
		while (iterator.hasNext()) {
			Movement movement = new Movement(iterator);
			movement = movement.toAbsoluteUpperCase(last);

			if (movement.getLetter() == 'Z')
				last = lastMove;

			if (movement.is('S'))
				movement = movement.mutoToC(mirrorControlPoint);

			movements.add(movement);

			if (movement.getLetter() == 'M')
				lastMove = movement.lastPosition();

			if (movement.lastPosition() != null)
				last = movement.lastPosition();

			mirrorControlPoint = movement.getMirrorControlPoint();
		}
	}

	private List<SvgCommand> insertMissingLetter(List<SvgCommand> commands) {
		final List<SvgCommand> result = new ArrayList<>();
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
				lastLetter = ((SvgCommandLetter) cmd).implicit();
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

	public String toSvg() {
		final StringBuilder result = new StringBuilder("<path d=\"");
		for (Movement move : movements) {
			result.append(move.toSvg());
			result.append(' ');
		}
		result.append("\"/>");
		return result.toString();
	}

	private UPath toUPath(double factorx, double factory) {
		UPath result = UPath.none();
		Movement previous = null;
		for (Movement move : movements) {
			final char letter = move.getLetter();
			final SvgPosition position = move.lastPosition();
			if (letter == 'M') {
				result.moveTo(position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'C') {
				final SvgPosition ctl1 = move.getSvgPosition(0);
				final SvgPosition ctl2 = move.getSvgPosition(2);
				result.cubicTo(ctl1.getXDouble() * factorx, ctl1.getYDouble() * factory, ctl2.getXDouble() * factorx,
						ctl2.getYDouble() * factory, position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'Q') {
				final SvgPosition ctl = move.getSvgPosition(0);
				result.cubicTo(ctl.getXDouble() * factorx, ctl.getYDouble() * factory, ctl.getXDouble() * factorx,
						ctl.getYDouble() * factory, position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'T') {
				if (previous.getLetter() != 'Q')
					throw new IllegalArgumentException();
				// https://stackoverflow.com/questions/5287559/calculating-control-points-for-a-shorthand-smooth-svg-path-bezier-curve
				final SvgPosition lastCtl = previous.getSvgPosition(0);
				final SvgPosition lastP = previous.lastPosition();
				final SvgPosition ctl = lastP.getMirror(lastCtl);
				result.cubicTo(ctl.getXDouble() * factorx, ctl.getYDouble() * factory, ctl.getXDouble() * factorx,
						ctl.getYDouble() * factory, position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'L') {
				result.lineTo(position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'A') {
				final double rx = move.getArgument(0);
				final double ry = move.getArgument(1);
				final double x_axis_rotation = move.getArgument(2);
				final double large_arc_flag = move.getArgument(3);
				final double sweep_flag = move.getArgument(4);
				result.arcTo(rx * factorx, ry * factory, x_axis_rotation, large_arc_flag, sweep_flag,
						position.getXDouble() * factorx, position.getYDouble() * factory);
			} else if (letter == 'Z') {
				result.closePath();
			} else {
				throw new UnsupportedOperationException("letter " + letter);
			}
		}
		result = result.translate(translate.getDx() * factorx, translate.getDy() * factory);
		return result;
	}

	private UPath toUPath(AffineTransform at) {
		UPath result = UPath.none();
		Movement previous = null;
		for (Movement move : movements) {
			final char letter = move.getLetter();
			final SvgPosition position = move.lastPosition();
			if (letter == 'M') {
				result.moveTo(position.affine(at));
			} else if (letter == 'C') {
				final SvgPosition ctl1 = move.getSvgPosition(0);
				final SvgPosition ctl2 = move.getSvgPosition(2);
				result.cubicTo(ctl1.affine(at), ctl2.affine(at), position.affine(at));
			} else if (letter == 'Q') {
				final SvgPosition ctl = move.getSvgPosition(0);
				result.cubicTo(ctl.affine(at), ctl.affine(at), position.affine(at));
			} else if (letter == 'T') {
				if (previous.getLetter() == 'Q') {
					// https://stackoverflow.com/questions/5287559/calculating-control-points-for-a-shorthand-smooth-svg-path-bezier-curve
					final SvgPosition lastCtl = previous.getSvgPosition(0);
					final SvgPosition lastP = previous.lastPosition();
					final SvgPosition ctl = lastP.getMirror(lastCtl);
					result.cubicTo(ctl.affine(at), ctl.affine(at), position.affine(at));
				} else if (previous.getLetter() == 'T') {
					// Same logic as for Q followed by T: reflect the previous control point
					final SvgPosition lastCtl = previous.getSvgPosition(0);
					final SvgPosition lastP = previous.lastPosition();
					final SvgPosition ctl = lastP.getMirror(lastCtl);
					result.cubicTo(ctl.affine(at), ctl.affine(at), position.affine(at));
				} else
					throw new IllegalArgumentException("" + previous.getLetter());
			} else if (letter == 'L') {
				result.lineTo(position.affine(at));
			} else if (letter == 'A') {
				final double rx = move.getArgument(0);
				final double ry = move.getArgument(1);
				final double x_axis_rotation = move.getArgument(2);
				final double large_arc_flag = move.getArgument(3);
				final double sweep_flag = move.getArgument(4);
				final XPoint2D tmp = position.affine(at);
				result.arcTo(rx * at.getScaleX(), ry * at.getScaleY(), x_axis_rotation, large_arc_flag, sweep_flag,
						tmp.getX(), tmp.getY());
			} else if (letter == 'Z') {
				result.closePath();
			} else {
				throw new UnsupportedOperationException("letter " + letter);
			}
			previous = move;
		}
		result = result.translate(translate.getDx() * at.getScaleX(), translate.getDy() * at.getScaleY());
		return result;
	}

	public void drawMe(UGraphic ug, double factor) {
		final UPath path = toUPath(factor, factor);
		ug.draw(path);
	}

	public void drawMe(UGraphic ug, AffineTransform at) {
		final UPath path = toUPath(at);
		ug.draw(path);
	}
}
