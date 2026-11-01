/* GraphicsAtom.java
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
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * An atom representing an atom containing a graphic.
 */
public class GraphicsAtom extends Atom {

    private Image image = null;
    private BufferedImage bimage;
    private Label c;
    private int w, h;

    private Atom base;
    private boolean first = true;
    private int interp = -1;

    public GraphicsAtom(String path, String option) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                URL url = new URL(path);
                image = Toolkit.getDefaultToolkit().getImage(url);
            } catch (MalformedURLException e) {
                image = null;
            }
        } else {
            image = Toolkit.getDefaultToolkit().getImage(path);
        }

        if (image != null) {
            c = new Label();
            MediaTracker tracker = new MediaTracker(c);
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
                image = null;
            }
        }
        draw();
        buildAtom(option);
    }

    protected void buildAtom(String option) {
        base = this;
        Map<String, String> options = ParseOption.parseMap(option);
        if (options.containsKey("width") || options.containsKey("height")) {
            base = new ResizeAtom(base, options.get("width"), options.get("height"), options.containsKey("keepaspectratio"));
        }
        if (options.containsKey("scale")) {
            double scl = Double.parseDouble(options.get("scale"));
            base = new ScaleAtom(base, scl, scl);
        }
        if (options.containsKey("angle") || options.containsKey("origin")) {
            base = new RotateAtom(base, options.get("angle"), options.get("origin"));
        }
        if (options.containsKey("interpolation")) {
            String meth = options.get("interpolation");
            if (meth.equalsIgnoreCase("bilinear")) {
                interp = GraphicsBox.BILINEAR;
            } else if (meth.equalsIgnoreCase("bicubic")) {
                interp = GraphicsBox.BICUBIC;
            } else if (meth.equalsIgnoreCase("nearest_neighbor")) {
                interp = GraphicsBox.NEAREST_NEIGHBOR;
            }
        }
    }

    public void draw() {
        if (image != null) {
            w = image.getWidth(c);
            h = image.getHeight(c);
            bimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bimage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }
    }

    public Box createBox(TeXEnvironment env) {
        if (image != null) {
            if (first) {
                first = false;
                return base.createBox(env);
            } else {
                env.isColored = true;
                float width = w * SpaceAtom.getFactor(TeXConstants.UNIT_PIXEL, env);
                float height = h * SpaceAtom.getFactor(TeXConstants.UNIT_PIXEL, env);
                return new GraphicsBox(bimage, width, height, env.getSize(), interp);
            }
        }

        return new TeXFormula("\\text{ No such image file ! }").root.createBox(env);
    }
}
