/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 14727 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;

public class SkinUtils {

	static public Skin loadSkin(String className) {
		final List<String> errors = new ArrayList<String>();
		Skin result = tryLoading(className, errors);
		if (result != null) {
			return result;
		}
		result = tryLoading("net.sourceforge.plantuml.skin." + className, errors);
		if (result != null) {
			return result;
		}
		final String packageName = StringUtils.goLowerCase(className);
		result = tryLoading(packageName + "." + className, errors);
		if (result != null) {
			return result;
		}
		result = tryLoading("net.sourceforge.plantuml.skin." + packageName + "." + className, errors);
		if (result != null) {
			return result;
		}
		for (String e : errors) {
			Log.println("err="+e);
		}
		return null;
	}

	private static Skin tryLoading(String className, List<String> errors) {
		try {
			final Class<Skin> cl = (Class<Skin>) Class.forName(className);
			return cl.newInstance();
		} catch (Exception e) {
			errors.add("Cannot load " + className);
			return null;
		}
	}
}
