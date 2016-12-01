/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.anim.AnimationDecoder;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.UnparsableGraphvizException;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.fun.IconLoader;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.mjpeg.MJPEGGenerator;
import net.sourceforge.plantuml.pdf.PdfConverter;
import net.sourceforge.plantuml.svek.EmptySvgException;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;
import net.sourceforge.plantuml.version.Version;

public abstract class UmlDiagram extends AbstractPSystem implements Diagram, Annotated {

	private boolean rotation;
	private boolean hideUnlinkedData;

	private int minwidth = Integer.MAX_VALUE;

	private DisplayPositionned title = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	private DisplayPositionned caption = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositionned header = DisplayPositionned.none(HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
	private DisplayPositionned footer = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositionned legend = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

	private final Pragma pragma = new Pragma();
	private Scale scale;
	private Animation animation;

	private final SkinParam skinParam = new SkinParam();

	final public void setTitle(DisplayPositionned title) {
		this.title = title;
	}

	final public void setCaption(DisplayPositionned caption) {
		this.caption = caption;
	}

	final public DisplayPositionned getCaption() {
		return caption;
	}

	@Override
	final public DisplayPositionned getTitle() {
		return title;
	}

	final public int getMinwidth() {
		return minwidth;
	}

	final public void setMinwidth(int minwidth) {
		this.minwidth = minwidth;
	}

	final public boolean isRotation() {
		return rotation;
	}

	final public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public void setParam(String key, String value) {
		skinParam.setParam(StringUtils.goLowerCase(key), value);
	}

	public final DisplayPositionned getHeader() {
		return header;
	}

	public final void setHeader(DisplayPositionned header) {
		this.header = header;
	}

	public final DisplayPositionned getFooter() {
		return footer;
	}

	public final void setFooter(DisplayPositionned footer) {
		this.footer = footer;
	}

	public final DisplayPositionned getFooterOrHeaderTeoz(FontParam param) {
		if (param == FontParam.FOOTER) {
			return getFooter();
		}
		if (param == FontParam.HEADER) {
			return getHeader();
		}
		throw new IllegalArgumentException();
	}

	abstract public UmlDiagramType getUmlDiagramType();

	public Pragma getPragma() {
		return pragma;
	}

	final public void setScale(Scale scale) {
		this.scale = scale;
	}

	final public Scale getScale() {
		return scale;
	}

	final public void setAnimation(Iterable<CharSequence> animationData) {
		try {
			final AnimationDecoder animationDecoder = new AnimationDecoder(animationData);
			this.animation = Animation.create(animationDecoder.decode());
		} catch (ScriptException e) {
			e.printStackTrace();
		}

	}

	final public Animation getAnimation() {
		return animation;
	}

	public final double getDpiFactor(FileFormatOption fileFormatOption) {
		if (getSkinParam().getDpi() == 96) {
			return 1.0;
		}
		return getSkinParam().getDpi() / 96.0;
	}

	public final int getDpi(FileFormatOption fileFormatOption) {
		return getSkinParam().getDpi();
	}

	public final boolean isHideUnlinkedData() {
		return hideUnlinkedData;
	}

	public final void setHideUnlinkedData(boolean hideUnlinkedData) {
		this.hideUnlinkedData = hideUnlinkedData;
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		fileFormatOption = fileFormatOption.withSvgLinkTarget(getSkinParam().getSvgLinkTarget());

		if (fileFormatOption.getFileFormat() == FileFormat.PDF) {
			return exportDiagramInternalPdf(os, index);
		}

		try {
			final ImageData imageData = exportDiagramInternal(os, index, fileFormatOption);
			this.lastInfo = new Dimension2DDouble(imageData.getWidth(), imageData.getHeight());
			return imageData;
		} catch (UnparsableGraphvizException e) {
			e.printStackTrace();
			exportDiagramError(os, e.getCause(), fileFormatOption, e.getGraphvizVersion());
		} catch (Exception e) {
			e.printStackTrace();
			exportDiagramError(os, e, fileFormatOption, null);
		}
		return new ImageDataSimple();
	}

	public void exportDiagramError(OutputStream os, Throwable exception, FileFormatOption fileFormat,
			String graphvizVersion) throws IOException {
		exportDiagramError(os, exception, fileFormat, getMetadata(), getFlashData(),
				getFailureText1(exception, graphvizVersion));
	}

	public static void exportDiagramError(OutputStream os, Throwable exception, FileFormatOption fileFormat,
			String metadata, String flash, List<String> strings) throws IOException {

		if (fileFormat.getFileFormat() == FileFormat.ATXT || fileFormat.getFileFormat() == FileFormat.UTXT) {
			exportDiagramErrorText(os, exception, strings);
			return;
		}

		strings.addAll(CommandExecutionResult.getStackTrace(exception));

		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				metadata, null, 0, 0, null, false);

		final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
		final BufferedImage im = utils.exportFlashcode(flash);
		if (im != null) {
			GraphvizCrash.addDecodeHint(strings);
		}

		final TextBlockBackcolored graphicStrings = GraphicStrings.createBlackOnWhite(strings, IconLoader.getRandom(),
				GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);

		if (im == null) {
			imageBuilder.setUDrawable(graphicStrings);
		} else {
			imageBuilder.setUDrawable(new UDrawable() {
				public void drawU(UGraphic ug) {
					graphicStrings.drawU(ug);
					final double height = graphicStrings.calculateDimension(ug.getStringBounder()).getHeight();
					ug = ug.apply(new UTranslate(0, height));
					ug.draw(new UImage(im));
				}
			});
		}
		imageBuilder.writeImageTOBEMOVED(fileFormat, os);
	}

