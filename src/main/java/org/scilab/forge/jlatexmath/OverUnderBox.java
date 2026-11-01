/* OverUnderBox.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009-2010 DENIZET Calixte
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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * A box representing another box with a delimiter box and a script box above or under it,
 * with script and delimiter seperated by a kern.
 */
public class OverUnderBox extends Box {

    // base, delimiter and script atom
    private final Box base;
    private final Box del;
    private final Box script;

    // kern amount between the delimiter and the script
    private final float kern;

    // whether the delimiter should be drawn over (<-> under) the base atom
    private final boolean over;

    /**
     * the parameter boxes must have an equal width!!
     *
     * @param b
     *           base box to be drawn on the baseline
     * @param d
     *           delimiter box
     * @param script
     *           subscript or superscript box
     * @param over
     *           true : draws delimiter and script box above the base box, false : under the
     *           base box
     */
    public OverUnderBox(Box b, Box d, Box script, float kern, boolean over) {
        base = b;
        del = d;
        this.script = script;
        this.kern = kern;
        this.over = over;

        // calculate metrics of the box
        width = b.getWidth();
        height = b.height
                 + (over ? d.getWidth() : 0)
                 + (over && script != null ? script.height + script.depth + kern : 0);
        depth = b.depth
                + (over ? 0 : d.getWidth())
                + (!over && script != null ? script.height + script.depth + kern : 0);
    }

    public void draw(Graphics2D g2, float x, float y) {
        drawDebug(g2, x, y);
        base.draw(g2, x, y);

        float yVar = y - base.height - del.getWidth();
        del.setDepth(del.getHeight() + del.getDepth());
        del.setHeight(0);
        if (over) { // draw delimiter and script above base box
            double transX = x + (del.height + del.depth) * 0.75, transY = yVar;
            AffineTransform oldAt = g2.getTransform();
            g2.translate(transX, transY);
            g2.rotate(Math.PI / 2);
            del.draw(g2, 0, 0);
            g2.setTransform(oldAt);

            // draw superscript
            if (script != null) {
                script.draw(g2, x, yVar - kern - script.depth);
            }
        }

        yVar = y + base.depth;
        if (!over) { // draw delimiter and script under base box
            double transX = x + (del.getHeight() + del.depth) * 0.75, transY = yVar;
            AffineTransform oldAt = g2.getTransform();
            g2.translate(transX, transY);
            g2.rotate(Math.PI / 2);
            del.draw(g2, 0, 0);
            g2.setTransform(oldAt);
            yVar += del.getWidth();

            // draw subscript
            if (script != null) {
                script.draw(g2, x, yVar + kern + script.height);
            }
        }
    }

    public int getLastFontId() {
        return base.getLastFontId();
    }
}
