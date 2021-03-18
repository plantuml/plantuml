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
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.AnimatedGifEncoder;
import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileUtils;
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
import net.sourceforge.plantuml.security.ImageIO;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.svg.LengthAdjust;
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

	private final ImageParameter param;

	private final double top;
	private final double right;
	private final double bottom;
	private final double left;

	private UDrawable udrawable;
	private boolean randomPixel;

	public static ImageBuilder build(ImageParameter imageParameter) {
		return new ImageBuilder(imageParameter);
	}

	private ImageBuilder(ImageParameter imageParameter) {
		this.param = imageParameter;

		this.top = imageParameter.getMargins().getTop();
		this.right = imageParameter.getMargins().getRight();
		this.bottom = imageParameter.getMargins().getBottom();
		this.left = imageParameter.getMargins().getLeft();

	}

	public void setUDrawable(UDrawable udrawable) {
		this.udrawable = udrawable;
	}

	public ImageData writeImageTOBEMOVED(long seed, OutputStream os) throws IOException {
		return writeImageTOBEMOVED(param.getFileFormatOption(), seed, os);
	}

	public ImageData writeImageTOBEMOVED(FileFormatOption fileFormatOption, long seed, OutputStream os)
			throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.MJPEG) {
			return writeImageMjpeg(os, fileFormatOption.getDefaultStringBounder(param.getSvgCharSizeHack()));
		} else if (fileFormat == FileFormat.ANIMATED_GIF) {
			return writeImageAnimatedGif(os, fileFormatOption.getDefaultStringBounder(param.getSvgCharSizeHack()));
		}
		return writeImageInternal(fileFormatOption, seed, os, param.getAnimation());
	}

	private ImageData writeImageInternal(FileFormatOption fileFormatOption, long seed, OutputStream os,
			Animation animationArg) throws IOException {
		Dimension2D dim = getFinalDimension(fileFormatOption.getDefaultStringBounder(param.getSvgCharSizeHack()));
		double dx = 0;
		double dy = 0;
		if (animationArg != null) {
			final MinMax minmax = param.getAnimation().getMinMax(dim);
			animationArg.setDimension(dim);
			dim = minmax.getDimension();
			dx = -minmax.getMinX();
			dy = -minmax.getMinY();
		}

		final UGraphic2 ug = createUGraphic(fileFormatOption, seed, dim, animationArg, dx, dy);
		UGraphic ug2 = ug;

		final UStroke borderStroke = param.getBorderStroke();
		if (borderStroke != null) {
			final HColor color = param.getBorderColor() == null ? HColorUtils.BLACK : param.getBorderColor();
			final double width = dim.getWidth() - borderStroke.getThickness();
			final double height = dim.getHeight() - borderStroke.getThickness();
			final URectangle shape = new URectangle(width, height).rounded(param.getBorderCorner());
			ug2.apply(color).apply(borderStroke).draw(shape);
		}
		if (randomPixel) {
			drawRandomPoint(ug2);
		}
		ug2 = ug2.apply(new UTranslate(left, top));
		final UGraphic ugDecored = handwritten(ug2);
		udrawable.drawU(ugDecored);
		ugDecored.flushUg();
		ug.writeImageTOBEMOVED(os, param.getMetadata(), 96);
		os.flush();

		if (ug instanceof UGraphicG2d) {
			final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
			if (urls.size() > 0) {
				final CMapData cmap = CMapData.cmapString(urls, param.getDpi());
				return new ImageDataComplex(dim, cmap, param.getWarningOrError());
			}
		}
		return new ImageDataSimple(dim);
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

	public Dimension2D getFinalDimension(StringBounder stringBounder) {
		final Dimension2D dim;

		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		dim = new Dimension2DDouble(limitFinder.getMaxX(), limitFinder.getMaxY());

		return new Dimension2DDouble(dim.getWidth() + 1 + left + right, dim.getHeight() + 1 + top + bottom);
	}

	private UGraphic handwritten(UGraphic ug) {
		if (param.isUseHandwritten()) {
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

		return new ImageDataSimple(dim);

	}

	private ImageData writeImageAnimatedGif(OutputStream os, StringBounder stringBounder) throws IOException {

		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		final Dimension2D dim = new Dimension2DDouble(limitFinder.getMaxX() + 1 + left + right,
				limitFinder.getMaxY() + 1 + top + bottom);

		final MinMax minmax = param.getAnimation().getMinMax(dim);

		final AnimatedGifEncoder e = new AnimatedGifEncoder();
		// e.setQuality(1);
		e.setRepeat(0);
		e.start(os);
		// e.setDelay(1000); // 1 frame per sec
		// e.setDelay(100); // 10 frame per sec
		e.setDelay(60); // 16 frame per sec
		// e.setDelay(50); // 20 frame per sec

		for (AffineTransformation at : param.getAnimation().getAll()) {
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

	private UGraphic2 createUGraphic(FileFormatOption option, long seed, final Dimension2D dim, Animation animationArg,
			double dx, double dy) {
		final ColorMapper colorMapper = param.getColorMapper();
		final double scaleFactor = (param.getScale() == null ? 1 : param.getScale().getScale(dim.getWidth(), dim.getHeight()))
				* param.getDpi() / 96.0;
		final FileFormat fileFormat = option.getFileFormat();
		switch (fileFormat) {
		case PNG:
			return createUGraphicPNG(colorMapper, scaleFactor, dim, param.getBackcolor(), animationArg, dx, dy,
					option.getWatermark());
		case SVG:
			return createUGraphicSVG(colorMapper, scaleFactor, dim, param.getBackcolor(),
					option.getSvgLinkTarget(), option.getHoverColor(), seed, option.getPreserveAspectRatio(),
					param.getlengthAdjust());
		case EPS:
			return new UGraphicEps(colorMapper, EpsStrategy.getDefault2());
		case EPS_TEXT:
			return new UGraphicEps(colorMapper, EpsStrategy.WITH_MACRO_AND_TEXT);
		case HTML5:
			return new UGraphicHtml5(colorMapper);
		case VDX:
			return new UGraphicVdx(colorMapper);
		case LATEX:
			return new UGraphicTikz(colorMapper, scaleFactor, true, option.getTikzFontDistortion());
		case LATEX_NO_PREAMBLE:
			return new UGraphicTikz(colorMapper, scaleFactor, false, option.getTikzFontDistortion());
		case BRAILLE_PNG:
			return new UGraphicBraille(colorMapper, fileFormat);
		case UTXT:
		case ATXT:
			return new UGraphicTxt();
		default:
			throw new UnsupportedOperationException(fileFormat.toString());
		}
	}

	private UGraphic2 createUGraphicSVG(ColorMapper colorMapper, double scaleFactor, Dimension2D dim, final HColor suggested,
			String svgLinkTarget, String hover, long seed, String preserveAspectRatio, LengthAdjust lengthAdjust) {
		HColor backColor = HColorUtils.WHITE;
		if (suggested instanceof HColorSimple) {
			backColor = suggested;
		}
		final boolean dimensionStyle = param.isSvgDimensionStyle();
		final UGraphicSvg ug;
		if (suggested instanceof HColorGradient) {
			ug = new UGraphicSvg(dimensionStyle, dim, colorMapper, (HColorGradient) suggested, false, scaleFactor,
					svgLinkTarget, hover, seed, preserveAspectRatio, param.getSvgCharSizeHack(),
					param.getlengthAdjust());
		} else if (backColor == null || colorMapper.toColor(backColor).equals(Color.WHITE)) {
			ug = new UGraphicSvg(dimensionStyle, dim, colorMapper, false, scaleFactor, svgLinkTarget, hover, seed,
					preserveAspectRatio, param.getSvgCharSizeHack(), param.getlengthAdjust());
		} else {
			final String tmp = colorMapper.toSvg(backColor);
			ug = new UGraphicSvg(dimensionStyle, dim, colorMapper, tmp, false, scaleFactor, svgLinkTarget, hover, seed,
					preserveAspectRatio, param.getSvgCharSizeHack(), param.getlengthAdjust());
		}
		return ug;

	}

	private UGraphic2 createUGraphicPNG(ColorMapper colorMapper, double scaleFactor, final Dimension2D dim,
			HColor mybackcolor, Animation affineTransforms, double dx, double dy, String watermark) {
		Color backColor = Color.WHITE;
		if (mybackcolor instanceof HColorSimple) {
			backColor = colorMapper.toColor(mybackcolor);
		} else if (mybackcolor instanceof HColorBackground) {
			backColor = null;
		}

		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * scaleFactor),
				(int) (dim.getHeight() * scaleFactor), backColor);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(colorMapper, graphics2D, scaleFactor,
				affineTransforms == null ? null : affineTransforms.getFirst(), dx, dy);
		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		if (mybackcolor instanceof HColorGradient) {
			ug.apply(mybackcolor.bg()).draw(new URectangle(im.getWidth() / scaleFactor, im.getHeight() / scaleFactor));
		}

		return ug;
	}

	public void setRandomPixel(boolean randomPixel) {
		this.randomPixel = randomPixel;

	}

}
