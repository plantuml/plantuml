/* MultlineAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2010 DENIZET Calixte
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
 * An atom representing a vertical row of other atoms.
 */
public class MultlineAtom extends Atom {

    public static SpaceAtom vsep_in = new SpaceAtom(TeXConstants.UNIT_EX, 0.0f, 1.0f, 0.0f);
    public static final int MULTLINE = 0;
    public static final int GATHER = 1;
    public static final int GATHERED = 2;

    private ArrayOfAtoms column;
    private int type;
    private boolean isPartial;

    public MultlineAtom(boolean isPartial, ArrayOfAtoms column, int type) {
        this.isPartial = isPartial;
        this.column = column;
        this.type = type;
    }

    public MultlineAtom(ArrayOfAtoms column, int type) {
        this(false, column, type);
    }

    public Box createBox(TeXEnvironment env) {
        float tw = env.getTextwidth();
        if (tw == Float.POSITIVE_INFINITY || type == GATHERED) {
            return new MatrixAtom(isPartial, column, "").createBox(env);
        }

        VerticalBox vb = new VerticalBox();
        Atom at = column.array.get(0).get(0);
        int alignment = type == GATHER ? TeXConstants.ALIGN_CENTER : TeXConstants.ALIGN_LEFT;
        if (at.alignment != -1) {
            alignment = at.alignment;
        }
        vb.add(new HorizontalBox(at.createBox(env), tw, alignment));
        Box Vsep = vsep_in.createBox(env);
        for (int i = 1; i < column.row - 1; i++) {
            at = column.array.get(i).get(0);
            alignment = TeXConstants.ALIGN_CENTER;
            if (at.alignment != -1) {
                alignment = at.alignment;
            }
            vb.add(Vsep);
            vb.add(new HorizontalBox(at.createBox(env), tw, alignment));
        }

        if (column.row > 1) {
            at = column.array.get(column.row - 1).get(0);
            alignment = type == GATHER ? TeXConstants.ALIGN_CENTER : TeXConstants.ALIGN_RIGHT;
            if (at.alignment != -1) {
                alignment = at.alignment;
            }
            vb.add(Vsep);
            vb.add(new HorizontalBox(at.createBox(env), tw, alignment));
        }

        float height = vb.getHeight() + vb.getDepth();
        vb.setHeight(height / 2);
        vb.setDepth(height / 2);

        return vb;
    }
}
