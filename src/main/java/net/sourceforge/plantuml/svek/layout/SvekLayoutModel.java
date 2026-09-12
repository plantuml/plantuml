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
import java.util.List;

/**
 * What Svek asks a layout engine to arrange, expressed without any engine type.
 *
 * Sizes are PlantUML units, the same ones Svek draws with; an engine that works in inches divides
 * by 72. Identifiers are the Svek IDs, so the result can be matched back element by element.
 */
public final class SvekLayoutModel {

	public enum Direction { TOP_TO_BOTTOM, LEFT_TO_RIGHT }
	public enum Routing { SPLINE, POLYLINE, ORTHO }
	public enum Shape { RECTANGLE, ROUNDED_RECTANGLE, ELLIPSE, CIRCLE, DIAMOND, OCTAGON, HEXAGON, POINT }
	public enum Rank { SAME, MIN, MAX, SOURCE, SINK }
	public enum Alignment { LEFT, CENTER, RIGHT }
	public enum LabelPosition { MAIN, TAIL, HEAD }

	public static final class GraphSpec {
		public final Direction direction;
		public final Routing routing;

		/** Minimum gap between two nodes of the same rank. */
		public final double nodeSep;

		/** Minimum gap between two consecutive ranks. */
		public final double rankSep;

		public GraphSpec(Direction direction, Routing routing, double nodeSep, double rankSep) {
			this.direction = direction;
			this.routing = routing;
			this.nodeSep = nodeSep;
			this.rankSep = rankSep;
		}
	}

	/**
	 * A rectangle inside a node that edges can attach to, such as a port or a shielded body.
	 * Its position is relative to the node origin.
	 */
	public static final class CellSpec {
		public final String id;
		public final double x;
		public final double y;
		public final double width;
		public final double height;

		public CellSpec(String id, double x, double y, double width, double height) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	public static final class NodeSpec {
		public final String id;
		public final double width;
		public final double height;
		public final Shape shape;

		/** The cell holding the drawn body, when the node is larger than what Svek paints. */
		public final String bodyCellId;

		public final List<CellSpec> cells;

		/** A node that only shapes the layout and is never drawn, such as a cluster centre point. */
		public final boolean layoutOnly;

		public NodeSpec(String id, double width, double height, Shape shape, String bodyCellId,
				List<CellSpec> cells) {
			this(id, width, height, shape, bodyCellId, cells, false);
		}

		public NodeSpec(String id, double width, double height, Shape shape, String bodyCellId,
				List<CellSpec> cells, boolean layoutOnly) {
			this.id = id;
			this.width = width;
			this.height = height;
			this.shape = shape;
			this.bodyCellId = bodyCellId;
			this.cells = Collections.unmodifiableList(new ArrayList<>(cells));
			this.layoutOnly = layoutOnly;
		}
	}

	public static final class ClusterSpec {
		public final String id;
		public final double titleWidth;
		public final double titleHeight;
		public final double horizontalMargin;
		public final double verticalMargin;

		/** Free space kept under the title so no child is drawn over it. */
		public final double contentTopPadding;

		/** A wrapper that only creates clearance and is never drawn. */
		public final boolean layoutOnly;

		public final Alignment titleAlignment;

		private ClusterSpec(ClusterSpecBuilder builder) {
			this.id = builder.id;
			this.titleWidth = builder.titleWidth;
			this.titleHeight = builder.titleHeight;
			this.horizontalMargin = builder.horizontalMargin;
			this.verticalMargin = builder.verticalMargin;
			this.contentTopPadding = builder.contentTopPadding;
			this.layoutOnly = builder.layoutOnly;
			this.titleAlignment = builder.titleAlignment;
		}

		public static ClusterSpecBuilder builder(String id) {
			return new ClusterSpecBuilder(id);
		}
	}

	public static final class ClusterSpecBuilder {
		private final String id;
		private double titleWidth;
		private double titleHeight;
		private double horizontalMargin = 10;
		private double verticalMargin = 10;
		private double contentTopPadding;
		private boolean layoutOnly;
		private Alignment titleAlignment = Alignment.CENTER;

		private ClusterSpecBuilder(String id) {
			this.id = id;
		}

		public ClusterSpecBuilder title(double width, double height) {
			return title(width, height, Alignment.CENTER);
		}

		public ClusterSpecBuilder title(double width, double height, Alignment alignment) {
			this.titleWidth = width;
			this.titleHeight = height;
			this.titleAlignment = alignment;
			return this;
		}

