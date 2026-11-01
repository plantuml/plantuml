/* TeXParser.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/p/jlatexmath
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
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a parser for LaTeX' formulas.
 */
public class TeXParser {

    TeXFormula formula;

    private StringBuffer parseString;
    private int pos;
    private int spos;
    private int line;
    private int col;
    private int len;
    private int group;
    private boolean insertion;
    private int atIsLetter;
    private boolean arrayMode;
    private boolean ignoreWhiteSpace = true;
    private boolean isPartial;

    // the escape character
    private static final char ESCAPE = '\\';

    // grouping characters (for parsing)
    private static final char L_GROUP = '{';
    private static final char R_GROUP = '}';
    private static final char L_BRACK = '[';
    private static final char R_BRACK = ']';
    private static final char DOLLAR = '$';
    private static final char DQUOTE = '\"';

    // Percent char for comments
    private static final char PERCENT = '%';

    // script characters (for parsing)
    private static final char SUB_SCRIPT = '_';
    private static final char SUPER_SCRIPT = '^';
    private static final char PRIME = '\'';
    private static final char BACKPRIME = '\u2035';
    private static final char DEGRE = '\u00B0';
    private static final char SUPZERO = '\u2070';
    private static final char SUPONE = '\u00B9';
    private static final char SUPTWO = '\u00B2';
    private static final char SUPTHREE = '\u00B3';
    private static final char SUPFOUR = '\u2074';
    private static final char SUPFIVE = '\u2075';
    private static final char SUPSIX = '\u2076';
    private static final char SUPSEVEN = '\u2077';
    private static final char SUPEIGHT = '\u2078';
    private static final char SUPNINE = '\u2079';
    private static final char SUPPLUS = '\u207A';
    private static final char SUPMINUS = '\u207B';
    private static final char SUPEQUAL = '\u207C';
    private static final char SUPLPAR = '\u207D';
    private static final char SUPRPAR = '\u207E';
    private static final char SUPN = '\u207F';
    private static final char SUBZERO = '\u2080';
    private static final char SUBONE = '\u2081';
    private static final char SUBTWO = '\u2082';
    private static final char SUBTHREE = '\u2083';
    private static final char SUBFOUR = '\u2084';
    private static final char SUBFIVE = '\u2085';
    private static final char SUBSIX = '\u2086';
    private static final char SUBSEVEN = '\u2087';
    private static final char SUBEIGHT = '\u2088';
    private static final char SUBNINE = '\u2089';
    private static final char SUBPLUS = '\u208A';
    private static final char SUBMINUS = '\u208B';
    private static final char SUBEQUAL = '\u208C';
    private static final char SUBLPAR = '\u208D';
    private static final char SUBRPAR = '\u208E';

    protected static boolean isLoading = false;

    private static final Set<String> unparsedContents = new HashSet<String>(6);
    static {
        unparsedContents.add("jlmDynamic");
        unparsedContents.add("jlmText");
        unparsedContents.add("jlmTextit");
        unparsedContents.add("jlmTextbf");
        unparsedContents.add("jlmTextitbf");
        unparsedContents.add("jlmExternalFont");
    }

