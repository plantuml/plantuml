/* TeXIcon.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

/* Modified by Calixte Denizet */


package org.scilab.forge.jlatexmath;



/**
 * The icon of a TeXFormula: its size, and the way to paint it on a
 * {@link TeXGraphics}.
 * <p>
 * This class cannot be instantiated directly. It can be constructed from a
 * TeXFormula using the {@link TeXFormula#createTeXIcon(int,float)} method.
 *
 * @author Kurt Vermeulen
 */
public class TeXIcon {


    public static float defaultSize = -1;
    public static float magFactor = 0;

    private Box box;

    private final float size;

    private TeXInsets insets = new TeXInsets(0, 0, 0, 0);

    private TeXColor fg = null;

    public boolean isColored = false;

    /**
     * Creates a new icon that will paint the given formula box in the given point size.
     *
     * @param b the formula box to be painted
     * @param size the point size
     */
    protected TeXIcon(Box b, float size) {
        this(b, size, false);
    }

    protected TeXIcon(Box b, float size, boolean trueValues) {
        box = b;

        if (defaultSize != -1) {
            size = defaultSize;
        }

        if (magFactor != 0) {
            this.size = size * Math.abs(magFactor);
        } else {
            this.size = size;
        }

        /* I add this little value because it seems that tftopl calculates badly
           the height and the depth of certains characters.
        */
        if (!trueValues) {
            insets.top += (int)(0.18f * size);
            insets.bottom += (int)(0.18f * size);
            insets.left += (int)(0.18f * size);
            insets.right += (int)(0.18f * size);
        }
    }

    public void setForeground(TeXColor fg) {
        this.fg = fg;
    }

    /**
     * Set the insets of the TeXIcon.
     *
     * @param insets the insets
     * @param trueValues true to force the true values
     */
    public void setInsets(TeXInsets insets, boolean trueValues) {
        this.insets = insets;
        if (!trueValues) {
            this.insets.top += (int)(0.18f * size);
            this.insets.bottom += (int)(0.18f * size);
            this.insets.left += (int)(0.18f * size);
            this.insets.right += (int)(0.18f * size);
        }
    }

    /**
     * Set the insets of the TeXIcon.
     *
     * @param insets the insets
     */
    public void setInsets(TeXInsets insets) {
        setInsets(insets, false);
    }

    /**
     * Get the total height of the TeXIcon. This also includes the insets.
     */
    public int getIconHeight() {
        return ((int) ((box.getHeight()) * size + 0.99 + insets.top)) +  ((int) ((box.getDepth()) * size + 0.99 + insets.bottom));
    }

    /**
     * Get the total width of the TeXIcon. This also includes the insets.
     */

    public int getIconWidth() {
        return (int) (box.getWidth() * size + 0.99 + insets.left + insets.right);
    }

    /**
     * Get the total width of the TeXIcon. This also includes the insets.
     */

    /**
     * Paints the formula on a canvas that is not AWT (see PLAN.md, phase 2). The
     * origin of the canvas is the top left corner of the icon; the foreground
     * color is the one of {@link #setForeground(TeXColor)}, else the one given. If
     * both are null, the color of the canvas is kept.
     */
    public void paint(TeXGraphics g2, TeXColor defaultForeground, int x, int y) {
        final TeXColor oldColor = g2.getColor();
        g2.saveTransform();
        g2.scale(size, size); // the point size
        final TeXColor foreground = fg != null ? fg : defaultForeground;
        if (foreground != null)
            g2.setColor(foreground);
        box.draw(g2, (x + insets.left) / size, (y + insets.top) / size + box.getHeight());
        g2.restoreTransform();
        g2.setColor(oldColor);
    }
}
