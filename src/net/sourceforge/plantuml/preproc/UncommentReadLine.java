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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.IOException;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.utils.StartUtils;

public class UncommentReadLine implements ReadLine {

	private final ReadLine raw;
	private final Pattern2 start;
	private final Pattern2 unpause;
	private String headerToRemove;
	private boolean paused;

	public UncommentReadLine(ReadLine source) {
		this.raw = source;
		this.start = MyPattern.cmpile(StartUtils.START_PATTERN);
		this.unpause = MyPattern.cmpile(StartUtils.PAUSE_PATTERN);
	}

	public CharSequence2 readLine() throws IOException {
		final CharSequence2 result = raw.readLine();

		if (result == null) {
			return null;
		}

		// final Matcher m = start.matcher(result);
		// if (m.find()) {
		// headerToRemove = m.group(1);
		// }
		final String tmp = StartUtils.beforeStartUml(result);
		if (tmp != null) {
			headerToRemove = tmp;
		}
		if (paused) {
			final Matcher2 m2 = unpause.matcher(result);
			if (m2.find()) {
				headerToRemove = m2.group(1);
			}
		}
		if (headerToRemove != null && headerToRemove.startsWith(result.toString2())) {
			return new CharSequence2Impl("", result.getLocation());
		}
		if (headerToRemove != null && result.toString2().startsWith(headerToRemove)) {
			return result.subSequence(headerToRemove.length(), result.length());
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
