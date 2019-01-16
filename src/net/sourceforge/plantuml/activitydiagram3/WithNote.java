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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class WithNote {

	private final Collection<PositionedNote> notes = new ArrayList<PositionedNote>();

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		this.notes.add(new PositionedNote(note, position, type, colors, swimlaneNote));
		return true;
	}

	final protected Ftile eventuallyAddNote(FtileFactory factory, Ftile ftile, Swimlane swimlane) {
		if (notes.size() == 0) {
			return ftile;
		}
		return factory.addNote(ftile, swimlane, notes);
	}

	public Collection<PositionedNote> getPositionedNotes() {
		return Collections.unmodifiableCollection(notes);
	}

	public boolean hasNotes() {
		return notes.size() > 0;
	}

}
