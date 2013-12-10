/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StartUtils;

public class StartDiagramExtractReader implements ReadLine {

	private final ReadLine raw;
	private boolean finished = false;

	public StartDiagramExtractReader(File f, int num, String charset) throws IOException {
		if (num < 0) {
			throw new IllegalArgumentException();
		}
		// this.charset = charset;
		raw = getReadLine(f, charset);
		String s = null;
		while ((s = raw.readLine()) != null) {
			if (StartUtils.isArobaseStartDiagram(s)) {
				if (num == 0) {
					return;
				}
				num--;
			}
		}
		finished = true;
	}

	private static ReadLine getReadLine(File f, String charset) throws IOException {
		
		if (charset == null) {
			Log.info("Using default charset");
			return new UncommentReadLine(new ReadLineReader(new FileReader(f)));
		}
		Log.info("Using charset " + charset);
		return new UncommentReadLine(new ReadLineReader(new InputStreamReader(new FileInputStream(f), charset)));
	}

	static public boolean containsStartDiagram(File f, String charset) throws IOException {
		ReadLine r = null;
		try {
			r = getReadLine(f, charset);
			String s = null;
			while ((s = r.readLine()) != null) {
				if (StartUtils.isArobaseStartDiagram(s)) {
					return true;
				}
			}
		} finally {
			if (r != null) {
				r.close();
			}
		}
		return false;
	}

	public String readLine() throws IOException {
		if (finished) {
			return null;
		}
		final String result = raw.readLine();
		if (result != null && StartUtils.isArobaseEndDiagram(result)) {
			finished = true;
			return null;
		}
		return result;
	}

	public void close() throws IOException {
		raw.close();
	}

}
