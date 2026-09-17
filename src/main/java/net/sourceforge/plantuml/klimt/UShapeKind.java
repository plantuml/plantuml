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
package net.sourceforge.plantuml.klimt;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.klimt.creole.legacy.AtomText;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.UImageTikz;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPixel;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;

/**
 * The closed set of {@link UShape} kinds a {@code UDriver} can be registered for, and the reason
 * an {@code AbstractUGraphic} can keep its drivers in a plain array rather than a map: the kind
 * is a small dense index, so a lookup is {@code drivers[kind.ordinal()]}.
 *
 * Membership here is exactly "some driver, in some output format, knows how to draw this" -- the
 * thirteen classes every {@code UGraphic} implementation registers between them. It is not the
 * set of all {@link UShape} implementations: most of those are containers or decorations that
 * never reach {@code AbstractUGraphic#draw} as themselves. Those keep {@link #UNKNOWN}, which no
 * driver is ever registered under, so asking for one still finds an empty slot and fails exactly
 * as an unregistered class did before.
 *
 * Each constant names the class it stands for, so this enum is the single place that pairs the
 * two; {@link UShape#getShapeKind()}'s overrides are checked against it by every diagram that
 * renders, since a shape returning the wrong kind would find the wrong driver (or none) the
 * first time it is drawn.
 */
public enum UShapeKind {

	/** Not drawable on its own: no driver is ever registered under this kind. */
	UNKNOWN(null),

	RECTANGLE(URectangle.class),

	LINE(ULine.class),

	POLYGON(UPolygon.class),

	ELLIPSE(UEllipse.class),

	TEXT(UText.class),

	PATH(UPath.class),

	DOT_PATH(DotPath.class),

	IMAGE(UImage.class),

	IMAGE_SVG(UImageSvg.class),

	IMAGE_TIKZ(UImageTikz.class),

	PIXEL(UPixel.class),

	CENTERED_CHARACTER(UCenteredCharacter.class),

	ATOM_TEXT(AtomText.class);

	/** How many slots a driver array needs to hold every kind. */
	public static final int COUNT = values().length;

	private static final Map<Class<?>, UShapeKind> BY_TYPE = buildByType();

	private final Class<? extends UShape> type;

	private UShapeKind(Class<? extends UShape> type) {
		this.type = type;
	}

	private static Map<Class<?>, UShapeKind> buildByType() {
		final Map<Class<?>, UShapeKind> result = new HashMap<Class<?>, UShapeKind>();
		for (UShapeKind kind : values())
			if (kind.type != null)
				result.put(kind.type, kind);

		return result;
	}

	/**
	 * The kind {@code type} stands for. Used when registering a driver, where only the class is
	 * in hand; drawing goes through {@link UShape#getShapeKind()} instead and never looks here.
	 */
	public static UShapeKind of(Class<? extends UShape> type) {
		final UShapeKind result = BY_TYPE.get(type);
		if (result == null)
			throw new IllegalArgumentException("No UShapeKind for " + type);

		return result;
	}

	// Kept so that a new constant cannot be added without saying which class it stands for.
	Class<? extends UShape> getType() {
		return type;
	}

}
