/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4749 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.skin.VisibilityModifier;

public class Member {

	private String display;
	private final boolean staticModifier;
	private final boolean abstractModifier;

	private boolean privateModifier;
	private boolean protectedModifier;
	private boolean publicModifier;
	private boolean packagePrivateModifier;

	public Member(String display) {
		final String lower = display.toLowerCase();
		this.staticModifier = lower.contains("{static}") || lower.contains("{classifier}");
		this.abstractModifier = lower.contains("{abstract}");
		final String displayClean = display.replaceAll("(?i)\\{(static|classifier|abstract)\\}", "").trim();

		if (VisibilityModifier.isVisibilityCharacter(displayClean.charAt(0))) {
			updateVisibility(display.charAt(0));
			this.display = displayClean.substring(1).trim();
		} else {
			this.display = displayClean;
		}
		assert VisibilityModifier.isVisibilityCharacter(this.display.charAt(0)) == false;

	}

	private void updateVisibility(char c) {
		if (c == '-') {
			this.privateModifier = true;
		}
		if (c == '#') {
			this.protectedModifier = true;
		}
		if (c == '+') {
			this.publicModifier = true;
		}
		if (c == '~') {
			this.packagePrivateModifier = true;
		}

	}

	public String getDisplay(boolean withVisibilityChar) {
		if (withVisibilityChar) {
			return getDisplayWithVisibilityChar();
		}
		return getDisplayWithoutVisibilityChar();
	}

	
	public String getDisplayWithoutVisibilityChar() {
		assert VisibilityModifier.isVisibilityCharacter(display.charAt(0)) == false;
		return display;
	}
	

	public String getDisplayWithVisibilityChar() {
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

	public final boolean isVisibilityModified() {
		return privateModifier || publicModifier || protectedModifier || packagePrivateModifier;
	}

	public final boolean isPrivate() {
		return privateModifier;
	}

	public final boolean isProtected() {
		return protectedModifier;
	}

	public final boolean isPublic() {
		return publicModifier;
	}

	public final boolean isPackagePrivate() {
		return packagePrivateModifier;
	}

}
