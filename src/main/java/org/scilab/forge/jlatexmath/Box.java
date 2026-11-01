/* Box.java
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

/**
 * An abstract graphical representation of a formula, that can be painted. All characters, font
 * sizes, positions are fixed. Only special Glue boxes could possibly stretch or shrink.
 * A box has 3 dimensions (width, height and depth), can be composed of other child boxes
 * that can possibly be shifted (up, down, left or right). Child boxes can also be positioned
 * outside their parent's box (defined by it's dimensions).
 * <p>
 * Subclasses must implement the abstract {@link #draw(Graphics2D, float, float)} method
 * (that paints the box). <b> This implementation must start with calling the method
 * {@link #startDraw(Graphics2D, float, float)} and end with calling the method
 * {@link #endDraw(Graphics2D)} to set and restore the color's that must be used for
 * painting the box and to draw the background!</b> They must also implement the abstract
 * {@link #getLastFontId()} method (the last font
 * that will be used when this box will be painted).
 */
public abstract class Box {

    public static boolean DEBUG = false;

    /**
     * The foreground color of the whole box. Child boxes can override this color.
     * If it's null and it has a parent box, the foreground color of the parent will
     * be used. If it has no parent, the foreground color of the component on which it
     * will be painted, will be used.
     */
    protected Color foreground;

    /**
     * The background color of the whole box. Child boxes can paint a background on top of
     * this background. If it's null, no background will be painted.
     */
    protected Color background;

    private Color prevColor; // used temporarily in startDraw and endDraw

    /**
     * The width of this box, i.e. the value that will be used for further
     * calculations.
     */
    protected float width = 0;

    /**
     * The height of this box, i.e. the value that will be used for further
     * calculations.
     */
    protected float height = 0;

    /**
     * The depth of this box, i.e. the value that will be used for further
     * calculations.
     */
    protected float depth = 0;

    /**
     * The shift amount: the meaning depends on the particular kind of box
     * (up, down, left, right)
     */
    protected float shift = 0;

    protected int type = -1;

    /**
     * List of child boxes
     */
    protected LinkedList<Box> children = new LinkedList<Box>();
    protected Box parent;
    protected Box elderParent;
    protected Color markForDEBUG;

    /**
     * Inserts the given box at the end of the list of child boxes.
     *
     * @param b the box to be inserted
     */
    public void add(Box b) {
        children.add(b);
        b.parent = this;
        b.elderParent = elderParent;
    }

    /**
     * Inserts the given box at the given position in the list of child boxes.
     *
     * @param pos the position at which to insert the given box
     * @param b the box to be inserted
     */
    public void add(int pos, Box b) {
        children.add(pos, b);
        b.parent = this;
        b.elderParent = elderParent;
    }

    /**
     * Creates an empty box (no children) with all dimensions set to 0 and no
     * foreground and background color set (default values will be used: null)
     */
    protected Box() {
        this(null, null);
    }

    /**
     * Creates an empty box (no children) with all dimensions set to 0 and sets
     * the foreground and background color of the box.
     *
     * @param fg the foreground color
     * @param bg the background color
     */
    protected Box(Color fg, Color bg) {
        foreground = fg;
        background = bg;
    }

    public void setParent(Box parent) {
        this.parent = parent;
    }

    public Box getParent() {
        return parent;
    }

    public void setElderParent(Box elderParent) {
        this.elderParent = elderParent;
    }

    public Box getElderParent() {
        return elderParent;
    }

    /**
     * Get the width of this box.
     *
     * @return the width of this box
     */
    public float getWidth() {
        return width;
    }

    public void negWidth() {
        width = -width;
    }

    /**
     * Get the height of this box.
     *
     * @return the height of this box
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get the depth of this box.
     *
     * @return the depth of this box
     */
    public float getDepth() {
        return depth;
    }

    /**
     * Get the shift amount for this box.
     *
     * @return the shift amount
     */
    public float getShift() {
        return shift;
    }

    /**
     * Set the width for this box.
     *
     * @param w the width
     */
    public void setWidth(float w) {
        width = w;
    }

    /**
     * Set the depth for this box.
     *
     * @param d the depth
     */
    public void setDepth(float d) {
        depth = d;
    }

    /**
     * Set the height for this box.
     *
     * @param h the height
     */
    public void setHeight(float h) {
        height = h;
    }

    /**
     * Set the shift amount for this box.
     *
     * @param s the shift amount
     */
    public void setShift(float s) {
        shift = s;
    }

    /**
     * Paints this box at the given coordinates using the given graphics context.
     *
     * @param g2 the graphics (2D) context to use for painting
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public abstract void draw(Graphics2D g2, float x, float y);

    /**
     * Get the id of the font that will be used the last when this box will be painted.
     *
     * @return the id of the last font that will be used.
     */
    public abstract int getLastFontId();

    /**
     * Stores the old color setting, draws the background of the box (if not null)
     * and sets the foreground color (if not null).
     *
     * @param g2 the graphics (2D) context
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    protected void startDraw(Graphics2D g2, float x, float y) {
        // old color
        prevColor = g2.getColor();
        if (background != null) { // draw background
            g2.setColor(background);
            g2.fill(new Rectangle2D.Float(x, y - height, width, height + depth));
        }
        if (foreground == null) {
            g2.setColor(prevColor); // old foreground color
        } else {
            g2.setColor(foreground); // overriding foreground color
        }
        drawDebug(g2, x, y);
    }

    protected void drawDebug(Graphics2D g2, float x, float y, boolean showDepth) {
        if (DEBUG) {
            Stroke st = g2.getStroke();
            if (markForDEBUG != null) {
                Color c = g2.getColor();
                g2.setColor(markForDEBUG);
                g2.fill(new Rectangle2D.Float(x, y - height, width, height + depth));
                g2.setColor(c);
            }
            g2.setStroke(new BasicStroke((float) (Math.abs(1 / g2.getTransform().getScaleX())), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            if (width < 0) {
                x += width;
                width = -width;
            }
            g2.draw(new Rectangle2D.Float(x, y - height, width, height + depth));
            if (showDepth) {
                Color c = g2.getColor();
                g2.setColor(Color.RED);
                if (depth > 0) {
                    g2.fill(new Rectangle2D.Float(x, y, width, depth));
                    g2.setColor(c);
                    g2.draw(new Rectangle2D.Float(x, y, width, depth));
                } else if (depth < 0) {
                    g2.fill(new Rectangle2D.Float(x, y + depth, width, -depth));
                    g2.setColor(c);
                    g2.draw(new Rectangle2D.Float(x, y + depth, width, -depth));
                } else {
                    g2.setColor(c);
                }
            }
            g2.setStroke(st);
        }
    }

    protected void drawDebug(Graphics2D g2, float x, float y) {
        if (DEBUG) {
            drawDebug(g2, x, y, true);
        }
    }

    /**
     * Restores the previous color setting.
     *
     * @param g2 the graphics (2D) context
     */
    protected void endDraw(Graphics2D g2) {
        g2.setColor(prevColor);
    }
}
