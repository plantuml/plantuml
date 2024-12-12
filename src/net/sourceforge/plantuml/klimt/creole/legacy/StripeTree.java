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

import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.creole.CreoleContext;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Stripe;
import net.sourceforge.plantuml.klimt.creole.StripeStyle;
import net.sourceforge.plantuml.klimt.creole.StripeStyleType;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.atom.AtomTree;
import net.sourceforge.plantuml.klimt.creole.atom.AtomWithMargin;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.style.ISkinSimple;

public class StripeTree implements Stripe {
    // ::remove folder when __HAXE__

	private FontConfiguration fontConfiguration;
	final private ISkinSimple skinParam;
	final private AtomTree tree;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.TREE, 0, '\0');

	public StripeTree(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
		this.fontConfiguration = fontConfiguration;
		this.skinParam = skinParam;
		this.tree = new AtomTree(fontConfiguration.getColor());
		this.marged = new AtomWithMargin(tree, 2, 2);
		analyzeAndAdd(line);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(marged);
	}

	public Atom getLHeader() {
		return null;
	}

	public void analyzeAndAdd(String line) {
		final List<String> lines = StripeTable.getWithNewlinesInternal(line);
		for (String s : lines) {
			final StripeSimple cell = new StripeSimple(fontConfiguration, stripeStyle, new CreoleContext(), skinParam,
					CreoleMode.FULL);
			final String text = s.replaceFirst("^\\s*\\|_", "");
			final int level = computeLevel(s);
			cell.analyzeAndAdd(text);
			this.tree.addCell(StripeTable.asAtom(Collections.singletonList(cell), 0), level);
		}

	}

	
	@JawsStrange
	private int computeLevel(String s) {
		int result = 1;
		while (s.length() > 0) {
			if (s.startsWith("  ")) {
				result++;
				s = s.substring(2);
				continue;
			} else if (s.startsWith("\t")) {
				result++;
				s = s.substring(1);
				continue;
			}
			return result;
		}
		return result;
	}

}
