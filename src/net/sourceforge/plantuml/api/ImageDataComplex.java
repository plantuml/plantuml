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
package net.sourceforge.plantuml.api;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.core.ImageData;

public class ImageDataComplex implements ImageData {

	private final Dimension2D info;
	private final CMapData cmap;
	private final String warningOrError;

//	public ImageDataComplex(Dimension2D info, CMapData cmap) {
//		this(info, cmap, null);
//	}

	public ImageDataComplex(Dimension2D info, CMapData cmap, String warningOrError) {
		if (info==null) {
			throw new IllegalArgumentException();
		}
		this.info = info;
		this.cmap = cmap;
		this.warningOrError = warningOrError;
	}

	public int getWidth() {
		return (int) info.getWidth();
	}

	public int getHeight() {
		return (int) info.getHeight();
	}

	public boolean containsCMapData() {
		return cmap != null && cmap.containsData();
	}

	public String getCMapData(String nameId) {
		return cmap.asString(nameId);
	}

	public String getWarningOrError() {
		return warningOrError;
	}

}
