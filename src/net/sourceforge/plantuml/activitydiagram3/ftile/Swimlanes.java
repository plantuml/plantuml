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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.InstructionList;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorAddNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorAddUrl;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorAssembly;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorCreateGroup;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorCreateParallel;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorIf;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorRepeat;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorSwitch;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorWhile;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.UGraphicInterceptorOneSwimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.VCompactFactory;
import net.sourceforge.plantuml.activitydiagram3.gtile.GConnection;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.compress.CompressionMode;
import net.sourceforge.plantuml.klimt.compress.SlotFinder;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicDelegator;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Styleable;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.utils.MathUtils;

public class Swimlanes extends AbstractTextBlock implements TextBlock, Styleable {

	private final ISkinParam skinParam;;
	private final Pragma pragma;

	private final List<Swimlane> swimlanesRaw = new ArrayList<>();
	private final List<Swimlane> swimlanesSpecial = new ArrayList<>();
	private final List<LaneDivider> dividers = new ArrayList<>();
	private Swimlane currentSwimlane = null;

	private final Instruction root = InstructionList.empty();
	private Instruction currentInstruction = root;

	private LinkRendering nextLinkRenderer = LinkRendering.none();
	private Style style;

	private List<Swimlane> swimlanes() {
		return Collections.unmodifiableList(swimlanesRaw);
	}

	private List<Swimlane> swimlanesSpecial() {
		if (swimlanesSpecial.size() == 0) {
			swimlanesSpecial.addAll(swimlanesRaw);
			final Swimlane last = new Swimlane("", Integer.MAX_VALUE);
			last.setMinMax(MinMax.getEmpty(true));
			swimlanesSpecial.add(last);
		}
		return Collections.unmodifiableList(swimlanesSpecial);
	}

