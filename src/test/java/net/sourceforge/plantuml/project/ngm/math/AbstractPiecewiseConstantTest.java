package net.sourceforge.plantuml.project.ngm.math;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class AbstractPiecewiseConstantTest {

	@Test
	void forwardIterationPerDay() throws Exception {
		PiecewiseConstant pc = new DailyPiecewiseConstant();
		
		Iterator<Segment> iterator = pc.iterateSegmentsFrom(LocalDateTime.of(2024, 6, 1, 10, 30));
		
		Segment s1 = iterator.next();
		assertThat(s1.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 1, 0, 0));
		assertThat(s1.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 2, 0, 0));
		
		Segment s2 = iterator.next();
		assertThat(s2.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 2, 0, 0));
		assertThat(s2.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 3, 0, 0));

		Segment s3 = iterator.next();
		assertThat(s3.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 3, 0, 0));
		assertThat(s3.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 4, 0, 0));
	}
	
	@Test
	void backwordIterationPerDay() throws Exception {
		PiecewiseConstant pc = new DailyPiecewiseConstant();
		
		Iterator<Segment> iterator = pc.iterateSegmentsBackwardFrom(LocalDateTime.of(2024, 6, 14, 10, 30));
		
		Segment s1 = iterator.next();
		assertThat(s1.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 14, 0, 0));
		assertThat(s1.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 15, 0, 0));
		
		Segment s2 = iterator.next();
		assertThat(s2.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 13, 0, 0));
		assertThat(s2.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 14, 0, 0));
		
		Segment s3 = iterator.next();
		assertThat(s3.aInclusive()).isEqualTo(LocalDateTime.of(2024, 6, 12, 0, 0));
		assertThat(s3.bExclusive()).isEqualTo(LocalDateTime.of(2024, 6, 13, 0, 0));
	}
	
	private static class DailyPiecewiseConstant extends AbstractPiecewiseConstant {

		@Override
		public Segment segmentAt(LocalDateTime instant) {
			LocalDateTime start = instant.toLocalDate().atStartOfDay();
			LocalDateTime end = start.plusDays(1);
			return Segment.forward(start, end, Fraction.of(1));
		}

		@Override
		public Fraction apply(LocalDateTime instant) {
			return Fraction.of(1);
		}
		
	}
}
