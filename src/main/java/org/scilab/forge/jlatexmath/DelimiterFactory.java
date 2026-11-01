/* DelimiterFactory.java
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

package org.scilab.forge.jlatexmath; // NOPMD

/**
 * Responsible for creating a box containing a delimiter symbol that exists
 * in different sizes.
 */
public class DelimiterFactory {

    public static Box create(SymbolAtom symbol, TeXEnvironment env, int size) {
        if (size > 4)
            return symbol.createBox(env);

        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();
        Char c = tf.getChar(symbol.getName(), style);
        int i;

        for (i = 1; i <= size && tf.hasNextLarger(c); i++)
            c = tf.getNextLarger(c, style);

        if (i <= size && !tf.hasNextLarger(c)) {
            CharBox A = new CharBox(tf.getChar('A', "mathnormal", style));
            Box b = create(symbol.getName(), env, size*(A.getHeight() + A.getDepth()));
            return b;
        }

        return new CharBox(c);
    }

    /**
     *
     * @param symbol the name of the delimiter symbol
     * @param env the TeXEnvironment in which to create the delimiter box
     * @param minHeight the minimum required total height of the box (height + depth).
     * @return the box representing the delimiter variant that fits best according to
     * 			the required minimum size.
     */
    public static Box create(String symbol, TeXEnvironment env, float minHeight) {
        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();
        Char c = tf.getChar(symbol, style);

        // start with smallest character
        Metrics m = c.getMetrics();
        float total = m.getHeight() + m.getDepth();

        // try larger versions of the same character until minHeight has been
        // reached
        while (total < minHeight && tf.hasNextLarger(c)) {
            c = tf.getNextLarger(c, style);
            m = c.getMetrics();
            total = m.getHeight() + m.getDepth();
        }
        if (total >= minHeight) { // tall enough character found
            return new CharBox(c);
        } else if (tf.isExtensionChar(c)) {
            // construct tall enough vertical box
            VerticalBox vBox = new VerticalBox();
            Extension ext = tf.getExtension(c, style); // extension info

            if (ext.hasTop()) { // insert top part
                c = ext.getTop();
                vBox.add(new CharBox(c));
            }

            boolean middle = ext.hasMiddle();
            if (middle) { // insert middle part
                c = ext.getMiddle();
                vBox.add(new CharBox(c));
            }

            if (ext.hasBottom()) { // insert bottom part
                c = ext.getBottom();
                vBox.add(new CharBox(c));
            }

            // insert repeatable part until tall enough
            c = ext.getRepeat();
            CharBox rep = new CharBox(c);
            while (vBox.getHeight() + vBox.getDepth() <= minHeight) {
                if (ext.hasTop() && ext.hasBottom()) {
                    vBox.add(1, rep);
                    if (middle)
                        vBox.add(vBox.getSize() - 1, rep);
                } else if (ext.hasBottom())
                    vBox.add(0, rep);
                else
                    vBox.add(rep);
            }

            return vBox;
        } else
            // no extensions, so return tallest possible character
            return new CharBox(c);
    }
}
