/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 * Original Author:  Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.gantt.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class SegmentTest {
	@Test
	void correctCreationOfSegment() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);

		Segment segment = Segment.forward(start, end, value);

		assertEquals(start, segment.startExclusive());
		assertEquals(end, segment.endExclusive());
		assertEquals(value, segment.getValue());
	}
	
	@Test
	void startCanNotBeNull() throws Exception {
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);
		
		assertThrows(NullPointerException.class, () -> Segment.forward(null, end, value));
	}

	@Test
	void endCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThrows(NullPointerException.class, () -> Segment.forward(start, null, value));
	}
	
	@Test
	void valueCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 17, 0);
		
		assertThrows(NullPointerException.class, () -> Segment.forward(start, end, null));
	}
	
	@Test 
	void startMustBeBeforeEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 17, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThrows(IllegalArgumentException.class, () -> Segment.forward(start, end, value));
	}
	
	@Test 
	void startMustNotBeEqualToEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThrows(IllegalArgumentException.class, () -> Segment.forward(start, end, value));
	}
		
//	@Test
//	void checkingIfTimeIsInsideSegment() throws Exception {
//		LocalDateTime start = LocalDateTime.of(2024, 3, 1, 9, 0);
//		LocalDateTime end = LocalDateTime.of(2024, 3, 1, 17, 0);
//		Fraction value = Fraction.of(1);
//		Segment segment = Segment.forward(start, end, value);
//		
//		LocalDateTime insideTime = LocalDateTime.of(2024, 3, 1, 12, 0);
//		assertTrue(segment.includes(insideTime));
//		
//		LocalDateTime beforeTime = LocalDateTime.of(2024, 3, 1, 8, 0);
//		assertFalse(segment.includes(beforeTime));
//		
//		LocalDateTime afterTime = LocalDateTime.of(2024, 3, 1, 18, 0);
//		assertFalse(segment.includes(afterTime));
//	}
//	
//	@Test
//	void checkingIfTimeAtBoundsIsIncluded() throws Exception {
//		LocalDateTime start = LocalDateTime.of(2024, 4, 1, 9, 0);
//		LocalDateTime end = LocalDateTime.of(2024, 4, 1, 17, 0);
//		Fraction value = Fraction.of(1);
//		Segment segment = Segment.forward(start, end, value);
//		
//		assertTrue(segment.includes(start));
//		assertFalse(segment.includes(end));
//	}
	
	@Test
	void splittingSegmentAtValidTime() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime splitTime = LocalDateTime.of(2024, 5, 1, 13, 0);
		Segment[] splitSegments = segment.split(splitTime);
		
		assertEquals(2, splitSegments.length);
		assertEquals(start, splitSegments[0].startExclusive());
		assertEquals(splitTime, splitSegments[0].endExclusive());
		assertEquals(value, splitSegments[0].getValue());
		
		assertEquals(splitTime, splitSegments[1].startExclusive());
		assertEquals(end, splitSegments[1].endExclusive());
		assertEquals(value, splitSegments[1].getValue());
	}
	
	@Test
	void splittingSegmentWithNullTimeThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertThrows(NullPointerException.class, () -> segment.split(null));
	}
	
	@Test
	void splittingSegmentOutsideOfBoundaryThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime beforeSplitTime = LocalDateTime.of(2024, 5, 1, 8, 0);
		assertThrows(IllegalArgumentException.class, () -> segment.split(beforeSplitTime));
		
		LocalDateTime afterSplitTime = LocalDateTime.of(2024, 5, 1, 18, 0);
		assertThrows(IllegalArgumentException.class, () -> segment.split(afterSplitTime));
	}
	
	@Test
	void splittingSegmentAtStartOrEndThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertThrows(IllegalArgumentException.class, () -> segment.split(start));
		
		assertThrows(IllegalArgumentException.class, () -> segment.split(end));
	}
	
	///// Testing intersection of segments
	@Test
	void intersectionOfNoSegments() throws Exception {
		assertThrows(IllegalArgumentException.class, 
				() -> Segment.intersection(new Segment[0]));
	}
	
	@Test
	void intersectionOfOneSegment() throws Exception {
		Segment segment = Segment.forward(
				LocalDateTime.of(2024, 6, 1, 9, 0),
				LocalDateTime.of(2024, 6, 1, 17, 0),
				Fraction.of(1));
		
		Segment result = Segment.intersection(new Segment[] { segment });
		
		assertSame(segment, result);
	}
	
	@Test
	void intersectionOfDisjointSegments() throws Exception {
		Segment segment1 = Segment.forward(
				LocalDateTime.of(2025, 7, 1, 9, 0),
				LocalDateTime.of(2025, 7, 1, 12, 0),
				Fraction.of(1));
		
		Segment segment2 = Segment.forward(
				LocalDateTime.of(2025, 7, 1, 13, 0),
				LocalDateTime.of(2025, 7, 1, 17, 0),
				Fraction.of(1));
		
		assertThrows(IllegalArgumentException.class, 
				() -> Segment.intersection(new Segment[] { segment1, segment2 }));
		
	}
	
	@Test
	void intersectionOfTwoSegments() throws Exception {
		Segment segment1 = Segment.forward(
				LocalDateTime.of(2025, 7, 1, 9, 0),
				LocalDateTime.of(2025, 7, 1, 13, 0),
				Fraction.of(1));
		
		Segment segment2 = Segment.forward(
				LocalDateTime.of(2025, 7, 1, 12, 0),
				LocalDateTime.of(2025, 7, 1, 17, 0),
				new Fraction(1, 2));
		
		Segment result = Segment.intersection(new Segment[] { segment1, segment2 });	
		
		assertEquals(LocalDateTime.of(2025, 7, 1, 12, 0), result.startExclusive());
		assertEquals(LocalDateTime.of(2025, 7, 1, 13, 0), result.endExclusive());
		assertEquals(new Fraction(1, 2), result.getValue());
	}
	
	
	@Test
	void intersectionOfMultipleSegmentsWithSumFunction() throws Exception {
		Segment[] segments = new Segment[] {
				Segment.forward(
						LocalDateTime.of(2025, 7, 1, 8, 0),
						LocalDateTime.of(2025, 7, 1, 16, 0),
						Fraction.of(1)),
				Segment.forward(
						LocalDateTime.of(2025, 7, 1, 9, 0),
						LocalDateTime.of(2025, 7, 1, 17, 0),
						new Fraction(2, 3)),
				Segment.forward(
						LocalDateTime.of(2025, 7, 1, 10, 0),
						LocalDateTime.of(2025, 7, 1, 18, 0),
						new Fraction(3, 4))
		};
		
		Segment result = Segment.intersection(segments, Fraction.SUM);	
		
		assertEquals(LocalDateTime.of(2025, 7, 1, 10, 0), result.startExclusive());
		assertEquals(LocalDateTime.of(2025, 7, 1, 16, 0), result.endExclusive());
		// 1 + 2/3 + 3/4 = 12/12 + 8/12 + 9/12 = 29/12
		assertEquals(new Fraction(29, 12), result.getValue());
	}

	///// Testing backward segments
	
	@Test
	void correctCreationOfBackwardSegment() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 1, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 1, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);

		Segment segment = Segment.backward(a, b, value);

		assertEquals(a, segment.startExclusive());
		assertEquals(b, segment.endExclusive());
		assertEquals(value, segment.getValue());
		assertEquals(TimeDirection.BACKWARD, segment.getTimeDirection());
	}
	
	@Test
	void backwardSegmentMustHaveAAfterB() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 1, 1, 9, 0);  // a is earlier (invalid)
		LocalDateTime b = LocalDateTime.of(2024, 1, 1, 17, 0); // b is later (invalid)
		Fraction value = Fraction.of(1);
		
		assertThrows(IllegalArgumentException.class, () -> Segment.backward(a, b, value));
	}
	
