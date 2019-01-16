/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 */
package smetana.core.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Debug {

	private final Map<String, String> methodNames = new HashMap<String, String>();
	private final Collection<String> called = new LinkedHashSet<String>();

	public void entering(String signature, String methodName) {
		methodNames.put(signature, methodName);
		if (called.contains(signature) == false) {
			called.add(signature);
		}
	}

	public void leaving(String signature, String methodName) {
	}

	public void reset() {
		methodNames.clear();
		called.clear();
	}

	public void printMe() {
		System.err.println("methodNames=" + methodNames.size());
		System.err.println("called=" + called.size());
		final List<String> called2 = new ArrayList<String>(called);
		for (int i = 0; i < called.size(); i++) {
			System.err.println("n " + i + " " + methodNames.get(called2.get(i)) + " " + called2.get(i));
		}
		final Set<String> called3 = new HashSet<String>(called);
		for (String s : called3) {
			System.err.println("p " + s);
		}
	}

}
