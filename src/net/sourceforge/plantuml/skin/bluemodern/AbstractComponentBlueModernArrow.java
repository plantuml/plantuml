/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 11394 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class AbstractComponentBlueModernArrow extends AbstractTextualComponent implements ArrowComponent {

	private final int arrowDeltaX = 12;
	private final int arrowDeltaY = 10;

	private final int arrowDeltaX2 = 10;
	private final int arrowDeltaY2 = 5;
	private final ArrowConfiguration arrowConfiguration;
	private final HtmlColor foregroundColor;

	public AbstractComponentBlueModernArrow(HtmlColor foregroundColor, HtmlColor fontColor, UFont font,
			Display stringsToDisplay, ArrowConfiguration arrowConfiguration, SpriteContainer spriteContainer) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignment.LEFT, 17, 17, 2, spriteContainer, 0, false);
		this.arrowConfiguration = arrowConfiguration;
		this.foregroundColor = foregroundColor;
	}

	protected final HtmlColor getForegroundColor() {
		return foregroundColor;
	}

	final protected int getArrowDeltaX() {
		return arrowDeltaX;
	}

	final protected int getArrowDeltaY() {
		return arrowDeltaY;
	}

	final protected int getArrowDeltaY2() {
		return arrowDeltaY2;
	}

	final protected int getArrowDeltaX2() {
		return arrowDeltaX2;
	}

	@Override
	public final double getPaddingY() {
		return 6;
	}

	final protected ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

}
