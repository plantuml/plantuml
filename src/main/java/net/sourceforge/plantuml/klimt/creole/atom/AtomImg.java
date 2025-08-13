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
package net.sourceforge.plantuml.klimt.creole.atom;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.creole.legacy.AtomTextUtils;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.ImgValign;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TileImageSvg;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Base64Coder;

public class AtomImg extends AbstractAtom implements Atom {

	public static final String DATA_IMAGE_PNG_BASE64 = "data:image/png;base64,";
	public static final String DATA_IMAGE_PNG_SPM = "data:image/png;spm ";
	private static final String DATA_IMAGE_SVG_BASE64 = "data:image/svg+xml;base64,";
	private final BufferedImage image;
	private final double scale;
	private final Url url;
	private final String rawFileName;

	private AtomImg(BufferedImage image, double scale, Url url, String rawFileName) {
		this.image = image;
		this.scale = scale;
		this.url = url;
		this.rawFileName = rawFileName;
	}

	public static Atom createQrcode(String flash, double scale) {
		BufferedImage im = null;
		// :: comment when __CORE__
		im = FlashCodeFactory.getFlashCodeUtils().exportFlashcode(flash, Color.BLACK, Color.WHITE);
		if (im == null)
			// ::done
			im = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

		return new AtomImg(
				new UImage(new PixelImage(im, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(scale).getImage(1), 1,
				null, null);
	}

	public static Atom create(String src, ImgValign valign, int vspace, double scale, Url url) {
		final UFont font = UFont.monospaced(14);
		final FontConfiguration fc = FontConfiguration.blackBlueTrue(font);

		if (src.startsWith(DATA_IMAGE_PNG_SPM)) {
			final String data = src.substring(DATA_IMAGE_PNG_SPM.length(), src.length());
			final int x = data.indexOf('/');
			if (x == -1)
				return AtomTextUtils.createLegacy("ERROR DATA IMAGE", fc);
			try {
				final Stdlib lib = Stdlib.retrieve(data.substring(0, x));
				final int num = Integer.parseInt(data.substring(x + 1));
				final BufferedImage image = lib.readDataImagePng(num).getNow();
				return new AtomImg(image, scale, url, null);


			} catch (Exception e) {
				return AtomTextUtils.createLegacy("ERROR " + e.toString(), fc);
			}

		}

		if (src.startsWith(DATA_IMAGE_PNG_BASE64)) {
			final String data = src.substring(DATA_IMAGE_PNG_BASE64.length(), src.length());
			try {
				final byte bytes[] = Base64Coder.decode(data);
				return buildRasterFromData(src, fc, bytes, scale, url);
			} catch (Exception e) {
				return AtomTextUtils.createLegacy("ERROR " + e.toString(), fc);
			}
		}

		if (src.startsWith(DATA_IMAGE_SVG_BASE64)) {
			final String data = src.substring(DATA_IMAGE_SVG_BASE64.length(), src.length());
			try {
				final byte bytes[] = Base64Coder.decode(data);
				final String tmp = new String(bytes);
				return new AtomImgSvg(new TileImageSvg(tmp, scale));
			} catch (Exception e) {
				return AtomTextUtils.createLegacy("ERROR " + e.toString(), fc);
			}
		}

		try {
			// Check if valid URL
			if (src.startsWith("http:") || src.startsWith("https:")) {
				if (src.endsWith(".svg"))
					return buildSvgFromUrl(src, fc, SURL.create(src), scale, url);

				return buildRasterFromUrl(src, fc, SURL.create(src), scale, url);
			}
			final SFile f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE)
					return AtomTextUtils.createLegacy("(File not found: " + f.getPrintablePath() + ")", fc);

				return AtomTextUtils.createLegacy("(Cannot decode)", fc);
			}
			if (f.getName().endsWith(".svg")) {
				final String tmp = FileUtils.readSvg(f);
				if (tmp == null)
					return AtomTextUtils.createLegacy("(Cannot decode)", fc);

				return new AtomImgSvg(new TileImageSvg(tmp, scale));
			}
			final BufferedImage read = f.readRasterImageFromFile();
			if (read == null) {
				if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE)
					return AtomTextUtils.createLegacy("(Cannot decode: " + f.getPrintablePath() + ")", fc);

				return AtomTextUtils.createLegacy("(Cannot decode)", fc);
			}
			return new AtomImg(f.readRasterImageFromFile(), scale, url, src);
		} catch (IOException e) {
			Logme.error(e);
			if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE)
				return AtomTextUtils.createLegacy("ERROR " + e.toString(), fc);

			return AtomTextUtils.createLegacy("ERROR", fc);
		}
	}

	private static Atom buildRasterFromData(String source, final FontConfiguration fc, final byte[] data, double scale,
			Url url) throws IOException {
		final BufferedImage read = SImageIO.read(data);
		if (read == null)
			return AtomTextUtils.createLegacy("(Cannot decode: " + source + ")", fc);

		return new AtomImg(read, scale, url, null);
	}

	private static Atom buildRasterFromUrl(String text, final FontConfiguration fc, SURL source, double scale, Url url)
			throws IOException {
		if (source == null)
			return AtomTextUtils.createLegacy("(Cannot decode: " + text + ")", fc);

		final BufferedImage read = source.readRasterImageFromURL();
		if (read == null)
			return AtomTextUtils.createLegacy("(Cannot decode: " + text + ")", fc);

		return new AtomImg(read, scale, url, "http");
	}

	private static Atom buildSvgFromUrl(String text, final FontConfiguration fc, SURL source, double scale, Url url)
			throws IOException {
		if (source == null)
			return AtomTextUtils.createLegacy("(Cannot decode SVG: " + text + ")", fc);

		final byte[] read = source.getBytes();
		if (read == null)
			return AtomTextUtils.createLegacy("(Cannot decode SVG: " + text + ")", fc);

		return new AtomImgSvg(new TileImageSvg(new String(read, UTF_8), scale));
	}

	// End

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(image.getWidth() * scale, image.getHeight() * scale);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		if (url != null)
			ug.startUrl(url);

		ug.draw(new UImage(new PixelImage(image, AffineTransformType.TYPE_BILINEAR)).withRawFileName(rawFileName)
				.scale(scale));

		if (url != null)
			ug.closeUrl();

	}

}
