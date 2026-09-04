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
package net.sourceforge.plantuml.style.parser2;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.stereo.Stereogroup;
import net.sourceforge.plantuml.stereo.Stereostyles;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.url.Url;

/**
 * What a {@link StyleAtomTrie} is asked to resolve: the element's tags (its
 * {@link SName} set plus any stereotypes, e.g. what the legacy
 * {@code StyleSignatureBasic} calls {@code snames}/{@code stereotypes}) and its
 * {@link LevelConstraint} (its mindmap/wbs depth and whether this is an
 * ancestor-inheritance lookup -- see {@link LevelConstraint}).
 */
public final class StyleQuery {

	private final SortedSet<StyleAtom> atoms;
	private final LevelConstraint levelConstraint;

	private StyleQuery(SortedSet<StyleAtom> atoms, LevelConstraint levelConstraint) {
		this.atoms = atoms;
		this.levelConstraint = levelConstraint;
	}

	public static StyleQuery of(Collection<SName> names, Collection<String> stereotypes,
			LevelConstraint levelConstraint) {
		final SortedSet<StyleAtom> atoms = new TreeSet<StyleAtom>();
		for (SName name : names)
			atoms.add(StyleAtom.of(name));
		for (String stereotype : stereotypes)
			atoms.add(StyleAtom.ofStereotype(stereotype));
		return new StyleQuery(atoms, levelConstraint);
	}

	public static StyleQuery of(Collection<SName> names, Collection<String> stereotypes) {
		return of(names, stereotypes, LevelConstraint.none());
	}

	public static StyleQuery of(Collection<SName> names) {
		return of(names, Collections.<String>emptySet(), LevelConstraint.none());
	}

	/**
	 * This same query, additionally requiring {@code stereotype} -- e.g. so a
	 * "frequent use" factory that starts from a plain, stereotype-less query (the
	 * common case, since most such factories are called with no stereotype in hand
	 * yet) can still fold one in once the caller has it, the way
	 * {@code StyleSignature.addStereotype} does for the legacy signature.
	 * {@code stereotype} is cleaned exactly as {@link StyleAtom#ofStereotype}
	 * cleans it (see there for why), so passing it in raw, un-lower-cased text is
	 * fine. Requiring several stereotypes at once (an element tagged with more than
	 * one, e.g. {@code <<foo>><<bar>>}) is just calling this again on the result,
	 * since a {@link TreeSet} silently dedupes a stereotype already present.
	 */
	public StyleQuery withStereotype(String stereotype) {
		final SortedSet<StyleAtom> result = new TreeSet<StyleAtom>(atoms);
		result.add(StyleAtom.ofStereotype(stereotype));
		return new StyleQuery(result, levelConstraint);
	}

	public StyleQuery withTOBECHANGED(Stereotype stereotype) {
		if (stereotype == null)
			return this;

		StyleQuery result = this;
		for (String s : stereotype.getMultipleLabels())
			result = result.withStereotype(s);
		return result;
	}

	/**
	 * This same query, additionally requiring every one of {@code stereogroup}'s own stereotype's
	 * labels at once, the same way {@link #withTOBECHANGED(Stereotype)} does -- mirroring
	 * {@code StyleSignature.withTOBECHANGED(Stereogroup)}.
	 */
	public StyleQuery withTOBECHANGED(Stereogroup stereogroup) {
		if (stereogroup == null)
			return this;

		return withTOBECHANGED(stereogroup.buildStereotype());
	}

	/**
	 * This same query, additionally requiring every one of {@code stereo}'s labels at once, the
	 * same way {@link #withTOBECHANGED(Stereotype)} does -- but also requiring the special
	 * {@link SName#stereotype} tag, mirroring {@code StyleSignature.forStereotypeItself}: used to
	 * resolve the CSS-class-like rule for the stereotype itself (e.g. {@code <<foo>>}'s own
	 * {@code .foo { ... }} declaration), as opposed to a plain element carrying that stereotype.
	 */
	public StyleQuery forStereotypeItself(Stereotype stereo) {
		if (stereo == null || stereo.getStyleNames().size() == 0)
			return this;

		final List<String> labels = stereo.getLabels(Guillemet.NONE);
		if (labels.size() == 0)
			return this;

		final SortedSet<StyleAtom> withStereotypeTag = new TreeSet<StyleAtom>(atoms);
		withStereotypeTag.add(StyleAtom.of(SName.stereotype));
		StyleQuery result = new StyleQuery(withStereotypeTag, levelConstraint);
		for (String name : labels)
			result = result.withStereotype(name);

		return result;
	}

	/**
	 * This same query, additionally requiring every stereo-style name in {@code stereostyles} --
	 * mirroring {@code StyleSignature.with(Stereostyles)}.
	 */
	public StyleQuery with(Stereostyles stereostyles) {
		if (stereostyles.isEmpty())
			return this;

		StyleQuery result = this;
		for (String name : stereostyles.getStyleNames())
			result = result.withStereotype(name);

		return result;
	}

	/**
	 * This same query, additionally constrained to depth {@code level} (an exact match unless the
	 * query was already starred) -- mirroring {@code StyleSignature.addLevel(int)}.
	 */
	public StyleQuery addLevel(int level) {
		return new StyleQuery(atoms, LevelConstraint.of(level, levelConstraint.isStar()));
	}

	public StyleQuery addClickable(Url url) {
		if (url == null)
			return this;

		final SortedSet<StyleAtom> result = new TreeSet<StyleAtom>(atoms);
		result.add(StyleAtom.of(SName.clickable));
		return new StyleQuery(result, levelConstraint);
	}

	/**
	 * This query, unioned with {@code other}: every atom either carries, plus the least
	 * restrictive combination of their two {@link LevelConstraint}s (deepest level, starred if
	 * either side is) -- mirroring {@code StyleKey.mergeWith}.
	 */
	public StyleQuery mergeWith(StyleQuery other) {
		final SortedSet<StyleAtom> result = new TreeSet<StyleAtom>(atoms);
		result.addAll(other.atoms);

		final int mergedLevel = Math.max(levelConstraint.getLevel(), other.levelConstraint.getLevel());
		final boolean mergedStar = levelConstraint.isStar() || other.levelConstraint.isStar();

		return new StyleQuery(result, LevelConstraint.of(mergedLevel, mergedStar));
	}

	/**
	 * This query, unioned in turn with each of {@code others}'s own signature -- mirroring
	 * {@code StyleSignature.mergeWith(List<Style>)}, via {@code StyleSignature.toQuery()} since a
	 * {@link Style}'s signature is still the legacy type.
	 */
	public StyleQuery mergeWith(List<Style> others) {
		StyleQuery result = this;
		for (Style other : others)
			result = result.mergeWith(other.getSignature().toQuery());

		return result;
	}

	public SortedSet<StyleAtom> getAtoms() {
		return atoms;
	}

	public LevelConstraint getLevelConstraint() {
		return levelConstraint;
	}

	@Override
	public String toString() {
		return atoms + " " + levelConstraint;
	}

}
