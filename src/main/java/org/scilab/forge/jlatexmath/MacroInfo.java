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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MacroInfo {

    public static HashMap<String, MacroInfo> Commands = new HashMap<String, MacroInfo>(300);
    public static HashMap<String, Object> Packages = new HashMap<String, Object>();

    public Object pack;
    public Method macro;
    public int nbArgs;
    public boolean hasOptions = false;
    public int posOpts;

    public MacroInfo(Object pack, Method macro, int nbArgs) {
        this.pack = pack;
        this.macro = macro;
        this.nbArgs = nbArgs;
    }

    public MacroInfo(Object pack, Method macro, int nbArgs, int posOpts) {
        this(pack, macro, nbArgs);
        this.hasOptions = true;
        this.posOpts = posOpts;
    }

    public MacroInfo(int nbArgs, int posOpts) {
        this(null, (Method) null, nbArgs);
        this.hasOptions = true;
        this.posOpts = posOpts;
    }

    public MacroInfo(int nbArgs) {
        this(null, (Method) null, nbArgs);
    }

    public MacroInfo(String className, String methodName, float nbArgs) {
        int nba = (int) nbArgs;
        Class<?>[] args = new Class<?>[] {TeXParser.class, String[].class};

        try {
            Object pack = Packages.get(className);
            if (pack == null) {
                Class<?> cl = Class.forName(className);
                pack = cl.getConstructor(new Class[0]).newInstance(new Object[0]);
                Packages.put(className, pack);
            }
            this.pack = pack;
            this.macro = pack.getClass().getDeclaredMethod(methodName, args);
            this.nbArgs = nba;
        } catch (Exception e) {
            System.err.println("Cannot load package " + className + ":");
            System.err.println(e.toString());
        }
    }

    public MacroInfo(String className, String methodName, float nbArgs, float posOpts) {
        int nba = (int) nbArgs;
        Class<?>[] args = new Class<?>[] {TeXParser.class, String[].class};

        try {
            Object pack = Packages.get(className);
            if (pack == null) {
                Class<?> cl = Class.forName(className);
                pack = cl.getConstructor(new Class[0]).newInstance(new Object[0]);
                Packages.put(className, pack);
            }
            this.pack = pack;
            this.macro = pack.getClass().getDeclaredMethod(methodName, args);
            this.nbArgs = nba;
            this.hasOptions = true;
            this.posOpts = (int) posOpts;
        } catch (Exception e) {
            System.err.println("Cannot load package " + className + ":");
            System.err.println(e.toString());
        }
    }

    public Object invoke(final TeXParser tp, final String[] args) throws ParseException {
        Object[] argsMethod = {(Object) tp, (Object) args};
        try {
            return macro.invoke(pack, argsMethod);
        } catch (IllegalAccessException e) {
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n", e);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n", e);
        } catch (InvocationTargetException e) {
            Throwable th = e.getCause();
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n" + th.getMessage());
        }
    }
}
