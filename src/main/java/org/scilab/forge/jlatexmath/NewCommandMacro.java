/* NewCommandMacro.java
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
import java.util.regex.Matcher;

public class NewCommandMacro {

    protected static HashMap<String, String> macrocode = new HashMap<String, String>();
    protected static HashMap<String, String> macroreplacement = new HashMap<String, String>();

    public NewCommandMacro() {
    }

    public static void addNewCommand(String name, String code, int nbargs) throws ParseException {
        //if (macrocode.get(name) != null)
        //throw new ParseException("Command " + name + " already exists ! Use renewcommand instead ...");
        macrocode.put(name, code);
        MacroInfo.Commands.put(name, new MacroInfo("org.scilab.forge.jlatexmath.NewCommandMacro", "executeMacro", nbargs));
    }

    public static void addNewCommand(String name, String code, int nbargs, String def) throws ParseException {
        if (macrocode.get(name) != null)
            throw new ParseException("Command " + name + " already exists ! Use renewcommand instead ...");
        macrocode.put(name, code);
        macroreplacement.put(name, def);
        MacroInfo.Commands.put(name, new MacroInfo("org.scilab.forge.jlatexmath.NewCommandMacro", "executeMacro", nbargs, 1));
    }

    public static boolean isMacro(String name) {
        return macrocode.containsKey(name);
    }

    public static void addReNewCommand(String name, String code, int nbargs) {
        if (macrocode.get(name) == null)
            throw new ParseException("Command " + name + " is not defined ! Use newcommand instead ...");
        macrocode.put(name, code);
        MacroInfo.Commands.put(name, new MacroInfo("org.scilab.forge.jlatexmath.NewCommandMacro", "executeMacro", nbargs));
    }

    public String executeMacro(TeXParser tp, String[] args) {
        String code = macrocode.get(args[0]);
        String rep;
        int nbargs = args.length - 11;
        int dec = 0;


        if (args[nbargs + 1] != null) {
            dec = 1;
            rep = Matcher.quoteReplacement(args[nbargs + 1]);
            code = code.replaceAll("#1", rep);
        } else if (macroreplacement.get(args[0]) != null) {
            dec = 1;
            rep = Matcher.quoteReplacement(macroreplacement.get(args[0]));
            code = code.replaceAll("#1", rep);
        }

        for (int i = 1; i <= nbargs; i++) {
            rep = Matcher.quoteReplacement(args[i]);
            code = code.replaceAll("#" + (i + dec), rep);
        }

        return code;
    }
}
