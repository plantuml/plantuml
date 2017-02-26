/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;

public class Fission {

	private final Stripe stripe;
	private final LineBreakStrategy maxWidth;

	public Fission(Stripe stripe, LineBreakStrategy maxWidth) {
		this.stripe = stripe;
		this.maxWidth = maxWidth;
		if (maxWidth == null) {
			throw new IllegalArgumentException();
		}
	}

	public List<Stripe> getSplitted(StringBounder stringBounder) {
		final double valueMaxWidth = maxWidth.getMathWidth();
		if (valueMaxWidth == 0) {
			return Arrays.asList(stripe);
		}
		final List<Stripe> result = new ArrayList<Stripe>();
		StripeSimple current = new StripeSimple();
		for (Atom atom : stripe.getAtoms()) {
			for (Atom atomSplitted : getSplitted(stringBounder, atom)) {
				final double width = atomSplitted.calculateDimension(stringBounder).getWidth();
				if (current.totalWidth + width > valueMaxWidth) {
					result.add(current);
					current = new StripeSimple();
				}
				current.addAtom(atomSplitted, width);
			}
		}
		if (current.totalWidth > 0) {
			result.add(current);
		}
		return Collections.unmodifiableList(result);
	}

	private Collection<? extends Atom> getSplitted(StringBounder stringBounder, Atom atom) {
		if (atom instanceof AtomText) {
			return ((AtomText) atom).getSplitted(stringBounder, maxWidth);
		}
		return Collections.singleton(atom);
	}

	private List<Stripe> getSplittedSimple() {
		final StripeSimple result = new StripeSimple();
		for (Atom atom : stripe.getAtoms()) {
			result.addAtom(atom, 0);

		}
		return Arrays.asList((Stripe) result);
	}

	static class StripeSimple implements Stripe {

		private final List<Atom> atoms = new ArrayList<Atom>();
		private double totalWidth;

		public List<Atom> getAtoms() {
			return Collections.unmodifiableList(atoms);
		}

		private void addAtom(Atom atom, double width) {
			this.atoms.add(atom);
			this.totalWidth += width;
		}

	}

}
