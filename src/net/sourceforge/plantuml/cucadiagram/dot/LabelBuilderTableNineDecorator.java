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

import java.io.IOException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.cucadiagram.IEntity;

class LabelBuilderTableNineDecorator extends LabelBuilderObjectOrClass implements LabelBuilder {
	
	private final LabelBuilder builder;

	LabelBuilderTableNineDecorator(FileFormat fileFormat, DotData data, IEntity entity, LabelBuilder builder) {
		super(fileFormat, data, entity);
		this.builder = builder;
		if (builder instanceof LabelBuilderClassOld) {
			((LabelBuilderClassOld)builder).patch();
		}
		
	}

	public void appendLabel(StringBuilder sb) throws IOException {
		final String backColor = getBackColorAroundEntity(getEntity());
		// backColor="\"#EE0000\"";
		sb.append("<TABLE BGCOLOR=" + backColor + " COLOR=" + backColor
				+ " BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"4\" CELLPADDING=\"0\">");
		sb.append("<TR>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("<TR>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("<TD PORT=\"h\">");

		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}

		sb.append("</TD>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("<TR>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("<TD>");
		sb.append(" ");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("</TABLE>");
	}


}