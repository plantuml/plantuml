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
package net.sourceforge.plantuml;

import java.io.File;

import net.sourceforge.plantuml.core.ImageData;

public class FileImageData {
	
	public static final int ERROR = 400;
	public static final int CRASH = 503;

	private final File file;
	private final ImageData imageData;

	public FileImageData(File file, ImageData imageData) {
		this.file = file;
		this.imageData = imageData;
	}

	public File getFile() {
		return file;
	}

	public ImageData getImageData() {
		return imageData;
	}

	public int getStatus() {
		if (imageData == null) {
			return 0;
		}
		return imageData.getStatus();
	}

}
