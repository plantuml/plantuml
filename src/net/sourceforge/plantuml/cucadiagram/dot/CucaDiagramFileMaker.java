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
 * Revision $Revision: 6197 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
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
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Imaged;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.eps.SvgToEpsConverter;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngRotation;
import net.sourceforge.plantuml.png.PngScaler;
import net.sourceforge.plantuml.png.PngSizer;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svg.SvgTitler;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public final class CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private final StaticFilesMap staticFilesMap;
	private final Rose rose = new Rose();

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMaker(CucaDiagram diagram) throws IOException {
		HtmlColor.setForceMonochrome(diagram.getSkinParam().isMonochrome());
		this.diagram = diagram;
		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS || diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			this.staticFilesMap = new StaticFilesMap(diagram.getSkinParam(), diagram.getDpiFactor(null));
		} else {
			this.staticFilesMap = null;
		}
	}

	static String changeName(String name) {
		return name.replaceAll("(?i)\\.png$", ".cmapx");
	}

	public List<File> createFile(File suggested, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggested);
			final String cmap = createFile(os, dotStrings, fileFormatOption);
			if (diagram.hasUrl() && fileFormat == FileFormat.PNG) {
				final File cmapFile = new File(changeName(suggested.getAbsolutePath()));
				final PrintWriter pw = new PrintWriter(cmapFile);
				pw.print(cmap);
				pw.close();
			}
		} finally {
			if (os != null) {
				os.close();
			}
		}

		if (fileFormat == FileFormat.PNG) {
			final List<File> result = new PngSplitter(suggested, diagram.getHorizontalPages(), diagram
					.getVerticalPages(), diagram.getMetadata(), diagram.getDpi(fileFormatOption)).getFiles();
			for (File f : result) {
				Log.info("Creating file: " + f);
			}
			return result;
		}
		Log.info("Creating file: " + suggested);
		return Arrays.asList(suggested);
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
		} else if (fileFormat == FileFormat.EPS_VIA_SVG) {
			return createEpsViaSvg(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.EPS) {
			return createEps(os, dotStrings, fileFormatOption);
		} else if (fileFormat == FileFormat.DOT) {
			return createDot(os, dotStrings, fileFormatOption);
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private String createEpsViaSvg(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {
		final File svgTmp = FileUtils.createTempFile("svgtmp", ".svg");
		final FileOutputStream svgOs = new FileOutputStream(svgTmp);
		final String status = createSvg(svgOs, dotStrings, fileFormatOption);
		svgOs.close();
		final SvgToEpsConverter converter = new SvgToEpsConverter(svgTmp);
		converter.createEps(os);
		return status;
	}

	private double deltaY;

	private String createSvg(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException, InterruptedException {

		try {
			deltaY = 0;
			final GraphvizMaker dotMaker = populateImagesAndCreateGraphvizMaker(dotStrings, fileFormatOption);
			final String dotString = dotMaker.createDotString();

			if (OptionFlags.getInstance().isKeepTmpFiles()) {
				traceDotString(dotString);
			}

			// final boolean isUnderline = dotMaker.isUnderline();
			final Graphviz graphviz = GraphvizUtils.create(dotString, "svg");

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			graphviz.createPng(baos);
			baos.close();
			dotMaker.clean();

			String svg = new String(baos.toByteArray(), "UTF-8").replace('\\', '/');

			final Dimension2D dim = getDimensionSvg(svg);
			if (dim != null) {
				svg = removeSvgXmlHeader(svg);

				svg = addTitleSvg(svg, dim.getWidth(), dim.getHeight());
				svg = addHeaderSvg(svg, dim.getWidth(), dim.getHeight());
				svg = addFooterSvg(svg, dim.getWidth(), dim.getHeight());

				// Image management
				final Pattern pImage = Pattern.compile("(?i)<image\\W[^>]*>");
				boolean changed;
				do {
					changed = false;
					final Matcher mImage = pImage.matcher(svg);
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
					svg = sb.toString();
				} while (changed);
			}

			os.write(svg.getBytes("UTF-8"));

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
			// cleanTemporaryFiles(diagram.entities().values());
			// cleanTemporaryFiles(diagram.getLinks());
		}
		return null;
	}

	private static String removeSvgXmlHeader(String svg) {
		svg = svg
				.replaceFirst(
						"(?i)<svg[^>]*>",
						"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" style=\"width:100%;height:100%;position:absolute;top:0;left:0;\">");
		return svg;
	}

	private Dimension2D getDimensionSvg(String svg) {
		final Pattern p = Pattern.compile("(?i)<polygon\\s+[^>]*points=\"([^\"]+)\"");
		final Matcher m = p.matcher(svg);
		if (m.find() == false) {
			return null;
		}
		final String points = m.group(1);
		final StringTokenizer st = new StringTokenizer(points, " ");
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			final StringTokenizer st2 = new StringTokenizer(token, ",");
			final double x = Double.parseDouble(st2.nextToken().trim());
			final double y = Double.parseDouble(st2.nextToken().trim());
			if (x < minX) {
				minX = x;
			}
			if (y < minY) {
				minY = y;
			}
			if (x > maxX) {
				maxX = x;
			}
			if (y > maxY) {
				maxY = y;
			}
		}
		return new Dimension2DDouble(maxX - minX, maxY - minY);
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
		return "scale(" + min + " " + min + ")";
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

			final Color background = diagram.getSkinParam().getBackgroundColor().getColor();
			im = addTitle(im, background);
			im = addFooter(im, background);
			im = addHeader(im, background);
			im = scaleImage(im, diagram.getScale());

			if (diagram.isRotation()) {
				im = PngRotation.process(im);
			}
			im = PngSizer.process(im, diagram.getMinwidth());
			PngIO.write(im, os, diagram.getMetadata(), diagram.getDpi(fileFormatOption));
		} finally {
			cleanTemporaryFiles(diagram.entities().values());
			cleanTemporaryFiles(diagram.getLinks());
		}

		if (cmap.length() > 0) {
			return cmap.toString();
		}
		return null;
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
		graphviz.createPng(baos);
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
		graphviz.createPng(baos);
		baos.close();

		final byte[] imageData = baos.toByteArray();
		Log.info("Reading " + imageData.length + " bytes from dot");
		// File f = new File("dummy.png");
		// FileOutputStream fos = new FileOutputStream(f);
		// fos.write(imageData);
		// fos.close();
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

	private BufferedImage addTitle(BufferedImage im, final Color background) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.TITLE, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.TITLE, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getTitle(), fontSize, fontFamily,
				HorizontalAlignement.CENTER, VerticalPosition.TOP);
		return pngTitler.processImage(im, background, 3);
	}

	private String addTitleSvg(String svg, double width, double height) throws IOException {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.TITLE, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.TITLE, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.TITLE, null);

		final SvgTitler svgTitler = new SvgTitler(titleColor, diagram.getTitle(), fontSize, fontFamily,
				HorizontalAlignement.CENTER, VerticalPosition.TOP, 3);
		this.deltaY += svgTitler.getHeight();
		return svgTitler.addTitleSvg(svg, width, height);
	}

	private String addHeaderSvg(String svg, double width, double height) throws IOException {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.HEADER, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.HEADER, null);
		final SvgTitler svgTitler = new SvgTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP, 3);
		this.deltaY += svgTitler.getHeight();
		return svgTitler.addTitleSvg(svg, width, height);
	}

	private String addFooterSvg(String svg, double width, double height) throws IOException {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.FOOTER, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.FOOTER, null);
		final SvgTitler svgTitler = new SvgTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM, 3);
		return svgTitler.addTitleSvg(svg, width, height + deltaY);
	}

	private BufferedImage addFooter(BufferedImage im, final Color background) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.FOOTER, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.FOOTER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM);
		return pngTitler.processImage(im, background, 3);
	}

	private BufferedImage addHeader(BufferedImage im, final Color background) throws IOException {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null).getColor();
		final String fontFamily = getSkinParam().getFontFamily(FontParam.HEADER, null);
		final int fontSize = getSkinParam().getFontSize(FontParam.HEADER, null);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP);
		return pngTitler.processImage(im, background, 3);
	}

	private void cleanTemporaryFiles(final Collection<? extends Imaged> imageFiles) throws IOException {
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			for (Imaged entity : imageFiles) {
				if (entity.getImageFile() != null) {
					entity.getImageFile().delete();
				}
			}
		}
	}

	DrawFile createImage(Entity entity, double dpiFactor, int dpi) throws IOException {
		if (entity.getType() == EntityType.NOTE) {
			return createImageForNote(entity.getDisplay(), entity.getSpecificBackColor(), dpiFactor, dpi);
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

	private DrawFile createImageForNoteOld2(String display, HtmlColor backColor, double dpiFactor, int dpi)
			throws IOException {
		final File fPng = FileUtils.createTempFile("plantumlB", ".png");

		final Rose skin = new Rose();

		final ISkinParam skinParam = new SkinParamBackcolored(getSkinParam(), backColor);
		final Component comp = skin
				.createComponent(ComponentType.NOTE, skinParam, StringUtils.getWithNewlines(display));

		final int width = (int) (comp.getPreferredWidth(stringBounder) * dpiFactor);
		final int height = (int) (comp.getPreferredHeight(stringBounder) * dpiFactor);

		final Color background = diagram.getSkinParam().getBackgroundColor().getColor();
		final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, background);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		comp.drawU(new UGraphicG2d(g2d, null, dpiFactor), new Dimension(width, height), new SimpleContext2D(false));
		PngIO.write(im, fPng, dpi);
		g2d.dispose();

		final UGraphicSvg ug = new UGraphicSvg(true);
		comp.drawU(ug, new Dimension(width, height), new SimpleContext2D(false));

		final File fEps = FileUtils.createTempFile("plantumlB", ".eps");
		final PrintWriter pw = new PrintWriter(fEps);
		final UGraphicEps uEps = new UGraphicEps(EpsStrategy.getDefault());
		comp.drawU(uEps, new Dimension(width, height), new SimpleContext2D(false));
		pw.print(uEps.getEPSCode());
		pw.close();

		return DrawFile.createFromFile(fPng, getSvg(ug), fEps);
	}

	private DrawFile createImageForNote(String display, HtmlColor backColor, final double dpiFactor, final int dpi)
			throws IOException {

		final Rose skin = new Rose();

		final ISkinParam skinParam = new SkinParamBackcolored(getSkinParam(), backColor);
		final Component comp = skin
				.createComponent(ComponentType.NOTE, skinParam, StringUtils.getWithNewlines(display));

		final int width = (int) (comp.getPreferredWidth(stringBounder) * dpiFactor);
		final int height = (int) (comp.getPreferredHeight(stringBounder) * dpiFactor);

		final Color background = diagram.getSkinParam().getBackgroundColor().getColor();

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final File fPng = FileUtils.createTempFile("plantumlB", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, background);
				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				comp.drawU(new UGraphicG2d(g2d, null, dpiFactor), new Dimension(width, height), new SimpleContext2D(
						false));
				PngIO.write(im, fPng, dpi);
				g2d.dispose();
				return fPng;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				final UGraphicSvg ug = new UGraphicSvg(true);
				comp.drawU(ug, new Dimension(width, height), new SimpleContext2D(false));
				return getSvg(ug);
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File fEps = FileUtils.createTempFile("plantumlB", ".eps");
				final PrintWriter pw = new PrintWriter(fEps);
				final UGraphicEps uEps = new UGraphicEps(EpsStrategy.getDefault());
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
		final Color interfaceBackground = rose.getHtmlColor(getSkinParam(), ColorParam.componentInterfaceBackground,
				stereo).getColor();
		final Color interfaceBorder = rose.getHtmlColor(getSkinParam(), ColorParam.componentInterfaceBorder, stereo)
				.getColor();
		final Color background = rose.getHtmlColor(getSkinParam(), ColorParam.background, stereo).getColor();
		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(circleInterface.getPreferredWidth(null)
						* dpiFactor, circleInterface.getPreferredHeight(null) * dpiFactor, background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(g2d, null, 1.0));

				final File png = FileUtils.createTempFile("circleinterface", ".png");
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("circleinterface", ".eps");
				UGraphicEps.copyEpsToFile(circleInterface, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(circleInterface);
			}

		};

		final Object signature = Arrays.asList("circleinterface", interfaceBackground, interfaceBorder, background,
				dpiFactor);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

	private DrawFile createImageForActor(Entity entity, final double dpiFactor) throws IOException {
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		final Color actorBackground = rose.getHtmlColor(getSkinParam(), ColorParam.usecaseActorBackground, stereo)
				.getColor();
		final Color actorBorder = rose.getHtmlColor(getSkinParam(), ColorParam.usecaseActorBorder, stereo).getColor();
		final Color background = rose.getHtmlColor(getSkinParam(), ColorParam.background, stereo).getColor();
		final StickMan stickMan = new StickMan(actorBackground, actorBorder);

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(stickMan.getPreferredWidth(null) * dpiFactor,
				// stickMan.getPreferredHeight(null) * dpiFactor, dpiFactor > 1
						// ? Color.BLUE : background);
						stickMan.getPreferredHeight(null) * dpiFactor, background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				stickMan.drawU(new UGraphicG2d(g2d, null, dpiFactor));

				final File png = FileUtils.createTempFile("actor", ".png");
				ImageIO.write(im, "png", png);
				return png;
			}
		};
		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("actor", ".eps");
				UGraphicEps.copyEpsToFile(stickMan, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(stickMan);
			}

		};

		final Object signature = Arrays.asList("actor", actorBackground, actorBorder, background, dpiFactor);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

	private DrawFile createImageForCircleCharacter(Entity entity, double dpiFactor) throws IOException {
		final Stereotype stereotype = entity.getStereotype();

		if (stereotype == null || stereotype.getColor() == null) {
			return null;
		}

		final String stereo = stereotype.getLabel();

		final Color classBorder = rose.getHtmlColor(getSkinParam(), ColorParam.classBorder, stereo).getColor();
		final Color classBackground = rose.getHtmlColor(getSkinParam(), ColorParam.classBackground, stereo).getColor();
		final Font font = diagram.getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, stereo);
		final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(), getSkinParam()
				.getCircledCharacterRadius(), font, stereotype.getColor(), classBorder, Color.BLACK);
		return circledCharacter.generateCircleCharacter(classBackground, dpiFactor);
	}

	private ISkinParam getSkinParam() {
		return diagram.getSkinParam();
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
			graphviz.createPng(baos);
			baos.close();

			dotMaker.clean();

			final boolean isUnderline = dotMaker.isUnderline();
			String eps = new String(baos.toByteArray(), "UTF-8");
			if (isUnderline) {
				eps = new UnderlineTrickEps(eps).getString();
			}

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

			os.write(eps.getBytes("UTF-8"));

		} finally {
			// cleanTemporaryFiles(diagram.entities().values());
			// cleanTemporaryFiles(diagram.getLinks());
		}
		return null;
	}

	private GraphvizMaker populateImagesAndCreateGraphvizMaker(List<String> dotStrings,
			FileFormatOption fileFormatOption) throws IOException, InterruptedException {
		populateImages(diagram.getDpiFactor(fileFormatOption), diagram.getDpi(fileFormatOption));
		populateImagesLink(diagram.getDpiFactor(fileFormatOption), diagram.getDpi(fileFormatOption));
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
				diagram.getSkinParam(), diagram.getRankdir(), diagram, diagram);

		dotData.setDpi(diagram.getDpi(fileFormatOption));

		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS || diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			dotData.setStaticImagesMap(staticFilesMap);

			if (diagram.isVisibilityModifierPresent()) {
				dotData.setVisibilityModifierPresent(true);
			}
		}
		
		return diagram.getStrategy().getGraphvizMaker(dotData, dotStrings, fileFormat);
		// return new DotMaker(dotData, dotStrings, fileFormat);
	}

	private void populateImages(double dpiFactor, int dpi) throws IOException {
		for (Entity entity : diagram.entities().values()) {
			final DrawFile f = createImage(entity, dpiFactor, dpi);
			if (f != null) {
				entity.setImageFile(f);
			}
		}
	}

	private void populateImagesLink(double dpiFactor, int dpi) throws IOException {
		for (Link link : diagram.getLinks()) {
			final String note = link.getNote();
			if (note == null) {
				continue;
			}
			final DrawFile f = createImageForNote(note, null, dpiFactor, dpi);
			if (f != null) {
				link.setImageFile(f);
			}
		}
	}

}
