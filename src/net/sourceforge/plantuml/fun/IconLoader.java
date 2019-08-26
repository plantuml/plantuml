/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 */
package net.sourceforge.plantuml.fun;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

public class IconLoader {

	private static final int NUMBER_OF_ICONS = 30;

	private final static Map<String, BufferedImage> all = new ConcurrentHashMap<String, BufferedImage>();
	static private final List<String> tmp = new ArrayList<String>();

	public static BufferedImage getRandom() {
		// return addTransparent(getIcon("sprite029.png"));
		return addTransparent(getIcon(getSomeQuote()));
	}

	private static String getSomeQuote() {
		synchronized (tmp) {
			if (tmp.size() == 0) {
				for (int i = 0; i < NUMBER_OF_ICONS; i++) {
					tmp.add("sprite" + String.format("%03d", i) + ".png");
				}
				Collections.shuffle(tmp);
			}
			final int size = tmp.size();
			final String result = tmp.get(size - 1);
			tmp.remove(size - 1);
			return result;
		}
	}

	private static BufferedImage getIcon(String name) {
		BufferedImage result = all.get(name);
		if (result == null) {
			result = getIconSlow(name);
			if (result != null) {
				all.put(name, result);
			}
		}
		return result;
	}

	private static BufferedImage getIconSlow(String name) {
		try {
			final InputStream is = IconLoader.class.getResourceAsStream(name);
			if (is == null) {
				return null;
			}
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage addTransparent(BufferedImage ico) {
		if (ico == null) {
			return null;
		}
		final BufferedImage transparentIcon = new BufferedImage(ico.getWidth(), ico.getHeight(),
				BufferedImage.TYPE_INT_ARGB_PRE);
		for (int i = 0; i < ico.getWidth(); i++) {
			for (int j = 0; j < ico.getHeight(); j++) {
				final int col = ico.getRGB(i, j);
				if (col != ico.getRGB(0, 0)) {
					transparentIcon.setRGB(i, j, col);
				}
			}
		}
		return transparentIcon;
	}

}
