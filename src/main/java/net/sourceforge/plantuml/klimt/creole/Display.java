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
package net.sourceforge.plantuml.klimt.creole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsFlags;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.CircledCharacter;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockSprited;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainer;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.Value;
import net.sourceforge.plantuml.style.ValueNull;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.warning.JawsWarning;
import net.sourceforge.plantuml.warning.Warning;

public class Display implements Iterable<CharSequence> {

	private final List<CharSequence> displayData;
	private final HorizontalAlignment naturalHorizontalAlignment;
	private final boolean isNull;
	private final CreoleMode defaultCreoleMode;
	private final boolean showStereotype;

	// Ideally, we should have implemented the Null Object Pattern from the start,
	// but it was not incorporated. We are now gradually removing occurrences of
	// <code>null</code> in the <code>Display</code> logic throughout our codebase.
	// Reference: https://en.wikipedia.org/wiki/Null_object_pattern
	public final static Display NULL = new Display(true, null, null, true, CreoleMode.FULL);

	@Override
	public int hashCode() {
		if (isNull)
			return 42;
		return displayData.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return this.displayData.equals(((Display) other).displayData);
	}

	public boolean equalsLike(Display other) {
		if (isNull(this))
			return isNull(other);
		return this.displayData.equals(other.displayData);
	}

	public boolean showStereotype() {
		return showStereotype;
	}

	public Display withoutStereotypeIfNeeded(Style usedStyle) {
		if (this == NULL)
			return NULL;

		final Value showStereotype = usedStyle.value(PName.ShowStereotype);
		if (showStereotype instanceof ValueNull || showStereotype.asBoolean())
			return this;

		return new Display(false, this, this.defaultCreoleMode);

	}

	public Stereotype getStereotypeIfAny() {
		for (CharSequence cs : displayData)
			if (cs instanceof Stereotype)
				return (Stereotype) cs;

		return null;

	}

	@JawsStrange
	public Display replaceBackslashT() {
		final Display result = new Display(this.showStereotype, this, defaultCreoleMode);
		for (int i = 0; i < result.displayData.size(); i++) {
			final CharSequence s = displayData.get(i);
			if (s.toString().contains("\\t"))
				result.displayData.set(i, s.toString().replace("\\t", "\t"));
		}
		return result;
	}

	public Display replace(String src, String dest) {
		final List<CharSequence> newDisplay = new ArrayList<>();
		for (CharSequence cs : displayData) {
			if (cs.toString().contains(src))
				cs = cs.toString().replace(src, dest);

			newDisplay.add(cs);
		}
		return new Display(showStereotype, newDisplay, naturalHorizontalAlignment, isNull, defaultCreoleMode);
	}

	public boolean isWhite() {
		return displayData == null || displayData.size() == 0
				|| (displayData.size() == 1 && displayData.get(0).toString().matches("\\s*"));
	}

	public static Display empty() {
		return new Display(true, (HorizontalAlignment) null, false, CreoleMode.FULL);
	}

	public static Display create(CharSequence... s) {
		return create(Arrays.asList(s));
	}

	public static Display createFoo(List<StringLocated> data) throws NoSuchColorException {
		final List<CharSequence> tmp = new ArrayList<>();
		for (StringLocated s : data)
			tmp.add(s.getString());

		final Display result = create(tmp);
		// CreoleParser.checkColor(result);
		return result;
	}

//	private static List<String> skip(String type, Iterator<StringLocated> it) {
//		final List<String> result = new ArrayList<String>();
//		result.add("@start" + type);
//		int nested = 1;
//		while (it.hasNext()) {
//			final StringLocated s2 = it.next();
//			result.add(s2.getString());
//			if (s2.getTrimmed().getString().startsWith(EmbeddedDiagram.EMBEDDED_START))
//				nested++;
//			else if (s2.getTrimmed().getString().equals(EmbeddedDiagram.EMBEDDED_END)) {
//				nested--;
//				if (nested == 0)
//					break;
//			}
//		}
//		result.add("@end" + type);
//		return result;
//	}

	public static Display create(Collection<? extends CharSequence> other) {
		return new Display(true, other, null, false, CreoleMode.FULL);
	}

	public static Display getWithNewlines(Quark<Entity> s) {
		return getWithNewlines(Pragma.createEmpty(), s.getName());
	}

