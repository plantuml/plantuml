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
package net.sourceforge.plantuml.sequencediagram;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.stereo.Stereotype;

public class ParticipantEnglober {

	final private ParticipantEnglober parent;
	final private Display title;
	final private HColor boxColor;
	final private Stereotype stereotype;

	@Override
	public String toString() {
		return title.toString();
	}

	public static ParticipantEnglober build(Display title, HColor boxColor, Stereotype stereotype) {
		return new ParticipantEnglober(null, title, boxColor, stereotype);
	}

	public ParticipantEnglober newChild(Display title, HColor boxColor, Stereotype stereotype) {
		return new ParticipantEnglober(this, title, boxColor, stereotype);
	}

	private ParticipantEnglober(ParticipantEnglober parent, Display title, HColor boxColor, Stereotype stereotype) {
		this.parent = parent;
		this.title = title;
		this.boxColor = boxColor;
		this.stereotype = stereotype;
	}

	public final Display getTitle() {
		return title;
	}

	public final HColor getBoxColor() {
		return boxColor;
	}

	public final Stereotype getStereotype() {
		return stereotype;
	}

	public final ParticipantEnglober getParent() {
		return parent;
	}

	public final List<ParticipantEnglober> getGenealogy() {
		final LinkedList<ParticipantEnglober> result = new LinkedList<>();
		ParticipantEnglober current = this;
		while (current != null) {
			result.addFirst(current);
			current = current.getParent();
		}
		return Collections.unmodifiableList(result);
	}

}
