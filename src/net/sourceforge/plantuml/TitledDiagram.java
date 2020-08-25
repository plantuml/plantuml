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

import java.io.IOException;

import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandSkinParamMultilines;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.sprite.Sprite;

public abstract class TitledDiagram extends AbstractPSystem implements Diagram, Annotated {

	private DisplayPositionned title = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

	private DisplayPositionned caption = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositionned legend = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private final DisplaySection header = DisplaySection.none();
	private final DisplaySection footer = DisplaySection.none();
	private Display mainFrame;

	private final SkinParam skinParam;

	public TitledDiagram() {
		this.skinParam = SkinParam.create(getUmlDiagramType());
	}

	public TitledDiagram(ISkinSimple orig) {
		this();
		if (orig != null) {
			this.skinParam.copyAllFrom(orig);
		}
	}

	abstract public UmlDiagramType getUmlDiagramType();

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public void setParam(String key, String value) {
		skinParam.setParam(StringUtils.goLowerCase(key), value);
	}

	public void addSprite(String name, Sprite sprite) {
		skinParam.addSprite(name, sprite);
	}

	public CommandExecutionResult loadSkin(String newSkin) throws IOException {
		getSkinParam().setDefaultSkin(newSkin + ".skin");
		return CommandExecutionResult.ok();
		// final String res = "/skin/" + filename + ".skin";
		// final InputStream internalIs = UmlDiagram.class.getResourceAsStream(res);
		// if (internalIs != null) {
		// final BlocLines lines2 = BlocLines.load(internalIs, new
		// LineLocationImpl(filename, null));
		// return loadSkinInternal(lines2);
		// }
		// if (OptionFlags.ALLOW_INCLUDE == false) {
		// return CommandExecutionResult.ok();
		// }
		// final File f = FileSystem.getInstance().getFile(filename + ".skin");
		// if (f == null || f.exists() == false || f.canRead() == false) {
		// return CommandExecutionResult.error("Cannot load skin from " + filename);
		// }
		// final BlocLines lines = BlocLines.load(f, new LineLocationImpl(f.getName(),
		// null));
		// return loadSkinInternal(lines);
	}

	// private CommandExecutionResult loadSkinInternal(final BlocLines lines) {
	// final CommandSkinParam cmd1 = new CommandSkinParam();
	// final CommandSkinParamMultilines cmd2 = new CommandSkinParamMultilines();
	// for (int i = 0; i < lines.size(); i++) {
	// final BlocLines ext1 = lines.subList(i, i + 1);
	// if (cmd1.isValid(ext1) == CommandControl.OK) {
	// cmd1.execute(this, ext1);
	// } else if (cmd2.isValid(ext1) == CommandControl.OK_PARTIAL) {
	// i = tryMultilines(cmd2, i, lines);
	// }
	// }
	// return CommandExecutionResult.ok();
	// }

//	private int tryMultilines(CommandSkinParamMultilines cmd2, int i, BlocLines lines) {
//		for (int j = i + 1; j <= lines.size(); j++) {
//			final BlocLines ext1 = lines.subList(i, j);
//			if (cmd2.isValid(ext1) == CommandControl.OK) {
//				cmd2.execute(this, ext1);
//				return j;
//			} else if (cmd2.isValid(ext1) == CommandControl.NOT_OK) {
//				return j;
//			}
//		}
//		return i;
//	}

	final public void setTitle(DisplayPositionned title) {
		if (title.isNull() || title.getDisplay().isWhite()) {
			return;
		}
		this.title = title;
	}

	@Override
	final public DisplayPositionned getTitle() {
		return title;
	}

	final public void setMainFrame(Display mainFrame) {
		this.mainFrame = mainFrame;
	}

	final public void setCaption(DisplayPositionned caption) {
		this.caption = caption;
	}

	final public DisplayPositionned getCaption() {
		return caption;
	}

	final public DisplaySection getHeader() {
		return header;
	}

	final public DisplaySection getFooter() {
		return footer;
	}

	final public DisplayPositionned getLegend() {
		return legend;
	}

	public void setLegend(DisplayPositionned legend) {
		this.legend = legend;
	}

	final public Display getMainFrame() {
		return mainFrame;
	}

}
