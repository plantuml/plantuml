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
 * Revision $Revision: 5339 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class StaticFiles {

	private final String circleInterfaceName = "cinterface.png";
	private final String lollipopName = "lollipop.png";
	private final String actorName = "actor.png";
	private final String cName = "stereotypec.png";
	private final String iName = "stereotypei.png";
	private final String aName = "stereotypea.png";
	private final String eName = "stereotypee.png";

	private final Color stereotypeCBackground;
	private final Color stereotypeIBackground;
	private final Color stereotypeABackground;
	private final Color stereotypeEBackground;

	private final Color interfaceBorder;
	private final Color classborder;
	private final Color actorBorder;
	private final Color classBackground;
	private final Color actorBackground;
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

	private static final Collection<DrawFile> toDelete = new ArrayList<DrawFile>();

	private void deleteOnExit() {
		if (toDelete.isEmpty()) {
			toDelete.addAll(staticImages.values());
			toDelete.addAll(visibilityImages.values());
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
						for (DrawFile f : toDelete) {
							f.delete();
						}
					}
				}
			});
		}
	}

	public StaticFiles(ISkinParam param) throws IOException {
		final Rose rose = new Rose();
		radius = param.getCircledCharacterRadius();
		circledFont = param.getFont(FontParam.CIRCLED_CHARACTER);
		// circledFont = new Font("Courier", Font.BOLD, 17);

		actorBorder = rose.getHtmlColor(param, ColorParam.actorBorder).getColor();
		classborder = rose.getHtmlColor(param, ColorParam.classBorder).getColor();
		interfaceBorder = rose.getHtmlColor(param, ColorParam.interfaceBorder).getColor();
		interfaceBackground = rose.getHtmlColor(param, ColorParam.interfaceBackground).getColor();
		actorBackground = rose.getHtmlColor(param, ColorParam.actorBackground).getColor();
		classBackground = rose.getHtmlColor(param, ColorParam.classBackground).getColor();
		stereotypeCBackground = rose.getHtmlColor(param, ColorParam.stereotypeCBackground).getColor();
		stereotypeABackground = rose.getHtmlColor(param, ColorParam.stereotypeABackground).getColor();
		stereotypeIBackground = rose.getHtmlColor(param, ColorParam.stereotypeIBackground).getColor();
		stereotypeEBackground = rose.getHtmlColor(param, ColorParam.stereotypeEBackground).getColor();

		background = param.getBackgroundColor().getColor();

		final File dir = getTmpDir();
		staticImages.put(EntityType.LOLLIPOP, ensurePngLollipopPresent(dir));
		staticImages.put(EntityType.CIRCLE_INTERFACE, ensurePngCircleInterfacePresent(dir));
		staticImages.put(EntityType.ACTOR, ensurePngActorPresent(dir));
		staticImages.put(EntityType.ABSTRACT_CLASS, ensurePngAPresent(dir));
		staticImages.put(EntityType.CLASS, ensurePngCPresent(dir));
		staticImages.put(EntityType.INTERFACE, ensurePngIPresent(dir));
		staticImages.put(EntityType.ENUM, ensurePngEPresent(dir));

		if (param.classAttributeIconSize() > 0) {
			for (VisibilityModifier modifier : EnumSet.allOf(VisibilityModifier.class)) {

				final Color back = modifier.getBackground() == null ? null : rose.getHtmlColor(param,
						modifier.getBackground()).getColor();
				final Color fore = rose.getHtmlColor(param, modifier.getForeground()).getColor();

				backgroundColor.put(modifier, back);
				foregroundColor.put(modifier, fore);
				visibilityImages.put(modifier,
						ensureVisibilityModifierPresent(modifier, dir, param.classAttributeIconSize()));
			}
		}

		deleteOnExit();
	}

	public File getTmpDir() {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		if (tmpDir.exists() == false || tmpDir.isDirectory() == false) {
			throw new IllegalStateException();
		}
		return tmpDir;
	}

	public static void delete(File f) {
		if (f == null) {
			return;
		}
		Thread.yield();
		Log.info("Deleting temporary file " + f);
		final boolean ok = f.delete();
		if (ok == false) {
			Log.error("Cannot delete: " + f);
		}
	}

	private DrawFile ensurePngActorPresent(final File dir) throws IOException {
		final StickMan stickMan = new StickMan(actorBackground, actorBorder);

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder((int) stickMan.getPreferredWidth(null),
						(int) stickMan.getPreferredHeight(null), background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				stickMan.drawU(new UGraphicG2d(g2d, null));

				final File png = new File(dir, actorName);
				Log.info("Creating temporary file: " + png);
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = new File(dir, actorName.replaceFirst("\\.png", ".eps"));
				UGraphicEps.copyEpsToFile(stickMan, epsFile);
				return epsFile;
			}
		};

		return new DrawFile(lpng, UGraphicG2d.getSvgString(stickMan), leps);
	}

	private DrawFile ensurePngCircleInterfacePresent(final File dir) throws IOException {

		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder((int) circleInterface.getPreferredWidth(null),
						(int) circleInterface.getPreferredHeight(null), background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(g2d, null));

				final File png = new File(dir, circleInterfaceName);
				Log.info("Creating temporary file: " + png);
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File epsFile = new File(dir, circleInterfaceName.replaceFirst("\\.png", ".eps"));
				Log.info("Creating temporary file: " + epsFile);
				UGraphicEps.copyEpsToFile(circleInterface, epsFile);
				return epsFile;
			}
		};

		return new DrawFile(lpng, UGraphicG2d.getSvgString(circleInterface), leps);

	}

	private DrawFile ensurePngLollipopPresent(final File dir) throws IOException {

		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder, 10, 2);

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				final EmptyImageBuilder builder = new EmptyImageBuilder((int) circleInterface.getPreferredWidth(null),
						(int) circleInterface.getPreferredHeight(null), background);

				final BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();

				circleInterface.drawU(new UGraphicG2d(g2d, null));

				final File result = new File(dir, lollipopName);
				Log.info("Creating temporary file: " + result);
				ImageIO.write(im, "png", result);
				return result;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {

			public File getNow() throws IOException {
				final File epsFile = new File(dir, lollipopName.replaceFirst("\\.png", ".eps"));
				Log.info("Creating temporary file: " + epsFile);
				UGraphicEps.copyEpsToFile(circleInterface, epsFile);
				return epsFile;
			}
		};

		return new DrawFile(lpng, UGraphicG2d.getSvgString(circleInterface), leps);

	}

	private DrawFile ensurePngCPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('C', radius, circledFont, stereotypeCBackground,
				classborder, Color.BLACK);
		return generateCircleCharacterFile(dir, cName, circledCharacter, classBackground);
	}

	private DrawFile ensurePngAPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('A', radius, circledFont, stereotypeABackground,
				classborder, Color.BLACK);
		return generateCircleCharacterFile(dir, aName, circledCharacter, classBackground);
	}

	private DrawFile ensurePngIPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('I', radius, circledFont, stereotypeIBackground,
				classborder, Color.BLACK);
		return generateCircleCharacterFile(dir, iName, circledCharacter, classBackground);
	}

	private DrawFile ensurePngEPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('E', radius, circledFont, stereotypeEBackground,
				classborder, Color.BLACK);
		return generateCircleCharacterFile(dir, eName, circledCharacter, classBackground);
	}

	private DrawFile generateCircleCharacterFile(File dir, String filename, final CircledCharacter circledCharacter,
			final Color yellow) throws IOException {
		final File png = new File(dir, filename);
		final File epsFile = new File(dir, filename.replaceFirst("\\.png", ".eps"));

		return generateCircleCharacter(png, epsFile, circledCharacter, yellow);
	}

	public DrawFile generateCircleCharacter(final File png, final File epsFile,
			final CircledCharacter circledCharacter, final Color yellow) throws IOException {

		final Lazy<File> lpng = new Lazy<File>() {

			public File getNow() throws IOException {
				Log.info("Creating temporary file: " + png);
				final EmptyImageBuilder builder = new EmptyImageBuilder(90, 90, yellow);
				BufferedImage im = builder.getBufferedImage();
				final Graphics2D g2d = builder.getGraphics2D();
				final StringBounder stringBounder = StringBounderUtils.asStringBounder(g2d);

				circledCharacter.draw(g2d, 0, 0);
				im = im.getSubimage(0, 0, (int) circledCharacter.getPreferredWidth(stringBounder) + 5,
						(int) circledCharacter.getPreferredHeight(stringBounder) + 1);

				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				Log.info("Creating temporary file: " + epsFile);
				UGraphicEps.copyEpsToFile(circledCharacter, epsFile);
				return epsFile;
			}
		};

		return new DrawFile(lpng, UGraphicG2d.getSvgString(circledCharacter), leps);
	}

	public final Map<EntityType, DrawFile> getStaticImages() {
		return Collections.unmodifiableMap(staticImages);
	}

	public final Map<VisibilityModifier, DrawFile> getVisibilityImages() {
		return Collections.unmodifiableMap(visibilityImages);
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

	private DrawFile ensureVisibilityModifierPresent(final VisibilityModifier modifier, final File dir, final int size)
			throws IOException {
		final UDrawable drawable = modifier.getUDrawable(size, foregroundColor.get(modifier),
				backgroundColor.get(modifier));

		final Lazy<File> lpng = new Lazy<File>() {
			public File getNow() throws IOException {
				final File png = new File(dir, modifier.name() + ".png");
				Log.info("Creating temporary file: " + png);
				final EmptyImageBuilder builder = new EmptyImageBuilder(size, size, classBackground);
				final BufferedImage im = builder.getBufferedImage();
				drawable.drawU(new UGraphicG2d(builder.getGraphics2D(), im));
				ImageIO.write(im, "png", png);
				return png;
			}
		};

		final Lazy<File> leps = new Lazy<File>() {
			public File getNow() throws IOException {
				final File eps = new File(dir, modifier.name() + ".eps");
				Log.info("Creating temporary file: " + eps);
				UGraphicEps.copyEpsToFile(drawable, eps);
				return eps;
			}
		};

		return new DrawFile(lpng, UGraphicG2d.getSvgString(drawable), leps);
	}

}
