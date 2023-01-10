
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

import net.sourceforge.plantuml.genericdiagram.GenericEntityType;
import net.sourceforge.plantuml.genericdiagram.GenericLinkArrow;
import net.sourceforge.plantuml.genericdiagram.GenericLinkDecor;
import net.sourceforge.plantuml.genericdiagram.GenericLinkStyle;
import net.sourceforge.plantuml.genericdiagram.IGenericLink;
import net.sourceforge.plantuml.genericdiagram.IGenericModelElement;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;

/**
 * Simple POJO data class to represent a generic link on a plantUML diagram
 */

public class GenericLink extends GenericModelElement implements IGenericLink, IGenericModelElement {

	// visual representation
	GenericLinkStyle style;
	GenericLinkDecor sourceDecor;
	GenericLinkDecor targetDecor;
	GenericLinkDecor middleDecor; // Deployment Diagram only

	// text annotations
	String sourceLabel;
	String targetLabel;

	// special cases
	GenericLinkArrow direction;      // Class / Object diagram only

	public GenericLink(int elementCount) {
		super(elementCount);
		setType(GenericEntityType.LINK);
	}

	public GenericLink(){
		setType(GenericEntityType.LINK);
	}

	@Override
	public boolean isLink(){
		return true;
	}
	public void setStyle(GenericLinkStyle style) {
		this.style = style;
	}

	public void setSourceDecor(GenericLinkDecor sourceDecor) {
		this.sourceDecor = sourceDecor;
	}

	public void setTargetDecor(GenericLinkDecor targetDecor) {
		this.targetDecor = targetDecor;
	}

	public void setMiddleDecor(GenericLinkDecor middleDecor) {
		this.middleDecor = middleDecor;
	}

	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}

	public void setDirection(GenericLinkArrow linkArrow) {
		this.direction = linkArrow;
	}

	@Override
	public GenericLinkStyle getStyle()
	{
		return style;
	}

	@Override
	public GenericLinkDecor getSourceDecor() {
		return sourceDecor;
	}

	@Override
	public GenericLinkDecor getTargetDecor() {
		return targetDecor;
	}

	@Override
	public GenericLinkDecor getMiddleDecor() {
		return middleDecor;
	}

	@Override
	public String getSourceLabel() {
		return sourceLabel;
	}

	@Override
	public String getTargetLabel() {
		return targetLabel;
	}

	@Override
	public GenericLinkArrow getDirection() {
		return direction;
	}

	@Override
	public void acceptVisitor(IGenericDiagramVisitor visitor) {
		visitor.visitLink(this);
	}
}
