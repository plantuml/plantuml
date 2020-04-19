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
package net.sourceforge.plantuml.graphic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class Img implements HtmlCommand {

	final static private Pattern2 srcPattern = MyPattern.cmpile("(?i)src[%s]*=[%s]*[\"%q]?([^%s\">]+)[\"%q]?");
	final static private Pattern2 vspacePattern = MyPattern.cmpile("(?i)vspace[%s]*=[%s]*[\"%q]?(\\d+)[\"%q]?");
	final static private Pattern2 valignPattern = MyPattern.cmpile("(?i)valign[%s]*=[%s]*[\"%q]?(top|bottom|middle)[\"%q]?");
	final static private Pattern2 noSrcColonPattern = MyPattern.cmpile("(?i)" + Splitter.imgPatternNoSrcColon);

	private final TextBlock tileImage;

	private Img(TextBlock image) {
		this.tileImage = image;
	}

	static int getVspace(String html) {
		final Matcher2 m = vspacePattern.matcher(html);
		if (m.find() == false) {
			return 0;
		}
		return Integer.parseInt(m.group(1));
	}

	static ImgValign getValign(String html) {
		final Matcher2 m = valignPattern.matcher(html);
		if (m.find() == false) {
			return ImgValign.TOP;
		}
		return ImgValign.valueOf(StringUtils.goUpperCase(m.group(1)));
	}

	static HtmlCommand getInstance(String html, boolean withSrc) {
		if (withSrc) {
			final Matcher2 m = srcPattern.matcher(html);
			final int vspace = getVspace(html);
			final ImgValign valign = getValign(html);
			return build(m, valign, vspace);
		}
		final Matcher2 m = noSrcColonPattern.matcher(html);
		return build(m, ImgValign.TOP, 0);
	}

	private static HtmlCommand build(final Matcher2 m, final ImgValign valign, final int vspace) {
		if (m.find() == false) {
			return new Text("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
//					final byte image[] = getFile(src);
//					final BufferedImage read = ImageIO.read(new ByteArrayInputStream(image));
					final BufferedImage read = FileUtils.readRasterImageFromURL(new URL(src));
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
			final BufferedImage read = FileUtils.readRasterImageFromFile(f);
			if (read == null) {
				return new Text("(Cannot decode: " + f + ")");
			}
			return new Img(new TileImage(FileUtils.readRasterImageFromFile(f), valign, vspace));
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
