/* ArrayOfAtoms.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
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

import java.util.LinkedList;

public class ArrayOfAtoms extends TeXFormula {

    public LinkedList<LinkedList<Atom>> array;
    public int col, row;

    public ArrayOfAtoms() {
        super();
        array = new LinkedList<LinkedList<Atom>>();
        array.add(new LinkedList<Atom>());
        row = 0;
    }

    public void addCol() {
        array.get(row).add(root);
        root = null;
    }

    public void addCol(int n) {
        array.get(row).add(root);
        for (int i = 1; i < n - 1; i++) {
            array.get(row).add(null);
        }
        root = null;
    }

    public void addRow() {
        addCol();
        array.add(new LinkedList<Atom>());
        row++;
    }

    public int getRows() {
        return row;
    }

    public int getCols() {
        return col;
    }

    public VRowAtom getAsVRow() {
        VRowAtom vr = new VRowAtom();
        vr.setAddInterline(true);
        for (LinkedList<Atom> r : array) {
            for (Atom a : r) {
                vr.append(a);
            }
        }

        return vr;
    }

    public void checkDimensions() {
        if (array.getLast().size() != 0)
            addRow();
        else if (root != null)
            addRow();

        row = array.size() - 1;
        col = array.get(0).size();

        for (int i = 1; i < row; i++) {
            if (array.get(i).size() > col) {
                col = array.get(i).size();
            }
        }

        /* Thanks to Juan Enrique Escobar Robles for this patch
           which let the user have empty columns */
        for (int i = 0; i < row; i++) {
            int  j = array.get(i).size();
            if (j != col && array.get(i).get(0) != null && array.get(i).get(0).type != TeXConstants.TYPE_INTERTEXT) {
                LinkedList<Atom> r = array.get(i);
                for(; j < col; j++) {
                    r.add(null);
                }
            }
        }
    }
}
