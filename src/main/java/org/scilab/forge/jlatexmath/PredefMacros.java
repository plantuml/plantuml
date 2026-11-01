/* predefMacros.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
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

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.StringTokenizer;

import org.scilab.forge.jlatexmath.dynamic.DynamicAtom;

/**
 * This class contains the most of basic commands of LaTeX, they're activated in
 * byt the class PredefinedCommands.java.
 **/
public class PredefMacros {

    static {
        NewEnvironmentMacro.addNewEnvironment("array", "\\array@@env{#1}{", "}", 1);
        NewEnvironmentMacro.addNewEnvironment("tabular", "\\array@@env{#1}{", "}", 1);
        NewEnvironmentMacro.addNewEnvironment("matrix", "\\matrix@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("smallmatrix", "\\smallmatrix@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("pmatrix", "\\left(\\begin{matrix}", "\\end{matrix}\\right)", 0);
        NewEnvironmentMacro.addNewEnvironment("bmatrix", "\\left[\\begin{matrix}", "\\end{matrix}\\right]", 0);
        NewEnvironmentMacro.addNewEnvironment("Bmatrix", "\\left\\{\\begin{matrix}", "\\end{matrix}\\right\\}", 0);
        NewEnvironmentMacro.addNewEnvironment("vmatrix", "\\left|\\begin{matrix}", "\\end{matrix}\\right|", 0);
        NewEnvironmentMacro.addNewEnvironment("Vmatrix", "\\left\\|\\begin{matrix}", "\\end{matrix}\\right\\|", 0);
        NewEnvironmentMacro.addNewEnvironment("eqnarray", "\\begin{array}{rcl}", "\\end{array}", 0);
        NewEnvironmentMacro.addNewEnvironment("align", "\\align@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("flalign", "\\flalign@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("alignat", "\\alignat@@env{#1}{", "}", 1);
        NewEnvironmentMacro.addNewEnvironment("aligned", "\\aligned@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("alignedat", "\\alignedat@@env{#1}{", "}", 1);
        NewEnvironmentMacro.addNewEnvironment("multline", "\\multline@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("cases", "\\left\\{\\begin{array}{l@{\\!}l}", "\\end{array}\\right.", 0);
        NewEnvironmentMacro.addNewEnvironment("split", "\\begin{array}{rl}", "\\end{array}", 0);
        NewEnvironmentMacro.addNewEnvironment("gather", "\\gather@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("gathered", "\\gathered@@env{", "}", 0);
        NewEnvironmentMacro.addNewEnvironment("math", "\\(", "\\)", 0);
        NewEnvironmentMacro.addNewEnvironment("displaymath", "\\[", "\\]", 0);
        NewCommandMacro.addNewCommand("operatorname", "\\mathop{\\mathrm{#1}}\\nolimits ", 1);
        NewCommandMacro.addNewCommand("DeclareMathOperator", "\\newcommand{#1}{\\mathop{\\mathrm{#2}}\\nolimits}", 2);
        NewCommandMacro.addNewCommand("substack", "{\\scriptstyle\\begin{array}{c}#1\\end{array}}", 1);
        NewCommandMacro.addNewCommand("dfrac", "\\genfrac{}{}{}{}{#1}{#2}", 2);
        NewCommandMacro.addNewCommand("tfrac", "\\genfrac{}{}{}{1}{#1}{#2}", 2);
        NewCommandMacro.addNewCommand("dbinom", "\\genfrac{(}{)}{0pt}{}{#1}{#2}", 2);
        NewCommandMacro.addNewCommand("tbinom", "\\genfrac{(}{)}{0pt}{1}{#1}{#2}", 2);
        NewCommandMacro.addNewCommand("pmod", "\\qquad\\mathbin{(\\mathrm{mod}\\ #1)}", 1);
        NewCommandMacro.addNewCommand("mod", "\\qquad\\mathbin{\\mathrm{mod}\\ #1}", 1);
        NewCommandMacro.addNewCommand("pod", "\\qquad\\mathbin{(#1)}", 1);
        NewCommandMacro.addNewCommand("dddot", "\\mathop{#1}\\limits^{...}", 1);
        NewCommandMacro.addNewCommand("ddddot", "\\mathop{#1}\\limits^{....}", 1);
        NewCommandMacro.addNewCommand("spdddot", "^{\\mathrm{...}}", 0);
        NewCommandMacro.addNewCommand("spbreve", "^{\\makeatletter\\sp@breve\\makeatother}", 0);
        NewCommandMacro.addNewCommand("sphat", "^{\\makeatletter\\sp@hat\\makeatother}", 0);
        NewCommandMacro.addNewCommand("spddot", "^{\\displaystyle..}", 0);
        NewCommandMacro.addNewCommand("spcheck", "^{\\vee}", 0);
        NewCommandMacro.addNewCommand("sptilde", "^{\\sim}", 0);
        NewCommandMacro.addNewCommand("spdot", "^{\\displaystyle.}", 0);
        NewCommandMacro.addNewCommand("d", "\\underaccent{\\dot}{#1}", 1);
        NewCommandMacro.addNewCommand("b", "\\underaccent{\\bar}{#1}", 1);
        NewCommandMacro.addNewCommand("Bra", "\\left\\langle{#1}\\right\\vert", 1);
        NewCommandMacro.addNewCommand("Ket", "\\left\\vert{#1}\\right\\rangle", 1);
        NewCommandMacro.addNewCommand("textsuperscript", "{}^{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("textsubscript", "{}_{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("textit", "\\mathit{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("textbf", "\\mathbf{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("textsf", "\\mathsf{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("texttt", "\\mathtt{\\text{#1}}", 1);
        NewCommandMacro.addNewCommand("textrm", "\\text{#1}", 1);
        NewCommandMacro.addNewCommand("degree", "^\\circ", 0);
        NewCommandMacro.addNewCommand("with", "\\mathbin{\\&}", 0);
        NewCommandMacro.addNewCommand("parr", "\\mathbin{\\rotatebox[origin=c]{180}{\\&}}", 0);
        NewCommandMacro.addNewCommand("copyright", "\\textcircled{\\raisebox{0.2ex}{c}}", 0);
        NewCommandMacro.addNewCommand("L", "\\mathrm{\\polishlcross L}", 0);
        NewCommandMacro.addNewCommand("l", "\\mathrm{\\polishlcross l}", 0);
        NewCommandMacro.addNewCommand("Join", "\\mathop{\\rlap{\\ltimes}\\rtimes}", 0);
    }

    public static final Atom fcscore_macro(final TeXParser tp, final String[] args) throws ParseException {
        int n = Integer.parseInt(args[1]);
        if (n > 5) {
            final int q = n / 5;
            final int r = n % 5;
            RowAtom rat = new RowAtom();
            for (int i = 0; i < q; i++) {
                rat.add(new FcscoreAtom(5));
            }
            rat.add(new FcscoreAtom(r));

            return rat;
        } else {
            return new FcscoreAtom(n);
        }
    }

    public static final Atom longdiv_macro(final TeXParser tp, final String[] args) throws ParseException {
        try {
            long dividend = Long.valueOf(args[1]);
            long divisor = Long.valueOf(args[2]);
            return new LongdivAtom(divisor, dividend);
        } catch (NumberFormatException e) {
            throw new ParseException("Divisor and dividend must be integer numbers");
        }
    }

    public static final Atom st_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new StrikeThroughAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom Braket_macro(final TeXParser tp, final String[] args) throws ParseException {
        String str = args[1].replaceAll("\\|", "\\\\middle\\\\vert ");
        return new TeXFormula(tp, "\\left\\langle " + str + "\\right\\rangle").root;
    }

    public static final Atom Set_macro(final TeXParser tp, final String[] args) throws ParseException {
        String str = args[1].replaceFirst("\\|", "\\\\middle\\\\vert ");
        return new TeXFormula(tp, "\\left\\{" + str + "\\right\\}").root;
    }

    public static final Atom spATbreve_macro(final TeXParser tp, final String[] args) throws ParseException {
        VRowAtom vra = new VRowAtom(new TeXFormula("\\displaystyle\\!\\breve{}").root);
        vra.setRaise(TeXConstants.UNIT_EX, 0.6f);

        return new SmashedAtom(vra, null);
    }

    public static final Atom spAThat_macro(final TeXParser tp, final String[] args) throws ParseException {
        VRowAtom vra = new VRowAtom(new TeXFormula("\\displaystyle\\widehat{}").root);
        vra.setRaise(TeXConstants.UNIT_EX, 0.6f);

        return new SmashedAtom(vra, null);
    }

    public static final Atom hvspace_macro(final TeXParser tp, final String[] args) throws ParseException {
        int i;
        for (i = 0; i < args[1].length() && !Character.isLetter(args[1].charAt(i)); i++);
        float f = 0;
        try {
            f = Float.parseFloat(args[1].substring(0, i));
        } catch (NumberFormatException e) {
            throw new ParseException(e.toString());
        }

        int unit;
        if (i != args[1].length()) {
            unit = SpaceAtom.getUnit(args[1].substring(i).toLowerCase());
        } else {
            unit = TeXConstants.UNIT_POINT;
        }

        if (unit == -1) {
            throw new ParseException("Unknown unit \"" + args[1].substring(i) + "\" !");
        }

        return args[0].charAt(0) == 'h' ? new SpaceAtom(unit, f, 0, 0) : new SpaceAtom(unit, 0, f, 0);
    }

    public static final Atom clrlap_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new LapedAtom(new TeXFormula(tp, args[1]).root, args[0].charAt(0));
    }

    public static final Atom mathclrlap_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new LapedAtom(new TeXFormula(tp, args[1]).root, args[0].charAt(4));
    }

    public static final Atom includegraphics_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new GraphicsAtom(args[1], args[2]);
    }

    public static final Atom rule_macro(final TeXParser tp, final String[] args) throws ParseException {
        float[] winfo = SpaceAtom.getLength(args[1]);
        if (winfo.length == 1) {
            throw new ParseException("Error in getting width in \\rule command !");
        }
        float[] hinfo = SpaceAtom.getLength(args[2]);
        if (hinfo.length == 1) {
            throw new ParseException("Error in getting height in \\rule command !");
        }

        float[] rinfo = SpaceAtom.getLength(args[3]);
        if (rinfo.length == 1) {
            throw new ParseException("Error in getting raise in \\rule command !");
        }

        return new RuleAtom((int) winfo[0], winfo[1], (int) hinfo[0], hinfo[1], (int) rinfo[0], -rinfo[1]);
    }

    /* Thanks to Juan Enrique Escobar Robles for this macro */
    public static final Atom cfrac_macro(final TeXParser tp, final String[] args) throws ParseException {
        int alig = TeXConstants.ALIGN_CENTER;
        if ("r".equals(args[3])) {
            alig = TeXConstants.ALIGN_RIGHT;
        } else if ("l".equals(args[3])) {
            alig = TeXConstants.ALIGN_LEFT;
        }
        TeXFormula num = new TeXFormula(tp, args[1], false);
        TeXFormula denom = new TeXFormula(tp, args[2], false);
        if (num.root == null || denom.root == null) {
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");
        }
        Atom f = new FractionAtom(num.root, denom.root, true, alig, TeXConstants.ALIGN_CENTER);
        RowAtom rat = new RowAtom();
        rat.add(new StyleAtom(TeXConstants.STYLE_DISPLAY, f));
        return rat;
    }

    public static final Atom frac_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula num = new TeXFormula(tp, args[1], false);
        TeXFormula denom = new TeXFormula(tp, args[2], false);
        if (num.root == null || denom.root == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");
        return new FractionAtom(num.root, denom.root, true);
    }

    public static final Atom sfrac_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula num = new TeXFormula(tp, args[1], false);
        TeXFormula denom = new TeXFormula(tp, args[2], false);
        if (num.root == null || denom.root == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");

        double scaleX = 0.75;
        double scaleY = 0.75;
        float raise1 = 0.45f;
        float shiftL = -0.13f;
        float shiftR = -0.065f;
        Atom slash = SymbolAtom.get("slash");

        if (!tp.isMathMode()) {
            scaleX = 0.6;
            scaleY = 0.5;
            raise1 = 0.75f;
            shiftL = -0.24f;
            shiftR = -0.24f;
            slash = new VRowAtom(new ScaleAtom(SymbolAtom.get("textfractionsolidus"), 1.25, 0.65));
            ((VRowAtom) slash).setRaise(TeXConstants.UNIT_EX, 0.4f);
        }

        VRowAtom snum = new VRowAtom(new ScaleAtom(num.root, scaleX, scaleY));
        snum.setRaise(TeXConstants.UNIT_EX, raise1);
        RowAtom at = new RowAtom(snum);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, shiftL, 0f, 0f));
        at.add(slash);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, shiftR, 0f, 0f));
        at.add(new ScaleAtom(denom.root, scaleX, scaleY));

        return at;
    }

    public static final Atom genfrac_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula left = new TeXFormula(tp, args[1], false);
        SymbolAtom L = null, R = null;
        if (left != null && left.root instanceof SymbolAtom) {
            L = (SymbolAtom) left.root;
        }

        TeXFormula right = new TeXFormula(tp, args[2], false);
        if (right != null && right.root instanceof SymbolAtom) {
            R = (SymbolAtom) right.root;
        }

        boolean rule = true;
        float[] ths = SpaceAtom.getLength(args[3]);
        if (args[3] == null || args[3].length() == 0 || ths.length == 1) {
            ths = new float[] {0.0f, 0.0f};
            rule = false;
        }

        int style = 0;
        if (args[4].length() != 0) {
            style = Integer.parseInt(args[4]);
        }
        TeXFormula num = new TeXFormula(tp, args[5], false);
        TeXFormula denom = new TeXFormula(tp, args[6], false);
        if (num.root == null || denom.root == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");
        Atom at = new FractionAtom(num.root, denom.root, rule, (int) ths[0], ths[1]);
        RowAtom rat = new RowAtom();
        rat.add(new StyleAtom(style * 2, new FencedAtom(at, L, R)));

        return rat;
    }

    public static final Atom over_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;
        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");
        return new FractionAtom(num, denom, true);
    }

    public static final Atom overwithdelims_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;

        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");

        Atom left = new TeXFormula(tp, args[1], false).root;
        if (left instanceof BigDelimiterAtom)
            left = ((BigDelimiterAtom)left).delim;
        Atom right = new TeXFormula(tp, args[2], false).root;
        if (right instanceof BigDelimiterAtom)
            right = ((BigDelimiterAtom)right).delim;
        if (left instanceof SymbolAtom && right instanceof SymbolAtom) {
            return new FencedAtom(new FractionAtom(num, denom, true), (SymbolAtom) left, (SymbolAtom) right);
        }

        RowAtom ra = new RowAtom();
        ra.add(left);
        ra.add(new FractionAtom(num, denom, true));
        ra.add(right);
        return ra;
    }

    public static final Atom atop_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;
        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");
        return new FractionAtom(num, denom, false);
    }

    public static final Atom atopwithdelims_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;

        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");

        Atom left = new TeXFormula(tp, args[1], false).root;
        if (left instanceof BigDelimiterAtom)
            left = ((BigDelimiterAtom)left).delim;
        Atom right = new TeXFormula(tp, args[2], false).root;
        if (right instanceof BigDelimiterAtom)
            right = ((BigDelimiterAtom)right).delim;
        if (left instanceof SymbolAtom && right instanceof SymbolAtom) {
            return new FencedAtom(new FractionAtom(num, denom, false), (SymbolAtom) left, (SymbolAtom) right);
        }

        RowAtom ra = new RowAtom();
        ra.add(left);
        ra.add(new FractionAtom(num, denom, false));
        ra.add(right);
        return ra;
    }

    public static final Atom choose_macro(final TeXParser tp, final String[] args) throws ParseException {
        return choose_brackets("lbrack", "rbrack", tp, args);
    }

    public static final Atom brack_macro(final TeXParser tp, final String[] args) throws ParseException {
        return choose_brackets("lsqbrack", "rsqbrack", tp, args);
    }

    public static final Atom bangle_macro(final TeXParser tp, final String[] args) throws ParseException {
        return choose_brackets("langle", "rangle", tp, args);
    }

    public static final Atom brace_macro(final TeXParser tp, final String[] args) throws ParseException {
        return choose_brackets("lbrace", "rbrace", tp, args);
    }

    public static final Atom choose_brackets(final String left, final String right, final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;
        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of choose can't be empty!");
        return new FencedAtom(new FractionAtom(num, denom, false), new SymbolAtom(left, TeXConstants.TYPE_OPENING, true), new SymbolAtom(right, TeXConstants.TYPE_CLOSING, true));
    }

    public static final Atom binom_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula num = new TeXFormula(tp, args[1], false);
        TeXFormula denom = new TeXFormula(tp, args[2], false);
        if (num.root == null || denom.root == null)
            throw new ParseException("Both binomial coefficients must be not empty !!");
        return new FencedAtom(new FractionAtom(num.root, denom.root, false), new SymbolAtom("lbrack", TeXConstants.TYPE_OPENING, true), new SymbolAtom("rbrack", TeXConstants.TYPE_CLOSING, true));
    }

    public static final Atom above_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        float[] dim = tp.getLength();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;
        if (dim == null || dim.length != 2) {
            throw new ParseException("Invalid length in above macro");
        }
        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");

        return new FractionAtom(num, denom, (int) dim[0], dim[1]);
    }

    public static final Atom abovewithdelims_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom num = tp.getFormulaAtom();
        float[] dim = tp.getLength();
        Atom denom = new TeXFormula(tp, tp.getOverArgument(), false).root;
        if (dim == null || dim.length != 2) {
            throw new ParseException("Invalid length in above macro");
        }
        if (num == null || denom == null)
            throw new ParseException("Both numerator and denominator of a fraction can't be empty!");

        Atom left = new TeXFormula(tp, args[1], false).root;
        if (left instanceof BigDelimiterAtom)
            left = ((BigDelimiterAtom)left).delim;
        Atom right = new TeXFormula(tp, args[2], false).root;
        if (right instanceof BigDelimiterAtom)
            right = ((BigDelimiterAtom)right).delim;
        if (left instanceof SymbolAtom && right instanceof SymbolAtom) {
            return new FencedAtom(new FractionAtom(num, denom, (int) dim[0], dim[1]), (SymbolAtom) left, (SymbolAtom) right);
        }

        RowAtom ra = new RowAtom();
        ra.add(left);
        ra.add(new FractionAtom(num, denom, true));
        ra.add(right);
        return ra;
    }

