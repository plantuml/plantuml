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
 * Original Author:  Arnaud Roques, Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Utilities to combine multiple {@link PiecewiseConstant} functions.
 *
 * <p>
 * In the New Gantt Model context, {@link PiecewiseConstant} typically
 * represents a time-dependent workload allocation expressed as a
 * {@link Fraction}.
 * </p>
 *
 * <p>
 * This class is also a {@link PiecewiseConstant}: it can represent the
 * combination of several piecewise-constant functions using a combiner
 * operation (sum, product, min, max...).
 * </p>
 */
public class Combiner extends AbstractPiecewiseConstant {

	private final List<PiecewiseConstant> elements;

	private final BiFunction<Fraction, Fraction, Fraction> operation;

	private Combiner(BiFunction<Fraction, Fraction, Fraction> operation, PiecewiseConstant... elements) {
		this.elements = Arrays.asList(elements);
		this.operation = operation;
	}

	public static PiecewiseConstant sum(PiecewiseConstant... elements) {
		if (elements.length == 1)
			return elements[0];
		return new Combiner(Fraction.SUM, elements);
	}

	public static PiecewiseConstant min(PiecewiseConstant... elements) {
		if (elements.length == 1)
			return elements[0];
		return new Combiner(Fraction.MIN, elements);
	}

	public static PiecewiseConstant max(PiecewiseConstant... elements) {
		if (elements.length == 1)
			return elements[0];
		return new Combiner(Fraction.MAX, elements);
	}

	public static PiecewiseConstant product(PiecewiseConstant... elements) {
		if (elements.length == 1)
			return elements[0];
		return new Combiner(Fraction.PRODUCT, elements);
	}

	@Override
	public Segment segmentAt(LocalDateTime instant, TimeDirection direction) {
		if (elements.size() < 2)
			throw new IllegalStateException("At least two functions are required for combination");

		final List<Segment> segments = elements.stream().map(f -> f.segmentAt(instant, direction))
				.collect(Collectors.toList());

		return Segment.intersection(segments, operation);
	}

}
