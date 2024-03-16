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

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.image.Opale;

public class FEntry implements Iterable<FEntry> {

	private final FEntry parent;
	private final List<String> note;
	private final String name;
	private FilesType type;
	private List<FEntry> children = new ArrayList<>();

	public static FEntry createRoot() {
		return new FEntry(null, FilesType.FOLDER, "", null);
	}

	private FEntry(FEntry parent, FilesType type, String name, List<String> note) {
		this.parent = parent;
		this.note = note;
		this.name = name;
		this.type = type;
	}

	public FEntry addRawEntry(String raw) {
		final int x = raw.indexOf('/');
		if (x == -1) {
			final FEntry result = new FEntry(this, FilesType.DATA, raw, null);
			children.add(result);
			return result;
		}
		final FEntry folder = getOrCreateFolder(raw.substring(0, x));
		final String remain = raw.substring(x + 1);
		if (remain.length() != 0)
			return folder.addRawEntry(remain);
		return null;
	}

	public void addNote(List<String> note) {
		final FEntry result = new FEntry(this, FilesType.NOTE, "NONE", note);
		children.add(result);
	}

	private FEntry getOrCreateFolder(String folderName) {
		for (FEntry child : children)
			if (child.type == FilesType.FOLDER && child.getName().equals(folderName))
				return child;

		final FEntry result = new FEntry(this, FilesType.FOLDER, folderName, null);
		children.add(result);
		return result;
	}

	@Override
	public Iterator<FEntry> iterator() {
		return Collections.unmodifiableCollection(children).iterator();
	}

	public FEntry getParent() {
		return parent;
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
		final TextBlock result = getTextBlock(fontConfiguration, skinParam);
		result.drawU(ug.apply(UTranslate.dx(deltax)));
		ug = ug.apply(UTranslate.dy(result.calculateDimension(ug.getStringBounder()).getHeight() + 2));
		for (FEntry child : children)
			ug = child.drawAndMove(ug, fontConfiguration, skinParam, deltax + 21);
		return ug;
	}

	private TextBlock getTextBlock(FontConfiguration fontConfiguration, ISkinParam skinParam) {
		if (type == FilesType.NOTE)
			return createOpale(skinParam);

		final Display display = Display.getWithNewlines(getEmoticon() + getName());
		TextBlock result = display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam,
				CreoleMode.NO_CREOLE);
		return result;
	}

	private Opale createOpale(ISkinParam skinParam) {

		final StyleSignatureBasic signature = StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram,
				SName.note);
		final Style style = signature.getMergedStyle(skinParam.getCurrentStyleBuilder());

		final FontConfiguration fc = FontConfiguration.create(skinParam, style);
		final double shadowing = style.value(PName.Shadowing).asDouble();
		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final UStroke stroke = style.getStroke();

		final Sheet sheet = skinParam
				.sheet(fc, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), CreoleMode.FULL)
				.createSheet(Display.create(note));
		final SheetBlock1 sheet1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		final Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, sheet1, false, stroke);
		return opale;
	}

}