	public static Display getWithNewlines2(Pragma pragma, String s) throws NoSuchColorException {
		final Display result = getWithNewlines(pragma, s);
		// CreoleParser.checkColor(result);
		return result;
	}

	public static List<String> getWithNewlines3(CharSequence s) {
		if (s == null)
			return null;

		final List<String> result = new ArrayList<>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == 't') {
					current.append('\t');
				} else if (c2 == '\\') {
					current.append(c2);
				}
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return Collections.unmodifiableList(result);
	}

	private final static Warning MORE_INFO = new Warning("More info on https://plantuml.com/newline");

	public static Display getWithNewlines(Pragma pragma, String s) {
		if (s == null)
			return NULL;

		final List<String> result = new ArrayList<>();
		final StringBuilder current = new StringBuilder();
		HorizontalAlignment naturalHorizontalAlignment = null;
		boolean rawMode = false;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			final String sub = s.substring(i);
			if (sub.startsWith("<math>") || sub.startsWith("<latex>") || sub.startsWith("[["))
				rawMode = true;
			else if (sub.startsWith("</math>") || sub.startsWith("</latex>") || sub.startsWith("]]"))
				rawMode = false;

			if (JawsFlags.SPECIAL_NEWLINE_IN_DISPLAY_CLASS && sub.startsWith("%newline()")) {
				result.add(current.toString());
				current.setLength(0);
				i += 9;
				// throw new IllegalStateException();
			} else if (JawsFlags.SPECIAL_NEWLINE_IN_DISPLAY_CLASS && sub.startsWith("%n()")) {
				result.add(current.toString());
				current.setLength(0);
				i += 3;
				// throw new IllegalStateException();
			} else if (Pragma.legacyReplaceBackslashNByNewline() && rawMode == false && c == '\\'
					&& i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n' || c2 == 'r' || c2 == 'l') {
					if (c2 == 'r') {
						naturalHorizontalAlignment = HorizontalAlignment.RIGHT;
						addWarning(pragma, JawsWarning.BACKSLASH_RIGHT);
					} else if (c2 == 'l') {
						naturalHorizontalAlignment = HorizontalAlignment.LEFT;
						addWarning(pragma, JawsWarning.BACKSLASH_LEFT);
					} else {
						addWarning(pragma, JawsWarning.BACKSLASH_NEWLINE);
					}

					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == 't') {
					current.append('\t');
					addWarning(pragma, JawsWarning.BACKSLASH_TABULATION);
				} else if (c2 == '\\') {
					current.append(c2);
					addWarning(pragma, JawsWarning.BACKSLASH_BACKSLASH);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else if (c == Jaws.BLOCK_E1_REAL_TABULATION) {
				// current.append('\t');
				current.append(c);
			} else if (c == Jaws.BLOCK_E1_REAL_BACKSLASH) {
				current.append('\\');
			} else if (c == Jaws.BLOCK_E1_NEWLINE_LEFT_ALIGN) {
				naturalHorizontalAlignment = HorizontalAlignment.LEFT;
				result.add(current.toString());
				current.setLength(0);
			} else if (c == Jaws.BLOCK_E1_INVISIBLE_QUOTE) {
				// No thing to do, just ignore this character
			} else if (c == Jaws.BLOCK_E1_NEWLINE_RIGHT_ALIGN) {
				naturalHorizontalAlignment = HorizontalAlignment.RIGHT;
				result.add(current.toString());
				current.setLength(0);
			} else if (rawMode == false && c == Jaws.BLOCK_E1_NEWLINE) {
				result.add(current.toString());
				current.setLength(0);
			} else if (c == Jaws.BLOCK_E1_BREAKLINE) {
				// Because of embedded diagrams
				result.add(current.toString());
				current.setLength(0);
			} else if (rawMode == false && c == BackSlash.hiddenNewLine()) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return new Display(true, result, naturalHorizontalAlignment, false, CreoleMode.FULL);
	}

	private static void addWarning(Pragma pragma, JawsWarning warning) {
		if (pragma.isTrue(PragmaKey.SHOW_DEPRECATION)) {
			pragma.addWarning(MORE_INFO);
			pragma.addWarning(warning.toWarning());
		}
	}

	private Display(boolean showStereotype, Display other, CreoleMode mode) {
		this(showStereotype, other.naturalHorizontalAlignment, other.isNull, mode);
		this.displayData.addAll(other.displayData);
	}

	private Display(boolean showStereotype, HorizontalAlignment naturalHorizontalAlignment, boolean isNull,
			CreoleMode defaultCreoleMode) {
		this.showStereotype = showStereotype;
		this.defaultCreoleMode = defaultCreoleMode;
		this.isNull = isNull;
		this.displayData = isNull ? null : new ArrayList<CharSequence>();
		this.naturalHorizontalAlignment = isNull ? null : naturalHorizontalAlignment;
	}

	private Display(boolean showStereotype, Collection<? extends CharSequence> other,
			HorizontalAlignment naturalHorizontalAlignment, boolean isNull, CreoleMode defaultCreoleMode) {
		this(showStereotype, naturalHorizontalAlignment, isNull, defaultCreoleMode);
		if (isNull == false)
			// this.displayData.addAll(manageEmbeddedDiagrams(other));
			this.displayData.addAll(other);

	}

//	private static List<CharSequence> manageEmbeddedDiagrams(final Collection<? extends CharSequence> strings) {
//		final List<CharSequence> result = new ArrayList<>();
//		final Iterator<? extends CharSequence> it = strings.iterator();
//		while (it.hasNext()) {
//			CharSequence s = it.next();
//			if (s instanceof String == false)
//				System.err.println("s=" + s.getClass());
//			final String type = EmbeddedDiagram.getEmbeddedType(s);
//			if (type != null) {
//				final List<CharSequence> other = new ArrayList<>();
//				other.add("@start" + type);
//				int nested = 1;
//				while (it.hasNext()) {
//					final CharSequence s2 = it.next();
//					if (StringUtils.trin(s2.toString()).equals(EmbeddedDiagram.EMBEDDED_END)) {
//						nested--;
//						if (nested == 0)
//							break;
//					}
//					if (StringUtils.trin(s2.toString()).startsWith(EmbeddedDiagram.EMBEDDED_START))
//						nested++;
//
//					other.add(s2);
//				}
//				other.add("@end" + type);
//				s = new EmbeddedDiagram(Display.create(other));
//			}
//			result.add(s);
//		}
//		return result;
//	}

	public Display manageGuillemet(boolean manageVisibilityModifier) {
		final List<CharSequence> result = new ArrayList<>();
		boolean first = true;
		for (CharSequence line : displayData) {
			String lineString = line.toString();
			if (manageVisibilityModifier && first && VisibilityModifier.isVisibilityCharacter(line))
				lineString = lineString.substring(1).trim();

			final String withGuillement = Guillemet.GUILLEMET.manageGuillemet(lineString);
			result.add(withGuillement);

			first = false;
		}
		return new Display(showStereotype, result, this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	public Display withPage(int page, int lastpage) {
		if (displayData == null)
			return this;

		final List<CharSequence> result = new ArrayList<>();
		for (CharSequence line : displayData) {
			line = line.toString().replace("%page%", "" + page);
			line = line.toString().replace("%lastpage%", "" + lastpage);
			result.add(line);
		}
		return new Display(showStereotype, result, this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	public Display removeEndingStereotype() {
		final Matcher2 m = patternStereotype.matcher(displayData.get(displayData.size() - 1));
		if (m.matches()) {
			final List<CharSequence> result = new ArrayList<>(this.displayData);
			result.set(result.size() - 1, m.group(1));
			return new Display(showStereotype, result, this.naturalHorizontalAlignment, this.isNull,
					this.defaultCreoleMode);
		}
		return this;
	}

	public final static Pattern2 patternStereotype = Pattern2.cmpile("^(.*?)(\\<\\<\\s*(.*)\\s*\\>\\>)\\s*$");

	public Stereotype getEndingStereotype() {
		final Matcher2 m = patternStereotype.matcher(displayData.get(displayData.size() - 1));
		if (m.matches())
			return Stereotype.build(m.group(2));

		return null;
	}

	public Display underlined() {
		final List<CharSequence> result = new ArrayList<>();
		for (CharSequence line : displayData)
			result.add("<u>" + line);

		return new Display(showStereotype, result, this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	private static final Pattern p = Pattern.compile("^([^:]+?)(\\s*:.+)$");

	public Display underlinedName() {
		final List<CharSequence> result = new ArrayList<>();
		for (CharSequence line : displayData) {
			if (result.size() == 0) {
				final Matcher m = p.matcher(line);
				if (m.matches())
					result.add("<u>" + m.group(1) + "</u>" + m.group(2));
				else
					result.add("<u>" + line);
			} else {
				result.add("<u>" + line);
			}
		}
		return new Display(showStereotype, result, this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	public Display withCreoleMode(CreoleMode mode) {
		if (isNull)
			throw new IllegalArgumentException();

		return new Display(this.showStereotype, this, mode);
	}

	@Override
	public String toString() {
		if (isNull)
			return "NULL";

		return displayData.toString();
	}

	public Display addAll(Display other) {
		final Display result = new Display(this.showStereotype, this, this.defaultCreoleMode);
		result.displayData.addAll(other.displayData);
		return result;
	}

	public Display addFirst(CharSequence s) {
		final Display result = new Display(this.showStereotype, this, this.defaultCreoleMode);
		result.displayData.add(0, s);
		return result;
	}

	public Display appendFirstLine(String appended) {
		final Display result = new Display(this.showStereotype, this, this.defaultCreoleMode);
		result.displayData.set(0, appended + result.displayData.get(0));
		return result;
	}

	public Display add(CharSequence s) {
		final Display result = new Display(this.showStereotype, this, this.defaultCreoleMode);
		result.displayData.add(s);
		return result;
	}

	public Display addGeneric(CharSequence s) {
		final Display result = new Display(this.showStereotype, this, this.defaultCreoleMode);
		final int size = displayData.size();
		if (size == 0)
			result.displayData.add("<" + s + ">");
		else
			result.displayData.set(size - 1, displayData.get(size - 1) + "<" + s + ">");

		return result;
	}

	public int size() {
		if (isNull)
			return 0;

		return displayData.size();
	}

	public CharSequence get(int i) {
		return displayData.get(i);
	}

	public ListIterator<CharSequence> iterator() {
		return Collections.unmodifiableList(displayData).listIterator();
	}

	public Display subList(int i, int size) {
		return new Display(showStereotype, displayData.subList(i, size), this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
	}

	public List<? extends CharSequence> asList() {
		if (displayData == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(displayData);
	}

	public boolean hasUrl() {
		final UrlBuilder urlBuilder = new UrlBuilder(null, UrlMode.ANYWHERE);
		for (CharSequence s : this)
			if (urlBuilder.getUrl(s.toString()) != null)
				return true;

		return false;
	}

	public HorizontalAlignment getNaturalHorizontalAlignment() {
		return naturalHorizontalAlignment;
	}

	public List<Display> splitMultiline(Pattern2 separator) {
		final List<Display> result = new ArrayList<>();
		Display pending = new Display(showStereotype, this.naturalHorizontalAlignment, this.isNull,
				this.defaultCreoleMode);
		result.add(pending);
		for (CharSequence line : displayData) {
			final Matcher2 m = separator.matcher(line);
			if (m.find()) {
				final CharSequence s1 = line.subSequence(0, m.start());
				pending.displayData.add(s1);
				final CharSequence s2 = line.subSequence(m.end(), line.length());
				pending = new Display(showStereotype, this.naturalHorizontalAlignment, this.isNull,
						this.defaultCreoleMode);
				result.add(pending);
				pending.displayData.add(s2);
			} else {
				pending.displayData.add(line);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public String toTooltipText() {
		if (size() == 0)
			return "";
		return get(0).toString();
	}

	// ------

	public static boolean isNull(Display display) {
		// Objects.requireNonNull(display);
		return display == null || display.isNull;
	}

	public TextBlock create(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer) {
		return create7(fontConfiguration, horizontalAlignment, spriteContainer, CreoleMode.FULL);
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
		return create0(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, creoleMode,
				fontForStereotype, htmlColorForStereotype, 0, 0);
	}

	public TextBlock create0(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, CreoleMode creoleMode,
			UFont fontForStereotype, HColor htmlColorForStereotype, double marginX1, double marginX2) {
		Objects.requireNonNull(maxMessageSize);
		if (getNaturalHorizontalAlignment() != null)
			horizontalAlignment = getNaturalHorizontalAlignment();

		final FontConfiguration stereotypeConfiguration = fontConfiguration.forceFont(fontForStereotype,
				htmlColorForStereotype);
		if (size() > 0) {
			if (get(0) instanceof Stereotype)
				return createStereotype(fontConfiguration, horizontalAlignment, spriteContainer, 0, fontForStereotype,
						htmlColorForStereotype, maxMessageSize, creoleMode, marginX1, marginX2);

			if (get(size() - 1) instanceof Stereotype)
				return createStereotype(fontConfiguration, horizontalAlignment, spriteContainer, size() - 1,
						fontForStereotype, htmlColorForStereotype, maxMessageSize, creoleMode, marginX1, marginX2);

			if (get(0) instanceof MessageNumber)
				return createMessageNumber(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize,
						stereotypeConfiguration, marginX1, marginX2);
		}

		return getCreole(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, creoleMode,
				stereotypeConfiguration, marginX1, marginX2);
	}

	private TextBlock createStereotype(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			SpriteContainer spriteContainer, int position, UFont fontForStereotype, HColor htmlColorForStereotype,
			LineBreakStrategy maxMessageSize, CreoleMode creoleMode, double marginX1, double marginX2) {
		final Stereotype stereotype = (Stereotype) get(position);
		TextBlock circledCharacter = null;
		if (stereotype.isSpotted())
			circledCharacter = new CircledCharacter(stereotype.getCharacter(), stereotype.getRadius(),
					stereotype.getCircledFont(), stereotype.getHtmlColor(), null, fontConfiguration.getColor());
		else
			circledCharacter = stereotype.getSprite(spriteContainer);

		final FontConfiguration stereotypeConfiguration = fontConfiguration.forceFont(fontForStereotype,
				htmlColorForStereotype);
		final TextBlock result = getCreole(fontConfiguration, horizontalAlignment, (ISkinSimple) spriteContainer,
				maxMessageSize, creoleMode, stereotypeConfiguration, marginX1, marginX2);
		if (circledCharacter != null)
			return new TextBlockSprited(circledCharacter, result);

		return result;
	}

	private TextBlock getCreole(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, CreoleMode creoleMode,
			FontConfiguration stereotypeConfiguration, double marginX1, double marginX2) {
		final Sheet sheet = spriteContainer
				.sheet(fontConfiguration, horizontalAlignment, creoleMode, stereotypeConfiguration).createSheet(this);
		final double padding = spriteContainer == null ? 0 : spriteContainer.getPadding();
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, maxMessageSize, padding, marginX1, marginX2);
		return new SheetBlock2(sheetBlock1, sheetBlock1, UStroke.withThickness(1.5));
	}

	private TextBlock createMessageNumber(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			ISkinSimple spriteContainer, LineBreakStrategy maxMessageSize, FontConfiguration stereotypeConfiguration,
			double marginX1, double marginX2) {
		TextBlock tb1 = subList(0, 1).getCreole(fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize,
				CreoleMode.FULL, stereotypeConfiguration, marginX1, marginX2);
		tb1 = TextBlockUtils.withMargin(tb1, 0, 4, 0, 0);
		final TextBlock tb2 = subList(1, size()).getCreole(fontConfiguration, horizontalAlignment, spriteContainer,
				maxMessageSize, CreoleMode.FULL, stereotypeConfiguration, marginX1, marginX2);
		return TextBlockUtils.mergeLR(tb1, tb2, VerticalAlignment.CENTER);

	}

	public boolean hasSeveralGuideLines() {
		return hasSeveralGuideLines(displayData);
	}

	@JawsStrange
	public static boolean hasSeveralGuideLines(String s) {
		final List<String> splitted;
		if (Pragma.legacyReplaceBackslashNByNewline())
			splitted = Arrays.asList(s.split("\\\\n"));
		else
			splitted = Arrays.asList(s.split("" + Jaws.BLOCK_E1_NEWLINE));
		return hasSeveralGuideLines(splitted);
	}

	private static boolean hasSeveralGuideLines(Collection<? extends CharSequence> all) {
		if (all.size() <= 1)
			return false;

		for (CharSequence cs : all) {
			final String s = cs.toString();
			if (s.startsWith("< "))
				return true;

			if (s.startsWith("> "))
				return true;

			if (s.endsWith(" <"))
				return true;

			if (s.endsWith(" >"))
				return true;
		}
		return false;
	}

//	private Object data;
//	private boolean mayHaveEmbedded;
//
//	public final Object getCachedDataForPerf() {
//		return data;
//	}
//
//	public final void setCachedDataForPerf(Object data) {
//		this.data = data;
//	}
//
//	public final void setMayHaveEmbedded() {
//		this.mayHaveEmbedded = true;
//	}
//
//	public final boolean mayHaveEmbedded() {
//		return mayHaveEmbedded;
//	}

}
