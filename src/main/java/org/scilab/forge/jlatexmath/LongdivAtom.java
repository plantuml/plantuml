/* GraphicsAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2017 DENIZET Calixte
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

import java.util.ArrayList;

/**
 * An atom representing a long division.
 */
public class LongdivAtom extends VRowAtom {

    public LongdivAtom(long divisor, long dividend) {
        setHalign(TeXConstants.ALIGN_RIGHT);
        setVtop(true);
        String[] res = makeResults(divisor, dividend);
        Atom rule = new RuleAtom(TeXConstants.UNIT_EX, 0f,
                                 TeXConstants.UNIT_EX, 2.6f,
                                 TeXConstants.UNIT_EX, 0.5f);
        for (int i = 0; i < res.length; ++i) {
            Atom num = new TeXFormula(res[i]).root;
            if (i % 2 == 0) {
                RowAtom ra = new RowAtom(num);
                ra.add(rule);
                if (i == 0) {
                    append(ra);
                } else {
                    append(new UnderlinedAtom(ra));
                }
            } else if (i == 1) {
                String div = Long.toString(divisor);
                SymbolAtom rparen = SymbolAtom.get(TeXFormula.symbolMappings[')']);
                Atom big = new BigDelimiterAtom(rparen, 1);
                Atom ph = new PhantomAtom(big, false, true, true);
                RowAtom ra = new RowAtom(ph);
                Atom raised = new RaiseAtom(big,
                                            TeXConstants.UNIT_X8, 3.5f,
                                            TeXConstants.UNIT_X8, 0f,
                                            TeXConstants.UNIT_X8, 0f);
                ra.add(new SmashedAtom(raised));
                ra.add(num);
                Atom a = new OverlinedAtom(ra);
                RowAtom ra1 = new RowAtom(new TeXFormula(div).root);
                ra1.add(new SpaceAtom(TeXConstants.THINMUSKIP));
                ra1.add(a);
                append(ra1);
            } else {
                RowAtom ra = new RowAtom(num);
                ra.add(rule);
                append(ra);
            }
        }
    }

    private String[] makeResults(long divisor, long dividend) {
        ArrayList<String> vec = new ArrayList<>();
        long q = dividend / divisor;
        vec.add(Long.toString(q));
        vec.add(Long.toString(dividend));

        while (q != 0) {
            final double p = (double) Math.floor(Math.log10((double) q));
            final double p10 = Math.pow(10., p);
            final long d = (long) (Math.floor(((double) q) / p10) * p10);
            final long dd = d * divisor;
            vec.add(Long.toString(dd));
            dividend -= dd;
            vec.add(Long.toString(dividend));
            q -= d;
        }

        String[] res = new String[vec.size()];
        return vec.toArray(res);
    }
}
