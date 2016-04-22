/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.webp.VP8Decoder;

public class Dedication {

	private final String signature;

	public Dedication(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	public byte[] getKey(String keepLetter) {
		try {
			return keepLetter.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private InputStream getInputStream(String keepLetter) {
		final byte[] key = getKey(keepLetter);
		final InputStream tmp = PSystemDedication.class.getResourceAsStream(getSignature() + ".png");
		return new DecoderInputStream(tmp, key);
	}

	public BufferedImage getBufferedImage(String keepLetter) {
		try {
			final InputStream is = getInputStream(keepLetter);
			final ImageInputStream iis = ImageIO.createImageInputStream(is);
			final VP8Decoder vp8Decoder = new VP8Decoder();
			vp8Decoder.decodeFrame(iis, false);
			iis.close();
			return vp8Decoder.getFrame().getBufferedImage();
		} catch (Exception e) {
			return null;
		}
	}

}
