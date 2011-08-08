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
 * Revision $Revision: 6590 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

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
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class StaticFiles {

	private final HtmlColor interfaceBorder;
	private final HtmlColor classborder;
	private final HtmlColor classBackground;
	private final HtmlColor interfaceBackground;
	private final HtmlColor background;

	final private UFont circledFont;
	final private double radius;

	private final Map<EntityType, DrawFile> staticImages = new EnumMap<EntityType, DrawFile>(EntityType.class);
	private final Map<VisibilityModifier, DrawFile> visibilityImages = new EnumMap<VisibilityModifier, DrawFile>(
			VisibilityModifier.class);

	private final Map<VisibilityModifier, HtmlColor> foregroundColor = new EnumMap<VisibilityModifier, HtmlColor>(
			VisibilityModifier.class);
	private final Map<VisibilityModifier, HtmlColor> backgroundColor = new EnumMap<VisibilityModifier, HtmlColor>(
			VisibilityModifier.class);

	private final double dpiFactor;
	private final ColorMapper colorMapper;

	public StaticFiles(ISkinParam param, String stereotype, double dpiFactor) throws IOException {
		this.colorMapper = param.getColorMapper();
		final Rose rose = new Rose();
		this.dpiFactor = dpiFactor;

		radius = param.getCircledCharacterRadius();
		circledFont = param.getFont(FontParam.CIRCLED_CHARACTER, stereotype);

		classborder = rose.getHtmlColor(param, ColorParam.classBorder, stereotype);
		interfaceBorder = rose.getHtmlColor(param, ColorParam.componentInterfaceBorder, stereotype);
		interfaceBackground = rose.getHtmlColor(param, ColorParam.componentInterfaceBackground, stereotype);
		classBackground = rose.getHtmlColor(param, ColorParam.classBackground, stereotype);
		final HtmlColor stereotypeCBackground = rose.getHtmlColor(param, ColorParam.stereotypeCBackground, stereotype);
		final HtmlColor stereotypeABackground = rose.getHtmlColor(param, ColorParam.stereotypeABackground, stereotype);
		final HtmlColor stereotypeIBackground = rose.getHtmlColor(param, ColorParam.stereotypeIBackground, stereotype);
		final HtmlColor stereotypeEBackground = rose.getHtmlColor(param, ColorParam.stereotypeEBackground, stereotype);

		background = param.getBackgroundColor();

		final File dir = FileUtils.getTmpDir();
		staticImages.put(EntityType.LOLLIPOP, getLollipop());
		staticImages.put(EntityType.ABSTRACT_CLASS, getCircledCharacter('A', stereotypeABackground));
		staticImages.put(EntityType.CLASS, getCircledCharacter('C', stereotypeCBackground));
		staticImages.put(EntityType.INTERFACE, getCircledCharacter('I', stereotypeIBackground));
		staticImages.put(EntityType.ENUM, getCircledCharacter('E', stereotypeEBackground));

		if (param.classAttributeIconSize() > 0) {
			for (VisibilityModifier modifier : EnumSet.allOf(VisibilityModifier.class)) {

				final HtmlColor back = modifier.getBackground() == null ? null : rose.getHtmlColor(param,
						modifier.getBackground(), stereotype);
				final HtmlColor fore = rose.getHtmlColor(param, modifier.getForeground(), stereotype);

				backgroundColor.put(modifier, back);
				foregroundColor.put(modifier, fore);
				visibilityImages.put(modifier,
						getVisibilityModifier(modifier, dir, param.classAttributeIconSize(), dpiFactor));
			}
		}
	}

	private DrawFile getLollipop() throws IOException {

		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder, 10, 2);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder(circleInterface.getPreferredWidth(null),
						circleInterface.getPreferredHeight(null), colorMapper.getMappedColor(background));

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(colorMapper, g2d, null, dpiFactor));

				final File result = FileUtils.createTempFile("lollipop", ".png");
				ImageIO.write(im, "png", result);
				return result;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = FileUtils.createTempFile("lollipop", ".eps");
				UGraphicEps.copyEpsToFile(colorMapper, circleInterface, epsFile);
				return epsFile;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(colorMapper, circleInterface);
			}
		};

		final Object signature = Arrays.asList("lollipop", interfaceBackground, interfaceBorder, background);

		return DrawFile.create(lpng, lsvg, leps, signature);

	}

	private DrawFile getCircledCharacter(char c, HtmlColor background) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter(c, radius, circledFont, background, classborder,
				HtmlColor.BLACK);
		return circledCharacter.generateCircleCharacter(colorMapper, classBackground, dpiFactor);
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
		final UDrawable drawable = modifier.getUDrawable(size, foregroundColor.get(modifier),
				backgroundColor.get(modifier));

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final File png = FileUtils.createTempFile("visi", ".png");
				final EmptyImageBuilder builder = new EmptyImageBuilder(size * dpiFactor, size * dpiFactor,
						colorMapper.getMappedColor(classBackground));
				final BufferedImage im = builder.getBufferedImage();
				drawable.drawU(new UGraphicG2d(colorMapper, builder.getGraphics2D(), im, dpiFactor));
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File eps = FileUtils.createTempFile("visi", ".eps");
				UGraphicEps.copyEpsToFile(colorMapper, drawable, eps);
				return eps;
			}
		};

		final Lazy<String> lsvg = new Lazy<String>() {
			public String getNow() throws IOException {
				return UGraphicG2d.getSvgString(colorMapper, drawable);
			}
		};

		final Object signature = Arrays.asList("visi", modifier, foregroundColor.get(modifier),
				backgroundColor.get(modifier), size, classBackground);

		return DrawFile.create(lpng, lsvg, leps, signature);
	}

	public void clean() {
		for (DrawFile f : staticImages.values()) {
			f.deleteDrawFile();
		}
		for (DrawFile f : visibilityImages.values()) {
			f.deleteDrawFile();
		}
	}

}
