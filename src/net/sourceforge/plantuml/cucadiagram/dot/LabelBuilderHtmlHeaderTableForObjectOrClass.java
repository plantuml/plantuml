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
import net.sourceforge.plantuml.cucadiagram.IEntity;

class LabelBuilderHtmlHeaderTableForObjectOrClass extends DotCommon implements LabelBuilder {

	private final IEntity entity;
	private final String circleAbsolutePath;
	private final int spring;
	private final BorderMode borderMode;
	private final boolean classes;

	LabelBuilderHtmlHeaderTableForObjectOrClass(FileFormat fileFormat, DotData data, IEntity entity,
			final String circleAbsolutePath, int spring, boolean classes, BorderMode borderMode) {
		super(fileFormat, data);
		this.entity = entity;
		this.circleAbsolutePath = circleAbsolutePath;
		this.classes = classes;
		this.borderMode = borderMode;
		this.spring = spring;

	}

	public void appendLabel(StringBuilder sb) throws IOException {
		if (spring == 0) {
			htmlHeaderTableForObjectOrClassOrInterfaceOrEnumNoSpring(sb, 0);
			return;
		}

		if (borderMode == BorderMode.NO_BORDER) {
			sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		} else if (borderMode == BorderMode.NO_BORDER_CELLSPACING_OLD) {
			sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"1\" CELLPADDING=\"1\">");
		} else if (borderMode == BorderMode.NO_BORDER_CELLSPACING_NEW) {
			final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
			sb.append("<TABLE BORDER=\"1\" CELLBORDER=\"0\" CELLSPACING=\"1\" CELLPADDING=\"1\" ");
			sb.append("COLOR=" + getColorString(ColorParam.classBorder, stereo) + " BGCOLOR="
					+ getColorString(ColorParam.classBackground, stereo));
			sb.append(">");
		} else if (borderMode == BorderMode.BORDER_1_WITHOUT_COLOR) {
			sb.append("<TABLE BORDER=\"1\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" ");
			sb.append(">");
		} else if (borderMode == BorderMode.BORDER_1_WITH_COLOR) {
			final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
			sb.append("<TABLE BORDER=\"1\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" ");
			sb.append("COLOR=" + getColorString(ColorParam.classBorder, stereo) + " BGCOLOR="
					+ getColorString(ColorParam.classBackground, stereo));

			sb.append(">");
		} else {
			throw new IllegalStateException();
		}
		sb.append("<TR>");

		for (int i = 0; i < spring; i++) {
			sb.append("<TD></TD>");
		}

		if (circleAbsolutePath != null) {
			appendImageAsTD(sb, circleAbsolutePath);
		}

		sb.append("<TD>");
		appendLabelAndStereotype(entity, sb, classes);
		sb.append("</TD>");

		for (int i = 0; i < spring; i++) {
			sb.append("<TD></TD>");
		}
		sb.append("</TR></TABLE>");

	}

	private void htmlHeaderTableForObjectOrClassOrInterfaceOrEnumNoSpring(StringBuilder sb, int cellSpacing)
			throws IOException {
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"" + cellSpacing + "\" CELLPADDING=\"0\">");
		sb.append("<TR>");
		if (circleAbsolutePath == null) {
			sb.append("<TD>");
		} else {
			if (getData().getDpi() == 96) {
				sb.append("<TD ALIGN=\"RIGHT\">");
				sb.append("<IMG SRC=\"" + circleAbsolutePath + "\"/></TD>");
			} else {
				addTdImageBugB1983(sb, circleAbsolutePath);
			}
			sb.append("<TD ALIGN=\"LEFT\">");
		}

		appendLabelAndStereotype(entity, sb, classes);
		sb.append("</TD></TR></TABLE>");

	}

}
