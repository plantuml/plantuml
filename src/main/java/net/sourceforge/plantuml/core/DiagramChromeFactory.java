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

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.Annotated;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.BigFrame;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.DecorateEntityImage;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.warning.Warning;

/**
 * Factory that decorates a raw diagram {@link TextBlock} with its chrome
 * elements: warnings banner, mainframe, legend, title, caption, header and
 * footer.
 *
 * <p>
 * This class extracts the annotation/decoration logic that was previously
 * spread across {@code AnnotatedBuilder} and {@code AnnotatedWorker}. It does
 * not deal with output format, handwritten mode, or any rendering concern
 * &mdash; it only assembles a new {@link TextBlock} that can be drawn later.
 *
 * <p>
 * Decoration order (inside-out):
 * <ol>
 * <li>warnings banner</li>
 * <li>mainframe</li>
 * <li>legend</li>
 * <li>title</li>
 * <li>caption</li>
 * <li>header / footer</li>
 * </ol>
 */
public final class DiagramChromeFactory {

	private static final FontConfiguration WARNING_FC = FontConfiguration.blackBlueTrue(UFontFactory.monospaced(10));

	// Utility class — no instantiation
	private DiagramChromeFactory() {
	}

	/**
	 * Takes a raw diagram {@link TextBlock} and wraps it with all the chrome
	 * elements found in the given {@code annotated} source (mainframe, legend,
	 * title, caption, header, footer), without any warnings.
	 *
	 * @param raw           the bare diagram content, without any decoration
	 * @param annotated     provides title, caption, legend, header, footer and
	 *                      mainframe
	 * @param skinParam     skin parameters used for styling
	 * @param stringBounder the string bounder for text measurement
	 * @return a new {@link TextBlock} that draws the raw content surrounded by all
	 *         its chrome elements
	 */
	public static TextBlock create(TextBlock raw, Annotated annotated, ISkinParam skinParam,
			StringBounder stringBounder) {
		return create(raw, annotated, skinParam, stringBounder, Collections.emptyList());
	}

	/**
	 * Takes a raw diagram {@link TextBlock} and wraps it with all the chrome
	 * elements found in the given {@code annotated} source (warnings, mainframe,
	 * legend, title, caption, header, footer).
	 *
	 * @param raw           the bare diagram content, without any decoration
	 * @param annotated     provides title, caption, legend, header, footer and
	 *                      mainframe
	 * @param skinParam     skin parameters used for styling
	 * @param stringBounder the string bounder for text measurement
	 * @param warnings      collection of warnings to display as a banner above the
	 *                      diagram; may be empty
	 * @return a new {@link TextBlock} that draws the raw content surrounded by all
	 *         its chrome elements
	 */
	public static TextBlock create(TextBlock raw, Annotated annotated, ISkinParam skinParam,
			StringBounder stringBounder, Collection<Warning> warnings) {

		TextBlock result = raw;
		BrowserLog.consoleLog(DiagramChromeFactory.class, "create " + warnings);
		result = addWarnings(result, warnings);
		result = decorateWithFrame(result, annotated, skinParam, stringBounder);
		result = addLegend(result, annotated, skinParam);
		result = addTitle(result, annotated, skinParam);
		result = addCaption(result, annotated, skinParam);
		result = addHeaderAndFooter(result, annotated, skinParam);
		return result;
	}

	// -----------------------------------------------------------------------
	// Warnings banner
	// -----------------------------------------------------------------------

	private static TextBlock addWarnings(TextBlock original, Collection<Warning> warnings) {
		if (warnings == null || warnings.isEmpty())
			return original;

		final TextBlock warningBanner = new WarningBannerBlock(warnings);
		return DecorateEntityImage.addTop(null, original, warningBanner, HorizontalAlignment.LEFT);
	}

	/**
	 * A self-contained {@link TextBlock} that renders a yellow warning banner.
	 * Reproduces the look of the warning drawing logic from {@code ImageBuilder}.
	 */
	private static class WarningBannerBlock implements TextBlock {

		private static final double HORIZONTAL_PADDING = 10;
		private static final double VERTICAL_PADDING_TOP = 15;
		private static final double VERTICAL_PADDING_BOTTOM = 10;
		private static final double LINE_SPACING = 10;
		private static final double CORNER_RADIUS = 5;

