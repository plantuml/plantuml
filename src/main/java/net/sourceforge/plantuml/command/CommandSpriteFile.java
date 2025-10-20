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
package net.sourceforge.plantuml.command;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.emoji.SpriteSvgNanoParser;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.klimt.sprite.SpriteImage;
import net.sourceforge.plantuml.klimt.sprite.SpriteSvg;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Log;

public class CommandSpriteFile extends SingleLineCommand2<TitledDiagram> {

	public static final CommandSpriteFile ME = new CommandSpriteFile();

	private CommandSpriteFile() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSpriteFile.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("sprite"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("\\$?"), //
				new RegexLeaf(1, "NAME", "([-%pLN_]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "FILE", "([^<>%g#]*)"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram system, LineLocation location, RegexResult arg, ParserPass currentPass) {
		// ::comment when __CORE__
		final String src = arg.get("FILE", 0);
		final Sprite sprite;
		try {
			if (src.startsWith("jar:")) {
				String inner;
				InputStream is;
				final String name = src.substring(4);
				inner =  name + ".svg";
				is = SpriteSvgNanoParser.getInternalSprite(inner);
				if (is == null) {
					inner = name + ".png";
					is = SpriteImage.getInternalSprite(inner);
					if (is == null)
						return CommandExecutionResult.error("No such internal sprite: " + name);
				}
				sprite = new SpriteImage(SImageIO.read(is));
			} else if (src.contains("~")) {
				final int idx = src.lastIndexOf("~");
				final SFile f = FileSystem.getInstance().getFile(src.substring(0, idx));
				if (f.exists() == false)
					return CommandExecutionResult.error("Cannot read: " + src);

				final String name = src.substring(idx + 1);
				sprite = getImageFromZip(f, name);
				if (sprite == null)
					return CommandExecutionResult.error("Cannot read: " + src);

			} else {
				final SFile f = FileSystem.getInstance().getFile(src);
				if (f.exists() == false)
					return CommandExecutionResult.error("Cannot read: " + src);

				if (isSvg(f.getName())) {
					final String tmp = FileUtils.readSvg(f);
					if (tmp == null)
						return CommandExecutionResult.error("Cannot read: " + src);

					sprite = new SpriteSvg(tmp);
				} else {
					final BufferedImage tmp = f.readRasterImageFromFile();
					if (tmp == null)
						return CommandExecutionResult.error("Cannot read: " + src);

					sprite = new SpriteImage(tmp);
				}
			}
		} catch (IOException e) {
			Log.error("Error reading " + src + " " + e);
			return CommandExecutionResult.error("Cannot read: " + src);
		}
		system.addSprite(arg.get("NAME", 0), sprite);
		// ::done
		return CommandExecutionResult.ok();
	}

	// ::comment when __CORE__
	private Sprite getImageFromZip(SFile f, String name) throws IOException {
		final InputStream tmp = f.openFile();
		if (tmp == null) {
			return null;
		}
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(tmp);
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				final String fileName = ze.getName();
				if (ze.isDirectory()) {
				} else if (fileName.equals(name)) {
					if (isSvg(name))
						return new SpriteSvg(FileUtils.readSvg(zis));
					else
						return new SpriteImage(SImageIO.read(zis));

				}
				ze = zis.getNextEntry();
			}
		} finally {
			if (zis != null) {
				zis.closeEntry();
				zis.close();
			}
		}
		return null;
	}

	private boolean isSvg(String name) {
		return name.toLowerCase().endsWith(".svg");
	}
	// ::done
}
