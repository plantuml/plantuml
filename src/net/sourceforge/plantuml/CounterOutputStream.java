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
 * Revision $Revision: 4780 $
 *
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;

public class CounterOutputStream extends OutputStream {

	private int length;
	private final OutputStream os;

	public CounterOutputStream(OutputStream os) {
		this.os = os;
	}

	/**
	 * Writes to nowhere
	 */
	@Override
	public void write(int b) throws IOException {
		os.write(b);
		length++;
	}

	/**
	 * Overridden for performance reason
	 */
	@Override
	public void write(byte b[]) throws IOException {
		os.write(b);
		length += b.length;
	}

	/**
	 * Overridden for performance reason
	 */
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		os.write(b, off, len);
		length += len;
	}

	public int getLength() {
		return length;
	}
	
	@Override
	public void flush() throws IOException {
		os.flush();
	}
	
	
	@Override
	public void close() throws IOException {
		os.close();
	}

}
