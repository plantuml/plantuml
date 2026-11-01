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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

/**
 * An {@link javax.swing.Icon} implementation that will paint the TeXFormula
 * that created it.
 * <p>
 * This class cannot be instantiated directly. It can be constructed from a
 * TeXFormula using the {@link TeXFormula#createTeXIcon(int,float)} method.
 *
 * @author Kurt Vermeulen
 */
public class TeXIcon implements Icon {

    private static final Color defaultColor = new Color(0, 0, 0);

    public static float defaultSize = -1;
    public static float magFactor = 0;

    private Box box;

    private final float size;

    private Insets insets = new Insets(0, 0, 0, 0);

    private Color fg = null;

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

    public void setForeground(Color fg) {
        this.fg = fg;
    }

    /**
     * Get the insets of the TeXIcon.
     *
     * @return the insets
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * Set the insets of the TeXIcon.
     *
     * @param insets the insets
     * @param trueValues true to force the true values
     */
    public void setInsets(Insets insets, boolean trueValues) {
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
    public void setInsets(Insets insets) {
        setInsets(insets, false);
    }

    /**
     * Change the width of the TeXIcon. The new width must be greater than the current
     * width, otherwise the icon will remain unchanged. The formula will be aligned to the
     * left ({@linkplain TeXConstants#ALIGN_LEFT}), to the right
     * ({@linkplain TeXConstants#ALIGN_RIGHT}) or will be centered
     * in the middle ({@linkplain TeXConstants#ALIGN_CENTER}).
     *
     * @param width the new width of the TeXIcon
     * @param alignment a horizontal alignment constant: LEFT, RIGHT or CENTER
     */
    public void setIconWidth(int width, int alignment) {
        float diff = width - getIconWidth();
        if (diff > 0)
            box = new HorizontalBox(box, box.getWidth() + diff, alignment);
    }

    /**
     * Change the height of the TeXIcon. The new height must be greater than the current
     * height, otherwise the icon will remain unchanged. The formula will be aligned on top
     * (TeXConstants.TOP), at the bottom (TeXConstants.BOTTOM) or will be centered
     * in the middle (TeXConstants.CENTER).
     *
     * @param height the new height of the TeXIcon
     * @param alignment a vertical alignment constant: TOP, BOTTOM or CENTER
     */
    public void setIconHeight(int height, int alignment) {
        float diff = height - getIconHeight();
        if (diff > 0)
            box = new VerticalBox(box, diff, alignment);
    }

    /**
     * Get the total height of the TeXIcon. This also includes the insets.
     */
    public int getIconHeight() {
        return ((int) ((box.getHeight()) * size + 0.99 + insets.top)) +  ((int) ((box.getDepth()) * size + 0.99 + insets.bottom));
    }

    /**
     * Get the total height of the TeXIcon. This also includes the insets.
     */
    public int getIconDepth() {
        return (int) (box.getDepth() * size + 0.99 + insets.bottom);
    }

    /**
     * Get the total width of the TeXIcon. This also includes the insets.
     */

    public int getIconWidth() {
        return (int) (box.getWidth() * size + 0.99 + insets.left + insets.right);
    }

    public float getTrueIconHeight() {
        return (box.getHeight() + box.getDepth()) * size;
    }

    /**
     * Get the total height of the TeXIcon. This also includes the insets.
     */
    public float getTrueIconDepth() {
        return box.getDepth() * size;
    }

    /**
     * Get the total width of the TeXIcon. This also includes the insets.
     */

    public float getTrueIconWidth() {
        return box.getWidth() * size;
    }

    public float getBaseLine() {
        return (float)( (box.getHeight() * size + 0.99 + insets.top) /
                        ((box.getHeight() + box.getDepth()) * size + 0.99 + insets.top + insets.bottom));
    }

    public Box getBox() {
        return box;
    }

    /**
     * Paint the {@link TeXFormula} that created this icon.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        // copy graphics settings
        RenderingHints oldHints = g2.getRenderingHints();
        AffineTransform oldAt = g2.getTransform();
        Color oldColor = g2.getColor();

        // new settings
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.scale(size, size); // the point size
        if (fg != null) {
            g2.setColor(fg);
        } else if (c != null) {
            g2.setColor(c.getForeground()); // foreground will be used as default painting color
        } else {
            g2.setColor(defaultColor);
        }

        // draw formula box
        box.draw(g2, (x + insets.left) / size, (y + insets.top) / size+ box.getHeight());

        // restore graphics settings
        g2.setRenderingHints(oldHints);
        g2.setTransform(oldAt);
        g2.setColor(oldColor);
    }
}
