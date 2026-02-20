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
package net.sourceforge.plantuml.core;

// ::comment when __TEAVM__
import java.awt.Graphics2D;
// ::done
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import net.atmp.SvgOption;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.braille.UGraphicBraille;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.debug.UGraphicDebug;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.drawing.eps.EpsStrategy;
import net.sourceforge.plantuml.klimt.drawing.eps.UGraphicEps;
import net.sourceforge.plantuml.klimt.drawing.g2d.UGraphicG2d;
import net.sourceforge.plantuml.klimt.drawing.html5.UGraphicHtml5;
import net.sourceforge.plantuml.klimt.drawing.svg.UGraphicSvg;
import net.sourceforge.plantuml.klimt.drawing.tikz.UGraphicTikz;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.drawing.visio.UGraphicVdx;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.text.SvgCharSizeHack;
import net.sourceforge.plantuml.url.CMapData;
import net.sourceforge.plantuml.url.Url;

/**
 * Exports a fully-decorated {@link TextBlock} to an {@link OutputStream} in a
 * given image format.
 *
 * <p>
 * This class handles the low-level rendering concerns that were previously
 * buried inside {@code ImageBuilder}: dimension calculation, scale/DPI,
 * UGraphic creation, margins, border, and stream writing.
 *
 * <p>
 * It does <b>not</b> handle diagram chrome (title, header, footer, etc.).
 * The input {@link TextBlock} is expected to be already fully decorated
 * (e.g. by {@link DiagramChromeFactory12026}). Handwritten mode is applied
 * automatically when {@code skinParam.handwritten()} is {@code true}.
 */
public class TextBlockExporter12026 {

	private final TextBlock textBlock;
	private final FileFormatOption fileFormatOption;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	private final HColor backcolor;
	private final ClockwiseTopRightBottomLeft margin;
	private final String metadata;
	private final long seed;
	private final int status;
	private final Scale scale;
	private final String warningOrError;
	private final Pragma pragma;

	// Optional diagram-level info for SVG attributes
	private final UmlDiagramType diagramType;

	private TextBlockExporter12026(Builder builder) {
		this.textBlock = builder.textBlock;
		this.fileFormatOption = builder.fileFormatOption;
		this.skinParam = builder.skinParam;
		this.backcolor = builder.backcolor;
		this.margin = builder.margin;
		this.metadata = builder.metadata;
		this.seed = builder.seed;
		this.status = builder.status;
		this.scale = builder.scale;
		this.warningOrError = builder.warningOrError;
		this.pragma = builder.pragma;
		this.diagramType = builder.diagramType;
		this.stringBounder = builder.fileFormatOption
				.getDefaultStringBounder(builder.skinParam != null ? builder.skinParam : SvgCharSizeHack.NO_HACK);
	}

	/**
	 * Exports the {@link TextBlock} to the given {@link OutputStream}.
	 *
	 * @param os the output stream to write to
	 * @return image metadata (dimensions, optional cmap data)
	 * @throws IOException if an I/O error occurs
	 */
	public ImageData exportTo(OutputStream os) throws IOException {
		final XDimension2D dim = calculateFinalDimension();

		final double scaleFactor = computeScaleFactor(dim);
		if (scaleFactor <= 0)
			throw new IllegalStateException("Bad scaleFactor");

		UGraphic ug = createUGraphic(dim, scaleFactor);

		maybeDrawBorder(ug, dim);

		ug = ug.apply(new UTranslate(margin.getLeft(), margin.getTop()));
		ug = applyHandwritten(ug);

		textBlock.drawU(ug);
		ug.flushUg();
		ug.writeToStream(os, metadata, 96);
		os.flush();

		if (!TeaVM.isTeaVM()) {
			if (ug instanceof UGraphicG2d) {
				final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
				if (urls.size() > 0) {
					final CMapData cmap = CMapData.cmapString(urls, scaleFactor);
					return new ImageDataComplex(dim, cmap, warningOrError, status);
				}
			}
		}

		return new ImageDataSimple(dim, status);
	}

	// -----------------------------------------------------------------------
	// Dimension calculation
	// -----------------------------------------------------------------------

	private XDimension2D calculateFinalDimension() {
		final XDimension2D dim = textBlock.calculateDimension(stringBounder);
		return new XDimension2D(dim.getWidth() + margin.getLeft() + margin.getRight(),
				dim.getHeight() + margin.getTop() + margin.getBottom());
	}

	private double computeScaleFactor(XDimension2D dim) {
		final int dpi = skinParam == null ? 96 : skinParam.getDpi();
		final double fromScale = scale == null ? 1 : scale.getScale(dim.getWidth(), dim.getHeight());
		return fromScale * dpi / 96.0;
	}

