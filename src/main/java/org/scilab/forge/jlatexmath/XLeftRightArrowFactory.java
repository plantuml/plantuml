/* XLeftRightArrowFactory.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://jlatexmath.sourceforge.net
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

/**
 * Responsible for creating a box containing a delimiter symbol that exists
 * in different sizes.
 */
public class XLeftRightArrowFactory {

    private static final Atom MINUS = SymbolAtom.get("minus");
    private static final Atom LEFT = SymbolAtom.get("leftarrow");
    private static final Atom RIGHT = SymbolAtom.get("rightarrow");

    public static Box create(boolean left, TeXEnvironment env, float width) {
        Box arr = left ? LEFT.createBox(env) : RIGHT.createBox(env);
        float h = arr.getHeight();
        float d = arr.getDepth();

        float swidth = arr.getWidth();
        if (width <= swidth) {
            arr.setDepth(d / 2);
            return arr;
        }

        Box minus = new SmashedAtom(MINUS, "").createBox(env);
        Box kern = new SpaceAtom(TeXConstants.UNIT_MU, -4f, 0, 0).createBox(env);
        float mwidth = minus.getWidth() + kern.getWidth();
        swidth += kern.getWidth();
        HorizontalBox hb = new HorizontalBox();
        float w;
        for (w = 0; w < width - swidth - mwidth; w += mwidth) {
            hb.add(minus);
            hb.add(kern);
        }

        float sf = (width - swidth - w) / minus.getWidth();

        hb.add(new SpaceAtom(TeXConstants.UNIT_MU, -2f * sf, 0, 0).createBox(env));
        hb.add(new ScaleAtom(MINUS, sf, 1).createBox(env));

        if (left) {
            hb.add(0, new SpaceAtom(TeXConstants.UNIT_MU, -3.5f, 0, 0).createBox(env));
            hb.add(0, arr);
        } else {
            hb.add(new SpaceAtom(TeXConstants.UNIT_MU, -2f * sf - 2f, 0, 0).createBox(env));
            hb.add(arr);
        }

        hb.setDepth(d / 2);
        hb.setHeight(h);

        return hb;
    }

    public static Box create(TeXEnvironment env, float width) {
        Box left = LEFT.createBox(env);
        Box right = RIGHT.createBox(env);
        float swidth = left.getWidth() + right.getWidth();

        if (width < swidth) {
            HorizontalBox hb = new HorizontalBox(left);
            hb.add(new StrutBox(-Math.min(swidth - width, left.getWidth()), 0, 0, 0));
            hb.add(right);
            return hb;
        }

        Box minus = new SmashedAtom(MINUS, "").createBox(env);
        Box kern = new SpaceAtom(TeXConstants.UNIT_MU, -3.4f, 0, 0).createBox(env);
        float mwidth = minus.getWidth() + kern.getWidth();
        swidth += 2 * kern.getWidth();

        HorizontalBox hb = new HorizontalBox();
        float w;
        for (w = 0; w < width - swidth - mwidth; w += mwidth) {
            hb.add(minus);
            hb.add(kern);
        }

        hb.add(new ScaleBox(minus, (width - swidth - w) / minus.getWidth(), 1));

        hb.add(0, kern);
        hb.add(0, left);
        hb.add(kern);
        hb.add(right);

        return hb;
    }
}
