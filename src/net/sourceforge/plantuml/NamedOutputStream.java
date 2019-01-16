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
package net.sourceforge.plantuml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NamedOutputStream extends OutputStream {

	private final OutputStream os;
	private final BaseFile basefile;

	public NamedOutputStream(File file) throws FileNotFoundException {
		this.os = new BufferedOutputStream(new FileOutputStream(file));
		this.basefile = new BaseFile(file);
	}

	public void close() throws IOException {
		os.close();

	}

	public void flush() throws IOException {
		os.flush();

	}

	public void write(byte[] b) throws IOException {
		os.write(b);

	}

	public void write(byte[] b, int off, int len) throws IOException {
		os.write(b, off, len);
	}

	public void write(int b) throws IOException {
		os.write(b);
	}

	public BaseFile getBasefile() {
		return basefile;
	}

}
