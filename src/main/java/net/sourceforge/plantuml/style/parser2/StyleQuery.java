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
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.SName;
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
		StyleQuery result = this;
		for (String s : stereotype.getMultipleLabels())
			result = result.withStereotype(s);
		return result;
	}
	
	public StyleQuery addClickable(Url url) {
		throw new UnsupportedOperationException();
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
