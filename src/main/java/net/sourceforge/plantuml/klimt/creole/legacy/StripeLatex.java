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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.creole.Neutron;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.atom.AtomMath;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.math.ScientificEquationSafe;

public class StripeLatex implements StripeRaw {
	// ::remove file when __CORE__
	final private FontConfiguration fontConfiguration;
	final private StringBuilder formula = new StringBuilder();
	private AtomMath atom;

	private boolean terminated;

	public StripeLatex(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(this);
	}

	public Atom getLHeader() {
		return null;
	}

	@Override
	public boolean addAndCheckTermination(String line) {
		if (Parser.isLatexEnd(line)) {
			this.terminated = true;
			return true;
		}
		this.formula.append(line);
		return false;
	}

	@Override
	public final boolean isTerminated() {
		return terminated;
	}

	private Atom getAtom() {
		if (atom == null) {
			final ScientificEquationSafe math = ScientificEquationSafe.fromLatex(formula.toString());
			atom = new AtomMath(math, fontConfiguration.getColor(), fontConfiguration.getExtendedColor());
		}
		return atom;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getAtom().calculateDimension(stringBounder);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		getAtom().drawU(ug);
	}

	@Override
	public List<Neutron> getNeutrons() {
		throw new UnsupportedOperationException();
	}

}