		private final Collection<Warning> warnings;

		WarningBannerBlock(Collection<Warning> warnings) {
			this.warnings = warnings;
		}

		@Override
		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final XDimension2D dim = calculateDimension(stringBounder);

			final HColorSet set = HColorSet.instance();
			final HColor back = set.getColorOrWhite("ffffcc").withDark(set.getColorOrWhite("774400"));
			final HColor border = set.getColorOrWhite("ffdd88").withDark(set.getColorOrWhite("aa5500"));

			final URectangle rect = URectangle.build(dim.getWidth(), dim.getHeight()).rounded(CORNER_RADIUS);
			ug.apply(back.bg()).apply(border).apply(UStroke.withThickness(3)).draw(rect);

			UGraphic ugText = ug.apply(HColors.BLACK).apply(new UTranslate(HORIZONTAL_PADDING, VERTICAL_PADDING_TOP));
			for (Warning w : warnings) {
				for (String s : w.getMessage()) {
					final UText text = UText.build(s, WARNING_FC);
					ugText.draw(text);
					final double height = text.calculateDimension(stringBounder).getHeight();
					ugText = ugText.apply(UTranslate.dy(height));
				}
				ugText = ugText.apply(UTranslate.dy(LINE_SPACING));
			}
		}

		@Override
		public XDimension2D calculateDimension(StringBounder stringBounder) {
			double width = 0;
			double height = 0;
			for (Warning w : warnings) {
				for (String s : w.getMessage()) {
					final XDimension2D lineDim = UText.build(s, WARNING_FC).calculateDimension(stringBounder);
					width = Math.max(width, lineDim.getWidth());
					height += lineDim.getHeight();
				}
				height += LINE_SPACING;
			}
			// Remove trailing spacing from last warning
			if (!warnings.isEmpty())
				height -= LINE_SPACING;

			return new XDimension2D(width + 2 * HORIZONTAL_PADDING, height + VERTICAL_PADDING_TOP + VERTICAL_PADDING_BOTTOM);
		}

