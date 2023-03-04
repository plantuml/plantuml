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
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;

public class DedicationSimple implements Dedication {

	private final byte crypted[];
	private final String sentence;

	public DedicationSimple(byte crypted[], String sentence) {
		this.crypted = crypted;
		this.sentence = sentence;
	}

	public synchronized BufferedImage getImage(TinyHashableString sentence) {
		if (same(this.sentence, sentence.getSentence()) == false)
			return null;

		try {
			byte[] current = crypted.clone();

			final RBlocks init = RBlocks.readFrom(current, 513);
			final RBlocks decoded = init.change(E, N);
			current = decoded.toByteArray(512);
			return SFile.getBufferedImageFromWebpButHeader(new ByteArrayInputStream(current));
		} catch (Throwable t) {
			Logme.error(t);
			return null;
		}
	}

	private boolean same(String s1, String s2) {
		s1 = s1.replaceAll("[^\\p{L}0-9]+", "");
		s2 = s2.replaceAll("[^\\p{L}0-9]+", "");
		return s1.equalsIgnoreCase(s2);
	}

}
