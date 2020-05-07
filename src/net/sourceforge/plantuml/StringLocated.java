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

import net.sourceforge.plantuml.command.regex.FoxSignature;
import net.sourceforge.plantuml.tim.TLineType;

final public class StringLocated {

	private final String s;
	private final LineLocation location;
	private final String preprocessorError;

	private StringLocated trimmed;
	private long fox = -1;
	private TLineType type;

	public StringLocated(String s, LineLocation location) {
		this(s, location, null);
	}

	@Override
	public String toString() {
		return s;
	}

	public StringLocated append(String endOfLine) {
		return new StringLocated(s + endOfLine, location, preprocessorError);
	}

	public StringLocated mergeEndBackslash(StringLocated next) {
		if (StringUtils.endsWithBackslash(s) == false) {
			throw new IllegalArgumentException();
		}
		return new StringLocated(s.substring(0, s.length() - 1) + next.s, location, preprocessorError);
	}

	public StringLocated(String s, LineLocation location, String preprocessorError) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		this.s = s;
		this.location = location;
		this.preprocessorError = preprocessorError;
	}

	public StringLocated withErrorPreprocessor(String preprocessorError) {
		return new StringLocated(s, location, preprocessorError);
	}

	public StringLocated substring(int start, int end) {
		return new StringLocated(this.getString().substring(start, end), this.getLocation(),
				this.getPreprocessorError());
	}

	public StringLocated substring(int start) {
		return new StringLocated(this.getString().substring(start), this.getLocation(), this.getPreprocessorError());
	}

	public StringLocated getTrimmed() {
		if (trimmed == null) {
			this.trimmed = new StringLocated(StringUtils.trin(this.getString()), location, preprocessorError);
			trimmed.fox = this.fox;
			trimmed.trimmed = trimmed;
		}
		return trimmed;
	}

//	public StringLocated getTrimmedRight() {
//		return new StringLocated(StringUtils.trinEnding(this.getString()), location, preprocessorError);
//	}

	public StringLocated removeInnerComment() {
		final String string = s.toString();
		final String trim = string.replace('\t', ' ').trim();
		if (trim.startsWith("/'")) {
			final int idx = string.indexOf("'/");
			if (idx != -1) {
				return new StringLocated(removeSpecialInnerComment(s.substring(idx + 2, s.length())), location,
						preprocessorError);
			}
		}
		if (trim.endsWith("'/")) {
			final int idx = string.lastIndexOf("/'");
			if (idx != -1) {
				return new StringLocated(removeSpecialInnerComment(s.substring(0, idx)), location, preprocessorError);
			}
		}
		if (trim.contains("/'''") && trim.contains("'''/")) {
			return new StringLocated(removeSpecialInnerComment(s), location, preprocessorError);
		}
		return this;
	}

	private String removeSpecialInnerComment(String s) {
		if (s.contains("/'''") && s.contains("'''/")) {
			return s.replaceAll("/'''[-\\w]*'''/", "");

		}
		return s;
	}

	public String getString() {
		return s;
	}

	public LineLocation getLocation() {
		return location;
	}

	public String getPreprocessorError() {
		return preprocessorError;
	}

	public long getFoxSignature() {
		if (fox == -1) {
			fox = FoxSignature.getFoxSignature(getString());
		}
		return fox;
	}

	public TLineType getType() {
		if (type == null) {
			type = TLineType.getFromLineInternal(s);
		}
		return type;
	}

}
