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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.creole.CreoleContext;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBuilder;
import net.sourceforge.plantuml.klimt.creole.Stripe;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinSimple;

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

	private List<Stripe> createStripes(String line, CreoleContext context, Stripe lastStripe,
			FontConfiguration fontConfiguration) {
		if (lastStripe instanceof StripeRaw) {
			final StripeRaw code = (StripeRaw) lastStripe;
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
			return Arrays.asList(new StripeTable(fontConfiguration, skinParam, line));
		} else if (Parser.isTreeStart(line)) {
			return Arrays.asList(new StripeTree(fontConfiguration, skinParam, line));
		} else if (Parser.isCodeStart(line)) {
			return Arrays.asList(new StripeCode(fontConfiguration.changeFamily(Parser.MONOSPACED)));
			// ::comment when __CORE__
		} else if (Parser.isLatexStart(line)) {
			return Arrays.asList(new StripeLatex(fontConfiguration));
			// ::done
		}
		return new CreoleStripeSimpleParser(line, context, fontConfiguration, skinParam, creoleMode)
				.createStripes(context);
	}

	public static boolean isTableLine(String line) {
		return line.matches("^(\\<#\\w+(,#?\\w+)?\\>)?\\|(\\=)?.*\\|$");
	}

	public static boolean doesStartByColor(String line) {
		return line.matches("^\\=?\\s*(\\<#\\w+(,#?\\w+)?\\>).*");
	}

	private final Map<Display, Sheet> cache = new HashMap<>();

	public Sheet createSheet(Display display) {
		if (Jaws.TRACE)
			System.err.println("createSheet " + display);
		Sheet result = cache.get(display);
		if (result == null) {
			result = createSheetSlow(display, false);
			cache.put(display, result);
		}
		return result;
	}

	private Sheet createSheetSlow(Display display, boolean checkColor) {
		final Sheet sheet = new Sheet(horizontalAlignment);
		if (Display.isNull(display) == false) {
			final CreoleContext context = new CreoleContext();
			final Iterator<CharSequence> it = display.iterator();
			while (it.hasNext()) {
				final CharSequence cs = it.next();
				if (Jaws.TRACE)
					System.err.println("createSheetSlow:" + cs);
				final List<Stripe> stripes;
				final String type = EmbeddedDiagram.getEmbeddedType(StringUtils.trinNoTrace(cs));
				if (type != null) {
					final Atom embeddedDiagram = EmbeddedDiagram.createAndSkip(type, it, skinParam);
					if (checkColor)
						stripes = null;
					else {
						stripes = Arrays.asList(new Stripe() {
							public Atom getLHeader() {
								return null;
							}

							public List<Atom> getAtoms() {
								return Arrays.asList(embeddedDiagram);
							}
						});
					}
				} else if (cs instanceof Stereotype) {
					if (display.showStereotype())
						for (String st : ((Stereotype) cs).getLabels(skinParam.guillemet()))
							sheet.add(createStripes(st, context, sheet.getLastStripe(), stereotype));

					continue;
				} else {
					stripes = createStripes(skinParam.guillemet().manageGuillemet(cs.toString()), context,
							sheet.getLastStripe(), fontConfiguration);
				}

				if (stripes != null)
					sheet.add(stripes);

			}
		}
		return sheet;
	}

//	public static void checkColor(Display result) throws NoSuchColorException {
//		FontConfiguration fc = FontConfiguration.blackBlueTrue(UFont.byDefault(10));
//		try {
//			new CreoleParser(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty(), CreoleMode.FULL, fc)
//					.createSheetSlow(result, true);
//		} catch (NoSuchColorRuntimeException e) {
//			throw new NoSuchColorException();
//		}
//	}
}
