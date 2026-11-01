/* TeXEnvironment.java
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

package org.scilab.forge.jlatexmath;

import java.awt.Color;

/**
 * Contains the used TeXFont-object, color settings and the current style in which a
 * formula must be drawn. It's used in the createBox-methods. Contains methods that
 * apply the style changing rules for subformula's.
 */
public class TeXEnvironment {

    // colors
    private Color background = null, color = null;

    // current style
    private int style = TeXConstants.STYLE_DISPLAY;

    // TeXFont used
    private TeXFont tf;

    // last used font
    private int lastFontId = TeXFont.NO_FONT;

    private float textwidth = Float.POSITIVE_INFINITY;

    private String textStyle;
    private boolean smallCap;
    private float scaleFactor = 1;
    private int interlineUnit;
    private float interline;

    public boolean isColored = false;

    public TeXEnvironment(int style, TeXFont tf) {
        this(style, tf, null, null);
    }

    public TeXEnvironment(int style, TeXFont tf, int widthUnit, float textwidth) {
        this(style, tf, null, null);
        this.textwidth = textwidth * SpaceAtom.getFactor(widthUnit, this);
    }

    private TeXEnvironment(int style, TeXFont tf, Color bg, Color c) {
        this.style = style;
        this.tf = tf;
        background = bg;
        color = c;
        setInterline(TeXConstants.UNIT_EX, 1f);
    }

    private TeXEnvironment(int style, float scaleFactor, TeXFont tf, Color bg, Color c, String textStyle, boolean smallCap) {
        this.style = style;
        this.scaleFactor = scaleFactor;
        this.tf = tf;
        this.textStyle = textStyle;
        this.smallCap = smallCap;
        background = bg;
        color = c;
        setInterline(TeXConstants.UNIT_EX, 1f);
    }

    public void setInterline(int unit, float len) {
        this.interline = len;
        this.interlineUnit = unit;
    }

    public float getInterline() {
        return interline * SpaceAtom.getFactor(interlineUnit, this);
    }

    public void setTextwidth(int widthUnit, float textwidth) {
        this.textwidth = textwidth * SpaceAtom.getFactor(widthUnit, this);
    }

    public float getTextwidth() {
        return textwidth;
    }

    public void setScaleFactor(float f) {
        scaleFactor = f;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    protected TeXEnvironment copy() {
        return new TeXEnvironment(style, scaleFactor, tf, background, color, textStyle, smallCap);
    }

    protected TeXEnvironment copy(TeXFont tf) {
        TeXEnvironment te = new TeXEnvironment(style, scaleFactor, tf, background, color, textStyle, smallCap);
        te.textwidth = textwidth;
        te.interline = interline;
        te.interlineUnit = interlineUnit;
        return te;
    }

    /**
     * @return a copy of the environment, but in a cramped style.
     */
    public TeXEnvironment crampStyle() {
        TeXEnvironment s = copy();
        s.style = (style % 2 == 1 ? style : style + 1);
        return s;
    }

    /**
     *
     * @return a copy of the environment, but in denominator style.
     */
    public TeXEnvironment denomStyle() {
        TeXEnvironment s = copy();
        s.style = 2 * (style / 2) + 1 + 2 - 2 * (style / 6);
        return s;
    }

    /**
     *
     * @return the background color setting
     */
    public Color getBackground() {
        return background;
    }

    /**
     *
     * @return the foreground color setting
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return the point size of the TeXFont
     */
    public float getSize() {
        return tf.getSize();
    }

    /**
     *
     * @return the current style
     */
    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * @return the current textStyle
     */
    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    /**
     * @return the current textStyle
     */
    public boolean getSmallCap() {
        return smallCap;
    }

    public void setSmallCap(boolean smallCap) {
        this.smallCap = smallCap;
    }

    /**
     *
     * @return the TeXFont to be used
     */
    public TeXFont getTeXFont() {
        return tf;
    }

    /**
     *
     * @return a copy of the environment, but in numerator style.
     */
    public TeXEnvironment numStyle() {
        TeXEnvironment s = copy();
        s.style = style + 2 - 2 * (style / 6);
        return s;
    }

    /**
     * Resets the color settings.
     *
     */
    public void reset() {
        color = null;
        background = null;
    }

    /**
     *
     * @return a copy of the environment, but with the style changed for roots
     */
    public TeXEnvironment rootStyle() {
        TeXEnvironment s = copy();
        s.style = TeXConstants.STYLE_SCRIPT_SCRIPT;
        return s;
    }

    /**
     *
     * @param c the background color to be set
     */
    public void setBackground(Color c) {
        background = c;
    }

    /**
     *
     * @param c the foreground color to be set
     */
    public void setColor(Color c) {
        color = c;
    }

    /**
     *
     * @return a copy of the environment, but in subscript style.
     */
    public TeXEnvironment subStyle() {
        TeXEnvironment s = copy();
        s.style = 2 * (style / 4) + 4 + 1;
        return s;
    }

    /**
     *
     * @return a copy of the environment, but in superscript style.
     */
    public TeXEnvironment supStyle() {
        TeXEnvironment s = copy();
        s.style = 2 * (style / 4) + 4 + (style % 2);
        return s;
    }

    public float getSpace() {
        return tf.getSpace(style) * tf.getScaleFactor();
    }

    public void setLastFontId(int id) {
        lastFontId = id;
    }

    public int getLastFontId() {
        // if there was no last font id (whitespace boxes only), use default "mu font"
        return (lastFontId == TeXFont.NO_FONT ? tf.getMuFontId() : lastFontId);
    }
}
