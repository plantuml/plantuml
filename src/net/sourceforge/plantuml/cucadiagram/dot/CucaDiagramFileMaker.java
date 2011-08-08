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
 * Revision $Revision: 6939 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.eps.EpsTitler;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.png.PngFlashcoder;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngRotation;
import net.sourceforge.plantuml.png.PngScaler;
import net.sourceforge.plantuml.png.PngSizer;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.svg.SvgData;
import net.sourceforge.plantuml.svg.SvgTitler;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public final class CucaDiagramFileMaker implements ICucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private final List<BufferedImage> flashcodes;
	private final StaticFilesMap staticFilesMap;
	private final Rose rose = new Rose();

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMaker(CucaDiagram diagram, List<BufferedImage> flashcodes) throws IOException {
		// HtmlColor.setForceMonochrome(diagram.getSkinParam().isMonochrome());
		this.diagram = diagram;
		this.flashcodes = flashcodes;
		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS || diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			this.staticFilesMap = new StaticFilesMap(diagram.getSkinParam(), diagram.getDpiFactor(null));
		} else {
			this.staticFilesMap = null;
		}
	}

	static private void traceDotString(String dotString) throws IOException {
		final File f = new File("dottmpfile" + UniqueSequence.getValue() + ".tmp");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(f));
			pw.print(dotString);
			Log.info("Creating file " + f);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}

	public String createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			return createPng(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.SVG) {
			return createSvg(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.EPS_TEXT) {
			return createEps(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.EPS) {
			return createEps(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.DOT) {
			return createDot(os, dotStrings, fileFormatOption);
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private double deltaY;

	private String createSvg(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		final StringBuilder cmap = new StringBuilder();
		try {
			deltaY = 0;
			String svg4 = getSvgData(dotStrings, fileFormatOption, cmap);

			SvgData svgData = SvgData.fromGraphViz(svg4);

			if (svgData != null) {

				final SvgTitler title = getTitleSvgTitler();
				final SvgTitler header = getHeaderSvgTitler();
				final SvgTitler footer = getFooterSvgTitler();

				svgData = title.addTitle(svgData);
				svgData = header.addTitle(svgData);
				svgData = footer.addTitle(svgData);

				// double supH = getTitleSvgHeight(svg);
				// supH += getHeaderSvgHeight(svg);
				// supH += getFooterSvgHeight(svg);
				//
				// svg = removeSvgXmlHeader1(svg);
				//
				// svg = addTitleSvg(svg, dim.getWidth(), dim.getHeight());
				// svg = addHeaderSvg(svg, dim.getWidth(), dim.getHeight());
				// svg = addFooterSvg(svg, dim.getWidth(), dim.getHeight());
				//
				// svg = modifySvgXmlHeader(svg, dim.getWidth(), dim.getHeight()
				// + supH, -50, 0);

				// Image management
				final Pattern pImage = Pattern.compile("(?i)<image\\W[^>]*>");

				svg4 = svgData.getSvg();
				boolean changed;
				do {
					changed = false;
					final Matcher mImage = pImage.matcher(svg4);
					final StringBuffer sb = new StringBuffer();
					while (mImage.find()) {
						final String image = mImage.group(0);
						final String href = getValue(image, "href");
						final double widthSvg = Double.parseDouble(getValuePx(image, "width"));
						final double heightSvg = Double.parseDouble(getValuePx(image, "height"));
						final double x = Double.parseDouble(getValue(image, "x"));
						final double y = Double.parseDouble(getValue(image, "y"));
						final DrawFile drawFile = getDrawFileFromHref(href);
						if (drawFile == null) {
							mImage.appendReplacement(sb, image);
						} else {
							final int widthPng = drawFile.getWidthPng();
							final int heightPng = drawFile.getHeightPng();
							String svg2 = drawFile.getSvg();
							final String scale = getScale(widthSvg, heightSvg, widthPng, heightPng);
							svg2 = svg2.replaceFirst("<[gG]>", "<g transform=\"translate(" + 0 + " " + 0 + ") " + scale
									+ "\">");
							svg2 = "<svg x=\"" + x + "\" y=\"" + y + "\">" + svg2 + "</svg>";
							mImage.appendReplacement(sb, svg2);
							changed = true;
						}
					}
					mImage.appendTail(sb);
					svg4 = sb.toString();
				} while (changed);
			}

			os.write(svg4.getBytes("UTF-8"));

			// final ByteArrayInputStream bais = new
			// ByteArrayInputStream(baos.toByteArray());
			// BufferedImage im = ImageIO.read(bais);
			// bais.close();
			// if (isUnderline) {
			// new UnderlineTrick(im, new Color(Integer.parseInt("FEFECF", 16)),
			// Color.BLACK).process();
			// }
			//
			// final Color background =
			// diagram.getSkinParam().getBackgroundColor().getColor();
			// im = addTitle(im, background);
			// im = addFooter(im, background);
			// im = addHeader(im, background);
			//
			// if (diagram.isRotation()) {
			// im = PngRotation.process(im);
			// }
			// im = PngSizer.process(im, diagram.getMinwidth());
			//
			// PngIO.write(im, os, diagram.getMetadata());
		} finally {
			clean();
		}
		if (cmap.length() > 0) {
			return translateXY(cmap.toString(), 0, (int) Math.round(deltaY));
		}
		return null;
	}

	static String translateXY(String cmap, int deltaX, int deltaY) {
		if (deltaY == 0) {
			return cmap;
		}
		final Pattern p = Pattern.compile("coords=\"(\\d+),(\\d+),(\\d+),(\\d+)\"");
		final Matcher m = p.matcher(cmap);

		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			final int x1 = Integer.parseInt(m.group(1)) + deltaX;
			final int y1 = Integer.parseInt(m.group(2)) + deltaY;
			final int x2 = Integer.parseInt(m.group(3)) + deltaX;
			final int y2 = Integer.parseInt(m.group(4)) + deltaY;
			m.appendReplacement(sb, "coords=\"" + x1 + "," + y1 + "," + x2 + "," + y2 + "\"");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String getSvgData(List<String> dotStrings, FileFormatOption fileFormatOption, StringBuilder cmap)
			throws IOException, InterruptedException, UnsupportedEncodingException {
		final GraphvizMaker dotMaker = populateImagesAndCreateGraphvizMaker(dotStrings, fileFormatOption);
		final String dotString = dotMaker.createDotString();

		if (OptionFlags.getInstance().isKeepTmpFiles()) {
			traceDotString(dotString);
		}
		// if (diagram.hasUrl()) {
		// final Graphviz graphviz = GraphvizUtils.create(dotString, "cmapx",
		// "svg");
		//
		// final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// graphviz.createPng(baos);
		// baos.close();
		// dotMaker.clean();
		//
		// final String cmapAndSvg = new String(baos.toByteArray(),
		// "UTF-8").replace('\\', '/');
		// final int x = cmapAndSvg.indexOf("<?xml ");
		// if (x == -1) {
		// throw new IllegalStateException();
		// }
		// cmap.append(cmapAndSvg.substring(0, x));
		// return cmapAndSvg.substring(x);
		//
		// }

		final Graphviz graphviz = GraphvizUtils.create(dotString, "svg");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graphviz.createFile(baos);
		baos.close();
		dotMaker.clean();

		return new String(baos.toByteArray(), "UTF-8").replace('\\', '/');
	}

	private DrawFile searchImageFile(File searched, Collection<? extends IEntity> entities) throws IOException {
		for (IEntity ent : entities) {
			final DrawFile df = ent.getImageFile(searched);
			if (df != null) {
				assert df.getPng().getCanonicalFile().equals(searched.getCanonicalFile());
				return df;
			}
		}
		return null;

	}

	private DrawFile getDrawFileFromHref(final String href) throws IOException {
		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS || diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			final DrawFile drawFile = staticFilesMap.getDrawFile(href);
			if (drawFile != null) {
				return drawFile;
			}
		}
		final File searched = new File(href).getCanonicalFile();

		final DrawFile result = searchImageFile(searched, diagram.entities().values());
		if (result != null) {
			return result;
		}

		for (Link ent : diagram.getLinks()) {
			final DrawFile df = ent.getImageFile();
			if (df == null) {
				continue;
			}
			if (df.getPng().getCanonicalFile().equals(searched)) {
				return df;
			}
		}
		Log.error("Cannot find " + href);
		return null;
	}

	private static String getScale(double widthSvg, double heightSvg, double widthPng, double heightPng) {
		final double v1 = heightSvg / heightPng;
		final double v2 = widthSvg / widthPng;
		final double min = Math.min(v1, v2);
		return "scale(" + format(min) + " " + format(min) + ")";
	}

	private static String format(double x) {
		return EpsGraphics.format(x);
	}

	private static String getValue(String s, String param) {
		final Pattern p = Pattern.compile("(?i)" + param + "=\"([^\"]+)\"");
		final Matcher m = p.matcher(s);
		m.find();
		return m.group(1);
	}

	private static String getValuePx(String s, String param) {
		final Pattern p = Pattern.compile("(?i)" + param + "=\"([^\"]+?)(?:px)?\"");
		final Matcher m = p.matcher(s);
		m.find();
		return m.group(1);
	}

	private String createPng(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		final StringBuilder cmap = new StringBuilder();
		double supX = 0;
		double supY = 0;
		try {
			final GraphvizMaker dotMaker = populateImagesAndCreateGraphvizMaker(dotStrings, fileFormatOption);
			final String dotString = dotMaker.createDotString();

			if (OptionFlags.getInstance().isKeepTmpFiles()) {
				traceDotString(dotString);
			}

			final byte[] imageData = getImageData(dotString, cmap);

			if (imageData.length == 0) {
				createError(os, imageData.length, new FileFormatOption(FileFormat.PNG), "imageData.length == 0");
				return null;
			}

			if (isPngHeader(imageData, 0) == false) {
				createError(os, imageData.length, new FileFormatOption(FileFormat.PNG), "No PNG header found",
						"Try -forcegd or -forcecairo flag");
				return null;
			}

			final ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
			BufferedImage im = ImageIO.read(bais);
			if (im == null) {
				createError(os, imageData.length, new FileFormatOption(FileFormat.PNG), "im == null");
				return null;
			}
			bais.close();
			dotMaker.clean();

			final boolean isUnderline = dotMaker.isUnderline();
			if (isUnderline) {
				new UnderlineTrick(im, new Color(Integer.parseInt("FEFECF", 16)), Color.BLACK).process();
			}

			final HtmlColor background = diagram.getSkinParam().getBackgroundColor();

			supY = getTitlePngHeight(stringBounder);
			supY += getHeaderPngHeight(stringBounder);

			supX = getOffsetX(stringBounder, im.getWidth());

			im = addTitle(im, background);
			im = addFooter(im, background);
			im = addHeader(im, background);
			im = scaleImage(im, diagram.getScale());

			if (diagram.isRotation()) {
				im = PngRotation.process(im);
			}
			im = PngSizer.process(im, diagram.getMinwidth());

			im = addFlashcode(im, diagram.getColorMapper().getMappedColor(background));

			PngIO.write(im, os, diagram.getMetadata(), diagram.getDpi(fileFormatOption));
		} finally {
			clean();
		}

		if (cmap.length() > 0) {
			return translateXY(cmap.toString(), (int) Math.round(supX), (int) Math.round(supY));
		}
		return null;
	}

	private BufferedImage addFlashcode(BufferedImage im, Color background) {
		if (flashcodes == null) {
			return im;
		}
		return new PngFlashcoder(flashcodes).processImage(im, background);
	}

	private String createDot(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		final GraphvizMaker dotMaker = populateImagesAndCreateGraphvizMaker(dotStrings, fileFormatOption);
		final String dotString = dotMaker.createDotString();
		os.write(dotString.getBytes());
		return null;
	}

	private BufferedImage scaleImage(BufferedImage im, Scale scale) {
		if (scale == null) {
			return im;
		}
		return PngScaler.scale(im, scale.getScale(im.getWidth(), im.getHeight()));
	}

	private byte[] getImageDataCmap(final String dotString, StringBuilder cmap2) throws IOException,
			InterruptedException {
		final Graphviz graphviz = GraphvizUtils.create(dotString, "cmapx", getPngType());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graphviz.createFile(baos);
		baos.close();

		final byte[] allData = baos.toByteArray();
		Log.info("Reading " + allData.length + " bytes from dot");

		final int pngStart = getPngStart(allData);

		final String cmap = new String(allData, 0, pngStart, "UTF8");
		Log.info("CMAP is " + cmap.length() + " long");
		cmap2.append(cmap);

		final byte[] imageData = new byte[allData.length - pngStart];
		System.arraycopy(allData, pngStart, imageData, 0, imageData.length);

		Log.info("PNG is " + imageData.length + " bytes from dot");
		return imageData;
	}

	private int getPngStart(byte[] allData) throws IOException {
		for (int i = 0; i < allData.length - 8; i++) {
			if (isPngHeader(allData, i)) {
				return i;
			}
		}
		throw new IOException("Cannot find PNG header");
	}

	private boolean isPngHeader(byte[] allData, int i) {
		if (i + 7 >= allData.length) {
			return false;
		}
		return allData[i] == (byte) 0x89 && allData[i + 1] == (byte) 0x50 && allData[i + 2] == (byte) 0x4E
				&& allData[i + 3] == (byte) 0x47 && allData[i + 4] == (byte) 0x0D && allData[i + 5] == (byte) 0x0A
				&& allData[i + 6] == (byte) 0x1A && allData[i + 7] == (byte) 0x0A;
	}

	private byte[] getImageData(final String dotString, StringBuilder cmap) throws IOException, InterruptedException {
		if (diagram.hasUrl()) {
			return getImageDataCmap(dotString, cmap);
		}
		final Graphviz graphviz = GraphvizUtils.create(dotString, getPngType());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graphviz.createFile(baos);
		baos.close();

		final byte[] imageData = baos.toByteArray();
		// final byte[] imageData = new byte[0];
		Log.info("Reading " + imageData.length + " bytes from dot");
		return imageData;
	}

	private String getPngType() {
		if (OptionFlags.getInstance().isForceCairo()) {
			return "png:cairo";
		}
		if (OptionFlags.getInstance().isForceGd()) {
			return "png:gd";
		}
		return "png";
	}

	void createError(OutputStream os, int length, FileFormatOption fileFormat, String... supp) throws IOException {
		final List<String> msg = new ArrayList<String>();
		msg.add("Error: Reading " + length + " byte(s) from dot");
		msg.add("Error reading the generated image");
		for (String s : supp) {
			msg.add(s);
		}
		final GraphicStrings errorResult = new GraphicStrings(msg);
		errorResult.writeImage(os, fileFormat);
	}

	private BufferedImage addTitle(BufferedImage im, final HtmlColor background) {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null);
		final UFont font = getSkinParam().getFont(FontParam.TITLE, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getTitle(), fontSize,
				fontFamily, HorizontalAlignement.CENTER, VerticalPosition.TOP);
		return pngTitler.processImage(im, background, 3);
	}

	private double getTitlePngHeight(StringBounder stringBounder) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null);
		final UFont font = getSkinParam().getFont(FontParam.TITLE, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getTitle(), fontSize,
				fontFamily, HorizontalAlignement.CENTER, VerticalPosition.TOP);
		return pngTitler.getOffsetY(stringBounder);
	}

	private double getOffsetX(StringBounder stringBounder, double imWidth) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null);
		final UFont font = getSkinParam().getFont(FontParam.TITLE, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getTitle(), fontSize,
				fontFamily, HorizontalAlignement.CENTER, VerticalPosition.TOP);
		return pngTitler.getOffsetX(imWidth, stringBounder);
	}

	private SvgTitler getTitleSvgTitler() throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null);
		final UFont font = getSkinParam().getFont(FontParam.TITLE, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();

		return new SvgTitler(diagram.getColorMapper(), titleColor, diagram.getTitle(), fontSize, fontFamily,
				HorizontalAlignement.CENTER, VerticalPosition.TOP, 3);
	}

	private SvgTitler getHeaderSvgTitler() throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final UFont font = getSkinParam().getFont(FontParam.HEADER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		return new SvgTitler(diagram.getColorMapper(), titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP, 3);
	}

	private SvgTitler getFooterSvgTitler() throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final UFont font = getSkinParam().getFont(FontParam.FOOTER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		return new SvgTitler(diagram.getColorMapper(), titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM, 3);
	}

	private String addTitleEps(EpsStrategy epsStrategy, String eps) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null);
		final UFont font = getSkinParam().getFont(FontParam.TITLE, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();

		final EpsTitler epsTitler = new EpsTitler(diagram.getColorMapper(), epsStrategy, titleColor,
				diagram.getTitle(), fontSize, fontFamily, HorizontalAlignement.CENTER, VerticalPosition.TOP, 3);
		this.deltaY += epsTitler.getHeight();
		return epsTitler.addTitleEps(eps);
	}

	private String addFooterEps(EpsStrategy epsStrategy, String eps) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final UFont font = getSkinParam().getFont(FontParam.FOOTER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final EpsTitler epsTitler = new EpsTitler(diagram.getColorMapper(), epsStrategy, titleColor, diagram
				.getFooter(), fontSize, fontFamily, diagram.getFooterAlignement(), VerticalPosition.BOTTOM, 3);
		return epsTitler.addTitleEps(eps);
	}

	private String addHeaderEps(EpsStrategy epsStrategy, String eps) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final UFont font = getSkinParam().getFont(FontParam.HEADER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final EpsTitler epsTitler = new EpsTitler(diagram.getColorMapper(), epsStrategy, titleColor, diagram
				.getHeader(), fontSize, fontFamily, diagram.getHeaderAlignement(), VerticalPosition.TOP, 3);
		this.deltaY += epsTitler.getHeight();
		return epsTitler.addTitleEps(eps);
	}

	private BufferedImage addFooter(BufferedImage im, final HtmlColor background) {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final UFont font = getSkinParam().getFont(FontParam.FOOTER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getFooter(), fontSize,
				fontFamily, diagram.getFooterAlignement(), VerticalPosition.BOTTOM);
		return pngTitler.processImage(im, background, 3);
	}

	private BufferedImage addHeader(BufferedImage im, final HtmlColor background) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final UFont font = getSkinParam().getFont(FontParam.HEADER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getHeader(), fontSize,
				fontFamily, diagram.getHeaderAlignement(), VerticalPosition.TOP);
		return pngTitler.processImage(im, background, 3);
	}

	private double getHeaderPngHeight(StringBounder stringBounder) throws IOException {
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final UFont font = getSkinParam().getFont(FontParam.HEADER, null);
		final String fontFamily = font.getFamily(null);
		final int fontSize = font.getSize();
		final PngTitler pngTitler = new PngTitler(diagram.getColorMapper(), titleColor, diagram.getHeader(), fontSize,
				fontFamily, diagram.getHeaderAlignement(), VerticalPosition.TOP);
		return pngTitler.getOffsetY(stringBounder);
	}

	DrawFile createImage(Entity entity, FileFormatOption option) throws IOException {
		final double dpiFactor = diagram.getDpiFactor(option);
		if (entity.getType() == EntityType.NOTE) {
			return createImageForNote(entity.getDisplay2(), entity.getSpecificBackColor(), option, entity.getParent());
		}
		if (entity.getType() == EntityType.ACTOR) {
			return createImageForActor(entity, dpiFactor);
		}
		if (entity.getType() == EntityType.CIRCLE_INTERFACE) {
			return createImageForCircleInterface(entity, dpiFactor);
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.ENUM || entity.getType() == EntityType.INTERFACE) {
			return createImageForCircleCharacter(entity, dpiFactor);
		}
		return null;
	}

	private DrawFile createImageForNote(List<? extends CharSequence> display2, HtmlColor noteBackColor, final FileFormatOption option,
			Group parent) throws IOException {

		final Rose skin = new Rose();

		final ISkinParam skinParam = new SkinParamBackcolored(getSkinParam(), noteBackColor);
		final Component comp = skin
				.createComponent(ComponentType.NOTE, skinParam, display2);

		final double dpiFactor = diagram.getDpiFactor(option);
		final int width = (int) (comp.getPreferredWidth(stringBounder) * dpiFactor);
		final int height = (int) (comp.getPreferredHeight(stringBounder) * dpiFactor);
		final int dpi = diagram.getDpi(option);
		HtmlColor backgroundColor = diagram.getSkinParam().getBackgroundColor();
		if (parent != null && parent.getBackColor() != null) {
			backgroundColor = parent.getBackColor();
		}
		final Color background = diagram.getColorMapper().getMappedColor(backgroundColor);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final File fPng = FileUtils.createTempFile("plantumlB", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, background);
				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				comp.drawU(new UGraphicG2d(diagram.getColorMapper(), g2d, null, dpiFactor),
						new Dimension(width, height), new SimpleContext2D(false));
				PngIO.write(im, fPng, dpi);
				g2d.dispose();
				return fPng;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				final UGraphicSvg ug = new UGraphicSvg(getSkinParam().getColorMapper(), true);
				comp.drawU(ug, new Dimension(width, height), new SimpleContext2D(false));
				return getSvg(ug);
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File fEps = FileUtils.createTempFile("plantumlB", ".eps");
				final PrintWriter pw = new PrintWriter(fEps);
				final UGraphicEps uEps = new UGraphicEps(getSkinParam().getColorMapper(), getEpsStrategy(option
						.getFileFormat()));
				comp.drawU(uEps, new Dimension(width, height), new SimpleContext2D(false));
				pw.print(uEps.getEPSCode());
				pw.close();
				return fEps;
			}
		};
		return DrawFile.create(lpng, lsvg, leps, null);
	}

	static public String getSvg(UGraphicSvg ug) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ug.createXml(baos);
		baos.close();
		final String s = new String(baos.toByteArray(), "UTF-8");
		final Pattern p = Pattern.compile("(?i)<g\\W.*</g>");
		final Matcher m = p.matcher(s);
		m.find();
		return m.group(0);
	}

	private DrawFile createImageForActivity(Entity entity) throws IOException {
		return null;
	}

	private DrawFile createImageForCircleInterface(Entity entity, final double dpiFactor) throws IOException {
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		final HtmlColor interfaceBackground = rose.getHtmlColor(getSkinParam(),
				ColorParam.componentInterfaceBackground, stereo);
		final HtmlColor interfaceBorder = rose
				.getHtmlColor(getSkinParam(), ColorParam.componentInterfaceBorder, stereo);
		final HtmlColor background = rose.getHtmlColor(getSkinParam(), ColorParam.background, stereo);
		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(circleInterface.getPreferredWidth(null)
						* dpiFactor, circleInterface.getPreferredHeight(null) * dpiFactor, diagram.getColorMapper()
						.getMappedColor(background));

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(diagram.getColorMapper(), g2d, null, 1.0));

				final File png = FileUtils.createTempFile("circleinterface", ".png");
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("circleinterface", ".eps");
				UGraphicEps.copyEpsToFile(getSkinParam().getColorMapper(), circleInterface, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(getSkinParam().getColorMapper(), circleInterface);
			}

		};

		final Object signature = Arrays.asList("circleinterface", interfaceBackground, interfaceBorder, background,
				dpiFactor);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

	private DrawFile createImageForActor(Entity entity, final double dpiFactor) throws IOException {
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		final HtmlColor actorBackground = rose.getHtmlColor(getSkinParam(), ColorParam.usecaseActorBackground, stereo);
		final HtmlColor actorBorder = rose.getHtmlColor(getSkinParam(), ColorParam.usecaseActorBorder, stereo);
		final HtmlColor background = rose.getHtmlColor(getSkinParam(), ColorParam.background, stereo);
		final StickMan stickMan = new StickMan(actorBackground, actorBorder);

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(stickMan.getPreferredWidth(null) * dpiFactor,
						stickMan.getPreferredHeight(null) * dpiFactor, diagram.getColorMapper().getMappedColor(
								background));

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				stickMan.drawU(new UGraphicG2d(diagram.getColorMapper(), g2d, null, dpiFactor));

				final File png = FileUtils.createTempFile("actor", ".png");
				ImageIO.write(im, "png", png);
				return png;
			}
		};
		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("actor", ".eps");
				UGraphicEps.copyEpsToFile(getSkinParam().getColorMapper(), stickMan, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(getSkinParam().getColorMapper(), stickMan);
			}

		};

		final Object signature = Arrays.asList("actor", actorBackground, actorBorder, background, dpiFactor);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

	private DrawFile createImageForCircleCharacter(Entity entity, double dpiFactor) throws IOException {
		final Stereotype stereotype = entity.getStereotype();

		if (stereotype == null || stereotype.getHtmlColor() == null) {
			return null;
		}

		final String stereo = stereotype.getLabel();

		final HtmlColor classBorder = rose.getHtmlColor(getSkinParam(), ColorParam.classBorder, stereo);
		final HtmlColor classBackground = rose.getHtmlColor(getSkinParam(), ColorParam.classBackground, stereo);
		final UFont font = diagram.getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, stereo);
		final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(), getSkinParam()
				.getCircledCharacterRadius(), font, stereotype.getHtmlColor(), classBorder, HtmlColor.BLACK);
		return circledCharacter.generateCircleCharacter(diagram.getColorMapper(), classBackground, dpiFactor);
	}

	private ISkinParam getSkinParam() {
		return diagram.getSkinParam();
	}

	private EpsStrategy getEpsStrategy(FileFormat format) {
		if (format == FileFormat.EPS_TEXT) {
			return EpsStrategy.WITH_MACRO_AND_TEXT;
		}
		return EpsStrategy.getDefault2();
	}

	private String createEps(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		try {
			deltaY = 0;
			final GraphvizMaker dotMaker = populateImagesAndCreateGraphvizMaker(dotStrings, fileFormatOption);
			final String dotString = dotMaker.createDotString();

			if (OptionFlags.getInstance().isKeepTmpFiles()) {
				traceDotString(dotString);
			}

			// final boolean isUnderline = dotMaker.isUnderline();
			final Graphviz graphviz = GraphvizUtils.create(dotString, "eps");

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			graphviz.createFile(baos);
			baos.close();

			dotMaker.clean();

			final boolean isUnderline = dotMaker.isUnderline();
			String eps = new String(baos.toByteArray(), "UTF-8");
			eps = cleanStrangeCharacter(eps);
			if (isUnderline) {
				eps = new UnderlineTrickEps(eps).getString();
			}

			if (diagram.getTitle() != null) {
				eps = addTitleEps(getEpsStrategy(fileFormatOption.getFileFormat()), eps);
			}
			if (diagram.getFooter() != null) {
				eps = addFooterEps(getEpsStrategy(fileFormatOption.getFileFormat()), eps);
			}
			if (diagram.getHeader() != null) {
				eps = addHeaderEps(getEpsStrategy(fileFormatOption.getFileFormat()), eps);
			}

			os.write(eps.getBytes("UTF-8"));

			// final Dimension2D dim = getDimensionSvg(svg);
			//
			// svg = svg
			// .replaceFirst(
			// "(?i)<svg[^>]*>",
			// "<svg xmlns=\"http://www.w3.org/2000/svg\"
			// xmlns:xlink=\"http://www.w3.org/1999/xlink\"
			// style=\"width:100%;height:100%;position:absolute;top:0;left:0;\">");
			//
			// svg = addTitleSvg(svg, dim.getWidth(), dim.getHeight());
			// svg = addHeaderSvg(svg, dim.getWidth(), dim.getHeight());
			// svg = addFooterSvg(svg, dim.getWidth(), dim.getHeight());
			//
			// // Image management
			// final Pattern pImage = Pattern.compile("(?i)<image\\W[^>]*>");
			// final Matcher mImage = pImage.matcher(svg);
			// final StringBuffer sb = new StringBuffer();
			// while (mImage.find()) {
			// final String image = mImage.group(0);
			// final String href = getValue(image, "href");
			// final double widthSvg = Double.parseDouble(getValuePx(image,
			// "width"));
			// final double heightSvg = Double.parseDouble(getValuePx(image,
			// "height"));
			// final double x = Double.parseDouble(getValue(image, "x"));
			// final double y = Double.parseDouble(getValue(image, "y"));
			// final DrawFile drawFile = getDrawFileFromHref(href);
			// final int widthPng = drawFile.getWidthPng();
			// final int heightPng = drawFile.getHeightPng();
			// String svg2 = drawFile.getSvg();
			// final String scale = getScale(widthSvg, heightSvg, widthPng,
			// heightPng);
			// svg2 = svg2.replaceFirst("<[gG]>", "<g transform=\"translate(" +
			// x + " " + y + ") " + scale + "\">");
			// mImage.appendReplacement(sb, svg2);
			// }
			// mImage.appendTail(sb);
			// svg = sb.toString();

		} finally {
			clean();
		}
		return null;
	}

	private void clean() throws IOException {
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			diagram.clean();
			if (staticFilesMap != null) {
				staticFilesMap.clean();
			}
		}
	}

	private GraphvizMaker populateImagesAndCreateGraphvizMaker(List<String> dotStrings,
			FileFormatOption fileFormatOption) throws IOException, InterruptedException {
		populateImages(fileFormatOption);
		populateImagesLink(fileFormatOption);
		final GraphvizMaker dotMaker = createDotMaker(dotStrings, fileFormatOption);
		return dotMaker;
	}

	private GraphvizMaker createDotMaker(List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (diagram.getUmlDiagramType() == UmlDiagramType.STATE
				|| diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			new CucaDiagramSimplifier(diagram, dotStrings, fileFormat);
		}
		final DotData dotData = new DotData(null, diagram.getLinks(), diagram.entities(), diagram.getUmlDiagramType(),
				diagram.getSkinParam(), diagram.getRankdir(), diagram, diagram, diagram.getColorMapper());

		dotData.setDpi(diagram.getDpi(fileFormatOption));

		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS || diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			dotData.setStaticImagesMap(staticFilesMap);
			if (diagram.isVisibilityModifierPresent()) {
				dotData.setVisibilityModifierPresent(true);
			}
		}

		if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			dotData.setHideEmptyDescription(((StateDiagram) diagram).isHideEmptyDescription());
		}

		return diagram.getSkinParam().getStrategy().getGraphvizMaker(dotData, dotStrings, fileFormat);
		// return new DotMaker(dotData, dotStrings, fileFormat);
	}

	private void populateImages(FileFormatOption option) throws IOException {
		for (Entity entity : diagram.entities().values()) {
			final DrawFile f = createImage(entity, option);
			if (f != null) {
				entity.setImageFile(f);
			}
		}
	}

	private void populateImagesLink(FileFormatOption option) throws IOException {
		for (Link link : diagram.getLinks()) {
			final List<? extends CharSequence> note = link.getNote();
			if (note == null) {
				continue;
			}
			final DrawFile f = createImageForNote(note, null, option, null);
			if (f != null) {
				link.setImageFile(f);
			}
		}
	}

	static String cleanStrangeCharacter(String eps) {
		final StringBuilder sb = new StringBuilder();
		final StringTokenizer st = new StringTokenizer(eps, "\r\n");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.equals(EpsGraphics.END_OF_FILE)) {
				sb.append(s);
				sb.append("\n");
				s = st.nextToken();
				if (s.equalsIgnoreCase("grestore") == false && st.hasMoreTokens()) {
					s = st.nextToken();
					if (s.equalsIgnoreCase("grestore") == false) {
						throw new IllegalStateException();
					}
				}
			}
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

}
