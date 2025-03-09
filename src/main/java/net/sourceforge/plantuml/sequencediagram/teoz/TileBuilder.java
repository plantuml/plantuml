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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Divider;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.HSpace;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Notes;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class TileBuilder {

	public static List<Tile> buildSeveral(Iterator<Event> it, TileArguments tileArguments, Tile parent,
			YGauge currentY) {
		final List<Tile> tiles = new ArrayList<>();
		while (it.hasNext()) {
			final Event ev = it.next();
			for (Tile tile : TileBuilder.buildOne(it, tileArguments, ev, parent, currentY)) {
				tiles.add(tile);
				final Real tmpMax = tile.getMaxX();
				if (YGauge.USE_ME) {
					final YGauge gauge = tile.getYGauge();
					currentY = gauge;
				}
			}
		}
		return Collections.unmodifiableList(tiles);
	}

	public static List<Tile> buildOne(Iterator<Event> it, TileArguments tileArguments, final Event ev, Tile parent,
			YGauge currentY) {

		final StringBounder stringBounder = tileArguments.getStringBounder();
		final Rose skin = tileArguments.getSkin();
		final ISkinParam skinParam = tileArguments.getSkinParam();
		final LivingSpaces livingSpaces = tileArguments.getLivingSpaces();

		final List<Tile> tiles = new ArrayList<>();
		// System.err.println("TileBuilder::buildOne " + ev);
		if (ev instanceof Message) {
			final Message msg = (Message) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(msg.getParticipant1());
			final LivingSpace livingSpace2 = livingSpaces.get(msg.getParticipant2());
			boolean reverse = false;
			Tile result = null;
			if (msg.isSelfMessage()) {
				result = new CommunicationTileSelf(tileArguments.getStringBounder(), livingSpace1, msg, skin, skinParam,
						livingSpaces, currentY);
			} else {
				result = new CommunicationTile(tileArguments.getStringBounder(), livingSpaces, msg, skin, skinParam,
						currentY);
				reverse = ((CommunicationTile) result).isReverse(stringBounder);
			}
			for (Note noteOnMessage : msg.getNoteOnMessages()) {
				final NotePosition notePosition = noteOnMessage.getPosition();
				if (notePosition == NotePosition.LEFT && msg.isSelfMessage())
					result = new CommunicationTileSelfNoteLeft((CommunicationTileSelf) result, msg, skin, skinParam,
							noteOnMessage, currentY);
				else if (notePosition == NotePosition.LEFT)
					result = new CommunicationTileNoteLeft(result, msg, skin, skinParam,
							reverse ? livingSpace2 : livingSpace1, noteOnMessage, currentY);
				else if (notePosition == NotePosition.RIGHT && msg.isSelfMessage())
					result = new CommunicationTileSelfNoteRight((CommunicationTileSelf) result, msg, skin, skinParam,
							noteOnMessage, currentY);
				else if (notePosition == NotePosition.RIGHT)
					result = new CommunicationTileNoteRight(result, msg, skin, skinParam,
							reverse ? livingSpace1 : livingSpace2, noteOnMessage, currentY);
				else if (notePosition == NotePosition.BOTTOM)
					result = new CommunicationTileNoteBottom(result, msg, skin, skinParam, noteOnMessage, currentY);
				else if (notePosition == NotePosition.TOP)
					result = new CommunicationTileNoteTop(result, msg, skin, skinParam, noteOnMessage, currentY);

			}
			tiles.add(result);
		} else if (ev instanceof MessageExo) {
			final MessageExo exo = (MessageExo) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(exo.getParticipant());
			Tile result = null;
			result = new CommunicationExoTile(livingSpace1, exo, skin, skinParam, tileArguments, currentY);
			for (Note noteOnMessage : exo.getNoteOnMessages()) {
				final NotePosition notePosition = exo.getNoteOnMessages().get(0).getPosition();
				if (notePosition == NotePosition.LEFT)
					result = new CommunicationTileNoteLeft(result, exo, skin, skinParam, livingSpace1, noteOnMessage,
							currentY);
				else if (notePosition == NotePosition.RIGHT)
					result = new CommunicationTileNoteRight(result, exo, skin, skinParam, livingSpace1, noteOnMessage,
							currentY);
			}
			tiles.add(result);
		} else if (ev instanceof Note) {
			final Note note = (Note) ev;
			LivingSpace livingSpace1 = livingSpaces.get(note.getParticipant());
			LivingSpace livingSpace2 = note.getParticipant2() == null ? null : livingSpaces.get(note.getParticipant2());
			if (livingSpace1 == null && livingSpace2 == null) {
				livingSpace1 = tileArguments.getFirstLivingSpace();
				livingSpace2 = tileArguments.getLastLivingSpace();
			}
			tiles.add(new NoteTile(tileArguments.getStringBounder(), livingSpace1, livingSpace2, note, skin, skinParam,
					currentY));
		} else if (ev instanceof Notes) {
			final Notes notes = (Notes) ev;
			tiles.add(new NotesTile(tileArguments.getStringBounder(), livingSpaces, notes, skin, skinParam, currentY));
		} else if (ev instanceof Divider) {
			final Divider divider = (Divider) ev;
			tiles.add(new DividerTile(divider, tileArguments, currentY));
		} else if (ev instanceof GroupingStart) {
			final GroupingStart start = (GroupingStart) ev;
			final GroupingTile groupingTile = new GroupingTile(it, start,
					tileArguments.withBackColorGeneral(start.getBackColorElement(), start.getBackColorGeneral()),
					tileArguments, currentY);
			if (YGauge.USE_ME == false)
				tiles.add(new EmptyTile(4, groupingTile, currentY));
			tiles.add(groupingTile);
			if (YGauge.USE_ME == false)
				tiles.add(new EmptyTile(4, groupingTile, currentY));

		} else if (ev instanceof GroupingLeaf && ((GroupingLeaf) ev).getType() == GroupingType.ELSE) {
			final GroupingLeaf anElse = (GroupingLeaf) ev;
			tiles.add(new ElseTile(anElse, skin, skinParam, parent, currentY));
		} else if (ev instanceof Reference) {
			final Reference ref = (Reference) ev;
			tiles.add(new ReferenceTile(ref, tileArguments.withBackColor(ref), currentY));
		} else if (ev instanceof Delay) {
			final Delay delay = (Delay) ev;
			tiles.add(new DelayTile(delay, tileArguments, currentY));
		} else if (ev instanceof HSpace) {
			final HSpace hspace = (HSpace) ev;
			tiles.add(new HSpaceTile(hspace, tileArguments, currentY));
		} else if (ev instanceof LifeEvent) {
			final LifeEvent lifeEvent = (LifeEvent) ev;
			final LivingSpace livingSpace = livingSpaces.get(lifeEvent.getParticipant());
			tiles.add(new LifeEventTile(lifeEvent, tileArguments, livingSpace, skin, skinParam, currentY));
		} else if (ev instanceof Newpage) {
			final Newpage newpage = (Newpage) ev;
			tiles.add(new NewpageTile(newpage, tileArguments, currentY));
		} else {
			System.err.println("TileBuilder::Ignoring " + ev.getClass());
		}
		return tiles;
	}
}
