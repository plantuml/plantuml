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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSet;

public class NoteOnMessage {

	private final Display notes;
	private final NotePosition notePosition;
	private final NoteStyle noteStyle;
	private final HtmlColor noteBackColor;
	private final Url urlNote;

	public NoteOnMessage(Display strings, NotePosition notePosition, NoteStyle noteStyle, String backcolor, Url url) {
		if (notePosition != NotePosition.LEFT && notePosition != NotePosition.RIGHT) {
			throw new IllegalArgumentException();
		}
		this.noteStyle = noteStyle;
		this.notes = strings;
		this.urlNote = url;
		this.notePosition = notePosition;
		this.noteBackColor = HtmlColorSet.getInstance().getColorIfValid(backcolor);
	}

	public SkinParamBackcolored getSkinParamNoteBackcolored(ISkinParam skinParam) {
		return new SkinParamBackcolored(skinParam, noteBackColor);
	}

	public boolean hasUrl() {
		if (notes != null && notes.hasUrl()) {
			return true;
		}
		// if (label != null && label.hasUrl()) {
		// return true;
		// }
		// return getUrl() != null;
		return urlNote != null;
	}

	public Display getDisplay() {
		return notes;
	}

	public NotePosition getNotePosition() {
		return notePosition;
	}

	public NoteStyle getNoteStyle() {
		return noteStyle;
	}

	public Url getUrlNote() {
		return urlNote;
	}

}
