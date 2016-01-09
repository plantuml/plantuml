/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PinLinksContinuousSet {

	private final Collection<PinLink> all = new ArrayList<PinLink>();

	public Skeleton createSkeleton() {
		final GrowingTree tree = new GrowingTree();
		final Collection<PinLink> pendings = new ArrayList<PinLink>(all);
		while (pendings.size() > 0) {
			for (Iterator<PinLink> it = pendings.iterator(); it.hasNext();) {
				final PinLink candidat = it.next();
				if (tree.canBeAdded(candidat)) {
					tree.add(candidat);
					it.remove();
				}
			}
		}
		return tree.createSkeleton();

	}

	public void add(PinLink newPinLink) {
		if (all.size() == 0) {
			all.add(newPinLink);
			return;
		}
		if (all.contains(newPinLink)) {
			throw new IllegalArgumentException("already");
		}
		for (PinLink aLink : all) {
			if (newPinLink.doesTouch(aLink)) {
				all.add(newPinLink);
				return;
			}
		}
		throw new IllegalArgumentException("not connex");
	}

	public void addAll(PinLinksContinuousSet other) {
		if (doesTouch(other) == false) {
			throw new IllegalArgumentException();
		}
		this.all.addAll(other.all);
	}

	public boolean doesTouch(PinLink other) {
		for (PinLink aLink : all) {
			if (other.doesTouch(aLink)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesTouch(PinLinksContinuousSet otherSet) {
		for (PinLink otherLink : otherSet.all) {
			if (doesTouch(otherLink)) {
				return true;
			}
		}
		return false;
	}

}
