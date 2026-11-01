/* ForeignTextAtom.java
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
 * Text that has no TeX glyph. It is measured by the {@link TeXTextMeasurer} of
 * the environment (a rough estimate if there is none), and drawn by the canvas
 * with {@link TeXGraphics#drawText(String, double)}.
 */
public class ForeignTextAtom extends Atom {

    private final String text;

    public ForeignTextAtom(String text) {
        this.text = text;
    }

    public Box createBox(TeXEnvironment env) {
        final TeXTextMeasurer measurer = env.getTextMeasurer();
        final float[] size = measurer == null ? estimate(text) : measurer.measure(text);
        final float em = SpaceAtom.getFactor(TeXConstants.UNIT_EM, env);
        return new ForeignTextBox(text, size[0] * em, size[1] * em, size[2] * em, em);
    }

    /**
     * Without a measurer: a character of a wide script (CJK, Hangul, kana) is one
     * em wide, the others half an em.
     */
    static float[] estimate(String text) {
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (Character.isLowSurrogate(c))
                continue;
            width += (c >= 0x1100 && c <= 0x11FF) || (c >= 0x2E80 && c <= 0xD7FF) || (c >= 0xF900 && c <= 0xFAFF)
                     || (c >= 0xFF00 && c <= 0xFFEF) || Character.isHighSurrogate(c) ? 1f : 0.5f;
        }
        return new float[] {width, 0.8f, 0.2f};
    }
}
