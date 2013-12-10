/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 8770 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;

public class Code implements Comparable<Code> {

	private final String code;

	private Code(String code) {
		if (code == null) {
			throw new IllegalArgumentException();
		}
		this.code = code;
	}

	public static Code of(String code) {
		if (code == null) {
			return null;
		}
		return new Code(code);
	}

	public final String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code;
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Code other = (Code) obj;
		return this.code.equals(other.code);
	}

	public Code addSuffix(String suffix) {
		return new Code(code + suffix);
	}

	public int compareTo(Code other) {
		return this.code.compareTo(other.code);
	}

	public Code eventuallyRemoveStartingAndEndingDoubleQuote() {
		return Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code));
	}

	public final String getNamespace(Map<Code, ILeaf> leafs, String namespaceSeparator) {
		String code = this.getCode();
		if (namespaceSeparator == null) {
			throw new IllegalArgumentException();
		}
		do {
			final int x = code.lastIndexOf(namespaceSeparator);
			if (x == -1) {
				return null;
			}
			code = code.substring(0, x);
		} while (leafs.containsKey(Code.of(code)));
		return code;
	}

	public final Code getShortName(Map<Code, ILeaf> leafs, String namespaceSeparator) {
		if (namespaceSeparator == null) {
			throw new IllegalArgumentException();
		}
		final String code = this.getCode();
		final String namespace = getNamespace(leafs, namespaceSeparator);
		if (namespace == null) {
			return Code.of(code);
		}
		return Code.of(code.substring(namespace.length() + namespaceSeparator.length()));
	}

	public final Code getFullyQualifiedCode(IGroup g, String namespaceSeparator) {
		if (namespaceSeparator == null) {
			throw new IllegalArgumentException();
		}
		final String code = this.getCode();
		if (code.startsWith(namespaceSeparator)) {
			return Code.of(code.substring(namespaceSeparator.length()));
		}
		if (code.contains(namespaceSeparator)) {
			return Code.of(code);
		}
		if (EntityUtils.groupRoot(g)) {
			return Code.of(code);
		}
		final String namespace = g.getNamespace();
		if (namespace == null) {
			return Code.of(code);
		}
		return Code.of(namespace + namespaceSeparator + code);
	}

}