		public ClusterSpecBuilder margins(double horizontal, double vertical) {
			this.horizontalMargin = horizontal;
			this.verticalMargin = vertical;
			return this;
		}

		public ClusterSpecBuilder contentTopPadding(double contentTopPadding) {
			this.contentTopPadding = contentTopPadding;
			return this;
		}

		public ClusterSpecBuilder layoutOnly(boolean layoutOnly) {
			this.layoutOnly = layoutOnly;
			return this;
		}

		public ClusterSpec build() {
			return new ClusterSpec(this);
		}
	}

	public static final class RankSpec {
		public final Rank rank;
		public final List<String> nodeIds;

		public RankSpec(Rank rank, List<String> nodeIds) {
			this.rank = rank;
			this.nodeIds = Collections.unmodifiableList(new ArrayList<>(nodeIds));
		}
	}

	public static final class LabelSpec {
		public final LabelPosition position;
		public final double width;
		public final double height;

		public LabelSpec(LabelPosition position, double width, double height) {
			this.position = position;
			this.width = width;
			this.height = height;
		}
	}

	public static final class EdgeSpec {
		public final String id;
		public final String tailId;
		public final String headId;

		/** Cell the edge attaches to, or null to attach to the node itself. */
		public final String tailCellId;
		public final String headCellId;

		/** Rank distance between the endpoints; zero asks for the same rank. */
		public final int minlen;

		/** An invisible edge still shapes the layout, but needs no path back. */
		public final boolean visible;

		/** False lets the engine place the endpoints freely instead of ranking them. */
		public final boolean constraint;

		/** Group name for edges that should leave their tail, or enter their head, together. */
		public final String sameTail;
		public final String sameHead;

		public final double weight;

		/** An edge that only shapes the layout and is never drawn. */
		public final boolean layoutOnly;

		public final List<LabelSpec> labels;

		private EdgeSpec(EdgeSpecBuilder builder) {
			this.id = builder.id;
			this.tailId = builder.tailId;
			this.headId = builder.headId;
			this.tailCellId = builder.tailCellId;
			this.headCellId = builder.headCellId;
			this.minlen = builder.minlen;
			this.visible = builder.visible;
			this.constraint = builder.constraint;
			this.sameTail = builder.sameTail;
			this.sameHead = builder.sameHead;
			this.weight = builder.weight;
			this.layoutOnly = builder.layoutOnly;
			this.labels = Collections.unmodifiableList(new ArrayList<>(builder.labels));
		}

		public static EdgeSpecBuilder builder(String id, String tailId, String headId) {
			return new EdgeSpecBuilder(id, tailId, headId);
		}
	}

	public static final class EdgeSpecBuilder {
		private final String id;
		private final String tailId;
		private final String headId;
		private String tailCellId;
		private String headCellId;
		private int minlen = 1;
		private boolean visible = true;
		private boolean constraint = true;
		private String sameTail;
		private String sameHead;
		private double weight = 1;
		private boolean layoutOnly;
		private List<LabelSpec> labels = Collections.emptyList();

		private EdgeSpecBuilder(String id, String tailId, String headId) {
			this.id = id;
			this.tailId = tailId;
			this.headId = headId;
		}

		public EdgeSpecBuilder cells(String tailCellId, String headCellId) {
			this.tailCellId = tailCellId;
			this.headCellId = headCellId;
			return this;
		}

		public EdgeSpecBuilder minlen(int minlen) {
			this.minlen = minlen;
			return this;
		}

		public EdgeSpecBuilder visible(boolean visible) {
			this.visible = visible;
			return this;
		}

		public EdgeSpecBuilder constraint(boolean constraint) {
			this.constraint = constraint;
			return this;
		}

		public EdgeSpecBuilder same(String sameTail, String sameHead) {
			this.sameTail = sameTail;
			this.sameHead = sameHead;
			return this;
		}

		public EdgeSpecBuilder weight(double weight) {
			this.weight = weight;
			return this;
		}

		public EdgeSpecBuilder layoutOnly(boolean layoutOnly) {
			this.layoutOnly = layoutOnly;
			return this;
		}

		public EdgeSpecBuilder labels(List<LabelSpec> labels) {
			this.labels = labels;
			return this;
		}

		public EdgeSpec build() {
			return new EdgeSpec(this);
		}
	}

	private SvekLayoutModel() {
	}
}
