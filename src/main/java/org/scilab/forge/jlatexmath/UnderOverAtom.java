/* UnderOverAtom.java
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

/**
 * An atom representing another atom with an atom above it (if not null) seperated
 * by a kern and in a smaller size depending on "overScriptSize" and/or an atom under
 * it (if not null) seperated by a kern and in a smaller size depending on "underScriptSize"
 */
public class UnderOverAtom extends Atom {

    // base, underscript and overscript
    private final Atom base;
    private final Atom under;
    private final Atom over;

    // kern between base and under- and overscript
    private final float underSpace;
    private final float overSpace;

    // units for the kerns
    private final int underUnit; // NOPMD
    // TODO: seems never to be used?
    private final int overUnit;

    // whether the under- and overscript should be drawn in a smaller size
    private final boolean underScriptSize;
    private final boolean overScriptSize;

    public UnderOverAtom(Atom base, Atom underOver, int underOverUnit,
                         float underOverSpace, boolean underOverScriptSize, boolean over) {
        // check if unit is valid
        SpaceAtom.checkUnit(underOverUnit);
        // units valid
        this.base = base;

        if (over) {
            this.under = null;
            this.underSpace = 0.0f;
            this.underUnit = 0;
            this.underScriptSize = false;
            this.over = underOver;
            this.overUnit = underOverUnit;
            this.overSpace = underOverSpace;
            this.overScriptSize = underOverScriptSize;
        } else {
            this.under = underOver;
            this.underUnit = underOverUnit;
            this.underSpace = underOverSpace;
            this.underScriptSize = underOverScriptSize;
            this.overSpace = 0.0f;
            this.over = null;
            this.overUnit = 0;
            this.overScriptSize = false;
        }
    }

    public UnderOverAtom(Atom base, Atom under, int underUnit, float underSpace,
                         boolean underScriptSize, Atom over, int overUnit, float overSpace,
                         boolean overScriptSize) throws InvalidUnitException {
        // check if units are valid
        SpaceAtom.checkUnit(underUnit);
        SpaceAtom.checkUnit(overUnit);

        // units valid
        this.base = base;
        this.under = under;
        this.underUnit = underUnit;
        this.underSpace = underSpace;
        this.underScriptSize = underScriptSize;
        this.over = over;
        this.overUnit = overUnit;
        this.overSpace = overSpace;
        this.overScriptSize = overScriptSize;
    }

    public Box createBox(TeXEnvironment env) {
        // create boxes in right style and calculate maximum width
        Box b = (base == null ? new StrutBox(0, 0, 0, 0) : base.createBox(env));
        Box o = null, u = null;
        float max = b.getWidth();
        if (over != null) {
            o = over.createBox(overScriptSize ? env.subStyle() : env);
            max = Math.max(max, o.getWidth());
        }
        if (under != null) {
            u = under.createBox(underScriptSize ? env.subStyle() : env);
            max = Math.max(max, u.getWidth());
        }

        // create vertical box
        VerticalBox vBox = new VerticalBox();

        // last font used by the base (for Mspace atoms following)
        env.setLastFontId(b.getLastFontId());

        // overscript + space
        if (over != null) {
            vBox.add(changeWidth(o, max));
            // unit will be valid (checked in constructor)
            vBox.add(new SpaceAtom(overUnit, 0, overSpace, 0).createBox(env));
        }

        // base
        Box c = changeWidth(b, max);
        vBox.add(c);

        // calculate future height of the vertical box (to make sure that the base
        // stays on the baseline!)
        float h = vBox.getHeight() + vBox.getDepth() - c.getDepth();

        // underscript + space
        if (under != null) {
            // unit will be valid (checked in constructor)
            vBox.add(new SpaceAtom(overUnit, 0, underSpace, 0).createBox(env));
            vBox.add(changeWidth(u, max));
        }

        // set height and depth
        vBox.setDepth(vBox.getHeight() + vBox.getDepth() - h);
        vBox.setHeight(h);
        return vBox;
    }

    private static Box changeWidth(Box b, float maxWidth) {
        if (b != null && Math.abs(maxWidth - b.getWidth()) > TeXFormula.PREC)
            return new HorizontalBox(b, maxWidth, TeXConstants.ALIGN_CENTER);
        else
            return b;
    }

    public int getLeftType() {
        return base.getLeftType();
    }

    public int getRightType() {
        return base.getRightType();
    }
}
