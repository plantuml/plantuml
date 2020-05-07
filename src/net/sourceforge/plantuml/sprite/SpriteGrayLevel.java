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
 *
 */
package net.sourceforge.plantuml.sprite;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.AsciiEncoderFinalZeros;
import net.sourceforge.plantuml.code.ByteArray;
import net.sourceforge.plantuml.code.CompressionZlib;
import net.sourceforge.plantuml.code.CompressionZopfliZlib;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.PairInt;
import net.sourceforge.plantuml.code.SpiralOnRectangle;
import net.sourceforge.plantuml.ugraphic.color.ColorChangerMonochrome;

public enum SpriteGrayLevel {

	GRAY_16(16), GRAY_8(8), GRAY_4(4);

	private final int nbColor;
	private static final ColorChangerMonochrome mono = new ColorChangerMonochrome();

	private SpriteGrayLevel(int nbColor) {
		this.nbColor = nbColor;
	}

	public static SpriteGrayLevel get(int n) {
		if (n == 4) {
			return SpriteGrayLevel.GRAY_4;
		}
		if (n == 8) {
			return SpriteGrayLevel.GRAY_8;
		}
		if (n == 16) {
			return SpriteGrayLevel.GRAY_16;
		}
		throw new UnsupportedOperationException();
	}

	public int getNbColor() {
		return nbColor;
	}

	public List<String> encode(BufferedImage img) {
		if (this == GRAY_16) {
			return encode16(img);
		}
		if (this == GRAY_8) {
			return encode8(img);
		}
		if (this == GRAY_4) {
			return encode4(img);
		}
		throw new UnsupportedOperationException();
	}

	private List<String> encode16(BufferedImage img) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		// final int type = img.getType();

		final List<String> result = new ArrayList<String>();

