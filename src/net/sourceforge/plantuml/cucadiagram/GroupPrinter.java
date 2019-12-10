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
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class GroupPrinter {

	private final PrintWriter pw;

	private GroupPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	private void printGroup(IGroup group) {
		pw.println("<table border=1 cellpadding=8 cellspacing=0>");
		pw.println("<tr>");
		pw.println("<td bgcolor=#DDDDDD>");
		pw.println(group.getCodeGetName());
		pw.println("<tr>");
		pw.println("<td>");
		if (group.getLeafsDirect().size() == 0) {
			pw.println("<i>No direct leaf</i>");
		} else {
			for (ILeaf leaf : group.getLeafsDirect()) {
				pw.println("<ul>");
				printLeaf(leaf);
				pw.println("</ul>");
			}
		}
		pw.println("</td>");
		pw.println("</tr>");
		if (group.getChildren().size() > 0) {
			pw.println("<tr>");
			pw.println("<td>");
			for (IGroup g : group.getChildren()) {
				pw.println("<br>");
				printGroup(g);
				pw.println("<br>");
			}
			pw.println("</td>");
			pw.println("</tr>");
		}
		pw.println("</table>");
	}

	private void printLeaf(ILeaf leaf) {
		pw.println("<li>" + leaf.getCodeGetName());
	}

	public static void print(File f, IGroup rootGroup) {
		try {
			final PrintWriter pw = new PrintWriter(f);
			pw.println("<html>");
			new GroupPrinter(pw).printGroup(rootGroup);
			pw.println("</html>");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
