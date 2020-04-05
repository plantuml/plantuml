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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.LineLocationImpl;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockSprited;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Display implements Iterable<CharSequence> {

	private final List<CharSequence> displayData;
	private final HorizontalAlignment naturalHorizontalAlignment;
	private final boolean isNull;
	private final CreoleMode defaultCreoleMode;

	public final static Display NULL = new Display(null, null, true, CreoleMode.FULL);

	public Display withoutStereotypeIfNeeded(Style usedStyle) {
		final boolean showStereotype = usedStyle.value(PName.ShowStereotype).asBoolean();
		if (showStereotype) {
			return this;
		}
		final List<CharSequence> copy = new ArrayList<CharSequence>(displayData);
		final Display result = new Display(naturalHorizontalAlignment, isNull, defaultCreoleMode);
		for (Iterator<CharSequence> it = copy.iterator(); it.hasNext();) {
			final CharSequence cs = it.next();
			if (cs instanceof Stereotype && usedStyle.getSignature().match(((Stereotype) cs))) {
				it.remove();
			}
		}
		result.displayData.addAll(copy);
		return result;
	}

	public Stereotype getStereotypeIfAny() {
		for (CharSequence cs : displayData) {
			if (cs instanceof Stereotype) {
				return (Stereotype) cs;
			}
		}
		return null;

	}

	public Display replaceBackslashT() {
		final Display result = new Display(this, defaultCreoleMode);
		for (int i = 0; i < result.displayData.size(); i++) {
			final CharSequence s = displayData.get(i);
			if (s.toString().contains("\\t")) {
				result.displayData.set(i, s.toString().replace("\\t", "\t"));
			}
		}
		return result;
	}

	public Display replace(String src, String dest) {
		final List<CharSequence> newDisplay = new ArrayList<CharSequence>();
		for (CharSequence cs : displayData) {
			if (cs.toString().contains(src)) {
				cs = cs.toString().replace(src, dest);
			}
			newDisplay.add(cs);
		}
		return new Display(newDisplay, naturalHorizontalAlignment, isNull, defaultCreoleMode);
	}

	public boolean isWhite() {
		return displayData == null || displayData.size() == 0
				|| (displayData.size() == 1 && displayData.get(0).toString().matches("\\s*"));
	}

	public static Display empty() {
		return new Display((HorizontalAlignment) null, false, CreoleMode.FULL);
	}

	public static Display create(CharSequence... s) {
		return create(Arrays.asList(s));
	}

	public static Display createFoo(List<StringLocated> data) {
		final List<CharSequence> tmp = new ArrayList<CharSequence>();
		for (StringLocated s : data) {
			tmp.add(s.getString());
		}
		return create(tmp);
	}

	public static Display create(Collection<? extends CharSequence> other) {
		return new Display(other, null, false, CreoleMode.FULL);
	}

	public static Display getWithNewlines(Code s) {
		return getWithNewlines(s.getName());
	}

	public static Display getWithNewlines(String s) {
		if (s == null) {
			// Thread.dumpStack();
			return NULL;
		}
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		HorizontalAlignment naturalHorizontalAlignment = null;
		boolean rawMode = false;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			final String sub = s.substring(i);
			if (sub.startsWith("<math>") || sub.startsWith("<latex>") || sub.startsWith("[[")) {
				rawMode = true;
			} else if (sub.startsWith("</math>") || sub.startsWith("</latex>") || sub.startsWith("]]")) {
				rawMode = false;
			}
			if (rawMode == false && c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n' || c2 == 'r' || c2 == 'l') {
					if (c2 == 'r') {
						naturalHorizontalAlignment = HorizontalAlignment.RIGHT;
					} else if (c2 == 'l') {
						naturalHorizontalAlignment = HorizontalAlignment.LEFT;
					}
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == 't') {
					current.append('\t');
				} else if (c2 == '\\') {
					current.append(c2);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else if (c == BackSlash.hiddenNewLine()) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return new Display(result, naturalHorizontalAlignment, false, CreoleMode.FULL);
	}

	private Display(Display other, CreoleMode mode) {
		this(other.naturalHorizontalAlignment, other.isNull, mode);
		this.displayData.addAll(other.displayData);
	}

	private Display(HorizontalAlignment naturalHorizontalAlignment, boolean isNull, CreoleMode defaultCreoleMode) {
		this.defaultCreoleMode = defaultCreoleMode;
		this.isNull = isNull;
		this.displayData = isNull ? null : new ArrayList<CharSequence>();
		this.naturalHorizontalAlignment = isNull ? null : naturalHorizontalAlignment;
	}

	private Display(Collection<? extends CharSequence> other, HorizontalAlignment naturalHorizontalAlignment,
			boolean isNull, CreoleMode defaultCreoleMode) {
		this(naturalHorizontalAlignment, isNull, defaultCreoleMode);
		if (isNull == false) {
			this.displayData.addAll(manageEmbeddedDiagrams(other));
		}
	}

	private static List<CharSequence> manageEmbeddedDiagrams(final Collection<? extends CharSequence> strings) {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		final Iterator<? extends CharSequence> it = strings.iterator();
		while (it.hasNext()) {
			CharSequence s = it.next();
			if (s != null && StringUtils.trin(s.toString()).equals("{{")) {
				final List<CharSequence> other = new ArrayList<CharSequence>();
				other.add("@startuml");
				while (it.hasNext()) {
					CharSequence s2 = it.next();
					if (s2 != null && StringUtils.trin(s2.toString()).equals("}}")) {
						break;
					}
					other.add(s2);
				}
				other.add("@enduml");
				s = new EmbeddedDiagram(Display.create(other));
			}
			result.add(s);
		}
		return result;
	}

	public Display manageGuillemet() {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		boolean first = true;
		for (CharSequence line : displayData) {
			if (line instanceof EmbeddedDiagram) {
				result.add(line);
			} else {
				String lineString = line.toString();
				if (first && VisibilityModifier.isVisibilityCharacter(line)) {
					lineString = lineString.substring(1).trim();
				}
				final String withGuillement = Guillemet.GUILLEMET.manageGuillemet(lineString);
				result.add(withGuillement);
			}
			first = false;
		}
		return new Display(result, this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
	}

	public Display withPage(int page, int lastpage) {
		if (displayData == null) {
			return this;
		}
		final List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence line : displayData) {
			line = line.toString().replace("%page%", "" + page);
			line = line.toString().replace("%lastpage%", "" + lastpage);
			result.add(line);
		}
		return new Display(result, this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
	}

	public Display removeEndingStereotype() {
		final Matcher2 m = patternStereotype.matcher(displayData.get(displayData.size() - 1));
		if (m.matches()) {
			final List<CharSequence> result = new ArrayList<CharSequence>(this.displayData);
			result.set(result.size() - 1, m.group(1));
			return new Display(result, this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
		}
		return this;
	}

	public final static Pattern2 patternStereotype = MyPattern.cmpile("^(.*?)(?:\\<\\<\\s*(.*)\\s*\\>\\>)\\s*$");

	public String getEndingStereotype() {
		final Matcher2 m = patternStereotype.matcher(displayData.get(displayData.size() - 1));
		if (m.matches()) {
			return m.group(2);
		}
		return null;
	}

	public Display underlined() {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence line : displayData) {
			result.add("<u>" + line);
		}
		return new Display(result, this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
	}

	public Display withCreoleMode(CreoleMode mode) {
		if (isNull) {
			throw new IllegalArgumentException();
		}
		return new Display(this, mode);
	}

	@Override
	public String toString() {
		if (isNull) {
			return "NULL";
		}
		return displayData.toString();
	}

	@Override
	public int hashCode() {
		return displayData.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return this.displayData.equals(((Display) other).displayData);
	}

	public Display addAll(Display other) {
		final Display result = new Display(this, this.defaultCreoleMode);
		result.displayData.addAll(other.displayData);
		return result;
	}

	public Display addFirst(CharSequence s) {
		final Display result = new Display(this, this.defaultCreoleMode);
		result.displayData.add(0, s);
		return result;
	}

	public Display add(CharSequence s) {
		final Display result = new Display(this, this.defaultCreoleMode);
		result.displayData.add(s);
		return result;
	}

	public Display addGeneric(CharSequence s) {
		final Display result = new Display(this, this.defaultCreoleMode);
		final int size = displayData.size();
		if (size == 0) {
			result.displayData.add("<" + s + ">");
		} else {
			result.displayData.set(size - 1, displayData.get(size - 1) + "<" + s + ">");
		}
		return result;
	}

	public int size() {
		if (isNull) {
			return 0;
		}
		return displayData.size();
	}

	public CharSequence get(int i) {
		return displayData.get(i);
	}

	public Iterator<CharSequence> iterator() {
		return Collections.unmodifiableList(displayData).iterator();
	}

	public Display subList(int i, int size) {
		return new Display(displayData.subList(i, size), this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	public List<? extends CharSequence> as() {
		return Collections.unmodifiableList(displayData);
	}

	public List<StringLocated> as2() {
		final List<StringLocated> result = new ArrayList<StringLocated>();
		LineLocationImpl location = new LineLocationImpl("inner", null);
		for (CharSequence cs : displayData) {
			location = location.oneLineRead();
			result.add(new StringLocated(cs.toString(), location));
		}
		return Collections.unmodifiableList(result);
	}

	public boolean hasUrl() {
		final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.ANYWHERE);
		for (CharSequence s : this) {
			if (urlBuilder.getUrl(s.toString()) != null) {
				return true;
			}
		}
		return false;
	}

	public HorizontalAlignment getNaturalHorizontalAlignment() {
		return naturalHorizontalAlignment;
	}

	public List<Display> splitMultiline(Pattern2 separator) {
		final List<Display> result = new ArrayList<Display>();
		Display pending = new Display(this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
		result.add(pending);
		for (CharSequence line : displayData) {
			final Matcher2 m = separator.matcher(line);
			if (m.find()) {
				final CharSequence s1 = line.subSequence(0, m.start());
				pending.displayData.add(s1);
				final CharSequence s2 = line.subSequence(m.end(), line.length());
				pending = new Display(this.naturalHorizontalAlignment, this.isNull, this.defaultCreoleMode);
				result.add(pending);
				pending.displayData.add(s2);
			} else {
				pending.displayData.add(line);
			}
		}
		return Collections.unmodifiableList(result);
	}

	// ------

	public static boolean isNull(Display display) {
		// if (display == null) {
		// throw new IllegalArgumentException();
		// }
		return display == null || display.isNull;
	}

	public TextBlock create(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer) {
		return create7(fontConfiguration, horizontalAlignment, spriteContainer, CreoleMode.FULL);
	}

	public TextBlock createWithNiceCreoleMode(FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer) {
		return create7(fontConfiguration, horizontalAlignment, spriteContainer, defaultCreoleMode);
	}

	public TextBlock create7(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, CreoleMode creoleMode) {
		return create0(fontConfiguration, horizontalAlignment, spriteContainer, LineBreakStrategy.NONE, creoleMode,
				null, null);
	}

	public TextBlock create8(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, CreoleMode modeSimpleLine, LineBreakStrategy maxMessageSize) {
		return create0(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, modeSimpleLine, null,
				null);
	}

	public TextBlock create9(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize) {
		return create0(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, defaultCreoleMode, null,
				null);
	}

	public TextBlock create0(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, CreoleMode creoleMode,
			UFont fontForStereotype, HColor htmlColorForStereotype) {
		if (maxMessageSize == null) {
			throw new IllegalArgumentException();
		}
		if (getNaturalHorizontalAlignment() != null) {
			horizontalAlignment = getNaturalHorizontalAlignment();
		}
		final FontConfiguration stereotypeConfiguration = fontConfiguration.forceFont(fontForStereotype,
				htmlColorForStereotype);
		if (size() > 0) {
			if (get(0) instanceof Stereotype) {
				return createStereotype(fontConfiguration, horizontalAlignment, spriteContainer, 0, fontForStereotype,
						htmlColorForStereotype, maxMessageSize, creoleMode);
			}
			if (get(size() - 1) instanceof Stereotype) {
				return createStereotype(fontConfiguration, horizontalAlignment, spriteContainer, size() - 1,
						fontForStereotype, htmlColorForStereotype, maxMessageSize, creoleMode);
			}
			if (get(0) instanceof MessageNumber) {
				return createMessageNumber(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize,
						stereotypeConfiguration);
			}
		}

		return getCreole(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, creoleMode,
				stereotypeConfiguration);
	}

	private TextBlock createStereotype(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			SpriteContainer spriteContainer, int position, UFont fontForStereotype, HColor htmlColorForStereotype,
			LineBreakStrategy maxMessageSize, CreoleMode creoleMode) {
		final Stereotype stereotype = (Stereotype) get(position);
		TextBlock circledCharacter = null;
		if (stereotype.isSpotted()) {
			circledCharacter = new CircledCharacter(stereotype.getCharacter(), stereotype.getRadius(),
					stereotype.getCircledFont(), stereotype.getHtmlColor(), null, fontConfiguration.getColor());
		} else {
			circledCharacter = stereotype.getSprite(spriteContainer);
		}
		final FontConfiguration stereotypeConfiguration = fontConfiguration.forceFont(fontForStereotype,
				htmlColorForStereotype);
		final TextBlock result = getCreole(fontConfiguration, horizontalAlignment, (ISkinSimple) spriteContainer,
				maxMessageSize, creoleMode, stereotypeConfiguration);
		if (circledCharacter != null) {
			return new TextBlockSprited(circledCharacter, result);
		}
		return result;
	}

	private TextBlock getCreole(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, CreoleMode creoleMode,
			FontConfiguration stereotypeConfiguration) {
		final Sheet sheet = new CreoleParser(fontConfiguration, horizontalAlignment, spriteContainer, creoleMode,
				stereotypeConfiguration).createSheet(this);
		final double padding = spriteContainer == null ? 0 : spriteContainer.getPadding();
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, maxMessageSize, padding);
		return new SheetBlock2(sheetBlock1, sheetBlock1, new UStroke(1.5));
	}

	private TextBlock createMessageNumber(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, FontConfiguration stereotypeConfiguration) {
		TextBlock tb1 = subList(0, 1).getCreole(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize,
				CreoleMode.FULL, stereotypeConfiguration);
		tb1 = TextBlockUtils.withMargin(tb1, 0, 4, 0, 0);
		final TextBlock tb2 = subList(1, size()).getCreole(fontConfiguration, horizontalAlignment, spriteContainer,
				maxMessageSize, CreoleMode.FULL, stereotypeConfiguration);
		return TextBlockUtils.mergeLR(tb1, tb2, VerticalAlignment.CENTER);

	}

}
