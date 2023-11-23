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

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.BigFrame;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.DecorateEntityImage;

public class AnnotatedBuilder {
	// ::remove file when __HAXE__

	private final Annotated annotated;
	private final ISkinParam skinParam;
	private final StringBounder stringBounder;

	public AnnotatedBuilder(Annotated annotated, ISkinParam skinParam, StringBounder stringBounder) {
		this.annotated = annotated;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
	}

	public boolean hasMainFrame() {
		return annotated.getMainFrame() != null;
	}

	public double mainFrameSuppHeight() {
		final Display mainFrame = annotated.getMainFrame();
		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.mainframe)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = FontConfiguration.create(getSkinParam(), style);
		final TextBlock title = mainFrame.create(fontConfiguration, HorizontalAlignment.CENTER, getSkinParam());
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);

		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding().incTop(dimTitle.getHeight() + 10);

		return margin.getBottom() + margin.getTop() + padding.getTop() + padding.getBottom() + 10;
	}

	public TextBlock decoreWithFrame(final TextBlock original) {
		final Display mainFrame = annotated.getMainFrame();
		if (mainFrame == null)
			return original;

//		final double x1 = 5;
//		final double x2 = 7;
//		final double y1 = 10;
//		final double y2 = 10;

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.mainframe)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = FontConfiguration.create(getSkinParam(), style);
		final TextBlock title = mainFrame.create(fontConfiguration, HorizontalAlignment.CENTER, getSkinParam());
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

		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				frame.drawU(ug.apply(margin.getTranslate()));
				original.drawU(ug.apply(margin.getTranslate().compose(padding.getTranslate().compose(delta))));
			}

			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				final XRectangle2D rect = original.getInnerPosition(member, stringBounder, strategy);
				return new XRectangle2D(dx + rect.getX() + margin.getLeft() + padding.getLeft(),
						dy + rect.getY() + margin.getTop() + padding.getTop() + dimTitle.getHeight(), rect.getWidth(),
						rect.getHeight());
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dim1 = original.calculateDimension(stringBounder);
				final XDimension2D dim2 = padding.apply(dim1);
				final XDimension2D dim3 = margin.apply(dim2);
				return dim3;
			}

			public HColor getBackcolor() {
				return symbolContext.getBackColor();
			}
		};
	}

	private ISkinParam getSkinParam() {
		return skinParam;
	}

	public TextBlock getLegend() {
		final DisplayPositioned legend = annotated.getLegend();
		return EntityImageLegend.create(legend.getDisplay(), getSkinParam());
	}

	public TextBlock getTitle() {
		final DisplayPositioned title = (DisplayPositioned) annotated.getTitle();
		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.title)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		final TextBlock block = style.createTextBlockBordered(title.getDisplay(), skinParam.getIHtmlColorSet(),
				skinParam, Style.ID_TITLE, LineBreakStrategy.NONE);
		return block;
	}

	public TextBlock getCaption() {
		final DisplayPositioned caption = annotated.getCaption();
		if (caption.isNull())
			return TextBlockUtils.empty(0, 0);

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.caption)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		return style.createTextBlockBordered(caption.getDisplay(), skinParam.getIHtmlColorSet(), skinParam,
				Style.ID_CAPTION, LineBreakStrategy.NONE);

	}

	public TextBlock addHeaderAndFooter(TextBlock original) {
		final DisplaySection footer = annotated.getFooter();
		final DisplaySection header = annotated.getHeader();
		if (footer.isNull() && header.isNull())
			return original;

		TextBlock textFooter = null;
		if (footer.isNull() == false) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.footer)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			textFooter = footer.createRibbon(FontConfiguration.create(getSkinParam(), FontParam.FOOTER, null),
					getSkinParam(), style);
		}
		TextBlock textHeader = null;
		if (header.isNull() == false) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.header)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			textHeader = header.createRibbon(FontConfiguration.create(getSkinParam(), FontParam.HEADER, null),
					getSkinParam(), style);
		}

		return DecorateEntityImage.addTopAndBottom(original, textHeader, header.getHorizontalAlignment(), textFooter,
				footer.getHorizontalAlignment());
	}

}
