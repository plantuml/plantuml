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
 * Original Author:  Matthew Leather
 *
 *
 */
package net.atmp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.AnnotatedBuilder;
import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.braille.UGraphicBraille;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.debug.UGraphicDebug;
import net.sourceforge.plantuml.klimt.drawing.eps.EpsStrategy;
import net.sourceforge.plantuml.klimt.drawing.eps.UGraphicEps;
import net.sourceforge.plantuml.klimt.drawing.g2d.UGraphicG2d;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.drawing.html5.UGraphicHtml5;
import net.sourceforge.plantuml.klimt.drawing.svg.SvgOption;
import net.sourceforge.plantuml.klimt.drawing.svg.UGraphicSvg;
import net.sourceforge.plantuml.klimt.drawing.tikz.UGraphicTikz;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.drawing.visio.UGraphicVdx;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.text.SvgCharSizeHack;
import net.sourceforge.plantuml.url.CMapData;
import net.sourceforge.plantuml.url.Url;

public class ImageBuilder {

	private boolean annotations;
	private HColor backcolor = getDefaultHBackColor();

	private XDimension2D dimension;
	private final FileFormatOption fileFormatOption;
	private UDrawable udrawable;
	private ClockwiseTopRightBottomLeft margin = ClockwiseTopRightBottomLeft.none();
	private String metadata;
	private long seed = 42;
	private ISkinParam skinParam;
	private StringBounder stringBounder;
	private int status = 0;
	private TitledDiagram titledDiagram;
	private boolean randomPixel;
	private String warningOrError;

	public static ImageBuilder imageBuilder(FileFormatOption fileFormatOption) {
		return new ImageBuilder(fileFormatOption);
	}

	public static ImageBuilder plainImageBuilder(UDrawable drawable, FileFormatOption fileFormatOption) {
		return imageBuilder(fileFormatOption).drawable(drawable);
	}

	public static ImageBuilder plainPngBuilder(UDrawable drawable) {
		return imageBuilder(new FileFormatOption(FileFormat.PNG)).drawable(drawable);
	}

	private ImageBuilder(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
		this.stringBounder = fileFormatOption.getDefaultStringBounder(SvgCharSizeHack.NO_HACK);
	}

	public ImageBuilder annotations(boolean annotations) {
		this.annotations = annotations;
		return this;
	}

	public ImageBuilder backcolor(HColor backcolor) {
		this.backcolor = backcolor;
		return this;
	}

	public ImageBuilder blackBackcolor() {
		return backcolor(HColors.BLACK);
	}

	public ImageBuilder dimension(XDimension2D dimension) {
		this.dimension = dimension;
		return this;
	}

	private int getDpi() {
		return skinParam == null ? 96 : skinParam.getDpi();
	}

	public ImageBuilder drawable(UDrawable drawable) {
		this.udrawable = drawable;
		if (backcolor == null && drawable instanceof TextBlock)
			backcolor = ((TextBlock) drawable).getBackcolor();

		return this;
	}

	public ImageBuilder margin(ClockwiseTopRightBottomLeft margin) {
		this.margin = margin;
		return this;
	}

	public ImageBuilder metadata(String metadata) {
		this.metadata = metadata;
		return this;
	}

	public ImageBuilder randomPixel() {
		this.randomPixel = true;
		return this;
	}

	public ImageBuilder seed(long seed) {
		this.seed = seed;
		return this;
	}

	public ImageBuilder status(int status) {
		this.status = status;
		return this;
	}

	private String getSvgLinkTarget() {
		if (fileFormatOption.getSvgLinkTarget() != null)
			return fileFormatOption.getSvgLinkTarget();
		else if (skinParam != null)
			return skinParam.getSvgLinkTarget();
		else
			return null;

	}

	public ImageBuilder warningOrError(String warningOrError) {
		this.warningOrError = warningOrError;
		return this;
	}

	public ImageBuilder styled(TitledDiagram diagram) {
		skinParam = diagram.getSkinParam();
		stringBounder = fileFormatOption.getDefaultStringBounder(skinParam);
		annotations = true;
		backcolor = diagram.calculateBackColor();
		margin = calculateMargin(diagram);
		metadata = fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null;
		seed = diagram.seed();
		titledDiagram = diagram;
		warningOrError = diagram.getWarningOrError();
		return this;
	}

