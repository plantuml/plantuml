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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class InstructionSwitch extends WithNote implements Instruction, InstructionCollection {

	private final List<Branch> branches = new ArrayList<Branch>();
	private final ISkinParam skinParam;

	private final Instruction parent;

	private Branch current;
	private final LinkRendering topInlinkRendering;
	private LinkRendering afterEndwhile = LinkRendering.none();
	private final Display labelTest;

	private final Swimlane swimlane;

	public boolean containsBreak() {
		for (Branch branch : branches) {
			if (branch.containsBreak()) {
				return true;
			}
		}
		return false;
	}

	public InstructionSwitch(Swimlane swimlane, Instruction parent, Display labelTest, LinkRendering inlinkRendering,
			HColor color, ISkinParam skinParam) {
		this.topInlinkRendering = inlinkRendering;
		this.parent = parent;
		this.skinParam = skinParam;
		this.labelTest = labelTest;
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
		this.swimlane = swimlane;
	}

	public void add(Instruction ins) {
		current.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		for (Branch branch : branches) {
			branch.updateFtile(factory);
		}
		Ftile result = factory.createSwitch(swimlane, branches, afterEndwhile, topInlinkRendering, labelTest);
		// if (getPositionedNotes().size() > 0) {
		// result = FtileWithNoteOpale.create(result, getPositionedNotes(), skinParam, false);
		// }
		// final List<WeldingPoint> weldingPoints = new ArrayList<WeldingPoint>();
		// for (Branch branch : branches) {
		// weldingPoints.addAll(branch.getWeldingPoints());
		// }
		// if (weldingPoints.size() > 0) {
		// result = new FtileDecorateWelding(result, weldingPoints);
		// }
		return result;
	}

	final public boolean kill() {
		throw new UnsupportedOperationException();
	}

	public LinkRendering getInLinkRendering() {
		return topInlinkRendering;
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (swimlane != null) {
			result.add(swimlane);
		}
		for (Branch branch : branches) {
			result.addAll(branch.getSwimlanes());
		}
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public Instruction getLast() {
		return branches.get(branches.size() - 1).getLast();
	}

	public boolean switchCase(Display labelCase, LinkRendering nextLinkRenderer) {
		this.current = new Branch(skinParam.getCurrentStyleBuilder(), swimlane, labelCase, labelCase, null, labelCase);
		this.branches.add(this.current);
		return true;
	}

	public Instruction getParent() {
		return parent;
	}

	public void endSwitch(LinkRendering nextLinkRenderer) {
		// TODO Auto-generated method stub

	}

	// public void afterEndwhile(LinkRendering linkRenderer) {
	// this.afterEndwhile = linkRenderer;
	// }

}
