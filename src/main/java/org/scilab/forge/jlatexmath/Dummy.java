/* Dummy.java
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
 * Used by RowAtom. The "textSymbol"-property and the type of an atom can change
 * (according to the TeX-algorithms used). Or this atom can be replaced by a ligature,
 * (if it was a CharAtom). But atoms cannot be changed, otherwise
 * different boxes could be made from the same TeXFormula, and that is not desired!
 * This "dummy atom" makes sure that changes to an atom (during the createBox-method of
 * a RowAtom) will be reset.
 */
public class Dummy {

    private Atom el;

    private boolean textSymbol = false;

    private int type = -1;

    /**
     * Creates a new Dummy for the given atom.
     *
     * @param a an atom
     */
    public Dummy(Atom a) {
        el = a;
    }

    /**
     * Changes the type of the atom
     *
     * @param t the new type
     */
    public void setType(int t) {
        type = t;
    }

    /**
     * Changes the type of the atom
     *
     * @param t the new type
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @return the changed type, or the old left type if it hasn't been changed
     */
    public int getLeftType() {
        return (type >= 0 ? type : el.getLeftType());
    }

    /**
     *
     * @return the changed type, or the old right type if it hasn't been changed
     */
    public int getRightType() {
        return (type >= 0 ? type : el.getRightType());
    }

    public boolean isCharSymbol() {
        return el instanceof CharSymbol;
    }

    public boolean isCharInMathMode() {
        return el instanceof CharAtom && ((CharAtom) el).isMathMode();
    }

    /**
     * This method will only be called if isCharSymbol returns true.
     */
    public CharFont getCharFont(TeXFont tf) {
        return ((CharSymbol) el).getCharFont(tf);
    }

    /**
     * Changes this atom into the given "ligature atom".
     *
     * @param a the ligature atom
     */
    public void changeAtom(FixedCharAtom a) {
        textSymbol = false;
        type = -1;
        el = a;
    }

    public Box createBox(TeXEnvironment rs) {
        if (textSymbol)
            ((CharSymbol) el).markAsTextSymbol();
        Box b = el.createBox(rs);
        if (textSymbol)
            ((CharSymbol) el).removeMark(); // atom remains unchanged!
        return b;
    }

    public void markAsTextSymbol() {
        textSymbol = true;
    }

    public boolean isKern() {
        return el instanceof SpaceAtom;
    }

    // only for Row-elements
    public void setPreviousAtom(Dummy prev) {
        if (el instanceof Row)
            ((Row) el).setPreviousAtom(prev);
    }
}
