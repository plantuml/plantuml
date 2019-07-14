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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;

class Magma {

	private final CucaDiagram diagram;
	private final List<ILeaf> standalones;
	private final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();

	public Magma(CucaDiagram system, List<ILeaf> standalones) {
		this.diagram = system;
		this.standalones = standalones;
	}

	public void putInSquare() {
		final SquareLinker<ILeaf> linker = new SquareLinker<ILeaf>() {
			public void topDown(ILeaf top, ILeaf down) {
				diagram.addLink(new Link(top, down, linkType, Display.NULL, 2, diagram.getSkinParam()
						.getCurrentStyleBuilder()));
			}

			public void leftRight(ILeaf left, ILeaf right) {
				diagram.addLink(new Link(left, right, linkType, Display.NULL, 1, diagram.getSkinParam()
						.getCurrentStyleBuilder()));
			}
		};
		new SquareMaker<ILeaf>().putInSquare(standalones, linker);
	}

	public IGroup getContainer() {
		final IGroup parent = standalones.get(0).getParentContainer();
		if (parent == null) {
			return null;
		}
		return parent.getParentContainer();
	}

	public boolean isComplete() {
		final IGroup parent = getContainer();
		if (parent == null) {
			return false;
		}
		return parent.size() == standalones.size();
	}

	private int squareSize() {
		return SquareMaker.computeBranch(standalones.size());
	}

	private ILeaf getTopLeft() {
		return standalones.get(0);
	}

	private ILeaf getBottomLeft() {
		int result = SquareMaker.getBottomLeft(standalones.size());
		return standalones.get(result);
	}

	private ILeaf getTopRight() {
		final int s = squareSize();
		return standalones.get(s - 1);
	}

	@Override
	public String toString() {
		return standalones.get(0).getParentContainer() + " " + standalones.toString() + " " + isComplete();
	}

	public void linkToDown(Magma down) {
		diagram.addLink(new Link(this.getBottomLeft(), down.getTopLeft(), linkType, Display.NULL, 2, diagram
				.getSkinParam().getCurrentStyleBuilder()));

	}

	public void linkToRight(Magma right) {
		diagram.addLink(new Link(this.getTopRight(), right.getTopLeft(), linkType, Display.NULL, 1, diagram
				.getSkinParam().getCurrentStyleBuilder()));
	}

}
