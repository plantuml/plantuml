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
 * Revision $Revision: 11788 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileSystem;

public class Img implements HtmlCommand {

	final static private Pattern srcPattern = Pattern.compile("(?i)src\\s*=\\s*[\"']?([^ \">]+)[\"']?");
	final static private Pattern vspacePattern = Pattern.compile("(?i)vspace\\s*=\\s*[\"']?(\\d+)[\"']?");
	final static private Pattern valignPattern = Pattern.compile("(?i)valign\\s*=\\s*[\"']?(top|bottom|middle)[\"']?");
	final static private Pattern noSrcColonPattern = Pattern.compile("(?i)" + Splitter.imgPatternNoSrcColon);

	private final TextBlock tileImage;

	private Img(TextBlock image) {
		this.tileImage = image;
	}

	static int getVspace(String html) {
		final Matcher m = vspacePattern.matcher(html);
		if (m.find() == false) {
			return 0;
		}
		return Integer.parseInt(m.group(1));
	}

	static ImgValign getValign(String html) {
		final Matcher m = valignPattern.matcher(html);
		if (m.find() == false) {
			return ImgValign.TOP;
		}
		return ImgValign.valueOf(m.group(1).toUpperCase());
	}

	static HtmlCommand getInstance(String html, boolean withSrc) {
		if (withSrc) {
			final Matcher m = srcPattern.matcher(html);
			final int vspace = getVspace(html);
			final ImgValign valign = getValign(html);
			return build(m, valign, vspace);
		}
		final Matcher m = noSrcColonPattern.matcher(html);
		return build(m, ImgValign.TOP, 0);
	}

	private static HtmlCommand build(final Matcher m, final ImgValign valign, final int vspace) {
		if (m.find() == false) {
			return new Text("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
					final byte image[] = getFile(src);
					final BufferedImage read = ImageIO.read(new ByteArrayInputStream(image));
					if (read == null) {
						return new Text("(Cannot decode: " + src + ")");
					}
					return new Img(new TileImage(read, valign, vspace));
				}
				return new Text("(File not found: " + f + ")");
			}
			if (f.getName().endsWith(".svg")) {
				return new Img(new TileImageSvg(f));
			}
			final BufferedImage read = ImageIO.read(f);
			if (read == null) {
				return new Text("(Cannot decode: " + f + ")");
			}
			return new Img(new TileImage(ImageIO.read(f), valign, vspace));
		} catch (IOException e) {
			return new Text("ERROR " + e.toString());
		}
	}

	public TextBlock createMonoImage() {
		return tileImage;
	}

	// Added by Alain Corbiere
	static byte[] getFile(String host) throws IOException {
		final ByteArrayOutputStream image = new ByteArrayOutputStream();
		InputStream input = null;
		try {
			final URL url = new URL(host);
			final URLConnection connection = url.openConnection();
			input = connection.getInputStream();
			final byte[] buffer = new byte[1024];
			int read;
			while ((read = input.read(buffer)) > 0) {
				image.write(buffer, 0, read);
			}
			image.close();
			return image.toByteArray();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
	// End

}
