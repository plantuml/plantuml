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
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.abel.DisplayPositionned;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.api.ApiStable;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.ColorOrder;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.warning.Warning;

public abstract class TitledDiagram extends AbstractPSystem implements Diagram, Annotated {
	// ::remove file when __HAXE__

	public static boolean FORCE_SMETANA = false;
	public static boolean FORCE_ELK = false;

	private DisplayPositioned title = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

	private DisplayPositioned caption = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositioned legend = DisplayPositioned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositioned header = DisplayPositioned.none(HorizontalAlignment.CENTER, null);
	private DisplayPositioned footer = DisplayPositioned.none(HorizontalAlignment.CENTER, null);
	private DisplayPositioned mainFrame = DisplayPositioned.none(null, null);
	private final UmlDiagramType type;

	private final SkinParam skinParam;

	public TitledDiagram(UmlSource source, UmlDiagramType type, Previous previous,
			PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.type = type;
		this.skinParam = SkinParam.create(type, Pragma.createEmpty(), preprocessing.getOption());
		if (previous != null)
			this.skinParam.copyAllFrom(previous);

	}

	public final StyleBuilder getCurrentStyleBuilder() {
		return skinParam.getCurrentStyleBuilder();
	}

	final public UmlDiagramType getUmlDiagramType() {
		return type;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	private boolean skinParamUsed;

	public boolean isSkinParamUsed() {
		return skinParamUsed;
	}

	public void setSkinParamUsed(boolean skinParamUsed) {
		this.skinParamUsed = skinParamUsed;
	}

	public void setParam(String key, String value) {
		skinParam.setParam(StringUtils.goLowerCase(key), value);
	}

	public void addSprite(String name, Sprite sprite) {
		skinParam.addSprite(name, sprite);
	}

	@DuplicateCode(reference = "StyleExtractor")
	public CommandExecutionResult loadSkin(String newSkin) throws IOException {
		final String filename = newSkin + ".skin";
		final InputStream is = StyleLoader.getInputStreamForStyle(filename);
		if (is == null)
			return CommandExecutionResult.error("Cannot find style " + newSkin);
		is.close();

		getSkinParam().setDefaultSkin(filename);
		return CommandExecutionResult.ok();
	}

	final public void setTitle(DisplayPositioned title) {
		if (title.isNull() || title.getDisplay().isWhite())
			return;
		this.title = title;
	}

	@Override
	final public DisplayPositionned getTitle() {
		return title;
	}

	@Override
	@ApiStable
	final public Display getTitleDisplay() {
		if (title == null)
			return Display.NULL;
		return title.getDisplay();
	}

	final public void setMainFrame(DisplayPositioned mainFrame) {
		this.mainFrame = mainFrame;
	}

	final public void setCaption(DisplayPositioned caption) {
		this.caption = caption;
	}

	@Override
	final public DisplayPositioned getCaption() {
		return caption;
	}

	@Override
	final public DisplayPositioned getHeader() {
		return header;
	}

	@Override
	final public DisplayPositioned getFooter() {
		return footer;
	}

	public void updateFooter(LineLocation location, Display display, HorizontalAlignment horizontalAlignment) {
		this.footer = this.footer.withDisplay(display).withHorizontalAlignment(horizontalAlignment)
				.withLocation(location);
	}

	public void updateHeader(LineLocation location, Display display, HorizontalAlignment horizontalAlignment) {
		this.header = this.header.withDisplay(display).withHorizontalAlignment(horizontalAlignment)
				.withLocation(location);
	}

	@Override
	final public DisplayPositioned getLegend() {
		return legend;
	}

	public void setLegend(DisplayPositioned legend) {
		this.legend = legend;
	}

	@Override
	final public DisplayPositioned getMainFrame() {
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
		// return true;
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(10);
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).styled(this);
	}

	public HColor calculateBackColor() {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document, this.getUmlDiagramType().getStyleName())
				.getMergedStyle(this.getSkinParam().getCurrentStyleBuilder());

		HColor backgroundColor = style.value(PName.BackGroundColor).asColor(this.getSkinParam().getIHtmlColorSet());
		if (backgroundColor == null)
			backgroundColor = HColors.transparent();

		return backgroundColor;
	}

	@Override
	protected ColorMapper muteColorMapper(ColorMapper init) {
		if (SkinParam.isDark(getSkinParam()))
			return ColorMapper.DARK_MODE;
		final String monochrome = getSkinParam().getValue("monochrome");
		if ("true".equals(monochrome))
			return ColorMapper.MONOCHROME;
		if ("reverse".equals(monochrome))
			return ColorMapper.MONOCHROME_REVERSE;

		final String reversecolor = getSkinParam().getValue("reversecolor");
		if (reversecolor == null)
			return init;

		if ("dark".equalsIgnoreCase(reversecolor))
			return ColorMapper.LIGTHNESS_INVERSE;

		final ColorOrder order = ColorOrder.fromString(reversecolor);
		if (order == null)
			return init;

		return ColorMapper.reverse(order);

	}

	protected abstract TextBlock getTextMainBlock(FileFormatOption fileFormatOption);

	@Override
	public void exportDiagramGraphic(UGraphic ug, FileFormatOption fileFormatOption) {
		final TextBlock textBlock = getTextMainBlock(fileFormatOption);
		textBlock.drawU(ug);
	}

	final public Pragma getPragma() {
		return skinParam.getPragma();
	}

	@Override
	public void addWarning(Warning warning) {
		getPragma().addWarning(warning);
	}

	@Override
	public Collection<Warning> getWarnings() {
		return join(getPreprocessingArtifact().getWarnings(), getPragma().getWarnings());
	}

	static private Collection<Warning> join(Collection<Warning> col1, Collection<Warning> col2) {
		final LinkedHashSet<Warning> result = new LinkedHashSet<>();
		result.addAll(col1);
		result.addAll(col2);
		return result;
	}

	public String getFlashData() {
		final UmlSource source = getSource();
		if (source == null)
			return "";

		return source.getPlainString("\n");
	}

}
