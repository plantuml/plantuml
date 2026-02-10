package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PiecewiseConstantHoursTest {

	private PiecewiseConstantHours hours;

	@BeforeEach
	void setUp() {
		// Working hours from 8:00 to 12:00 and from 14:00 to 18:00
		hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.of(8, 0), LocalTime.of(12, 0), new Fraction(1, 2))
				.with(LocalTime.of(14, 0), LocalTime.of(18, 0), Fraction.ONE);
	}

	@Test
	void segmentAt_forward_beforeFirstWorkingHour_returnsZeroSegment() {
		// Given: 6:00 AM (before working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(6, 0);

		// When/Then: Segment from midnight to 8:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-01T08:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_forward_duringMorningWork_returnsOneSegment() {
		// Given: 10:00 AM (during morning working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(10, 0);

		// When/Then: Segment from 8:00 to 12:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T08:00, 2025-12-01T12:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_forward_duringLunchBreak_returnsZeroSegment() {
		// Given: 1:00 PM (during lunch break)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(13, 0);

		// When/Then: Segment from 12:00 to 14:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T12:00, 2025-12-01T14:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_forward_duringAfternoonWork_returnsOneSegment() {
		// Given: 4:00 PM (during afternoon working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(16, 0);

		// When/Then: Segment from 14:00 to 18:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T14:00, 2025-12-01T18:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_forward_afterWorkingHours_returnsZeroSegmentUntilMidnight() {
		// Given: 8:00 PM (after working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(20, 0);

		// When/Then: Segment from 18:00 to midnight with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T18:00, 2025-12-02T00:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_forward_exactlyAtBoundary_returnsSegmentStartingAtThatBoundary() {
		// Given: Exactly at 8:00 AM (boundary)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(8, 0);

		// When/Then: Segment from 8:00 to 12:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T08:00, 2025-12-01T12:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_forward_atMidnight_returnsFirstSegment() {
		// Given: Exactly at midnight
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When/Then: Segment from midnight to 8:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-01T08:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_backward_beforeFirstWorkingHour_returnsZeroSegment() {
		// Given: 6:00 AM (before working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(6, 0);

		// When/Then: Segment from 8:00 to midnight with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T08:00, 2025-12-01T00:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_backward_duringMorningWork_returnsOneSegment() {
		// Given: 10:00 AM (during morning working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(10, 0);

		// When/Then: Segment from 12:00 to 8:00 with value 1/2
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T12:00, 2025-12-01T08:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_backward_duringLunchBreak_returnsZeroSegment() {
		// Given: 1:00 PM (during lunch break)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(13, 0);

		// When/Then: Segment from 14:00 to 12:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T14:00, 2025-12-01T12:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_backward_duringAfternoonWork_returnsOneSegment() {
		// Given: 4:00 PM (during afternoon working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(16, 0);

		// When/Then: Segment from 18:00 to 14:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T18:00, 2025-12-01T14:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_afterWorkingHours_returnsZeroSegmentFromMidnight() {
		// Given: 8:00 PM (after working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(20, 0);

		// When/Then: Segment from midnight (next day) to 18:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-02T00:00, 2025-12-01T18:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_backward_exactlyAtBoundary_returnsSegmentEndingAtPreviousBoundary() {
		// Given: Exactly at 8:00 AM (boundary)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(8, 0);

		// When/Then: Segment from 8:00 to midnight with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T08:00, 2025-12-01T00:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_backward_atMidnight_returnsLastSegmentOfPreviousDay() {
		// Given: Exactly at midnight
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When/Then: Segment from midnight to 18:00 (previous day) with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T00:00, 2025-11-30T18:00[ value=0", segment.toString());
	}

}
