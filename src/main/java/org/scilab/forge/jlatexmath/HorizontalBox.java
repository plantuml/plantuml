/* HorizontalBox.java
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

package org.scilab.forge.jlatexmath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A box composed of a horizontal row of child boxes.
 */
public class HorizontalBox extends Box {

    private float curPos = 0; // NOPMD
    protected List<Integer> breakPositions;

    public HorizontalBox(Box b, float w, int alignment) {
        if (w != Float.POSITIVE_INFINITY) {
            float rest = w - b.getWidth();
            if (rest > 0) {
                if (alignment == TeXConstants.ALIGN_CENTER || alignment == TeXConstants.ALIGN_NONE) {
                    StrutBox s = new StrutBox(rest / 2, 0, 0, 0);
                    add(s);
                    add(b);
                    add(s);
                } else if (alignment == TeXConstants.ALIGN_LEFT) {
                    add(b);
                    add(new StrutBox(rest, 0, 0, 0));
                } else if (alignment == TeXConstants.ALIGN_RIGHT) {
                    add(new StrutBox(rest, 0, 0, 0));
                    add(b);
                } else {
                    add(b);
                }
            } else {
                add(b);
            }
        } else {
            add(b);
        }
    }

    public HorizontalBox(Box b) {
        add(b);
    }

    public HorizontalBox() {
        // basic horizontal box
    }

    public HorizontalBox(Color fg, Color bg) {
        super(fg, bg);
    }

    public HorizontalBox cloneBox() {
        HorizontalBox b = new HorizontalBox(foreground, background);
        b.shift = shift;

        return b;
    }

    public void draw(Graphics2D g2, float x, float y) {
        startDraw(g2, x, y);
        float xPos = x;
        for (Box box: children) {
            /*int i = children.indexOf(box);
              if (breakPositions != null && breakPositions.indexOf(i) != -1) {
              box.markForDEBUG = java.awt.Color.BLUE;
              }*/

            box.draw(g2, xPos, y + box.shift);
            xPos += box.getWidth();
        }
        endDraw(g2);
    }

    public final void add(Box b) {
        recalculate(b);
        super.add(b);
    }

    public final void add(int pos, Box b) {
        recalculate(b);
        super.add(pos, b);
    }

    private void recalculate(Box b) {
        // Commented for ticket 764
        // \left(\!\!\!\begin{array}{c}n\\\\r\end{array}\!\!\!\right)+123
        //curPos += b.getWidth();
        //width = Math.max(width, curPos);
        width += b.getWidth();
        height = Math.max((children.size() == 0 ? Float.NEGATIVE_INFINITY : height), b.height - b.shift);
        depth = Math.max((children.size() == 0 ? Float.NEGATIVE_INFINITY : depth), b.depth + b.shift);
    }

    public int getLastFontId() {
        // iterate from the last child box to the first untill a font id is found
        // that's not equal to NO_FONT
        int fontId = TeXFont.NO_FONT;
        for (ListIterator<Box> it = children.listIterator(children.size()); fontId == TeXFont.NO_FONT && it.hasPrevious();)
            fontId = ((Box) it.previous()).getLastFontId();

        return fontId;
    }

    public void addBreakPosition(int pos) {
        if (breakPositions == null) {
            breakPositions = new ArrayList<Integer>();
        }
        breakPositions.add(pos);
    }

    protected HorizontalBox[] split(int position) {
        return split(position, 1);
    }

    protected HorizontalBox[] splitRemove(int position) {
        return split(position, 2);
    }

    private HorizontalBox[] split(int position, int shift) {
        HorizontalBox hb1 = cloneBox();
        HorizontalBox hb2 = cloneBox();
        for (int i = 0; i <= position; i++) {
            hb1.add(children.get(i));
        }

        for (int i = position + shift; i < children.size(); i++) {
            hb2.add(children.get(i));
        }

        if (breakPositions != null) {
            for (int i = 0; i < breakPositions.size(); i++) {
                if (breakPositions.get(i) > position + 1) {
                    hb2.addBreakPosition(breakPositions.get(i) - position - 1);
                }
            }
        }

        return new HorizontalBox[] {hb1, hb2};
    }
}
