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
 * Revision $Revision: 4237 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.List;

import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class Note implements Event, SpecificBackcolorable {

	private final Participant p;
	private final Participant p2;

	private final List<String> strings;

	private final NotePosition position;

	public Note(Participant p, NotePosition position, List<String> strings) {
		this.p = p;
		this.p2 = null;
		this.position = position;
		this.strings = strings;
	}

	public Note(Participant p, Participant p2, List<String> strings) {
		this.p = p;
		this.p2 = p2;
		this.position = NotePosition.OVER_SEVERAL;
		this.strings = strings;
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public List<String> getStrings() {
		return strings;
	}

	public NotePosition getPosition() {
		return position;
	}

	private HtmlColor specificBackcolor;
	
	public HtmlColor getSpecificBackColor() {
		return specificBackcolor;
	}

	public void setSpecificBackcolor(String s) {
		this.specificBackcolor = HtmlColor.getColorIfValid(s);
	}

}
