/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6570 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;

public class TextBlockUtils {

	public static TextBlock create(List<? extends CharSequence> texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement) {
		if (texts.size() > 0 && texts.get(0) instanceof Stereotype) {
			return createStereotype(texts, fontConfiguration, horizontalAlignement);
		}
		if (texts.size() > 0 && texts.get(0) instanceof MessageNumber) {
			return createMessageNumber(texts, fontConfiguration, horizontalAlignement);
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignement);
	}

	private static TextBlock createMessageNumber(List<? extends CharSequence> texts,
			FontConfiguration fontConfiguration, HorizontalAlignement horizontalAlignement) {
		final MessageNumber number = (MessageNumber) texts.get(0);
		return new TextBlockWithNumber(number.getNumber(), texts.subList(1, texts.size()), fontConfiguration,
				horizontalAlignement);

	}

	private static TextBlock createStereotype(List<? extends CharSequence> texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement) {
		final Stereotype stereotype = (Stereotype) texts.get(0);
		if (stereotype.isSpotted()) {
			final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(),
					stereotype.getRadius(), stereotype.getCircledFont(), stereotype.getHtmlColor(), null,
					fontConfiguration.getColor());
			if (stereotype.getLabel() == null) {
				return new TextBlockSpotted(circledCharacter, texts.subList(1, texts.size()), fontConfiguration,
						horizontalAlignement);
			}
			return new TextBlockSpotted(circledCharacter, texts, fontConfiguration, horizontalAlignement);
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignement);
	}

	// static private Font deriveForCircleCharacter(Font font) {
	// final float size = font.getSize2D();
	// return font.deriveFont(size - 1).deriveFont(Font.BOLD);
	// }

}
