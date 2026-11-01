/*
 * Generates the glyph outlines of the JLaTeXMath fonts (glyphs.bin).
 *
 * Why: the TeX fonts of JLaTeXMath are not drawn by a font engine any more
 * (PLAN.md, phase 2): a glyph is a path given to UGraphic, so that the output
 * is the same on the JVM, in JavaScript, in Wasm and in the C build, and does
 * not depend on the machine. The outlines are extracted here, once, from the
 * TTF files, and committed.
 *
 * Usage, from the repository root (JDK 8+, no build needed):
 *
 *   java tools/jlatexmath-glyphs/GenerateGlyphOutlines.java
 *
 * It reads the TrueType tables directly (head, hhea, hmtx, maxp, loca, glyf, cmap), as
 * tools/font-metrics/GenerateFontMetrics.java does, and takes only the
 * characters that the XML metrics of the font declare (<Char code="...">): the
 * other glyphs of the TTF are never drawn.
 *
 * The TTF files are in tools/jlatexmath-glyphs/ttf (they are not needed at run
 * time, so they are not in the jar), with the same relative paths as the XML
 * metrics in src/main/resources/org/scilab/forge/jlatexmath. Three files are
 * written in the resources, next to the XML metrics:
 *   fonts/glyphs.bin            the base fonts (latin, maths, euler, base)
 *   cyrillic/fonts/glyphs.bin   the Cyrillic alphabet
 *   greek/fonts/glyphs.bin      the Greek alphabet
 *
 * Format of a file (big endian):
 *   "JLMG", u8 version (1), u16 number of fonts, then for each font
 *     UTF name (file name without .ttf), u16 unitsPerEm, u16 number of glyphs,
 *     then for each glyph
 *       u16 code (the character as the XML metrics name it), then commands:
 *         u8 command: 0 moveTo, 1 lineTo, 2 quadTo, 3 close, 4 end of glyph
 *         followed by 1 point (moveTo, lineTo) or 2 points (quadTo: control
 *         then end), each point as two zigzag varints dx, dy relative to the
 *         previous point (the first one to 0,0).
 *   The y axis is the one of the font (up), the unit is 1/unitsPerEm em.
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GenerateGlyphOutlines {

	private static final Path RES = Paths.get("src/main/resources/org/scilab/forge/jlatexmath");
	private static final Path TTF = Paths.get("tools/jlatexmath-glyphs/ttf");

	private static final int MOVE = 0;
	private static final int LINE = 1;
	private static final int QUAD = 2;
	private static final int CLOSE = 3;
	private static final int END = 4;

	public static void main(String[] args) throws Exception {
		generate("fonts");
		generate("cyrillic/fonts");
		generate("greek/fonts");
	}

	private static void generate(String relativeDir) throws Exception {
		final Path dir = TTF.resolve(relativeDir);
		final List<Path> ttfs;
		try (Stream<Path> walk = Files.walk(dir)) {
			ttfs = walk.filter(p -> p.toString().endsWith(".ttf")).sorted().collect(Collectors.toList());
		}
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream out = new DataOutputStream(bytes);
		out.writeBytes("JLMG");
		out.writeByte(1);
		out.writeShort(ttfs.size());
		int glyphs = 0;
		long ttfBytes = 0;
		for (Path ttf : ttfs) {
			final String name = ttf.getFileName().toString().replace(".ttf", "");
			final Path xml = RES.resolve(TTF.relativize(ttf)).resolveSibling(name + ".xml");
			if (Files.exists(xml) == false)
				throw new IOException("No metrics for " + ttf);
			final Font font = new Font(Files.readAllBytes(ttf));
			ttfBytes += Files.size(ttf);
			final TreeSet<Integer> codes = new TreeSet<Integer>();
			for (int code : declaredCodes(xml))
				if (font.glyphOf(code) == null)
					System.out.println("  " + name + ": code " + code + " is declared by the metrics but the font has no glyph for it, skipped");
				else
					codes.add(code);
			out.writeUTF(name);
			out.writeShort(font.unitsPerEm);
			out.writeShort(codes.size());
			for (int code : codes) {
				out.writeShort(code);
				writeOutline(out, font.outline(font.glyphOf(code)));
			}
			glyphs += codes.size();
		}
		out.flush();
		final Path target = RES.resolve(relativeDir).resolve("glyphs.bin");
		Files.write(target, bytes.toByteArray());
		System.out.printf("%-60s %3d fonts %5d glyphs %8d bytes (TTF files: %d bytes)%n", target, ttfs.size(), glyphs,
				bytes.size(), ttfBytes);
	}

	/** The characters that the metrics of a font describe: &lt;Char code="..."&gt;. */
	private static TreeSet<Integer> declaredCodes(Path xml) throws Exception {
		final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.toFile());
		final NodeList list = doc.getElementsByTagName("Char");
		final TreeSet<Integer> result = new TreeSet<Integer>();
		for (int i = 0; i < list.getLength(); i++)
			result.add(Integer.parseInt(((Element) list.item(i)).getAttribute("code")));
		return result;
	}

	// ------------------------------------------------------------------ output

	private static void writeOutline(DataOutputStream out, List<int[]> commands) throws IOException {
		int px = 0;
		int py = 0;
		for (int[] c : commands) {
			out.writeByte(c[0]);
			for (int k = 1; k + 1 < c.length; k += 2) {
				varint(out, c[k] - px);
				varint(out, c[k + 1] - py);
				px = c[k];
				py = c[k + 1];
			}
		}
		out.writeByte(END);
	}

	private static void varint(DataOutputStream out, int value) throws IOException {
		int v = (value << 1) ^ (value >> 31);
		while ((v & ~0x7F) != 0) {
			out.writeByte((v & 0x7F) | 0x80);
			v >>>= 7;
		}
		out.writeByte(v);
	}

	// ------------------------------------------------------------------ TTF reader

	private static class Font {
		private final ByteBuffer d;
		private final Map<String, int[]> tables = new HashMap<String, int[]>();
		final int unitsPerEm;
		private final int numGlyphs;
		private final boolean longLoca;
		private final Map<Integer, Integer> cmap = new TreeMap<Integer, Integer>();

		Font(byte[] data) {
			d = ByteBuffer.wrap(data);
			final int n = d.getShort(4) & 0xFFFF;
			for (int i = 0; i < n; i++) {
				final int p = 12 + 16 * i;
				final String tag = new String(data, p, 4, java.nio.charset.StandardCharsets.ISO_8859_1);
				tables.put(tag, new int[] { d.getInt(p + 8), d.getInt(p + 12) });
			}
			final int head = tables.get("head")[0];
			unitsPerEm = d.getShort(head + 18) & 0xFFFF;
			longLoca = d.getShort(head + 50) != 0;
			numGlyphs = d.getShort(tables.get("maxp")[0] + 4) & 0xFFFF;
			readCmap();
		}

		Integer glyphOf(int code) {
			return cmap.get(code);
		}

		private void readCmap() {
			final int base = tables.get("cmap")[0];
			final int nt = d.getShort(base + 2) & 0xFFFF;
			// Preference: (3,1) Unicode BMP, then (0,x), then (3,0) symbol, then (1,0) Macintosh.
			final int[] order = { 0x0301, 0x0003, 0x0300, 0x0100 };
			final Map<Integer, Integer> found = new HashMap<Integer, Integer>();
			for (int i = 0; i < nt; i++) {
				final int pid = d.getShort(base + 4 + 8 * i) & 0xFFFF;
				final int eid = d.getShort(base + 6 + 8 * i) & 0xFFFF;
				final int off = d.getInt(base + 8 + 8 * i);
				found.put(pid == 0 ? 0x0003 : (pid << 8) | eid, base + off);
			}
			for (int key : order)
				if (found.containsKey(key)) {
					readSubtable(found.get(key), key == 0x0300);
					return;
				}
			throw new IllegalStateException("no usable cmap");
		}

		private void readSubtable(int p, boolean symbol) {
			final int format = d.getShort(p) & 0xFFFF;
			if (format == 0) {
				for (int c = 0; c < 256; c++)
					put(c, d.get(p + 6 + c) & 0xFF, symbol);
			} else if (format == 6) {
				final int first = d.getShort(p + 6) & 0xFFFF;
				final int count = d.getShort(p + 8) & 0xFFFF;
				for (int i = 0; i < count; i++)
					put(first + i, d.getShort(p + 10 + 2 * i) & 0xFFFF, symbol);
			} else if (format == 4) {
				final int segX2 = d.getShort(p + 6) & 0xFFFF;
				final int seg = segX2 / 2;
				final int endP = p + 14;
				final int startP = endP + segX2 + 2;
				final int deltaP = startP + segX2;
				final int rangeP = deltaP + segX2;
				for (int s = 0; s < seg; s++) {
					final int end = d.getShort(endP + 2 * s) & 0xFFFF;
					final int start = d.getShort(startP + 2 * s) & 0xFFFF;
					final int delta = d.getShort(deltaP + 2 * s);
					final int range = d.getShort(rangeP + 2 * s) & 0xFFFF;
					for (int c = start; c <= end && c != 0xFFFF; c++) {
						int gid;
						if (range == 0)
							gid = (c + delta) & 0xFFFF;
						else {
							final int g = d.getShort(rangeP + 2 * s + range + 2 * (c - start)) & 0xFFFF;
							gid = g == 0 ? 0 : (g + delta) & 0xFFFF;
						}
						put(c, gid, symbol);
					}
				}
			} else {
				throw new IllegalStateException("cmap format " + format + " is not supported");
			}
		}

		private void put(int code, int gid, boolean symbol) {
			if (gid == 0)
				return;
			cmap.put(symbol && code >= 0xF000 ? code - 0xF000 : code, gid);
		}

		/** The outline of a glyph as commands {op, x, y, ...}, y up, in font units. */
		List<int[]> outline(int gid) {
			final List<int[]> result = new ArrayList<int[]>();
			final int glyf = tables.get("glyf")[0];
			final int loca = tables.get("loca")[0];
			final int from = (longLoca ? d.getInt(loca + 4 * gid) : (d.getShort(loca + 2 * gid) & 0xFFFF) * 2);
			final int to = (longLoca ? d.getInt(loca + 4 * (gid + 1)) : (d.getShort(loca + 2 * (gid + 1)) & 0xFFFF) * 2);
			if (from == to)
				return result; // blank glyph (space)
			int p = glyf + from;
			final int contours = d.getShort(p);
			p += 10;
			if (contours < 0)
				throw new IllegalStateException("composite glyph " + gid + ": not used by the JLaTeXMath fonts");
			simple(p, contours, result);
			// As FreeType (so Java2D) does: the outline is moved so that its left edge (xMin)
			// is at the left side bearing of hmtx. They differ by one unit for a few glyphs.
			final int shift = leftSideBearing(gid) - d.getShort(glyf + from + 2);
			if (shift != 0)
				for (int[] cmd : result)
					for (int k = 1; k < cmd.length; k += 2)
						cmd[k] += shift;
			return result;
		}

		private int leftSideBearing(int gid) {
			final int hmtx = tables.get("hmtx")[0];
			final int numberOfHMetrics = d.getShort(tables.get("hhea")[0] + 34) & 0xFFFF;
			if (gid < numberOfHMetrics)
				return d.getShort(hmtx + 4 * gid + 2);
			return d.getShort(hmtx + 4 * numberOfHMetrics + 2 * (gid - numberOfHMetrics));
		}

		private void simple(int p, int contours, List<int[]> result) {
			final int[] ends = new int[contours];
			for (int i = 0; i < contours; i++) {
				ends[i] = d.getShort(p) & 0xFFFF;
				p += 2;
			}
			final int n = contours == 0 ? 0 : ends[contours - 1] + 1;
			final int insLen = d.getShort(p) & 0xFFFF;
			p += 2 + insLen;
			final int[] flags = new int[n];
			for (int i = 0; i < n;) {
				final int f = d.get(p++) & 0xFF;
				flags[i++] = f;
				if ((f & 8) != 0) {
					int rep = d.get(p++) & 0xFF;
					while (rep-- > 0)
						flags[i++] = f;
				}
			}
			final int[] xs = new int[n];
			final int[] ys = new int[n];
			int v = 0;
			for (int i = 0; i < n; i++) {
				final int f = flags[i];
				if ((f & 2) != 0) {
					final int dx = d.get(p++) & 0xFF;
					v += (f & 16) != 0 ? dx : -dx;
				} else if ((f & 16) == 0) {
					v += d.getShort(p);
					p += 2;
				}
				xs[i] = v;
			}
			v = 0;
			for (int i = 0; i < n; i++) {
				final int f = flags[i];
				if ((f & 4) != 0) {
					final int dy = d.get(p++) & 0xFF;
					v += (f & 32) != 0 ? dy : -dy;
				} else if ((f & 32) == 0) {
					v += d.getShort(p);
					p += 2;
				}
				ys[i] = v;
			}
			int start = 0;
			for (int c = 0; c < contours; c++) {
				final int end = ends[c];
				contour(xs, ys, flags, start, end, result);
				start = end + 1;
			}
		}

		/** One closed contour of on-curve (flag 1) and off-curve points of a quadratic spline. */
		private void contour(int[] xs, int[] ys, int[] flags, int from, int to, List<int[]> out) {
			final int n = to - from + 1;
			if (n < 2)
				return;
			// A start on the curve: an on-curve point, or else the middle of two off-curve points.
			int first = -1;
			for (int i = 0; i < n; i++)
				if ((flags[from + i] & 1) != 0) {
					first = i;
					break;
				}
			int sx;
			int sy;
			int begin;
			if (first >= 0) {
				sx = xs[from + first];
				sy = ys[from + first];
				begin = first + 1;
			} else {
				sx = (xs[from] + xs[from + 1]) / 2;
				sy = (ys[from] + ys[from + 1]) / 2;
				begin = 1;
			}
			out.add(new int[] { MOVE, sx, sy });
			int cx = 0;
			int cy = 0;
			boolean pending = false;
			final int steps = first >= 0 ? n - 1 : n;
			for (int k = 0; k < steps; k++) {
				final int i = from + (begin + k) % n;
				final boolean on = (flags[i] & 1) != 0;
				if (on) {
					if (pending)
						out.add(new int[] { QUAD, cx, cy, xs[i], ys[i] });
					else
						out.add(new int[] { LINE, xs[i], ys[i] });
					pending = false;
				} else {
					if (pending)
						out.add(new int[] { QUAD, cx, cy, (cx + xs[i]) / 2, (cy + ys[i]) / 2 });
					cx = xs[i];
					cy = ys[i];
					pending = true;
				}
			}
			if (pending)
				out.add(new int[] { QUAD, cx, cy, sx, sy });
			out.add(new int[] { CLOSE });
		}
	}
}
