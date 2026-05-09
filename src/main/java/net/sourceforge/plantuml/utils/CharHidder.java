/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.utils;

public class CharHidder {

	public static String addTileAtBegin(String s) {
		return "~" + s;
	}

	public static String hide(String s) {
	    final int len = s.length();
	    char[] buf = null; // allocated lazily on first match
	    int n = 0; // write index in buf
	    int i = 0;
	    while (i < len) {
	        final char c = s.charAt(i);
	        if (c == '\\' && i + 1 < len && s.charAt(i + 1) == '~') {
	            if (buf == null) {
	                buf = new char[len];
	                s.getChars(0, i, buf, 0);
	                n = i;
	            }
	            buf[n++] = (char) ('\uE000' + '~');
	            i += 2;
	        } else if (c == '~' && i + 1 < len) {
	            final char c2 = s.charAt(i + 1);
	            if (isToBeHidden(c2)) {
	                if (buf == null) {
	                    buf = new char[len];
	                    s.getChars(0, i, buf, 0);
	                    n = i;
	                }
	                buf[n++] = (char) ('\uE000' + c2);
	                i += 2;
	            } else {
	                if (buf != null) {
	                    buf[n++] = c;
	                    buf[n++] = c2;
	                }
	                i += 2;
	            }
	        } else {
	            if (buf != null)
	                buf[n++] = c;
	            i++;
	        }
	    }
	    return buf == null ? s : new String(buf, 0, n);
	}
	private static boolean isToBeHidden(final char c) {
		if (c == '_' || c == '-' || c == '\"' || c == '#' || c == ']' || c == '[' || c == '*' || c == '.' || c == '/'
				|| c == '<')
			return true;

		return false;
	}

	private static char hideChar(char c) {
		if (c > 255)
			throw new IllegalArgumentException();

		return (char) ('\uE000' + c);
	}

	private static char unhideChar(char c) {
		if (c >= '\uE000' && c <= '\uE0FF')
			return (char) (c - '\uE000');

		return c;
	}

	public static String unhide(String s) {
	    final int len = s.length();
	    for (int i = 0; i < len; i++) {
	        final char c = s.charAt(i);
	        if (c >= '\uE000' && c <= '\uE0FF') {
	            // First hidden char found at position i.
	            final char[] buf = new char[len];
	            s.getChars(0, i, buf, 0);
	            buf[i] = (char) (c - '\uE000');
	            for (int j = i + 1; j < len; j++) {
	                final char cj = s.charAt(j);
	                if (cj >= '\uE000' && cj <= '\uE0FF')
	                    buf[j] = (char) (cj - '\uE000');
	                else
	                    buf[j] = cj;
	            }
	            return new String(buf);
	        }
	    }
	    return s;
	}
}
