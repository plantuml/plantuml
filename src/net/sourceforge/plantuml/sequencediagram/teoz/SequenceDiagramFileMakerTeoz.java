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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.MathUtils;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormatOption fileFormatOption;
	private final Rose skin;
	private final AnnotatedWorker annotatedWorker;
	private final int index;

	public SequenceDiagramFileMakerTeoz(SequenceDiagram sequenceDiagram, Rose skin, FileFormatOption fileFormatOption,
			int index) {
		this.index = index;
		this.stringBounder = fileFormatOption.getDefaultStringBounder();
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		this.skin = skin;
		this.body = new PlayingSpaceWithParticipants(createMainTile());
		this.footer = getFooterOrHeader(FontParam.FOOTER);
		this.header = getFooterOrHeader(FontParam.HEADER);
		this.annotatedWorker = new AnnotatedWorker(sequenceDiagram, sequenceDiagram.getSkinParam(), stringBounder);

		this.min1 = body.getMinX(stringBounder);

		this.title = getTitle();
		this.legend = getLegend();
		this.caption = annotatedWorker.getCaption();

		this.heightEnglober1 = englobers.getOffsetForEnglobers(stringBounder);
		this.heightEnglober2 = heightEnglober1 == 0 ? 0 : 10;

		final double totalWidth = MathUtils.max(body.calculateDimension(stringBounder).getWidth(),
				title.calculateDimension(stringBounder).getWidth(), footer.calculateDimension(stringBounder).getWidth(),
				header.calculateDimension(stringBounder).getWidth(),
				legend.calculateDimension(stringBounder).getWidth());
		final double totalHeight = body.calculateDimension(stringBounder).getHeight() + heightEnglober1
				+ heightEnglober2 + title.calculateDimension(stringBounder).getHeight()
				+ header.calculateDimension(stringBounder).getHeight()
				+ legend.calculateDimension(stringBounder).getHeight()
				+ footer.calculateDimension(stringBounder).getHeight() + (annotatedWorker.hasMainFrame() ? 10 : 0);
		this.dimTotal = new Dimension2DDouble(totalWidth, totalHeight);
	}

	private Englobers englobers;
	private final StringBounder stringBounder;

	private final TextBlock footer;
	private final TextBlock header;

	private final PlayingSpaceWithParticipants body;

	private final TextBlock title;
	private final TextBlock legend;
	private final TextBlock caption;
	private final Dimension2D dimTotal;
	private final Real min1;

	private final LivingSpaces livingSpaces = new LivingSpaces();
	private final double heightEnglober1;
	private final double heightEnglober2;

	private double oneOf(double a, double b) {
		if (a == 1) {
			return b;
		}
		return a;
	}

	public ImageData createOne(OutputStream os, final int index, boolean isWithMetadata) throws IOException {
		if (this.index != index) {
			throw new IllegalStateException();
		}
		final double dpiFactor = diagram.getDpiFactor(fileFormatOption, dimTotal);

		final double scale = 1;
		final String metadata = fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null;

		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(3);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 3;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildD(diagram.getSkinParam(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2), diagram.getAnimation(), metadata,
		null, oneOf(scale, dpiFactor));

		imageBuilder.setUDrawable(new Foo(index));
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, diagram.seed(), os);

	}

	class Foo implements UDrawable {

		private final int index;

		Foo(int index) {
			this.index = index;
		}

		public void drawU(UGraphic ug) {
			drawInternal(ug, index);
		}

	}

	private UGraphic goDownAndCenterForEnglobers(UGraphic ug) {
		ug = goDown(ug, title);
		ug = goDown(ug, header);
		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.TOP) {
			ug = goDown(ug, legend);
		}
		final double dx = (dimTotal.getWidth() - body.calculateDimension(stringBounder).getWidth()) / 2;
		return ug.apply(UTranslate.dx(dx));
	}

	private UGraphic goDown(UGraphic ug, TextBlock size) {
		return ug.apply(UTranslate.dy(size.calculateDimension(stringBounder).getHeight()));
	}

	public void printAligned(UGraphic ug, HorizontalAlignment align, final TextBlock layer) {
		double dx = 0;
		if (align == HorizontalAlignment.RIGHT) {
			dx = dimTotal.getWidth() - layer.calculateDimension(stringBounder).getWidth();
		} else if (align == HorizontalAlignment.CENTER) {
			dx = (dimTotal.getWidth() - layer.calculateDimension(stringBounder).getWidth()) / 2;
		}
		layer.drawU(ug.apply(UTranslate.dx(dx)));
	}

	private PlayingSpace createMainTile() {
		final RealOrigin origin = RealUtils.createOrigin();
		Real currentPos = origin.addAtLeast(0);
		for (Participant p : diagram.participants()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, getSkinParam(), currentPos,
					diagram.events());
			livingSpaces.put(p, livingSpace);
			currentPos = livingSpace.getPosD(stringBounder).addAtLeast(0);
		}

		final TileArguments tileArguments = new TileArguments(stringBounder, livingSpaces, skin, diagram.getSkinParam(),
				origin);

		this.englobers = new Englobers(tileArguments);
		final PlayingSpace mainTile = new PlayingSpace(diagram, englobers, tileArguments);
		this.livingSpaces.addConstraints(stringBounder);
		mainTile.addConstraints(stringBounder);
		this.englobers.addConstraints(stringBounder);
		origin.compileNow();
		tileArguments.setBordered(mainTile);
		return mainTile;
	}

	public ISkinParam getSkinParam() {
		return diagram.getSkinParam();
	}

	private TextBlock getTitle() {
		if (diagram.getTitle().isNull()) {
			return new ComponentAdapter(null);
		}
		final TextBlock compTitle;
		if (SkinParam.USE_STYLES()) {
			final Style style = StyleSignature.of(SName.root, SName.title)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			compTitle = style.createTextBlockBordered(diagram.getTitle().getDisplay(),
					diagram.getSkinParam().getIHtmlColorSet(), diagram.getSkinParam());
			return compTitle;
		} else {
			compTitle = TextBlockUtils.title(new FontConfiguration(getSkinParam(), FontParam.TITLE, null),
					diagram.getTitle().getDisplay(), getSkinParam());
			return TextBlockUtils.withMargin(compTitle, 7, 7);
		}
	}

	private TextBlock getLegend() {
		final Display legend = diagram.getLegend().getDisplay();
		if (Display.isNull(legend)) {
			return TextBlockUtils.empty(0, 0);
		}
		return EntityImageLegend.create(legend, diagram.getSkinParam());
	}

	public TextBlock getFooterOrHeader(final FontParam param) {
		if (diagram.getFooterOrHeaderTeoz(param).isNull()) {
			return new TeozLayer(null, stringBounder, param);
		}
		final DisplaySection display = diagram.getFooterOrHeaderTeoz(param).withPage(index + 1, getNbPages());
		final HColor hyperlinkColor = getSkinParam().getHyperlinkColor();
		final HColor titleColor = getSkinParam().getFontHtmlColor(null, param);
		final String fontFamily = getSkinParam().getFont(null, false, param).getFamily(null);
		final int fontSize = getSkinParam().getFont(null, false, param).getSize();
		Style style = null;
		final ISkinParam skinParam = diagram.getSkinParam();
		if (SkinParam.USE_STYLES()) {
			final StyleSignature def = param.getStyleDefinition();
			style = def.getMergedStyle(skinParam.getCurrentStyleBuilder());
		}
		final PngTitler pngTitler = new PngTitler(titleColor, display, fontSize, fontFamily, hyperlinkColor,
				getSkinParam().useUnderlineForHyperlink(), style, skinParam.getIHtmlColorSet(), skinParam);
		return new TeozLayer(pngTitler, stringBounder, param);
	}

	public int getNbPages() {
		return body.getNbPages();
	}

	private void drawInternal(UGraphic ug, int index) {
		body.setIndex(index);
		final UTranslate min1translate = UTranslate.dx(-min1.getCurrentValue());
		ug = ug.apply(min1translate);

		englobers.drawEnglobers(goDownAndCenterForEnglobers(ug),
				body.calculateDimension(stringBounder).getHeight() + heightEnglober1 + heightEnglober2 / 2,
				new SimpleContext2D(true));

		printAligned(ug, diagram.getFooterOrHeaderTeoz(FontParam.HEADER).getHorizontalAlignment(), header);
		ug = goDown(ug, header);

		HorizontalAlignment titleAlignment = HorizontalAlignment.CENTER;
		if (SkinParam.USE_STYLES()) {
			final StyleSignature def = FontParam.TITLE.getStyleDefinition();
			titleAlignment = def.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder())
					.getHorizontalAlignment();
		}
		printAligned(ug, titleAlignment, title);
		ug = goDown(ug, title);

		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.TOP) {
			printAligned(ug, diagram.getLegend().getHorizontalAlignment(), legend);
			ug = goDown(ug, legend);
		}

		ug = ug.apply(UTranslate.dy(heightEnglober1));
		final TextBlock bodyFramed = annotatedWorker.addFrame(body);
		printAligned(ug.apply(UTranslate.dx((annotatedWorker.hasMainFrame() ? 4 : 0))), HorizontalAlignment.CENTER,
				bodyFramed);
		ug = goDown(ug, bodyFramed);
		ug = ug.apply(UTranslate.dy(heightEnglober2));

		printAligned(ug, HorizontalAlignment.CENTER, caption);

		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.BOTTOM) {
			printAligned(ug, diagram.getLegend().getHorizontalAlignment(), legend);
			ug = goDown(ug, legend);
		}

		printAligned(ug, diagram.getFooterOrHeaderTeoz(FontParam.FOOTER).getHorizontalAlignment(), footer);
	}

}