		for (int y = 0; y < height; y++) {
			final StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
				final int level = getGrayOn16(img, x, y);
				final char code = "0123456789ABCDEF".charAt(level);
				sb.append(code);
			}
			result.add(sb.toString());
		}
		return Collections.unmodifiableList(result);
	}

	private List<String> encode8(BufferedImage img) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		// final int type = img.getType();

		final List<String> result = new ArrayList<String>();

		for (int y = 0; y < height; y += 2) {
			final StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
				final int level1 = getGrayOn16(img, x, y) / 2;
				assert level1 >= 0 && level1 <= 7;
				final int level2 = getGrayOn16(img, x, y + 1) / 2;
				assert level2 >= 0 && level2 <= 7;
				final int v = level1 * 8 + level2;
				sb.append(AsciiEncoder.encode6bit((byte) v));
			}
			result.add(sb.toString());
		}
		return Collections.unmodifiableList(result);
	}

	private List<String> encode4(BufferedImage img) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		// final int type = img.getType();

		final List<String> result = new ArrayList<String>();

		for (int y = 0; y < height; y += 3) {
			final StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
				final int level1 = getGrayOn16(img, x, y) / 4;
				assert level1 >= 0 && level1 <= 3;
				final int level2 = getGrayOn16(img, x, y + 1) / 4;
				assert level2 >= 0 && level2 <= 3;
				final int level3 = getGrayOn16(img, x, y + 2) / 4;
				assert level3 >= 0 && level3 <= 3;
				final int v = level1 * 16 + level2 * 4 + level3;
				sb.append(AsciiEncoder.encode6bit((byte) v));
			}
			result.add(sb.toString());
		}
		return Collections.unmodifiableList(result);
	}

	private int getGrayOn16(BufferedImage img, int x, int y) {
		if (x >= img.getWidth()) {
			return 0;
		}
		if (y >= img.getHeight()) {
			return 0;
		}
		final Color g = mono.getChangedColor(new Color(img.getRGB(x, y)));
		final int grey = 255 - g.getRed();
		return grey / 16;
	}

	public Sprite buildSprite(int width, int height, List<String> strings) {
		if (this == SpriteGrayLevel.GRAY_16) {
			return buildSprite16(strings);
		}
		if (this == SpriteGrayLevel.GRAY_8) {
			return buildSprite8(width, height, strings);
		}
		if (this == SpriteGrayLevel.GRAY_4) {
			return buildSprite4(width, height, strings);
		}
		throw new UnsupportedOperationException(toString());
	}

	private Sprite buildSprite16(List<String> strings) {
		final SpriteMonochrome result = new SpriteMonochrome(strings.get(0).length(), strings.size(), 16);
		for (int col = 0; col < result.getWidth(); col++) {
			for (int line = 0; line < result.getHeight(); line++) {
				if (col >= strings.get(line).length()) {
					continue;
				}
				if (strings.get(line).charAt(col) != '0') {
					final String s = "" + strings.get(line).charAt(col);
					final int x = Integer.parseInt(StringUtils.goUpperCase(s), 16);
					result.setGrey(col, line, x);
				}
			}
		}
		return result;
	}

	private Sprite buildSprite8(int width, int height, List<String> strings) {
		final SpriteMonochrome result = new SpriteMonochrome(width, height, 8);
		for (int col = 0; col < result.getWidth(); col++) {
			for (int line = 0; line < strings.size(); line++) {
				if (col >= strings.get(line).length()) {
					continue;
				}
				final int v = AsciiEncoder.decode6bit(strings.get(line).charAt(col));
				final int w1 = v / 8;
				final int w2 = v % 8;
				result.setGrey(col, line * 2, w1);
				result.setGrey(col, line * 2 + 1, w2);

			}
		}
		return result;
	}

	private Sprite buildSprite4(int width, int height, List<String> strings) {
		final SpriteMonochrome result = new SpriteMonochrome(width, height, 4);
		for (int col = 0; col < result.getWidth(); col++) {
			for (int line = 0; line < strings.size(); line++) {
				if (col >= strings.get(line).length()) {
					continue;
				}
				int v = AsciiEncoder.decode6bit(strings.get(line).charAt(col));
				final int w1 = v / 16;
				v = v % 16;
				final int w2 = v / 4;
				final int w3 = v % 4;
				result.setGrey(col, line * 3, w1);
				result.setGrey(col, line * 3 + 1, w2);
				result.setGrey(col, line * 3 + 2, w3);

			}
		}
		return result;
	}

	public List<String> encodeZ(BufferedImage img) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		final byte raw[] = new byte[width * height];
		int cpt = 0;
		final int coef = 16 / nbColor;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				final int color = getGrayOn16(img, x, y) / coef;
				raw[cpt++] = (byte) color;
			}
		}
		// final byte[] comp = new CompressionZlib().compress(raw);
		final byte[] comp = new CompressionZopfliZlib().compress(raw);
		return cut(new AsciiEncoderFinalZeros().encode(comp));
	}

	private List<String> encodeZSpiral(BufferedImage img) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		final byte raw[] = new byte[width * height];
		final int coef = 16 / nbColor;
		final SpiralOnRectangle spiral = new SpiralOnRectangle(width, height);
		for (int cpt = 0; cpt < width * height; cpt++) {
			final PairInt pt = spiral.nextPoint();
			final int color = getGrayOn16(img, pt.getX(), pt.getY()) / coef;
			raw[cpt] = (byte) color;
		}
		// final byte[] comp = new CompressionZlib().compress(raw);
		final byte[] comp = new CompressionZopfliZlib().compress(raw);
		return cut(new AsciiEncoderFinalZeros().encode(comp));
	}

	private List<String> cut(String s) {
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < s.length(); i += 120) {
			final int j = Math.min(i + 120, s.length());
			result.add(s.substring(i, j));

		}
		return Collections.unmodifiableList(result);
	}

	public Sprite buildSpriteZ(int width, int height, String compressed) {
		final byte comp[] = new AsciiEncoder().decode(compressed);
		try {
			final ByteArray img = new CompressionZlib().decompress(comp);
			final SpriteMonochrome result = new SpriteMonochrome(width, height, nbColor);
			int cpt = 0;
			for (int line = 0; line < result.getHeight(); line++) {
				for (int col = 0; col < result.getWidth(); col++) {
					result.setGrey(col, line, img.getByteAt(cpt++));

				}
			}
			return result;
		} catch (NoPlantumlCompressionException e) {
			e.printStackTrace();
			return null;
		}
	}

}
