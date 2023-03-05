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
package net.sourceforge.plantuml.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileImageData;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.stereo.Stereotype;

public final class CucaDiagramHtmlMaker {
	// ::remove folder when __CORE__

	private final CucaDiagram diagram;
	private final SFile dir;

	public CucaDiagramHtmlMaker(CucaDiagram diagram, SFile dir) {
		this.diagram = diagram;
		this.dir = dir;
	}

	public List<FileImageData> create() throws IOException {
		dir.mkdirs();
		if (dir.exists() == false) {
			throw new IOException("Cannot create " + dir);
		}
		final SFile f = dir.file("index.html");
		final PrintWriter pw = f.createPrintWriter();
		pw.println("<html>");
		printAllType(pw, LeafType.ENUM);
		printAllType(pw, LeafType.INTERFACE);
		printAllType(pw, LeafType.ANNOTATION);
		printAllType(pw, LeafType.ABSTRACT_CLASS);
		printAllType(pw, LeafType.CLASS);
		htmlClose(pw);
		return Arrays.asList(new FileImageData(dir, null));
	}

	private void printAllType(final PrintWriter pw, LeafType type) throws IOException {
		if (hasSome(type)) {
			pw.println("<h2>" + type.toHtml() + "</h2>");
			for (final Entity ent : diagram.getEntityFactory().leafs()) {
				if (ent.getLeafType() != type) {
					continue;
				}
				export(ent);
				pw.println("<li>");
				pw.println(LinkHtmlPrinter.htmlLink(ent));
				pw.println("</li>");
			}
			// for (Map.Entry<Code, IEntity> ent : new TreeMap<Code,
			// IEntity>(diagram.getLeafs()).entrySet()) {
			// if (ent.getValue().getEntityType() != type) {
			// continue;
			// }
			// export(ent.getValue());
			// pw.println("<li>");
			// pw.println(LinkHtmlPrinter.htmlLink(ent.getValue()));
			// pw.println("</li>");
			// }
		}
	}

	private boolean hasSome(final LeafType type) {
		for (Entity ent : diagram.getEntityFactory().leafs()) {
			if (ent.getLeafType() == type) {
				return true;
			}
		}
		return false;
	}

	private void export(Entity entity) throws IOException {
		final SFile f = dir.file(LinkHtmlPrinter.urlOf(entity));
		final PrintWriter pw = f.createPrintWriter();
		pw.println("<html>");
		pw.println("<title>" + StringUtils.unicodeForHtml(entity.getName()) + "</title>");
		pw.println("<h2>" + entity.getLeafType().toHtml() + "</h2>");
		for (CharSequence s : entity.getDisplay()) {
			pw.println(StringUtils.unicodeForHtml(s.toString()));
			pw.println("<br>");
		}
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null) {
			pw.println("<hr>");
			pw.println("<h3>Stereotype</h3>");
			for (String s : stereotype.getLabels(diagram.getSkinParam().guillemet())) {
				pw.println(s);
				pw.println("<br>");
			}
		}

		pw.println("<hr>");
		if (entity.getBodier().getFieldsToDisplay().size() == 0) {
			pw.println("<h2>No fields</h2>");
		} else {
			pw.println("<h2>Fields:</h2>");
			pw.println("<ul>");
			for (CharSequence m : entity.getBodier().getFieldsToDisplay()) {
				pw.println("<li>");
				pw.println(StringUtils.unicodeForHtml(m.toString()));
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		pw.println("<hr>");
		if (entity.getBodier().getMethodsToDisplay().size() == 0) {
			pw.println("<h2>No methods</h2>");
		} else {
			pw.println("<h2>Methods:</h2>");
			pw.println("<ul>");
			for (CharSequence m : entity.getBodier().getMethodsToDisplay()) {
				pw.println("<li>");
				pw.println(StringUtils.unicodeForHtml(m.toString()));
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		pw.println("<hr>");
		final Collection<Link> links = getLinksButNotes(entity);
		if (links.size() == 0) {
			pw.println("<h2>No links</h2>");
		} else {
			pw.println("<h2>Links:</h2>");
			pw.println("<ul>");
			for (Link l : links) {
				pw.println("<li>");
				new LinkHtmlPrinter(l, entity).printLink(pw);
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		final Collection<Entity> notes = getNotes(entity);
		if (notes.size() > 0) {
			pw.println("<hr>");
			pw.println("<h2>Notes:</h2>");
			pw.println("<ul>");
			for (Entity note : notes) {
				pw.println("<li>");
				for (CharSequence s : note.getDisplay()) {
					pw.println(StringUtils.unicodeForHtml(s.toString()));
					pw.println("<br>");
				}
				pw.println("</li>");
			}
			pw.println("</ul>");

		}

		htmlClose(pw);
	}

	private void htmlClose(final PrintWriter pw) {
		pw.println("<hr>");
		pw.println("<a href=index.html>Back to index</a>");
		pw.println("</html>");
		pw.close();
	}

	private Collection<Entity> getNotes(Entity ent) {
		final List<Entity> result = new ArrayList<>();
		for (Link link : diagram.getLinks()) {
			if (link.contains(ent) == false) {
				continue;
			}
			if (link.getEntity1().getLeafType() == LeafType.NOTE || link.getEntity2().getLeafType() == LeafType.NOTE) {
				result.add(link.getOther(ent));
			}
		}
		return Collections.unmodifiableList(result);
	}

	private Collection<Link> getLinksButNotes(Entity ent) {
		final List<Link> result = new ArrayList<>();
		for (Link link : diagram.getLinks()) {
			if (link.contains(ent) == false) {
				continue;
			}
			if (link.getEntity1().getLeafType() == LeafType.NOTE || link.getEntity2().getLeafType() == LeafType.NOTE) {
				continue;
			}
			result.add(link);
		}
		return Collections.unmodifiableList(result);
	}
}
