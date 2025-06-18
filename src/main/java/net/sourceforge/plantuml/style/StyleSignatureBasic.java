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
import net.sourceforge.plantuml.utils.Log;

public class StyleSignatureBasic implements StyleSignature {
	// ::remove file when __HAXE__

	private final EnumSet<SName> snames;
	private final Set<String> stereotypes;
	private final boolean isStared;
	private final int level;

	public static StyleSignatureBasic createStereotype(String s) {
		return empty().addStereotype(s);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(snames + " " + stereotypes);
		if (level != -1)
			sb.append(" " + level);
		if (isStared)
			sb.append(" (*)");
		return sb.toString();
	}

	public static StyleSignatureBasic empty() {
		return new StyleSignatureBasic(false, EnumSet.noneOf(SName.class), Collections.emptySet(), -1);
	}

	private StyleSignatureBasic(boolean isStared, EnumSet<SName> snames, Set<String> stereotypes, int level) {
		this.isStared = isStared;
		this.snames = snames;
		this.stereotypes = stereotypes;
		this.level = level;
	}

	public StyleSignatureBasic addClickable(Url url) {
		if (url == null)
			return this;

		final EnumSet<SName> result = snames.clone();
		result.add(SName.clickable);
		return new StyleSignatureBasic(isStared, result, stereotypes, level);

	}

	public StyleSignatureBasic addLevel(int level) {
		return new StyleSignatureBasic(isStared, snames, stereotypes, level);
	}

	public StyleSignatureBasic addStereotype(String stereo) {
		final Set<String> result = new HashSet<>(stereotypes);
		result.add(clean(stereo));

		return new StyleSignatureBasic(isStared, snames, result, level);
	}

	@Override
	public StyleSignature with(Stereostyles stereostyles) {
		if (stereostyles.isEmpty())
			return this;
		final Set<String> result = new HashSet<>(stereotypes);
		for (String name : stereostyles.getStyleNames())
			result.add(name);

		return new StyleSignatureBasic(isStared, snames, result, level);

	}

	public StyleSignatureBasic addStereotype(Stereotype stereo) {
		if (stereo == null)
			return this;

		final Set<String> result = new HashSet<>(stereotypes);

		final List<String> labels = stereo.getLabels(Guillemet.NONE);

		for (String s : labels)
			result.add(clean(s));

		return new StyleSignatureBasic(isStared, snames, result, level);
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
		final EnumSet<SName> result = snames.clone();
		result.add(name);
		return new StyleSignatureBasic(isStared, result, stereotypes, level);
	}

	public StyleSignatureBasic addStar() {
		return new StyleSignatureBasic(true, snames, stereotypes, level);
	}

	public boolean isStarred() {
		return isStared;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StyleSignatureBasic))
			return false;
		final StyleSignatureBasic other = (StyleSignatureBasic) obj;
		return Objects.equals(snames, other.snames) && Objects.equals(stereotypes, other.stereotypes)
				&& isStared == other.isStared && level == other.level;
	}

	private transient int cachedHashCode = 0;

	@Override
	public int hashCode() {
		int result = cachedHashCode;
		if (result == 0) {
			result = Objects.hash(snames, stereotypes, isStared, level);
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

		if (declaration.level != -1)
			if (declaration.isStared) {
				if (element.level == -1)
					return false;
				if (element.level < declaration.level)
					return false;

			} else {
				if (element.level == -1)
					return false;
				if (element.level != declaration.level)
					return false;
			}

		if (element.isStarred() && declaration.isStarred() == false)
			return false;

		if (element.snames.containsAll(declaration.snames) == false)
			return false;

		if (element.stereotypes.containsAll(declaration.stereotypes) == false)
			return false;

		return true;
	}

	public static StyleSignatureBasic of(SName... names) {

		final EnumSet<SName> result = EnumSet.noneOf(SName.class);
		for (SName name : names)
			result.add(name);
		return new StyleSignatureBasic(false, result, Collections.emptySet(), -1);
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

		final EnumSet<SName> result1 = snames.clone();
		result1.addAll(other.snames);

		final Set<String> result2 = new HashSet<>(stereotypes);
		result2.addAll(other.stereotypes);

		return new StyleSignatureBasic(isStared || other.isStared, result1, result2, Math.max(level, other.level));
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

	public int size() {
		return snames.size() + stereotypes.size();
	}

}