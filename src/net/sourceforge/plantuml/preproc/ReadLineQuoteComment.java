/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;

public class ReadLineQuoteComment implements ReadLine {

	private final ReadLine raw;
	private boolean longComment = false;

	public ReadLineQuoteComment(ReadLine source) {
		this.raw = source;
	}

	public void close() throws IOException {
		raw.close();
	}

	public CharSequence2 readLine() throws IOException {
		while (true) {
			final CharSequence2 result = raw.readLine();
			if (result == null) {
				return null;
			}
			final String trim = result.toString().replace('\t', ' ').trim();
			if (this.longComment && trim.endsWith("'/")) {
				this.longComment = false;
				continue;
			}
			if (this.longComment) {
				continue;
			}
			if (trim.startsWith("'")) {
				continue;
			}
			if (trim.startsWith("/'") && trim.endsWith("'/")) {
				continue;
			}
			if (trim.startsWith("/'") && trim.contains("'/") == false) {
				this.longComment = true;
				continue;
			}
			return ((CharSequence2Impl) result).removeInnerComment();
		}
	}

}
