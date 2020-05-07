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
package net.sourceforge.plantuml.code;

import java.io.IOException;
import java.io.StringReader;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;

public class ArobaseStringCompressor implements StringCompressor {

	private final static Pattern2 p = MyPattern.cmpile("(?s)(?i)^[%s]*(@startuml[^\\n\\r]*)?[%s]*(.*?)[%s]*(@enduml)?[%s]*$");

	public String compress(final String data) throws IOException {
		final ReadLine r = new UncommentReadLine(ReadLineReader.create(new StringReader(data), "COMPRESS"));
		final StringBuilder sb = new StringBuilder();
		final StringBuilder full = new StringBuilder();
		StringLocated s = null;
		boolean startDone = false;
		while ((s = r.readLine()) != null) {
			append(full, s);
			if (s.getString().startsWith("@startuml")) {
				startDone = true;
			} else if (s.getString().startsWith("@enduml")) {
				return sb.toString();
			} else if (startDone) {
				append(sb, s);
			}
		}
		if (startDone == false) {
			return compressOld(full.toString());
		}
		return sb.toString();
	}

	private void append(final StringBuilder sb, StringLocated s) {
		if (sb.length() > 0) {
			sb.append('\n');
		}
		sb.append(s.getString());
	}

	private String compressOld(String s) throws IOException {
		final Matcher2 m = p.matcher(s);
		if (m.find()) {
			return clean(m.group(2));
		}
		return "";
	}

	public String decompress(String s) {
		String result = clean(s);
		if (result.startsWith("@start")) {
			return result;
		}
		result = "@startuml\n" + result;
		if (result.endsWith("\n") == false) {
			result += "\n";
		}
		result += "@enduml";
		return result;
	}

	private String clean(String s) {
		// s = s.replace("\0", "");
		s = StringUtils.trin(s);
		s = clean1(s);
		s = s.replaceAll("@enduml[^\\n\\r]*", "");
		s = s.replaceAll("@startuml[^\\n\\r]*", "");
		s = StringUtils.trin(s);
		return s;
	}

	private String clean1(String s) {
		final Matcher2 m = p.matcher(s);
		if (m.matches()) {
			return m.group(2);
		}
		return s;
	}

}