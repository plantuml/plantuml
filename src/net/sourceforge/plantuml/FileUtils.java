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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;

// Used by the Eclipse Plugin, so do not change package location.
public class FileUtils {

	private static AtomicInteger counter;

	public static void resetCounter() {
		counter = new AtomicInteger(0);
	}

	static public File createTempFile(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false) {
			throw new IllegalArgumentException();
		}
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		final File f;
		if (counter == null) {
			f = File.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new File(name);
		}
		Log.info("Creating temporary file: " + f);
		f.deleteOnExit();
		return f;
	}

	private static void copyInternal(final InputStream fis, final OutputStream fos) throws IOException {
		final byte[] buf = new byte[10240];
		int len;
		while ((len = fis.read(buf)) > 0) {
			fos.write(buf, 0, len);
		}
		fos.close();
		fis.close();
	}

	static public void copyToFile(File src, File dest) throws IOException {
		if (dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(dest));
		copyInternal(fis, fos);
	}

	static public void copyToStream(File src, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToStream(InputStream is, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(is);
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToFile(byte[] src, File dest) throws IOException {
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(dest));
		fos.write(src);
		fos.close();
	}

	static public String readSvg(File svgFile) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(svgFile));
		return readSvg(br, false, true);
	}

	static public String readSvg(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return readSvg(br, false, false);
	}

	static public String readFile(File svgFile) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(svgFile));
		return readSvg(br, true, true);
	}

	private static String readSvg(final BufferedReader br, boolean withNewline, boolean withClose) throws IOException {
		final StringBuilder sb = new StringBuilder();
		String s;
		while ((s = br.readLine()) != null) {
			sb.append(s);
			if (withNewline) {
				sb.append("\n");
			}
		}
		if (withClose) {
			br.close();
		}
		return sb.toString();
	}

	// public static BufferedImage ImageIO_read(File f) throws IOException {
	// return ImageIO.read(f);
	// }

	// http://forum.plantuml.net/9048/img-tag-for-sequence-diagram-participants-does-always-render
	
	public synchronized static BufferedImage readRasterImageFromFile(File f) {
		// https://www.experts-exchange.com/questions/26171948/Why-are-ImageIO-read-images-losing-their-transparency.html
		// https://stackoverflow.com/questions/18743790/can-java-load-images-with-transparency

		try {
			return readRasterImage(new ImageIcon(f.getAbsolutePath()));
		} catch (Exception e) {
			return null;
		}
	}

	public synchronized static BufferedImage readRasterImageFromURL(URL url) {
		try {
			return readRasterImage(new ImageIcon(url));
		} catch (Exception e) {
			return null;
		}
	}

	private synchronized static BufferedImage readRasterImage(final ImageIcon imageIcon) {
		final Image tmpImage = imageIcon.getImage();
		final BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(tmpImage, 0, 0, null);
		tmpImage.flush();
		return image;
	}

}
