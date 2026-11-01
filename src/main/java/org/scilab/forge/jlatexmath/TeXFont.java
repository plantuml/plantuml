/* TeXFont.java
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
 * An interface representing a "TeXFont", which is responsible for all the necessary
 * fonts and font information.
 *
 * @author Kurt Vermeulen
 */
public interface TeXFont {

    public static final int NO_FONT = -1;

    /**
     * Derives a new {@link TeXFont} object with the given point size
     *
     * @param pointSize the new size (in points) of the derived {@link TeXFont}
     * @return a <b>copy</b> of this {@link TeXFont} with the new size
     */
    public TeXFont deriveFont(float pointSize);

    public TeXFont scaleFont(float factor);

    public float getScaleFactor();

    public float getAxisHeight(int style);

    public float getBigOpSpacing1(int style);

    public float getBigOpSpacing2(int style);

    public float getBigOpSpacing3(int style);

    public float getBigOpSpacing4(int style);

    public float getBigOpSpacing5(int style);

    /**
     * Get a Char-object specifying the given character in the given text style with
     * metric information depending on the given "style".
     *
     * @param c alphanumeric character
     * @param textStyle the text style in which the character should be drawn
     * @param style the style in which the atom should be drawn
     * @return the Char-object specifying the given character in the given text style
     * @throws TextStyleMappingNotFoundException if there's no text style defined with
     * 		the given name
     */
    public Char getChar(char c, String textStyle, int style)
    throws TextStyleMappingNotFoundException;

    /**
     * Get a Char-object for this specific character containing the metric information
     *
     * @param cf CharFont-object determining a specific character of a specific font
     * @param style the style in which the atom should be drawn
     * @return the Char-object for this character containing metric information
     */
    public Char getChar(CharFont cf, int style);

    /**
     * Get a Char-object for the given symbol with metric information depending on
     * "style".
     *
     * @param name the symbol name
     * @param style the style in which the atom should be drawn
     * @return a Char-object for this symbol with metric information
     * @throws SymbolMappingNotFoundException if there's no symbol defined with the given
     * 			name
     */
    public Char getChar(String name, int style)
    throws SymbolMappingNotFoundException;

    /**
     * Get a Char-object specifying the given character in the default text style with
     * metric information depending on the given "style".
     *
     * @param c alphanumeric character
     * @param style the style in which the atom should be drawn
     * @return the Char-object specifying the given character in the default text style
     */
    public Char getDefaultChar(char c, int style);

    public float getDefaultRuleThickness(int style);

    public float getDenom1(int style);

    public float getDenom2(int style);

    /**
     * Get an Extension-object for the given Char containing the 4 possible parts to
     * build an arbitrary large variant. This will only be called if isExtensionChar(Char)
     * returns true.
     *
     * @param c a Char-object for a specific character
     * @param style the style in which the atom should be drawn
     * @return an Extension object containing the 4 possible parts
     */
    public Extension getExtension(Char c, int style);

    /**
     * Get the kern value to be inserted between the given characters in the given style.
     *
     * @param left left character
     * @param right right character
     * @param style the style in which the atom should be drawn
     * @return the kern value between both characters (default 0)
     */
    public float getKern(CharFont left, CharFont right, int style);

    /**
     * Get the ligature that replaces both characters (if any).
     *
     * @param left left character
     * @param right right character
     * @return a ligature replacing both characters (or null: no ligature)
     */
    public CharFont getLigature(CharFont left, CharFont right);

    public int getMuFontId();

    /**
     * Get the next larger version of the given character. This is only called if
     * hasNextLarger(Char) returns true.
     *
     * @param c character
     * @param style the style in which the atom should be drawn
     * @return the next larger version of this character
     */
    public Char getNextLarger(Char c, int style);

    public float getNum1(int style);

    public float getNum2(int style);

    public float getNum3(int style);

    public float getQuad(int style, int fontCode);

    /**
     *
     * @return the point size of this TeXFont
     */
    public float getSize();

    /**
     * Get the kern amount of the character defined by the given CharFont followed by the
     * "skewchar" of it's font. This is used in the algorithm for placing an accent above
     * a single character.
     *
     * @param cf the character and it's font above which an accent has to be placed
     * @param style the render style
     * @return the kern amount of the character defined by cf followed by the
     * "skewchar" of it's font.
     */
    public float getSkew(CharFont cf, int style);

    public float getSpace(int style);

    public float getSub1(int style);

    public float getSub2(int style);

    public float getSubDrop(int style);

    public float getSup1(int style);

    public float getSup2(int style);

    public float getSup3(int style);

    public float getSupDrop(int style);

    public float getXHeight(int style, int fontCode);

    public float getEM(int style);

    /**
     *
     * @param c a character
     * @return true if the given character has a larger version, false otherwise
     */
    public boolean hasNextLarger(Char c);

    public boolean hasSpace(int font);

    public void setBold(boolean bold);

    public boolean getBold();

    public void setRoman(boolean rm);

    public boolean getRoman();

    public void setTt(boolean tt);

    public boolean getTt();

    public void setIt(boolean it);

    public boolean getIt();

    public void setSs(boolean ss);

    public boolean getSs();

    /**
     *
     * @param c a character
     * @return true if the given character contains extension information to buid
     * 			an arbitrary large version of this character.
     */
    public boolean isExtensionChar(Char c);

    public TeXFont copy();
}
