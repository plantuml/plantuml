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

abstract class LabelBuilderObjectOrClass extends DotCommon implements LabelBuilder {

	private final IEntity entity;

	LabelBuilderObjectOrClass(FileFormat fileFormat, DotData data, IEntity entity) {
		super(fileFormat, data);
		this.entity = entity;
	}


	protected final void buildTableVisibility(IEntity entity, boolean isField, final StringBuilder sb, int spring)
			throws IOException {
		final LabelBuilder builder = new LabelBuilderTableVisibility(getFileFormat(), getData(), entity, isField,
				spring);
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
	}

	protected final String getHtmlHeaderTableForObjectOrClassOrInterfaceOrEnum(IEntity entity, final String circleAbsolutePath,
			int spring, boolean classes, BorderMode border) throws IOException {
		final LabelBuilder builder = new LabelBuilderHtmlHeaderTableForObjectOrClass(getFileFormat(), getData(),
				entity, circleAbsolutePath, spring, classes, border);
		final StringBuilder sb = new StringBuilder();
		builder.appendLabel(sb);
		if (builder.isUnderline()) {
			setUnderline(true);
		}
		return sb.toString();
	}


	protected final IEntity getEntity() {
		return entity;
	}

}