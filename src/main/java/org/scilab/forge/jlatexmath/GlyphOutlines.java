/* GlyphOutlines.java
 * =========================================================================
 * Reader of the glyphs.bin files (outlines of the JLaTeXMath fonts).
 *
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 2, step 2b): a glyph is a path, not a call to the font engine of AWT.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The outlines of the glyphs of the TeX fonts, read from a {@code glyphs.bin}
 * file made by {@code tools/jlatexmath-glyphs/GenerateGlyphOutlines.java} (the
 * format is described there).
 * <p>
 * No dependency on {@code java.awt}, {@code javax.*} or reflection: the same
 * code runs on the JVM and in the TeaVM builds, and gives the same numbers
 * everywhere. A glyph is given to a {@link Sink} as a sequence of absolute
 * points, in font units, y axis up.
 */
public final class GlyphOutlines {

	private static final int MOVE = 0;
	private static final int LINE = 1;
	private static final int QUAD = 2;
	private static final int CLOSE = 3;
	private static final int END = 4;

	/** Receives the outline of a glyph. */
	public interface Sink {

		void moveTo(int x, int y);

		void lineTo(int x, int y);

		/** Quadratic curve from the current point. */
		void quadTo(int controlX, int controlY, int x, int y);

		void close();
	}

	/** The glyphs of one font (one TTF file). */
	public static final class GlyphFont {

		private final byte[] data;
		private final String name;
		private final int unitsPerEm;
		/** The codes, in increasing order. */
		private final int[] codes;
		/** For each code, the offset in data of its first command. */
		private final int[] starts;

		private GlyphFont(byte[] data, String name, int unitsPerEm, int[] codes, int[] starts) {
			this.data = data;
			this.name = name;
			this.unitsPerEm = unitsPerEm;
			this.codes = codes;
			this.starts = starts;
		}

		/** The name of the TTF file, without ".ttf". */
		public String getName() {
			return name;
		}

		/** The size of the em square: one unit of the outlines is 1/unitsPerEm em. */
		public int getUnitsPerEm() {
			return unitsPerEm;
		}

		public int getNumberOfGlyphs() {
			return codes.length;
		}

		public boolean hasGlyph(int code) {
			return indexOf(code) >= 0;
		}

		/**
		 * Gives the outline of a glyph to the sink.
		 *
		 * @return false if the font has no glyph for this code (nothing is given to
		 *         the sink); a blank glyph (a space) returns true and draws nothing.
		 */
		public boolean visit(int code, Sink sink) {
			final int index = indexOf(code);
			if (index < 0)
				return false;
			final int[] pos = { starts[index] };
			int x = 0;
			int y = 0;
			while (true) {
				final int command = data[pos[0]++] & 0xFF;
				switch (command) {
				case MOVE:
					x += varint(data, pos);
					y += varint(data, pos);
					sink.moveTo(x, y);
					break;
				case LINE:
					x += varint(data, pos);
					y += varint(data, pos);
					sink.lineTo(x, y);
					break;
				case QUAD:
					final int cx = x + varint(data, pos);
					final int cy = y + varint(data, pos);
					x = cx + varint(data, pos);
					y = cy + varint(data, pos);
					sink.quadTo(cx, cy, x, y);
					break;
				case CLOSE:
					sink.close();
					break;
				case END:
					return true;
				default:
					throw new IllegalStateException("Bad command " + command + " in glyphs of " + name);
				}
			}
		}

		private int indexOf(int code) {
			int low = 0;
			int high = codes.length - 1;
			while (low <= high) {
				final int mid = (low + high) >>> 1;
				if (codes[mid] < code)
					low = mid + 1;
				else if (codes[mid] > code)
					high = mid - 1;
				else
					return mid;
			}
			return -1;
		}
	}

	private final Map<String, GlyphFont> fonts = new HashMap<String, GlyphFont>();

	private GlyphOutlines() {
	}

	/** The names of the fonts of this file (TTF file names without ".ttf"). */
	public Set<String> getFontNames() {
		return Collections.unmodifiableSet(fonts.keySet());
	}

	/** The font of a TTF file name, with or without ".ttf", or null. */
	public GlyphFont getFont(String ttfName) {
		if (ttfName.endsWith(".ttf"))
			ttfName = ttfName.substring(0, ttfName.length() - 4);
		return fonts.get(ttfName);
	}

	// ------------------------------------------------------------------ loading

	private static final Map<Class<?>, GlyphOutlines> CACHE = new HashMap<Class<?>, GlyphOutlines>();

