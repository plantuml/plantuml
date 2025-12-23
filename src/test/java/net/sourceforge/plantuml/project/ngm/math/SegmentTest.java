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
package net.sourceforge.plantuml.project.ngm.math;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Test;

class SegmentTest {
	@Test
	void correctCreationOfSegment() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);

		Segment segment = Segment.forward(start, end, value);

		assertThat(segment.aInclusive()).isEqualTo(start);
		assertThat(segment.bExclusive()).isEqualTo(end);
		assertThat(segment.getValue()).isEqualTo(value);
	}
	
	@Test
	void startCanNotBeNull() throws Exception {
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> Segment.forward(null, end, value))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void endCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> Segment.forward(start, null, value))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test
	void valueCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 17, 0);
		
		assertThatThrownBy(() -> Segment.forward(start, end, null))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test 
	void startMustBeBeforeEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 17, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> Segment.forward(start, end, value))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test 
	void startMustNotBeEqualToEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> Segment.forward(start, end, value))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void checkingIfTimeIsNullWhenCallingInside() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 3, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 3, 1, 17, 0);
		Fraction value = Fraction.of(1);	
		Segment segment = Segment.forward(start, end, value);
		
		assertThatThrownBy(() -> segment.includes(null))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test
	void checkingIfTimeIsInsideSegment() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 3, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 3, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime insideTime = LocalDateTime.of(2024, 3, 1, 12, 0);
		assertThat(segment.includes(insideTime)).isTrue();
		
		LocalDateTime beforeTime = LocalDateTime.of(2024, 3, 1, 8, 0);
		assertThat(segment.includes(beforeTime)).isFalse();
		
		LocalDateTime afterTime = LocalDateTime.of(2024, 3, 1, 18, 0);
		assertThat(segment.includes(afterTime)).isFalse();
	}
	
	@Test
	void checkingIfTimeAtBoundsIsIncluded() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 4, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 4, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertThat(segment.includes(start)).isTrue();
		assertThat(segment.includes(end)).isFalse();
	}
	
	@Test
	void splittingSegmentAtValidTime() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime splitTime = LocalDateTime.of(2024, 5, 1, 13, 0);
		Segment[] splitSegments = segment.split(splitTime);
		
		assertThat(splitSegments).hasSize(2);
		assertThat(splitSegments[0].aInclusive()).isEqualTo(start);
		assertThat(splitSegments[0].bExclusive()).isEqualTo(splitTime);
		assertThat(splitSegments[0].getValue()).isEqualTo(value);
		
		assertThat(splitSegments[1].aInclusive()).isEqualTo(splitTime);
		assertThat(splitSegments[1].bExclusive()).isEqualTo(end);
		assertThat(splitSegments[1].getValue()).isEqualTo(value);
	}
	
	@Test
	void splittingSegmentWithNullTimeThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertThatThrownBy(() -> segment.split(null))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test
	void splittingSegmentOutsideOfBoundaryThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		LocalDateTime beforeSplitTime = LocalDateTime.of(2024, 5, 1, 8, 0);
		assertThatThrownBy(() -> segment.split(beforeSplitTime))
			.isInstanceOf(IllegalArgumentException.class);
		
		LocalDateTime afterSplitTime = LocalDateTime.of(2024, 5, 1, 18, 0);
		assertThatThrownBy(() -> segment.split(afterSplitTime))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	void splittingSegmentAtStartOrEndThrowsException() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 5, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 5, 1, 17, 0);
		Fraction value = Fraction.of(1);
		Segment segment = Segment.forward(start, end, value);
		
		assertThatThrownBy(() -> segment.split(start))
			.isInstanceOf(IllegalArgumentException.class);
		
		assertThatThrownBy(() -> segment.split(end))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	///// Testing intersection of segments
	@Test
	void intersectionOfNoSegments() throws Exception {
		assertThrows(IllegalArgumentException.class, 
				() -> Segment.intersection(List.of()));
	}
	
	@Test
	void intersectionOfOneSegment() throws Exception {
		Segment segment = Segment.forward(
				LocalDateTime.of(2024, 6, 1, 9, 0),
				LocalDateTime.of(2024, 6, 1, 17, 0),
				Fraction.of(1));
		
		Segment result = Segment.intersection(List.of(segment));
		
		assertThat(result).isSameAs(segment);
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
				() -> Segment.intersection(List.of(segment1, segment2)));
		
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
		
		Segment result = Segment.intersection(List.of(segment1, segment2));	
		
		assertThat(result.aInclusive()).isEqualTo(LocalDateTime.of(2025, 7, 1, 12, 0));
		assertThat(result.bExclusive()).isEqualTo(LocalDateTime.of(2025, 7, 1, 13, 0));
		assertThat(result.getValue()).isEqualTo(new Fraction(1, 2));
	}
	
	
	@Test
	void intersectionOfMultipleSegmentsWithSumFunction() throws Exception {
		List<Segment> segments = List.of(
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
		);
		
		Segment result = Segment.intersection(segments, Fraction.SUM);	
		
		assertThat(result.aInclusive()).isEqualTo(LocalDateTime.of(2025, 7, 1, 10, 0));
		assertThat(result.bExclusive()).isEqualTo(LocalDateTime.of(2025, 7, 1, 16, 0));
		// 1 + 2/3 + 3/4 = 12/12 + 8/12 + 9/12 = 29/12
		assertThat(result.getValue()).isEqualTo(new Fraction(29, 12));
	}

}