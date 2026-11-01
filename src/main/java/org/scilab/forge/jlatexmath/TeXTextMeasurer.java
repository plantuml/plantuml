/* TeXTextMeasurer.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (text that has no TeX glyph, such as CJK, is drawn by the host application).
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

/**
 * Measures the text that no TeX font can draw (Chinese, Japanese, Korean,
 * Arabic, ... or any character without a TeX symbol). The TeX fonts have the
 * glyphs of the TeX alphabets only; the rest is drawn by the application that
 * hosts the library, with its own font, through
 * {@link TeXGraphics#drawText(String, double)}.
 * <p>
 * The measure is given here because the layout of a formula needs the size of
 * the text before anything is drawn. It is given to the layout by
 * {@link TeXFormula.TeXIconBuilder#setTextMeasurer(TeXTextMeasurer)}, not kept
 * in a static field, so that formulas made at the same time in several threads
 * can use different measurers.
 */
public interface TeXTextMeasurer {

	/**
	 * @return the size of the text for a font size of 1 (so, in em): the width,
	 *         the height above the baseline, and the depth below the baseline.
	 */
	float[] measure(String text);

}
