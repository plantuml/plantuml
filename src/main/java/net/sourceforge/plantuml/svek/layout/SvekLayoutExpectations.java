/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.sourceforge.plantuml.svek.layout;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.ClusterSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.LabelPosition;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.NodeSpec;

/**
 * What the caller asked the layout engine to produce, used to check the result before it is applied.
 *
 * The specs describe the visible geometry Svek will draw, which is not always what was emitted: a node
 * spec may carry ports or shielding that the drawn body does not have.
 */
public final class SvekLayoutExpectations {

	public final Map<String, NodeSpec> nodes = new LinkedHashMap<>();
	public final Map<String, ClusterSpec> clusters = new LinkedHashMap<>();
	public final Map<String, EdgeSpec> edges = new LinkedHashMap<>();

	/** Direct members only; a packed cluster has no layout identity, so its content belongs to the parent. */
	public final Map<String, Set<String>> clusterNodeIds = new LinkedHashMap<>();
	public final Map<String, Set<String>> clusterClusterIds = new LinkedHashMap<>();
	public final Set<String> rootNodeIds = new LinkedHashSet<>();
	public final Set<String> rootClusterIds = new LinkedHashSet<>();

	/** Edges that must come back with a path; the others may be laid out without one. */
	public final Set<String> visibleEdgeIds = new LinkedHashSet<>();
	public final Map<String, Set<LabelPosition>> requiredLabels = new LinkedHashMap<>();

	private Direction direction;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
