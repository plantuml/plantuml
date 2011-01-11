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
 * Revision $Revision: 5924 $
 *
 */
package net.sourceforge.plantuml.printskin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.SkinUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

class PrintSkin extends AbstractPSystem {

	private static final Font FONT1 = new Font("SansSerif", Font.PLAIN, 10);

	final private Skin skin;
	final private List<String> toPrint;
	private UGraphic ug;
	private float xpos = 10;
	private float ypos = 0;
	private float maxYpos = 0;

	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException, InterruptedException {
		final List<File> result = Arrays.asList(suggestedFile);
		final BufferedImage im = createImage();

		PngIO.write(im.getSubimage(0, 0, im.getWidth(), (int) maxYpos), suggestedFile, 96);
		return result;

	}

	public void createFile(OutputStream os, int index, FileFormatOption fileFormat) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im.getSubimage(0, 0, im.getWidth(), (int) maxYpos), os, 96);
	}

	private BufferedImage createImage() {
		final EmptyImageBuilder builder = new EmptyImageBuilder(1500, 830, Color.WHITE);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();
		
		ug = new UGraphicG2d(g2d, null, 1.0);

		for (ComponentType type : ComponentType.all() ) {
			printComponent(type);
			ypos += 10;
			maxYpos = Math.max(maxYpos, ypos);
			if (ypos > 620) {
				ypos = 0;
				xpos += 200;
			}
		}
		g2d.dispose();
		return im;
	}

	private void printComponent(ComponentType type) {
		println(type.name());
		final Component comp = skin.createComponent(type, new SkinParam(), toPrint);
		if (comp == null) {
			println("null");
			return;
		}
		double height = comp.getPreferredHeight(ug.getStringBounder());
		double width = comp.getPreferredWidth(ug.getStringBounder());
		println("height = " + String.format("%4.2f", height));
		println("width = " + width);

		if (height == 0) {
			height = 42;
		}
		if (width == 0) {
			width = 42;
		}
		ug.getParam().setColor(Color.LIGHT_GRAY);
		ug.getParam().setBackcolor(Color.LIGHT_GRAY);
		ug.draw(xpos-1, ypos-1, new URectangle(width+2, height+2));
		//g2d.drawRect((int) xpos - 1, (int) ypos - 1, (int) width + 2, (int) height + 2);

		//final AffineTransform at = g2d.getTransform();
		//g2d.translate(xpos, ypos);
		ug.translate(xpos, ypos);
		ug.getParam().reset();
		comp.drawU(ug, new Dimension2DDouble(width, height), new SimpleContext2D(false));
		ug.translate(-xpos, -ypos);
		//g2d.setTransform(at);

		ypos += height;
	}

	private void println(String s) {
		final TextBlock textBlock = TextBlockUtils.create(Arrays.asList(s), FONT1, Color.BLACK,
				HorizontalAlignement.LEFT);
		textBlock.drawU(ug, xpos, ypos);
		ypos += textBlock.calculateDimension(ug.getStringBounder()).getHeight();
	}

	public String getDescription() {
		return "Printing of " + skin.getClass().getName();
	}

	public PrintSkin(String className, List<String> toPrint) {
		this.skin = SkinUtils.loadSkin(className);
		this.toPrint = toPrint;
	}
}
