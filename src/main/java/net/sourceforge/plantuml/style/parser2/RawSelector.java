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

import net.sourceforge.plantuml.style.SName;

/**
 * One alternative name inside a (possibly comma-separated) selector list, exactly as it
 * was spelled in a .skin file, e.g. the three names in {@code participant,actor,boundary
 * { ... }}.
 *
 * This is a purely syntactic classification: it says how the token was spelled, not
 * whether or how it matches anything. Turning this into an actual matching rule (merging
 * duplicate selectors, expanding comma lists, cascading) is the job of the in-memory
 * style model, built in a later pass on top of {@link RawStyleSheet}.
 */
public final class RawSelector {

	public enum Kind {
		/** A name known to {@link SName}, e.g. {@code sequenceDiagram}, {@code participant}. */
		NAME,
		/** A name starting with a dot, e.g. {@code .myStereotype}. */
		STEREOTYPE,
		/** A {@code depth(n)} pseudo-selector. */
		DEPTH,
		/** Anything else: a name {@link SName} does not know about. */
		UNKNOWN
	}

	private final Kind kind;
	private final String raw;
	private final SName sname;
	private final int depth;

	private RawSelector(Kind kind, String raw, SName sname, int depth) {
		this.kind = kind;
		this.raw = raw;
		this.sname = sname;
		this.depth = depth;
	}

	/** Classifies one already-trimmed selector token, exactly as it was spelled in the file. */
	public static RawSelector classify(String raw) {
		if (raw.startsWith("."))
			return new RawSelector(Kind.STEREOTYPE, raw, null, -1);

		if (raw.startsWith("depth(") && raw.endsWith(")")) {
			final String inside = raw.substring("depth(".length(), raw.length() - 1).trim();
			try {
				return new RawSelector(Kind.DEPTH, raw, null, Integer.parseInt(inside));
			} catch (NumberFormatException e) {
				// Not really a depth(...) selector after all: fall through and classify as usual.
			}
		}

		final SName sname = SName.retrieve(raw);
		if (sname == null)
			return new RawSelector(Kind.UNKNOWN, raw, null, -1);

		return new RawSelector(Kind.NAME, raw, sname, -1);
	}

	public Kind getKind() {
		return kind;
	}

	public String getRaw() {
		return raw;
	}

	/** Non-null only when {@link #getKind()} is {@link Kind#NAME}. */
	public SName getSName() {
		return sname;
	}

	/** Meaningful only when {@link #getKind()} is {@link Kind#DEPTH}. */
	public int getDepth() {
		return depth;
	}

	@Override
	public String toString() {
		return raw;
	}

}
