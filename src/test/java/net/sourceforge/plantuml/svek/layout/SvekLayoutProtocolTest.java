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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.EdgeSpec;
import net.sourceforge.plantuml.svek.layout.SvekLayoutModel.Direction;

class SvekLayoutProtocolTest {

	@Test
	void copiesMutableCollectionsAtTheBoundary() {
		final Map<String, SvekLayoutResult.NodeGeometry> nodes = new LinkedHashMap<>();
		nodes.put("a", new SvekLayoutResult.NodeGeometry("a", new SvekLayoutResult.Bounds(1, 2, 11, 12), null));

		final SvekLayoutResult result = new SvekLayoutResult(
				new SvekLayoutResult.Bounds(0, 0, 20, 20), Direction.TOP_TO_BOTTOM, nodes,
				new LinkedHashMap<>(),
				new LinkedHashMap<>());
		nodes.clear();

		assertEquals(Collections.singleton("a"), result.nodes.keySet());
		assertThrows(UnsupportedOperationException.class, () -> result.nodes.clear());
	}

	@Test
	void copiesPathPoints() {
		final SvekLayoutResult.Path path = new SvekLayoutResult.Path(false,
				asList(new SvekLayoutResult.Point(1, 2), new SvekLayoutResult.Point(3, 4)));

		assertEquals(2, path.points.size());
		assertThrows(UnsupportedOperationException.class, () -> path.points.clear());
	}

	@Test
	void rejectsNullSuccessfulResult() {
		assertThrows(NullPointerException.class, () -> SvekLayoutResponse.success(null));
	}

	@Test
	void rejectsNullDeclineCode() {
		assertThrows(NullPointerException.class, () -> SvekLayoutResponse.declined(null, "unsupported layout"));
	}

	@Test
	void edgeCellFieldsIdentifyEndpointCellIds() {
		final EdgeSpec edge = EdgeSpec.builder("edge", "tail", "head").cells("tail-cell", "head-cell").build();

		assertEquals("tail-cell", edge.tailCellId);
		assertEquals("head-cell", edge.headCellId);
	}

	@Test
	void resultRetainsDirectionAndExactEdgeEndpoints() {
		final Map<String, SvekLayoutResult.EdgeGeometry> edges = new LinkedHashMap<>();
		edges.put("edge", new SvekLayoutResult.EdgeGeometry("edge", "tail", "head", null, null, null, null));
		final SvekLayoutResult result = new SvekLayoutResult(new SvekLayoutResult.Bounds(0, 0, 20, 20),
				Direction.LEFT_TO_RIGHT, new LinkedHashMap<>(),
				new LinkedHashMap<>(), edges);

		assertEquals(Direction.LEFT_TO_RIGHT, result.direction);
		assertEquals("tail", result.edges.get("edge").tailId);
		assertEquals("head", result.edges.get("edge").headId);
	}
}
