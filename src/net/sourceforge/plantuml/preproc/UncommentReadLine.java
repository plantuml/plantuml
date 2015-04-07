/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 5200 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;

public class UncommentReadLine implements ReadLine {

	private final ReadLine raw;
	private final Pattern start;
	private final Pattern unpause;
	private String headerToRemove;
	private boolean paused;

	public UncommentReadLine(ReadLine source) {
		this.raw = source;
		this.start = MyPattern.cmpile("(?i)((?:\\W|\\<[^<>]*\\>)*)@start");
		this.unpause = MyPattern.cmpile("(?i)((?:\\W|\\<[^<>]*\\>)*)@unpause");
	}

	public String readLine() throws IOException {
		final String result = raw.readLine();

		if (result == null) {
			return null;
		}

		final Matcher m = start.matcher(result);
		if (m.find()) {
			headerToRemove = m.group(1);
		}
		if (paused) {
			final Matcher m2 = unpause.matcher(result);
			if (m2.find()) {
				headerToRemove = m2.group(1);
			}
		}
		if (headerToRemove != null && headerToRemove.startsWith(result)) {
			return "";
		}
		if (headerToRemove != null && result.startsWith(headerToRemove)) {
			return result.substring(headerToRemove.length());
		}
		return result;
	}

	public void close() throws IOException {
		this.raw.close();
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
