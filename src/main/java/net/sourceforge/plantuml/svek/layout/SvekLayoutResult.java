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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;

public final class SvekLayoutResult {

	public static final class Point {
		public final double x;
		public final double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public static final class Bounds {
		public final double minX;
		public final double minY;
		public final double maxX;
		public final double maxY;

		public Bounds(double minX, double minY, double maxX, double maxY) {
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		public double width() {
			return maxX - minX;
		}

		public double height() {
			return maxY - minY;
		}
	}

	public static final class Path {
		public final boolean cubic;
		public final List<Point> points;

		public Path(boolean cubic, List<Point> points) {
			this.cubic = cubic;
			this.points = points == null ? null : Collections.unmodifiableList(new ArrayList<>(points));
		}
	}

	public static final class NodeGeometry {
		public final String id;
		public final Bounds bounds;
		public final List<Point> polygon;

		public NodeGeometry(String id, Bounds bounds, List<Point> polygon) {
			this.id = id;
			this.bounds = bounds;
			this.polygon = polygon == null ? null
					: Collections.unmodifiableList(new ArrayList<>(polygon));
		}
	}

	public static final class ClusterGeometry {
		public final String id;
		public final Bounds bounds;
		public final Point title;

		public ClusterGeometry(String id, Bounds bounds, Point title) {
			this.id = id;
			this.bounds = bounds;
			this.title = title;
		}
	}

	public static final class EdgeGeometry {
		public final String id;
		public final String tailId;
		public final String headId;
		public final Path path;
		public final Point mainLabel;
		public final Point tailLabel;
		public final Point headLabel;

		public EdgeGeometry(String id, String tailId, String headId, Path path, Point mainLabel, Point tailLabel,
				Point headLabel) {
			this.id = id;
			this.tailId = tailId;
			this.headId = headId;
			this.path = path;
			this.mainLabel = mainLabel;
			this.tailLabel = tailLabel;
			this.headLabel = headLabel;
		}
	}

	public final Bounds graphBounds;
	public final Direction direction;
	public final Map<String, NodeGeometry> nodes;
	public final Map<String, ClusterGeometry> clusters;
	public final Map<String, EdgeGeometry> edges;

	public SvekLayoutResult(Bounds graphBounds, Direction direction, Map<String, NodeGeometry> nodes,
			Map<String, ClusterGeometry> clusters, Map<String, EdgeGeometry> edges) {
		this.graphBounds = graphBounds;
		this.direction = direction;
		this.nodes = nodes == null ? null
				: Collections.unmodifiableMap(new LinkedHashMap<>(nodes));
		this.clusters = clusters == null ? null
				: Collections.unmodifiableMap(new LinkedHashMap<>(clusters));
		this.edges = edges == null ? null
				: Collections.unmodifiableMap(new LinkedHashMap<>(edges));
	}
}
