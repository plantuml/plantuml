/*
 * Checks the glyphs.bin files read by org.scilab.forge.jlatexmath.GlyphOutlines
 * against the AWT rendering of the TTF files (dev-time tool, needs AWT).
 *
 * For every font loaded by JLaTeXMath (base, Cyrillic, Greek) and every glyph
 * of its glyphs.bin, the outline is compared with the one of java.awt.Font
 * (Area XOR, in % of the area of the AWT glyph). Prints the worst glyphs.
 *
 * Usage, from the repository root, after compiling src/main/java into "classes":
 *
 *   java -Djava.awt.headless=true -cp classes:src/main/resources tools/jlatexmath-glyphs/CheckGlyphOutlines.java
 */

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.scilab.forge.jlatexmath.FontInfo;
import org.scilab.forge.jlatexmath.GlyphOutlines;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.cyrillic.CyrillicRegistration;
import org.scilab.forge.jlatexmath.greek.GreekRegistration;

public class CheckGlyphOutlines {

	public static void main(String[] args) throws Exception {
				org.scilab.forge.jlatexmath.DefaultTeXFont.registerAlphabet(new CyrillicRegistration());
		org.scilab.forge.jlatexmath.DefaultTeXFont.registerAlphabet(new GreekRegistration());
		// the alphabets are loaded on demand, when a formula uses one of their characters
		new TeXFormula("x \\text{\u0416 \u03b1} \\mathbf{\u0416 \u03b1} \\mathit{\u0416 \u03b1} \\mathsf{\u0416 \u03b1} \\mathtt{\u0416 \u03b1}").createBufferedImage(0, 20, java.awt.Color.BLACK, null);
		int fonts = 0;
		int glyphs = 0;
		double worst = 0;
		String worstName = "";
		for (int id = 0; FontInfo.getFontInfo(id) != null; id++) {
			final FontInfo info = FontInfo.getFontInfo(id);
			final GlyphOutlines.GlyphFont gf = info.getGlyphs();
			if (gf == null) {
				System.out.println("NO GLYPHS for font id " + id);
				continue;
			}
			fonts++;
			final Font font = info.getFont().deriveFont((float) gf.getUnitsPerEm());
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
				final Area mine = new Area(path);
				final Area xor = new Area(mine);
				xor.exclusiveOr(awt);
				final double ref = area(awt);
				final double diff = area(xor);
				glyphs++;
				if (ref == 0 && diff == 0)
					continue;
				final double percent = 100.0 * diff / Math.max(ref, 1);
				if (percent > worst) {
					worst = percent;
					worstName = gf.getName() + " U+" + Integer.toHexString(code);
				}
			}
		}
		System.out.printf("%d fonts, %d glyphs, worst difference %.2f%% (%s)%n", fonts, glyphs, worst, worstName);
		if (worst > 1.0)
			System.exit(1);
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
