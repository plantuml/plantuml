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
package net.sourceforge.plantuml.klimt.sprite;

import java.io.IOException;
import java.io.OutputStream;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFactorySprite;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class StdlibDiagram extends UmlDiagram {
	// ::remove file when __CORE__

	private static final int WIDTH = 1800;
	private String name;

	public StdlibDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.HELP, previous, preprocessing);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Sprites)");
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).annotations(false);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				try {
					drawInternal(ug);
				} catch (IOException e) {
					Logme.error(e);
				}
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(WIDTH, 4096);
			}
		};
	}

	public void setStdlibName(String name) {
		this.name = name;
	}

	private void drawInternal(UGraphic ug) throws IOException {
		double x = 0;
		double y = 0;
		double rawHeight = 0;
		final Stdlib folder = Stdlib.retrieve(name);

		final CommandFactorySprite factorySpriteCommand = CommandFactorySprite.ME;

		Command<WithSprite> cmd = factorySpriteCommand.createMultiLine(false);

//		final List<String> all = folder.extractAllSprites();
//		int nb = 0;
//		for (String s : all) {
//			// System.err.println("s="+s);
//			final BlocLines bloc = BlocLines.fromArray(s.split("\n"));
//			try {
//				cmd.execute(this, bloc, ParserPass.ONE);
//			} catch (NoSuchColorException e) {
//				Logme.error(e);
//			}
////			System.err.println("nb=" + nb);
//			nb++;
//		}

		for (String n : getSkinParam().getAllSpriteNames()) {
			final Sprite sprite = getSkinParam().getSprite(n);
			TextBlock blockName = Display.create(n).create(FontConfiguration.blackBlueTrue(UFont.sansSerif(14)),
					HorizontalAlignment.LEFT, getSkinParam());
			TextBlock tb = sprite.asTextBlock(getBlack(), null, 1.0);
			tb = TextBlockUtils.mergeTB(tb, blockName, HorizontalAlignment.CENTER);
			tb.drawU(ug.apply(new UTranslate(x, y)));
			final XDimension2D dim = tb.calculateDimension(ug.getStringBounder());
			rawHeight = Math.max(rawHeight, dim.getHeight());
			x += dim.getWidth();
			x += 30;
			if (x > WIDTH) {
				x = 0;
				y += rawHeight + 50;
				rawHeight = 0;
				if (y > 1024) {
//					break;
				}
			}
		}
	}

	private HColor getBlack() {
		return HColors.BLACK.withDark(HColors.WHITE);
	}
}
