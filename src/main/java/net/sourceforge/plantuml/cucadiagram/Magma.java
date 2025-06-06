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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;

public class Magma {

	private final CucaDiagram diagram;
	private final List<Entity> standalones;
	private final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();

	public Magma(CucaDiagram system, List<Entity> standalones) {
		this.diagram = system;
		this.standalones = standalones;
	}

	public void putInSquare() {
		final SquareLinker<Entity> linker = new SquareLinker<Entity>() {
			public void topDown(Entity top, Entity down) {
				diagram.addLink(new Link(null, diagram, diagram.getSkinParam().getCurrentStyleBuilder(),
						top, down, linkType, LinkArg.noDisplay(2)));
			}

			public void leftRight(Entity left, Entity right) {
				diagram.addLink(new Link(null, diagram, diagram.getSkinParam().getCurrentStyleBuilder(),
						left, right, linkType, LinkArg.noDisplay(1)));
			}
		};
		new SquareMaker<Entity>().putInSquare(standalones, linker);
	}

	public Entity getContainer() {
		final Entity parent = standalones.get(0).getParentContainer();
		if (parent == null)
			return null;

		return parent.getParentContainer();
	}

	public boolean isComplete() {
		final Entity parent = getContainer();
		if (parent == null)
			return false;

		return parent.countChildren() == standalones.size();
	}

	private int squareSize() {
		return SquareMaker.computeBranch(standalones.size());
	}

	private Entity getTopLeft() {
		return standalones.get(0);
	}

	private Entity getBottomLeft() {
		int result = SquareMaker.getBottomLeft(standalones.size());
		return standalones.get(result);
	}

	private Entity getTopRight() {
		final int s = squareSize();
		return standalones.get(s - 1);
	}

	@Override
	public String toString() {
		return standalones.get(0).getParentContainer() + " " + standalones.toString() + " " + isComplete();
	}

	public void linkToDown(Magma down) {
		diagram.addLink(new Link(null, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), this.getBottomLeft(),
				down.getTopLeft(), linkType, LinkArg.noDisplay(2)));

	}

	public void linkToRight(Magma right) {
		diagram.addLink(new Link(null, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), this.getTopRight(),
				right.getTopLeft(), linkType, LinkArg.noDisplay(1)));
	}

}
