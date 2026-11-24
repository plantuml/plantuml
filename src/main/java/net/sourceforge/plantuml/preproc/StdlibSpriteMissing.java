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
package net.sourceforge.plantuml.preproc;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomTextUtils;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.Sprite;

/**
 * What a sprite of the standard library draws when the library has no sprite
 * of that name: a short message, as AtomImg does for an image it cannot read,
 * rather than nothing. A sprite that silently drew nothing is how the 15 blank
 * icons of edgy2 went unnoticed, in the diagrams and in the test references
 * (#3039).
 * <p>
 * The message gives the reference as it is written in the declaration
 * ("sprite $x :edgy:edgy2:bActivityIcon"), so that it can be searched for in
 * the source of the library.
 */
public class StdlibSpriteMissing implements Sprite {

	private final String reference;

	public StdlibSpriteMissing(String reference) {
		this.reference = reference;
	}

	@Override
	public TextBlock asTextBlock(HColor fontColor, HColor forcedColor, double scale, HColor backColor) {
		final FontConfiguration font = FontConfiguration.blackBlueTrue(UFontFactory.byDefault(10));
		final HColor color = fontColor == null ? HColors.BLACK.withDark(HColors.WHITE) : fontColor;
		return AtomTextUtils.createLegacy("(Unknown sprite " + reference + ")", font.changeColor(color));
	}

}