    /**
     * Create a new TeXParser
     *
     * @param parseString the string to be parsed
     * @param formula the formula where to put the atoms
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(String parseString, TeXFormula formula) {
        this(parseString, formula, true);
    }

    /**
     * Create a new TeXParser
     *
     * @param isPartial if true certains exceptions are not thrown
     * @param parseString the string to be parsed
     * @param formula the formula where to put the atoms
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(boolean isPartial, String parseString, TeXFormula formula) {
        this(parseString, formula, false);
        this.isPartial = isPartial;
        firstpass();
    }

    /**
     * Create a new TeXParser with or without a first pass
     *
     * @param isPartial if true certains exceptions are not thrown
     * @param parseString the string to be parsed
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(boolean isPartial, String parseString, TeXFormula formula, boolean firstpass) {
        this.formula = formula;
        this.isPartial = isPartial;
        if (parseString != null) {
            this.parseString = new StringBuffer(parseString);
            this.len = parseString.length();
            this.pos = 0;
            if (firstpass) {
                firstpass();
            }
        } else {
            this.parseString = null;
            this.pos = 0;
            this.len = 0;
        }
    }

    /**
     * Create a new TeXParser with or without a first pass
     *
     * @param parseString the string to be parsed
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(String parseString, TeXFormula formula, boolean firstpass) {
        this(false, parseString, formula, firstpass);
    }

    /**
     * Create a new TeXParser in the context of an array. When the parser meets a & a new atom is added in the current line and when a \\ is met, a new line is created.
     *
     * @param isPartial if true certains exceptions are not thrown
     * @param parseString the string to be parsed
     * @param aoa an ArrayOfAtoms where to put the elements
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(boolean isPartial, String parseString, ArrayOfAtoms aoa, boolean firstpass) {
        this(isPartial, parseString, (TeXFormula) aoa, firstpass);
        arrayMode = true;
    }

    /**
     * Create a new TeXParser in the context of an array. When the parser meets a & a new atom is added in the current line and when a \\ is met, a new line is created.
     *
     * @param isPartial if true certains exceptions are not thrown
     * @param parseString the string to be parsed
     * @param aoa an ArrayOfAtoms where to put the elements
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(boolean isPartial, String parseString, ArrayOfAtoms aoa, boolean firstpass, boolean space) {
        this(isPartial, parseString, (TeXFormula) aoa, firstpass, space);
        arrayMode = true;
    }

    /**
     * Create a new TeXParser in the context of an array. When the parser meets a & a new atom is added in the current line and when a \\ is met, a new line is created.
     *
     * @param parseString the string to be parsed
     * @param aoa an ArrayOfAtoms where to put the elements
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(String parseString, ArrayOfAtoms aoa, boolean firstpass) {
        this(false, parseString, (TeXFormula) aoa, firstpass);
    }

    /**
     * Create a new TeXParser which ignores or not the white spaces, it's useful for mbox command
     *
     * @param isPartial if true certains exceptions are not thrown
     * @param parseString the string to be parsed
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @param space a boolean to indicate if the parser must ignore or not the white space
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(boolean isPartial, String parseString, TeXFormula formula, boolean firstpass, boolean space) {
        this(isPartial, parseString, formula, firstpass);
        this.ignoreWhiteSpace = space;
    }

    /**
     * Create a new TeXParser which ignores or not the white spaces, it's useful for mbox command
     *
     * @param parseString the string to be parsed
     * @param firstpass a boolean to indicate if the parser must replace the user-defined macros by their content
     * @param space a boolean to indicate if the parser must ignore or not the white space
     * @throws ParseException if the string could not be parsed correctly
     */
    public TeXParser(String parseString, TeXFormula formula, boolean firstpass, boolean space) {
        this(false, parseString, formula, firstpass);
        this.ignoreWhiteSpace = space;
    }

    /**
     * Reset the parser with a new latex expression
     */
    public void reset(String latex) {
        parseString = new StringBuffer(latex);
        len = parseString.length();
        formula.root = null;
        pos = 0;
        spos = 0;
        line = 0;
        col = 0;
        group = 0;
        insertion = false;
        atIsLetter = 0;
        arrayMode = false;
        ignoreWhiteSpace = true;
        firstpass();
    }

    /** Return true if we get a partial formula
     */
    public boolean getIsPartial() {
        return isPartial;
    }

    /** Get the number of the current line
     */
    public int getLine() {
        return line;
    }

    /** Get the number of the current column
     */
    public int getCol() {
        return pos - col - 1;
    }

    /** Get the last atom of the current formula
     */
    public Atom getLastAtom() {
        Atom at = formula.root;
        if (at instanceof RowAtom)
            return ((RowAtom)at).getLastAtom();
        formula.root = null;
        return at;
    }

    /** Get the atom represented by the current formula
     */
    public Atom getFormulaAtom() {
        Atom at = formula.root;
        formula.root = null;
        return at;
    }

    /** Put an atom in the current formula
     */
    public void addAtom(Atom at) {
        formula.add(at);
    }

    /** Indicate if the character @ can be used in the command's name
     */
    public void makeAtLetter() {
        atIsLetter++;
    }

    /** Indicate if the character @ can be used in the command's name
     */
    public void makeAtOther() {
        atIsLetter--;
    }

    /** Return a boolean indicating if the character @ is considered as a letter or not
     */
    public boolean isAtLetter() {
        return (atIsLetter != 0);
    }

    /** Return a boolean indicating if the parser is used to parse an array or not
     */
    public boolean isArrayMode() {
        return arrayMode;
    }

    public void setArrayMode(boolean arrayMode) {
        this.arrayMode = arrayMode;
    }

    /** Return a boolean indicating if the parser must ignore white spaces
     */
    public boolean isIgnoreWhiteSpace() {
        return ignoreWhiteSpace;
    }

    /** Return a boolean indicating if the parser is in math mode
     */
    public boolean isMathMode() {
        return ignoreWhiteSpace;
    }

    /** Return the current position in the parsed string
     */
    public int getPos() {
        return pos;
    }

    /** Rewind the current parsed string
     * @param n the number of character to be rewinded
     * @return the new position in the parsed string
     */
    public int rewind(int n) {
        pos -= n;
        return pos;
    }

    public String getStringFromCurrentPos() {
        return parseString.substring(pos);
    }

    public void finish() {
        pos = parseString.length();
    }

    /** Add a new row when the parser is in array mode
     * @throws ParseException if the parser is not in array mode
     */
    public void addRow() throws ParseException {
        if (!arrayMode)
            throw new ParseException("You can add a row only in array mode !");
        ((ArrayOfAtoms)formula).addRow();
    }

