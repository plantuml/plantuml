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
 */
package net.sourceforge.plantuml.filesdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;

public class FilesEntry implements Iterable<FilesEntry> {

	private final String name;
	private FilesType type;
	private List<FilesEntry> children = new ArrayList<>();

	public FilesEntry(String name, FilesType type) {
		this.name = name;
		this.type = type;
	}

	public FilesEntry addRawEntry(String raw) {
		final int x = raw.indexOf('/');
		if (x == -1) {
			final FilesEntry result = new FilesEntry(raw, FilesType.DATA);
			children.add(result);
			return result;
		}
		final FilesEntry folder = getOrCreateFolder(raw.substring(0, x));
		final String remain = raw.substring(x + 1);
		if (remain.length() == 0)
			return folder;
		return folder.addRawEntry(remain);
	}

	private FilesEntry getOrCreateFolder(String folderName) {
		for (FilesEntry child : children)
			if (child.type == FilesType.FOLDER && child.getName().equals(folderName))
				return child;

		final FilesEntry result = new FilesEntry(folderName, FilesType.FOLDER);
		children.add(result);
		return result;
	}

	@Override
	public Iterator<FilesEntry> iterator() {
		return Collections.unmodifiableCollection(children).iterator();
	}

	public String getName() {
		return name;
	}

	public String getEmoticon() {
		if (type == FilesType.FOLDER)
			return "<:1f4c2:>";
		// return "<:1f4c1:>";
		return "<:1f4c4:>";
	}

	public UGraphic drawAndMove(UGraphic ug, FontConfiguration fontConfiguration, ISkinParam skinParam, double deltax) {
		final Display display = Display.getWithNewlines(getEmoticon() + getName());
		TextBlock result = display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
		result.drawU(ug.apply(UTranslate.dx(deltax)));
		ug = ug.apply(UTranslate.dy(result.calculateDimension(ug.getStringBounder()).getHeight() + 2));
		for (FilesEntry child : children)
			ug = child.drawAndMove(ug, fontConfiguration, skinParam, deltax + 21);
		return ug;
	}

}
