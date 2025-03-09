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
 */
package net.sourceforge.plantuml.cheneer;

import java.util.Stack;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ChenEerDiagram extends AbstractEntityDiagram {

	public ChenEerDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessingArtifact) {
		super(source, UmlDiagramType.CHEN_EER, previous, preprocessingArtifact);
	}

	private final Stack<Entity> ownerStack = new Stack<Entity>();

	/**
	 * Pushes the owner of the following attributes.
	 *
	 * @see #peekOwner()
	 * @param group the entity that owns the following attributes
	 */
	public void pushOwner(Entity group) {
		ownerStack.push(group);
	}

	/**
	 * Pops an attribute owner from the stack. See also {@link #peekOwner()}.
	 *
	 * @see #peekOwner()
	 * @return true if an owner was popped, false if the stack was empty
	 */
	public boolean popOwner() {
		if (ownerStack.isEmpty()) {
			return false;
		}
		ownerStack.pop();
		return true;
	}

	/**
	 * Returns the owner of the current attribute.
	 *
	 * <p>
	 * This is used to link attributes based on their lexical position (how they
	 * appear in sources) without nesting the entities (like how packages are
	 * done). It is for this reason that we can't use CucaDiagram.getCurrentGroup,
	 * as that method nests the entities.
	 *
	 * @return the owner of the current attribute, or null if there is no owner
	 */
	public Entity peekOwner() {
		if (ownerStack.isEmpty()) {
			return null;
		}
		return ownerStack.peek();
	}

}