    private void firstpass() throws ParseException {
        if (len != 0) {
            char ch;
            String com;
            int spos;
            String[] args;
            MacroInfo mac;
            while (pos < len) {
                ch = parseString.charAt(pos);
                switch (ch) {
                case ESCAPE :
                    spos = pos;
                    com = getCommand();
                    if ("newcommand".equals(com) || "renewcommand".equals(com)) {
                        args = getOptsArgs(2, 2);
                        mac = MacroInfo.Commands.get(com);
                        try {
                            mac.invoke(this, args);
                        } catch (ParseException e) {
                            if (!isPartial) {
                                throw e;
                            }
                        }
                        parseString.delete(spos, pos);
                        len = parseString.length();
                        pos = spos;
                    } else if (NewCommandMacro.isMacro(com)) {
                        mac = MacroInfo.Commands.get(com);
                        args = getOptsArgs(mac.nbArgs, mac.hasOptions ? 1 : 0);
                        args[0] = com;
                        try {
                            parseString.replace(spos, pos, (String) mac.invoke(this, args));
                        } catch (ParseException e) {
                            if (!isPartial) {
                                throw e;
                            } else {
                                spos += com.length() + 1;
                            }
                        }
                        len = parseString.length();
                        pos = spos;
                    } else if ("begin".equals(com)) {
                        args = getOptsArgs(1, 0);
                        mac = MacroInfo.Commands.get(args[1] + "@env");
                        if (mac == null) {
                            if (!isPartial) {
                                throw new ParseException("Unknown environment: " + args[1] + " at position " + getLine() + ":" + getCol());
                            }
                        } else {
                            try {
                                String[] optarg = getOptsArgs(mac.nbArgs - 1, 0);
                                String grp = getGroup("\\begin{" + args[1] + "}", "\\end{" + args[1] + "}");
                                String expr = "{\\makeatletter \\" + args[1] + "@env";
                                for (int i = 1; i <= mac.nbArgs - 1; i++)
                                    expr += "{" + optarg[i] + "}";
                                expr += "{" + grp  + "}\\makeatother}";
                                parseString.replace(spos, pos, expr);
                                len = parseString.length();
                                pos = spos;
                            } catch (ParseException e) {
                                if (!isPartial) {
                                    throw e;
                                }
                            }
                        }
                    } else if ("makeatletter".equals(com))
                        atIsLetter++;
                    else if ("makeatother".equals(com))
                        atIsLetter--;
                    else if (unparsedContents.contains(com)) {
                        getOptsArgs(1, 0);
                    }
                    break;
                case PERCENT :
                    spos = pos++;
                    char chr;
                    while (pos < len) {
                        chr = parseString.charAt(pos++);
                        if (chr == '\r' || chr == '\n') {
                            break;
                        }
                    }
                    if (pos < len) {
                        pos--;
                    }
                    parseString.replace(spos, pos, "");
                    len = parseString.length();
                    pos = spos;
                    break;
                case DEGRE :
                    parseString.replace(pos, pos + 1, "^{\\circ}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPTWO :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{2}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPTHREE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{3}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPONE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{1}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPZERO :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{0}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPFOUR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{4}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPFIVE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{5}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPSIX :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{6}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPSEVEN :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{7}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPEIGHT :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{8}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPNINE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{9}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPPLUS :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{+}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPMINUS :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{-}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPEQUAL :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{=}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPLPAR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{(}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPRPAR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{)}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUPN :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsup{n}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBTWO :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{2}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBTHREE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{3}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBONE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{1}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBZERO :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{0}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBFOUR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{4}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBFIVE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{5}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBSIX :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{6}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBSEVEN :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{7}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBEIGHT :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{8}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBNINE :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{9}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBPLUS :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{+}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBMINUS :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{-}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBEQUAL :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{=}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBLPAR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{(}");
                    len = parseString.length();
                    pos++;
                    break;
                case SUBRPAR :
                    parseString.replace(pos, pos + 1, "\\jlatexmathcumsub{)}");
                    len = parseString.length();
                    pos++;
                    break;
                default :
                    pos++;
                }
            }
            pos = 0;
            len = parseString.length();
        }
    }

