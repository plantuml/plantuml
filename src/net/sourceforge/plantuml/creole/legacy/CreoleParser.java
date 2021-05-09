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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.creole.CreoleContext;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBuilder;
import net.sourceforge.plantuml.creole.Stripe;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorRuntimeException;

public class CreoleParser implements SheetBuilder {

	private final FontConfiguration fontConfiguration;
	private final ISkinSimple skinParam;
	private final HorizontalAlignment horizontalAlignment;
	private final CreoleMode creoleMode;
	private final FontConfiguration stereotype;

	public CreoleParser(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple skinParam, CreoleMode creoleMode, FontConfiguration stereotype) {
		this.stereotype = stereotype;
		this.creoleMode = creoleMode;
		this.fontConfiguration = fontConfiguration;
		this.skinParam = Objects.requireNonNull(skinParam);
		this.horizontalAlignment = horizontalAlignment;
	}

	private Stripe createStripe(String line, CreoleContext context, Stripe lastStripe,
			FontConfiguration fontConfiguration) {
		if (lastStripe instanceof StripeCode) {
			final StripeCode code = (StripeCode) lastStripe;
			if (code.isTerminated()) {
				lastStripe = null;
			} else {
				final boolean terminated = code.addAndCheckTermination(line);
				return null;
			}
		}

		if (lastStripe instanceof StripeTable && isTableLine(line)) {
			final StripeTable table = (StripeTable) lastStripe;
			table.analyzeAndAddLine(line);
			return null;
		} else if (lastStripe instanceof StripeTree && Parser.isTreeStart(StringUtils.trinNoTrace(line))) {
			final StripeTree tree = (StripeTree) lastStripe;
			tree.analyzeAndAdd(line);
			return null;
		} else if (isTableLine(line)) {
			return new StripeTable(fontConfiguration, skinParam, line);
		} else if (Parser.isTreeStart(line)) {
			return new StripeTree(fontConfiguration, skinParam, line);
		} else if (Parser.isCodeStart(line)) {
			return new StripeCode(fontConfiguration.changeFamily(Parser.MONOSPACED), skinParam, line);
		}
		return new CreoleStripeSimpleParser(line, context, fontConfiguration, skinParam, creoleMode)
				.createStripe(context);
	}

	public static boolean isTableLine(String line) {
		return line.matches("^(\\<#\\w+(,#?\\w+)?\\>)?\\|(\\=)?.*\\|$");
	}

	public static boolean doesStartByColor(String line) {
		return line.matches("^\\=?\\s*(\\<#\\w+(,#?\\w+)?\\>).*");
	}

	public Sheet createSheet(Display display) {
		final Sheet sheet = new Sheet(horizontalAlignment);
		if (Display.isNull(display) == false) {
			final CreoleContext context = new CreoleContext();
			for (CharSequence cs : display) {
				final Stripe stripe;
				if (cs instanceof EmbeddedDiagram) {
					final Atom atom = ((EmbeddedDiagram) cs).asDraw(skinParam);
					stripe = new Stripe() {
						public Atom getLHeader() {
							return null;
						}

						public List<Atom> getAtoms() {
							return Arrays.asList(atom);
						}
					};
				} else if (cs instanceof Stereotype) {
					for (String st : ((Stereotype) cs).getLabels(skinParam.guillemet())) {
						sheet.add(createStripe(st, context, sheet.getLastStripe(), stereotype));
					}
					continue;
				} else {
					stripe = createStripe(cs.toString(), context, sheet.getLastStripe(), fontConfiguration);
				}
				if (stripe != null) {
					sheet.add(stripe);
				}
			}
		}
		return sheet;
	}

	public static void checkColor(Display result) throws NoSuchColorException {
		FontConfiguration fc = FontConfiguration.blackBlueTrue(UFont.byDefault(10));
		try {
			new CreoleParser(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty(), CreoleMode.FULL, fc)
					.createSheet(result);
		} catch (NoSuchColorRuntimeException e) {
			throw new NoSuchColorException();
		}
	}
}
