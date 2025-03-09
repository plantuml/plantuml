/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.security;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class SImageIO {

	// ::comment when __CORE__
	public static ImageOutputStream createImageOutputStream(OutputStream os) throws IOException {
		return javax.imageio.ImageIO.createImageOutputStream(os);
	}
	// ::done

	public static void write(RenderedImage image, String format, OutputStream os) throws IOException {
		javax.imageio.ImageIO.write(image, format, os);
	}

	// ::comment when __CORE__
	public static void write(RenderedImage image, String format, SFile file) throws IOException {
		javax.imageio.ImageIO.write(image, format, file.conv());
	}

	public static BufferedImage read(java.io.File file) throws IOException {
		return javax.imageio.ImageIO.read(file);
	}

	public static BufferedImage read(SFile file) throws IOException {
		return javax.imageio.ImageIO.read(file.conv());
	}
	// ::done

	public static BufferedImage read(InputStream is) throws IOException {
		return javax.imageio.ImageIO.read(is);
	}

	public static BufferedImage read(byte[] bytes) throws IOException {
		return javax.imageio.ImageIO.read(new ByteArrayInputStream(bytes));
	}

	// ::comment when __CORE__
	public static ImageInputStream createImageInputStream(SFile file) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(file.conv());
	}

	public static ImageInputStream createImageInputStream(Object obj) throws IOException {
		if (obj instanceof SFile)
			obj = ((SFile) obj).conv();

		return javax.imageio.ImageIO.createImageInputStream(obj);
	}

	public static ImageInputStream createImageInputStream(InputStream is) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(is);
	}

	public static Iterator<ImageReader> getImageReaders(ImageInputStream iis) {
		return javax.imageio.ImageIO.getImageReaders(iis);
	}

	public static Iterator<ImageWriter> getImageWritersBySuffix(String string) {
		return javax.imageio.ImageIO.getImageWritersBySuffix(string);
	}
	// ::done

}
