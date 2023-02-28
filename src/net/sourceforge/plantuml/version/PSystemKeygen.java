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
package net.sourceforge.plantuml.version;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.SignatureUtils;

public class PSystemKeygen extends PlainDiagram {
	// ::remove file when __CORE__

	final private String key;

	public PSystemKeygen(UmlSource source, String key) {
		super(source);
		this.key = key;
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				try {
					drawInternal(ug);
				} catch (IOException e) {
					Logme.error(e);
				}
			}
		};
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Key)");
	}

	private void drawInternal(UGraphic ug) throws IOException {
		final LicenseInfo installed = LicenseInfo.retrieveNamedSlow();
		if (key.length() == 0) {
			drawFlash(ug, installed);
			return;
		}
		final LicenseInfo info = LicenseInfo.retrieveNamed(key);
		if (info.isNone()) {
			drawFlash(ug, installed);
			return;
		}
		final List<String> strings = header();
		strings.add("<u>Provided license information</u>:");
		License.addLicenseInfo(strings, info);
		strings.add(" ");
		strings.add("========================================================================");
		try {
			LicenseInfo.persistMe(key);
		} catch (BackingStoreException e) {
			strings.add("<i>Error: Cannot store license key.</i>");
		}

		if (installed.isNone()) {
			strings.add("No license currently installed.");
			strings.add(" ");
			strings.add("<b>Please copy license.txt to one of those files</b>:");
			for (SFile f : LicenseInfo.fileCandidates()) {
				strings.add(f.getAbsolutePath());
			}
			strings.add(" ");
		} else {
			strings.add("<u>Installed license</u>:");
			License.addLicenseInfo(strings, installed);
			strings.add(" ");
		}

		final TextBlock disp = GraphicStrings.createBlackOnWhite(strings);
		disp.drawU(ug);
	}

	private ArrayList<String> header() {
		final ArrayList<String> strings = new ArrayList<>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
//		if (OptionFlags.ALLOW_INCLUDE) {
//			strings.add("Loaded from " + Version.getJarPath());
//		}
		strings.add(" ");
		return strings;
	}

	private void drawFlash(UGraphic ug, LicenseInfo info) throws IOException {
		final List<String> strings = header();
		strings.add("To get your <i>Professional Edition License</i>,");
		strings.add("please send this qrcode to <b>plantuml@gmail.com</b> :");

		TextBlock disp = GraphicStrings.createBlackOnWhite(strings);
		disp.drawU(ug);

		ug = ug.apply(UTranslate.dy(disp.calculateDimension(ug.getStringBounder()).getHeight()));
		final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
		final BufferedImage im = utils.exportFlashcode(
				Version.versionString() + "\n" + SignatureUtils.toHexString(PLSSignature.signature()), Color.BLACK,
				Color.WHITE);
		if (im != null) {
			final UImage flash = new UImage(new PixelImage(im, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(4);
			ug.draw(flash);
			ug = ug.apply(UTranslate.dy(flash.getHeight()));
		}

		if (info.isNone() == false) {
			strings.clear();
			strings.add("<u>Installed license</u>:");
			License.addLicenseInfo(strings, info);
			strings.add(" ");
			disp = GraphicStrings.createBlackOnWhite(strings);
			disp.drawU(ug);
		}

	}
}
