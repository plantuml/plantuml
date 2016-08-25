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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.Colors;

public class Note extends AbstractEvent implements Event, SpecificBackcolorable {

	private final Participant p;
	private final Participant p2;

	private final Display strings;

	private final NotePosition position;
	private NoteStyle style = NoteStyle.NORMAL;

	// private Stereotype stereotype;

	private final Url url;

	public Note(Participant p, NotePosition position, Display strings) {
		this(p, null, position, strings);
	}

	public Note(Participant p, Participant p2, Display strings) {
		this(p, p2, NotePosition.OVER_SEVERAL, strings);
	}

	private Note(Participant p, Participant p2, NotePosition position, Display strings) {
		this.p = p;
		this.p2 = p2;
		this.position = position;
		if (strings != null && strings.size() > 0) {
			final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.AT_START);
			final String s = strings.asStringWithHiddenNewLine();
			this.url = urlBuilder.getUrl(s);
		} else {
			this.url = null;
		}

		if (this.url == null) {
			this.strings = strings;
		} else {
			this.strings = strings.removeUrlHiddenNewLineUrl();
		}
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public Display getStrings() {
		return strings;
	}

	public NotePosition getPosition() {
		return position;
	}

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	// public void setSpecificColorTOBEREMOVED(ColorType type, HtmlColor color) {
	// if (color != null) {
	// this.colors = colors.add(type, color);
	// }
	// }

	private Colors colors = Colors.empty();

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public boolean dealWith(Participant someone) {
		return p == someone || p2 == someone;
	}

	public Url getUrl() {
		return url;
	}

	public boolean hasUrl() {
		return url != null;
	}

	public final NoteStyle getStyle() {
		return style;
	}

	public final void setStyle(NoteStyle style) {
		this.style = style;
	}

	public ISkinParam getSkinParamBackcolored(ISkinParam skinParam) {
		// return new SkinParamBackcolored(skinParam, getColors(skinParam).getColor(ColorType.BACK));
		return colors.mute(skinParam);
	}

	public void setStereotype(Stereotype stereotype) {
		// this.stereotype = stereotype;
	}

	@Override
	public String toString() {
		return super.toString() + " " + strings;
	}

}
