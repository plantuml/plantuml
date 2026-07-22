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
package net.sourceforge.plantuml.mcp;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import net.atmp.SvgOption;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.AbstractDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramChromeFactory;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.font.StringBounderFromWidthTable;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.drawing.svg.UGraphicSvg;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.log.Logme;

/**
 * Renders a single PlantUML diagram to a deterministic SVG, in a headless,
 * DOM-free and AWT-free way suitable for the TeaVM/Node build.
 *
 * <p>
 * This is the headless counterpart of
 * {@link net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser}: it follows
 * the same "build the {@link TextBlock}, decorate it with chrome, draw it onto a
 * {@link UGraphic}" pipeline, but writes to the dependency-free
 * {@link UGraphicSvg} backend (a string-producing SVG mini-DOM) instead of the
 * browser's {@code SvgGraphicsTeaVM} (which needs the DOM). It deliberately does
 * <b>not</b> go through {@link net.sourceforge.plantuml.core.TextBlockExporter}:
 * that path compiles a PNG branch ({@code EmptyImageBuilder} ->
 * {@code java.awt.Graphics2D}), which is unavailable under TeaVM.
 *
 * <p>
 * Text dimensions come from {@link StringBounderFromWidthTable}, so the output
 * is deterministic and independent of any installed fonts.
 */
public class DiagramRendererTeaVM {

	private static final StringBounder STRING_BOUNDER = new StringBounderFromWidthTable(FileFormat.SVG_DETERMINISTIC);

	public McpResult render(String source) throws IOException {
		if (source.startsWith("@start") == false)
			return McpResult.noAtStart();

		final SourceStringReader ss = new SourceStringReader(source, UTF_8);
		final List<BlockUml> blocks = ss.getBlocks();
		if (blocks.size() != 1)
			return McpResult.badInput();

		final BlockUml blockUml = blocks.get(0);
		final Diagram diagram = blockUml.getDiagram();

		// On a parse error (or a non-graphical diagram) there is no SVG to
		// produce: report the structured diagnostic only.
		if (diagram instanceof PSystemError || diagram instanceof UgDiagram == false)
			return new McpResult(diagram);

		try {
			final String svg = buildSvg((UgDiagram) diagram);
			return new McpResult(diagram, svg);
		} catch (Throwable e) {
			Logme.error(e);
			return McpResult.renderError(diagram, "Rendering failed: " + e);
		}
	}

	/**
	 * Builds the SVG for a graphical diagram, mirroring
	 * {@code PlantUMLBrowser.buildSvg} but targeting the headless
	 * {@link UGraphicSvg} backend.
	 */
	private static String buildSvg(UgDiagram diagram) throws Exception {
		final ColorMapper colorMapper = ColorMapper.IDENTITY;
		final FileFormatOption fileFormatOption = new FileFormatOption(FileFormat.SVG_DETERMINISTIC);

		TextBlock tb = diagram.getTextBlock(0, fileFormatOption);

		HColor backcolor = tb.getBackcolor();
		if (backcolor == null)
			backcolor = HColors.WHITE.withDark(HColors.BLACK);

		if (diagram instanceof TitledDiagram)
			tb = DiagramChromeFactory.create(tb, (TitledDiagram) diagram, ((TitledDiagram) diagram).getSkinParam(),
					diagram.getWarnings(), ((TitledDiagram) diagram).getTitle());

		final XDimension2D dim = tb.calculateDimension(STRING_BOUNDER);
		final Scale scale = ((AbstractDiagram) diagram).getScale();
		final double scaleFactor = scale == null ? 1.0 : scale.getScale(dim.getWidth(), dim.getHeight());

		SvgOption option = SvgOption.basic();
		option = option.withMinDim(dim);
		option = option.withBackcolor(backcolor);
		option = option.withScale(scaleFactor);
		option = option.withColorMapper(colorMapper);
		option = option.withDecimal(0);

		UGraphic ug = UGraphicSvg.build(option, false, diagram.seed(), STRING_BOUNDER, FileFormat.SVG_DETERMINISTIC);
		if (diagram.isHandwritten())
			ug = new UGraphicHandwritten(ug);

		tb.drawU(ug);
		ug.flushUg();

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ug.writeToStream(baos, null, 96);
		baos.flush();
		return new String(baos.toByteArray(), UTF_8);
	}

}
