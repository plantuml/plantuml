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
package net.sourceforge.plantuml.style;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.stereo.Stereostyles;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;

public class StyleSignatureBasic implements StyleSignature {
	// ::remove file when __HAXE__

	private final Set<String> stereotypes;
	private final StyleKey key;

	public static StyleSignatureBasic createStereotype(String s) {
		return empty().addStereotype(s);
	}

	@Override
	public String toString() {
		return key + " " + stereotypes;
	}

	public static StyleSignatureBasic empty() {
		return new StyleSignatureBasic(StyleKey.empty(), Collections.emptySet());
	}

	private StyleSignatureBasic(StyleKey key, Set<String> stereotypes) {
		this.key = key;
		this.stereotypes = stereotypes;
	}

	public StyleSignatureBasic addClickable(Url url) {
		if (url == null)
			return this;

		return new StyleSignatureBasic(key.addClickable(url), stereotypes);

	}

	public StyleSignatureBasic addLevel(int level) {
		return new StyleSignatureBasic(key.addLevel(level), stereotypes);
	}

	public StyleSignatureBasic addStereotype(String stereo) {
		final Set<String> result = new HashSet<>(stereotypes);
		result.add(clean(stereo));

		return new StyleSignatureBasic(key, result);
	}

	@Override
	public StyleSignature with(Stereostyles stereostyles) {
		if (stereostyles.isEmpty())
			return this;
		final Set<String> result = new HashSet<>(stereotypes);
		for (String name : stereostyles.getStyleNames())
			result.add(name);

		return new StyleSignatureBasic(key, result);

	}

	public StyleSignatureBasic addStereotype(Stereotype stereo) {
		if (stereo == null)
			return this;

		final Set<String> result = new HashSet<>(stereotypes);

		final List<String> labels = stereo.getLabels(Guillemet.NONE);

		for (String s : labels)
			result.add(clean(s));

		return new StyleSignatureBasic(key, result);
	}

	@Override
	public StyleSignature withTOBECHANGED(Stereotype stereo) {
		if (stereo == null || stereo.getStyleNames().size() == 0)
			return this;

		final List<String> labels = stereo.getLabels(Guillemet.NONE);
		if (labels.size() == 0)
			return this;

		final StyleSignatures result = new StyleSignatures();
		for (String name : labels)
			result.add(this.addStereotype(name));

		return result;
	}

	public StyleSignature forStereotypeItself(Stereotype stereo) {
		if (stereo == null || stereo.getStyleNames().size() == 0)
			return this;

		final List<String> labels = stereo.getLabels(Guillemet.NONE);
		if (labels.size() == 0)
			return this;

		final StyleSignatures result = new StyleSignatures();
		for (String name : labels)
			result.add(this.addStereotype(name).addSName(SName.stereotype));

		return result;

	}

	public StyleSignatureBasic addSName(SName name) {
		final EnumSet<SName> result = key.snames.clone();
		result.add(name);
		return new StyleSignatureBasic(key.addSName(name), stereotypes);
	}

	public StyleSignatureBasic addStar() {
		return new StyleSignatureBasic(key.addStar(), stereotypes);
	}

	public boolean isStarred() {
		return key.isStared;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StyleSignatureBasic))
			return false;
		final StyleSignatureBasic other = (StyleSignatureBasic) obj;
		return Objects.equals(key, other.key) && Objects.equals(stereotypes, other.stereotypes);
	}

	private transient int cachedHashCode = 0;

	@Override
	public int hashCode() {
		int result = cachedHashCode;
		if (result == 0) {
			result = Objects.hash(key, stereotypes);
			cachedHashCode = result;
		}
		return result;
	}

//	private final Map<StyleSignatureBasic, Boolean> matchAllCache = new ConcurrentHashMap<>();

	public boolean matchAll(StyleSignatureBasic other) {
//		final Boolean computeIfAbsent = matchAllCache.computeIfAbsent(other, k -> matchAllImpl(this, other));
//		return computeIfAbsent;
		return matchAllImpl(this, other);
	}

	private static boolean matchAllImpl(StyleSignatureBasic declaration, StyleSignatureBasic element) {

		if (declaration.key.level != -1)
			if (declaration.key.isStared) {
				if (element.key.level == -1)
					return false;
				if (element.key.level < declaration.key.level)
					return false;

			} else {
				if (element.key.level == -1)
					return false;
				if (element.key.level != declaration.key.level)
					return false;
			}

		if (element.isStarred() && declaration.isStarred() == false)
			return false;

		if (element.key.snames.containsAll(declaration.key.snames) == false)
			return false;

		if (element.stereotypes.containsAll(declaration.stereotypes) == false)
			return false;

		return true;
	}

	public static StyleSignatureBasic of(SName... names) {
		return new StyleSignatureBasic(StyleKey.of(names), Collections.emptySet());
	}

	private String clean(String name) {
		final StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			final char c = name.charAt(i);
			if (c != '_' && c != '.')
				sb.append(Character.toLowerCase(c));

		}
		return sb.toString();
	}

	public StyleSignatureBasic mergeWith(List<Style> others) {
		StyleSignatureBasic result = this;
		for (Style other : others)
			result = result.mergeWith(other.getSignature());

		return result;
	}

	public StyleSignatureBasic mergeWith(StyleSignatureBasic other) {

		final Set<String> result2 = new HashSet<>(stereotypes);
		result2.addAll(other.stereotypes);

		return new StyleSignatureBasic(key.mergeWith(other.key), result2);
	}

	@Override
	public Style getMergedStyle(StyleBuilder styleBuilder) {
		if (styleBuilder == null)
			return null;

		return styleBuilder.getMergedStyle(this);
	}

	public final boolean isWithDot() {
		return stereotypes.size() > 0;
	}

	// Frequent use

	public static StyleSignatureBasic activity() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	public static StyleSignatureBasic activityDiamond() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	public static StyleSignatureBasic activityArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.arrow);
	}

	// Really sorry about that :-)
	public static StyleSignatureBasic of(SName name0, SName name1, SName name2, SName name3, SName[] names) {
		if (names.length == 1)
			return of(name0, name1, name2, name3, names[0]);
		if (names.length == 2)
			return of(name0, name1, name2, name3, names[0], names[1]);
		throw new UnsupportedOperationException();
	}

	public static StyleSignatureBasic of(SName name0, SName name1, SName name2, SName[] sNames, SName... other) {
		final SName[] concat = new SName[3 + sNames.length + other.length];

		concat[0] = name0;
		concat[1] = name1;
		concat[2] = name2;
		System.arraycopy(sNames, 0, concat, 3, sNames.length);
		System.arraycopy(other, 0, concat, 3 + sNames.length, other.length);

		return of(concat);
	}

	public boolean isEmpty() {
		return key.snames.isEmpty() && stereotypes.isEmpty();
	}

	public StyleKey getKey() {
		return key;
	}

	public Set<String> getStereotypes() {
		return stereotypes;
	}

}