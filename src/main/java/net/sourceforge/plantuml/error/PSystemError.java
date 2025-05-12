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
 */
package net.sourceforge.plantuml.error;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.atmp.ImageBuilder;
import net.atmp.PixelImage;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileImageData;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.asciiart.UmlCharArea;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.eggs.PSystemWelcome;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockRaw;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.version.LicenseInfo;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public abstract class PSystemError extends PlainDiagram {
	// ::remove folder when __HAXE__

	// Dodgy kludge for testing - we will need a different approach if we want to
	// test addMessageDedication() etc.
	private static boolean disableTimeBasedErrorDecorations = false;

	public static void disableTimeBasedErrorDecorations() {
		PSystemError.disableTimeBasedErrorDecorations = true;
	}

	protected List<StringLocated> trace;
	protected ErrorUml singleError;

	public PSystemError(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).blackBackcolor().randomPixel().status(FileImageData.ERROR);
	}

	final protected StringLocated getLastLine() {
		return trace.get(trace.size() - 1);
	}

	final public LineLocation getLineLocation() {
		return getLastLine().getLocation();
	}

	final public Collection<ErrorUml> getErrorsUml() {
		return Collections.singleton(singleError);
	}

	final public ErrorUml getFirstError() {
		return singleError;
	}

	final public String getWarningOrError() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getDescription());
		sb.append(BackSlash.CHAR_NEWLINE);
		for (CharSequence t : getTitle().getDisplay()) {
			sb.append(t);
			sb.append(BackSlash.CHAR_NEWLINE);
		}
		sb.append(BackSlash.CHAR_NEWLINE);
		return sb.toString();
	}

	private TextBlock getGraphicalFormatted() {
		final FontConfiguration fc0 = GraphicStrings.sansSerif14(HColors.BLACK).bold();
		final FontConfiguration fc1 = GraphicStrings.sansSerif14(HColors.MY_GREEN).bold();
		final FontConfiguration fc2 = GraphicStrings.sansSerif14(HColors.RED).bold();
		final FontConfiguration fc4 = GraphicStrings.sansSerif12(HColors.MY_GREEN).bold().italic();

		final List<String> fullBody = getTextFullBody();
		final TextBlock result0 = TextBlockUtils.addBackcolor(
				TextBlockUtils.withMargin(new TextBlockRaw(getTextFromStack(), fc0), 1, 1, 1, 4), HColors.MY_GREEN);
		final TextBlock result1 = new TextBlockRaw(allButLast(fullBody), fc1);
		final TextBlock result2 = new TextBlockRaw(onlyLast(fullBody), fc1.wave(HColors.RED));
		final TextBlock result3 = new TextBlockRaw(getTextError(), fc2);
		final TextBlock result4 = TextBlockUtils.withMargin(new TextBlockRaw(header(), fc4), 0, 2, 0, 8);
		TextBlock result = result0;
		result = TextBlockUtils.mergeTB(result, result1, HorizontalAlignment.LEFT);
		result = TextBlockUtils.mergeTB(result, result2, HorizontalAlignment.LEFT);
		result = TextBlockUtils.mergeTB(result, result3, HorizontalAlignment.LEFT);
		result = TextBlockUtils.mergeTB(result4, result, HorizontalAlignment.LEFT);
		result = TextBlockUtils.withMargin(result, 5, 5);
		return TextBlockUtils.addBackcolor(result, HColors.BLACK);
	}

	private List<String> header() {
		final ReportLog result = new ReportLog();
		result.add("PlantUML " + Version.versionString());
		result.checkOldVersionWarningRaw();
		return result.asList();
	}

	public List<String> getPureAsciiFormatted() {
		final List<String> result = getTextFromStack();
		result.addAll(getTextFullBody());
		result.add("^^^^^");
		result.addAll(getTextError());
		return result;
	}

	private List<String> getTextFromStack() {
		LineLocation lineLocation = getLineLocation();
		final List<String> result = new ArrayList<>();
		if (lineLocation != null) {
			append(result, lineLocation);
			while (lineLocation.getParent() != null) {
				lineLocation = lineLocation.getParent();
				append(result, lineLocation);
			}
		}
		return result;
	}

	protected List<String> getTextFullBody() {
		final List<String> result = new ArrayList<>();
		result.add(" ");
		final int traceSize = trace.size();
		if (traceSize > 40) {
			for (StringLocated s : trace.subList(0, 5)) {
				addToResult(result, s);
			}
			result.add("...");
			final int skipped = traceSize - 5 - 20;
			result.add("... ( skipping " + skipped + " lines )");
			result.add("...");
			for (StringLocated s : trace.subList(traceSize - 20, traceSize)) {
				addToResult(result, s);
			}
		} else {
			for (StringLocated s : trace) {
				addToResult(result, s);
			}
		}
		return result;
	}

	private void addToResult(final List<String> result, StringLocated s) {
		String tmp = s.getString();
		if (tmp.length() > 120) {
			tmp = tmp.substring(0, 120) + " ...";
		}
		result.add(tmp);
	}

	private List<String> getTextError() {
		return Arrays.asList(" " + singleError.getError());
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		// ::comment when __CORE__
		if (fileFormat.getFileFormat() == FileFormat.ATXT || fileFormat.getFileFormat() == FileFormat.UTXT) {
			final UGraphicTxt ugt = new UGraphicTxt();
			final UmlCharArea area = ugt.getCharArea();
			area.drawStringsLRSimple(getPureAsciiFormatted(), 0, 0);
			area.print(SecurityUtils.createPrintStream(os));
			return new ImageDataSimple(1, 1);

		}
		// ::done
		return super.exportDiagramNow(os, num, fileFormat);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
		final TextBlock result = getGraphicalFormatted();

		TextBlock udrawable;
		if (getSource().getTotalLineCountLessThan5()) {
			udrawable = addWelcome(result);
		} else {
			udrawable = result;
		}
		final int min = (int) (System.currentTimeMillis() / 60000L) % 60;
		// udrawable = addMessageAdopt(udrawable);
		if (disableTimeBasedErrorDecorations) {
			// do nothing
		} else if (min == 1 || min == 8 || min == 13 || min == 55) {
			udrawable = addMessagePatreon(udrawable);
		} else if (min == 15) {
			udrawable = addMessageLiberapay(udrawable);
		} else if (min == 30 || min == 39 || min == 48) {
			udrawable = addMessageDedication(udrawable);
			// ::comment when __CORE__
		} else if (getSource().containsIgnoreCase("arecibo")) {
			udrawable = addMessageArecibo(udrawable);
			// ::done
		}
		return udrawable;
	}

	private void append(List<String> result, LineLocation lineLocation) {
		if (lineLocation.getDescription() != null) {
			result.add("[From " + lineLocation.getDescription() + " (line " + (lineLocation.getPosition() + 1) + ") ]");
		}
	}

	final public DiagramDescription getDescription() {
		return new DiagramDescription("(Error)");
	}

	private List<String> allButLast(List<String> full) {
		return full.subList(0, full.size() - 1);
	}

	private List<String> onlyLast(List<String> full) {
		return full.subList(full.size() - 1, full.size());
	}

	private TextBlock getWelcome() throws IOException {
		return new PSystemWelcome(getSource(), GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT, getPreprocessingArtifact())
				.getGraphicStrings();
	}

	private TextBlock addWelcome(final TextBlock result) throws IOException {
		final TextBlock welcome = getWelcome();
		return TextBlockUtils.mergeTB(welcome, result, HorizontalAlignment.LEFT);
	}

	private TextBlock addMessageLiberapay(final TextBlock source) throws IOException {
		// ::comment when __CORE__
		if (LicenseInfo.retrieveNamedOrDistributorQuickIsValid())
			return source;
		// ::done

		final TextBlock message = getMessageLiberapay();
		TextBlock result = TextBlockUtils.mergeTB(message, source, HorizontalAlignment.LEFT);
		result = TextBlockUtils.mergeTB(result, message, HorizontalAlignment.LEFT);
		return result;
	}

	private TextBlock addMessagePatreon(final TextBlock source) throws IOException {
		// ::comment when __CORE__
		if (LicenseInfo.retrieveNamedOrDistributorQuickIsValid())
			return source;
		// ::done

		final TextBlock message = getMessagePatreon();
		TextBlock result = TextBlockUtils.mergeTB(message, source, HorizontalAlignment.LEFT);
		result = TextBlockUtils.mergeTB(result, message, HorizontalAlignment.LEFT);
		return result;
	}

	private TextBlock addMessageDedication(final TextBlock source) throws IOException {
		// ::comment when __CORE__
		if (LicenseInfo.retrieveNamedOrDistributorQuickIsValid())
			return source;
		// ::done

		final TextBlock message = getMessageDedication();
		TextBlock result = TextBlockUtils.mergeTB(message, source, HorizontalAlignment.LEFT);
		return result;
	}

	// ::comment when __CORE__
	private TextBlock addMessageAdopt(final TextBlock source) throws IOException {
		if (LicenseInfo.retrieveNamedOrDistributorQuickIsValid()) {
			return source;
		}
		final TextBlock message = getMessageAdopt();
		TextBlock result = TextBlockUtils.mergeTB(message, source, HorizontalAlignment.LEFT);
		return result;
	}

	private TextBlock addMessageArecibo(final TextBlock source) throws IOException {
		final UImage message = new UImage(
				new PixelImage(PSystemVersion.getArecibo(), AffineTransformType.TYPE_BILINEAR));
		TextBlock result = TextBlockUtils.mergeLR(source, TextBlockUtils.fromUImage(message), VerticalAlignment.TOP);
		return result;
	}
	// ::done

	private TextBlock getMessageDedication() {
		final HColorSimple backColor = (HColorSimple) HColorSet.instance().getColorOrWhite("#eae2c9");

		// ::comment when __CORE__
		final BufferedImage qrcode = smaller(FlashCodeFactory.getFlashCodeUtils()
				.exportFlashcode("https://plantuml.com/dedication", Color.BLACK, backColor.getAwtColor()));
		// ::done
		final Display disp = Display.create("<b>Add your own dedication into PlantUML", " ", "For just $5 per month!",
				"Details on <i>[[https://plantuml.com/dedication]]");

		final UFont font = UFont.sansSerif(14);
		final FontConfiguration fc = FontConfiguration.create(font, HColors.BLACK, HColors.BLACK, null);
		final TextBlock text = TextBlockUtils
				.withMargin(disp.create(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty()), 10, 0);
		final TextBlock result;
		// ::comment when __CORE__
		if (qrcode == null) {
			// ::done
			result = text;
			// ::comment when __CORE__
		} else {
			final UImage qr = new UImage(new PixelImage(qrcode, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(3);
			result = TextBlockUtils.mergeLR(text, TextBlockUtils.fromUImage(qr), VerticalAlignment.CENTER);
		}
		// ::done
		return TextBlockUtils.addBackcolor(result, backColor);

	}

	private TextBlock getMessageAdopt() {
		final HColor backColor = HColorSet.instance().getColorOrWhite("#eff4d2");

		final Display disp = Display.create("<b>Adopt-a-Word and put your message here!", " ",
				"Details on <i>[[https://plantuml.com/adopt]]", " ");

		final UFont font = UFont.sansSerif(14);
		final FontConfiguration fc = FontConfiguration.create(font, HColors.BLACK, HColors.BLACK, null);
		final TextBlock text = TextBlockUtils
				.withMargin(disp.create(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty()), 10, 0);
		final TextBlock result;
		result = text;
		return TextBlockUtils.addBackcolor(result, backColor);

	}

	private TextBlock getMessagePatreon() {
		final UImage message = new UImage(
				new PixelImage(PSystemVersion.getTime01(), AffineTransformType.TYPE_BILINEAR));
		final Color back = new Color(message.getImage(1).getRGB(0, 0));
		final HColor backColor = HColors.simple(back);

		// ::comment when __CORE__
		final BufferedImage qrcode = smaller(FlashCodeFactory.getFlashCodeUtils()
				.exportFlashcode("https://plantuml.com/patreon", Color.BLACK, Color.WHITE));
		// ::done

		final int scale = 2;

		final double imWidth;
		final double imHeight;
		// ::comment when __CORE__
		if (qrcode == null) {
			// ::done
			imWidth = message.getWidth();
			imHeight = message.getHeight();
			// ::comment when __CORE__
		} else {
			imWidth = message.getWidth() + qrcode.getWidth() * scale + 20;
			imHeight = Math.max(message.getHeight(), qrcode.getHeight() * scale + 10);
		}
		// ::done
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				// ::comment when __CORE__
				if (qrcode == null) {
					// ::done
					ug.apply(new UTranslate(1, 1)).draw(message);
					// ::comment when __CORE__
				} else {
					final UImage qr = new UImage(new PixelImage(qrcode, AffineTransformType.TYPE_NEAREST_NEIGHBOR))
							.scale(scale);
					ug.apply(new UTranslate(1, (imHeight - message.getHeight()) / 2)).draw(message);
					ug.apply(new UTranslate(1 + message.getWidth(), (imHeight - qr.getHeight()) / 2)).draw(qr);
				}
				// ::done
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(imWidth + 1, imHeight + 1);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return MinMax.fromMax(imWidth + 1, imHeight + 1);
			}

			public HColor getBackcolor() {
				return backColor;
			}
		};

	}

	private TextBlock getMessageLiberapay() {
		final UImage message = new UImage(
				new PixelImage(PSystemVersion.getTime15(), AffineTransformType.TYPE_BILINEAR));
		final Color back = new Color(message.getImage(1).getRGB(0, 0));
		final HColor backColor = HColors.simple(back);

		// ::comment when __CORE__
		final BufferedImage qrcode = smaller(FlashCodeFactory.getFlashCodeUtils()
				.exportFlashcode("https://plantuml.com/lp", Color.BLACK, Color.WHITE));
		// ::done

		final int scale = 2;

		final double imWidth;
		final double imHeight;
		// ::comment when __CORE__
		if (qrcode == null) {
			// ::done
			imWidth = message.getWidth();
			imHeight = message.getHeight();
			// ::comment when __CORE__
		} else {
			imWidth = message.getWidth() + qrcode.getWidth() * scale + 20;
			imHeight = Math.max(message.getHeight(), qrcode.getHeight() * scale + 10);
		}
		// ::done

		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				// ::comment when __CORE__
				if (qrcode == null) {
					// ::done
					ug.apply(new UTranslate(1, 1)).draw(message);
					// ::comment when __CORE__
				} else {
					final UImage qr = new UImage(new PixelImage(qrcode, AffineTransformType.TYPE_NEAREST_NEIGHBOR))
							.scale(scale);
					ug.apply(new UTranslate(1, (imHeight - message.getHeight()) / 2)).draw(message);
					ug.apply(new UTranslate(1 + message.getWidth(), (imHeight - qr.getHeight()) / 2)).draw(qr);
				}
				// ::done
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(imWidth + 1, imHeight + 1);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return MinMax.fromMax(imWidth + 1, imHeight + 1);
			}

			public HColor getBackcolor() {
				return backColor;
			}
		};

	}

	public int score() {
		final int result = trace.size() * 10 + singleError.score();
		return result;
	}

	private BufferedImage smaller(BufferedImage im) {
		if (im == null) {
			return null;
		}
		final int nb = 1;
		return im.getSubimage(nb, nb, im.getWidth() - 2 * nb, im.getHeight() - 2 * nb);
	}

}
