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

import java.util.Map;
import java.util.Stack;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ChenEerDiagram extends AbstractEntityDiagram {

	public ChenEerDiagram(UmlSource source, Map<String, String> skinParam) {
		super(source, UmlDiagramType.CHEN_EER, skinParam);
	}

	private final Stack<Entity> ownerStack = new Stack<Entity>();

	public void pushOwner(Entity group) {
		ownerStack.push(group);
	}

	public boolean popOwner() {
		if (ownerStack.isEmpty()) {
			return false;
		}
		ownerStack.pop();
		return true;
	}

	public Entity peekOwner() {
		if (ownerStack.isEmpty()) {
			return null;
		}
		return ownerStack.peek();
	}

}
