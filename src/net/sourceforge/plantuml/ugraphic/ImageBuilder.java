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
 * Original Author:  Matthew Leather
 * 
 *
 */
package net.sourceforge.plantuml.ugraphic;

import static net.sourceforge.plantuml.SkinParam.DEFAULT_PRESERVE_ASPECT_RATIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.AnimatedGifEncoder;
import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SvgCharSizeHack;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.anim.AffineTransformation;
import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.braille.UGraphicBraille;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.mjpeg.MJPEGGenerator;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.svg.LengthAdjust;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.ugraphic.debug.UGraphicDebug;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.ugraphic.html5.UGraphicHtml5;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.tikz.UGraphicTikz;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.ugraphic.visio.UGraphicVdx;

public class ImageBuilder {

	private Animation animation;
	private boolean annotations;
	private HColor backcolor = getDefaultHBackColor();
	private ColorMapper colorMapper = new ColorMapperIdentity();
	private Dimension2D dimension;
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
		return backcolor(HColorUtils.BLACK);
	}

	public ImageBuilder dimension(Dimension2D dimension) {
		this.dimension = dimension;
		return this;
	}

	private int getDpi() {
		return skinParam == null ? 96 : skinParam.getDpi();
	}

	public ImageBuilder drawable(UDrawable drawable) {
		this.udrawable = drawable;
		if (backcolor == null && drawable instanceof TextBlockBackcolored) {
			backcolor = ((TextBlockBackcolored) drawable).getBackcolor();
		}
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
		if (fileFormatOption.getSvgLinkTarget() != null) {
			return fileFormatOption.getSvgLinkTarget();
		} else if (skinParam != null) {
			return skinParam.getSvgLinkTarget();
		} else {
			return null;
		}
	}

	public ImageBuilder warningOrError(String warningOrError) {
		this.warningOrError = warningOrError;
		return this;
	}

	public ImageBuilder styled(TitledDiagram diagram) {
		skinParam = diagram.getSkinParam();
		stringBounder = fileFormatOption.getDefaultStringBounder(skinParam);
		animation = diagram.getAnimation();
		annotations = true;
		backcolor = diagram.calculateBackColor();
		colorMapper = skinParam.getColorMapper();
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
			final AnnotatedWorker annotatedWorker = new AnnotatedWorker(titledDiagram, skinParam, stringBounder);
			udrawable = annotatedWorker.addAdd((TextBlock) udrawable);
		}

		switch (fileFormatOption.getFileFormat()) {
		case MJPEG:
			return writeImageMjpeg(os);
		case ANIMATED_GIF:
			return writeImageAnimatedGif(os);
		default:
			return writeImageInternal(fileFormatOption, os, animation);
		}
	}

	public byte[] writeByteArray() throws IOException {
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			write(baos);
			return baos.toByteArray();
		}
	}

	private ImageData writeImageInternal(FileFormatOption fileFormatOption, OutputStream os, Animation animationArg)
			throws IOException {
		Dimension2D dim = getFinalDimension();
		double dx = 0;
		double dy = 0;
		if (animationArg != null) {
			final MinMax minmax = animationArg.getMinMax(dim);
			animationArg.setDimension(dim);
			dim = minmax.getDimension();
			dx = -minmax.getMinX();
			dy = -minmax.getMinY();
		}
		final Scale scale = titledDiagram == null ? null : titledDiagram.getScale();
		final double scaleFactor = (scale == null ? 1 : scale.getScale(dim.getWidth(), dim.getHeight())) * getDpi()
				/ 96.0;
		UGraphic ug = createUGraphic(fileFormatOption, dim, animationArg, dx, dy, scaleFactor);
		maybeDrawBorder(ug, dim);
		if (randomPixel) {
			drawRandomPoint(ug);
		}
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

	private void maybeDrawBorder(UGraphic ug, Dimension2D dim) {
		if (skinParam == null)
			return;

		final HColor color = new Rose().getHtmlColor(skinParam, ColorParam.diagramBorder);

		UStroke stroke = skinParam.getThickness(LineParam.diagramBorder, null);
		if (stroke == null && color != null)
			stroke = new UStroke();
		if (stroke == null)
			return;

		final URectangle rectangle = new URectangle(dim.getWidth() - stroke.getThickness(),
				dim.getHeight() - stroke.getThickness())
						.rounded(skinParam.getRoundCorner(CornerParam.diagramBorder, null));

		ug.apply(color == null ? HColorUtils.BLACK : color).apply(stroke).draw(rectangle);
	}

	private void drawRandomPoint(UGraphic ug2) {
		final Random rnd = new Random();
		final int red = rnd.nextInt(40);
		final int green = rnd.nextInt(40);
		final int blue = rnd.nextInt(40);
		final Color c = new Color(red, green, blue);
		final HColor color = new HColorSimple(c, false);
		ug2.apply(color).apply(color.bg()).draw(new URectangle(1, 1));
	}

	private Dimension2D getFinalDimension() {
		if (dimension == null) {
			final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
			udrawable.drawU(limitFinder);
			dimension = new Dimension2DDouble(limitFinder.getMaxX() + 1 + margin.getLeft() + margin.getRight(),
					limitFinder.getMaxY() + 1 + margin.getTop() + margin.getBottom());
		}
		return dimension;
	}

	private UGraphic handwritten(UGraphic ug) {
		if (skinParam != null && skinParam.handwritten()) {
			return new UGraphicHandwritten(ug);
		}
//		if (OptionFlags.OMEGA_CROSSING) {
//			return new UGraphicCrossing(ug);
//		} else {
		return ug;
//		}
	}

	private ImageData writeImageMjpeg(OutputStream os) throws IOException {

		final Dimension2D dim = getFinalDimension();

		final SFile f = new SFile("c:/tmp.avi");

		final int nbframe = 100;

		final MJPEGGenerator m = new MJPEGGenerator(f, getAviImage(null).getWidth(null),
				getAviImage(null).getHeight(null), 12.0, nbframe);
		for (int i = 0; i < nbframe; i++) {
			// AffineTransform at = AffineTransform.getRotateInstance(1.0);
			AffineTransform at = AffineTransform.getTranslateInstance(dim.getWidth() / 2, dim.getHeight() / 2);
			at.rotate(90.0 * Math.PI / 180.0 * i / 100);
			at.translate(-dim.getWidth() / 2, -dim.getHeight() / 2);
			// final AffineTransform at = AffineTransform.getTranslateInstance(i, 0);
			// final ImageIcon ii = new ImageIcon(getAviImage(at));
			// m.addImage(ii.getImage());
			throw new UnsupportedOperationException();
		}
		m.finishAVI();

		FileUtils.copyToStream(f, os);

		return createImageData(dim);
	}

	private ImageData writeImageAnimatedGif(OutputStream os) throws IOException {

		final Dimension2D dim = getFinalDimension();

		final MinMax minmax = animation.getMinMax(dim);

		final AnimatedGifEncoder e = new AnimatedGifEncoder();
		// e.setQuality(1);
		e.setRepeat(0);
		e.start(os);
		// e.setDelay(1000); // 1 frame per sec
		// e.setDelay(100); // 10 frame per sec
		e.setDelay(60); // 16 frame per sec
		// e.setDelay(50); // 20 frame per sec

		for (AffineTransformation at : animation.getAll()) {
			final ImageIcon ii = new ImageIcon(getAviImage(at));
			e.addFrame((BufferedImage) ii.getImage());
		}
		e.finish();
		return createImageData(dim);
	}

	private Image getAviImage(AffineTransformation affineTransform) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeImageInternal(new FileFormatOption(FileFormat.PNG), baos, Animation.singleton(affineTransform));
		baos.close();
		return SImageIO.read(baos.toByteArray());
	}

	private UGraphic createUGraphic(FileFormatOption option, final Dimension2D dim, Animation animationArg, double dx,
			double dy, double scaleFactor) {
		switch (option.getFileFormat()) {
		case PNG:
			return createUGraphicPNG(scaleFactor, dim, animationArg, dx, dy, option.getWatermark());
		case SVG:
			return createUGraphicSVG(scaleFactor, dim);
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
		default:
			throw new UnsupportedOperationException(option.getFileFormat().toString());
		}
	}

	private UGraphic createUGraphicSVG(double scaleFactor, Dimension2D dim) {
		final String hoverPathColorRGB = getHoverPathColorRGB();
		final LengthAdjust lengthAdjust = skinParam == null ? LengthAdjust.defaultValue() : skinParam.getlengthAdjust();
		final String preserveAspectRatio = getPreserveAspectRatio();
		final boolean svgDimensionStyle = skinParam == null || skinParam.svgDimensionStyle();
		final String svgLinkTarget = getSvgLinkTarget();
		final UGraphicSvg ug = new UGraphicSvg(backcolor, svgDimensionStyle, dim, colorMapper, false, scaleFactor,
				svgLinkTarget, hoverPathColorRGB, seed, preserveAspectRatio, stringBounder, lengthAdjust);
		return ug;

	}

	private UGraphic createUGraphicPNG(double scaleFactor, final Dimension2D dim, Animation affineTransforms,
			double dx, double dy, String watermark) {
		Color backColor = getDefaultBackColor();

		if (this.backcolor instanceof HColorSimple) {
			backColor = colorMapper.toColor(this.backcolor);
		} else if (this.backcolor instanceof HColorBackground || this.backcolor instanceof HColorNone) {
			backColor = null;
		}

		if (OptionFlags.getInstance().isReplaceWhiteBackgroundByTransparent() && backColor != null
				&& backColor.equals(Color.WHITE)) {
			backColor = new Color(0, 0, 0, 0);
		}

		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * scaleFactor),
				(int) (dim.getHeight() * scaleFactor), backColor, stringBounder);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(backcolor, colorMapper, stringBounder, graphics2D, scaleFactor,
				affineTransforms == null ? null : affineTransforms.getFirst(), dx, dy);
		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ug.getBufferedImage();
		if (this.backcolor instanceof HColorGradient) {
			ug.apply(this.backcolor.bg())
					.draw(new URectangle(im.getWidth() / scaleFactor, im.getHeight() / scaleFactor));
		}

		return ug;
	}

	static private Color getDefaultBackColor() {
		return Color.WHITE;
	}

	static private HColor getDefaultHBackColor() {
		return HColorUtils.WHITE;
	}

	private String getHoverPathColorRGB() {
		if (fileFormatOption.getHoverColor() != null) {
			return fileFormatOption.getHoverColor();
		} else if (skinParam != null) {
			final HColor color = skinParam.hoverPathColor();
			if (color != null) {
				return colorMapper.toRGB(color);
			}
		}
		return null;
	}

	private static ClockwiseTopRightBottomLeft calculateMargin(TitledDiagram diagram) {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature.of(SName.root, SName.document)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			if (style.hasValue(PName.Margin)) {
				return style.getMargin();
			}
		}
		return diagram.getDefaultMargins();
	}

	public String getPreserveAspectRatio() {
		if (fileFormatOption.getPreserveAspectRatio() != null) {
			return fileFormatOption.getPreserveAspectRatio();
		} else if (skinParam != null) {
			return skinParam.getPreserveAspectRatio();
		} else {
			return DEFAULT_PRESERVE_ASPECT_RATIO;
		}
	}

	private ImageDataSimple createImageData(Dimension2D dim) {
		return new ImageDataSimple(dim, status);
	}

}
