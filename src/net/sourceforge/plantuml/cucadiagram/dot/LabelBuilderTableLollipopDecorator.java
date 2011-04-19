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
 * Revision $Revision: 6295 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.cucadiagram.IEntity;

class LabelBuilderTableLollipopDecorator extends LabelBuilderObjectOrClass implements LabelBuilder {

	private final LabelBuilder builder;

	private final String northPath;
	private final String southPath;
	private final String eastPath;
	private final String westPath;

	LabelBuilderTableLollipopDecorator(FileFormat fileFormat, DotData data, IEntity entity, LabelBuilder builder,
			String northPath, String southPath, String eastPath, String westPath, Collection<IEntity> collection) {
		super(fileFormat, data, entity);
		this.builder = builder;
		this.northPath = northPath;
		this.southPath = southPath;
		this.eastPath = eastPath;
		this.westPath = westPath;

	}

	public void appendLabel(StringBuilder sb) throws IOException {
		final String backColor = getBackColorAroundEntity(getEntity());
		// backColor="\"#EE0000\"";
		sb.append("<TABLE BGCOLOR=" + backColor + " COLOR=" + backColor
				+ " BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"1\" CELLPADDING=\"0\">");
		sb.append("<TR>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		appendBlankOrImage(sb, northPath, Orientation.NORTH);
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("<TR>");
		appendBlankOrImage(sb, westPath, Orientation.WEST);
		sb.append("<TD PORT=\"h\">");

		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}

		sb.append("</TD>");
		appendBlankOrImage(sb, eastPath, Orientation.EAST);
		sb.append("</TR>");
		sb.append("<TR>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		appendBlankOrImage(sb, southPath, Orientation.SOUTH);
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("</TABLE>");
	}

	private void appendBlankOrImage(StringBuilder sb, String path, Orientation orientation) throws IOException {
		if (path == null) {
			sb.append("<TD>");
			sb.append(" ");
			sb.append("</TD>");
		} else if (path.endsWith(".png")) {
			if (getData().getDpi() == 96) {
				final BufferedImage im = ImageIO.read(new File(path));
				final int height = im.getHeight();
				final int width = im.getWidth();
				// sb.append("<TD FIXEDSIZE=\"TRUE\" WIDTH=\"" + width + "\" HEIGHT=\"" + height + "\"><IMG SRC=\"" +
				// path
				// + "\"/></TD>");
				sb.append("<TD><IMG SRC=\"" + path + "\"/></TD>");
			} else {
				addTdImageBugB1983(sb, path);
			}
		} else if (path.endsWith(".eps")) {
			sb.append("<TD><IMG SRC=\"" + path + "\"/></TD>");
		}

	}

}