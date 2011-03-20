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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class StartumlExtractReader implements ReadLine {

	private final ReadLine raw;

	public StartumlExtractReader(File f, int num) throws FileNotFoundException {
		if (num < 0) {
			throw new IllegalArgumentException();
		}
		raw = getReadLine(f);
	}

	static public boolean containsStartuml(File f) throws IOException {
		ReadLine r = null;
		try {
			r = getReadLine(f);
		} finally {
			if (r != null) {
				r.close();
			}
		}
		return false;
	}

	private static ReadLine getReadLine(File f) throws FileNotFoundException {
		return new UncommentReadLine(new ReadLineReader(new FileReader(f)));
	}

	public String readLine() throws IOException {
		return raw.readLine();
	}

	public void close() throws IOException {
		raw.close();
	}

}
