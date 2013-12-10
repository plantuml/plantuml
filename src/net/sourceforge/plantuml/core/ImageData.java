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
 * Revision $Revision: 9885 $
 *
 */
package net.sourceforge.plantuml.core;

// Remove CmapData and Dimension2D
// Merge CucaDiagramFileMakerResult
/**
 * Information about a generated image for a diagram.
 * For some diagrams, there are some position information about elements
 * from the diagram. In that case, the method <code>containsCMapData()</code> returns
 * <code>true</code> and you can retrieve those information using
 * <code>getCMapData()</code> method.
 * 
 * @author Arnaud Roques
 * 
 */
public interface ImageData {

	/**
	 * Width in pixel of the image.
	 * @return
	 */
	public int getWidth();

	/**
	 * Height in pixel of the image.
	 * @return
	 */
	public int getHeight();

	/**
	 * Indicates if the image has some position information.
	 * @return <code>true</code> if the image has position information.
	 */
	public boolean containsCMapData();

	/**
	 * Return position information as a CMap formated string.
	 * For example, if you call this method with <code>nameId</code>
	 * set to "foo_map", you will get something like:
	 * 
	 * <code><pre>
	 * &lt;map id="foo_map" name="foo_map"&gt;
	 * &lt;area shape="rect" id="..." href="..." title="..." alt="" coords="64,68,93,148"/&gt;
	 * &lt;/map&gt;
	 * </pre></code>
	 * 
	 * @param nameId thie id to be used in the cmap data string.
	 * @return
	 */
	public String getCMapData(String nameId);
	
	public String getWarningOrError();


}
