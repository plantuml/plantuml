/* TeXInsets.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3: no java.awt). It replaces java.awt.Insets.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

/**
 * The space around a {@link TeXIcon}, in pixels (what {@code java.awt.Insets}
 * was).
 */
public class TeXInsets {

    public int top;
    public int left;
    public int bottom;
    public int right;

    public TeXInsets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
