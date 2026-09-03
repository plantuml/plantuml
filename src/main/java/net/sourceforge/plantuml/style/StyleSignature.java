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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.stereo.Stereogroup;
import net.sourceforge.plantuml.stereo.Stereostyles;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.parser2.LevelConstraint;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;

public class StyleSignature {

	public static final String STAR = "*";

	private final Set<String> stereotypes;
	private final StyleKey key;

	public static StyleSignature createStereotype(String s) {
		return empty().addStereotype(s);
	}
	
	
	public StyleQuery toQuery() {
		final StyleKey key = getKey();
		return StyleQuery.of(key.snames, getStereotypes(), LevelConstraint.of(key.level, key.isStared));
	}



	@Override
	public String toString() {
		return key + " " + stereotypes;
	}

	public static StyleSignature empty() {
		return new StyleSignature(StyleKey.empty(), Collections.emptySet());
	}

	private StyleSignature(StyleKey key, Set<String> stereotypes) {
		this.key = key;
		this.stereotypes = stereotypes;
	}

	public StyleSignature addClickable(Url url) {
		if (url == null)
			return this;

		return new StyleSignature(key.addClickable(url), stereotypes);

		
		
	}
	

	public StyleSignature addLevel(int level) {
		return new StyleSignature(key.addLevel(level), stereotypes);
	}

	public StyleSignature addStereotype(String stereo) {
		final Set<String> result = new HashSet<>(stereotypes);
		result.add(clean(stereo));

		return new StyleSignature(key, result);
	}

	public StyleSignature addStereotype(Stereotype stereo) {
		if (stereo == null)
			return this;

		final Set<String> result = new HashSet<>(stereotypes);

		final List<String> labels = stereo.getLabels(Guillemet.NONE);

		for (String s : labels)
			result.add(clean(s));

		return new StyleSignature(key, result);
	}

	public StyleSignature with(Stereostyles stereostyles) {
		if (stereostyles.isEmpty())
			return this;
		final Set<String> result = new HashSet<>(stereotypes);
		for (String name : stereostyles.getStyleNames())
			result.add(name);

		return new StyleSignature(key, result);

	}


	/**
	 * This same signature, additionally requiring every one of {@code stereo}'s labels at once --
	 * e.g. an element tagged both {@code <<foo>>} and {@code <<bar>>} resolves against a single
	 * signature requiring both "foo" and "bar", exactly like a CSS selector requiring several
	 * classes at once ({@code .foo.bar}). Because {@link Specificity}'s stereotype-count tier
	 * counts requirements rather than flatly boosting a boolean, a declaration naming more
	 * stereotypes always outranks one naming fewer, whichever file order they were declared in --
	 * so folding every label into one signature here, instead of resolving each separately and
	 * picking a winner (the old {@code StyleSignatures} composite's job), is now enough on its own.
	 */
	public StyleSignature withTOBECHANGED(Stereotype stereo) {
		if (stereo == null || stereo.getStyleNames().size() == 0)
			return this;

		final List<String> labels = stereo.getLabels(Guillemet.NONE);
		if (labels.size() == 0)
			return this;

		StyleSignature result = this;
		for (String name : labels)
			result = result.addStereotype(name);

		return result;
	}
	
	public StyleSignature withTOBECHANGED(Stereogroup stereogroup) {
		if (stereogroup == null)
			return this;
		return withTOBECHANGED(stereogroup.buildStereotype());
	}



	public StyleSignature forStereotypeItself(Stereotype stereo) {
		if (stereo == null || stereo.getStyleNames().size() == 0)
			return this;

		final List<String> labels = stereo.getLabels(Guillemet.NONE);
		if (labels.size() == 0)
			return this;

		StyleSignature result = this.addSName(SName.stereotype);
		for (String name : labels)
			result = result.addStereotype(name);

		return result;

	}

	public StyleSignature addSName(SName name) {
		final EnumSet<SName> result = key.snames.clone();
		result.add(name);
		return new StyleSignature(key.addSName(name), stereotypes);
	}

	public StyleSignature addStar() {
		return new StyleSignature(key.addStar(), stereotypes);
	}

	public boolean isStarred() {
		return key.isStared;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof StyleSignature))
			return false;
		final StyleSignature other = (StyleSignature) obj;
		return Objects.equals(key, other.key) && Objects.equals(stereotypes, other.stereotypes);
	}

	private transient int cachedHashCode = 0;

	@Override
	public int hashCode() {
		// Thread.dumpStack();
		int result = cachedHashCode;
		if (result == 0) {
			result = Objects.hash(key, stereotypes);
			cachedHashCode = result;
		}
		return result;
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

	public StyleSignature mergeWith(List<Style> others) {
		StyleSignature result = this;
		for (Style other : others)
			result = result.mergeWith(other.getSignature());

		return result;
	}

	public StyleSignature mergeWith(StyleSignature other) {

		final Set<String> result2 = new HashSet<>(stereotypes);
		result2.addAll(other.stereotypes);

		return new StyleSignature(key.mergeWith(other.key), result2);
	}

	public Style getMergedStyleREMOVEME(StyleBuilder styleBuilder) {
		if (styleBuilder == null)
			return null;

		return styleBuilder.getMergedStyleTOBEREMOVED(this);
	}

	public final boolean isWithDot() {
		return stereotypes.size() > 0;
	}

	// Frequent use

	public static StyleQuery activityArrow() {
		return StyleQuery.of(Arrays.asList(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.arrow));
	}

	public static StyleSignature ofSName0(SName... names) {
		return new StyleSignature(StyleKey.ofSNames(names), Collections.emptySet());
	}

	// Really sorry about that :-)
	public static StyleSignature ofSName1(SName name0, SName name1, SName name2, SName name3, SName[] names) {
		if (names.length == 1)
			return ofSName0(name0, name1, name2, name3, names[0]);
		if (names.length == 2)
			return ofSName0(name0, name1, name2, name3, names[0], names[1]);
		throw new UnsupportedOperationException();
	}

	public static StyleSignature ofSName2(SName name0, SName name1, SName name2, SName[] sNames, SName... other) {
		final SName[] concat = new SName[3 + sNames.length + other.length];

		concat[0] = name0;
		concat[1] = name1;
		concat[2] = name2;
		System.arraycopy(sNames, 0, concat, 3, sNames.length);
		System.arraycopy(other, 0, concat, 3 + sNames.length, other.length);

		return ofSName0(concat);
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