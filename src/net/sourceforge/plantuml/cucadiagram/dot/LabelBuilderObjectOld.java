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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;

class LabelBuilderObjectOld extends LabelBuilderObjectOrClass implements LabelBuilder {

	LabelBuilderObjectOld(FileFormat fileFormat, DotData data, IEntity entity) {
		super(fileFormat, data, entity);
	}

	public void appendLabel(StringBuilder sb) throws IOException {
		final String stereo = getEntity().getStereotype() == null ? null : getEntity().getStereotype().getLabel();

		sb.append("<<TABLE BGCOLOR=" + getColorString(ColorParam.classBackground, stereo)
				+ " BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">");
		sb.append("<TR><TD>");

		final int longuestFieldOrAttribute = getLongestFieldOrAttribute(getEntity());
		final int longuestHeader = getLonguestHeader(getEntity());
		final int spring = computeSpring(longuestHeader, longuestFieldOrAttribute, 30);

		sb.append(getHtmlHeaderTableForObjectOrClassOrInterfaceOrEnum(getEntity(), null, spring, false, BorderMode.NO_BORDER));

		sb.append("</TD></TR>");
		sb.append("<TR ALIGN=\"LEFT\"><TD " + getWitdh55() + " ALIGN=\"LEFT\">");

		if (getEntity().getFieldsToDisplay().size() == 0) {
			sb.append(manageHtmlIB(" ", FontParam.OBJECT_ATTRIBUTE, stereo));
		} else {
			for (Member att : getEntity().getFieldsToDisplay()) {
				sb.append(manageHtmlIB(att.getDisplayWithVisibilityChar(), FontParam.OBJECT_ATTRIBUTE, stereo));
				sb.append("<BR ALIGN=\"LEFT\"/>");
			}
		}

		sb.append("</TD></TR>");
		sb.append("</TABLE>>");

	}

}