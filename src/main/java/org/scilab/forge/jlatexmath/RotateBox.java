/* RotateBox.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2009-2011 DENIZET Calixte
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
import java.awt.geom.Point2D;

/**
 * A box representing a rotated box.
 */
public class RotateBox extends Box {

    public static final int BL = 0;
    public static final int BC = 1;
    public static final int BR = 2;
    public static final int TL = 3;
    public static final int TC = 4;
    public static final int TR = 5;
    public static final int BBL = 6;
    public static final int BBR = 7;
    public static final int BBC = 8;
    public static final int CL = 9;
    public static final int CC = 10;
    public static final int CR = 11;

    protected double angle = 0;
    private Box box;
    private float xmax, xmin, ymax, ymin;

    private float shiftX;
    private float shiftY;

    public RotateBox(Box b, double angle, float x, float y) {
        this.box = b;
        this.angle = angle * Math.PI / 180;
        height = b.height;
        depth = b.depth;
        width = b.width;
        double s = Math.sin(this.angle);
        double c = Math.cos(this.angle);
        shiftX = (float) (x * (1 - c) + y * s);
        shiftY = (float) (y * (1 - c) - x * s);
        xmax = (float) Math.max(-height * s, Math.max(depth * s, Math.max(width * c + depth * s, width * c - height * s))) + shiftX;
        xmin = (float) Math.min(-height * s, Math.min(depth * s, Math.min(width * c + depth * s, width * c - height * s))) + shiftX;
        ymax = (float) Math.max(height * c, Math.max(-depth * c, Math.max(width * s - depth * c, width * s + height * c)));
        ymin = (float) Math.min(height * c, Math.min(-depth * c, Math.min(width * s - depth * c, width * s + height * c)));
        width = xmax - xmin;
        height = ymax + shiftY;
        depth = -ymin - shiftY;
    }

    public RotateBox(Box b, double angle, Point2D.Float origin) {
        this(b, angle, origin.x, origin.y);
    }

    public RotateBox(Box b, double angle, int option) {
        this(b, angle, calculateShift(b, option));
    }

    public static int getOrigin(String option) {
        if (option == null || option.length() == 0) {
            return BBL;
        }

        if (option.length() == 1) {
            option += "c";
        }
        if (option.equals("bl") || option.equals("lb")) {
            return BL;
        } else if (option.equals("bc") || option.equals("cb")) {
            return BC;
        } else if (option.equals("br") || option.equals("rb")) {
            return BR;
        } else if (option.equals("cl") || option.equals("lc")) {
            return CL;
        } else if (option.equals("cc")) {
            return CC;
        } else if (option.equals("cr") || option.equals("cr")) {
            return CR;
        } else if (option.equals("tl") || option.equals("lt")) {
            return TL;
        } else if (option.equals("tc") || option.equals("ct")) {
            return TC;
        } else if (option.equals("tr") || option.equals("rt")) {
            return TR;
        } else if (option.equals("Bl") || option.equals("lB")) {
            return BBL;
        } else if (option.equals("Bc") || option.equals("cB")) {
            return BBC;
        } else if (option.equals("Br") || option.equals("rB")) {
            return BBR;
        } else

            return BBL;
    }

    private static Point2D.Float calculateShift(Box b, int option) {
        Point2D.Float p = new Point2D.Float(0, -b.depth);
        switch (option) {
        case BL :
            p.x = 0;
            p.y = -b.depth;
            break;
        case BR :
            p.x = b.width;
            p.y = -b.depth;
            break;
        case BC :
            p.x = b.width / 2;
            p.y = - b.depth;
            break;
        case TL :
            p.x = 0;
            p.y = b.height;
            break;
        case TR :
            p.x = b.width;
            p.y = b.height;
            break;
        case TC :
            p.x = b.width / 2;
            p.y = b.height;
            break;
        case BBL :
            p.x = 0;
            p.y = 0;
            break;
        case BBR :
            p.x = b.width;
            p.y = 0;
            break;
        case BBC :
            p.x = b.width / 2;
            p.y = 0;
            break;
        case CL :
            p.x = 0;
            p.y = (b.height - b.depth) / 2;
            break;
        case CR :
            p.x = b.width;
            p.y = (b.height - b.depth) / 2;
            break;
        case CC :
            p.x = b.width / 2;
            p.y = (b.height - b.depth) / 2;
            break;
        default :
        }

        return p;
    }

    public void draw(Graphics2D g2, float x, float y) {
        drawDebug(g2, x, y);
        box.drawDebug(g2, x, y, true);
        y -= shiftY;
        x += shiftX - xmin;
        g2.rotate(-angle, x, y);
        box.draw(g2, x, y);
        box.drawDebug(g2, x, y, true);
        g2.rotate(angle, x, y);
    }

    public int getLastFontId() {
        return box.getLastFontId();
    }
}
