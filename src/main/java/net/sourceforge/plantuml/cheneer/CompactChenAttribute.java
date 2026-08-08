/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */
package net.sourceforge.plantuml.cheneer;

import net.sourceforge.plantuml.stereo.Stereotype;

final class CompactChenAttribute {
	enum UnderlineStyle {
		NONE, SOLID, DASHED
	}

	private final String qualifiedIdentity;
	private final String displayName;
	private final String domain;
	private final int depth;
	private final Stereotype stereotype;
	private final boolean key;
	private final boolean discriminator;
	private final boolean multi;
	private final boolean derived;

	CompactChenAttribute(String qualifiedIdentity, String displayName, String domain, int depth,
			Stereotype stereotype) {
		this.qualifiedIdentity = qualifiedIdentity;
		this.displayName = displayName;
		this.domain = domain;
		this.depth = depth;
		this.stereotype = stereotype;
		this.key = hasLabel(stereotype, "key");
		this.discriminator = hasLabel(stereotype, "discriminator");
		this.multi = hasLabel(stereotype, "multi");
		this.derived = hasLabel(stereotype, "derived");
	}

	String getQualifiedIdentity() {
		return qualifiedIdentity;
	}

	String getDisplayName() {
		return displayName;
	}

	String getDomain() {
		return domain;
	}

	int getDepth() {
		return depth;
	}

	Stereotype getStereotype() {
		return stereotype;
	}

	boolean isKey() {
		return key;
	}

	boolean isDiscriminator() {
		return discriminator;
	}

	boolean isMulti() {
		return multi;
	}

	boolean isDerived() {
		return derived;
	}

	UnderlineStyle getUnderlineStyle() {
		if (key)
			return UnderlineStyle.SOLID;
		if (discriminator)
			return UnderlineStyle.DASHED;
		return UnderlineStyle.NONE;
	}

	private static boolean hasLabel(Stereotype stereotype, String expected) {
		if (stereotype == null)
			return false;

		for (String label : stereotype.getMultipleLabels())
			if (label.equalsIgnoreCase(expected))
				return true;

		return false;
	}

}
