/* FencedAtom.java
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

import java.util.List;

/**
 * An atom representing a base atom surrounded with delimiters that change their size
 * according to the height of the base.
 */
public class FencedAtom extends Atom {

    // parameters used in the TeX algorithm
    private static final int DELIMITER_FACTOR = 901;

    private static final float DELIMITER_SHORTFALL = 5f;

    // base atom
    private final Atom base;

    // delimiters
    private SymbolAtom left = null;
    private SymbolAtom right = null;
    private final List<MiddleAtom> middle;

    /**
     * Creates a new FencedAtom from the given base and delimiters
     *
     * @param base the base to be surrounded with delimiters
     * @param l the left delimiter
     * @param r the right delimiter
     */
    public FencedAtom(Atom base, SymbolAtom l, SymbolAtom r) {
        this(base, l, null, r);
    }

    public FencedAtom(Atom base, SymbolAtom l, List<MiddleAtom> m, SymbolAtom r) {
        if (base == null)
            this.base = new RowAtom(); // empty base
        else
            this.base = base;
        if (l == null || !l.getName().equals("normaldot")) {
            left = l;
        }
        if (r == null || !r.getName().equals("normaldot")) {
            right = r;
        }
        middle =  m;
    }

    public int getLeftType() {
        return TeXConstants.TYPE_INNER;
    }

    public int getRightType() {
        return TeXConstants.TYPE_INNER;
    }

    /**
     * Centers the given box with resprect to the given axis, by setting an appropriate
     * shift value.
     *
     * @param box
     *           box to be vertically centered with respect to the axis
     */
    private static void center(Box box, float axis) {
        float h = box.getHeight(), total = h + box.getDepth();
        box.setShift(-(total / 2 - h) - axis);
    }

    public Box createBox(TeXEnvironment env) {
        TeXFont tf = env.getTeXFont();
        Box content = base.createBox(env);
        float shortfall = DELIMITER_SHORTFALL * SpaceAtom.getFactor(TeXConstants.UNIT_POINT, env);
        float axis = tf.getAxisHeight(env.getStyle());
        float delta = Math.max(content.getHeight() - axis, content.getDepth() + axis);
        float minHeight = Math.max((delta / 500) * DELIMITER_FACTOR, 2 * delta - shortfall);

        // construct box
        HorizontalBox hBox = new HorizontalBox();

        if (middle != null) {
            for (int i = 0; i < middle.size(); i++) {
                MiddleAtom at = middle.get(i);
                if (at.base instanceof SymbolAtom) {
                    Box b = DelimiterFactory.create(((SymbolAtom) at.base).getName(), env, minHeight);
                    center(b, axis);
                    at.box = b;
                }
            }
            if (middle.size() != 0) {
                content = base.createBox(env);
            }
        }

        // left delimiter
        if (left != null) {
            Box b = DelimiterFactory.create(left.getName(), env, minHeight);
            center(b, axis);
            hBox.add(b);
        }

        // glue between left delimiter and content (if not whitespace)
        if (!(base instanceof SpaceAtom)) {
            hBox.add(Glue.get(TeXConstants.TYPE_OPENING, base.getLeftType(), env));
        }

        // add content
        hBox.add(content);

        // glue between right delimiter and content (if not whitespace)
        if (!(base instanceof SpaceAtom)) {
            hBox.add(Glue.get(base.getRightType(), TeXConstants.TYPE_CLOSING, env));
        }

        // right delimiter
        if (right != null) {
            Box b = DelimiterFactory.create(right.getName(), env, minHeight);
            center(b, axis);
            hBox.add(b);
        }

        return hBox;
    }
}
