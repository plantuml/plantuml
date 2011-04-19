/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6466 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class SequenceDiagramFileMaker implements FileMaker {

	private static final StringBounder dummyStringBounder;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		dummyStringBounder = StringBounderUtils.asStringBounder(imDummy.createGraphics());
	}

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final Dimension2D fullDimension;
	private final List<Page> pages;
	private final FileFormatOption fileFormatOption;
	private final List<BufferedImage> flashcodes;
	
	private int offsetX;
	private int offsetY;



	public SequenceDiagramFileMaker(SequenceDiagram sequenceDiagram, Skin skin, FileFormatOption fileFormatOption,
			List<BufferedImage> flashcodes) {
		HtmlColor.setForceMonochrome(sequenceDiagram.getSkinParam().isMonochrome());
		this.flashcodes = flashcodes;
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox(), sequenceDiagram.getAutonewpage());

		for (Participant p : sequenceDiagram.participants().values()) {
			initializer.addParticipant(p, sequenceDiagram.getEnglober(p));
		}

		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			if (ev instanceof Message) {
				// TODO mieux faire
				final Message m = (Message) ev;
				for (LifeEvent lifeEvent : m.getLiveEvents()) {
					if (lifeEvent.getType() == LifeEventType.DESTROY
					/*
					 * || lifeEvent.getType() == LifeEventType.CREATE
					 */) {
						initializer.addEvent(lifeEvent);
					}
				}
			}
		}
		drawableSet = initializer.createDrawableSet(dummyStringBounder);
		final List<Newpage> newpages = new ArrayList<Newpage>();
		for (Event ev : drawableSet.getAllEvents()) {
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(dummyStringBounder, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox(), sequenceDiagram.getTitle()).getPages();
	}

	public int getNbPages() {
		return pages.size();
	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			List<String> title) {

		final double headerHeight = drawableSet.getHeadHeight(dummyStringBounder);
		final double tailHeight = drawableSet.getTailHeight(dummyStringBounder, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin().createComponent(ComponentType.NEWPAGE,
				drawableSet.getSkinParam(), Arrays.asList("")).getPreferredHeight(dummyStringBounder);

		return new PageSplitter(fullDimension.getHeight(), headerHeight, positions, tailHeight, signatureHeight,
				newpageHeight, title);
	}


	public void createOne(OutputStream os, int index) throws IOException {
		final UGraphic createImage = createImage((int) fullDimension.getWidth(), pages.get(index), index);
		if (createImage instanceof UGraphicG2d) {
			final BufferedImage im = ((UGraphicG2d) createImage).getBufferedImage();
			PngIO.write(im, os, diagram.getMetadata(), diagram.getDpi(fileFormatOption));
		} else if (createImage instanceof UGraphicSvg) {
			final UGraphicSvg svg = (UGraphicSvg) createImage;
			svg.createXml(os);
		} else if (createImage instanceof UGraphicEps) {
			final UGraphicEps eps = (UGraphicEps) createImage;
			os.write(eps.getEPSCode().getBytes());
		}
	}

	private double getImageWidth(SequenceDiagramArea area, boolean rotate, double dpiFactor) {
		final int minsize = diagram.getMinwidth();
		final double w = getImageWidthWithoutMinsize(area, rotate, dpiFactor);
		if (minsize == Integer.MAX_VALUE) {
			return w;
		}
		if (w >= minsize) {
			return w;
		}
		return minsize;
	}

	private double getScale(double width, double height) {
		if (diagram.getScale() == null) {
			return 1;
		}
		return diagram.getScale().getScale(width, height);
	}

	private double getImageWidthWithoutMinsize(SequenceDiagramArea area, boolean rotate, double dpiFactor) {
		final double w;
		if (rotate) {
			w = area.getHeight() * getScale(area.getWidth(), area.getHeight()) * dpiFactor;
		} else {
			w = area.getWidth() * getScale(area.getWidth(), area.getHeight()) * dpiFactor;
		}
		return w;
	}

	private double getImageHeight(SequenceDiagramArea area, final Page page, boolean rotate, double dpiFactor) {
		if (rotate) {
			return area.getWidth() * getScale(area.getWidth(), area.getHeight()) * dpiFactor;
		}
		return area.getHeight() * getScale(area.getWidth(), area.getHeight()) * dpiFactor;
	}
	
	private UGraphic createImage(final int diagramWidth, final Page page, final int indice) {
		double delta = 0;
		if (indice > 0) {
			delta = page.getNewpage1() - page.getHeaderHeight();
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}

		final SequenceDiagramArea area = new SequenceDiagramArea(diagramWidth, page.getHeight());

		Component compTitle = null;

		if (page.getTitle() != null) {
			compTitle = drawableSet.getSkin().createComponent(ComponentType.TITLE, drawableSet.getSkinParam(),
					page.getTitle());
			area.setTitleArea(compTitle.getPreferredWidth(dummyStringBounder), compTitle
					.getPreferredHeight(dummyStringBounder));
		}
		addFooter2(area);
		addHeader2(area);
		
		offsetX = (int) Math.round(area.getSequenceAreaX());
		offsetY = (int) Math.round(area.getSequenceAreaY());

		final Color backColor = diagram.getSkinParam().getBackgroundColor().getColor();
		final UGraphic ug;
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		final double dpiFactor = diagram.getDpiFactor(fileFormatOption);
		final double imageWidth = getImageWidth(area, diagram.isRotation(), dpiFactor);
		if (fileFormat == FileFormat.PNG) {
			double imageHeight = getImageHeight(area, page, diagram.isRotation(), dpiFactor);
			if (imageHeight == 0) {
				imageHeight = 1;
			}
			double flashCodeHeight = 0;
			if (flashcodes != null) {
				flashCodeHeight = flashcodes.get(0).getHeight();
			}
			final EmptyImageBuilder builder = new EmptyImageBuilder(imageWidth, imageHeight + flashCodeHeight,
					backColor);

			final Graphics2D graphics2D = builder.getGraphics2D();
			if (flashcodes != null) {
				graphics2D.drawImage(flashcodes.get(0), null, 0, (int) imageHeight);
			}
			if (diagram.isRotation()) {
				final AffineTransform at = new AffineTransform(0, 1, 1, 0, 0, 0);
				at.concatenate(new AffineTransform(-1, 0, 0, 1, imageHeight, 0));
				at.concatenate(AffineTransform.getTranslateInstance(0.01, 0));
				graphics2D.setTransform(at);
			}
			final AffineTransform scale = graphics2D.getTransform();
			scale.scale(getScale(area.getWidth(), area.getHeight()), getScale(area.getWidth(), area.getHeight()));
			graphics2D.setTransform(scale);
			ug = new UGraphicG2d(graphics2D, builder.getBufferedImage(), dpiFactor);
		} else if (fileFormat == FileFormat.SVG) {
			if (backColor.equals(Color.WHITE)) {
				ug = new UGraphicSvg(false);
			} else {
				ug = new UGraphicSvg(HtmlColor.getAsHtml(backColor), false);
			}
		} else if (fileFormat == FileFormat.EPS) {
			ug = new UGraphicEps(EpsStrategy.getDefault());
		} else {
			throw new UnsupportedOperationException();
		}

		final int diff = (int) Math.round((imageWidth - getImageWidthWithoutMinsize(area, diagram.isRotation(),
				dpiFactor)) / 2);
		if (diagram.isRotation()) {
			ug.translate(0, diff / dpiFactor);
		} else {
			ug.translate(diff / dpiFactor, 0);
		}

		if (compTitle != null) {
			ug.translate(area.getTitleX(), area.getTitleY());
			final StringBounder stringBounder = ug.getStringBounder();
			final double h = compTitle.getPreferredHeight(stringBounder);
			final double w = compTitle.getPreferredWidth(stringBounder);
			compTitle.drawU(ug, new Dimension2DDouble(w, h), new SimpleContext2D(false));
			ug.translate(-area.getTitleX(), -area.getTitleY());
		}

		addHeader3(area, ug);
		addFooter3(area, ug);

		ug.translate(area.getSequenceAreaX(), area.getSequenceAreaY());
		drawableSet.drawU(ug, delta, diagramWidth, page, diagram.isShowFootbox());

		return ug;
	}

	static public File computeFilename(File pngFile, int i, FileFormat fileFormat) {
		if (i == 0) {
			return pngFile;
		}
		final File dir = pngFile.getParentFile();
		String name = pngFile.getName();
		name = name.replaceAll("\\" + fileFormat.getFileSuffix() + "$", "_" + String.format("%03d", i)
				+ fileFormat.getFileSuffix());
		return new File(dir, name);

	}

	private void addFooter2(SequenceDiagramArea area) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.FOOTER, null);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.FOOTER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM);
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setFooterArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addHeader2(SequenceDiagramArea area) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.HEADER, null);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.HEADER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP);
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setHeaderArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addFooter3(SequenceDiagramArea area, UGraphic ug) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.FOOTER, null);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.FOOTER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM);
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug, area.getFooterX(diagram.getFooterAlignement()), area.getFooterY());
	}

	private void addHeader3(SequenceDiagramArea area, UGraphic ug) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.HEADER, null);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.HEADER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP);
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug, area.getHeaderX(diagram.getHeaderAlignement()), area.getHeaderY());
	}

	public static StringBounder getDummystringbounder() {
		return dummyStringBounder;
	}

	public void appendCmap(StringBuilder cmap) {
		drawableSet.appendCmap(cmap, offsetX, offsetY, dummyStringBounder);
	}

}
