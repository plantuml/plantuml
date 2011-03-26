/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4768 $
 *
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final public class UmlSource {

	final private List<String> source = new ArrayList<String>();

	@Deprecated
	public UmlSource(UmlSource start) {
		this.source.addAll(start.source);
	}

	public UmlSource(List<String> source) {
		this.source.addAll(source);
	}

	@Deprecated
	public UmlSource() {
	}

	public Iterator<String> iterator() {
		return source.iterator();
	}

	@Deprecated
	public void append(String s) {
		source.add(s);
	}

	public String getPlainString() {
		final StringBuilder sb = new StringBuilder();
		for (String s : source) {
			sb.append(s);
			sb.append('\n');
		}
		return sb.toString();
	}

	public String getLine(int n) {
		return source.get(n);
	}

	public int getSize() {
		return source.size();
	}

	public boolean isEmpty() {
		for (String s : source) {
			if (BlockUmlBuilder.isArobaseStartuml(s)) {
				continue;
			}
			if (BlockUmlBuilder.isArobaseEnduml(s)) {
				continue;
			}
			if (s.matches("\\s*'.*")) {
				continue;
			}
			if (s.trim().length() != 0) {
				return false;
			}
		}
		return true;
	}

}