    public static final Atom textstyle_macros(final TeXParser tp, final String[] args) throws ParseException {
        String style = args[0];
        if ("frak".equals(args[0]))
            style = "mathfrak";
        else if ("Bbb".equals(args[0]))
            style = "mathbb";
        else if ("bold".equals(args[0]))
            return new BoldAtom(new TeXFormula(tp, args[1], false).root);
        else if ("cal".equals(args[0]))
            style = "mathcal";

        TeXFormula.FontInfos fontInfos = TeXFormula.externalFontMap.get(Character.UnicodeBlock.BASIC_LATIN);
        if (fontInfos != null) {
            TeXFormula.externalFontMap.put(Character.UnicodeBlock.BASIC_LATIN, null);
        }
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (fontInfos != null) {
            TeXFormula.externalFontMap.put(Character.UnicodeBlock.BASIC_LATIN, fontInfos);
        }

        return new TextStyleAtom(at, style);
    }

    public static final Atom mbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom group = new RomanAtom(new TeXFormula(tp, args[1], "mathnormal", false, false).root);
        return new StyleAtom(TeXConstants.STYLE_TEXT, group);
    }

    public static final Atom text_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new RomanAtom(new TeXFormula(tp, args[1], "mathnormal", false, false).root);
    }

    public static final Atom underscore_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderscoreAtom();
    }

    public static final Atom accent_macros(final TeXParser tp, final String[] args) throws ParseException {
        return new AccentedAtom(new TeXFormula(tp, args[1], false).root, args[0]);
    }

    public static final Atom grkaccent_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new AccentedAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root, false);
    }

    public static final Atom accent_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new AccentedAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom accentbis_macros(final TeXParser tp, final String[] args) throws ParseException {
        String acc = "";
        switch (args[0].charAt(0)) {
        case '~' :
            acc = "tilde";
            break;
        case '\'' :
            acc = "acute";
            break;
        case '^' :
            acc = "hat";
            break;
        case '\"' :
            acc = "ddot";
            break;
        case '`' :
            acc = "grave";
            break;
        case '=' :
            acc = "bar";
            break;
        case '.' :
            acc = "dot";
            break;
        case 'u' :
            acc = "breve";
            break;
        case 'v' :
            acc = "check";
            break;
        case 'H' :
            acc = "doubleacute";
            break;
        case 't' :
            acc = "tie";
            break;
        case 'r' :
            acc = "mathring";
            break;
        case 'U' :
            acc = "cyrbreve";
        }

        return new AccentedAtom(new TeXFormula(tp, args[1], false).root, acc);
    }

    public static final Atom cedilla_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new CedillaAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom IJ_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new IJAtom(args[0].charAt(0) == 'I');
    }

    public static final Atom TStroke_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TStrokeAtom(args[0].charAt(0) == 'T');
    }

    public static final Atom LCaron_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new LCaronAtom(args[0].charAt(0) == 'L');
    }

    public static final Atom tcaron_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new tcaronAtom();
    }

    public static final Atom ogonek_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OgonekAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom nbsp_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SpaceAtom();
    }

    public static final Atom sqrt_macro(final TeXParser tp, final String[] args) throws ParseException {
        if (args[2] == null)
            return new NthRoot(new TeXFormula(tp, args[1], false).root, null);
        return new NthRoot(new TeXFormula(tp, args[1], false).root, new TeXFormula(tp, args[2], false).root);
    }

    public static final Atom overrightarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, false, true);
    }

    public static final Atom overleftarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, true, true);
    }

    public static final Atom overleftrightarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, true);
    }

    public static final Atom underrightarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, false, false);
    }

    public static final Atom underleftarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, true, false);
    }

    public static final Atom underleftrightarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverArrowAtom(new TeXFormula(tp, args[1], false).root, false);
    }

    public static final Atom xleftarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new XArrowAtom(new TeXFormula(tp, args[1], false).root, new TeXFormula(tp, args[2]).root, true);
    }

    public static final Atom xrightarrow_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new XArrowAtom(new TeXFormula(tp, args[1], false).root, new TeXFormula(tp, args[2]).root, false);
    }

    public static final Atom sideset_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula tf = new TeXFormula();
        tf.add(new PhantomAtom(new TeXFormula(tp, args[3]).root, false, true, true));
        tf.append(tp.getIsPartial(), args[1]);
        tf.add(new SpaceAtom(TeXConstants.UNIT_MU, -0.3f, 0f, 0f));
        tf.append(tp.getIsPartial(), args[3] + "\\nolimits" + args[2]);
        return new TypedAtom(TeXConstants.TYPE_ORDINARY, TeXConstants.TYPE_ORDINARY, tf.root);
    }

    public static final Atom prescript_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom base = new TeXFormula(tp, args[3]).root;
        tp.addAtom(new ScriptsAtom(new PhantomAtom(base, false, true, true), new TeXFormula(tp, args[2]).root, new TeXFormula(tp, args[1]).root, false));
        tp.addAtom(new SpaceAtom(TeXConstants.UNIT_MU, -0.3f, 0f, 0f));
        return new TypedAtom(TeXConstants.TYPE_ORDINARY, TeXConstants.TYPE_ORDINARY, base);
    }

    public static final Atom underbrace_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("rbrace"), TeXConstants.UNIT_EX, 0, false);
    }

    public static final Atom overbrace_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("lbrace"), TeXConstants.UNIT_EX, 0, true);
    }

    public static final Atom underbrack_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("rsqbrack"), TeXConstants.UNIT_EX, 0, false);
    }

    public static final Atom overbrack_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("lsqbrack"), TeXConstants.UNIT_EX, 0, true);
    }

    public static final Atom underparen_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("rbrack"), TeXConstants.UNIT_EX, 0, false);
    }

    public static final Atom overparen_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverUnderDelimiter(new TeXFormula(tp, args[1], false).root, null, SymbolAtom.get("lbrack"), TeXConstants.UNIT_EX, 0, true);
    }

    public static final Atom overline_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OverlinedAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom underline_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderlinedAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathop_macro(final TeXParser tp, final String[] args) throws ParseException {
        TypedAtom at =  new TypedAtom(TeXConstants.TYPE_BIG_OPERATOR, TeXConstants.TYPE_BIG_OPERATOR, new TeXFormula(tp, args[1], false).root);
        at.type_limits = TeXConstants.SCRIPT_NORMAL;
        return at;
    }

    public static final Atom mathpunct_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_PUNCTUATION, TeXConstants.TYPE_PUNCTUATION, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathord_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_ORDINARY, TeXConstants.TYPE_ORDINARY, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathrel_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathinner_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_INNER, TeXConstants.TYPE_INNER, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathbin_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_BINARY_OPERATOR, TeXConstants.TYPE_BINARY_OPERATOR, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathopen_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_OPENING, TeXConstants.TYPE_OPENING, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathclose_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_CLOSING, TeXConstants.TYPE_CLOSING, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom joinrel_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, new SpaceAtom(TeXConstants.UNIT_MU, -2.6f, 0, 0));
    }

    public static final Atom smash_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SmashedAtom(new TeXFormula(tp, args[1], false).root, args[2]);
    }

    public static final Atom vdots_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new VdotsAtom();
    }

    public static final Atom ddots_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_INNER, TeXConstants.TYPE_INNER, new DdotsAtom());
    }

    public static final Atom iddots_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TypedAtom(TeXConstants.TYPE_INNER, TeXConstants.TYPE_INNER, new IddotsAtom());
    }

    public static final Atom nolimits_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = tp.getLastAtom();
        at.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        return at.clone();
    }

    public static final Atom limits_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = tp.getLastAtom();
        at.type_limits = TeXConstants.SCRIPT_LIMITS;
        return at.clone();
    }

    public static final Atom normal_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = tp.getLastAtom();
        at.type_limits = TeXConstants.SCRIPT_NORMAL;
        return at.clone();
    }

    public static final Atom left_macro(final TeXParser tp, final String[] args) throws ParseException {
        String grp = tp.getGroup("\\left", "\\right");
        Atom left = new TeXFormula(tp, args[1], false).root;
        if (left instanceof BigDelimiterAtom)
            left = ((BigDelimiterAtom)left).delim;
        Atom right = tp.getArgument();
        if (right instanceof BigDelimiterAtom)
            right = ((BigDelimiterAtom)right).delim;
        if (left instanceof SymbolAtom && right instanceof SymbolAtom) {
            TeXFormula tf = new TeXFormula(tp, grp, false);
            return new FencedAtom(tf.root, (SymbolAtom)left, tf.middle, (SymbolAtom)right);
        }

        RowAtom ra = new RowAtom();
        ra.add(left);
        ra.add(new TeXFormula(tp, grp, false).root);
        ra.add(right);
        return ra;
    }

    public static final Atom leftparenthesis_macro(final TeXParser tp, final String[] args) throws ParseException {
        String grp = tp.getGroup("\\(", "\\)");
        return new MathAtom(new TeXFormula(tp, grp, false).root, TeXConstants.STYLE_TEXT);
    }

    public static final Atom leftbracket_macro(final TeXParser tp, final String[] args) throws ParseException {
        String grp = tp.getGroup("\\[", "\\]");
        return new MathAtom(new TeXFormula(tp, grp, false).root, TeXConstants.STYLE_DISPLAY);
    }

    public static final Atom middle_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new MiddleAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom cr_macro(final TeXParser tp, final String[] args) throws ParseException {
        if (tp.isArrayMode()) {
            tp.addRow();
        } else {
            ArrayOfAtoms array = new ArrayOfAtoms();
            array.add(tp.formula.root);
            array.addRow();
            TeXParser parser = new TeXParser(tp.getIsPartial(), tp.getStringFromCurrentPos(), array, false, tp.isIgnoreWhiteSpace());
            parser.parse();
            array.checkDimensions();
            tp.finish();
            tp.formula.root = array.getAsVRow();//new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.ARRAY, TeXConstants.ALIGN_LEFT, false);
        }

        return null;
    }

    public static final Atom backslashcr_macro(final TeXParser tp, final String[] args) throws ParseException {
        return cr_macro(tp, args);
    }

    public static final Atom intertext_macro(final TeXParser tp, final String[] args) throws ParseException {
        if (!tp.isArrayMode()) {
            throw new ParseException("Bad environment for \\intertext command !");
        }

        String str = args[1].replaceAll("\\^\\{\\\\prime\\}", "\'");
        str = str.replaceAll("\\^\\{\\\\prime\\\\prime\\}", "\'\'");
        Atom at = new RomanAtom(new TeXFormula(tp, str, "mathnormal", false, false).root);
        at.type = TeXConstants.TYPE_INTERTEXT;
        tp.addAtom(at);
        tp.addRow();
        return null;
    }

    public static final Atom smallmatrixATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.SMALLMATRIX);
    }

    public static final Atom matrixATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.MATRIX);
    }

    public static final Atom multicolumn_macro(final TeXParser tp, final String[] args) throws ParseException {
        int n = Integer.parseInt(args[1]);
        tp.addAtom(new MulticolumnAtom(n, args[2], new TeXFormula(tp, args[3]).root));
        ((ArrayOfAtoms)tp.formula).addCol(n);
        return null;
    }

    public static final Atom hdotsfor_macro(final TeXParser tp, final String[] args) throws ParseException {
        int n = Integer.parseInt(args[1]);
        float f = 1;
        if (args[2] != null) {
            f = Float.parseFloat(args[2]);
        }
        tp.addAtom(new HdotsforAtom(n, f));
        ((ArrayOfAtoms)tp.formula).addCol(n);
        return null;
    }

    public static final Atom arrayATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[2], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, args[1], true);
    }

    public static final Atom alignATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.ALIGN);
    }

    public static final Atom flalignATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.FLALIGN);
    }

    public static final Atom alignatATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[2], array, false);
        parser.parse();
        array.checkDimensions();
        int n = Integer.parseInt(args[1]);
        if (array.col != 2 * n) {
            throw new ParseException("Bad number of equations in alignat environment !");
        }

        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.ALIGNAT);
    }

    public static final Atom alignedATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.ALIGNED);
    }

    public static final Atom alignedatATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[2], array, false);
        parser.parse();
        array.checkDimensions();
        int n = Integer.parseInt(args[1]);
        if (array.col != 2 * n) {
            throw new ParseException("Bad number of equations in alignedat environment !");
        }

        return new MatrixAtom(tp.getIsPartial(), array, MatrixAtom.ALIGNEDAT);
    }

    public static final Atom multlineATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        if (array.col > 1) {
            throw new ParseException("Character '&' is only available in array mode !");
        }
        if (array.col == 0) {
            return null;
        }

        return new MultlineAtom(tp.getIsPartial(), array, MultlineAtom.MULTLINE);
    }

    public static final Atom gatherATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        if (array.col > 1) {
            throw new ParseException("Character '&' is only available in array mode !");
        }
        if (array.col == 0) {
            return null;
        }

        return new MultlineAtom(tp.getIsPartial(), array, MultlineAtom.GATHER);
    }

    public static final Atom gatheredATATenv_macro(final TeXParser tp, final String[] args) throws ParseException {
        ArrayOfAtoms array = new ArrayOfAtoms();
        TeXParser parser = new TeXParser(tp.getIsPartial(), args[1], array, false);
        parser.parse();
        array.checkDimensions();
        if (array.col > 1) {
            throw new ParseException("Character '&' is only available in array mode !");
        }
        if (array.col == 0) {
            return null;
        }

        return new MultlineAtom(tp.getIsPartial(), array, MultlineAtom.GATHERED);
    }

    public static final Atom shoveright_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1]).root;
        at.alignment = TeXConstants.ALIGN_RIGHT;
        return at;
    }

    public static final Atom shoveleft_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1]).root;
        at.alignment = TeXConstants.ALIGN_LEFT;
        return at;
    }

    public static final Atom newcommand_macro(final TeXParser tp, final String[] args) throws ParseException {
        String newcom = args[1];
        Integer nbArgs;
        if (!tp.isValidName(newcom)) {
            throw new ParseException("Invalid name for the command :" + newcom);
        }

        if (args[3] == null)
            nbArgs = new Integer(0);
        else
            nbArgs = Integer.parseInt(args[3]);

        if (nbArgs == null) {
            throw new ParseException("The optional argument should be an integer !");
        }

        if (args[4] == null)
            NewCommandMacro.addNewCommand(newcom.substring(1), args[2], nbArgs.intValue());
        else
            NewCommandMacro.addNewCommand(newcom.substring(1), args[2], nbArgs.intValue(), args[4]);

        return null;
    }

    public static final Atom renewcommand_macro(final TeXParser tp, final String[] args) throws ParseException {
        String newcom = args[1];
        Integer nbArgs;
        if (!tp.isValidName(newcom)) {
            throw new ParseException("Invalid name for the command :" + newcom);
        }

        if (args[3] == null)
            nbArgs = new Integer(0);
        else
            nbArgs = Integer.parseInt(args[3]);

        if (nbArgs == null)
            throw new ParseException("The optional argument should be an integer !");

        NewCommandMacro.addReNewCommand(newcom.substring(1), args[2], nbArgs.intValue());

        return null;
    }

    public static final Atom makeatletter_macro(final TeXParser tp, final String[] args) throws ParseException {
        tp.makeAtLetter();
        return null;
    }

    public static final Atom makeatother_macro(final TeXParser tp, final String[] args) throws ParseException {
        tp.makeAtOther();
        return null;
    }

    public static final Atom newenvironment_macro(final TeXParser tp, final String[] args) throws ParseException {
        Integer opt = args[4] == null ? 0 : Integer.parseInt(args[4]);
        if (opt == null)
            throw new ParseException("The optional argument should be an integer !");

        NewEnvironmentMacro.addNewEnvironment(args[1], args[2], args[3], opt.intValue());
        return null;
    }

    public static final Atom renewenvironment_macro(final TeXParser tp, final String[] args) throws ParseException {
        Integer opt = args[4] == null ? 0 : Integer.parseInt(args[4]);
        if (opt == null)
            throw new ParseException("The optional argument should be an integer !");

        NewEnvironmentMacro.addReNewEnvironment(args[1], args[2], args[3], opt.intValue());
        return null;
    }

    public static final Atom fbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new FBoxAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom questeq_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(SymbolAtom.get(TeXFormula.symbolMappings['=']), new ScaleAtom(SymbolAtom.get(TeXFormula.symbolMappings['?']), 0.75f), TeXConstants.UNIT_MU, 2.5f, true, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom stackrel_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[3], false).root, TeXConstants.UNIT_MU, 0.5f, true, new TeXFormula(tp, args[1], false).root, TeXConstants.UNIT_MU, 2.5f, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom stackbin_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[3], false).root, TeXConstants.UNIT_MU, 0.5f, true, new TeXFormula(tp, args[1], false).root, TeXConstants.UNIT_MU, 2.5f, true);
        return new TypedAtom(TeXConstants.TYPE_BINARY_OPERATOR, TeXConstants.TYPE_BINARY_OPERATOR, at);
    }

    public static final Atom overset_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root, TeXConstants.UNIT_MU, 2.5f, true, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom underset_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root, TeXConstants.UNIT_MU, 0.5f, true, false);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom accentset_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new AccentedAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom underaccent_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new UnderOverAtom(new TeXFormula(tp, args[2], false).root, new TeXFormula(tp, args[1], false).root, TeXConstants.UNIT_MU, 0.3f, true, false);
    }

    public static final Atom undertilde_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        return new UnderOverAtom(at, new AccentedAtom(new PhantomAtom(at, true, false, false), "widetilde"), TeXConstants.UNIT_MU, 0.3f, true, false);
    }

    public static final Atom boldsymbol_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new BoldAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom mathrm_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new RomanAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom rm_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new RomanAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root);
    }

    public static final Atom mathbf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new BoldAtom(new RomanAtom(new TeXFormula(tp, args[1], false).root));
    }

    public static final Atom bf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new BoldAtom(new RomanAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root));
    }

    public static final Atom mathtt_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TtAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom tt_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TtAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root);
    }

    public static final Atom mathit_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ItAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom it_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ItAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root);
    }

    public static final Atom mathsf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SsAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom sf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SsAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root);
    }

    public static final Atom LaTeX_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new LaTeXAtom();
    }

    public static final Atom GeoGebra_macro(final TeXParser tp, final String[] args) throws ParseException {
        TeXFormula tf = new TeXFormula("\\mathbb{G}\\mathsf{e}");
        tf.add(new GeoGebraLogoAtom());
        tf.add("\\mathsf{Gebra}");
        return new ColorAtom(tf.root, null, new Color(102, 102, 102));
    }

    public static final Atom hphantom_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new PhantomAtom(new TeXFormula(tp, args[1], false).root, true, false, false);
    }

    public static final Atom vphantom_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new PhantomAtom(new TeXFormula(tp, args[1], false).root, false, true, true);
    }

    public static final Atom phantom_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new PhantomAtom(new TeXFormula(tp, args[1], false).root, true, true, true);
    }

    public static final Atom big_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        return new BigDelimiterAtom((SymbolAtom) at, 1);
    }

    public static final Atom Big_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        return new BigDelimiterAtom((SymbolAtom) at, 2);
    }

    public static final Atom bigg_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        return new BigDelimiterAtom((SymbolAtom) at, 3);
    }

    public static final Atom Bigg_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        return new BigDelimiterAtom((SymbolAtom) at, 4);
    }

    public static final Atom bigl_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 1);
        att.type = TeXConstants.TYPE_OPENING;
        return att;
    }

    public static final Atom Bigl_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 2);
        att.type = TeXConstants.TYPE_OPENING;
        return att;
    }

    public static final Atom biggl_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 3);
        att.type = TeXConstants.TYPE_OPENING;
        return att;
    }

    public static final Atom Biggl_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 4);
        att.type = TeXConstants.TYPE_OPENING;
        return att;
    }

    public static final Atom bigr_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 1);
        att.type = TeXConstants.TYPE_CLOSING;
        return att;
    }

    public static final Atom Bigr_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 2);
        att.type = TeXConstants.TYPE_CLOSING;
        return att;
    }

    public static final Atom biggr_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 3);
        att.type = TeXConstants.TYPE_CLOSING;
        return att;
    }

    public static final Atom Biggr_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new TeXFormula(tp, args[1], false).root;
        if (!(at instanceof SymbolAtom)) {
            return at;
        }
        Atom att = new BigDelimiterAtom((SymbolAtom) at, 4);
        att.type = TeXConstants.TYPE_CLOSING;
        return att;
    }

    public static final Atom displaystyle_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom group = new TeXFormula(tp, tp.getOverArgument(), false).root;
        return new StyleAtom(TeXConstants.STYLE_DISPLAY, group);
    }

    public static final Atom scriptstyle_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom group = new TeXFormula(tp, tp.getOverArgument(), false).root;
        return new StyleAtom(TeXConstants.STYLE_SCRIPT, group);
    }

    public static final Atom textstyle_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom group = new TeXFormula(tp, tp.getOverArgument(), false).root;
        return new StyleAtom(TeXConstants.STYLE_TEXT, group);
    }

    public static final Atom scriptscriptstyle_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom group = new TeXFormula(tp, tp.getOverArgument(), false).root;
        return new StyleAtom(TeXConstants.STYLE_SCRIPT_SCRIPT, group);
    }

    public static final Atom rotatebox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new RotateAtom(new TeXFormula(tp, args[2]).root, args[1] == null ? 0 : Double.parseDouble(args[1]), args[3]);
    }

    public static final Atom reflectbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ReflectAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom scalebox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ScaleAtom(new TeXFormula(tp, args[2]).root, Double.parseDouble(args[1]), args[3] == null ? Double.parseDouble(args[1]) : Double.parseDouble(args[3]));
    }

    public static final Atom resizebox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ResizeAtom(new TeXFormula(tp, args[3]).root, args[1], args[2], args[1].equals("!") || args[2].equals("!"));
    }

    public static final Atom raisebox_macro(final TeXParser tp, final String[] args) throws ParseException {
        float[] raise = SpaceAtom.getLength(args[1]);
        if (raise.length == 1) {
            throw new ParseException("Error in getting raise in \\raisebox command !");
        }
        float[] height = SpaceAtom.getLength(args[3]);
        float[] depth = SpaceAtom.getLength(args[4]);
        if (height.length == 1 || height[1] == 0) {
            height = new float[] {-1, 0};
        }
        if (depth.length == 1 || depth[1] == 0) {
            depth = new float[] {-1, 0};
        }

        return new RaiseAtom(new TeXFormula(tp, args[2]).root, (int) raise[0], raise[1], (int) height[0], height[1], (int) depth[0], depth[1]);
    }

    public static final Atom shadowbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ShadowAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom ovalbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new OvalAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom doublebox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new DoubleFramedAtom(new TeXFormula(tp, args[1]).root);
    }

    public static final Atom definecolor_macro(final TeXParser tp, final String[] args) throws ParseException {
        Color color = null;
        if ("gray".equals(args[2])) {
            float f = Float.parseFloat(args[3]);
            color = new Color(f, f, f);
        } else if ("rgb".equals(args[2])) {
            StringTokenizer stok = new StringTokenizer(args[3], ";,");
            if (stok.countTokens() != 3)
                throw new ParseException("The color definition must have three components !");
            float r = Float.parseFloat(stok.nextToken().trim());
            float g = Float.parseFloat(stok.nextToken().trim());
            float b = Float.parseFloat(stok.nextToken().trim());
            color = new Color(r, g, b);
        } else if ("cmyk".equals(args[2])) {
            StringTokenizer stok = new StringTokenizer(args[3], ",;");
            if (stok.countTokens() != 4)
                throw new ParseException("The color definition must have four components !");
            float[] cmyk = new float[4];
            for (int i = 0; i < 4; i++)
                cmyk[i] = Float.parseFloat(stok.nextToken().trim());
            float k = 1 - cmyk[3];
            color = new Color(k * (1 - cmyk[0]), k * (1 - cmyk[1]), k * (1 - cmyk[2]));
        } else
            throw new ParseException("The color model is incorrect !");

        ColorAtom.Colors.put(args[1], color);
        return null;
    }

    public static final Atom fgcolor_macro(final TeXParser tp, final String[] args) throws ParseException {
        try {
            return new ColorAtom(new TeXFormula(tp, args[2]).root, null, ColorAtom.getColor(args[1]));
        } catch (NumberFormatException e) {
            throw new ParseException(e.toString());
        }
    }

    public static final Atom bgcolor_macro(final TeXParser tp, final String[] args) throws ParseException {
        try {
            return new ColorAtom(new TeXFormula(tp, args[2]).root, ColorAtom.getColor(args[1]), null);
        } catch (NumberFormatException e) {
            throw new ParseException(e.toString());
        }
    }

    public static final Atom textcolor_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new ColorAtom(new TeXFormula(tp, args[2]).root, null, ColorAtom.getColor(args[1]));
    }

    public static final Atom colorbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        Color c = ColorAtom.getColor(args[1]);
        return new FBoxAtom(new TeXFormula(tp, args[2]).root, c, c);
    }

    public static final Atom fcolorbox_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new FBoxAtom(new TeXFormula(tp, args[3]).root, ColorAtom.getColor(args[2]), ColorAtom.getColor(args[1]));
    }

    public static final Atom cong_macro(final TeXParser tp, final String[] args) throws ParseException {
        VRowAtom vra = new VRowAtom(SymbolAtom.get("equals"));
        vra.add(new SpaceAtom(TeXConstants.UNIT_MU, 0f, 1.5f, 0f));
        vra.add(SymbolAtom.get("sim"));
        vra.setRaise(TeXConstants.UNIT_MU, -1f);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, vra);
    }

    public static final Atom doteq_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(SymbolAtom.get("equals"), SymbolAtom.get("ldotp"), TeXConstants.UNIT_MU, 3.7f, false, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom jlmDynamic_macro(final TeXParser tp, final String[] args) throws ParseException {
        if (DynamicAtom.hasAnExternalConverterFactory()) {
            return new DynamicAtom(args[1], args[2]);
        } else {
            throw new ParseException("No ExternalConverterFactory set !");
        }
    }

    public static final Atom jlmExternalFont_macro(final TeXParser tp, final String[] args) throws ParseException {
        JavaFontRenderingBox.setFont(args[1]);
        return null;
    }

    public static final Atom jlmText_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new JavaFontRenderingAtom(args[1], Font.PLAIN);
    }

    public static final Atom jlmTextit_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new JavaFontRenderingAtom(args[1], Font.ITALIC);
    }

    public static final Atom jlmTextbf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new JavaFontRenderingAtom(args[1], Font.BOLD);
    }

    public static final Atom jlmTextitbf_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new JavaFontRenderingAtom(args[1], Font.BOLD | Font.ITALIC);
    }

    public static final Atom DeclareMathSizes_macro(final TeXParser tp, final String[] args) throws ParseException {
        DefaultTeXFont.setMathSizes(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
        return null;
    }

    public static final Atom magnification_macro(final TeXParser tp, final String[] args) throws ParseException {
        DefaultTeXFont.setMagnification(Float.parseFloat(args[1]));
        return null;
    }

    public static final Atom hline_macro(final TeXParser tp, final String[] args) throws ParseException {
        if (!tp.isArrayMode())
            throw new ParseException("The macro \\hline is only available in array mode !");
        return new HlineAtom();
    }

    public static final Atom size_macros(final TeXParser tp, final String[] args) throws ParseException {
        float f = 1f;
        if ("tiny".equals(args[0])) {
            f = 0.5f;
        } else if ("scriptsize".equals(args[0])) {
            f = 0.7f;
        } else if ("footnotesize".equals(args[0])) {
            f = 0.8f;
        } else if ("small".equals(args[0])) {
            f = 0.9f;
        } else if ("normalsize".equals(args[0])) {
            f = 1f;
        } else if ("large".equals(args[0])) {
            f = 1.2f;
        } else if ("Large".equals(args[0])) {
            f = 1.4f;
        } else if ("LARGE".equals(args[0])) {
            f = 1.8f;
        } else if ("huge".equals(args[0])) {
            f = 2f;
        } else if ("Huge".equals(args[0])) {
            f = 2.5f;
        }

        return new MonoScaleAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root, f);
    }

    public static final Atom jlatexmathcumsup_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new CumulativeScriptsAtom(tp.getLastAtom(), null, new TeXFormula(tp, args[1]).root);
    }

    public static final Atom jlatexmathcumsub_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new CumulativeScriptsAtom(tp.getLastAtom(), new TeXFormula(tp, args[1]).root, null);
    }

    public static final Atom dotminus_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(SymbolAtom.get("minus"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, -3.3f, false, true);
        return new TypedAtom(TeXConstants.TYPE_BINARY_OPERATOR, TeXConstants.TYPE_BINARY_OPERATOR, at);
    }

    public static final Atom ratio_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom geoprop_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom ddot = new RowAtom(SymbolAtom.get("normaldot"));
        ddot.add(new SpaceAtom(TeXConstants.UNIT_MU, 4f, 0f, 0f));
        ddot.add(SymbolAtom.get("normaldot"));
        Atom at = new UnderOverAtom(SymbolAtom.get("minus"), ddot, TeXConstants.UNIT_MU, -3.4f, false, ddot, TeXConstants.UNIT_MU, -3.4f, false);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom minuscolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("minus"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        at.add(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom minuscoloncolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("minus"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        at.add(colon);
        at.add(colon);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom simcolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("sim"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        at.add(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom simcoloncolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("sim"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        at.add(colon);
        at.add(colon);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom approxcolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("approx"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        at.add(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom approxcoloncolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("approx"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        at.add(colon);
        at.add(colon);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom equalscolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("equals"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        at.add(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom equalscoloncolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(SymbolAtom.get("equals"));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.095f, 0f, 0f));
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        at.add(colon);
        at.add(colon);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom colonminus_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("minus"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom coloncolonminus_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        RowAtom at = new RowAtom(colon);
        at.add(colon);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("minus"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom colonequals_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("equals"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom coloncolonequals_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        RowAtom at = new RowAtom(colon);
        at.add(colon);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("equals"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom coloncolon_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        RowAtom at = new RowAtom(colon);
        at.add(colon);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom colonsim_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("sim"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom coloncolonsim_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        RowAtom at = new RowAtom(colon);
        at.add(colon);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("sim"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom colonapprox_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom at = new RowAtom(new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true));
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("approx"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom coloncolonapprox_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom colon = new UnderOverAtom(SymbolAtom.get("normaldot"), SymbolAtom.get("normaldot"), TeXConstants.UNIT_MU, 5.2f, false, true);
        RowAtom at = new RowAtom(colon);
        at.add(colon);
        at.add(new SpaceAtom(TeXConstants.UNIT_EM, -0.32f, 0f, 0f));
        at.add(SymbolAtom.get("approx"));
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom smallfrowneq_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new UnderOverAtom(SymbolAtom.get("equals"), SymbolAtom.get("smallfrown"), TeXConstants.UNIT_MU, -2f, true, true);
        return new TypedAtom(TeXConstants.TYPE_RELATION, TeXConstants.TYPE_RELATION, at);
    }

    public static final Atom hstrok_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom ra = new RowAtom(new SpaceAtom(TeXConstants.UNIT_EX, -0.1f, 0f, 0f));
        ra.add(SymbolAtom.get("bar"));
        VRowAtom vra = new VRowAtom(new LapedAtom(ra, 'r'));
        vra.setRaise(TeXConstants.UNIT_EX, -0.1f);
        RowAtom at = new RowAtom(vra);
        at.add(new RomanAtom(new CharAtom('h', tp.formula.textStyle)));
        return at;
    }

    public static final Atom Hstrok_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom ra = new RowAtom(new SpaceAtom(TeXConstants.UNIT_EX, 0.28f, 0f, 0f));
        ra.add(SymbolAtom.get("textendash"));
        VRowAtom vra = new VRowAtom(new LapedAtom(ra, 'r'));
        vra.setRaise(TeXConstants.UNIT_EX, 0.55f);
        RowAtom at = new RowAtom(vra);
        at.add(new RomanAtom(new CharAtom('H', tp.formula.textStyle)));
        return at;
    }

    public static final Atom dstrok_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom ra = new RowAtom(new SpaceAtom(TeXConstants.UNIT_EX, 0.25f, 0f, 0f));
        ra.add(SymbolAtom.get("bar"));
        VRowAtom vra = new VRowAtom(new LapedAtom(ra, 'r'));
        vra.setRaise(TeXConstants.UNIT_EX, -0.1f);
        RowAtom at = new RowAtom(vra);
        at.add(new RomanAtom(new CharAtom('d', tp.formula.textStyle)));
        return at;
    }

    public static final Atom Dstrok_macro(final TeXParser tp, final String[] args) throws ParseException {
        RowAtom ra = new RowAtom(new SpaceAtom(TeXConstants.UNIT_EX, -0.1f, 0f, 0f));
        ra.add(SymbolAtom.get("bar"));
        VRowAtom vra = new VRowAtom(new LapedAtom(ra, 'r'));
        vra.setRaise(TeXConstants.UNIT_EX, -0.55f);
        RowAtom at = new RowAtom(vra);
        at.add(new RomanAtom(new CharAtom('D', tp.formula.textStyle)));
        return at;
    }

    public static final Atom kern_macro(final TeXParser tp, final String[] args) throws ParseException {
        float[] info = SpaceAtom.getLength(args[1]);
        if (info.length == 1) {
            throw new ParseException("Error in getting kern in \\kern command !");
        }

        return new SpaceAtom((int) info[0], info[1], 0f, 0f);
    }

    public static final Atom char_macro(final TeXParser tp, final String[] args) throws ParseException {
        String number = args[1];
        int radix = 10;
        if (number.startsWith("0x") || number.startsWith("0X")) {
            number = number.substring(2);
            radix = 16;
        } else if (number.startsWith("x") || number.startsWith("X")) {
            number = number.substring(1);
            radix = 16;
        } else if (number.startsWith("0")) {
            number = number.substring(1);
            radix = 8;
        }
        int n = Integer.parseInt(number, radix);
        return tp.convertCharacter((char) n, true);
    }

    public static final Atom T_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new RotateAtom(new TeXFormula(tp, args[1]).root, 180, "origin=cc");
    }

    public static final Atom romannumeral_macro(final TeXParser tp, final String[] args) throws ParseException {
        int[] numbers = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] letters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        String roman = "";
        int num = Integer.parseInt(args[1].trim());
        for (int i = 0; i < numbers.length; i++) {
            while (num >= numbers[i]) {
                roman += letters[i];
                num -= numbers[i];
            }
        }

        if (args[0].charAt(0) == 'r') {
            roman = roman.toLowerCase();
        }

        return new TeXFormula(roman, false).root;
    }

    public static final Atom textcircled_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new TextCircledAtom(new RomanAtom(new TeXFormula(tp, args[1]).root));
    }

    public static final Atom textsc_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SmallCapAtom(new TeXFormula(tp, args[1], false).root);
    }

    public static final Atom sc_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SmallCapAtom(new TeXFormula(tp, tp.getOverArgument(), null, false, tp.isIgnoreWhiteSpace()).root);
    }

    public static final Atom quad_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SpaceAtom(TeXConstants.UNIT_EM, 1f, 0f, 0f);
    }

    public static final Atom qquad_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new SpaceAtom(TeXConstants.UNIT_EM, 2f, 0f, 0f);
    }

    public static final Atom muskip_macros(final TeXParser tp, final String[] args) throws ParseException {
        int type = 0;
        if (args[0].equals(",")) {
            type = TeXConstants.THINMUSKIP;
        } else if (args[0].equals(":")) {
            type = TeXConstants.MEDMUSKIP;
        } else if (args[0].equals(";")) {
            type = TeXConstants.THICKMUSKIP;
        } else if (args[0].equals("thinspace")) {
            type = TeXConstants.THINMUSKIP;
        } else if (args[0].equals("medspace")) {
            type = TeXConstants.MEDMUSKIP;
        } else if (args[0].equals("thickspace")) {
            type = TeXConstants.THICKMUSKIP;
        } else if (args[0].equals("!")) {
            type = TeXConstants.NEGTHINMUSKIP;
        } else if (args[0].equals("negthinspace")) {
            type = TeXConstants.NEGTHINMUSKIP;
        } else if (args[0].equals("negmedspace")) {
            type = TeXConstants.NEGMEDMUSKIP;
        } else if (args[0].equals("negthickspace")) {
            type = TeXConstants.NEGTHICKMUSKIP;
        }

        return new SpaceAtom(type);
    }

    public static final Atom surd_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new VCenteredAtom(SymbolAtom.get("surdsign"));
    }

    public static final Atom int_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("int").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        return integral;
    }

    public static final Atom oint_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("oint").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        return integral;
    }

    public static final Atom iint_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("int").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        RowAtom ra = new RowAtom(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.lookAtLastAtom = true;
        return new TypedAtom(TeXConstants.TYPE_BIG_OPERATOR, TeXConstants.TYPE_BIG_OPERATOR, ra);
    }

    public static final Atom iiint_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("int").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        RowAtom ra = new RowAtom(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.lookAtLastAtom = true;
        return new TypedAtom(TeXConstants.TYPE_BIG_OPERATOR, TeXConstants.TYPE_BIG_OPERATOR, ra);
    }

    public static final Atom iiiint_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("int").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        RowAtom ra = new RowAtom(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -6f, 0f, 0f));
        ra.add(integral);
        ra.lookAtLastAtom = true;
        return new TypedAtom(TeXConstants.TYPE_BIG_OPERATOR, TeXConstants.TYPE_BIG_OPERATOR, ra);
    }

    public static final Atom idotsint_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom integral = SymbolAtom.get("int").clone();
        integral.type_limits = TeXConstants.SCRIPT_NOLIMITS;
        RowAtom ra = new RowAtom(integral);
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -1f, 0f, 0f));
        Atom cdotp = SymbolAtom.get("cdotp");
        RowAtom cdots = new RowAtom(cdotp);
        cdots.add(cdotp);
        cdots.add(cdotp);
        ra.add(new TypedAtom(TeXConstants.TYPE_INNER, TeXConstants.TYPE_INNER, cdots));
        ra.add(new SpaceAtom(TeXConstants.UNIT_MU, -1f, 0f, 0f));
        ra.add(integral);
        ra.lookAtLastAtom = true;
        return new TypedAtom(TeXConstants.TYPE_BIG_OPERATOR, TeXConstants.TYPE_BIG_OPERATOR, ra);
    }

    public static final Atom lmoustache_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new BigDelimiterAtom((SymbolAtom) SymbolAtom.get("lmoustache").clone(), 1);
        at.type = TeXConstants.TYPE_OPENING;
        return at;
    }

    public static final Atom rmoustache_macro(final TeXParser tp, final String[] args) throws ParseException {
        Atom at = new BigDelimiterAtom((SymbolAtom) SymbolAtom.get("rmoustache").clone(), 1);
        at.type = TeXConstants.TYPE_CLOSING;
        return at;
    }

    public static final Atom insertBreakMark_macro(final TeXParser tp, final String[] args) throws ParseException {
        return new BreakMarkAtom();
    }

    public static final Atom jlmXML_macro(final TeXParser tp, final String[] args) throws ParseException {
        Map<String, String> map = tp.formula.jlmXMLMap;
        String str = args[1];
        StringBuffer buffer = new StringBuffer();
        int start = 0;
        int pos;
        while ((pos = str.indexOf("$")) != -1) {
            if (pos < str.length() - 1) {
                start = pos;
                while (++start < str.length() && Character.isLetter(str.charAt(start)));
                String key = str.substring(pos + 1, start);
                String value = map.get(key);
                if (value != null) {
                    buffer.append(str.substring(0, pos));
                    buffer.append(value);
                } else {
                    buffer.append(str.substring(0, start));
                }
                str = str.substring(start);
            } else {
                buffer.append(str);
                str = "";
            }
        }
        buffer.append(str);
        str = buffer.toString();

        return new TeXFormula(tp, str).root;
    }
}
