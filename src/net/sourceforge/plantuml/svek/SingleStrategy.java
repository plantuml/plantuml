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
 * Modified by : Arno Peterson
 *
 * 
 */
package net.sourceforge.plantuml.svek;


public enum SingleStrategy {

	SQUARRE, HLINE, VLINE;

//	private Collection<Link> generateLinks(List<ILeaf> standalones) {
//		return putInSquare(standalones);
//	}

//	private Collection<Link> putInSquare(List<ILeaf> standalones) {
//		final List<Link> result = new ArrayList<Link>();
//		final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
//		final int branch = computeBranch(standalones.size());
//		int headBranch = 0;
//		for (int i = 1; i < standalones.size(); i++) {
//			final int dist = i - headBranch;
//			final IEntity ent2 = standalones.get(i);
//			final Link link;
//			if (dist == branch) {
//				final IEntity ent1 = standalones.get(headBranch);
//				link = new Link(ent1, ent2, linkType, Display.NULL, 2);
//				headBranch = i;
//			} else {
//				final IEntity ent1 = standalones.get(i - 1);
//				link = new Link(ent1, ent2, linkType, Display.NULL, 1);
//			}
//			result.add(link);
//		}
//		return Collections.unmodifiableCollection(result);
//	}

	static int computeBranch(int size) {
		final double sqrt = Math.sqrt(size);
		final int r = (int) sqrt;
		if (r * r == size) {
			return r;
		}
		return r + 1;
	}

}