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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.oregon;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemOregon extends AbstractPSystem {
	
	private Screen screen;

	public PSystemOregon(Keyboard keyboard) {
		final BasicGame game = new OregonBasicGame();
		try {
			game.run(keyboard);
			this.screen = game.getScreen();
//			this.screen = new Screen();
//			screen.print("Game ended??");
		} catch (NoInputException e) {
			this.screen = game.getScreen();
		}
	}

//	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException, InterruptedException {
//		OutputStream os = null;
//		try {
//			os = new FileOutputStream(suggestedFile);
//			getGraphicStrings().writeImage(os, fileFormat);
//		} finally {
//			if (os != null) {
//				os.close();
//			}
//		}
//		return Arrays.asList(suggestedFile);
//	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat) throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final Font font = new Font("Monospaced", Font.PLAIN, 14);
		return new GraphicStrings(screen.getLines(), font, Color.GREEN, Color.BLACK, true);
	}

	public String getDescription() {
		return "(The Oregon Trail)";
	}

}
