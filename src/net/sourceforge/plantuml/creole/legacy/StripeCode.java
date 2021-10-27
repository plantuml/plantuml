/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.creole.legacy;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Stripe;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class StripeCode implements Stripe, Atom {

	final private FontConfiguration fontConfiguration;
	private final List<String> raw = new ArrayList<>();

	private boolean terminated;

	public StripeCode(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
//		this.skinParam = skinParam;
		this.fontConfiguration = fontConfiguration;
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(this);
	}

	public Atom getLHeader() {
		return null;
	}

	public boolean addAndCheckTermination(String line) {
		if (Parser.isCodeEnd(line)) {
			this.terminated = true;
			return true;
		}
		this.raw.add(line);
		return false;
	}

	public final boolean isTerminated() {
		return terminated;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (String s : raw) {
			final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
			width = Math.max(width, dim.getWidth());
			height += dim.getHeight();
		}
		return new Dimension2DDouble(width, height);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		double y = 0;
		for (String s : raw) {
			final UText shape = new UText(s, fontConfiguration);
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
			y += dim.getHeight();
			ug.apply(UTranslate.dy(y - shape.getDescent(stringBounder))).draw(shape);
		}
	}

	public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
		return Arrays.asList((Atom) this);
	}

}
