/* RowAtom.java
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

/* Modified by Calixte Denizet to handle the case where several ligatures occure*/

package org.scilab.forge.jlatexmath;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.ListIterator;

import org.scilab.forge.jlatexmath.dynamic.DynamicAtom;

/**
 * An atom representing a horizontal row of other atoms, to be seperated by glue.
 * It's also responsible for inserting kerns and ligatures.
 */
public class RowAtom extends Atom implements Row {

    // atoms to be displayed horizontally next to eachother
    protected LinkedList<Atom> elements = new LinkedList<Atom>();

    public boolean lookAtLastAtom = false;

    // previous atom (for nested Row atoms)
    private Dummy previousAtom = null;

    // set of atom types that make a previous bin atom change to ord
    private static BitSet binSet;

    // set of atom types that can possibly need a kern or, together with the
    // previous atom, be replaced by a ligature
    private static BitSet ligKernSet;

    static {
        // fill binSet
        binSet = new BitSet (16);
        binSet.set(TeXConstants.TYPE_BINARY_OPERATOR);
        binSet.set(TeXConstants.TYPE_BIG_OPERATOR);
        binSet.set(TeXConstants.TYPE_RELATION);
        binSet.set(TeXConstants.TYPE_OPENING);
        binSet.set(TeXConstants.TYPE_PUNCTUATION);

        // fill ligKernSet
        ligKernSet = new BitSet (16);
        ligKernSet.set(TeXConstants.TYPE_ORDINARY);
        ligKernSet.set(TeXConstants.TYPE_BIG_OPERATOR);
        ligKernSet.set(TeXConstants.TYPE_BINARY_OPERATOR);
        ligKernSet.set(TeXConstants.TYPE_RELATION);
        ligKernSet.set(TeXConstants.TYPE_OPENING);
        ligKernSet.set(TeXConstants.TYPE_CLOSING);
        ligKernSet.set(TeXConstants.TYPE_PUNCTUATION);
    }

    protected RowAtom() {
        // empty
    }

    public RowAtom(Atom el) {
        if (el != null) {
            if (el instanceof RowAtom)
                // no need to make an mrow the only element of an mrow
                elements.addAll(((RowAtom) el).elements);
            else
                elements.add(el);
        }
    }

    public Atom getLastAtom() {
        if (elements.size() != 0) {
            return elements.removeLast();
        }

        return new SpaceAtom(TeXConstants.UNIT_POINT, 0.0f, 0.0f, 0.0f);
    }

    public final void add(Atom el) {
        if (el != null) {
            elements.add(el);
        }
    }

    /**
     *
     * @param cur
     *           current atom being processed
     * @param prev
     *           previous atom
     */
    private void changeToOrd(Dummy cur, Dummy prev, Atom next) {
        int type = cur.getLeftType();
        if (type == TeXConstants.TYPE_BINARY_OPERATOR && ((prev == null || binSet.get(prev.getRightType())) || next == null)) {
            cur.setType(TeXConstants.TYPE_ORDINARY);
        } else if (next != null && cur.getRightType() == TeXConstants.TYPE_BINARY_OPERATOR) {
            int nextType = next.getLeftType();
            if (nextType == TeXConstants.TYPE_RELATION || nextType == TeXConstants.TYPE_CLOSING || nextType == TeXConstants.TYPE_PUNCTUATION) {
                cur.setType(TeXConstants.TYPE_ORDINARY);
            }
        }
    }

    public Box createBox(TeXEnvironment env) {
        TeXFont tf = env.getTeXFont();
        HorizontalBox hBox = new HorizontalBox(env.getColor(), env.getBackground());
        int position = 0;
        env.reset();

        // convert atoms to boxes and add to the horizontal box
        for (ListIterator<Atom> it = elements.listIterator(); it.hasNext();) {
            Atom at = it.next();
            position++;

            boolean markAdded = false;
            while (at instanceof BreakMarkAtom) {
                if (!markAdded) {
                    markAdded = true;
                }
                if (it.hasNext()) {
                    at = it.next();
                    position++;
                } else {
                    break;
                }
            }

            if (at instanceof DynamicAtom && ((DynamicAtom) at).getInsertMode()) {
                Atom a = ((DynamicAtom) at).getAtom();
                if (a instanceof RowAtom) {
                    elements.remove(position - 1);
                    elements.addAll(position - 1, ((RowAtom) a).elements);
                    it = elements.listIterator(position - 1);
                    at = it.next();
                } else {
                    at = a;
                }
            }

            Dummy atom = new Dummy(at);

            // if necessary, change BIN type to ORD
            Atom nextAtom = null;
            if (it.hasNext()) {
                nextAtom = it.next();
                it.previous();
            }
            changeToOrd(atom, previousAtom, nextAtom);

            // check for ligatures or kerning
            float kern = 0;
            // Calixte : I put a while to handle the case where there are
            // several ligatures as in ffi or ffl
            while (it.hasNext() && atom.getRightType() == TeXConstants.TYPE_ORDINARY && atom.isCharSymbol()) {
                Atom next = it.next();
                position++;
                if (next instanceof CharSymbol && ligKernSet.get(next.getLeftType())) {
                    atom.markAsTextSymbol();
                    CharFont l = atom.getCharFont(tf), r = ((CharSymbol) next).getCharFont(tf);
                    CharFont lig = tf.getLigature(l, r);
                    if (lig == null) {
                        kern = tf.getKern(l, r, env.getStyle());
                        it.previous();
                        position--;
                        break; // iterator remains unchanged (no ligature!)
                    } else { // ligature
                        atom.changeAtom(new FixedCharAtom(lig)); // go on with the
                        // ligature
                    }
                } else {
                    it.previous();
                    position--;
                    break;
                }// iterator remains unchanged
            }

            // insert glue, unless it's the first element of the row
            // OR this element or the next is a Kern.
            if (it.previousIndex() != 0 && previousAtom != null && !previousAtom.isKern() && !atom.isKern()) {
                hBox.add(Glue.get(previousAtom.getRightType(), atom.getLeftType(), env));
            }

            // insert atom's box
            atom.setPreviousAtom(previousAtom);
            Box b = atom.createBox(env);
            if (atom.isCharInMathMode() && b instanceof CharBox) {
                // When we've a single char, we need to add italic correction
                // As an example: (TVY) looks crappy...
                CharBox cb = (CharBox) b;
                cb.addItalicCorrectionToWidth();
            }
            if (markAdded || (at instanceof CharAtom && Character.isDigit(((CharAtom) at).getCharacter()))) {
                hBox.addBreakPosition(hBox.children.size());
            }
            hBox.add(b);

            // set last used fontId (for next atom)
            env.setLastFontId(b.getLastFontId());

            // insert kern
            if (Math.abs(kern) > TeXFormula.PREC) {
                hBox.add(new StrutBox(kern, 0, 0, 0));
            }

            // kerns do not interfere with the normal glue-rules without kerns
            if (!atom.isKern()) {
                previousAtom = atom;
            }
        }
        // reset previousAtom
        previousAtom = null;

        return hBox;
    }

    public void setPreviousAtom(Dummy prev) {
        previousAtom = prev;
    }

    public int getLeftType() {
        if (elements.size() == 0) {
            return TeXConstants.TYPE_ORDINARY;
        } else {
            return (elements.get(0)).getLeftType();
        }
    }

    public int getRightType() {
        if (elements.size() == 0) {
            return TeXConstants.TYPE_ORDINARY;
        } else {
            return (elements.get(elements.size() - 1)).getRightType();
        }
    }
}
