/*
 * $Id: SpecialSymbol.java 3963 2009-06-11 11:45:49Z psoares33 $
 *
 * Copyright 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * https://github.com/LibrePDF/OpenPDF
 */

package org.openpdf.text;

/**
 * This class contains the symbols that correspond with special symbols.
 * <p>
 * When you construct a <CODE>Phrase</CODE> with Phrase.getInstance using a <CODE>String</CODE>, this
 * <CODE>String</CODE> can contain special Symbols. These are characters with an int value between 913 and 937 (except
 * 930) and between 945 and 969. With this class the value of the corresponding character of the Font Symbol, can be
 * retrieved.
 *
 * @author Bruno Lowagie
 * @author Evelyne De Cordier
 * @see Phrase
 */

public class SpecialSymbol {

    /**
     * Returns the first occurrence of a special symbol in a <CODE>String</CODE>.
     *
     * @param string a <CODE>String</CODE>
     * @return an index of -1 if no special symbol was found
     */
    public static int index(String string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (getCorrespondingSymbol(string.charAt(i)) != ' ') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets a chunk with a symbol character.
     *
     * @param c    a character that has to be changed into a symbol
     * @param font Font if there is no SYMBOL character corresponding with c
     * @return a SYMBOL version of a character
     */
    public static Chunk get(char c, Font font) {
        char greek = SpecialSymbol.getCorrespondingSymbol(c);
        if (greek == ' ') {
            return new Chunk(String.valueOf(c), font);
        }
        Font symbol = new Font(Font.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
        String s = String.valueOf(greek);
        return new Chunk(s, symbol);
    }

    /**
     * Looks for the corresponding symbol in the font Symbol.
     *
     * @param c the original ASCII-char
     * @return the corresponding symbol in font Symbol
     */
    public static char getCorrespondingSymbol(char c) {
        switch (c) {
            case 913:
                return 'A'; // ALFA
            case 914:
                return 'B'; // BETA
            case 915:
                return 'G'; // GAMMA
            case 916:
                return 'D'; // DELTA
            case 917:
                return 'E'; // EPSILON
            case 918:
                return 'Z'; // ZETA
            case 919:
                return 'H'; // ETA
            case 920:
                return 'Q'; // THETA
            case 921:
                return 'I'; // IOTA
            case 922:
                return 'K'; // KAPPA
            case 923:
                return 'L'; // LAMBDA
            case 924:
                return 'M'; // MU
            case 925:
                return 'N'; // NU
            case 926:
                return 'X'; // XI
            case 927:
                return 'O'; // OMICRON
            case 928:
                return 'P'; // PI
            case 929:
                return 'R'; // RHO
            case 931:
                return 'S'; // SIGMA
            case 932:
                return 'T'; // TAU
            case 933:
                return 'U'; // UPSILON
            case 934:
                return 'F'; // PHI
            case 935:
                return 'C'; // CHI
            case 936:
                return 'Y'; // PSI
            case 937:
                return 'W'; // OMEGA
            case 945:
                return 'a'; // alfa
            case 946:
                return 'b'; // beta
            case 947:
                return 'g'; // gamma
            case 948:
                return 'd'; // delta
            case 949:
                return 'e'; // epsilon
            case 950:
                return 'z'; // zeta
            case 951:
                return 'h'; // eta
            case 952:
                return 'q'; // theta
            case 953:
                return 'i'; // iota
            case 954:
                return 'k'; // kappa
            case 955:
                return 'l'; // lambda
            case 956:
                return 'm'; // mu
            case 957:
                return 'n'; // nu
            case 958:
                return 'x'; // xi
            case 959:
                return 'o'; // omicron
            case 960:
                return 'p'; // pi
            case 961:
                return 'r'; // rho
            case 962:
                return 'V'; // sigma
            case 963:
                return 's'; // sigma
            case 964:
                return 't'; // tau
            case 965:
                return 'u'; // upsilon
            case 966:
                return 'f'; // phi
            case 967:
                return 'c'; // chi
            case 968:
                return 'y'; // psi
            case 969:
                return 'w'; // omega
            default:
                return ' ';
        }
    }
}