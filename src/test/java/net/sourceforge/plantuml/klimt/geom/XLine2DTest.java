/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
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
package net.sourceforge.plantuml.klimt.geom;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test class for XLine2D
 */
class XLine2DTest {

	@Test
	void testConstructorAndGetters() {
		XLine2D line = new XLine2D(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(1.0, line.getX1(), 0.001);
		assertEquals(2.0, line.getY1(), 0.001);
		assertEquals(3.0, line.getX2(), 0.001);
		assertEquals(4.0, line.getY2(), 0.001);
		
		// Test des champs publics
		assertEquals(1.0, line.x1, 0.001);
		assertEquals(2.0, line.y1, 0.001);
		assertEquals(3.0, line.x2, 0.001);
		assertEquals(4.0, line.y2, 0.001);
	}

	@Test
	void testLineFromPoints() {
		XPoint2D p1 = new XPoint2D(5.0, 10.0);
		XPoint2D p2 = new XPoint2D(15.0, 20.0);
		
		XLine2D line = XLine2D.line(p1, p2);
		
		assertEquals(5.0, line.getX1(), 0.001);
		assertEquals(10.0, line.getY1(), 0.001);
		assertEquals(15.0, line.getX2(), 0.001);
		assertEquals(20.0, line.getY2(), 0.001);
	}

	@Test
	void testGetMiddle() {
		XLine2D line = new XLine2D(0.0, 0.0, 10.0, 20.0);
		XPoint2D middle = line.getMiddle();
		
		assertEquals(5.0, middle.getX(), 0.001);
		assertEquals(10.0, middle.getY(), 0.001);
	}

	@Test
	void testGetP1AndGetP2() {
		XLine2D line = new XLine2D(1.0, 2.0, 3.0, 4.0);
		
		XPoint2D p1 = line.getP1();
		assertEquals(1.0, p1.getX(), 0.001);
		assertEquals(2.0, p1.getY(), 0.001);
		
		XPoint2D p2 = line.getP2();
		assertEquals(3.0, p2.getX(), 0.001);
		assertEquals(4.0, p2.getY(), 0.001);
	}

	@Test
	void testWithPoint1() {
		XLine2D line = new XLine2D(1.0, 2.0, 3.0, 4.0);
		XPoint2D newPoint = new XPoint2D(10.0, 20.0);
		
		XLine2D newLine = line.withPoint1(newPoint);
		
		assertEquals(10.0, newLine.getX1(), 0.001);
		assertEquals(20.0, newLine.getY1(), 0.001);
		assertEquals(3.0, newLine.getX2(), 0.001);
		assertEquals(4.0, newLine.getY2(), 0.001);
		
		// Vérifier que la ligne originale n'a pas changé
		assertEquals(1.0, line.getX1(), 0.001);
	}

	@Test
	void testWithPoint2() {
		XLine2D line = new XLine2D(1.0, 2.0, 3.0, 4.0);
		XPoint2D newPoint = new XPoint2D(10.0, 20.0);
		
		XLine2D newLine = line.withPoint2(newPoint);
		
		assertEquals(1.0, newLine.getX1(), 0.001);
		assertEquals(2.0, newLine.getY1(), 0.001);
		assertEquals(10.0, newLine.getX2(), 0.001);
		assertEquals(20.0, newLine.getY2(), 0.001);
		
		// Vérifier que la ligne originale n'a pas changé
		assertEquals(3.0, line.getX2(), 0.001);
	}

	@Test
	void testGetAngle() {
		// Ligne horizontale vers la droite (0 degrés)
		XLine2D lineH = new XLine2D(0.0, 0.0, 10.0, 0.0);
		assertEquals(0.0, lineH.getAngle(), 0.001);
		
		// Ligne verticale vers le haut (90 degrés = π/2)
		XLine2D lineV = new XLine2D(0.0, 0.0, 0.0, 10.0);
		assertEquals(Math.PI / 2, lineV.getAngle(), 0.001);
		
		// Ligne à 45 degrés
		XLine2D line45 = new XLine2D(0.0, 0.0, 10.0, 10.0);
		assertEquals(Math.PI / 4, line45.getAngle(), 0.001);
	}

	@Test
	void testPtSegDistSq() {
		// Distance d'un point au segment
		double dist = XLine2D.ptSegDistSq(0.0, 0.0, 10.0, 0.0, 5.0, 3.0);
		
		// Le point (5, 3) est à distance 3 du segment horizontal
		// donc distance au carré = 9
		assertEquals(9.0, dist, 0.001);
		
		// Point sur le segment
		dist = XLine2D.ptSegDistSq(0.0, 0.0, 10.0, 0.0, 5.0, 0.0);
		assertEquals(0.0, dist, 0.001);
	}

	@Test
	void testIntersect() {
		// Deux lignes qui se croisent
		XLine2D line1 = new XLine2D(0.0, 0.0, 10.0, 10.0);
		XLine2D line2 = new XLine2D(0.0, 10.0, 10.0, 0.0);
		
		XPoint2D intersection = line1.intersect(line2);
		
		assertNotNull(intersection);
		assertEquals(5.0, intersection.getX(), 0.001);
		assertEquals(5.0, intersection.getY(), 0.001);
	}

	@Test
	void testIntersectNoIntersection() {
		// Deux lignes parallèles
		XLine2D line1 = new XLine2D(0.0, 0.0, 10.0, 0.0);
		XLine2D line2 = new XLine2D(0.0, 5.0, 10.0, 5.0);
		
		XPoint2D intersection = line1.intersect(line2);
		
		assertNull(intersection);
	}

	@Test
	void testNegativeCoordinates() {
		XLine2D line = new XLine2D(-10.0, -20.0, 30.0, 40.0);
		
		assertEquals(-10.0, line.getX1(), 0.001);
		assertEquals(-20.0, line.getY1(), 0.001);
		assertEquals(30.0, line.getX2(), 0.001);
		assertEquals(40.0, line.getY2(), 0.001);
	}
}