    /** Parse the input string
     * @throws ParseException if an error is encountered during parsing
     */
    public void parse() throws ParseException {
        if (len != 0) {
            char ch;
            while (pos < len) {
                ch = parseString.charAt(pos);

                switch (ch) {
                case '\n' :
                    line++;
                    col = pos;
                case '\t' :
                case '\r' :
                    pos++;
                    break;
                case ' ' :
                    pos++;
                    if (!ignoreWhiteSpace) {// We are in a mbox
                        formula.add(new SpaceAtom());
                        formula.add(new BreakMarkAtom());
                        while (pos < len) {
                            ch = parseString.charAt(pos);
                            if (ch != ' ' || ch != '\t' || ch != '\r')
                                break;
                            pos++;
                        }
                    }
                    break;
                case DOLLAR :
                    pos++;
                    if (!ignoreWhiteSpace) {// We are in a mbox
                        int style = TeXConstants.STYLE_TEXT;
                        boolean doubleDollar = false;
                        if (parseString.charAt(pos) == DOLLAR) {
                            style = TeXConstants.STYLE_DISPLAY;
                            doubleDollar = true;
                            pos++;
                        }

                        formula.add(new MathAtom(new TeXFormula(this, getDollarGroup(DOLLAR), false).root, style));
                        if (doubleDollar) {
                            if (parseString.charAt(pos) == DOLLAR) {
                                pos++;
                            }
                        }
                    }
                    break;
                case ESCAPE :
                    Atom at = processEscape();
                    formula.add(at);
                    if (arrayMode && at instanceof HlineAtom) {
                        ((ArrayOfAtoms) formula).addRow();
                    }
                    if (insertion) {
                        insertion = false;
                    }
                    break;
                case L_GROUP :
                    Atom atom = getArgument();
                    if (atom != null) {
                        atom.type = TeXConstants.TYPE_ORDINARY;
                    }
                    formula.add(atom);
                    break;
                case R_GROUP :
                    group--;
                    pos++;
                    if (group == -1)
                        throw new ParseException("Found a closing '" + R_GROUP + "' without an opening '" + L_GROUP + "'!");
                    return;
                case SUPER_SCRIPT :
                    formula.add(getScripts(ch));
                    break;
                case SUB_SCRIPT :
                    if (ignoreWhiteSpace) {
                        formula.add(getScripts(ch));
                    } else {
                        formula.add(new UnderscoreAtom());
                        pos++;
                    }
                    break;
                case '&' :
                    if (!arrayMode)
                        throw new ParseException("Character '&' is only available in array mode !");
                    ((ArrayOfAtoms) formula).addCol();
                    pos++;
                    break;
                case '~' :
                    formula.add(new SpaceAtom());
                    pos++;
                    break;
                case PRIME :
                    if (ignoreWhiteSpace) {
                        formula.add(new CumulativeScriptsAtom(getLastAtom(), null, SymbolAtom.get("prime")));
                    } else {
                        formula.add(convertCharacter(PRIME, true));
                    }
                    pos++;
                    break;
                case BACKPRIME :
                    if (ignoreWhiteSpace) {
                        formula.add(new CumulativeScriptsAtom(getLastAtom(), null, SymbolAtom.get("backprime")));
                    } else {
                        formula.add(convertCharacter(BACKPRIME, true));
                    }
                    pos++;
                    break;
                case DQUOTE :
                    if (ignoreWhiteSpace) {
                        formula.add(new CumulativeScriptsAtom(getLastAtom(), null, SymbolAtom.get("prime")));
                        formula.add(new CumulativeScriptsAtom(getLastAtom(), null, SymbolAtom.get("prime")));
                    } else {
                        formula.add(convertCharacter(PRIME, true));
                        formula.add(convertCharacter(PRIME, true));
                    }
                    pos++;
                    break;
                default :
                    formula.add(convertCharacter(ch, false));
                    pos++;
                }
            }
        }

        if (formula.root == null && !arrayMode) {
            formula.add(new EmptyAtom());
        }
    }

    private Atom getScripts(char f) throws ParseException {
        pos++;
        Atom first = getArgument();
        Atom second = null;
        char s = '\0';

        if (pos < len)
            s = parseString.charAt(pos);

        if (f == SUPER_SCRIPT && s == SUPER_SCRIPT) {
            second = first;
            first = null;
        } else if (f == SUB_SCRIPT && s == SUPER_SCRIPT) {
            pos++;
            second = getArgument();
        } else if (f == SUPER_SCRIPT && s == SUB_SCRIPT) {
            pos++;
            second = first;
            first = getArgument();
        } else if (f == SUPER_SCRIPT && s != SUB_SCRIPT) {
            second = first;
            first = null;
        }

        Atom at;
        if (formula.root instanceof RowAtom) {
            at = ((RowAtom)formula.root).getLastAtom();
        } else if (formula.root == null) {
            at = new PhantomAtom(new CharAtom('M', "mathnormal"), false, true, true);
        } else {
            at = formula.root;
            formula.root = null;
        }

        if (at.getRightType() == TeXConstants.TYPE_BIG_OPERATOR)
            return new BigOperatorAtom(at, first, second);
        else if (at instanceof OverUnderDelimiter) {
            if (((OverUnderDelimiter)at).isOver()) {
                if (second != null) {
                    ((OverUnderDelimiter)at).addScript(second);
                    return new ScriptsAtom(at, first, null);
                }
            } else if (first != null) {
                ((OverUnderDelimiter)at).addScript(first);
                return new ScriptsAtom(at, null, second);
            }
        }

        return new ScriptsAtom(at, first, second);
    }

