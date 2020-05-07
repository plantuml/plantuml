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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.sourceforge.plantuml.AnimatedGifEncoder;
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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.anim.AffineTransformation;
import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.braille.UGraphicBraille;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.mjpeg.MJPEGGenerator;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.ugraphic.crossing.UGraphicCrossing;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.ugraphic.html5.UGraphicHtml5;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.tikz.UGraphicTikz;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.ugraphic.visio.UGraphicVdx;

public class ImageBuilder {

	private final ColorMapper colorMapper;
	private final double dpiFactor;
	private final HColor mybackcolor;
	private final String metadata;
	private final String warningOrError;
	private final double top;
	private final double right;
	private final double bottom;
	private final double left;
	private final Animation animation;
	private final boolean useHandwritten;

	private UDrawable udrawable;

	private final UStroke borderStroke;
	private final HColor borderColor;
	private final double borderCorner;

	private final boolean svgDimensionStyle;
	private boolean randomPixel;

	public static ImageBuilder buildA(ColorMapper colorMapper, boolean useHandwritten, Animation animation,
			String metadata, String warningOrError, double dpiFactor, HColor mybackcolor) {
		return new ImageBuilder(colorMapper, useHandwritten, animation, metadata, warningOrError, dpiFactor,
				mybackcolor, ClockwiseTopRightBottomLeft.none());
	}

	public static ImageBuilder buildB(ColorMapper colorMapper, boolean useHandwritten,
			ClockwiseTopRightBottomLeft margins, Animation animation, String metadata, String warningOrError,
			double dpiFactor, HColor mybackcolor) {
		return new ImageBuilder(colorMapper, useHandwritten, animation, metadata, warningOrError, dpiFactor,
				mybackcolor, margins);
	}

	public static ImageBuilder buildC(ISkinParam skinParam, ClockwiseTopRightBottomLeft margins, Animation animation,
			String metadata, String warningOrError, double dpiFactor, HColor mybackcolor) {
		return new ImageBuilder(skinParam, animation, metadata, warningOrError, dpiFactor, mybackcolor, margins);
	}

	public static ImageBuilder buildD(ISkinParam skinParam, ClockwiseTopRightBottomLeft margins, Animation animation,
			String metadata, String warningOrError, double dpiFactor) {
		return new ImageBuilder(skinParam, animation, metadata, warningOrError, dpiFactor,
				skinParam.getBackgroundColor(false), margins);
	}

	private ImageBuilder(ColorMapper colorMapper, boolean useHandwritten, Animation animation, String metadata,
			String warningOrError, double dpiFactor, HColor mybackcolor, ClockwiseTopRightBottomLeft margins) {
		this.top = margins.getTop();
		this.right = margins.getRight();
		this.bottom = margins.getBottom();
		this.left = margins.getLeft();
		this.animation = animation;
		this.metadata = metadata;
		this.warningOrError = warningOrError;
		this.dpiFactor = dpiFactor;

		this.borderColor = null;
		this.borderCorner = 0;

		this.svgDimensionStyle = true;
		this.colorMapper = colorMapper;
		this.mybackcolor = mybackcolor;
		this.useHandwritten = useHandwritten;

		this.borderStroke = null;

	}

	private ImageBuilder(ISkinParam skinParam, Animation animation, String metadata, String warningOrError,
			double dpiFactor, HColor mybackcolor, ClockwiseTopRightBottomLeft margins) {
		this.top = margins.getTop();
		this.right = margins.getRight();
		this.bottom = margins.getBottom();
		this.left = margins.getLeft();
		this.animation = animation;
		this.metadata = metadata;
		this.warningOrError = warningOrError;
		this.dpiFactor = dpiFactor;

		final Rose rose = new Rose();
		this.borderColor = rose.getHtmlColor(skinParam, ColorParam.diagramBorder);
		this.borderCorner = skinParam.getRoundCorner(CornerParam.diagramBorder, null);

		this.svgDimensionStyle = skinParam.svgDimensionStyle();
		this.colorMapper = skinParam.getColorMapper();
		this.mybackcolor = mybackcolor;
		this.useHandwritten = skinParam.handwritten();

		final UStroke thickness = skinParam.getThickness(LineParam.diagramBorder, null);
		if (thickness == null && borderColor != null) {
			this.borderStroke = new UStroke();
		} else {
			this.borderStroke = thickness;
		}

	}

	public void setUDrawable(UDrawable udrawable) {
		this.udrawable = udrawable;
	}