	/**
	 * The outlines that go with the fonts of a registration object: the file
	 * {@code fonts/glyphs.bin} of the package of its class. {@code base} is null
	 * for the base fonts, the registration object for an alphabet (Cyrillic,
	 * Greek), as in {@link FontInfo}.
	 * <p>
	 * Read on the first use only, nothing is parsed by a diagram without math.
	 */
	public static synchronized GlyphOutlines forBase(Object base) {
		final Class<?> clazz = base == null ? GlyphOutlines.class : base.getClass();
		GlyphOutlines result = CACHE.get(clazz);
		if (result == null) {
			// TODO phase 3: resource abstraction (class path, EmbeddedFiles, bundle).
			final InputStream in = clazz.getResourceAsStream("fonts/glyphs.bin");
			if (in == null)
				throw new IllegalStateException("No fonts/glyphs.bin next to " + clazz.getName());
			try {
				result = parse(readAll(in));
			} catch (IOException e) {
				throw new IllegalStateException("Cannot read fonts/glyphs.bin of " + clazz.getName(), e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					// Nothing to do
				}
			}
			CACHE.put(clazz, result);
		}
		return result;
	}

	private static byte[] readAll(InputStream in) throws IOException {
		byte[] buffer = new byte[1 << 16];
		int size = 0;
		while (true) {
			if (size == buffer.length) {
				final byte[] bigger = new byte[buffer.length * 2];
				System.arraycopy(buffer, 0, bigger, 0, size);
				buffer = bigger;
			}
			final int n = in.read(buffer, size, buffer.length - size);
			if (n < 0)
				break;
			size += n;
		}
		final byte[] result = new byte[size];
		System.arraycopy(buffer, 0, result, 0, size);
		return result;
	}

	/** Parses the content of a glyphs.bin file: finds the glyphs, decodes none. */
	public static GlyphOutlines parse(byte[] data) {
		if (data.length < 7 || data[0] != 'J' || data[1] != 'L' || data[2] != 'M' || data[3] != 'G')
			throw new IllegalArgumentException("Not a glyphs.bin file");
		if (data[4] != 1)
			throw new IllegalArgumentException("Unknown glyphs.bin version " + data[4]);
		final GlyphOutlines result = new GlyphOutlines();
		final int[] pos = { 5 };
		final int numberOfFonts = u16(data, pos);
		for (int f = 0; f < numberOfFonts; f++) {
			final int nameLength = u16(data, pos);
			// DataOutputStream.writeUTF: the names are plain ASCII
			final char[] chars = new char[nameLength];
			for (int i = 0; i < nameLength; i++)
				chars[i] = (char) (data[pos[0]++] & 0xFF);
			final String name = new String(chars);
			final int unitsPerEm = u16(data, pos);
			final int numberOfGlyphs = u16(data, pos);
			final int[] codes = new int[numberOfGlyphs];
			final int[] starts = new int[numberOfGlyphs];
			for (int g = 0; g < numberOfGlyphs; g++) {
				codes[g] = u16(data, pos);
				starts[g] = pos[0];
				skipOutline(data, pos);
				if (g > 0 && codes[g] <= codes[g - 1])
					throw new IllegalArgumentException("Codes not sorted in " + name);
			}
			result.fonts.put(name, new GlyphFont(data, name, unitsPerEm, codes, starts));
		}
		return result;
	}

	private static void skipOutline(byte[] data, int[] pos) {
		while (true) {
			final int command = data[pos[0]++] & 0xFF;
			switch (command) {
			case MOVE:
			case LINE:
				varint(data, pos);
				varint(data, pos);
				break;
			case QUAD:
				varint(data, pos);
				varint(data, pos);
				varint(data, pos);
				varint(data, pos);
				break;
			case CLOSE:
				break;
			case END:
				return;
			default:
				throw new IllegalArgumentException("Bad command " + command);
			}
		}
	}

	private static int u16(byte[] data, int[] pos) {
		final int result = ((data[pos[0]] & 0xFF) << 8) | (data[pos[0] + 1] & 0xFF);
		pos[0] += 2;
		return result;
	}

	/** Zigzag varint. */
	private static int varint(byte[] data, int[] pos) {
		int result = 0;
		int shift = 0;
		while (true) {
			final int b = data[pos[0]++] & 0xFF;
			result |= (b & 0x7F) << shift;
			if ((b & 0x80) == 0)
				break;
			shift += 7;
		}
		return (result >>> 1) ^ -(result & 1);
	}
}
