package net.sourceforge.plantuml.math;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class AsciiMathSafe {

	private AsciiMath math;
	private final String form;

	public AsciiMathSafe(String form) {
		this.form = form;
		try {
			this.math = new AsciiMath(form);
		} catch (Exception e) {
			Log.info("Error parsing " + form);
		}
	}

	public static void main(String[] args) throws IOException {
		AsciiMathSafe math = new AsciiMathSafe("sum_(i=1)^n i^3=((n(n+1))/2)^2");
		PrintWriter pw = new PrintWriter(new File("math2.svg"));
		pw.println(math.getSvg());
		pw.close();
		ImageIO.write(math.getImage(), "png", new File("math2.png"));
	}

	private ImageData dimSvg;

	public String getSvg() {
		try {
			final String svg = math.getSvg();
			dimSvg = new ImageDataSimple(math.getDimension());
			return svg;
		} catch (Exception e) {
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				dimSvg = imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.SVG), baos);
			} catch (IOException e1) {
				return null;
			}
			return new String(baos.toByteArray());
		}
	}

	public BufferedImage getImage() {
		try {
			return math.getImage();
		} catch (Exception e) {
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.PNG), baos);
				return ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	private ImageBuilder getRollback() {
		final TextBlock block = GraphicStrings.createBlackOnWhiteMonospaced(Arrays.asList(form));
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, null, null, null, 0, 0,
				null, false);
		imageBuilder.setUDrawable(block);
		return imageBuilder;
	}

	public ImageData export(OutputStream os, FileFormatOption fileFormat) throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			final BufferedImage image = getImage();
			ImageIO.write(image, "png", os);
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		if (fileFormat.getFileFormat() == FileFormat.SVG) {
			os.write(getSvg().getBytes());
			return dimSvg;
		}
		return null;
	}

}
