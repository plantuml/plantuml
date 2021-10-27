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
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;

public class PSystemDedication extends PlainDiagram {

	private final BufferedImage img;

	public PSystemDedication(UmlSource source, BufferedImage img) {
		super(source);
		this.img = Objects.requireNonNull(img);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug.draw(new UImage(new PixelImage(img, AffineTransformType.TYPE_BILINEAR)));
			}
		};
	}

	public static BufferedImage getBufferedImage(InputStream is) {
		try {
			final Class<?> clVP8Decoder = Class.forName("net.sourceforge.plantuml.webp.VP8Decoder");
			final Object vp8Decoder = clVP8Decoder.newInstance();
			// final VP8Decoder vp8Decoder = new VP8Decoder();
			final Method decodeFrame = clVP8Decoder.getMethod("decodeFrame", ImageInputStream.class);
			final ImageInputStream iis = SImageIO.createImageInputStream(is);
			decodeFrame.invoke(vp8Decoder, iis);
			// vp8Decoder.decodeFrame(iis);
			iis.close();
			final Object frame = clVP8Decoder.getMethod("getFrame").invoke(vp8Decoder);
			return (BufferedImage) frame.getClass().getMethod("getBufferedImage").invoke(frame);
			// final VP8Frame frame = vp8Decoder.getFrame();
			// return frame.getBufferedImage();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Dedication)");
	}

}
