package net.sourceforge.plantuml.project.ngm.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class PiecewiseConstantSpecificDaysTest {

	@Test
	void iterateSegmentsFrom_whenAtMidnight_includesThatDay() {
		// Given: A pattern with all days at 100%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ONE);
		LocalDateTime from = LocalDate.of(2025, 12, 1).atStartOfDay();

		// When: Iterating from exactly midnight
		Iterator<Segment> it = f.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: First segment is the full day
		Segment s0 = it.next();
		assertEquals(from, s0.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), s0.endExclusive());
		assertEquals(Fraction.ONE, s0.getValue());
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-02T00:00[ value=1", s0.toString());
	}

	@Test
	void iterateSegmentsFrom_whenAtMidday_returnsSegmentContainingThatInstant() {
		// Given: Dec 1 at 100%, Dec 2 at 50%, default 0%
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 1), Fraction.ONE)
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(1, 2));
		LocalDateTime from = LocalDate.of(2025, 12, 1).atTime(12, 0); // noon

		// When: Iterating from noon
		Iterator<Segment> it = pw.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: First segment is the full day containing the instant (starts at 00:00)
		Segment segment1 = it.next();
		assertEquals(LocalDate.of(2025, 12, 1).atStartOfDay(), segment1.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 2).atStartOfDay(), segment1.endExclusive());
		assertEquals(Fraction.ONE, segment1.getValue());
		assertEquals("FORWARD ]2025-12-01T00:00, 2025-12-02T00:00[ value=1", segment1.toString());

		// Then: Second segment is Dec 2 with 50%
		Segment tuesday = it.next();
		assertEquals("FORWARD ]2025-12-02T00:00, 2025-12-03T00:00[ value=1/2", tuesday.toString());
	}

	@Test
	void iterateSegmentsFrom_producesDailySegments_withCorrectValuesOverSeveralDays() {
		// Given: Dec 2 at 40%, Dec 3 at 60%, default 0%
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(2, 5)) // 40%
				.withDay(LocalDate.of(2025, 12, 3), new Fraction(3, 5)); // 60%
		LocalDateTime from = LocalDate.of(2025, 12, 2).atStartOfDay();

		// When: Iterating from Dec 2
		final Iterator<Segment> it = pw.iterateSegmentsFrom(from, TimeDirection.FORWARD);

		// Then: Dec 2 is 40%
		assertEquals("FORWARD ]2025-12-02T00:00, 2025-12-03T00:00[ value=2/5", it.next().toString());

		// Then: Dec 3 is 60%
		assertEquals("FORWARD ]2025-12-03T00:00, 2025-12-04T00:00[ value=3/5", it.next().toString());

		// Then: Dec 4 defaults to 0%
		assertEquals("FORWARD ]2025-12-04T00:00, 2025-12-05T00:00[ value=0", it.next().toString());
	}

	@Test
	void iterateSegmentsBackwardFrom_producesDailySegments_withCorrectValuesOverSeveralDays() {
		// Given: Dec 2 at 40%, Dec 3 at 60%, default 0%
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(2, 5)) // 40%
				.withDay(LocalDate.of(2025, 12, 3), new Fraction(3, 5)); // 60%
		LocalDateTime from = LocalDate.of(2025, 12, 4).atStartOfDay();

		// When: Iterating backward from Dec 4
		Iterator<Segment> it = pw.iterateSegmentsFrom(from, TimeDirection.BACKWARD);

		// Then: Dec 3 is 60%
		assertEquals("BACKWARD ]2025-12-04T00:00, 2025-12-03T00:00[ value=3/5", it.next().toString());

		// Then: Dec 2 is 40%
		assertEquals("BACKWARD ]2025-12-03T00:00, 2025-12-02T00:00[ value=2/5", it.next().toString());

		// Then: Dec 1 defaults to 0%
		assertEquals("BACKWARD ]2025-12-02T00:00, 2025-12-01T00:00[ value=0", it.next().toString());
	}

	@Test
	void segmentValue_isConsistentWithApply_forAnyInstantWithinDay() {
		// Given: Dec 1 at 30%, Dec 2 at 50%, Dec 3 at 100%
		PiecewiseConstantSpecificDays pw = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 1), new Fraction(3, 10)) // 30%
				.withDay(LocalDate.of(2025, 12, 2), new Fraction(1, 2)) // 50%
				.withDay(LocalDate.of(2025, 12, 3), Fraction.ONE); // 100%

		// Then: For any instant within a day, the segment value matches the day's workload
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 1, 10, 15));  // Dec 1 at 10:15
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 2, 23, 59));  // Dec 2 at 23:59
		assertApplyMatchesFirstDailySegment(pw, LocalDateTime.of(2025, 12, 3, 0, 1));    // Dec 3 at 00:01
	}

	/**
	 * Verifies that the segment containing the given instant has the expected
	 * structure: starts at day boundary, spans exactly one day, and contains the
	 * instant.
	 */
	private static void assertApplyMatchesFirstDailySegment(PiecewiseConstant pw, LocalDateTime instant) {
		LocalDateTime dayStart = instant.toLocalDate().atStartOfDay();

		final Iterator<Segment> it = pw.iterateSegmentsFrom(dayStart, TimeDirection.FORWARD);
		assertTrue(it.hasNext());

		final Segment seg = it.next();

		assertEquals(dayStart, seg.startExclusive());
		assertEquals(dayStart.plusDays(1), seg.endExclusive());
		assertFalse(instant.isBefore(seg.startExclusive()));
		assertTrue(instant.isBefore(seg.endExclusive()));
	}

	// Tests for segmentAt()

	@Test
	void segmentAt_forward_returnsWholeDaySegment() {
		// Given: Dec 15 at 50%, default 100%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2025, 12, 15), new Fraction(1, 2));
		LocalDateTime instant = LocalDateTime.of(2025, 12, 15, 12, 0);

		// When/Then: Forward segment covers the whole day
		Segment segment = f.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals(LocalDate.of(2025, 12, 15).atStartOfDay(), segment.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 16).atStartOfDay(), segment.endExclusive());
		assertEquals(new Fraction(1, 2), segment.getValue());
		assertEquals(TimeDirection.FORWARD, segment.getTimeDirection());
		assertEquals("FORWARD ]2025-12-15T00:00, 2025-12-16T00:00[ value=1/2", segment.toString());
	}

	@Test
	void segmentAt_forwardAtMidnight_returnsSegmentStartingAtThatDay() {
		// Given: Default 100%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ONE);
		LocalDateTime instant = LocalDate.of(2025, 12, 10).atStartOfDay();

		// When/Then: Segment starts at that midnight
		Segment segment = f.segmentAt(instant, TimeDirection.FORWARD);
		assertEquals(instant, segment.startExclusive());
		assertEquals(instant.plusDays(1), segment.endExclusive());
		assertEquals(TimeDirection.FORWARD, segment.getTimeDirection());
		assertEquals("FORWARD ]2025-12-10T00:00, 2025-12-11T00:00[ value=1", segment.toString());
	}

	@Test
	void segmentAt_backward_returnsBackwardSegmentForCurrentDay() {
		// Given: Dec 14 at 75%, Dec 15 at 50%, default 100%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ONE)
				.withDay(LocalDate.of(2025, 12, 14), new Fraction(3, 4))
				.withDay(LocalDate.of(2025, 12, 15), new Fraction(1, 2));

		// When/Then: Backward from noon gives backward segment for Dec 15
		final LocalDateTime noon = LocalDateTime.of(2025, 12, 15, 12, 0);
		final Segment segment = f.segmentAt(noon, TimeDirection.BACKWARD);
		assertEquals(LocalDate.of(2025, 12, 16).atStartOfDay(), segment.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 15).atStartOfDay(), segment.endExclusive());
		assertEquals(new Fraction(1, 2), segment.getValue());
		assertEquals(TimeDirection.BACKWARD, segment.getTimeDirection());
		assertEquals("BACKWARD ]2025-12-16T00:00, 2025-12-15T00:00[ value=1/2", segment.toString());

		assertEquals("FORWARD ]2025-12-15T00:00, 2025-12-16T00:00[ value=1/2",
				f.segmentAt(noon, TimeDirection.FORWARD).toString());

		// At midnight: forward gives Dec 15, backward gives Dec 14
		final LocalDateTime midnight = LocalDate.of(2025, 12, 15).atStartOfDay();
		assertEquals("FORWARD ]2025-12-15T00:00, 2025-12-16T00:00[ value=1/2",
				f.segmentAt(midnight, TimeDirection.FORWARD).toString());
		assertEquals("BACKWARD ]2025-12-15T00:00, 2025-12-14T00:00[ value=3/4",
				f.segmentAt(midnight, TimeDirection.BACKWARD).toString());
	}

	@Test
	void segmentAt_backwardAtMidnight_returnsSegmentForPreviousDay() {
		// Given: Dec 9 at 66%, Dec 10 at 100%, default 0%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 9), new Fraction(2, 3))
				.withDay(LocalDate.of(2025, 12, 10), Fraction.ONE);
		LocalDateTime instant = LocalDate.of(2025, 12, 10).atStartOfDay();

		// When/Then: Backward at midnight Dec 10 gives Dec 9
		Segment segment = f.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals(LocalDate.of(2025, 12, 10).atStartOfDay(), segment.startExclusive());
		assertEquals(LocalDate.of(2025, 12, 9).atStartOfDay(), segment.endExclusive());
		assertEquals(new Fraction(2, 3), segment.getValue());
		assertEquals(TimeDirection.BACKWARD, segment.getTimeDirection());
		assertEquals("BACKWARD ]2025-12-10T00:00, 2025-12-09T00:00[ value=2/3", segment.toString());
	}

	@Test
	void segmentAt_forwardAndBackward_atMiddayHaveSameValue() {
		// Given: Dec 19 at 30%, Dec 20 at 70%, default 0%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 19), new Fraction(3, 10))
				.withDay(LocalDate.of(2025, 12, 20), new Fraction(7, 10));
		LocalDateTime instant = LocalDateTime.of(2025, 12, 20, 15, 30);

		// When/Then: Both directions give the same value for Dec 20
		Segment forward = f.segmentAt(instant, TimeDirection.FORWARD);
		Segment backward = f.segmentAt(instant, TimeDirection.BACKWARD);
		assertEquals("FORWARD ]2025-12-20T00:00, 2025-12-21T00:00[ value=7/10", forward.toString());
		assertEquals("BACKWARD ]2025-12-21T00:00, 2025-12-20T00:00[ value=7/10", backward.toString());
		assertEquals(new Fraction(7, 10), forward.getValue());
		assertEquals(new Fraction(7, 10), backward.getValue());
	}

	@Test
	void segmentAt_forwardAndBackward_atMidnightHaveDifferentValues() {
		// Given: Dec 24 at 25%, Dec 25 at 50%, default 0%
		PiecewiseConstantSpecificDays f = PiecewiseConstantSpecificDays.of(Fraction.ZERO)
				.withDay(LocalDate.of(2025, 12, 24), new Fraction(1, 4))
				.withDay(LocalDate.of(2025, 12, 25), new Fraction(1, 2));
		final LocalDateTime midnight = LocalDate.of(2025, 12, 25).atStartOfDay();

		// When/Then: At midnight, forward gives Dec 25, backward gives Dec 24
		final Segment forward = f.segmentAt(midnight, TimeDirection.FORWARD);
		final Segment backward = f.segmentAt(midnight, TimeDirection.BACKWARD);
		assertEquals("FORWARD ]2025-12-25T00:00, 2025-12-26T00:00[ value=1/2", forward.toString());
		assertEquals("BACKWARD ]2025-12-25T00:00, 2025-12-24T00:00[ value=1/4", backward.toString());
		assertEquals(new Fraction(1, 2), forward.getValue());
		assertEquals(new Fraction(1, 4), backward.getValue());
	}

}