	// -----------------------------------------------------------------------
	// Border
	// -----------------------------------------------------------------------

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

	// -----------------------------------------------------------------------
	// Handwritten mode
	// -----------------------------------------------------------------------

	private UGraphic applyHandwritten(UGraphic ug) {
		if (skinParam != null && skinParam.handwritten())
			return new UGraphicHandwritten(ug);
		return ug;
	}

	// -----------------------------------------------------------------------
	// UGraphic factory
	// -----------------------------------------------------------------------

	private UGraphic createUGraphic(XDimension2D dim, double scaleFactor) {
		// ::comment when __TEAVM__
		final ColorMapper colorMapper = fileFormatOption.getColorMapper();
		final Pragma p = pragma != null ? pragma : Pragma.createEmpty();
		switch (fileFormatOption.getFileFormat()) {
		case PNG:
		case PNG_EMPTY:
		case RAW:
			return createUGraphicPNG(scaleFactor, dim, fileFormatOption.getWatermark(),
					fileFormatOption.getFileFormat());
		case SVG:
			return createUGraphicSVG(scaleFactor, dim, p);
		case EPS:
			return new UGraphicEps(backcolor, colorMapper, stringBounder, EpsStrategy.getDefault2());
		case EPS_TEXT:
			return new UGraphicEps(backcolor, colorMapper, stringBounder, EpsStrategy.WITH_MACRO_AND_TEXT);
		case HTML5:
			return new UGraphicHtml5(backcolor, colorMapper, stringBounder);
		case VDX:
			return new UGraphicVdx(backcolor, colorMapper, stringBounder);
		case LATEX:
			return new UGraphicTikz(backcolor, colorMapper, stringBounder, scaleFactor, true, p);
		case LATEX_NO_PREAMBLE:
			return new UGraphicTikz(backcolor, colorMapper, stringBounder, scaleFactor, false, p);
		case BRAILLE_PNG:
			return new UGraphicBraille(backcolor, colorMapper, stringBounder);
		case UTXT:
		case ATXT:
			return new UGraphicTxt();
		case DEBUG:
			return new UGraphicDebug(scaleFactor, dim, getSvgLinkTarget(), getHoverPathColorRGB(), seed,
					getPreserveAspectRatio());
		default:
			// ::done
			throw new UnsupportedOperationException(fileFormatOption.getFileFormat().toString());
		// ::comment when __TEAVM__
		}
		// ::done
	}

	// ::comment when __TEAVM__
	private UGraphic createUGraphicSVG(double scaleFactor, XDimension2D dim, Pragma p) {
		SvgOption option = SvgOption.basic().withPreserveAspectRatio(getPreserveAspectRatio());
		option = option.withHoverPathColorRGB(getHoverPathColorRGB());
		option = option.withMinDim(dim);
		option = option.withBackcolor(backcolor);
		option = option.withScale(scaleFactor);
		option = option.withColorMapper(fileFormatOption.getColorMapper());
		option = option.withLinkTarget(getSvgLinkTarget());
		option = option.withFont(p.getValue(PragmaKey.SVG_FONT));
		option = option.withPragma(p);
		if (skinParam != null)
			option = option.withConfigurationStore(skinParam.options());
		if (diagramType != null) {
			option = option.withRootAttribute("data-diagram-type", diagramType.name());
		}

		if (p.isTrue(PragmaKey.SVG_INTERACTIVE)) {
			String interactiveBaseFilename = "default";
			if (diagramType == UmlDiagramType.SEQUENCE)
				interactiveBaseFilename = "sequencediagram";
			option = option.withInteractive(interactiveBaseFilename);
		}

		if (skinParam != null) {
			option = option.withLengthAdjust(skinParam.getlengthAdjust());
			option = option.withSvgDimensionStyle(skinParam.svgDimensionStyle());
		}

		return UGraphicSvg.build(option, false, seed, stringBounder);
	}

