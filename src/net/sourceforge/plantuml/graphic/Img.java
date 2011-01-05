/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5697 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileSystem;

public class Img implements HtmlCommand {

	final static private Pattern srcPattern = Pattern.compile("(?i)src\\s*=\\s*[\"']?([^ \">]+)[\"']?");
	final static private Pattern vspacePattern = Pattern.compile("(?i)vspace\\s*=\\s*[\"']?(\\d+)[\"']?");
	final static private Pattern valignPattern = Pattern.compile("(?i)valign\\s*=\\s*[\"']?(top|bottom|middle)[\"']?");
	final static private Pattern srcPattern2 = Pattern.compile("(?i)" + Splitter.imgPattern2);

	private final TileImage tileImage;
	private final String filePath;

	private Img(TileImage image, String filePath) throws IOException {
		this.tileImage = image;
		this.filePath = filePath;
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

	static HtmlCommand getInstance(String html) {
		final Matcher m = srcPattern.matcher(html);
		if (m.find() == false) {
			return new Text("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				return new Text("(File not found: " + f + ")");
			}
			final int vspace = getVspace(html);
			final ImgValign valign = getValign(html);
			return new Img(new TileImage(ImageIO.read(f), valign, vspace), src);
		} catch (IOException e) {
			return new Text("ERROR " + e.toString());
		}
	}

	static HtmlCommand getInstance2(String html) {
		final Matcher m = srcPattern2.matcher(html);
		if (m.find() == false) {
			return new Text("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				return new Text("(File not found: " + f + ")");
			}
			return new Img(new TileImage(ImageIO.read(f), ImgValign.TOP, 0), src);
		} catch (IOException e) {
			return new Text("ERROR " + e.toString());
		}
	}

	public TileImage createMonoImage() {
		return tileImage;
	}

	public final String getFilePath() {
		return filePath;
	}
}