	public ImageData writeImageTOBEMOVED(FileFormatOption fileFormatOption, long seed, OutputStream os)
			throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.MJPEG) {
			return writeImageMjpeg(os, fileFormatOption.getDefaultStringBounder());
		} else if (fileFormat == FileFormat.ANIMATED_GIF) {
			return writeImageAnimatedGif(os, fileFormatOption.getDefaultStringBounder());
		}
		return writeImageInternal(fileFormatOption, seed, os, animation);
	}

	private static Semaphore SEMAPHORE_SMALL;
	private static Semaphore SEMAPHORE_BIG;
	private static int MAX_PRICE = 0;

	public static void setMaxPixel(int max) {
		MAX_PRICE = max / 2;
		SEMAPHORE_SMALL = new Semaphore(MAX_PRICE, true);
		SEMAPHORE_BIG = new Semaphore(MAX_PRICE, true);
	}

	private int getPrice(FileFormatOption fileFormatOption, Dimension2D dim) {
		// if (fileFormatOption.getFileFormat() != FileFormat.PNG) {
		// return 0;
		// }
		if (MAX_PRICE == 0) {
			return 0;
		}
		final int price = Math.min(MAX_PRICE,
				((int) (dim.getHeight() * dpiFactor)) * ((int) (dim.getWidth() * dpiFactor)));
		return price;
	}

	private Semaphore getSemaphore(int price) {
		if (price == 0) {
			return null;
		}
		if (price == MAX_PRICE) {
			return SEMAPHORE_BIG;
		}
		return SEMAPHORE_SMALL;
	}

	private ImageData writeImageInternal(FileFormatOption fileFormatOption, long seed, OutputStream os,
			Animation animationArg) throws IOException {
		Dimension2D dim = getFinalDimension(fileFormatOption.getDefaultStringBounder());
		double dx = 0;
		double dy = 0;
		if (animationArg != null) {
			final MinMax minmax = animation.getMinMax(dim);
			animationArg.setDimension(dim);
			dim = minmax.getDimension();
			dx = -minmax.getMinX();
			dy = -minmax.getMinY();
		}

		final int price = getPrice(fileFormatOption, dim);
		final Semaphore semaphore = getSemaphore(price);
		if (semaphore != null) {
			try {
				semaphore.acquire(price);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}
		try {
			final UGraphic2 ug = createUGraphic(fileFormatOption, seed, dim, animationArg, dx, dy);
			UGraphic ug2 = ug;
//			if (externalMargin1 > 0) {
//				ug2 = ug2.apply(new UTranslate(externalMargin1, externalMargin1));
//			}
			if (borderStroke != null) {
				final HColor color = borderColor == null ? HColorUtils.BLACK : borderColor;
//				final URectangle shape = new URectangle(dim.getWidth() - externalMargin() - borderStroke.getThickness(),
//						dim.getHeight() - externalMargin() - borderStroke.getThickness()).rounded(borderCorner);
				final URectangle shape = new URectangle(dim.getWidth() - borderStroke.getThickness(),
						dim.getHeight() - borderStroke.getThickness()).rounded(borderCorner);
				ug2.apply(color).apply(borderStroke).draw(shape);
			}
			if (randomPixel) {
				drawRandomPoint(ug2);
			}
//			if (externalMargin1 > 0) {
//				ug2 = ug2.apply(new UTranslate(externalMargin2, externalMargin2));
//			}
			ug2 = ug2.apply(new UTranslate(left, top));
			final UGraphic ugDecored = handwritten(ug2);
			udrawable.drawU(ugDecored);
			ugDecored.flushUg();
			ug.writeImageTOBEMOVED(os, metadata, 96);
			os.flush();

			if (ug instanceof UGraphicG2d) {
				final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
				if (urls.size() > 0) {
					final CMapData cmap = CMapData.cmapString(urls, dpiFactor);
					return new ImageDataComplex(dim, cmap, warningOrError);
				}
			}
			return new ImageDataSimple(dim);
		} finally {
			if (semaphore != null) {
				semaphore.release(price);
			}
		}

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

//	private double externalMargin() {
//		return 2 * (externalMargin1 + externalMargin2);
//	}

	public Dimension2D getFinalDimension(StringBounder stringBounder) {
		final Dimension2D dim;

		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		dim = new Dimension2DDouble(limitFinder.getMaxX(), limitFinder.getMaxY());

//		return new Dimension2DDouble(dim.getWidth() + 1 + margin1 + margin2 + externalMargin(),
//				dim.getHeight() + 1 + margin1 + margin2 + externalMargin());
		return new Dimension2DDouble(dim.getWidth() + 1 + left + right, dim.getHeight() + 1 + top + bottom);
	}

	private UGraphic handwritten(UGraphic ug) {
		if (useHandwritten) {
			return new UGraphicHandwritten(ug);
		}
		if (OptionFlags.OMEGA_CROSSING) {
			return new UGraphicCrossing(ug);
		} else {
			return ug;
		}
	}

	private ImageData writeImageMjpeg(OutputStream os, StringBounder stringBounder) throws IOException {

		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		final Dimension2D dim = new Dimension2DDouble(limitFinder.getMaxX() + 1 + left + right,
				limitFinder.getMaxY() + 1 + top + bottom);

		final File f = new File("c:/tmp.avi");

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

		return new ImageDataSimple(dim);

	}

	private ImageData writeImageAnimatedGif(OutputStream os, StringBounder stringBounder) throws IOException {

		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		final Dimension2D dim = new Dimension2DDouble(limitFinder.getMaxX() + 1 + left + right,
				limitFinder.getMaxY() + 1 + top + bottom);

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
		return new ImageDataSimple(dim);

	}

	private Image getAviImage(AffineTransformation affineTransform) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeImageInternal(new FileFormatOption(FileFormat.PNG), 42, baos, Animation.singleton(affineTransform));
		baos.close();

		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Image im = ImageIO.read(bais);
		bais.close();
		return im;
	}

	private UGraphic2 createUGraphic(FileFormatOption fileFormatOption, long seed, final Dimension2D dim,
			Animation animationArg, double dx, double dy) {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		switch (fileFormat) {
		case PNG:
			return createUGraphicPNG(colorMapper, dpiFactor, dim, mybackcolor, animationArg, dx, dy,
					fileFormatOption.getWatermark());
		case SVG:
			return createUGraphicSVG(colorMapper, dpiFactor, dim, mybackcolor, fileFormatOption.getSvgLinkTarget(),
					fileFormatOption.getHoverColor(), seed, fileFormatOption.getPreserveAspectRatio());
		case EPS:
			return new UGraphicEps(colorMapper, EpsStrategy.getDefault2());
		case EPS_TEXT:
			return new UGraphicEps(colorMapper, EpsStrategy.WITH_MACRO_AND_TEXT);
		case HTML5:
			return new UGraphicHtml5(colorMapper);
		case VDX:
			return new UGraphicVdx(colorMapper);
		case LATEX:
			return new UGraphicTikz(colorMapper, dpiFactor, true, fileFormatOption.getTikzFontDistortion());
		case LATEX_NO_PREAMBLE:
			return new UGraphicTikz(colorMapper, dpiFactor, false, fileFormatOption.getTikzFontDistortion());
		case BRAILLE_PNG:
			return new UGraphicBraille(colorMapper, fileFormat);
		case UTXT:
		case ATXT:
			return new UGraphicTxt();
		default:
			throw new UnsupportedOperationException(fileFormat.toString());
		}
	}

	private UGraphic2 createUGraphicSVG(ColorMapper colorMapper, double scale, Dimension2D dim, final HColor suggested,
			String svgLinkTarget, String hover, long seed, String preserveAspectRatio) {
		HColor backColor = HColorUtils.WHITE;
		if (suggested instanceof HColorSimple) {
			backColor = suggested;
		}
		final UGraphicSvg ug;
		if (suggested instanceof HColorGradient) {
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, (HColorGradient) suggested, false, scale,
					svgLinkTarget, hover, seed, preserveAspectRatio);
		} else if (backColor == null || colorMapper.toColor(backColor).equals(Color.WHITE)) {
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, false, scale, svgLinkTarget, hover, seed,
					preserveAspectRatio);
		} else {
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, colorMapper.toSvg(backColor), false, scale,
					svgLinkTarget, hover, seed, preserveAspectRatio);
		}
		return ug;

	}

	private UGraphic2 createUGraphicPNG(ColorMapper colorMapper, double dpiFactor, final Dimension2D dim,
			HColor mybackcolor, Animation affineTransforms, double dx, double dy, String watermark) {
		Color backColor = Color.WHITE;
		if (mybackcolor instanceof HColorSimple) {
			backColor = colorMapper.toColor(mybackcolor);
		} else if (mybackcolor instanceof HColorBackground) {
			backColor = null;
		}

		/*
		 * if (rotation) { builder = new EmptyImageBuilder((int) (dim.getHeight() *
		 * dpiFactor), (int) (dim.getWidth() * dpiFactor), backColor); graphics2D =
		 * builder.getGraphics2D(); graphics2D.rotate(-Math.PI / 2);
		 * graphics2D.translate(-builder.getBufferedImage().getHeight(), 0); } else {
		 */
		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * dpiFactor),
				(int) (dim.getHeight() * dpiFactor), backColor);
		final Graphics2D graphics2D = builder.getGraphics2D();

		// }
		final UGraphicG2d ug = new UGraphicG2d(colorMapper, graphics2D, dpiFactor,
				affineTransforms == null ? null : affineTransforms.getFirst(), dx, dy);
		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		if (mybackcolor instanceof HColorGradient) {
			ug.apply(mybackcolor.bg()).draw(new URectangle(im.getWidth() / dpiFactor, im.getHeight() / dpiFactor));
		}

		return ug;
	}

	public void setRandomPixel(boolean randomPixel) {
		this.randomPixel = randomPixel;

	}

}
