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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.Collection;
import java.util.Objects;

import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileKilled;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileBox;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileWithNoteOpale;
import net.sourceforge.plantuml.activitydiagram3.gtile.GtileWithNotes;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.url.Url;

public class InstructionSimple extends MonoSwimable implements Instruction {

	private boolean killed = false;
	private final Display label;
	private final Colors colors;
	private final LinkRendering inlinkRendering;
	private final BoxStyle boxStyle;
	private final Url url;
	private final Stereotype stereotype;

	@Override
	public boolean containsBreak() {
		return false;
	}

	public InstructionSimple(Display label, LinkRendering inlinkRendering, Swimlane swimlane, BoxStyle boxStyle,
			Url url, Colors colors, Stereotype stereotype) {
		super(swimlane);
		this.stereotype = stereotype;
		this.url = url;
		this.boxStyle = boxStyle;
		this.label = label;
		this.inlinkRendering = Objects.requireNonNull(inlinkRendering);
		this.colors = Objects.requireNonNull(colors);
	}

	// ::comment when __CORE__
	@Override
	public Gtile createGtile(ISkinParam skinParam, StringBounder stringBounder) {
		GtileBox result = GtileBox.create(stringBounder, colors.mute(skinParam), label, getSwimlaneIn(), boxStyle,
				stereotype);
		if (hasNotes()) {
			final Collection<PositionedNote> notes = getPositionedNotes();
			if (notes.size() == 0)
				throw new UnsupportedOperationException("wip");
			if (notes.size() > 0)
				return new GtileWithNotes(result, notes, skinParam);

			return new GtileWithNoteOpale(result, notes.iterator().next(), skinParam, false);

		}
		return result;
	}
	// ::done

	@Override
	public Ftile createFtile(FtileFactory factory) {
		Ftile result = factory.activity(label, getSwimlaneIn(), boxStyle, colors, stereotype);
		if (url != null) {
			result = factory.addUrl(result, url);
		}
		result = eventuallyAddNote(factory, result, result.getSwimlaneIn(), VerticalAlignment.CENTER);
		if (killed) {
			return new FtileKilled(result);
		}
		return result;
	}

	@Override
	public CommandExecutionResult add(Instruction other) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public boolean kill() {
		this.killed = true;
		return true;
	}

	@Override
	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

}
