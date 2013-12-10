/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class StripeTable implements Stripe {

	static enum Mode {
		HEADER, NORMAL
	};

	private FontConfiguration fontConfiguration;
	final private ISkinParam skinParam;
	final private AtomTable table;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	public StripeTable(FontConfiguration fontConfiguration, ISkinParam skinParam, String line) {
		this.fontConfiguration = fontConfiguration;
		this.skinParam = skinParam;
		this.table = new AtomTable(fontConfiguration.getColor());
		this.marged = new AtomWithMargin(table, 4, 4);
		analyzeAndAddInternal(line, Mode.HEADER);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom> singletonList(marged);
	}

	private static Atom asAtom(StripeSimple stripe) {
		final Sheet sheet = new Sheet();
		sheet.add(stripe);
		return new SheetBlock(sheet, null, new UStroke());
	}

	private void analyzeAndAddInternal(String line, Mode mode) {
		table.newLine();
		for (final StringTokenizer st = new StringTokenizer(line, "|"); st.hasMoreTokens();) {
			String v = st.nextToken();
			if (mode == Mode.HEADER && v.startsWith("=")) {
				v = v.substring(1);
			}
			final StripeSimple cell = new StripeSimple(getFontConfiguration(mode), stripeStyle, new CreoleContext(),
					skinParam);
			cell.analyzeAndAdd(v);
			table.addCell(asAtom(cell));
		}
	}

	private FontConfiguration getFontConfiguration(Mode mode) {
		if (mode == Mode.NORMAL) {
			return fontConfiguration;
		}
		return fontConfiguration.bold();
	}

	public void analyzeAndAddNormal(String line) {
		analyzeAndAddInternal(line, Mode.NORMAL);
	}

}
