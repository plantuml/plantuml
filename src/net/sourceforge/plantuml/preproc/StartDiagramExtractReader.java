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
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc2.ReadFilterMergeLines;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.utils.StartUtils;

public class StartDiagramExtractReader implements ReadLine {

	private final ReadLine raw;
	private boolean finished = false;

	public static StartDiagramExtractReader build(FileWithSuffix f2, StringLocated s, Charset charset) {
		return new StartDiagramExtractReader(getReadLine(f2, s, charset), f2.getSuffix());
	}

	public static StartDiagramExtractReader build(SURL url, StringLocated s, String uid, Charset charset) {
		return new StartDiagramExtractReader(getReadLine(url, s, charset), uid);
	}

	public static StartDiagramExtractReader build(InputStream is, StringLocated s, String desc) {
		return new StartDiagramExtractReader(getReadLine(is, s, desc), null);
	}

	private StartDiagramExtractReader(ReadLine raw, String suf) {
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

	private static ReadLine getReadLine(FileWithSuffix f2, StringLocated s, Charset charset) {
		try {
			final Reader tmp1 = f2.getReader(charset);
			if (tmp1 == null)
				return new ReadLineSimple(s, "Cannot open " + f2.getDescription());

			return uncommentAndMerge(ReadLineReader.create(tmp1, f2.getDescription()));
		} catch (IOException e) {
			return new ReadLineSimple(s, e.toString());
		}
	}

	private static ReadLine getReadLine(InputStream is, StringLocated s, String description) {
		return uncommentAndMerge(ReadLineReader.create(new InputStreamReader(is), description));
	}

	private static ReadLine getReadLine(SURL url, StringLocated s, Charset charset) {
		final InputStream tmp = url.openStream();
		if (tmp == null)
			return new ReadLineSimple(s, "Cannot connect");

		return uncommentAndMerge(ReadLineReader.create(new InputStreamReader(tmp, charset), url.toString()));
	}

	private static ReadLine uncommentAndMerge(ReadLine reader) {
		return new UncommentReadLine(new ReadFilterMergeLines().applyFilter(reader));
	}

	static public boolean containsStartDiagram(FileWithSuffix f2, StringLocated s, Charset charset) throws IOException {
		final ReadLine r = getReadLine(f2, s, charset);
		return containsStartDiagram(r);
	}

	static public boolean containsStartDiagram(SURL url, StringLocated s, Charset charset) throws IOException {
		final ReadLine r = getReadLine(url, s, charset);
		return containsStartDiagram(r);
	}

	static public boolean containsStartDiagram(InputStream is, StringLocated s, String description) throws IOException {
		final ReadLine r = getReadLine(is, s, description);
		return containsStartDiagram(r);
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
