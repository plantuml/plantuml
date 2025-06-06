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
package net.sourceforge.plantuml.klimt.creole.command;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.geom.ImgValign;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TileImage;
import net.sourceforge.plantuml.klimt.shape.TileImageSvg;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;

public class Img implements HtmlCommand {

	final static private Pattern2 srcPattern = Pattern2.cmpile("src[%s]*=[%s]*[\"%q]?([^%s\">]+)[\"%q]?");
	final static private Pattern2 vspacePattern = Pattern2.cmpile("vspace[%s]*=[%s]*[\"%q]?(\\d+)[\"%q]?");
	final static private Pattern2 valignPattern = Pattern2.cmpile("valign[%s]*=[%s]*[\"%q]?(top|bottom|middle)[\"%q]?");
	final static private Pattern2 noSrcColonPattern = Pattern2.cmpile(Splitter.imgPatternNoSrcColon);

	private final TextBlock tileImage;

	private Img(TextBlock image) {
		this.tileImage = image;
	}

	static int getVspace(String html) {
		final Matcher2 m = vspacePattern.matcher(html);
		if (m.find() == false)
			return 0;

		return Integer.parseInt(m.group(1));
	}

	static ImgValign getValign(String html) {
		final Matcher2 m = valignPattern.matcher(html);
		if (m.find() == false)
			return ImgValign.TOP;

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
			return new PlainText("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final SFile f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
					final SURL tmp = SURL.create(src);
					if (tmp == null)
						return new PlainText("(Cannot decode: " + src + ")");

					final BufferedImage read = tmp.readRasterImageFromURL();
					if (read == null)
						return new PlainText("(Cannot decode: " + src + ")");

					return new Img(new TileImage(read, valign, vspace));
				}
				return new PlainText("(Cannot decode: " + f + ")");
			}
			if (f.getName().endsWith(".svg")) {
				final String tmp = FileUtils.readSvg(f);
				if (tmp == null)
					return new PlainText("(Cannot decode: " + f + ")");

				return new Img(new TileImageSvg(tmp, 1));
			}
			final BufferedImage read = f.readRasterImageFromFile();
			if (read == null)
				return new PlainText("(Cannot decode: " + f + ")");

			return new Img(new TileImage(f.readRasterImageFromFile(), valign, vspace));
		} catch (IOException e) {
			Logme.error(e);
			return new PlainText("ERROR " + e.toString());
		}
	}

	public TextBlock createMonoImage() {
		return tileImage;
	}

}
