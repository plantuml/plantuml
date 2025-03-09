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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.MathUtils;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormatOption fileFormatOption;
	private final Rose skin;
	private final AnnotatedBuilder annotatedBuilder;

	private final int index;

	public SequenceDiagramFileMakerTeoz(SequenceDiagram diagram, Rose skin, FileFormatOption fileFormatOption,
			int index) {
		this.index = index;
		this.stringBounder = fileFormatOption.getDefaultStringBounder(diagram.getSkinParam());
		this.diagram = diagram;
		this.fileFormatOption = fileFormatOption;
		this.skin = skin;
		this.body = new PlayingSpaceWithParticipants(createMainTile());
		this.footer = getFooterOrHeader(FontParam.FOOTER);
		this.header = getFooterOrHeader(FontParam.HEADER);
		this.annotatedBuilder = new AnnotatedBuilder(diagram, diagram.getSkinParam(), stringBounder);

		this.min1 = body.getMinX(stringBounder);

		this.title = getTitle();
		this.legend = getLegend();
		this.caption = annotatedBuilder.getCaption();

		this.heightEnglober1 = dolls.getOffsetForEnglobers(stringBounder);
		this.heightEnglober2 = heightEnglober1 == 0 ? 0 : 10;

		final double totalWidth = MathUtils.max(body.calculateDimension(stringBounder).getWidth(),
				title.calculateDimension(stringBounder).getWidth(), footer.calculateDimension(stringBounder).getWidth(),
				header.calculateDimension(stringBounder).getWidth(),
				legend.calculateDimension(stringBounder).getWidth());
		final double totalHeight = body.calculateDimension(stringBounder).getHeight() + heightEnglober1
				+ heightEnglober2 + title.calculateDimension(stringBounder).getHeight()
				+ header.calculateDimension(stringBounder).getHeight()
				+ legend.calculateDimension(stringBounder).getHeight()
				+ caption.calculateDimension(stringBounder).getHeight()
				+ footer.calculateDimension(stringBounder).getHeight() + (annotatedBuilder.hasMainFrame() ? 10 : 0);
		this.dimTotal = new XDimension2D(totalWidth, totalHeight);
	}

	private Dolls dolls;
	private final StringBounder stringBounder;

	private final TextBlock footer;
	private final TextBlock header;

	private final PlayingSpaceWithParticipants body;

	private final TextBlock title;
	private final TextBlock legend;
	private final TextBlock caption;
	private final XDimension2D dimTotal;
	private final Real min1;

	private final LivingSpaces livingSpaces = new LivingSpaces();
	private final double heightEnglober1;
	private final double heightEnglober2;

	public ImageData createOne(OutputStream os, final int index, boolean isWithMetadata) throws IOException {
		if (this.index != index)
			throw new IllegalStateException();

		return diagram.createImageBuilder(fileFormatOption).drawable(new Foo(index)).write(os);
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
		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.TOP)
			ug = goDown(ug, legend);

		final double dx = (dimTotal.getWidth() - body.calculateDimension(stringBounder).getWidth()) / 2;
		return ug.apply(UTranslate.dx(dx));
	}

	private UGraphic goDown(UGraphic ug, TextBlock size) {
		return ug.apply(UTranslate.dy(size.calculateDimension(stringBounder).getHeight()));
	}

	public void printAligned(UGraphic ug, HorizontalAlignment align, final TextBlock layer) {
		double dx = 0;
		if (align == HorizontalAlignment.RIGHT)
			dx = dimTotal.getWidth() - layer.calculateDimension(stringBounder).getWidth();
		else if (align == HorizontalAlignment.CENTER)
			dx = (dimTotal.getWidth() - layer.calculateDimension(stringBounder).getWidth()) / 2;

		layer.drawU(ug.apply(UTranslate.dx(dx)));
	}

	private PlayingSpace createMainTile() {
		final RealOrigin xorigin = RealUtils.createOrigin();
		Real xcurrent = xorigin.addAtLeast(0);
		final RealOrigin yorigin = RealUtils.createOrigin();
		for (Participant p : diagram.participants()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, getSkinParam(), xcurrent,
					diagram.events());
			livingSpaces.put(p, livingSpace);
			xcurrent = livingSpace.getPosD(stringBounder).addAtLeast(0);
		}

		final TileArguments tileArguments = new TileArguments(stringBounder, livingSpaces, skin, diagram.getSkinParam(),
				xorigin, yorigin);

		this.dolls = new Dolls(tileArguments);
		final PlayingSpace mainTile = new PlayingSpace(diagram, dolls, tileArguments);
		this.livingSpaces.addConstraints(stringBounder);
		mainTile.addConstraints();
		this.dolls.addConstraints(stringBounder);
		xorigin.compileNow();
		if (YGauge.USE_ME)
			System.err.println("COMPILING Y");
		yorigin.compileNow();
		tileArguments.setBordered(mainTile);
		return mainTile;
	}

	public ISkinParam getSkinParam() {
		return diagram.getSkinParam();
	}

	private TextBlock getTitle() {
		if (diagram.getTitle().isNull())
			return new ComponentAdapter(null);

		final Style style = StyleSignatureBasic.of(SName.root, SName.document, SName.title)
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
		final TextBlock compTitle = style.createTextBlockBordered(diagram.getTitle().getDisplay(),
				diagram.getSkinParam().getIHtmlColorSet(), diagram.getSkinParam(), Style.ID_TITLE,
				LineBreakStrategy.NONE);
		return compTitle;

	}

	private TextBlock getLegend() {
		final Display legend = diagram.getLegend().getDisplay();
		if (Display.isNull(legend))
			return TextBlockUtils.empty(0, 0);

		return EntityImageLegend.create(legend, diagram.getSkinParam());
	}

	public TextBlock getFooterOrHeader(final FontParam param) {
		if (diagram.getFooterOrHeaderTeoz(param).isNull())
			return new TeozLayer(null, stringBounder, param);

		final DisplaySection display = diagram.getFooterOrHeaderTeoz(param).withPage(index + 1, getNbPages());
		final ISkinParam skinParam = diagram.getSkinParam();

		final StyleSignatureBasic def = param.getStyleDefinition(null);
		final Style style = def.getMergedStyle(skinParam.getCurrentStyleBuilder());

		final PngTitler pngTitler = new PngTitler(display, style, skinParam.getIHtmlColorSet(), skinParam);
		return new TeozLayer(pngTitler, stringBounder, param);
	}

	public int getNbPages() {
		return body.getNbPages();
	}

	private void drawInternal(UGraphic ug, int index) {
		body.setIndex(index);
		final UTranslate min1translate = UTranslate.dx(-min1.getCurrentValue());
		ug = ug.apply(min1translate);

		dolls.drawEnglobers(goDownAndCenterForEnglobers(ug),
				body.calculateDimension(stringBounder).getHeight() + heightEnglober1 + heightEnglober2 / 2,
				new SimpleContext2D(true));

		printAligned(ug, diagram.getFooterOrHeaderTeoz(FontParam.HEADER).getHorizontalAlignment(), header);
		ug = goDown(ug, header);

		final StyleSignatureBasic def = FontParam.TITLE.getStyleDefinition(null);
		final HorizontalAlignment titleAlignment = def.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder())
				.getHorizontalAlignment();

		printAligned(ug, titleAlignment, title);
		ug = goDown(ug, title);

		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.TOP) {
			printAligned(ug, diagram.getLegend().getHorizontalAlignment(), legend);
			ug = goDown(ug, legend);
		}

		ug = ug.apply(UTranslate.dy(heightEnglober1));
		final TextBlock bodyFramed = annotatedBuilder.decoreWithFrame(body);
		printAligned(ug.apply(UTranslate.dx((annotatedBuilder.hasMainFrame() ? 4 : 0))), HorizontalAlignment.CENTER,
				bodyFramed);
		ug = goDown(ug, bodyFramed);
		ug = ug.apply(UTranslate.dy(heightEnglober2));

		if (diagram.getLegend().getVerticalAlignment() == VerticalAlignment.BOTTOM) {
			printAligned(ug, diagram.getLegend().getHorizontalAlignment(), legend);
			ug = goDown(ug, legend);
		}
		printAligned(ug, HorizontalAlignment.CENTER, caption);
		ug = goDown(ug, caption);

		printAligned(ug, diagram.getFooterOrHeaderTeoz(FontParam.FOOTER).getHorizontalAlignment(), footer);
	}

	@Override
	public void createOneGraphic(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

}
