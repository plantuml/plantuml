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
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Divider;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.Skin;

public class TileBuilder {

	public static List<Tile> buildSeveral(Iterator<Event> it, TileArguments tileArguments, Tile parent) {
		final List<Tile> tiles = new ArrayList<Tile>();
		while (it.hasNext()) {
			final Event ev = it.next();
			final Tile tile = TileBuilder.buildOne(it, tileArguments, ev, parent);
			if (tile != null) {
				tiles.add(tile);
				tileArguments.getOmega().ensureBiggerThan(tile.getMaxX(tileArguments.getStringBounder()));
			}
		}
		return Collections.unmodifiableList(tiles);
	}

	public static Tile buildOne(Iterator<Event> it, TileArguments tileArguments, final Event ev, Tile parent) {

		final StringBounder stringBounder = tileArguments.getStringBounder();
		final Skin skin = tileArguments.getSkin();
		final ISkinParam skinParam = tileArguments.getSkinParam();
		final LivingSpaces livingSpaces = tileArguments.getLivingSpaces();

		Tile tile = null;
		// System.err.println("TileBuilder::buildOne " + ev);
		if (ev instanceof Message) {
			final Message msg = (Message) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(msg.getParticipant1());
			final LivingSpace livingSpace2 = livingSpaces.get(msg.getParticipant2());
			boolean reverse = false;
			if (msg.isSelfMessage()) {
				tile = new CommunicationTileSelf(livingSpace1, msg, skin, skinParam, livingSpaces);
			} else {
				// System.err.println("msg=" + msg);
				tile = new CommunicationTile(livingSpace1, livingSpace2, msg, skin, skinParam);
				reverse = ((CommunicationTile) tile).isReverse(stringBounder);
			}
			if (msg.getNote() != null) {
				final NotePosition notePosition = msg.getNotePosition();
				if (notePosition == NotePosition.LEFT) {
					tile = new CommunicationTileNoteLeft((TileWithUpdateStairs) tile, msg, skin, skinParam,
							reverse ? livingSpace2 : livingSpace1);
				} else if (notePosition == NotePosition.RIGHT && msg.isSelfMessage()) {
					tile = new CommunicationTileSelfNoteRight((CommunicationTileSelf) tile, msg, skin, skinParam);
				} else if (notePosition == NotePosition.RIGHT) {
					tile = new CommunicationTileNoteRight((TileWithUpdateStairs) tile, msg, skin, skinParam,
							reverse ? livingSpace1 : livingSpace2);
				}
			}
		} else if (ev instanceof Note) {
			final Note note = (Note) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(note.getParticipant());
			final LivingSpace livingSpace2 = note.getParticipant2() == null ? null : livingSpaces.get(note
					.getParticipant2());
			tile = new NoteTile(livingSpace1, livingSpace2, note, skin, skinParam);
		} else if (ev instanceof Divider) {
			final Divider divider = (Divider) ev;
			tile = new DividerTile(divider, skin, skinParam, tileArguments.getOrigin(), tileArguments.getOmega());
		} else if (ev instanceof MessageExo) {
			final MessageExo exo = (MessageExo) ev;
			final LivingSpace livingSpace1 = livingSpaces.get(exo.getParticipant());
			tile = new CommunicationExoTile(livingSpace1, exo, skin, skinParam, tileArguments.getOrigin(),
					tileArguments.getOmega());
		} else if (ev instanceof GroupingStart) {
			final GroupingStart start = (GroupingStart) ev;
			tile = new GroupingTile(it, start, tileArguments);
			// tile = TileUtils.withMargin(tile, 10, 10, 10, 10);
		} else if (ev instanceof GroupingLeaf && ((GroupingLeaf) ev).getType() == GroupingType.ELSE) {
			final GroupingLeaf anElse = (GroupingLeaf) ev;
			tile = new ElseTile(anElse, skin, skinParam, parent);
		} else if (ev instanceof Reference) {
			final Reference ref = (Reference) ev;
			tile = new ReferenceTile(ref, tileArguments);
		} else if (ev instanceof Delay) {
			final Delay delay = (Delay) ev;
			tile = new DelayTile(delay, tileArguments);
		} else if (ev instanceof LifeEvent) {
			final LifeEvent lifeEvent = (LifeEvent) ev;
			final LivingSpace livingSpace = livingSpaces.get(lifeEvent.getParticipant());
			tile = new LifeEventTile(lifeEvent, tileArguments, livingSpace, skin, skinParam);
		} else {
			System.err.println("TileBuilder::Ignoring " + ev.getClass());
		}
		return tile;
	}

}
