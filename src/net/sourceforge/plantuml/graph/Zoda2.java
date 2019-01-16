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
package net.sourceforge.plantuml.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Zoda2 {

	private final Map<ANode, Heap> heaps = new LinkedHashMap<ANode, Heap>();

	public ANode getNode(String code) {
		for (ANode n : heaps.keySet()) {
			if (n.getCode().equals(code)) {
				return n;
			}
		}
		return null;
	}

	public ANode createAloneNode(String code) {
		if (getNode(code) != null) {
			throw new IllegalArgumentException();
		}
		final Heap h = new Heap();
		final ANode n = h.addNode(code);
		heaps.put(n, h);
		return n;
	}

	public List<? extends ANode> getNodes() {
		return Collections.unmodifiableList(new ArrayList<ANode>(heaps.keySet()));
	}

	public Set<Heap> getHeaps() {
		return new HashSet<Heap>(heaps.values());
	}

	public void addLink(String link, int diffHeight, Object userData) {
		final LinkString l;
		try {
			l = new LinkString(link);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		final ANode n1 = getNode(l.getNode1());
		final ANode n2 = getNode(l.getNode2());
		final Heap h1 = n1 == null ? null : heaps.get(n1);
		final Heap h2 = n2 == null ? null : heaps.get(n2);
		assert h1 == null || h1.isEmpty() == false;
		assert h2 == null || h2.isEmpty() == false;
		if (h1 == null && h2 == null) {
			final Heap h = new Heap();
			h.addLink(link, diffHeight, userData);
			recordHeap(h);
		} else if (h1 == h2) {
			assert h1 != null && h2 != null;
			h1.addLink(link, diffHeight, userData);
		} else if (h1 == null) {
			h2.addLink(link, diffHeight, userData);
			recordHeap(h2);
		} else if (h2 == null) {
			h1.addLink(link, diffHeight, userData);
			recordHeap(h1);
		} else {
			assert h1 != null && h2 != null;
			assert h1.getNodes().contains(n1);
			h1.importing(n1, n2, h2, diffHeight, userData);
			recordHeap(h1);
			assert heapMerged(h1, h2);
		}
	}

	private boolean heapMerged(final Heap destination, final Heap source) {
		for (ANode n : source.getNodes()) {
			assert heaps.get(n) == destination;
		}
		return true;
	}

	private void recordHeap(final Heap h) {
		for (ANode n : h.getNodes()) {
			heaps.put((ANodeImpl) n, h);
		}
	}

}
