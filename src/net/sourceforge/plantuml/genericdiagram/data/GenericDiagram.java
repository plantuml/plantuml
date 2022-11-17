
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package net.sourceforge.plantuml.genericdiagram.data;


import net.sourceforge.plantuml.genericdiagram.GenericDiagramType;
import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.IGenericDiagram;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;

public class GenericDiagram extends GenericEntity implements IGenericDiagram {

	String title;
	String header;
	String footer;
	String caption;
	String legend;
	GenericDiagramType diagramType;

	String sourceFile;

	public GenericDiagram(int elementCount) {
		super(elementCount);
		setType(GenericEntityType.DIAGRAM);
	}

	public GenericDiagram() {
		setType(GenericEntityType.DIAGRAM);

	}

	@Override
	public boolean isDiagram() {
		return true;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	@Override
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Override
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	@Override
	public GenericDiagramType getDiagramType() {
		return diagramType;
	}

	public void setDiagramType(GenericDiagramType diagramType) {
		this.diagramType = diagramType;
	}

	@Override
	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {
		visitor.visitDiagram(this);
	}
}
