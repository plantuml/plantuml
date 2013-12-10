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

public class Code2 {

	private final Namespace namespace;
	private final String unqualifiedCode;

	private Code2(Namespace namespace, String unqualifiedCode) {
		if (namespace == null) {
			throw new IllegalArgumentException();
		}
		if (unqualifiedCode == null) {
			throw new IllegalArgumentException();
		}
		this.namespace = namespace;
		this.unqualifiedCode = unqualifiedCode;
	}

	public final Namespace getNamespace() {
		return namespace;
	}

	public final String getUnqualifiedCode() {
		return unqualifiedCode;
	}

	public final String getFullQualifiedCode() {
		if (namespace.isMain()) {
			return unqualifiedCode;
		}
		return namespace.getNamespace() + "." + unqualifiedCode;
	}

	@Override
	public int hashCode() {
		return namespace.hashCode() + 43 * unqualifiedCode.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Code2 other = (Code2) obj;
		return this.unqualifiedCode.equals(other.unqualifiedCode) && this.namespace.equals(other.namespace);
	}
}