	private static void exportDiagramErrorText(OutputStream os, Throwable exception, List<String> strings) {
		final PrintWriter pw = new PrintWriter(os);
		exception.printStackTrace(pw);
		pw.println();
		pw.println();
		for (String s : strings) {
			s = s.replaceAll("\\</?\\w+?\\>", "");
			pw.println(s);
		}
		pw.flush();
	}

	public String getFlashData() {
		final UmlSource source = getSource();
		if (source == null) {
			return "";
		}
		return source.getPlainString();
	}

	static private List<String> getFailureText1(Throwable exception, String graphvizVersion) {
		final List<String> strings = GraphvizCrash.anErrorHasOccured(exception);
		strings.add("PlantUML (" + Version.versionString() + ") cannot parse result from dot/GraphViz.");
		if (exception instanceof EmptySvgException) {
			strings.add("Because dot/GraphViz returns an empty string.");
		}
		GraphvizCrash.checkOldVersionWarning(strings);
		if (graphvizVersion != null) {
			strings.add(" ");
			strings.add("GraphViz version used : " + graphvizVersion);
		}
		GraphvizCrash.pleaseGoTo(strings);
		GraphvizCrash.addProperties(strings);
		strings.add(" ");
		GraphvizCrash.thisMayBeCaused(strings);
		strings.add(" ");
		GraphvizCrash.youShouldSendThisDiagram(strings);
		strings.add(" ");
		return strings;
	}

	public static List<String> getFailureText2(Throwable exception) {
		final List<String> strings = GraphvizCrash.anErrorHasOccured(exception);
		strings.add("PlantUML (" + Version.versionString() + ") has crashed.");
		GraphvizCrash.checkOldVersionWarning(strings);
		strings.add(" ");
		GraphvizCrash.youShouldSendThisDiagram(strings);
		strings.add(" ");
		return strings;
	}

	private void exportDiagramInternalMjpeg(OutputStream os) throws IOException {
		final File f = new File("c:/test.avi");
		final int nb = 150;
		final double framerate = 30;
		final MJPEGGenerator m = new MJPEGGenerator(f, 640, 480, framerate, nb);

		for (int i = 0; i < nb; i++) {
			final AffineTransform at = new AffineTransform();
			final double coef = (nb - 1 - i) * 1.0 / nb;
			at.setToShear(coef, coef);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// exportDiagramTOxxBEREMOVED(baos, null, 0, new FileFormatOption(FileFormat.PNG, at));
			baos.close();
			final BufferedImage im = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
			m.addImage(im);
		}
		m.finishAVI();

	}

	private Dimension2D lastInfo;

	private ImageData exportDiagramInternalPdf(OutputStream os, int index) throws IOException {
		final File svg = FileUtils.createTempFile("pdf", ".svf");
		final File pdfFile = FileUtils.createTempFile("pdf", ".pdf");
		final OutputStream fos = new BufferedOutputStream(new FileOutputStream(svg));
		final ImageData result = exportDiagram(fos, index, new FileFormatOption(FileFormat.SVG));
		fos.close();
		PdfConverter.convert(svg, pdfFile);
		FileUtils.copyToStream(pdfFile, os);
		return result;
	}

	protected abstract ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException;

	final protected void exportCmap(File suggestedFile, final ImageData cmapdata) throws FileNotFoundException {
		final String name = changeName(suggestedFile.getAbsolutePath());
		final File cmapFile = new File(name);
		PrintWriter pw = null;
		try {
			if (PSystemUtils.canFileBeWritten(cmapFile) == false) {
				return;
			}
			pw = new PrintWriter(cmapFile);
			pw.print(cmapdata.getCMapData(cmapFile.getName().substring(0, cmapFile.getName().length() - 6)));
			pw.close();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	static String changeName(String name) {
		return name.replaceAll("(?i)\\.\\w{3}$", ".cmapx");
	}

	@Override
	public String getWarningOrError() {
		if (lastInfo == null) {
			return null;
		}
		final double actualWidth = lastInfo.getWidth();
		if (actualWidth == 0) {
			return null;
		}
		final String value = getSkinParam().getValue("widthwarning");
		if (value == null) {
			return null;
		}
		if (value.matches("\\d+") == false) {
			return null;
		}
		final int widthwarning = Integer.parseInt(value);
		if (actualWidth > widthwarning) {
			return "The image is " + ((int) actualWidth) + " pixel width. (Warning limit is " + widthwarning + ")";
		}
		return null;
	}

	public void addSprite(String name, Sprite sprite) {
		skinParam.addSprite(name, sprite);
	}

	public final DisplayPositionned getLegend() {
		return legend;
	}

	public final void setLegend(DisplayPositionned legend) {
		this.legend = legend;
	}

	private boolean useJDot;

	public void setUseJDot(boolean useJDot) {
		this.useJDot = useJDot;
	}

	public boolean isUseJDot() {
		return useJDot;
	}

	public void setDotExecutable(String dotExecutable) {
		skinParam.setDotExecutable(dotExecutable);
	}
}
