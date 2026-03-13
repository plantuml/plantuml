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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.UgSimpleDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.SignatureUtils;

public class PSystemKeygen extends UgSimpleDiagram {

	final private String key;

	public PSystemKeygen(UmlSource source, String key, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.key = key;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		getTextBlock().drawU(ug);
	}

	private TextBlock getTextBlock() {
		final LicenseInfo installed = LicenseInfo.retrieveNamedSlow();
		if (key.length() == 0 || LicenseInfo.retrieveNamed(key).isNone())
			return getFlashTextBlock(installed);

		final LicenseInfo info = LicenseInfo.retrieveNamed(key);
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
			for (SFile f : LicenseInfo.fileCandidates())
				strings.add(f.getAbsolutePath());
			strings.add(" ");
		} else {
			strings.add("<u>Installed license</u>:");
			License.addLicenseInfo(strings, installed);
			strings.add(" ");
		}
		return GraphicStrings.createBlackOnWhite(strings);
	}

	private TextBlock getFlashTextBlock(LicenseInfo info) {
		final List<String> strings = header();
		strings.add("To get your <i>Professional Edition License</i>,");
		strings.add("please send this qrcode to <b>plantuml@gmail.com</b> :");
		final TextBlock header = GraphicStrings.createBlackOnWhite(strings);

		UImage flash = null;
		try {
			final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
			final PortableImage im = utils.exportFlashcode(
					Version.versionString() + "\n" + SignatureUtils.toHexString(PLSSignature.signature()), XColor.BLACK,
					XColor.WHITE);
			if (im != null)
				flash = new UImage(new PixelImage(im, AffineTransformType.TYPE_NEAREST_NEIGHBOR)).scale(4);
		} catch (IOException e) {
			Logme.error(e);
		}

		TextBlock footer = null;
		if (info.isNone() == false) {
			final List<String> footerStrings = new ArrayList<>();
			footerStrings.add("<u>Installed license</u>:");
			License.addLicenseInfo(footerStrings, info);
			footerStrings.add(" ");
			footer = GraphicStrings.createBlackOnWhite(footerStrings);
		}

		final UImage finalFlash = flash;
		final TextBlock finalFooter = footer;
		return new TextBlock() {
			@Override
			public void drawU(UGraphic ug) {
				header.drawU(ug);
				ug = ug.apply(UTranslate.dy(header.calculateDimension(ug.getStringBounder()).getHeight()));
				if (finalFlash != null) {
					ug.draw(finalFlash);
					ug = ug.apply(UTranslate.dy(finalFlash.getHeight()));
				}
				if (finalFooter != null)
					finalFooter.drawU(ug);
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dimHeader = header.calculateDimension(stringBounder);
				double width = dimHeader.getWidth();
				double height = dimHeader.getHeight();
				if (finalFlash != null) {
					width = Math.max(width, finalFlash.getWidth());
					height += finalFlash.getHeight();
				}
				if (finalFooter != null) {
					final XDimension2D dimFooter = finalFooter.calculateDimension(stringBounder);
					width = Math.max(width, dimFooter.getWidth());
					height += dimFooter.getHeight();
				}
				return new XDimension2D(width, height);
			}
		};
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Key)");
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

}
