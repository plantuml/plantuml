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
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.skin.VisibilityModifier;

class LabelBuilderTableVisibility extends DotCommon implements LabelBuilder {

	private final IEntity entity;
	private final boolean isField;
	private final int spring;

	LabelBuilderTableVisibility(FileFormat fileFormat, DotData data, IEntity entity, boolean isField, int spring) {
		super(fileFormat, data);
		this.entity = entity;
		this.isField = isField;
		this.spring = spring;
	}

	public void appendLabel(StringBuilder sb) throws IOException {
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");

		final boolean hasStatic = hasStatic(entity.getMethodsToDisplay());
		final boolean dpiNormal = getData().getDpi() == 96;
		for (Member att : isField ? entity.getFieldsToDisplay() : entity.getMethodsToDisplay()) {
			sb.append("<TR>");
			if (dpiNormal) {
				sb.append("<TD WIDTH=\"10\">");
			}
			String s = att.getDisplayWithVisibilityChar();
			final VisibilityModifier visibilityModifier = VisibilityModifier
					.getVisibilityModifier(s.charAt(0), isField);
			if (visibilityModifier != null) {
				final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
				final String modifierFile = StringUtils
						.getPlateformDependentAbsolutePath(getData().getVisibilityImages(visibilityModifier, stereo)
								.getPngOrEps(getFileFormat() == FileFormat.EPS));
				if (dpiNormal) {
					sb.append("<IMG SRC=\"" + modifierFile + "\"/>");
				} else {
					addTdImageBugB1983(sb, modifierFile);
				}
				s = s.substring(1);
			}
			if (dpiNormal) {
				sb.append("</TD>");
			}
			sb.append("<TD ALIGN=\"LEFT\">");
			sb.append(manageHtmlIBspecial(att, FontParam.CLASS_ATTRIBUTE, hasStatic, getColorString(
					ColorParam.classBackground, null), false));
			sb.append("</TD>");
			for (int i = 0; i < spring; i++) {
				sb.append("<TD></TD>");
			}
			sb.append("</TR>");
		}
		sb.append("</TABLE>");
	}

}