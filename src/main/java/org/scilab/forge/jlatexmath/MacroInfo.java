/* MacroInfo.java
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

import java.util.HashMap;

/**
 * A command of the formulas: its number of arguments, and what it does. The
 * predefined commands are {@link PredefMacroInfo}, which override {@link #invoke};
 * the commands that a formula defines ({@code \newcommand}) have a
 * {@link MacroHandler}. There is no reflection ({@code Method.invoke}): it does
 * not work in the TeaVM C build.
 */
public class MacroInfo {

    /** What a command defined by a formula does. */
    public interface MacroHandler {
        Object invoke(TeXParser tp, String[] args) throws ParseException;
    }

    public static HashMap<String, MacroInfo> Commands = new HashMap<String, MacroInfo>(300);

    private final MacroHandler handler;
    public int nbArgs;
    public boolean hasOptions = false;
    public int posOpts;

    public MacroInfo(MacroHandler handler, int nbArgs) {
        this.handler = handler;
        this.nbArgs = nbArgs;
    }

    public MacroInfo(MacroHandler handler, int nbArgs, int posOpts) {
        this(handler, nbArgs);
        this.hasOptions = true;
        this.posOpts = posOpts;
    }

    public MacroInfo(int nbArgs, int posOpts) {
        this(null, nbArgs, posOpts);
    }

    public MacroInfo(int nbArgs) {
        this(null, nbArgs);
    }

    public Object invoke(final TeXParser tp, final String[] args) throws ParseException {
        if (handler == null) {
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n");
        }
        try {
            return handler.invoke(tp, args);
        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n" + e.getMessage());
        }
    }
}
