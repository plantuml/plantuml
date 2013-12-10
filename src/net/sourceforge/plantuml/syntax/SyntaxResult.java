/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 4639 $
 * 
 */
package net.sourceforge.plantuml.syntax;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.plantuml.UmlDiagramType;

public class SyntaxResult {

	private UmlDiagramType umlDiagramType;
	private boolean isError;
	private String description;
	private int errorLinePosition;
	private Collection<String> errors = new TreeSet<String>();
	private List<String> suggest;

	public UmlDiagramType getUmlDiagramType() {
		return umlDiagramType;
	}

	public boolean isError() {
		return isError;
	}

	public String getDescription() {
		return description;
	}

	public int getErrorLinePosition() {
		return errorLinePosition;
	}

	public List<String> getSuggest() {
		return suggest;
	}

	public Collection<String> getErrors() {
		return Collections.unmodifiableCollection(errors);
	}

	public void setUmlDiagramType(UmlDiagramType umlDiagramType) {
		this.umlDiagramType = umlDiagramType;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setErrorLinePosition(int errorLinePosition) {
		this.errorLinePosition = errorLinePosition;
	}

	public void addErrorText(String error) {
		this.errors.add(error);
	}

	public void setSuggest(List<String> suggest) {
		this.suggest = suggest;
	}

}
