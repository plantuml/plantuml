/* ForeignTextBox.java
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
 * The box of a {@link ForeignTextAtom}: its text, drawn by the canvas.
 */
public class ForeignTextBox extends Box {

    private final String text;
    private final float fontSize;

    /**
     * @param fontSize the font size, in the units of the box (the size of 1 em)
     */
    public ForeignTextBox(String text, float width, float height, float depth, float fontSize) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.fontSize = fontSize;
    }

    public void draw(TeXGraphics g2, float x, float y) {
        drawDebug(g2, x, y);
        g2.saveTransform();
        g2.translate(x, y);
        g2.drawText(text, fontSize);
        g2.restoreTransform();
    }

    public int getLastFontId() {
        return TeXFont.NO_FONT;
    }
}
