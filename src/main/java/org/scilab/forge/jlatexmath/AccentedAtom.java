/* AccentedAtom.java
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
 * An atom representing another atom with an accent symbol above it.
 */
public class AccentedAtom extends Atom {

    // accent symbol
    private final SymbolAtom accent;
    private boolean acc = false;
    private boolean changeSize = true;

    // base atom
    protected Atom base = null;
    protected Atom underbase = null;

    public AccentedAtom(Atom base, Atom accent) throws InvalidSymbolTypeException {
        this.base = base;
        if (base instanceof AccentedAtom)
            underbase = ((AccentedAtom)base).underbase;
        else
            underbase = base;

        if (!(accent instanceof SymbolAtom))
            throw new InvalidSymbolTypeException("Invalid accent");

        this.accent = (SymbolAtom)accent;
        this.acc = true;
    }

    public AccentedAtom(Atom base, Atom accent, boolean changeSize) throws InvalidSymbolTypeException {
        this(base, accent);
        this.changeSize = changeSize;
    }

    /**
     * Creates an AccentedAtom from a base atom and an accent symbol defined by its name
     *
     * @param base base atom
     * @param accentName name of the accent symbol to be put over the base atom
     * @throws InvalidSymbolTypeException if the symbol is not defined as an accent ('acc')
     * @throws SymbolNotFoundException if there's no symbol defined with the given name
     */
    public AccentedAtom(Atom base, String accentName)
    throws InvalidSymbolTypeException, SymbolNotFoundException {
        accent = SymbolAtom.get(accentName);
        if (accent.type == TeXConstants.TYPE_ACCENT) {
            this.base = base;
            if (base instanceof AccentedAtom)
                underbase = ((AccentedAtom)base).underbase;
            else
                underbase = base;
        } else
            throw new InvalidSymbolTypeException("The symbol with the name '"
                                                 + accentName + "' is not defined as an accent ("
                                                 + TeXSymbolParser.TYPE_ATTR + "='acc') in '"
                                                 + TeXSymbolParser.RESOURCE_NAME + "'!");
    }

    /**
     * Creates an AccentedAtom from a base atom and an accent symbol defined as a TeXFormula.
     * This is used for parsing MathML.
     *
     * @param base base atom
     * @param acc TeXFormula representing an accent (SymbolAtom)
     * @throws InvalidTeXFormulaException if the given TeXFormula does not represent a
     * 			single SymbolAtom (type "TeXConstants.TYPE_ACCENT")
     * @throws InvalidSymbolTypeException if the symbol is not defined as an accent ('acc')
     */
    public AccentedAtom(Atom base, TeXFormula acc)
    throws InvalidTeXFormulaException, InvalidSymbolTypeException {
        if (acc == null)
            throw new InvalidTeXFormulaException(
                "The accent TeXFormula can't be null!");
        else {
            Atom root = acc.root;
            if (root instanceof SymbolAtom) {
                accent = (SymbolAtom) root;
                if (accent.type == TeXConstants.TYPE_ACCENT)
                    this.base = base;
                else
                    throw new InvalidSymbolTypeException(
                        "The accent TeXFormula represents a single symbol with the name '"
                        + accent.getName()
                        + "', but this symbol is not defined as an accent ("
                        + TeXSymbolParser.TYPE_ATTR + "='acc') in '"
                        + TeXSymbolParser.RESOURCE_NAME + "'!");
            } else
                throw new InvalidTeXFormulaException(
                    "The accent TeXFormula does not represent a single symbol!");
        }
    }

    public Box createBox(TeXEnvironment env) {
        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();

        // set base in cramped style
        Box b = (base == null ? new StrutBox(0, 0, 0, 0) : base.createBox(env.crampStyle()));

        float u = b.getWidth();
        float s = 0;
        if (underbase instanceof CharSymbol)
            s = tf.getSkew(((CharSymbol) underbase).getCharFont(tf), style);

        // retrieve best Char from the accent symbol
        Char ch = tf.getChar(accent.getName(), style);
        while (tf.hasNextLarger(ch)) {
            Char larger = tf.getNextLarger(ch, style);
            if (larger.getWidth() <= u)
                ch = larger;
            else
                break;
        }

        // calculate delta
        float ec = -SpaceAtom.getFactor(TeXConstants.UNIT_MU, env);
        float delta = acc ? ec : Math.min(b.getHeight(), tf.getXHeight(style, ch.getFontCode()));

        // create vertical box
        VerticalBox vBox = new VerticalBox();

        // accent
        Box y;
        float italic = ch.getItalic();
        Box cb = new CharBox(ch);
        if (acc)
            cb = accent.createBox(changeSize ? env.subStyle() : env);

        if (Math.abs(italic) > TeXFormula.PREC) {
            y = new HorizontalBox(new StrutBox(-italic, 0, 0, 0));
            y.add(cb);
        } else
            y = cb;

        // if diff > 0, center accent, otherwise center base
        float diff = (u - y.getWidth()) / 2;
        y.setShift(s + (diff > 0 ? diff : 0));
        if (diff < 0)
            b = new HorizontalBox(b, y.getWidth(), TeXConstants.ALIGN_CENTER);
        vBox.add(y);

        // kern
        vBox.add(new StrutBox(0, changeSize ? -delta : -b.getHeight(), 0, 0));
        // base
        vBox.add(b);

        // set height and depth vertical box
        float total = vBox.getHeight() + vBox.getDepth(), d = b.getDepth();
        vBox.setDepth(d);
        vBox.setHeight(total - d);

        if (diff < 0) {
            HorizontalBox hb = new HorizontalBox(new StrutBox(diff, 0, 0, 0));
            hb.add(vBox);
            hb.setWidth(u);
            return hb;
        }

        return vBox;
    }
}
