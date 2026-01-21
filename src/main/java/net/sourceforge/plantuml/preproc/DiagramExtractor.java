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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.utils.StartUtils;

public class DiagramExtractor implements ReadLine {

	private final ReadLine raw;
	private boolean finished = false;

	public DiagramExtractor(ReadLine raw, String suf) {
		int bloc = 0;
		String uid = null;
		if (suf != null && suf.matches("\\d+"))
			bloc = Integer.parseInt(suf);
		else
			uid = suf;

		if (bloc < 0)
			bloc = 0;

		this.raw = raw;
		StringLocated s = null;
		try {
			while ((s = raw.readLine()) != null) {
				if (StartUtils.isArobaseStartDiagram(s.getString()) && checkUid(uid, s)) {
					if (bloc == 0)
						return;
					bloc--;
				}
			}
		} catch (IOException e) {
			Logme.error(e);
			Log.error("Error " + e);
		}
		finished = true;
	}

	private boolean checkUid(String uid, StringLocated s) {
		if (uid == null)
			return true;

		if (s.toString().matches(".*id=" + uid + "\\W.*"))
			return true;

		return false;
	}

	public StringLocated readLine() throws IOException {
		if (finished)
			return null;

		final StringLocated result = raw.readLine();
		if (result != null && StartUtils.isArobaseEndDiagram(result.getString())) {
			finished = true;
			return null;
		}
		return result;
	}

	public void close() throws IOException {
		raw.close();
	}

}
