/*
 * Checks the glyphs.bin files, read by org.scilab.forge.jlatexmath.GlyphOutlines,
 * against the AWT rendering of the TTF files (dev-time tool, needs AWT).
 *
 * For every font of the three glyphs.bin files and every glyph of it, the outline
 * is compared with the one of java.awt.Font made from the TTF file (Area XOR, in %
 * of the area of the AWT glyph). Prints the worst glyph, and exits with 1 if it
 * is over 1%.
 *
 * Usage, from the repository root, after compiling src/main/java into "classes":
 *
 *   java -Djava.awt.headless=true -cp classes tools/jlatexmath-glyphs/CheckGlyphOutlines.java
 */

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.scilab.forge.jlatexmath.GlyphOutlines;

public class CheckGlyphOutlines {

	private static final Path RES = Paths.get("src/main/resources/org/scilab/forge/jlatexmath");
	private static final Path TTF = Paths.get("tools/jlatexmath-glyphs/ttf");

	public static void main(String[] args) throws Exception {
		int fonts = 0;
		int glyphs = 0;
		double worst = 0;
		String worstName = "";
		for (String dir : new String[] { "fonts", "cyrillic/fonts", "greek/fonts" }) {
			final GlyphOutlines outlines = GlyphOutlines.parse(Files.readAllBytes(RES.resolve(dir).resolve("glyphs.bin")));
			for (String name : new TreeSet<String>(outlines.getFontNames())) {
				final GlyphOutlines.GlyphFont gf = outlines.getFont(name);
				final File ttf = find(TTF.resolve(dir), name + ".ttf");
				final Font font = Font.createFont(Font.TRUETYPE_FONT, ttf).deriveFont((float) gf.getUnitsPerEm());
				fonts++;
				for (int code = 0; code < 65536; code++) {
					if (gf.hasGlyph(code) == false)
						continue;
					final Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO);
					gf.visit(code, new GlyphOutlines.Sink() {
						public void moveTo(int x, int y) {
							path.moveTo(x, -y);
						}

						public void lineTo(int x, int y) {
							path.lineTo(x, -y);
						}

						public void quadTo(int cx, int cy, int x, int y) {
							path.quadTo(cx, -cy, x, -y);
						}

						public void close() {
							path.closePath();
						}
					});
					final Area awt = new Area(font.createGlyphVector(new FontRenderContext(null, true, true),
							new char[] { (char) code }).getOutline());
					final Area xor = new Area(new Area(path));
					xor.exclusiveOr(awt);
					final double ref = area(awt);
					final double diff = area(xor);
					glyphs++;
					if (ref == 0 && diff == 0)
						continue;
					final double percent = 100.0 * diff / Math.max(ref, 1);
					if (percent > worst) {
						worst = percent;
						worstName = name + " U+" + Integer.toHexString(code);
					}
				}
			}
		}
		System.out.printf("%d fonts, %d glyphs, worst difference %.2f%% (%s)%n", fonts, glyphs, worst, worstName);
		if (worst > 1.0)
			System.exit(1);
	}

	private static File find(Path dir, String fileName) throws Exception {
		try (Stream<Path> walk = Files.walk(dir)) {
			return walk.filter(p -> p.getFileName().toString().equals(fileName)).collect(Collectors.toList()).get(0)
					.toFile();
		}
	}

	private static double area(Area a) {
		// signed sum, so that the holes (opposite direction) are subtracted
		double total = 0;
		final PathIterator it = a.getPathIterator(new AffineTransform(), 0.05);
		final double[] c = new double[6];
		double sx = 0, sy = 0, px = 0, py = 0, sum = 0;
		while (it.isDone() == false) {
			switch (it.currentSegment(c)) {
			case PathIterator.SEG_MOVETO:
				sx = px = c[0];
				sy = py = c[1];
				sum = 0;
				break;
			case PathIterator.SEG_LINETO:
				sum += px * c[1] - c[0] * py;
				px = c[0];
				py = c[1];
				break;
			case PathIterator.SEG_CLOSE:
				sum += px * sy - sx * py;
				total += sum / 2;
				px = sx;
				py = sy;
				sum = 0;
				break;
			default:
				break;
			}
			it.next();
		}
		return Math.abs(total);
	}
}
