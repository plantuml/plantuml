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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.preproc2.ReadFilterMergeLines;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.StartUtils;

public class DiagramDetector {

	static public ReadLine extractFromUrl(SURL url, StringLocated s, String uid, Charset charset) throws IOException {
		final ReadLine raw = newReadLineFromUrl(url, s, charset);
		if (containsStartDiagram(raw)) {
			final ReadLine raw1 = newReadLineFromUrl(url, s, charset);
			return new DiagramExtractor(raw1, uid);
		}
		return null;
	}

	static public ReadLine extractFromBytes(byte[] puml, String description) throws IOException {
		final ReadLine raw = newReadLineFromInputStream(new ByteArrayInputStream(puml), description);
		if (containsStartDiagram(raw)) {
			final ReadLine raw1 = newReadLineFromInputStream(new ByteArrayInputStream(puml), description);
			return new DiagramExtractor(raw1, null);
		}
		return null;
	}

	public static ReadLine extractFromFile(InputFile f2, String description) throws IOException {
		final ReadLine raw = newReadLineFromInputStream(f2.newInputStream(), description);
		if (containsStartDiagram(raw)) {
			final ReadLine raw1 = newReadLineFromInputStream(f2.newInputStream(), description);
			return new DiagramExtractor(raw1, null);
		}
		return null;
	}

	private static ReadLine newReadLineFromInputStream(InputStream is, String description) {
		final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		final ReadLine raw = ReadLineReader.create(isr, description);
		return uncommentAndMerge(raw);
	}

	private static ReadLine newReadLineFromUrl(SURL url, StringLocated s, Charset charset) {
		final InputStream tmp = url.openStream();
		if (tmp == null)
			return new ReadLineSimple(s, "Cannot connect");

		final InputStreamReader isr = new InputStreamReader(tmp, charset);
		final ReadLine raw = ReadLineReader.create(isr, url.toString());
		return uncommentAndMerge(raw);
	}

	private static ReadLine uncommentAndMerge(ReadLine reader) {
		final ReadLine raw = new ReadFilterMergeLines().applyFilter(reader);
		return new UncommentReadLine(raw);
	}

	private static boolean containsStartDiagram(final ReadLine r) throws IOException {
		try {
			StringLocated s = null;
			while ((s = r.readLine()) != null)
				if (StartUtils.isArobaseStartDiagram(s.getString()))
					return true;

		} finally {
			if (r != null)
				r.close();

		}
		return false;
	}

}