	public ImageData write(OutputStream os) throws IOException {
		if (annotations && titledDiagram != null) {
			if (!(udrawable instanceof TextBlock))
				throw new IllegalStateException("udrawable is not a TextBlock");
			final AnnotatedBuilder builder = new AnnotatedBuilder(titledDiagram, skinParam, stringBounder);
			final AnnotatedWorker annotatedWorker = new AnnotatedWorker(titledDiagram, skinParam, stringBounder,
					builder);
			udrawable = annotatedWorker.addAdd((TextBlock) udrawable);
		}

		return writeImageInternal(os);
	}

	public byte[] writeByteArray() throws IOException {
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			write(baos);
			return baos.toByteArray();
		}
	}

	private ImageData writeImageInternal(OutputStream os) throws IOException {
		XDimension2D dim = getFinalDimension();
		final Scale scale = titledDiagram == null ? null : titledDiagram.getScale();
		final double scaleFactor = (scale == null ? 1 : scale.getScale(dim.getWidth(), dim.getHeight())) * getDpi()
				/ 96.0;
		if (scaleFactor <= 0)
			throw new IllegalStateException("Bad scaleFactor");
		WasmLog.log("...image drawing...");
		UGraphic ug = createUGraphic(dim, scaleFactor,
				titledDiagram == null ? new Pragma() : titledDiagram.getPragma());
		maybeDrawBorder(ug, dim);
		if (randomPixel)
			drawRandomPoint(ug);

		ug = handwritten(ug.apply(new UTranslate(margin.getLeft(), margin.getTop())));
		udrawable.drawU(ug);
		ug.flushUg();
		ug.writeToStream(os, metadata, 96);
		os.flush();

		if (ug instanceof UGraphicG2d) {
			final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
			if (urls.size() > 0) {
				final CMapData cmap = CMapData.cmapString(urls, scaleFactor);
				return new ImageDataComplex(dim, cmap, warningOrError, status);
			}
		}
		return createImageData(dim);
	}

	private void maybeDrawBorder(UGraphic ug, XDimension2D dim) {
		if (skinParam == null)
			return;

		final HColor color = new Rose().getHtmlColor(skinParam, ColorParam.diagramBorder);

		UStroke stroke = skinParam.getThickness(LineParam.diagramBorder, null);
		if (stroke == null && color != null)
			stroke = UStroke.simple();
		if (stroke == null)
			return;

		final URectangle rectangle = URectangle
				.build(dim.getWidth() - stroke.getThickness(), dim.getHeight() - stroke.getThickness())
				.rounded(skinParam.getRoundCorner(CornerParam.diagramBorder, null));

		ug.apply(color == null ? HColors.BLACK : color).apply(stroke).draw(rectangle);
	}

	private void drawRandomPoint(UGraphic ug2) {
		final Random rnd = new Random();
		final int red = rnd.nextInt(40);
		final int green = rnd.nextInt(40);
		final int blue = rnd.nextInt(40);
		final Color c = new Color(red, green, blue);
		final HColor color = HColors.simple(c);
		ug2.apply(color).apply(color.bg()).draw(URectangle.build(1, 1));
	}

	private XDimension2D getFinalDimension() {
		if (dimension == null) {
			final LimitFinder limitFinder = LimitFinder.create(stringBounder, true);
			udrawable.drawU(limitFinder);
			dimension = new XDimension2D(limitFinder.getMaxX() + 1 + margin.getLeft() + margin.getRight(),
					limitFinder.getMaxY() + 1 + margin.getTop() + margin.getBottom());
		}
		return dimension;
	}

	private UGraphic handwritten(UGraphic ug) {
		if (skinParam != null && skinParam.handwritten())
			return new UGraphicHandwritten(ug);

		return ug;
	}

	private UGraphic createUGraphic(final XDimension2D dim, double scaleFactor, Pragma pragma) {
		final ColorMapper colorMapper = fileFormatOption.getColorMapper();
		switch (fileFormatOption.getFileFormat()) {
		case PNG:
		case RAW:
			return createUGraphicPNG(scaleFactor, dim, fileFormatOption.getWatermark(),
					fileFormatOption.getFileFormat());
		case SVG:
			return createUGraphicSVG(scaleFactor, dim, pragma);
		// ::comment when __CORE__
		case EPS:
			return new UGraphicEps(backcolor, colorMapper, stringBounder, EpsStrategy.getDefault2());
		case EPS_TEXT:
			return new UGraphicEps(backcolor, colorMapper, stringBounder, EpsStrategy.WITH_MACRO_AND_TEXT);
		case HTML5:
			return new UGraphicHtml5(backcolor, colorMapper, stringBounder);
		case VDX:
			return new UGraphicVdx(backcolor, colorMapper, stringBounder);
		case LATEX:
			return new UGraphicTikz(backcolor, colorMapper, stringBounder, scaleFactor, true);
		case LATEX_NO_PREAMBLE:
			return new UGraphicTikz(backcolor, colorMapper, stringBounder, scaleFactor, false);
		case BRAILLE_PNG:
			return new UGraphicBraille(backcolor, colorMapper, stringBounder);
		case UTXT:
		case ATXT:
			return new UGraphicTxt();
		case DEBUG:
			return new UGraphicDebug(scaleFactor, dim, getSvgLinkTarget(), getHoverPathColorRGB(), seed,
					getPreserveAspectRatio());
		// ::done
		default:
			throw new UnsupportedOperationException(fileFormatOption.getFileFormat().toString());
		}
	}

	private UGraphic createUGraphicSVG(double scaleFactor, XDimension2D dim, Pragma pragma) {
		SvgOption option = SvgOption.basic().withPreserveAspectRatio(getPreserveAspectRatio());
		option = option.withHoverPathColorRGB(getHoverPathColorRGB());
		option = option.withMinDim(dim);
		option = option.withBackcolor(backcolor);
		option = option.withScale(scaleFactor);
		option = option.withColorMapper(fileFormatOption.getColorMapper());
		option = option.withLinkTarget(getSvgLinkTarget());
		option = option.withFont(pragma.getValue("svgfont"));

		if ("true".equalsIgnoreCase(pragma.getValue("svginteractive")))
			option = option.withInteractive();
		if (skinParam != null) {
			option = option.withLengthAdjust(skinParam.getlengthAdjust());
			option = option.withSvgDimensionStyle(skinParam.svgDimensionStyle());
		}

		final UGraphicSvg ug = UGraphicSvg.build(option, false, seed, stringBounder);
		return ug;

	}

	private UGraphic createUGraphicPNG(double scaleFactor, final XDimension2D dim, String watermark,
			FileFormat format) {
		Color pngBackColor = new Color(0, 0, 0, 0);

		if (this.backcolor instanceof HColorSimple)
			pngBackColor = this.backcolor.toColor(fileFormatOption.getColorMapper());

		if (OptionFlags.getInstance().isReplaceWhiteBackgroundByTransparent()
				&& (Color.WHITE.equals(pngBackColor) || Color.BLACK.equals(pngBackColor)))
			pngBackColor = new Color(0, 0, 0, 0);

		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * scaleFactor),
				(int) (dim.getHeight() * scaleFactor), pngBackColor, stringBounder);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(backcolor, fileFormatOption.getColorMapper(), stringBounder, graphics2D,
				scaleFactor, format);

		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ug.getBufferedImage();
		if (this.backcolor instanceof HColorGradient)
			ug.apply(this.backcolor.bg())
					.draw(URectangle.build(im.getWidth() / scaleFactor, im.getHeight() / scaleFactor));

		return ug;
	}

	static private HColor getDefaultHBackColor() {
		return HColors.WHITE;
	}

	private String getHoverPathColorRGB() {
		if (fileFormatOption.getHoverColor() != null) {
			return fileFormatOption.getHoverColor();
		} else if (skinParam != null) {
			final HColor color = skinParam.hoverPathColor();
			if (color != null)
				return color.toRGB(fileFormatOption.getColorMapper());

		}
		return null;
	}

	private static ClockwiseTopRightBottomLeft calculateMargin(TitledDiagram diagram) {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document)
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
		if (style.hasValue(PName.Margin))
			return style.getMargin();

		return diagram.getDefaultMargins();
	}

	public String getPreserveAspectRatio() {
		if (fileFormatOption.getPreserveAspectRatio() != null)
			return fileFormatOption.getPreserveAspectRatio();
		else if (skinParam != null)
			return skinParam.getPreserveAspectRatio();
		else
			return SkinParam.DEFAULT_PRESERVE_ASPECT_RATIO;

	}

	private ImageDataSimple createImageData(XDimension2D dim) {
		return new ImageDataSimple(dim, status);
	}

}