//	@Test
//	void backwardSegmentIncludesTimeInside() throws Exception {
//		LocalDateTime a = LocalDateTime.of(2024, 3, 1, 17, 0); // a is later (included)
//		LocalDateTime b = LocalDateTime.of(2024, 3, 1, 9, 0);  // b is earlier (excluded)
//		Fraction value = Fraction.of(1);
//		Segment segment = Segment.backward(a, b, value);
//		
//		LocalDateTime insideTime = LocalDateTime.of(2024, 3, 1, 12, 0);
//		assertTrue(segment.includes(insideTime));
//		
//		LocalDateTime beforeB = LocalDateTime.of(2024, 3, 1, 8, 0);
//		assertFalse(segment.includes(beforeB));
//		
//		LocalDateTime afterA = LocalDateTime.of(2024, 3, 1, 18, 0);
//		assertFalse(segment.includes(afterA));
//	}
//	
//	@Test
//	void backwardSegmentIncludesAButExcludesB() throws Exception {
//		LocalDateTime a = LocalDateTime.of(2024, 4, 1, 17, 0); // a is later (included)
//		LocalDateTime b = LocalDateTime.of(2024, 4, 1, 9, 0);  // b is earlier (excluded)
//		Fraction value = Fraction.of(1);
//		Segment segment = Segment.backward(a, b, value);
//		
//		assertTrue(segment.includes(a));
//		assertFalse(segment.includes(b));
//	}
	
	///// Testing strictIncludes for forward segments
	
	@Test
	void forwardSegmentStrictIncludesTimeInside() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 3, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 3, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime insideTime = LocalDateTime.of(2024, 3, 1, 12, 0);
		assertTrue(segment.includes(insideTime));
		
		LocalDateTime beforeTime = LocalDateTime.of(2024, 3, 1, 8, 0);
		assertFalse(segment.includes(beforeTime));
		
		LocalDateTime afterTime = LocalDateTime.of(2024, 3, 1, 18, 0);
		assertFalse(segment.includes(afterTime));
	}
	
	@Test
	void forwardSegmentStrictIncludesExcludesBothBounds() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 4, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 4, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertFalse(segment.includes(start));
		assertFalse(segment.includes(end));
	}
	
	///// Testing strictIncludes for backward segments
	
	@Test
	void backwardSegmentStrictIncludesTimeInside() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 3, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 3, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);
		Segment segment = Segment.backward(a, b, value);
		
		LocalDateTime insideTime = LocalDateTime.of(2024, 3, 1, 12, 0);
		assertTrue(segment.includes(insideTime));
		
		LocalDateTime beforeB = LocalDateTime.of(2024, 3, 1, 8, 0);
		assertFalse(segment.includes(beforeB));
		
		LocalDateTime afterA = LocalDateTime.of(2024, 3, 1, 18, 0);
		assertFalse(segment.includes(afterA));
	}
	
	@Test
	void backwardSegmentStrictIncludesExcludesBothBounds() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 4, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 4, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);
		Segment segment = Segment.backward(a, b, value);
		
		assertFalse(segment.includes(a));
		assertFalse(segment.includes(b));
	}
	
	///// Testing split for backward segments
	
	@Test
	void splittingBackwardSegmentAtValidTime() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 5, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 5, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);
		Segment segment = Segment.backward(a, b, value);
		
		LocalDateTime splitTime = LocalDateTime.of(2024, 5, 1, 13, 0);
		Segment[] splitSegments = segment.split(splitTime);
		
		assertEquals(2, splitSegments.length);
		
		// First segment: [a, splitTime) in backward direction
		assertEquals(a, splitSegments[0].startExclusive());
		assertEquals(splitTime, splitSegments[0].endExclusive());
		assertEquals(value, splitSegments[0].getValue());
		assertEquals(TimeDirection.BACKWARD, splitSegments[0].getTimeDirection());
		
		// Second segment: [splitTime, b) in backward direction
		assertEquals(splitTime, splitSegments[1].startExclusive());
		assertEquals(b, splitSegments[1].endExclusive());
		assertEquals(value, splitSegments[1].getValue());
		assertEquals(TimeDirection.BACKWARD, splitSegments[1].getTimeDirection());
	}
	
	@Test
	void splittingBackwardSegmentOutsideOfBoundaryThrowsException() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 5, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 5, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);
		Segment segment = Segment.backward(a, b, value);
		
		LocalDateTime beforeB = LocalDateTime.of(2024, 5, 1, 8, 0);
		assertThrows(IllegalArgumentException.class, () -> segment.split(beforeB));
		
		LocalDateTime afterA = LocalDateTime.of(2024, 5, 1, 18, 0);
		assertThrows(IllegalArgumentException.class, () -> segment.split(afterA));
	}
	
	@Test
	void splittingBackwardSegmentAtBoundsThrowsException() throws Exception {
		LocalDateTime a = LocalDateTime.of(2024, 5, 1, 17, 0); // a is later
		LocalDateTime b = LocalDateTime.of(2024, 5, 1, 9, 0);  // b is earlier
		Fraction value = Fraction.of(1);
		Segment segment = Segment.backward(a, b, value);
		
		assertThrows(IllegalArgumentException.class, () -> segment.split(a));
		
		assertThrows(IllegalArgumentException.class, () -> segment.split(b));
	}
	
	///// Testing intersection for backward segments
	
	@Test
	void intersectionOfTwoBackwardSegments() throws Exception {
		Segment segment1 = Segment.backward(
				LocalDateTime.of(2025, 7, 1, 17, 0),
				LocalDateTime.of(2025, 7, 1, 9, 0),
				Fraction.of(1));
		
		Segment segment2 = Segment.backward(
				LocalDateTime.of(2025, 7, 1, 15, 0),
				LocalDateTime.of(2025, 7, 1, 10, 0),
				new Fraction(1, 2));
		
		Segment result = Segment.intersection(new Segment[] { segment1, segment2 });
		
		assertEquals(LocalDateTime.of(2025, 7, 1, 15, 0), result.startExclusive());
		assertEquals(LocalDateTime.of(2025, 7, 1, 10, 0), result.endExclusive());
		assertEquals(new Fraction(1, 2), result.getValue());
		assertEquals(TimeDirection.BACKWARD, result.getTimeDirection());
	}
	
	@Test
	void intersectionOfMultipleBackwardSegmentsWithSumFunction() throws Exception {
		Segment[] segments = new Segment[] {
				Segment.backward(
						LocalDateTime.of(2025, 7, 1, 18, 0),
						LocalDateTime.of(2025, 7, 1, 8, 0),
						Fraction.of(1)),
				Segment.backward(
						LocalDateTime.of(2025, 7, 1, 17, 0),
						LocalDateTime.of(2025, 7, 1, 9, 0),
						new Fraction(2, 3)),
				Segment.backward(
						LocalDateTime.of(2025, 7, 1, 16, 0),
						LocalDateTime.of(2025, 7, 1, 10, 0),
						new Fraction(3, 4))
		};
		
		Segment result = Segment.intersection(segments, Fraction.SUM);
		
		assertEquals(LocalDateTime.of(2025, 7, 1, 16, 0), result.startExclusive());
		assertEquals(LocalDateTime.of(2025, 7, 1, 10, 0), result.endExclusive());
		// 1 + 2/3 + 3/4 = 12/12 + 8/12 + 9/12 = 29/12
		assertEquals(new Fraction(29, 12), result.getValue());
		assertEquals(TimeDirection.BACKWARD, result.getTimeDirection());
	}
	
	@Test
	void intersectionOfDisjointBackwardSegments() throws Exception {
		Segment segment1 = Segment.backward(
				LocalDateTime.of(2025, 7, 1, 17, 0),
				LocalDateTime.of(2025, 7, 1, 14, 0),
				Fraction.of(1));
		
		Segment segment2 = Segment.backward(
				LocalDateTime.of(2025, 7, 1, 12, 0),
				LocalDateTime.of(2025, 7, 1, 9, 0),
				Fraction.of(1));
		
		assertThrows(IllegalArgumentException.class, () -> Segment.intersection(new Segment[] { segment1, segment2 }));
	}
	
	@Test
	void intersectionOfMixedDirectionsThrowsException() throws Exception {
		Segment forwardSegment = Segment.forward(
				LocalDateTime.of(2025, 7, 1, 9, 0),
				LocalDateTime.of(2025, 7, 1, 17, 0),
				Fraction.of(1));
		
		Segment backwardSegment = Segment.backward(
				LocalDateTime.of(2025, 7, 1, 17, 0),
				LocalDateTime.of(2025, 7, 1, 9, 0),
				Fraction.of(1));
		
		assertTrue(assertThrows(IllegalArgumentException.class, () -> Segment.intersection(new Segment[] { forwardSegment, backwardSegment })).getMessage().contains("same direction"));
	}

}
