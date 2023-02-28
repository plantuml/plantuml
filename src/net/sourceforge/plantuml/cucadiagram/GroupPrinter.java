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
package net.sourceforge.plantuml.cucadiagram;

import java.io.IOException;
import java.io.PrintWriter;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;

public class GroupPrinter {
	// ::remove file when __CORE__

	private final PrintWriter pw;

	private GroupPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	private void printGroup(Entity group) {
		pw.println("<table border=1 cellpadding=8 cellspacing=0>");
		pw.println("<tr>");
		pw.println("<td bgcolor=#DDDDDD>");
		pw.println(group.getName());
		pw.println("<tr>");
		pw.println("<td>");
		if (group.leafs().size() == 0) {
			pw.println("<i>No direct leaf</i>");
		} else {
			for (Entity leaf : group.leafs()) {
				pw.println("<ul>");
				printLeaf(leaf);
				pw.println("</ul>");
			}
		}
		pw.println("</td>");
		pw.println("</tr>");
		if (group.groups().size() > 0) {
			pw.println("<tr>");
			pw.println("<td>");
			for (Entity g : group.groups()) {
				pw.println("<br>");
				printGroup(g);
				pw.println("<br>");
			}
			pw.println("</td>");
			pw.println("</tr>");
		}
		pw.println("</table>");
	}

	private void printLeaf(Entity leaf) {
		pw.println("<li>" + leaf.getName());
	}

	public static void print(SFile f, Entity rootGroup) {
		try (PrintWriter pw = f.createPrintWriter()) {
			pw.println("<html>");
			new GroupPrinter(pw).printGroup(rootGroup);
			pw.println("</html>");
		} catch (IOException e) {
			Logme.error(e);
		}
	}

}
