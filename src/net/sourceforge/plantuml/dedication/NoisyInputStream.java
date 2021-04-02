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

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.utils.MTRandom;

public class NoisyInputStream extends InputStream {

	private final MTRandom rnd;
	private final InputStream source;

	public NoisyInputStream(InputStream source, byte[] pass) {
		this.source = source;
		this.rnd = new MTRandom(pass);
	}

	private byte getNextByte() {
		return (byte) rnd.nextInt();
	}

	@Override
	public void close() throws IOException {
		source.close();
	}

	@Override
	public int read() throws IOException {
		int b = source.read();
		if (b == -1) {
			return -1;
		}
		b = (b ^ getNextByte()) & 0xFF;
		return b;
	}

}
