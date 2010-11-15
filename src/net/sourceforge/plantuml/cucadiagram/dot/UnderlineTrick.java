/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3829 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UnderlineTrick {

	static class Segment {
		final private int start;
		final private int end;

		Segment(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return "" + start + "-" + end;
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("Usage: fileName searchedColor underlineColor");
			System.exit(0);
		}
		final File f = new File(args[0]);
		final BufferedImage im = ImageIO.read(f);
		final Color searchedColor = new Color(Integer.parseInt(args[1], 16));
		final Color underlineColor = new Color(Integer.parseInt(args[2], 16));
		new UnderlineTrick(im, searchedColor, underlineColor).process();
		ImageIO.write(im, "png", f);

	}

	private final BufferedImage im;
	private final int searchedColor;
	private final int underlineColor;

	public UnderlineTrick(BufferedImage im, Color searchedColor, Color underlineColor) {
		this.im = im;
		this.searchedColor = searchedColor.getRGB();
		this.underlineColor = underlineColor.getRGB();
	}

	public void process() {
		for (int line = 0; line < im.getHeight(); line++) {
			int x = 0;
			while (x < im.getWidth()) {
				final Segment segStart = searchSegment(x, line);
				if (segStart == null) {
					break;
				}
				final Segment segEnd = searchSegment(segStart.end + 1, line);
				if (segEnd == null) {
					break;
				}
				drawLine(segStart.end, segEnd.start, line);
				x = segEnd.end + 1;
			}

		}
	}

	private void drawLine(int start, int end, int line) {
		for (int i = start; i < end; i++) {
			im.setRGB(i, line, underlineColor);
		}
	}

	private Segment searchSegment(final int i, final int line) {
		for (int nb = i; nb < im.getWidth(); nb++) {
			final Segment seg = searchSegmentExact(nb, line);
			if (seg != null) {
				return seg;
			}
		}
		return null;

	}

	private Segment searchSegmentExact(final int i, final int line) {
		for (int nb = 0; nb < 6; nb++) {
			if (im.getRGB(i + nb, line) != searchedColor) {
				return null;
			}
		}
		for (int end = i; end < im.getWidth(); end++) {
			if (im.getRGB(end, line) != searchedColor) {
				return new Segment(i, end - 1);
			}
		}
		return new Segment(i, im.getWidth() - 1);
	}

}
