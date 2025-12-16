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

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class SegmentTest {
	@Test
	void correctCreationOfSegment() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);

		Segment segment = new Segment(start, end, value);

		assertThat(segment.getStartInclusive()).isEqualTo(start);
		assertThat(segment.getEndExclusive()).isEqualTo(end);
		assertThat(segment.getValue()).isEqualTo(value);
	}
	
	@Test
	void startCanNotBeNull() throws Exception {
		LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> new Segment(null, end, value))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void endCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> new Segment(start, null, value))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test
	void valueCanNotBeNull() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 17, 0);
		
		assertThatThrownBy(() -> new Segment(start, end, null))
			.isInstanceOf(NullPointerException.class);
	}
	
	@Test 
	void startMustBeBeforeEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 17, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> new Segment(start, end, value))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test 
	void startMustNotBeEqualToEnd() throws Exception {
		LocalDateTime start = LocalDateTime.of(2024, 2, 1, 15, 0);
		LocalDateTime end = LocalDateTime.of(2024, 2, 1, 15, 0);
		Fraction value = Fraction.of(1);
		
		assertThatThrownBy(() -> new Segment(start, end, value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}