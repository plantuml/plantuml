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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A box representing a box containing a graphics.
 */
public class GraphicsBox extends Box {

    public final static int BILINEAR = 0;
    public final static int NEAREST_NEIGHBOR = 1;
    public final static int BICUBIC = 2;

    private BufferedImage image;
    private float scl;
    private Object interp;

    public GraphicsBox(BufferedImage image, float width, float height, float size, int interpolation) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.scl = 1 / size;
        depth = 0;
        shift = 0;
        switch (interpolation) {
        case BILINEAR :
            interp = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            break;
        case NEAREST_NEIGHBOR :
            interp = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            break;
        case BICUBIC :
            interp = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            break;
        default :
            interp = null;
        }
    }

    public void draw(Graphics2D g2, float x, float y) {
        AffineTransform oldAt = g2.getTransform();
        Object oldKey = null;
        if (interp != null) {
            oldKey = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interp);
        }
        g2.translate(x, y - height);
        g2.scale(scl, scl);
        g2.drawImage(image, 0, 0, null);
        g2.setTransform(oldAt);
        if (oldKey != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldKey);
        }
    }

    public int getLastFontId() {
        return 0;
    }
}
