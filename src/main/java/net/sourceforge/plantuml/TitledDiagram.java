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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.api.ApiStable;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramChromeFactory12026;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.ColorOrder;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.pdf.PdfConverter;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SFile;
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

public abstract class TitledDiagram extends UgDiagram implements Annotated, WithSprite {

	private static final Pattern DIGITS = Pattern.compile("\\d+");
	private XDimension2D lastInfo;

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
	private String namespaceSeparator = null;

	public TitledDiagram(UmlSource source, UmlDiagramType type, Previous previous,
			PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.type = type;
		this.skinParam = SkinParam.create(source.getPathSystem(), type, Pragma.createEmpty(),
				preprocessing.getOption());
		if (previous != null)
			this.skinParam.copyAllFrom(previous);

	}

	@Override
	public boolean isHandwritten() {
		if (skinParam.handwritten())
			return true;
		return super.isHandwritten();
	}

	public void setNamespaceSeparator(String namespaceSeparator) {
		this.namespaceSeparator = namespaceSeparator;
	}

	final public String getNamespaceSeparator() {
		return namespaceSeparator;
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
	final public DisplayPositioned getTitle() {
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

	@Override
	public final HColor calculateBackColor() {
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

	protected abstract TextBlock getTextMainBlock01970(FileFormatOption fileFormatOption);

	@Override
	public void exportDiagramGraphic01970(UGraphic ug, FileFormatOption fileFormatOption) {
		try {
			final TextBlock textBlock = getTextMainBlock01970(fileFormatOption);
			createImageBuilder(fileFormatOption).drawable(textBlock).drawU(ug);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}
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

	public final DisplayPositioned getFooterOrHeaderTeoz(FontParam param) {
		if (param == FontParam.FOOTER)
			return getFooter();

		if (param == FontParam.HEADER)
			return getHeader();

		throw new IllegalArgumentException();
	}

//	@Override
//	final protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
//			throws IOException {
//
//		fileFormatOption = fileFormatOption.withTikzFontDistortion(getSkinParam().getTikzFontDistortion());
//		fileFormatOption.getTikzFontDistortion().updateFromPragma(getPragma());
//
//		if (!TeaVM.isTeaVM()) {
//			if (fileFormatOption.getFileFormat() == FileFormat.PDF)
//				return exportDiagramInternalPdf(os, index);
//		}
//
//		try {
//			final ImageData imageData = exportDiagramInternal(os, index, fileFormatOption);
//			this.lastInfo = new XDimension2D(imageData.getWidth(), imageData.getHeight());
//			return imageData;
//		} catch (NoStyleAvailableException e) {
//			Logme.error(e);
//			final CrashReportHandler report = new CrashReportHandler(null, getMetadata(), getFlashData());
//
//			report.add("There is an issue with your plantuml.jar file:");
//			report.add("We cannot load any style from it!");
//
//			report.checkOldVersionWarning();
//			report.addProperties();
//			report.addEmptyLine();
//
//			report.exportDiagramError(fileFormatOption, seed(), os);
//			return ImageDataSimple.error(e);
//		} catch (UnparsableGraphvizException e) {
//			Logme.error(e);
//			final CrashReportHandler report = new CrashReportHandler(e.getCause(), getMetadata(), getFlashData());
//
//			report.anErrorHasOccured(e.getCause(), getFlashData());
//			report.add("PlantUML (" + Version.versionString() + ") cannot parse result from dot/GraphViz.");
//			if (e.getCause() instanceof EmptySvgException)
//				report.add("Because dot/GraphViz returns an empty string.");
//
//			if (e.getGraphvizVersion() != null) {
//				report.addEmptyLine();
//				report.add("GraphViz version used : " + e.getGraphvizVersion());
//			}
//			report.pleaseCheckYourGraphVizVersion();
//			report.addProperties();
//			report.addEmptyLine();
//			report.thisMayBeCaused();
//			report.addEmptyLine();
//			report.youShouldSendThisDiagram();
//			report.addEmptyLine();
//
//			report.exportDiagramError(fileFormatOption, seed(), os);
//			return ImageDataSimple.error(e);
//		} catch (Throwable e) {
//			Logme.error(e);
//			final CrashReportHandler report = new CrashReportHandler(e, getMetadata(), getFlashData());
//			report.anErrorHasOccured(e, getFlashData());
//			report.addProperties();
//			report.addEmptyLine();
//			report.youShouldSendThisDiagram();
//			report.addEmptyLine();
//			report.exportDiagramError(fileFormatOption, seed(), os);
//			return ImageDataSimple.error(e);
//		}
//	}

	private ImageData exportDiagramInternalPdf(OutputStream os, int index) throws IOException {
		final File svg = FileUtils.createTempFileLegacy("pdf", ".svf");
		final File pdfFile = FileUtils.createTempFileLegacy("pdf", ".pdf");
		final ImageData result;
		try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(svg))) {
			result = exportDiagram(fos, index, new FileFormatOption(FileFormat.SVG));
		}
		PdfConverter.convert(svg, pdfFile);
		FileUtils.copyToStream(pdfFile, os);
		return result;
	}

	final protected void exportCmap(SuggestedFile suggestedFile, int index, final ImageData cmapdata)
			throws FileNotFoundException {
		final String name = changeName(suggestedFile.getFile(index).getAbsolutePath());
		final SFile cmapFile = new SFile(name);
		try (PrintWriter pw = cmapFile.createPrintWriter()) {
			if (PSystemUtils.canFileBeWritten(cmapFile) == false)
				return;

			pw.print(cmapdata.getCMapData(cmapFile.getName().substring(0, cmapFile.getName().length() - 6)));
		}
	}

	static String changeName(String name) {
		return name.replaceAll("(?i)\\.\\w{3}$", ".cmapx");
	}

	@Override
	public String getWarningOrError() {
		if (lastInfo == null)
			return null;

		final double actualWidth = lastInfo.getWidth();
		if (actualWidth == 0)
			return null;

		final String value = getSkinParam().getValue("widthwarning");
		if (value == null)
			return null;

		if (!DIGITS.matcher(value).matches())
			return null;

		final int widthwarning = Integer.parseInt(value);
		if (actualWidth > widthwarning)
			return "The image is " + ((int) actualWidth) + " pixel width. (Warning limit is " + widthwarning + ")";

		return null;
	}

	public void setHideEmptyDescription(boolean hideEmptyDescription) {
	}

	public Previous getPrevious() {
		return Previous.createFrom(getSkinParam().values());
	}

	@Override
	public TextBlock addChrome(TextBlock result, FileFormatOption fileFormatOption) {
		final TitledDiagram titledDiagram = (TitledDiagram) this;
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(titledDiagram.getSkinParam());
		result = DiagramChromeFactory12026.create(result, titledDiagram, titledDiagram.getSkinParam(), stringBounder,
				getWarnings());
		return result;
	}

}
