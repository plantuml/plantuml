/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.StringBounder;

public class Bibliotekon {

	private final List<Cluster> allCluster = new ArrayList<>();

	private final Map<EntityImp, SvekNode> nodeMap = new LinkedHashMap<EntityImp, SvekNode>();

	private final List<SvekLine> lines0 = new ArrayList<>();
	private final List<SvekLine> lines1 = new ArrayList<>();
	private final List<SvekLine> allLines = new ArrayList<>();

	public SvekNode createNode(EntityImp ent, IEntityImage image, ColorSequence colorSequence,
			StringBounder stringBounder) {
		final SvekNode node = new SvekNode(ent, image, colorSequence, stringBounder);
		nodeMap.put(ent, node);
		// System.err.println("createNode " + ent + " " + nodeMap.size());
		return node;
	}

	public Cluster getCluster(EntityImp ent) {
		for (Cluster cl : allCluster)
			if (cl.getGroups().contains(ent))
				return cl;

		return null;
	}

	public void addLine(SvekLine line) {
		allLines.add(line);
		if (first(line)) {
			if (line.hasNoteLabelText()) {
				// lines0.add(0, line);
				for (int i = 0; i < lines0.size(); i++) {
					final SvekLine other = lines0.get(i);
					if (other.hasNoteLabelText() == false && line.sameConnections(other)) {
						lines0.add(i, line);
						return;
					}
				}
				lines0.add(line);
			} else {
				lines0.add(line);
			}
		} else {
			lines1.add(line);
		}
	}

	private static boolean first(SvekLine line) {
		final int length = line.getLength();
		if (length == 1)
			return true;

		return false;
	}

	public void addCluster(Cluster current) {
		allCluster.add(current);
	}

	public SvekNode getNode(EntityImp ent) {
		return nodeMap.get(ent);
	}

	public String getNodeUid(EntityImp ent) {
		// System.err.println("Getting for " + ent);
		final SvekNode result = getNode(ent);
		if (result != null) {
			String uid = result.getUid();
			if (result.isShielded())
				uid = uid + ":h";

			return uid;
		}
		assert result == null;
		if (ent.isGroup()) {
			for (EntityImp i : nodeMap.keySet())
				if (ent.getQuark().equals(i.getQuark()))
					return getNode(i).getUid();
			return Cluster.getSpecialPointId(ent);
		}
		throw new IllegalStateException();
	}

	public String getWarningOrError(int warningOrError) {
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<EntityImp, SvekNode> ent : nodeMap.entrySet()) {
			final SvekNode sh = ent.getValue();
			final double maxX = sh.getMinX() + sh.getWidth();
			if (maxX > warningOrError) {
				final EntityImp entity = ent.getKey();
				sb.append(entity.getCodeGetName() + " is overpassing the width limit.");
				sb.append("\n");
			}

		}
		return sb.length() == 0 ? "" : sb.toString();
	}

	public Map<String, Double> getMaxX() {
		final Map<String, Double> result = new HashMap<String, Double>();
		for (Map.Entry<EntityImp, SvekNode> ent : nodeMap.entrySet()) {
			final SvekNode sh = ent.getValue();
			final double maxX = sh.getMinX() + sh.getWidth();
			final EntityImp entity = ent.getKey();
			result.put(entity.getCodeGetName(), maxX);
		}
		return Collections.unmodifiableMap(result);
	}

	public List<SvekLine> allLines() {
		return Collections.unmodifiableList(allLines);
	}

	public List<SvekLine> lines0() {
		return Collections.unmodifiableList(lines0);
	}

	public List<SvekLine> lines1() {
		return Collections.unmodifiableList(lines1);
	}

	public List<Cluster> allCluster() {
		return Collections.unmodifiableList(allCluster);
	}

	public Collection<SvekNode> allNodes() {
		return Collections.unmodifiableCollection(nodeMap.values());
	}

	public List<SvekLine> getAllLineConnectedTo(EntityImp leaf) {
		final List<SvekLine> result = new ArrayList<>();
		for (SvekLine line : allLines)
			if (line.isLinkFromOrTo(leaf))
				result.add(line);

		return Collections.unmodifiableList(result);
	}

	public SvekLine getLine(Link link) {
		for (SvekLine line : allLines)
			if (line.isLink(link))
				return line;

		throw new IllegalArgumentException();
	}

	public EntityImp getOnlyOther(EntityImp entity) {
		for (SvekLine line : allLines) {
			final EntityImp other = line.getOther(entity);
			if (other != null)
				return other;

		}
		return null;
	}

	public EntityImp getLeaf(SvekNode node) {
		for (Map.Entry<EntityImp, SvekNode> ent : nodeMap.entrySet())
			if (ent.getValue() == node)
				return ent.getKey();

		throw new IllegalArgumentException();
	}
}
