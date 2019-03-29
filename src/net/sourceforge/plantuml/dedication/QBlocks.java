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
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QBlocks {

	private final List<QBlock> all = new ArrayList<QBlock>();

	private QBlocks() {

	}

	public static QBlocks readFrom(InputStream source, int size) throws IOException {
		final QBlocks result = new QBlocks();
		while (true) {
			final QBlock block = QBlock.read(source, size);
			if (block == null) {
				return result;
			}
			result.all.add(block);
		}
	}

	public QBlocks change(BigInteger E, BigInteger N) {
		final QBlocks result = new QBlocks();
		for (QBlock rsa : all) {
			result.all.add(rsa.change(E, N));
		}
		return result;
	}

	public void writeTo(OutputStream os, int size) throws IOException {
		for (QBlock rsa : all) {
			rsa.write(os, size);
		}
	}

//	public String encodeAscii() {
//		final StringBuilder sb = new StringBuilder();
//		final AsciiEncoder encoder = new AsciiEncoder();
//		for (QBlock rsa : all) {
//			sb.append(encoder.encode(rsa.getDataRaw()));
//			sb.append("!");
//		}
//		return sb.toString();
//	}

//	public static QBlocks descodeAscii(String s) {
//		final QBlocks result = new QBlocks();
//		final AsciiEncoder encoder = new AsciiEncoder();
//		for (String bl : s.split("!")) {
//			final BigInteger bigInteger = new BigInteger(encoder.decode(bl));
//			result.all.add(new QBlock(bigInteger));
//
//		}
//		return result;
//	}
}