		@Override
		public HColor getBackcolor() {
			return null;
		}
	}

	// -----------------------------------------------------------------------
	// Mainframe
	// -----------------------------------------------------------------------

	private static TextBlock decorateWithFrame(TextBlock original, Annotated annotated, ISkinParam skinParam,
			StringBounder stringBounder) {

		final Display mainFrame = annotated.getMainFrame().getDisplay();
		if (Display.isNull(mainFrame))
			return original;

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.mainframe)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = FontConfiguration.create(skinParam, style);
		final TextBlock title = mainFrame.create(fontConfiguration, HorizontalAlignment.CENTER, skinParam);
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);

		final Fashion symbolContext = style.getSymbolContext(skinParam.getIHtmlColorSet());
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding().incTop(dimTitle.getHeight() + 10);

		final MinMax originalMinMax = TextBlockUtils.getMinMax(original, stringBounder, false);

		final double ww = originalMinMax.getMinX() >= 0 ? originalMinMax.getMaxX() : originalMinMax.getWidth();
		final double hh = originalMinMax.getMinY() >= 0 ? originalMinMax.getMaxY() : originalMinMax.getHeight();
		final double dx = originalMinMax.getMinX() < 0 ? -originalMinMax.getMinX() : 0;
		final double dy = originalMinMax.getMinY() < 0 ? -originalMinMax.getMinY() : 0;
		final UTranslate delta = new UTranslate(dx, dy);

		final double width = padding.getLeft() + Math.max(ww + 12, dimTitle.getWidth() + 10) + padding.getRight();
		final double height = padding.getTop() + dimTitle.getHeight() + hh + padding.getBottom();

		final TextBlock frame = new BigFrame(title, width, height, symbolContext);

		return new TextBlock() {

			public void drawU(UGraphic ug) {
				frame.drawU(ug.apply(margin.getTranslate()));
				original.drawU(ug.apply(margin.getTranslate().compose(padding.getTranslate().compose(delta))));
			}

			@Override
			public XRectangle2D getInnerPosition(CharSequence member, StringBounder sb) {
				final XRectangle2D rect = original.getInnerPosition(member, sb);
				return new XRectangle2D(dx + rect.getX() + margin.getLeft() + padding.getLeft(),
						dy + rect.getY() + margin.getTop() + padding.getTop() + dimTitle.getHeight(), rect.getWidth(),
						rect.getHeight());
			}

			public XDimension2D calculateDimension(StringBounder sb) {
				final XDimension2D dim1 = original.calculateDimension(sb);
				final XDimension2D dim2 = padding.apply(dim1);
				final XDimension2D dim3 = margin.apply(dim2);
				return dim3;
			}

			public HColor getBackcolor() {
				return symbolContext.getBackColor();
			}
		};
	}

	// -----------------------------------------------------------------------
	// Legend
	// -----------------------------------------------------------------------

	private static TextBlock addLegend(TextBlock original, Annotated annotated, ISkinParam skinParam) {

		final DisplayPositioned legend = annotated.getLegend();
		if (legend.isNull())
			return original;

		final UGroup group = new UGroup(legend.getLineLocation());
		group.put(UGroupType.CLASS, "legend");

		final TextBlock legendBlock = EntityImageLegend.create(legend.getDisplay(), skinParam);
		return DecorateEntityImage.add(group, original, legendBlock, legend.getHorizontalAlignment(),
				legend.getVerticalAlignment());
	}

	// -----------------------------------------------------------------------
	// Title
	// -----------------------------------------------------------------------

	private static TextBlock addTitle(TextBlock original, Annotated annotated, ISkinParam skinParam) {

		final DisplayPositioned title = (DisplayPositioned) annotated.getTitle();
		if (title.isNull())
			return original;

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.title)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final TextBlock titleBlock = style.createTextBlockBordered(title.getDisplay(), skinParam.getIHtmlColorSet(),
				skinParam, Style.ID_TITLE, LineBreakStrategy.NONE);

		final UGroup group = new UGroup(title.getLineLocation());
		group.put(UGroupType.CLASS, "title");
		return DecorateEntityImage.addTop(group, original, titleBlock, HorizontalAlignment.CENTER);
	}

	// -----------------------------------------------------------------------
	// Caption
	// -----------------------------------------------------------------------

	private static TextBlock addCaption(TextBlock original, Annotated annotated, ISkinParam skinParam) {

		final DisplayPositioned caption = annotated.getCaption();
		if (caption.isNull())
			return original;

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.caption)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final TextBlock captionBlock = style.createTextBlockBordered(caption.getDisplay(), skinParam.getIHtmlColorSet(),
				skinParam, Style.ID_CAPTION, LineBreakStrategy.NONE);

		final UGroup group = new UGroup(caption.getLineLocation());
		group.put(UGroupType.CLASS, "caption");
		return DecorateEntityImage.addBottom(group, original, captionBlock, HorizontalAlignment.CENTER);
	}

	// -----------------------------------------------------------------------
	// Header & Footer
	// -----------------------------------------------------------------------

	private static TextBlock addHeaderAndFooter(TextBlock original, Annotated annotated, ISkinParam skinParam) {

		final DisplayPositioned header = annotated.getHeader();
		final DisplayPositioned footer = annotated.getFooter();
		if (footer.isNull() && header.isNull())
			return original;

		final UGroup group1 = new UGroup(header.getLineLocation());
		group1.put(UGroupType.CLASS, "header");

		TextBlock textHeader = null;
		if (!header.isNull()) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.header)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			textHeader = header.createRibbon(FontConfiguration.create(skinParam, FontParam.HEADER, null), skinParam,
					style);
		}

		final UGroup group2 = new UGroup(footer.getLineLocation());
		group2.put(UGroupType.CLASS, "footer");

		TextBlock textFooter = null;
		if (!footer.isNull()) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.footer)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			textFooter = footer.createRibbon(FontConfiguration.create(skinParam, FontParam.FOOTER, null), skinParam,
					style);
		}

		return DecorateEntityImage.addTopAndBottom(original, group1, textHeader, header.getHorizontalAlignment(),
				group2, textFooter, footer.getHorizontalAlignment());
	}

}
