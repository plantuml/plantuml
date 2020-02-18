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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class Member {

	private final String display;
	private final boolean staticModifier;
	private final boolean abstractModifier;
	private final Url url;
	private final boolean hasUrl;

	private final VisibilityModifier visibilityModifier;

	@Override
	public String toString() {
		return super.toString() + " " + display;
	}

	public Member(String tmpDisplay, boolean isMethod, boolean manageModifier) {
		tmpDisplay = tmpDisplay.replaceAll("(?i)\\{(method|field)\\}\\s*", "");
		if (manageModifier) {
			final Pattern2 finalUrl = MyPattern.cmpile("^(.*?)(?:\\[(" + UrlBuilder.getRegexp() + ")\\])?$");
			final Matcher2 matcher = finalUrl.matcher(tmpDisplay);
			if (matcher.matches() == false) {
				throw new IllegalStateException();
			}
			tmpDisplay = matcher.group(1);
			final String urlString = matcher.group(2);
			if (urlString == null) {
				this.url = null;
			} else {
				this.url = new UrlBuilder(null, ModeUrl.STRICT).getUrl(urlString);
			}
		} else {
			this.url = null;
		}
		this.hasUrl = this.url != null;
		final String lower = StringUtils.goLowerCase(tmpDisplay);

		if (manageModifier) {
			this.staticModifier = lower.contains("{static}") || lower.contains("{classifier}");
			this.abstractModifier = lower.contains("{abstract}");
			String displayClean = tmpDisplay.replaceAll("(?i)\\{(static|classifier|abstract)\\}\\s*", "").trim();
			if (displayClean.length() == 0) {
				displayClean = " ";
			}

			if (VisibilityModifier.isVisibilityCharacter(displayClean)) {
				visibilityModifier = VisibilityModifier.getVisibilityModifier(displayClean, isMethod == false);
				this.display = StringUtils.trin(Guillemet.GUILLEMET.manageGuillemet(displayClean.substring(1)));
			} else {
				this.display = Guillemet.GUILLEMET.manageGuillemet(displayClean);
				visibilityModifier = null;
			}
		} else {
			this.staticModifier = false;
			this.visibilityModifier = null;
			this.abstractModifier = false;
			tmpDisplay = StringUtils.trin(tmpDisplay);
			this.display = tmpDisplay.length() == 0 ? " " : Guillemet.GUILLEMET.manageGuillemet(StringUtils
					.trin(tmpDisplay));
		}
	}

	public String getDisplay(boolean withVisibilityChar) {
		if (withVisibilityChar) {
			return getDisplayWithVisibilityChar();
		}
		return getDisplayWithoutVisibilityChar();
	}

	private String getDisplayWithoutVisibilityChar() {
		// assert display.length() == 0 || VisibilityModifier.isVisibilityCharacter(display.charAt(0)) == false;
		return display;
	}

	private String getDisplayWithVisibilityChar() {
		if (isPrivate()) {
			return "-" + display;
		}
		if (isPublic()) {
			return "+" + display;
		}
		if (isPackagePrivate()) {
			return "~" + display;
		}
		if (isProtected()) {
			return "#" + display;
		}
		if (isIEMandatory()) {
			return "*" + display;
		}
		return display;
	}

	@Override
	public boolean equals(Object obj) {
		final Member other = (Member) obj;
		return this.display.equals(other.display);
	}

	@Override
	public int hashCode() {
		return display.hashCode();
	}

	public final boolean isStatic() {
		return staticModifier;
	}

	public final boolean isAbstract() {
		return abstractModifier;
	}

	private boolean isPrivate() {
		return visibilityModifier == VisibilityModifier.PRIVATE_FIELD
				|| visibilityModifier == VisibilityModifier.PRIVATE_METHOD;
	}

	private boolean isProtected() {
		return visibilityModifier == VisibilityModifier.PROTECTED_FIELD
				|| visibilityModifier == VisibilityModifier.PROTECTED_METHOD;
	}

	private boolean isPublic() {
		return visibilityModifier == VisibilityModifier.PUBLIC_FIELD
				|| visibilityModifier == VisibilityModifier.PUBLIC_METHOD;
	}

	private boolean isPackagePrivate() {
		return visibilityModifier == VisibilityModifier.PACKAGE_PRIVATE_FIELD
				|| visibilityModifier == VisibilityModifier.PACKAGE_PRIVATE_METHOD;
	}

	private boolean isIEMandatory() {
		return visibilityModifier == VisibilityModifier.IE_MANDATORY;
	}

	public final VisibilityModifier getVisibilityModifier() {
		return visibilityModifier;
	}

	public final Url getUrl() {
		return url;
	}

	public boolean hasUrl() {
		return hasUrl;
	}

	public static boolean isMethod(String s) {
		// s = UrlBuilder.purgeUrl(s);
		if (s.contains("{method}")) {
			return true;
		}
		if (s.contains("{field}")) {
			return false;
		}
		return s.contains("(") || s.contains(")");
	}
}
