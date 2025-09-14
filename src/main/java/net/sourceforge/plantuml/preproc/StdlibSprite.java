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
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.klimt.sprite.SpriteMonochrome;

public class StdlibSprite implements Sprite {

	private SpriteMonochrome sprite;
	private byte[] data;
	private final int width;
	private final int height;

	public StdlibSprite(int width, int height, byte[] data) {
		this.width = width;
		this.height = height;
		this.data = data;
	}

	@Override
	public TextBlock asTextBlock(HColor fontColor, HColor forcedColor, double scale, final HColor backColor) {
		synchronized (this) {
			if (sprite == null) {
				int pos = 0;
				sprite = new SpriteMonochrome(width, height, 16);
				final int nbLines = (height + 1) / 2;

				for (int j = 0; j < nbLines; j++) {

					for (int i = 0; i < width; i++) {
						final int b = data[pos++];
						final int b1 = (b & 0xF0) >> 4;
						final int b2 = (b & 0x0F);
						sprite.setGray(i, j * 2, b1);
						sprite.setGray(i, j * 2 + 1, b2);
					}
				}
				this.data = null; // free memory
			}

		}
		return sprite.asTextBlock(fontColor, forcedColor, scale, backColor);
	}

}