    /** Get the contents between two delimiters
     * @param openclose the opening and closing character (such $)
     * @return the enclosed contents
     * @throws ParseException if the contents are badly enclosed
     */
    public String getDollarGroup(char openclose) throws ParseException {
        int spos = pos;
        char ch;

        do {
            ch = parseString.charAt(pos++);
            if (ch == ESCAPE) {
                pos++;
            }
        } while (pos < len && ch != openclose);

        if (ch == openclose) {
            return parseString.substring(spos, pos - 1);
        } else {
            return parseString.substring(spos, pos);
        }
    }

    /** Get the contents between two delimiters
     * @param open the opening character
     * @param close the closing character
     * @return the enclosed contents
     * @throws ParseException if the contents are badly enclosed
     */
    public String getGroup(char open, char close) throws ParseException {
        if (pos == len)
            return null;

        int group, spos;
        char ch = parseString.charAt(pos);

        if (pos < len && ch == open) {
            group = 1;
            spos = pos;
            while (pos < len - 1 && group != 0) {
                pos++;
                ch = parseString.charAt(pos);
                if (ch == open)
                    group++;
                else if (ch == close)
                    group--;
                else if (ch == ESCAPE && pos != len - 1)
                    pos++;
            }

            pos++;

            if (group != 0) {
                return parseString.substring(spos + 1, pos);
            }

            return parseString.substring(spos + 1, pos - 1);
        } else {
            throw new ParseException("missing '" + open + "'!");
        }
    }

    /** Get the contents between two strings as in \begin{foo}...\end{foo}
     * @param open the opening string
     * @param close the closing string
     * @return the enclosed contents
     * @throws ParseException if the contents are badly enclosed
     */
    public String getGroup(String open, String close) throws ParseException {
        int group = 1;
        int ol = open.length(), cl = close.length();
        boolean lastO = isValidCharacterInCommand(open.charAt(ol - 1));
        boolean lastC = isValidCharacterInCommand(close.charAt(cl - 1));
        int oc = 0, cc = 0;
        int startC = 0;
        char prev = '\0';
        StringBuffer buf = new StringBuffer();

        while (pos < len && group != 0) {
            char c = parseString.charAt(pos);
            char c1;

            if (prev != ESCAPE && c == ' ') {//Trick to handle case where close == "\end   {foo}"
                while (pos < len && parseString.charAt(pos++) == ' ') {
                    buf.append(' ');
                }
                c = parseString.charAt(--pos);
                if (isValidCharacterInCommand(prev) && isValidCharacterInCommand(c)) {
                    oc = cc = 0;
                }
            }

            if (c == open.charAt(oc))
                oc++;
            else
                oc = 0;

            if (c == close.charAt(cc)) {
                if (cc == 0) {
                    startC = pos;
                }
                cc++;
            } else
                cc = 0;

            if (pos + 1 < len) {
                c1 = parseString.charAt(pos + 1);

                if (oc == ol) {
                    if (!lastO || !isValidCharacterInCommand(c1)) {
                        group++;
                    }
                    oc = 0;
                }

                if (cc == cl) {
                    if (!lastC || !isValidCharacterInCommand(c1)) {
                        group--;
                    }
                    cc = 0;
                }
            } else {
                if (oc == ol) {
                    group++;
                    oc = 0;
                }
                if (cc == cl) {
                    group--;
                    cc = 0;
                }
            }

            prev = c;
            buf.append(c);
            pos++;
        }

        if (group != 0) {
            if (isPartial) {
                return buf.toString();
            }
            throw new ParseException("The token " + open + " must be closed by " + close);
        }

        return buf.substring(0, buf.length() - pos + startC);
    }

    /** Get the argument of a command in his atomic format
     * @return the corresponding atom
     * @throws ParseException if the argument is incorrect
     */
    public Atom getArgument() throws ParseException {
        skipWhiteSpace();
        char ch;
        if (pos < len) {
            ch = parseString.charAt(pos);
        } else {
            return new EmptyAtom();
        }
        if (ch == L_GROUP) {
            TeXFormula tf = new TeXFormula();
            TeXFormula sformula = this.formula;
            this.formula = tf;
            pos++;
            group++;
            parse();
            this.formula = sformula;
            if (this.formula.root == null) {
                RowAtom at = new RowAtom();
                at.add(tf.root);
                return at;
            }
            return tf.root;
        }

        if (ch == ESCAPE) {
            Atom at = processEscape();
            if (insertion) {
                insertion = false;
                return getArgument();
            }
            return at;
        }

        Atom at = convertCharacter(ch, true);
        pos++;
        return at;
    }

