/* GraphicsBox.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * A box representing a box containing a graphics.
 */
public class GeoGebraLogoBox extends Box {

    private static final Color gray = new Color(102, 102, 102);
    private static final Color blue = new Color(153, 153, 255);

    private static final BasicStroke st = new BasicStroke(3.79999995f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4f);

    public GeoGebraLogoBox(float w, float h) {
        this.depth = 0;
        this.height = h;
        this.width = w;
        this.shift = 0;
    }

    public void draw(Graphics2D g2, float x, float y) {
        AffineTransform oldAt = g2.getTransform();
        Color oldC = g2.getColor();
        Stroke oldS = g2.getStroke();
        g2.translate(x + 0.25f * height / 2.15f, y - 1.75f / 2.15f * height);
        g2.setColor(gray);
        g2.setStroke(st);
        g2.scale(0.05f * height / 2.15f, 0.05f * height / 2.15f);
        g2.rotate(-26 * Math.PI / 180, 20.5, 17.5);
        g2.drawArc(0, 0, 43, 32, 0, 360);
        g2.rotate(26 * Math.PI / 180, 20.5, 17.5);
        g2.setStroke(oldS);
        drawCircle(g2, 16f, -5f);
        drawCircle(g2, -1f, 7f);
        drawCircle(g2, 5f, 28f);
        drawCircle(g2, 27f, 24f);
        drawCircle(g2, 36f, 3f);
        g2.setStroke(oldS);
        g2.setTransform(oldAt);
        g2.setColor(oldC);
    }

    private static void drawCircle(Graphics2D g2, float x, float y) {
        g2.setColor(blue);
        g2.translate(x, y);
        g2.fillArc(0, 0, 8, 8, 0, 360);
        g2.setColor(Color.BLACK);
        g2.drawArc(0, 0, 8, 8, 0, 360);
        g2.translate(-x, -y);
    }

    public int getLastFontId() {
        return 0;
    }
}