	private UGraphic createUGraphicPNG(double scaleFactor, XDimension2D dim, String watermark, FileFormat format) {
		XColor pngBackColor = new XColor(0, 0, 0, 0);

		if (this.backcolor instanceof HColorSimple)
			pngBackColor = this.backcolor.toColor(fileFormatOption.getColorMapper());

		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.REPLACE_WHITE_BACKGROUND_BY_TRANSPARENT)
				&& (XColor.WHITE.equals(pngBackColor) || XColor.BLACK.equals(pngBackColor)))
			pngBackColor = new XColor(0, 0, 0, 0);

		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * scaleFactor),
				(int) (dim.getHeight() * scaleFactor), pngBackColor, stringBounder);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(backcolor, fileFormatOption.getColorMapper(), stringBounder, graphics2D,
				scaleFactor, format);

		ug.setPortableImage(builder.getPortableImage());
		final PortableImage im = ug.getPortableImage();
		if (this.backcolor instanceof HColorGradient)
			ug.apply(this.backcolor.bg())
					.draw(URectangle.build(im.getWidth() / scaleFactor, im.getHeight() / scaleFactor));

		return ug;
	}

	private String getHoverPathColorRGB() {
		if (fileFormatOption.getHoverColor() != null)
			return fileFormatOption.getHoverColor();
		if (skinParam != null) {
			final HColor color = skinParam.hoverPathColor();
			if (color != null)
				return color.toRGB(fileFormatOption.getColorMapper());
		}
		return null;
	}
	// ::done

	private String getSvgLinkTarget() {
		if (fileFormatOption.getSvgLinkTarget() != null)
			return fileFormatOption.getSvgLinkTarget();
		if (skinParam != null)
			return skinParam.getSvgLinkTarget();
		return null;
	}

	private String getPreserveAspectRatio() {
		if (fileFormatOption.getPreserveAspectRatio() != null)
			return fileFormatOption.getPreserveAspectRatio();
		if (skinParam != null)
			return skinParam.getPreserveAspectRatio();
		return SkinParam.DEFAULT_PRESERVE_ASPECT_RATIO;
	}

	// =======================================================================
	// Builder
	// =======================================================================

	/**
	 * Creates a new {@link Builder} for the given decorated {@link TextBlock} and
	 * output format.
	 *
	 * @param textBlock        the fully-decorated diagram content
	 * @param fileFormatOption the desired output format and options
	 * @return a new builder
	 */
	public static Builder builder(TextBlock textBlock, FileFormatOption fileFormatOption) {
		return new Builder(textBlock, fileFormatOption);
	}

	public static class Builder {
		// Required
		private final TextBlock textBlock;
		private final FileFormatOption fileFormatOption;

		// Optional with defaults
		private ISkinParam skinParam;
		private HColor backcolor = HColors.WHITE.withDark(HColors.BLACK);
		private ClockwiseTopRightBottomLeft margin = ClockwiseTopRightBottomLeft.none();
		private String metadata;
		private long seed = 42;
		private int status = 0;
		private Scale scale;
		private String warningOrError;
		private Pragma pragma;
		private UmlDiagramType diagramType;

		private Builder(TextBlock textBlock, FileFormatOption fileFormatOption) {
			this.textBlock = textBlock;
			this.fileFormatOption = fileFormatOption;
			if (textBlock.getBackcolor() != null) {
				this.backcolor = textBlock.getBackcolor();
			}
		}

		public Builder skinParam(ISkinParam skinParam) {
			this.skinParam = skinParam;
			return this;
		}

		public Builder backcolor(HColor backcolor) {
			this.backcolor = backcolor;
			return this;
		}

		public Builder margin(ClockwiseTopRightBottomLeft margin) {
			this.margin = margin;
			return this;
		}

		public Builder metadata(String metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder seed(long seed) {
			this.seed = seed;
			return this;
		}

		public Builder status(int status) {
			this.status = status;
			return this;
		}

		public Builder scale(Scale scale) {
			this.scale = scale;
			return this;
		}

		public Builder warningOrError(String warningOrError) {
			this.warningOrError = warningOrError;
			return this;
		}

		public Builder pragma(Pragma pragma) {
			this.pragma = pragma;
			return this;
		}

		public Builder diagramType(UmlDiagramType diagramType) {
			this.diagramType = diagramType;
			return this;
		}

		/**
		 * Convenience method to configure this builder from a {@link TitledDiagram},
		 * similar to what {@code ImageBuilder.styled()} does.
		 *
		 * @param diagram the source diagram
		 * @return this builder
		 */
		public Builder styled(TitledDiagram diagram) {
			this.skinParam = diagram.getSkinParam();
			this.backcolor = diagram.calculateBackColor();
			this.margin = calculateMargin(diagram);
			this.metadata = fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null;
			this.seed = diagram.seed();
			this.warningOrError = diagram.getWarningOrError();
			this.status = 0;
			this.scale = diagram.getScale();
			this.pragma = diagram.getPragma();
			this.diagramType = diagram.getUmlDiagramType();
			return this;
		}

		public TextBlockExporter12026 build() {
			return new TextBlockExporter12026(this);
		}

		private static ClockwiseTopRightBottomLeft calculateMargin(net.sourceforge.plantuml.TitledDiagram diagram) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.document)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			if (style.hasValue(PName.Margin))
				return style.getMargin();
			return diagram.getDefaultMargins();
		}
	}

}
