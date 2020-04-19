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
package net.sourceforge.plantuml.flashcode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import ext.plantuml.com.google.zxing.BarcodeFormat;
import ext.plantuml.com.google.zxing.EncodeHintType;
import ext.plantuml.com.google.zxing.WriterException;
import ext.plantuml.com.google.zxing.client.j2se.MatrixToImageWriter;
import ext.plantuml.com.google.zxing.common.BitMatrix;
import ext.plantuml.com.google.zxing.qrcode.QRCodeWriter;
import ext.plantuml.com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.sourceforge.plantuml.Log;

public class FlashCodeUtilsZxing implements FlashCodeUtils {

	private static final boolean USE_FLASH = true;

	public BufferedImage exportFlashcode(String s, Color fore, Color back) {
		if (USE_FLASH == false) {
			return null;
		}
		try {
			final QRCodeWriter writer = new QRCodeWriter();
			final Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
			final int multiple = 1;
			final BitMatrix bit = writer.encode(s, BarcodeFormat.QR_CODE, multiple, hints);
			return MatrixToImageWriter.toBufferedImage(bit, fore.getRGB() | 0xFF000000, back.getRGB() | 0xFF000000);
		} catch (WriterException e) {
			Log.debug("Cannot create qrcode " + e);
			// e.printStackTrace();
			return null;
		}
	}

}
