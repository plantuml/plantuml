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

import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.anim.AnimationDecoder;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositioned;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public abstract class TitledDiagram extends AbstractPSystem implements Diagram, Annotated {

	public static boolean FORCE_SMETANA = false;
	public static boolean FORCE_ELK = false;

	private DisplayPositioned title = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

	private DisplayPositioned caption = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositioned legend = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private final DisplaySection header = DisplaySection.none();
	private final DisplaySection footer = DisplaySection.none();
	private Display mainFrame;
	private final UmlDiagramType type;

	private final SkinParam skinParam;

	private Animation animation;

	private final Pragma pragma = new Pragma();

	public Pragma getPragma() {
		return pragma;
	}

	public TitledDiagram(UmlSource source, UmlDiagramType type) {
		super(source);
		this.type = type;
		this.skinParam = SkinParam.create(type);
	}

	public final StyleBuilder getCurrentStyleBuilder() {
		return skinParam.getCurrentStyleBuilder();
	}

	public TitledDiagram(UmlSource source, UmlDiagramType type, ISkinSimple orig) {
		this(source, type);
		if (orig != null) {
			this.skinParam.copyAllFrom(orig);
		}
	}

	final public UmlDiagramType getUmlDiagramType() {
		return type;
	}

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

	final public void setTitle(DisplayPositioned title) {
		if (title.isNull() || title.getDisplay().isWhite()) {
			return;
		}
		this.title = title;
	}

	@Override
	final public DisplayPositioned getTitle() {
		return title;
	}

	final public void setMainFrame(Display mainFrame) {
		this.mainFrame = mainFrame;
	}

	final public void setCaption(DisplayPositioned caption) {
		this.caption = caption;
	}

	final public DisplayPositioned getCaption() {
		return caption;
	}

	final public DisplaySection getHeader() {
		return header;
	}

	final public DisplaySection getFooter() {
		return footer;
	}

	final public DisplayPositioned getLegend() {
		return legend;
	}

	public void setLegend(DisplayPositioned legend) {
		this.legend = legend;
	}

	final public Display getMainFrame() {
		return mainFrame;
	}

	private boolean useSmetana;
	private boolean useElk;

	public void setUseSmetana(boolean useSmetana) {
		this.useSmetana = useSmetana;
	}

	public void setUseElk(boolean useElk) {
		this.useElk = useElk;
	}

	public boolean isUseElk() {
		if (FORCE_ELK)
			return true;
		return this.useElk;
	}

	public boolean isUseSmetana() {
		if (FORCE_SMETANA)
			return true;
		return useSmetana;
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(10);
	}

	final public void setAnimation(Iterable<CharSequence> animationData) {
//		try {
		final AnimationDecoder animationDecoder = new AnimationDecoder(animationData);
		this.animation = Animation.create(animationDecoder.decode());
//		} catch (ScriptException e) {
//			e.printStackTrace();
//		}
	}

	final public Animation getAnimation() {
		return animation;
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).styled(this);
	}

	public HColor calculateBackColor() {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature.of(SName.root, SName.document, this.getUmlDiagramType().getStyleName())
					.getMergedStyle(this.getSkinParam().getCurrentStyleBuilder());

			HColor backgroundColor = style.value(PName.BackGroundColor).asColor(this.getSkinParam().getThemeStyle(),
					this.getSkinParam().getIHtmlColorSet());
			if (backgroundColor == null) {
				backgroundColor = HColorUtils.transparent();
			}
			return backgroundColor;

		}
		return this.getSkinParam().getBackgroundColor();
	}

}
