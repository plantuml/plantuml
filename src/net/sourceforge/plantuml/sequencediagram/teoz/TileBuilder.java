/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
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
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Notes;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.Skin;

public class TileBuilder {

	public static List<Tile> buildSeveral(Iterator<Event> it, TileArguments tileArguments, Tile parent) {
		final List<Tile> tiles = new ArrayList<Tile>();
		while (it.hasNext()) {
			final Event ev = it.next();
			for (Tile tile : TileBuilder.buildOne(it, tileArguments, ev, parent)) {
				tiles.add(tile);
				final Real tmpMax = tile.getMaxX(tileArguments.getStringBounder());
			}
		}
		return Collections.unmodifiableList(tiles);
	}

	public static List<Tile> buildOne(Iterator<Event> it, TileArguments tileArguments, final Event ev, Tile parent) {

		final StringBounder stringBounder = tileArguments.getStringBounder();
		final Skin skin = tileArguments.getSkin();
		final ISkinParam skinParam = tileArguments.getSkinParam();
		final LivingSpaces livingSpaces = tileArguments.getLivingSpaces();

		final List<Tile> tiles = new ArrayList<Tile>();
		// System.err.println("TileBuilder::buildOne " + ev);
		if (ev instanceof Message) {
			final Message msg = (Message) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(msg.getParticipant1());
			final LivingSpace livingSpace2 = livingSpaces.get(msg.getParticipant2());
			boolean reverse = false;
			Tile result = null;
			if (msg.isSelfMessage()) {
				result = new CommunicationTileSelf(livingSpace1, msg, skin, skinParam, livingSpaces);
			} else {
				// System.err.println("msg=" + msg);
				result = new CommunicationTile(livingSpace1, livingSpace2, msg, skin, skinParam);
				reverse = ((CommunicationTile) result).isReverse(stringBounder);
			}
			if (msg.getNote() != null) {
				final NotePosition notePosition = msg.getNotePosition();
				if (notePosition == NotePosition.LEFT) {
					result = new CommunicationTileNoteLeft((TileWithUpdateStairs) result, msg, skin, skinParam,
							reverse ? livingSpace2 : livingSpace1);
				} else if (notePosition == NotePosition.RIGHT && msg.isSelfMessage()) {
					result = new CommunicationTileSelfNoteRight((CommunicationTileSelf) result, msg, skin, skinParam);
				} else if (notePosition == NotePosition.RIGHT) {
					result = new CommunicationTileNoteRight((TileWithUpdateStairs) result, msg, skin, skinParam,
							reverse ? livingSpace1 : livingSpace2);
				}
			}
			tiles.add(result);
		} else if (ev instanceof MessageExo) {
			final MessageExo exo = (MessageExo) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(exo.getParticipant());
			Tile result = null;
			result = new CommunicationExoTile(livingSpace1, exo, skin, skinParam, tileArguments);
			if (exo.getNote() != null) {
				final NotePosition notePosition = exo.getNotePosition();
				if (notePosition == NotePosition.LEFT) {
					result = new CommunicationTileNoteLeft((TileWithUpdateStairs) result, exo, skin, skinParam,
							livingSpace1);
				} else if (notePosition == NotePosition.RIGHT) {
					result = new CommunicationTileNoteRight((TileWithUpdateStairs) result, exo, skin, skinParam,
							livingSpace1);
				}
			}
			tiles.add(result);
		} else if (ev instanceof Note) {
			final Note note = (Note) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(note.getParticipant());
			final LivingSpace livingSpace2 = note.getParticipant2() == null ? null : livingSpaces.get(note
					.getParticipant2());
			tiles.add(new NoteTile(livingSpace1, livingSpace2, note, skin, skinParam));
		} else if (ev instanceof Notes) {
			final Notes notes = (Notes) ev;
			tiles.add(new NotesTile(livingSpaces, notes, skin, skinParam));
		} else if (ev instanceof Divider) {
			final Divider divider = (Divider) ev;
			tiles.add(new DividerTile(divider, tileArguments));
		} else if (ev instanceof GroupingStart) {
			final GroupingStart start = (GroupingStart) ev;
			tiles.add(new EmptyTile(4, tileArguments));
			tiles.add(new GroupingTile(it, start, tileArguments.withBackColorGeneral(start.getBackColorElement(),
					start.getBackColorGeneral()), tileArguments));
			tiles.add(new EmptyTile(4, tileArguments));
			// tiles.add(TileUtils.withMargin(tile, 0, 0, 4, 4);
		} else if (ev instanceof GroupingLeaf && ((GroupingLeaf) ev).getType() == GroupingType.ELSE) {
			final GroupingLeaf anElse = (GroupingLeaf) ev;
			tiles.add(new ElseTile(anElse, skin, skinParam, parent));
		} else if (ev instanceof Reference) {
			final Reference ref = (Reference) ev;
			tiles.add(new ReferenceTile(ref, tileArguments.withBackColor(ref)));
		} else if (ev instanceof Delay) {
			final Delay delay = (Delay) ev;
			tiles.add(new DelayTile(delay, tileArguments));
		} else if (ev instanceof HSpace) {
			final HSpace hspace = (HSpace) ev;
			tiles.add(new HSpaceTile(hspace, tileArguments));
		} else if (ev instanceof LifeEvent) {
			final LifeEvent lifeEvent = (LifeEvent) ev;
			final LivingSpace livingSpace = livingSpaces.get(lifeEvent.getParticipant());
			tiles.add(new LifeEventTile(lifeEvent, tileArguments, livingSpace, skin, skinParam));
		} else {
			System.err.println("TileBuilder::Ignoring " + ev.getClass());
		}
		return tiles;
	}
}
