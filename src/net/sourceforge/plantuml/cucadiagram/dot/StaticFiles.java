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
 * Revision $Revision: 5823 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class StaticFiles {

	private final Color interfaceBorder;
	private final Color classborder;
	private final Color classBackground;
	private final Color interfaceBackground;
	private final Color background;

	final private Font circledFont;
	final private double radius;

	private final Map<EntityType, DrawFile> staticImages = new EnumMap<EntityType, DrawFile>(EntityType.class);
	private final Map<VisibilityModifier, DrawFile> visibilityImages = new EnumMap<VisibilityModifier, DrawFile>(
			VisibilityModifier.class);

	private final Map<VisibilityModifier, Color> foregroundColor = new EnumMap<VisibilityModifier, Color>(
			VisibilityModifier.class);
	private final Map<VisibilityModifier, Color> backgroundColor = new EnumMap<VisibilityModifier, Color>(
			VisibilityModifier.class);

	private final double dpiFactor;

	public StaticFiles(ISkinParam param, String stereotype, double dpiFactor) throws IOException {
		final Rose rose = new Rose();
		this.dpiFactor = dpiFactor;

		radius = param.getCircledCharacterRadius();
		circledFont = param.getFont(FontParam.CIRCLED_CHARACTER, stereotype);

		classborder = rose.getHtmlColor(param, ColorParam.classBorder, stereotype).getColor();
		interfaceBorder = rose.getHtmlColor(param, ColorParam.componentInterfaceBorder, stereotype).getColor();
		interfaceBackground = rose.getHtmlColor(param, ColorParam.componentInterfaceBackground, stereotype).getColor();
		classBackground = rose.getHtmlColor(param, ColorParam.classBackground, stereotype).getColor();
		final Color stereotypeCBackground = rose.getHtmlColor(param, ColorParam.stereotypeCBackground, stereotype)
				.getColor();
		final Color stereotypeABackground = rose.getHtmlColor(param, ColorParam.stereotypeABackground, stereotype)
				.getColor();
		final Color stereotypeIBackground = rose.getHtmlColor(param, ColorParam.stereotypeIBackground, stereotype)
				.getColor();
		final Color stereotypeEBackground = rose.getHtmlColor(param, ColorParam.stereotypeEBackground, stereotype)
				.getColor();

		background = param.getBackgroundColor().getColor();

		final File dir = FileUtils.getTmpDir();
		staticImages.put(EntityType.LOLLIPOP, getLollipop());
		staticImages.put(EntityType.ABSTRACT_CLASS, getCircledCharacter('A', stereotypeABackground));
		staticImages.put(EntityType.CLASS, getCircledCharacter('C', stereotypeCBackground));
		staticImages.put(EntityType.INTERFACE, getCircledCharacter('I', stereotypeIBackground));
		staticImages.put(EntityType.ENUM, getCircledCharacter('E', stereotypeEBackground));

		if (param.classAttributeIconSize() > 0) {
			for (VisibilityModifier modifier : EnumSet.allOf(VisibilityModifier.class)) {

				final Color back = modifier.getBackground() == null ? null : rose.getHtmlColor(param,
						modifier.getBackground(), stereotype).getColor();
				final Color fore = rose.getHtmlColor(param, modifier.getForeground(), stereotype).getColor();

				backgroundColor.put(modifier, back);
				foregroundColor.put(modifier, fore);
				visibilityImages.put(modifier, getVisibilityModifier(modifier, dir, param.classAttributeIconSize(),
						dpiFactor));
			}
		}
	}

	private DrawFile getLollipop() throws IOException {

		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder, 10, 2);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(circleInterface.getPreferredWidth(null),
						circleInterface.getPreferredHeight(null), background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(g2d, null, dpiFactor));

				final File result = FileUtils.createTempFile("lollipop", ".png");
				ImageIO.write(im, "png", result);
				return result;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("lollipop", ".eps");
				UGraphicEps.copyEpsToFile(circleInterface, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(circleInterface);
			}
		};

		final Object signature = Arrays.asList("lollipop", interfaceBackground, interfaceBorder, background);

		return DrawFile.create(lpng, lsvg, leps, signature);

	}

	private DrawFile getCircledCharacter(char c, Color background) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter(c, radius, circledFont, background, classborder,
				Color.BLACK);
		return circledCharacter.generateCircleCharacter(classBackground, dpiFactor);
	}

	public DrawFile getStaticImages(EntityType type) {
		return staticImages.get(type);
	}

	public final DrawFile getVisibilityImages(VisibilityModifier visibilityModifier) {
		return visibilityImages.get(visibilityModifier);
	}

	public DrawFile getDrawFile(String pngPath) throws IOException {
		final File searched = new File(pngPath).getCanonicalFile();
		for (DrawFile drawFile : staticImages.values()) {
			final File png = drawFile.getPng().getCanonicalFile();
			if (png.equals(searched)) {
				return drawFile;
			}
		}
		for (DrawFile drawFile : visibilityImages.values()) {
			final File png = drawFile.getPng().getCanonicalFile();
			if (png.equals(searched)) {
				return drawFile;
			}
		}
		return null;
	}

	private DrawFile getVisibilityModifier(final VisibilityModifier modifier, final File dir, final int size,
			final double dpiFactor) throws IOException {
		final UDrawable drawable = modifier.getUDrawable(size, foregroundColor.get(modifier), backgroundColor
				.get(modifier));

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final File png = FileUtils.createTempFile("visi", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder(size * dpiFactor, size * dpiFactor,
						classBackground);
				final BufferedImage im = builder.getBufferedImage();
				drawable.drawU(new UGraphicG2d(builder.getGraphics2D(), im, dpiFactor));
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File eps = FileUtils.createTempFile("visi", ".eps");
				UGraphicEps.copyEpsToFile(drawable, eps);
				return eps;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(drawable);
			}
		};

		final Object signature = Arrays.asList("visi", modifier, foregroundColor.get(modifier), backgroundColor
				.get(modifier), size, classBackground);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

}
