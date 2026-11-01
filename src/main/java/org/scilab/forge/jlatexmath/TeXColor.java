/* TeXColor.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3: no java.awt). It replaces java.awt.Color.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

/**
 * An immutable sRGB color with an alpha, 8 bits each. It has what the library
 * used of {@code java.awt.Color}, with the same results: the constants, the
 * constructors with integers (0 to 255) and with floats (0 to 1, rounded as AWT
 * does), and {@link #decode(String)}.
 */
public final class TeXColor {

    public static final TeXColor BLACK = new TeXColor(0, 0, 0);
    public static final TeXColor WHITE = new TeXColor(255, 255, 255);
    public static final TeXColor RED = new TeXColor(255, 0, 0);
    public static final TeXColor GREEN = new TeXColor(0, 255, 0);
    public static final TeXColor BLUE = new TeXColor(0, 0, 255);
    public static final TeXColor CYAN = new TeXColor(0, 255, 255);
    public static final TeXColor MAGENTA = new TeXColor(255, 0, 255);
    public static final TeXColor YELLOW = new TeXColor(255, 255, 0);

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public TeXColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    /**
     * @throws IllegalArgumentException if a component is not between 0 and 255
     */
    public TeXColor(int r, int g, int b, int a) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 || a < 0 || a > 255) {
            throw new IllegalArgumentException("Color parameter outside of expected range");
        }
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public TeXColor(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    /**
     * @throws IllegalArgumentException if a component is not between 0 and 1
     */
    public TeXColor(float r, float g, float b, float a) {
        this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
    }

    /**
     * Converts "#RRGGBB", "0xRRGGBB" or a decimal number.
     *
     * @throws NumberFormatException if the string is not a number
     */
    public static TeXColor decode(String s) {
        final int i = Integer.decode(s).intValue();
        return new TeXColor((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public boolean equals(Object o) {
        if (o instanceof TeXColor == false) {
            return false;
        }
        final TeXColor other = (TeXColor) o;
        return red == other.red && green == other.green && blue == other.blue && alpha == other.alpha;
    }

    public int hashCode() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public String toString() {
        return "TeXColor[r=" + red + ",g=" + green + ",b=" + blue + ",a=" + alpha + "]";
    }
}