    public String getOverArgument() throws ParseException {
        if (pos == len)
            return null;

        int ogroup = 1, spos;
        char ch = '\0';

        spos = pos;
        while (pos < len && ogroup != 0) {
            ch = parseString.charAt(pos);
            switch (ch) {
            case L_GROUP :
                ogroup++;
                break;
            case '&' :
                /* if a & is encountered at the same level as \over
                   we must break the argument */
                if (ogroup == 1) {
                    ogroup--;
                }
                break;
            case R_GROUP :
                ogroup--;
                break;
            case ESCAPE :
                pos++;
                /* if a \\ or a \cr is encountered at the same level as \over
                   we must break the argument */
                if (pos < len && parseString.charAt(pos) == '\\' && ogroup == 1) {
                    ogroup--;
                    pos--;
                } else if (pos < len - 1 && parseString.charAt(pos) == 'c' && parseString.charAt(pos + 1) == 'r' && ogroup == 1) {
                    ogroup--;
                    pos--;
                }
                break;
            }
            pos++;
        }

        if (ogroup >= 2)
            // end of string reached, but not processed properly
            throw new ParseException("Illegal end,  missing '}' !");

        String str;
        if (ogroup == 0) {
            str = parseString.substring(spos, pos - 1);
        } else {
            str = parseString.substring(spos, pos);
            ch = '\0';
        }

        if (ch == '&' || ch == '\\' || ch == R_GROUP)
            pos--;

        return str;
    }

    public float[] getLength() throws ParseException {
        if (pos == len)
            return null;

        int spos;
        char ch = '\0';

        skipWhiteSpace();
        spos = pos;
        while (pos < len && ch != ' ') {
            ch = parseString.charAt(pos++);
        }
        skipWhiteSpace();

        return SpaceAtom.getLength(parseString.substring(spos, pos - 1));
    }