	public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.swimlane);
	}

	public Swimlanes(ISkinParam skinParam, Pragma pragma) {
		this.skinParam = skinParam;
		this.pragma = pragma;
	}

	protected Style getStyle() {
		if (style == null)
			this.style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());

		return style;
	}

	private FtileFactory getFtileFactory(StringBounder stringBounder) {
		FtileFactory factory = new VCompactFactory(skinParam, stringBounder);
		factory = new FtileFactoryDelegatorAddUrl(factory);
		factory = new FtileFactoryDelegatorAssembly(factory);
		factory = new FtileFactoryDelegatorIf(factory, pragma);
		factory = new FtileFactoryDelegatorSwitch(factory);
		factory = new FtileFactoryDelegatorWhile(factory);
		factory = new FtileFactoryDelegatorRepeat(factory);
		factory = new FtileFactoryDelegatorCreateParallel(factory);
		// factory = new FtileFactoryDelegatorCreateParallelAddingMargin(new
		// FtileFactoryDelegatorCreateParallel1(factory));
		factory = new FtileFactoryDelegatorAddNote(factory);
		factory = new FtileFactoryDelegatorCreateGroup(factory);
		return factory;
	}

	public void swimlane(String name, HColor color, Display label) {
		currentSwimlane = getOrCreate(name);
		if (color != null)
			currentSwimlane.setSpecificColorTOBEREMOVED(ColorType.BACK, color);

		if (Display.isNull(label) == false)
			currentSwimlane.setDisplay(label);

	}

	private Swimlane getOrCreate(String name) {
		for (Swimlane s : swimlanes())
			if (s.getName().equals(name))
				return s;

		final Swimlane result = new Swimlane(name, swimlanesRaw.size());
		swimlanesRaw.add(result);
		return result;
	}

	class Cross extends UGraphicDelegator {

		private Cross(UGraphic ug) {
			super(ug);
		}

		@Override
		public void draw(UShape shape) {
			if (shape instanceof Ftile) {
				final Ftile tile = (Ftile) shape;
				tile.drawU(this);
			} else if (shape instanceof Connection) {
				final Connection connection = (Connection) shape;
				final Ftile tile1 = connection.getFtile1();
				final Ftile tile2 = connection.getFtile2();

				if (tile1 == null || tile2 == null)
					return;

				if (tile1.getSwimlaneOut() != tile2.getSwimlaneIn()) {
					final ConnectionCross connectionCross = new ConnectionCross(connection);
					connectionCross.drawU(getUg());
				}
				// ::comment when __CORE__
			} else if (shape instanceof Gtile) {
				final Gtile tile = (Gtile) shape;
				tile.drawU(this);
			} else if (shape instanceof GConnection) {
				final GConnection connection = (GConnection) shape;
				System.err.println("CROSS IN SWIMLANES");
				connection.drawTranslatable(getUg());
				// connection.drawU(this);
				// throw new UnsupportedOperationException();
				// ::done
			}
		}

		public UGraphic apply(UChange change) {
			return new Cross(getUg().apply(change));
		}

	}

	public final void computeSize(StringBounder stringBounder) {
		final SlotFinder ug = SlotFinder.create(CompressionMode.ON_Y, stringBounder);
		if (swimlanes().size() > 1) {
			TextBlock full = root.createFtile(getFtileFactory(stringBounder));
			computeSizeInternal(ug, full);
		}

	}

	public final void drawU(UGraphic ug) {
		// ::comment when __CORE__
		if (Gtile.USE_GTILE) {
			drawGtile(ug);
			return;
		}
		// ::done

		TextBlock full = root.createFtile(getFtileFactory(ug.getStringBounder()));

		ug = new UGraphicForSnake(ug);
		if (swimlanes().size() > 1) {
			drawWhenSwimlanes(ug, full);
		} else {
			// BUG42
			full = new TextBlockInterceptorUDrawable(full);
			full.drawU(ug);
			ug.flushUg();
		}
	}

	// ::comment when __CORE__
	private void drawGtile(UGraphic ug) {
		TextBlock full = root.createGtile(skinParam, ug.getStringBounder());

		ug = new UGraphicForSnake(ug);
		if (swimlanes().size() > 1) {
			drawWhenSwimlanes(ug, full);
		} else {
			full = new TextBlockInterceptorUDrawable(full);
			full.drawU(ug);
			ug.flushUg();
		}

	}
	// ::done

	private TextBlock getTitle(Swimlane swimlane) {
		final HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
		final FontConfiguration fontConfiguration = getStyle().getFontConfiguration(skinParam.getIHtmlColorSet());

		LineBreakStrategy wrap = getWrap();
		if (wrap.isAuto())
			wrap = new LineBreakStrategy("" + ((int) swimlane.getActualWidth()));

		return swimlane.getDisplay().create9(fontConfiguration, horizontalAlignment, skinParam, wrap);
	}

	private LineBreakStrategy getWrap() {
		LineBreakStrategy wrap = skinParam.swimlaneWrapTitleWidth();
		if (wrap == LineBreakStrategy.NONE)
			wrap = style.wrapWidth();

		return wrap;
	}

	private UTranslate getTitleHeightTranslate(final StringBounder stringBounder) {
		double titlesHeight = getTitlesHeight(stringBounder);
		return UTranslate.dy(titlesHeight > 0 ? titlesHeight + 5 : 0);
	}

	private double getTitlesHeight(StringBounder stringBounder) {
		double titlesHeight = 0;
		for (Swimlane swimlane : swimlanes()) {
			final TextBlock swTitle = getTitle(swimlane);
			titlesHeight = Math.max(titlesHeight, swTitle.calculateDimension(stringBounder).getHeight());
		}
		return titlesHeight;
	}

	private void drawWhenSwimlanes(UGraphic ug, TextBlock full) {
		final StringBounder stringBounder = ug.getStringBounder();
		final UTranslate titleHeightTranslate = getTitleHeightTranslate(stringBounder);

		drawTitlesBackground(ug);

		final XDimension2D dimensionFull = full.calculateDimension(stringBounder);
		int i = 0;
		assert dividers.size() == swimlanes().size() + 1;
		for (Swimlane swimlane : swimlanesSpecial()) {
			final LaneDivider divider1 = dividers.get(i);

			final double xpos = swimlane.getTranslate().getDx() + swimlane.getMinMax().getMinX();
			final HColor back = swimlane.getColors().getColor(ColorType.BACK);
			if (back != null && back.isTransparent() == false) {
				final LaneDivider divider2 = dividers.get(i + 1);
				final UGraphic background = ug.apply(back.bg()).apply(back)
						.apply(UTranslate.dx(xpos - divider1.getX2()));
				final double width = swimlane.getActualWidth() + divider1.getX2() + divider2.getX1();
				final double height = dimensionFull.getHeight() + titleHeightTranslate.getDy();
				background.draw(URectangle.build(width, height).ignoreForCompressionOnX().ignoreForCompressionOnY());
			}

			full.drawU(new UGraphicInterceptorOneSwimlane(ug, swimlane, swimlanes()).apply(swimlane.getTranslate())
					.apply(getTitleHeightTranslate(stringBounder)));

			final double dividerWith = divider1.calculateDimension(stringBounder).getWidth();
			divider1.drawU(ug.apply(UTranslate.dx(xpos - dividerWith)));
			i++;
		}

		final Cross cross = new Cross(ug.apply(getTitleHeightTranslate(stringBounder)));
		full.drawU(cross);
		cross.flushUg();

		drawTitles(ug);

	}

	private void drawTitlesBackground(UGraphic ug) {
		final HColor color = getStyle().value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		if (color != null) {
			final double titleHeight = getTitlesHeight(ug.getStringBounder());
			double fullWidth = swimlanesSpecial().get(swimlanesSpecial().size() - 1).getTranslate().getDx() - 2 * 5 - 1;
			final URectangle back = URectangle.build(fullWidth, titleHeight).ignoreForCompressionOnX()
					.ignoreForCompressionOnY();
			ug.apply(UTranslate.dx(5)).apply(color.bg()).apply(color).draw(back);
		}
	}

	private void drawTitles(UGraphic ug) {
		for (Swimlane swimlane : swimlanes()) {
			final TextBlock swTitle = getTitle(swimlane);
			final double x2 = swimlane.getTranslate().getDx() + swimlane.getMinMax().getMinX();
			final CenteredText centeredText = new CenteredText(swTitle, getWidthWithoutTitle(swimlane));
			ug.apply(UTranslate.dx(x2)).draw(centeredText);
		}
	}

	private void computeDrawingWidths(UGraphic ug, TextBlock full) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Swimlane swimlane : swimlanes()) {
			final LimitFinder limitFinder = LimitFinder.create(stringBounder, false);
			final UGraphicInterceptorOneSwimlane interceptor = new UGraphicInterceptorOneSwimlane(
					new UGraphicForSnake(limitFinder), swimlane, swimlanes());
			full.drawU(interceptor);
			interceptor.flushUg();
			final MinMax minMax = limitFinder.getMinMax();
			swimlane.setMinMax(minMax);
		}
	}

	private void computeSizeInternal(UGraphic ug, TextBlock full) {
		computeDrawingWidths(ug, full);

		double min = skinParam.swimlaneWidth();

		if (min == ISkinParam.SWIMLANE_WIDTH_SAME)
			for (Swimlane swimlane : swimlanes())
				min = Math.max(min, getWidthWithoutTitle(swimlane));

		final StringBounder stringBounder = ug.getStringBounder();

		for (int i = 0; i < swimlanesSpecial().size(); i++) {
			final Swimlane swimlane = swimlanesSpecial().get(i);
			final double swimlaneActualWidth = MathUtils.max(min, getWidthWithoutTitle(swimlane));
			swimlane.setWidth(swimlaneActualWidth);
		}

		final UTranslate titleHeightTranslate = getTitleHeightTranslate(stringBounder);
		final XDimension2D dimensionFull = full.calculateDimension(stringBounder);

		dividers.clear();
		double xpos = 0;
		for (int i = 0; i < swimlanesSpecial().size(); i++) {
			final Swimlane swimlane = swimlanesSpecial().get(i);
			double x1 = getHalfMissingSpace(stringBounder, i, min);
			double x2 = getHalfMissingSpace(stringBounder, i + 1, min);
			final LaneDivider laneDivider = new LaneDivider(skinParam, x1, x2,
					dimensionFull.getHeight() + titleHeightTranslate.getDy());
			dividers.add(laneDivider);

			final double xx = xpos + laneDivider.getWidth() - swimlane.getMinMax().getMinX()
					+ (swimlane.getActualWidth() - getWidthWithoutTitle(swimlane)) / 2.0;
			swimlane.setTranslate(UTranslate.dx(xx));

			xpos += swimlane.getActualWidth() + laneDivider.getWidth();
		}
		assert dividers.size() == swimlanes().size() + 1;
	}

	public double getHalfMissingSpace(StringBounder stringBounder, int i, double min) {
		if (i == 0 || i > swimlanesSpecial().size())
			return 5;

		final Swimlane swimlane = swimlanesSpecial().get(i - 1);
		final double swimlaneActualWidth = Math.max(min, getWidthWithoutTitle(swimlane));
		final double titleWidth = getTitle(swimlane).calculateDimension(stringBounder).getWidth();
		if (titleWidth <= swimlaneActualWidth)
			return 5;

		assert titleWidth > swimlaneActualWidth;
		return Math.max(5, 5 + (titleWidth - swimlaneActualWidth) / 2);
	}

	private double getWidthWithoutTitle(Swimlane swimlane) {
		return swimlane.getMinMax().getWidth();
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getMinMax(stringBounder).getDimension();
	}

	public Instruction getCurrent() {
		return currentInstruction;
	}

	public void setCurrent(Instruction current) {
		this.currentInstruction = current;
	}

	public LinkRendering nextLinkRenderer() {
		return nextLinkRenderer;
	}

	public void setNextLinkRenderer(LinkRendering link) {
		this.nextLinkRenderer = Objects.requireNonNull(link);
	}

	public Swimlane getCurrentSwimlane() {
		return currentSwimlane;
	}

	private MinMax cachedMinMax;

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		if (cachedMinMax == null)
			cachedMinMax = TextBlockUtils.getMinMax(this, stringBounder, false);

		return cachedMinMax;
	}

	public StyleBuilder getCurrentStyleBuilder() {
		return skinParam.getCurrentStyleBuilder();
	}

}
