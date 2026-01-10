package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PiecewiseConstantHoursMidnightTest {

	private PiecewiseConstantHours hours;

	@BeforeEach
	void setUp() {
		// Working hours from 0:00 to 12:00 (50%) and from 14:00 to 23:59 (100%)
		// Gap from 12:00 to 14:00 (0% - lunch break)
		hours = PiecewiseConstantHours.of(Fraction.ZERO)
				.with(LocalTime.MIDNIGHT, LocalTime.of(12, 0), new Fraction(1, 2))
				.with(LocalTime.of(14, 0), LocalTime.MIDNIGHT, Fraction.ONE);
	}

	@Test
	void segmentAt_forward_atMidnight_returnsFirstSegment() {
		// Given: Exactly at midnight
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When/Then: Segment from midnight to 12:00 with value 1/2
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-01T12:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_forward_duringMorningWork_returnsHalfSegment() {
		// Given: 6:00 AM (during morning working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(6, 0);

		// When/Then: Segment from midnight to 12:00 with value 1/2
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-01T12:00[ value=1/2", segment.toString());
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

		// When/Then: Segment from 14:00 to 23:59:59.999999999 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T14:00, 2025-12-02T00:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_forward_lateEvening_returnsOneSegment() {
		// Given: 11:00 PM (late evening, still in afternoon work segment)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(23, 0);

		// When/Then: Segment from 14:00 to 23:59:59.999999999 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T14:00, 2025-12-02T00:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_forward_exactlyAtNoon_returnsLunchBreakSegment() {
		// Given: Exactly at 12:00 (boundary - start of lunch break)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(12, 0);

		// When/Then: Segment from 12:00 to 14:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T12:00, 2025-12-01T14:00[ value=0", segment.toString());
	}

	@Test
	void segmentAt_forward_exactlyAt14h_returnsAfternoonSegment() {
		// Given: Exactly at 14:00 (boundary - start of afternoon work)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(14, 0);

		// When/Then: Segment from 14:00 to 23:59:59.999999999 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals("FORWARD ]2025-12-01T14:00, 2025-12-02T00:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_atMidnight_returnsLastSegmentOfPreviousDay() {
		// Given: Exactly at midnight
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When/Then: Segment from midnight to 14:00 (previous day) with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T00:00, 2025-11-30T14:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_duringMorningWork_returnsHalfSegment() {
		// Given: 6:00 AM (during morning working hours)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(6, 0);

		// When/Then: Segment from 12:00 to midnight with value 1/2
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T12:00, 2025-12-01T00:00[ value=1/2", segment.toString());
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

		// When/Then: Segment from midnight (next day) to 14:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-02T00:00, 2025-12-01T14:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_lateEvening_returnsOneSegment() {
		// Given: 11:00 PM (late evening, still in afternoon work segment)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(23, 0);

		// When/Then: Segment from midnight (next day) to 14:00 with value 1
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-02T00:00, 2025-12-01T14:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_exactlyAtNoon_returnsMorningSegment() {
		// Given: Exactly at 12:00 (boundary)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(12, 0);

		// When/Then: Segment from 12:00 to midnight with value 1/2
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T12:00, 2025-12-01T00:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_backward_exactlyAt14h_returnsLunchBreakSegment() {
		// Given: Exactly at 14:00 (boundary)
		LocalDateTime instant = LocalDate.of(2025, 12, 1).atTime(14, 0);

		// When/Then: Segment from 14:00 to 12:00 with value 0
		Segment segment = hours.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("BACKWARD ]2025-12-01T14:00, 2025-12-01T12:00[ value=0", segment.toString());
	}

}