    /** Convert a character in the corresponding atom in using the file TeXFormulaSettings.xml for non-alphanumeric characters
     * @param c the character to be converted
     * @return the corresponding atom
     * @throws ParseException if the character is unknown
     */
    public Atom convertCharacter(char c, boolean oneChar) throws ParseException {
        if (ignoreWhiteSpace) {// The Unicode Greek letters in math mode are not drawn with the Greek font
            if (c >= 945 && c <= 969) {
                return SymbolAtom.get(TeXFormula.symbolMappings[c]);
            } else if (c >= 913 && c <= 937) {
                return new TeXFormula(TeXFormula.symbolFormulaMappings[c]).root;
            }
        }

        c = convertToRomanNumber(c);
        if (((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
            if (!isLoading && !DefaultTeXFont.loadedAlphabets.contains(block)) {
                DefaultTeXFont.addAlphabet(DefaultTeXFont.registeredAlphabets.get(block));
            }

            String symbolName = TeXFormula.symbolMappings[c];
            if (symbolName == null && (TeXFormula.symbolFormulaMappings == null || TeXFormula.symbolFormulaMappings[c] == null)) {
                TeXFormula.FontInfos fontInfos = null;
                boolean isLatin = Character.UnicodeBlock.BASIC_LATIN.equals(block);
                if ((isLatin && TeXFormula.isRegisteredBlock(Character.UnicodeBlock.BASIC_LATIN)) || !isLatin) {
                    fontInfos = TeXFormula.getExternalFont(block);
                }
                if (fontInfos != null) {
                    if (oneChar) {
                        return new JavaFontRenderingAtom(Character.toString(c), fontInfos);
                    }
                    int start = pos++;
                    int end = len - 1;
                    while (pos < len) {
                        c = parseString.charAt(pos);
                        if (!Character.UnicodeBlock.of(c).equals(block)) {
                            end = --pos;
                            break;
                        }
                        pos++;
                    }
                    return new JavaFontRenderingAtom(parseString.substring(start, end + 1), fontInfos);
                }

                if (!isPartial) {
                    throw new ParseException("Unknown character : '"
                                             + Character.toString(c) + "' (or " + ((int) c) + ")");
                } else {
                    return new ColorAtom(new RomanAtom(new TeXFormula("\\text{(Unknown char " + ((int) c) + ")}").root), null, Color.RED);
                }
            } else {
                if (!ignoreWhiteSpace) {// we are in text mode
                    if (TeXFormula.symbolTextMappings[c] != null) {
                        return SymbolAtom.get(TeXFormula.symbolTextMappings[c]).setUnicode(c);
                    }
                }
                if (TeXFormula.symbolFormulaMappings != null && TeXFormula.symbolFormulaMappings[c] != null) {
                    return new TeXFormula(TeXFormula.symbolFormulaMappings[c]).root;
                }

                try {
                    return SymbolAtom.get(symbolName);
                } catch (SymbolNotFoundException e) {
                    throw new ParseException("The character '"
                                             + Character.toString(c)
                                             + "' was mapped to an unknown symbol with the name '"
                                             + symbolName + "'!", e);
                }
            }
        } else {
            // alphanumeric character
            TeXFormula.FontInfos fontInfos = TeXFormula.externalFontMap.get(Character.UnicodeBlock.BASIC_LATIN);
            if (fontInfos != null) {
                if (oneChar) {
                    return new JavaFontRenderingAtom(Character.toString(c), fontInfos);
                }
                int start = pos++;
                int end = len - 1;
                while (pos < len) {
                    c = parseString.charAt(pos);
                    if (((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))) {
                        end = --pos;
                        break;
                    }
                    pos++;
                }
                return new JavaFontRenderingAtom(parseString.substring(start, end + 1), fontInfos);
            }

            return new CharAtom(c, formula.textStyle, ignoreWhiteSpace);
        }
    }

    private String getCommand() {
        int spos = ++pos;
        char ch = '\0';

        while (pos < len) {
            ch = parseString.charAt(pos);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (atIsLetter == 0 || ch != '@'))
                break;

            pos++;
        }

        if (ch == '\0')
            return "";

        if (pos == spos) {
            pos++;
        }

        String com = parseString.substring(spos, pos);
        if ("cr".equals(com) && pos < len && parseString.charAt(pos) == ' ') {
            pos++;
        }

        return com;
    }

    private Atom processEscape() throws ParseException {
        spos = pos;
        String command = getCommand();

        if (command.length() == 0) {
            return new EmptyAtom();
        }

        if (MacroInfo.Commands.get(command) != null)
            return processCommands(command);

        try {
            return TeXFormula.get(command).root;
        } catch (FormulaNotFoundException e) {
            try {
                return SymbolAtom.get(command);
            } catch (SymbolNotFoundException e1) { }
        }

        // not a valid command or symbol or predefined TeXFormula found
        if (!isPartial) {
            throw new ParseException("Unknown symbol or command or predefined TeXFormula: '" + command + "'");
        } else {
            return new ColorAtom(new RomanAtom(new TeXFormula("\\backslash " + command).root), null, Color.RED);
        }
    }

    private void insert(int beg, int end, String formula) {
        parseString.replace(beg, end, formula);
        len = parseString.length();
        pos = beg;
        insertion = true;
    }

    /** Get the arguments ant the options of a command
     * @param nbArgs the number of arguments of the command
     * @param opts must be 1 if the options are found before the first argument and must be 2 if they must be found before the second argument
     * @return an array containing arguments and at the end the options are put
     */
    /* Should be improved */
    public String[] getOptsArgs(int nbArgs, int opts) {
        //A maximum of 10 options can be passed to a command
        String[] args = new String[nbArgs + 10 + 1];
        if (nbArgs != 0) {

            //We get the options just after the command name
            if (opts == 1) {
                int j = nbArgs + 1;
                try {
                    for (; j < nbArgs + 11; j++) {
                        skipWhiteSpace();
                        args[j] = getGroup(L_BRACK, R_BRACK);
                    }
                } catch (ParseException e) {
                    args[j] = null;
                }
            }

            //We get the first argument
            skipWhiteSpace();
            try {
                args[1] = getGroup(L_GROUP, R_GROUP);
            } catch (ParseException e) {
                if (parseString.charAt(pos) != '\\') {
                    args[1] = "" + parseString.charAt(pos);
                    pos++;
                } else
                    args[1] = getCommandWithArgs(getCommand());
            }

            //We get the options after the first argument
            if (opts == 2) {
                int j = nbArgs + 1;
                try {
                    for (; j < nbArgs + 11; j++) {
                        skipWhiteSpace();
                        args[j] = getGroup(L_BRACK, R_BRACK);
                    }
                } catch (ParseException e) {
                    args[j] = null;
                }
            }

            //We get the next arguments
            for (int i = 2; i <= nbArgs; i++) {
                skipWhiteSpace();
                try {
                    args[i] = getGroup(L_GROUP, R_GROUP);
                } catch (ParseException e) {
                    if (parseString.charAt(pos) != '\\') {
                        args[i] = "" + parseString.charAt(pos);
                        pos++;
                    } else {
                        args[i] = getCommandWithArgs(getCommand());
                    }
                }
            }

            if (ignoreWhiteSpace) {
                skipWhiteSpace();
            }
        }
        return args;
    }

    /**
     * return a string with command and options and args
     * @param command name of command
     * @return
     * @author Juan Enrique Escobar Robles
     */
    private String getCommandWithArgs(String command) {
        if (command.equals("left")) {
            return getGroup("\\left", "\\right");
        }

        MacroInfo mac = MacroInfo.Commands.get(command);
        if (mac != null) {
            int mac_opts = 0;
            if (mac.hasOptions) {
                mac_opts = mac.posOpts;
            }

            String[] mac_args = getOptsArgs(mac.nbArgs, mac_opts);
            StringBuffer mac_arg = new StringBuffer("\\");
            mac_arg.append(command);
            for (int j = 0; j < mac.posOpts; j++) {
                String arg_t = mac_args[mac.nbArgs + j + 1];
                if (arg_t != null) {
                    mac_arg.append("[").append(arg_t).append("]");
                }
            }

            for (int j = 0; j < mac.nbArgs; j++) {
                String arg_t = mac_args[j + 1];
                if (arg_t != null) {
                    mac_arg.append("{").append(arg_t).append("}");
                }
            }

            return mac_arg.toString();
        }

        return "\\" + command;
    }

    /**
     * Processes the given TeX command (by parsing following command arguments
     * in the parse string).
     */
    private Atom processCommands(String command) throws ParseException {
        MacroInfo mac = MacroInfo.Commands.get(command);
        int opts = 0;
        if (mac.hasOptions)
            opts = mac.posOpts;

        String[] args = getOptsArgs(mac.nbArgs, opts);
        args[0] = command;

        if (NewCommandMacro.isMacro(command)) {
            String ret = (String) mac.invoke(this, args);
            insert(spos, pos, ret);
            return null;
        }

        return (Atom) mac.invoke(this, args);
    }

    /** Test the validity of the name of a command. It must contains only alpha characters and eventually a @ if makeAtletter activated
     * @param com the command's name
     * @return the validity of the name
     */
    public final boolean isValidName(String com) {
        if (com == null || "".equals(com)) {
            return false;
        }

        char c = '\0';
        if (com.charAt(0) == '\\') {
            int pos = 1;
            int len = com.length();
            while (pos < len) {
                c = com.charAt(pos);
                if (!Character.isLetter(c) && (atIsLetter == 0 || c != '@'))
                    break;
                pos++;
            }
        } else {
            return false;
        }

        return Character.isLetter(c);
    }

    /** Test the validity of a character in a command. It must contains only alpha characters and eventually a @ if makeAtletter activated
     * @param com the command's name
     * @return the validity of the name
     */
    public final boolean isValidCharacterInCommand(char ch) {
        return Character.isLetter(ch) || (atIsLetter != 0 && ch == '@');
    }

    private final void skipWhiteSpace() {
        char c;
        while (pos < len) {
            c = parseString.charAt(pos);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
                break;
            if (c == '\n') {
                line++;
                col = pos;
            }
            pos++;
        }
    }

    /**
     * The aim of this method is to convert foreign number into roman ones !
     */
    private static char convertToRomanNumber(char c) {
        if (c == 0x66b) {//Arabic dot
            return '.';
        } else if (0x660 <= c && c <= 0x669) {//Arabic
            return (char) (c - (char) 0x630);
        } else if (0x6f0 <= c && c <= 0x6f9) {//Arabic
            return (char) (c - (char) 0x6c0);
        } else if (0x966 <= c && c <= 0x96f) {//Devanagari
            return (char) (c - (char) 0x936);
        } else if (0x9e6 <= c && c <= 0x9ef) {//Bengali
            return (char) (c - (char) 0x9b6);
        } else if (0xa66 <= c && c <= 0xa6f) {//Gurmukhi
            return (char) (c - (char) 0xa36);
        } else if (0xae6 <= c && c <= 0xaef) {//Gujarati
            return (char) (c - (char) 0xab6);
        } else if (0xb66 <= c && c <= 0xb6f) {//Oriya
            return (char) (c - (char) 0xb36);
        } else if (0xc66 <= c && c <= 0xc6f) {//Telugu
            return (char) (c - (char) 0xc36);
        } else if (0xd66 <= c && c <= 0xd6f) {//Malayalam
            return (char) (c - (char) 0xd36);
        } else if (0xe50 <= c && c <= 0xe59) {//Thai
            return (char) (c - (char) 0xe20);
        } else if (0xed0 <= c && c <= 0xed9) {//Lao
            return (char) (c - (char) 0xea0);
        } else if (0xf20 <= c && c <= 0xf29) {//Tibetan
            return (char) (c - (char) 0xe90);
        } else if (0x1040 <= c && c <= 0x1049) {//Myanmar
            return (char) (c - (char) 0x1010);
        } else if (0x17e0 <= c && c <= 0x17e9) {//Khmer
            return (char) (c - (char) 0x17b0);
        } else if (0x1810 <= c && c <= 0x1819) {//Mongolian
            return (char) (c - (char) 0x17e0);
        } else if (0x1b50 <= c && c <= 0x1b59) {//Balinese
            return (char) (c - (char) 0x1b20);
        } else if (0x1bb0 <= c && c <= 0x1bb9) {//Sundanese
            return (char) (c - (char) 0x1b80);
        } else if (0x1c40 <= c && c <= 0x1c49) {//Lepcha
            return (char) (c - (char) 0x1c10);
        } else if (0x1c50 <= c && c <= 0x1c59) {//Ol Chiki
            return (char) (c - (char) 0x1c20);
        } else if (0xa8d0 <= c && c <= 0xa8d9) {//Saurashtra
            return (char) (c - (char) 0xa8a0);
        }

        return c;
    }
}
