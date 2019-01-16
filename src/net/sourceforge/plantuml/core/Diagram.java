/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.core;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.FileFormatOption;

/**
 * Represents a single diagram. A Diagram could be a UML (sequence diagram, class diagram...) or an non-UML diagram.
 * 
 * @author Arnaud Roques
 */
public interface Diagram {

	/**
	 * Export the diagram as an image to some format. Note that a diagram could be drawn as several images (think about
	 * <code>new page</code> for sequence diagram for example).
	 * 
	 * @param os
	 *            where to write the image
	 * @param num
	 *            usually 0 (index of the image to be exported for this diagram).
	 * @param fileFormat
	 *            file format to use
	 * 
	 * @return a description of the generated image
	 * 
	 * @throws IOException
	 */
	ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException;

	/**
	 * Number of images in this diagram (usually, 1)
	 * 
	 * @return usually 1
	 */
	int getNbImages();

	DiagramDescription getDescription();

	String getMetadata();

	String getWarningOrError();

	/**
	 * The original source of the diagram
	 * 
	 * @return
	 */
	UmlSource getSource();

	/**
	 * Check if the Diagram have some links.
	 * 
	 * @return
	 */
	public boolean hasUrl();

}
