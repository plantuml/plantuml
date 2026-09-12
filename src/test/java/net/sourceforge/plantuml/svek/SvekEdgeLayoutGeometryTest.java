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
package net.sourceforge.plantuml.svek;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import nonreg.svg.GraphvizRequiredTestFilter;
import test.utils.PlantUmlTestUtils;

@ExtendWith(GraphvizRequiredTestFilter.class)
class SvekEdgeLayoutGeometryTest {

	@Test
	void graphvizLayoutPreservesMainTailAndHeadLabels() throws Exception {
		final String svg = PlantUmlTestUtils.exportDiagram(
				"@startuml",
				"class First",
				"class Second",
				"First \"tail label\" --> \"head label\" Second : main label",
				"@enduml")
				.assertNoError()
				.asString(FileFormat.SVG);

		assertTrue(svg.contains(">First<"));
		assertTrue(svg.contains(">Second<"));
		assertTrue(svg.contains(">main label<"));
		assertTrue(svg.contains(">tail label<"));
		assertTrue(svg.contains(">head label<"));
	}

	@Test
	void exposesPackageVisibleGeometryApplication() throws Exception {
		final Method method = SvekEdge.class.getDeclaredMethod("applyLayoutGeometry", DotPath.class,
				PointListIterator.class, XPoint2D.class, XPoint2D.class, XPoint2D.class);

		assertFalse(Modifier.isPublic(method.getModifiers()));
		assertFalse(Modifier.isProtected(method.getModifiers()));
		assertFalse(Modifier.isPrivate(method.getModifiers()));
	}
}
