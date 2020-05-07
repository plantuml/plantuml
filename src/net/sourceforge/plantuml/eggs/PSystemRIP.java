/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.eggs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class PSystemRIP extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private final BufferedImage image;

	public PSystemRIP() throws IOException {
		strings.add(" To my Grandfather,");
		strings.add(" A mon grand-pere,");
		strings.add(" ");
		strings.add("          <b>Jean CANOUET");
		strings.add(" ");
		strings.add(" 31-OCT-1921 <i>(Neuilly-Sur-Seine, France)");
		strings.add(" 15-SEP-2009 <i>(Nanterre, France)");
		strings.add(" ");
		strings.add("         <b>Requiescat In Pace");
		strings.add(" ");

		final InputStream is = new ByteArrayInputStream(imm);
		image = ImageIO.read(is);
		is.close();
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final TextBlockBackcolored result = getGraphicStrings();
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(),
				false, null, getMetadata(), null, 1.0, result.getBackcolor());
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private TextBlockBackcolored getGraphicStrings() throws IOException {
		return GraphicStrings.createBlackOnWhite(strings, image, GraphicPosition.BOTTOM);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(RIP)");
	}

	private static final byte imm[] = new byte[] { (byte) 255, (byte) 216, (byte) 255, (byte) 224, (byte) 0, (byte) 16,
			(byte) 74, (byte) 70, (byte) 73, (byte) 70, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 88,
			(byte) 2, (byte) 88, (byte) 0, (byte) 0, (byte) 255, (byte) 219, (byte) 0, (byte) 67, (byte) 0, (byte) 13,
			(byte) 9, (byte) 10, (byte) 11, (byte) 10, (byte) 8, (byte) 13, (byte) 11, (byte) 10, (byte) 11, (byte) 14,
			(byte) 14, (byte) 13, (byte) 15, (byte) 19, (byte) 32, (byte) 21, (byte) 19, (byte) 18, (byte) 18,
			(byte) 19, (byte) 39, (byte) 28, (byte) 30, (byte) 23, (byte) 32, (byte) 46, (byte) 41, (byte) 49,
			(byte) 48, (byte) 46, (byte) 41, (byte) 45, (byte) 44, (byte) 51, (byte) 58, (byte) 74, (byte) 62,
			(byte) 51, (byte) 54, (byte) 70, (byte) 55, (byte) 44, (byte) 45, (byte) 64, (byte) 87, (byte) 65,
			(byte) 70, (byte) 76, (byte) 78, (byte) 82, (byte) 83, (byte) 82, (byte) 50, (byte) 62, (byte) 90,
			(byte) 97, (byte) 90, (byte) 80, (byte) 96, (byte) 74, (byte) 81, (byte) 82, (byte) 79, (byte) 255,
			(byte) 219, (byte) 0, (byte) 67, (byte) 1, (byte) 14, (byte) 14, (byte) 14, (byte) 19, (byte) 17,
			(byte) 19, (byte) 38, (byte) 21, (byte) 21, (byte) 38, (byte) 79, (byte) 53, (byte) 45, (byte) 53,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79,
			(byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 79, (byte) 255, (byte) 192, (byte) 0, (byte) 17,
			(byte) 8, (byte) 0, (byte) 135, (byte) 0, (byte) 162, (byte) 3, (byte) 1, (byte) 34, (byte) 0, (byte) 2,
			(byte) 17, (byte) 1, (byte) 3, (byte) 17, (byte) 1, (byte) 255, (byte) 196, (byte) 0, (byte) 31, (byte) 0,
			(byte) 0, (byte) 1, (byte) 5, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 0,
			(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 2, (byte) 3,
			(byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 255, (byte) 196,
			(byte) 0, (byte) 181, (byte) 16, (byte) 0, (byte) 2, (byte) 1, (byte) 3, (byte) 3, (byte) 2, (byte) 4,
			(byte) 3, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 0, (byte) 0, (byte) 1, (byte) 125, (byte) 1,
			(byte) 2, (byte) 3, (byte) 0, (byte) 4, (byte) 17, (byte) 5, (byte) 18, (byte) 33, (byte) 49, (byte) 65,
			(byte) 6, (byte) 19, (byte) 81, (byte) 97, (byte) 7, (byte) 34, (byte) 113, (byte) 20, (byte) 50,
			(byte) 129, (byte) 145, (byte) 161, (byte) 8, (byte) 35, (byte) 66, (byte) 177, (byte) 193, (byte) 21,
			(byte) 82, (byte) 209, (byte) 240, (byte) 36, (byte) 51, (byte) 98, (byte) 114, (byte) 130, (byte) 9,
			(byte) 10, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) 26, (byte) 37, (byte) 38, (byte) 39,
			(byte) 40, (byte) 41, (byte) 42, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57,
			(byte) 58, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74,
			(byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 99,
			(byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 115, (byte) 116,
			(byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 131, (byte) 132, (byte) 133,
			(byte) 134, (byte) 135, (byte) 136, (byte) 137, (byte) 138, (byte) 146, (byte) 147, (byte) 148, (byte) 149,
			(byte) 150, (byte) 151, (byte) 152, (byte) 153, (byte) 154, (byte) 162, (byte) 163, (byte) 164, (byte) 165,
			(byte) 166, (byte) 167, (byte) 168, (byte) 169, (byte) 170, (byte) 178, (byte) 179, (byte) 180, (byte) 181,
			(byte) 182, (byte) 183, (byte) 184, (byte) 185, (byte) 186, (byte) 194, (byte) 195, (byte) 196, (byte) 197,
			(byte) 198, (byte) 199, (byte) 200, (byte) 201, (byte) 202, (byte) 210, (byte) 211, (byte) 212, (byte) 213,
			(byte) 214, (byte) 215, (byte) 216, (byte) 217, (byte) 218, (byte) 225, (byte) 226, (byte) 227, (byte) 228,
			(byte) 229, (byte) 230, (byte) 231, (byte) 232, (byte) 233, (byte) 234, (byte) 241, (byte) 242, (byte) 243,
			(byte) 244, (byte) 245, (byte) 246, (byte) 247, (byte) 248, (byte) 249, (byte) 250, (byte) 255, (byte) 196,
			(byte) 0, (byte) 31, (byte) 1, (byte) 0, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1,
			(byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
			(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10,
			(byte) 11, (byte) 255, (byte) 196, (byte) 0, (byte) 181, (byte) 17, (byte) 0, (byte) 2, (byte) 1, (byte) 2,
			(byte) 4, (byte) 4, (byte) 3, (byte) 4, (byte) 7, (byte) 5, (byte) 4, (byte) 4, (byte) 0, (byte) 1,
			(byte) 2, (byte) 119, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 17, (byte) 4, (byte) 5, (byte) 33,
			(byte) 49, (byte) 6, (byte) 18, (byte) 65, (byte) 81, (byte) 7, (byte) 97, (byte) 113, (byte) 19,
			(byte) 34, (byte) 50, (byte) 129, (byte) 8, (byte) 20, (byte) 66, (byte) 145, (byte) 161, (byte) 177,
			(byte) 193, (byte) 9, (byte) 35, (byte) 51, (byte) 82, (byte) 240, (byte) 21, (byte) 98, (byte) 114,
			(byte) 209, (byte) 10, (byte) 22, (byte) 36, (byte) 52, (byte) 225, (byte) 37, (byte) 241, (byte) 23,
			(byte) 24, (byte) 25, (byte) 26, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 53,
			(byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 58, (byte) 67, (byte) 68, (byte) 69, (byte) 70,
			(byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87,
			(byte) 88, (byte) 89, (byte) 90, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104,
			(byte) 105, (byte) 106, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121,
			(byte) 122, (byte) 130, (byte) 131, (byte) 132, (byte) 133, (byte) 134, (byte) 135, (byte) 136, (byte) 137,
			(byte) 138, (byte) 146, (byte) 147, (byte) 148, (byte) 149, (byte) 150, (byte) 151, (byte) 152, (byte) 153,
			(byte) 154, (byte) 162, (byte) 163, (byte) 164, (byte) 165, (byte) 166, (byte) 167, (byte) 168, (byte) 169,
			(byte) 170, (byte) 178, (byte) 179, (byte) 180, (byte) 181, (byte) 182, (byte) 183, (byte) 184, (byte) 185,
			(byte) 186, (byte) 194, (byte) 195, (byte) 196, (byte) 197, (byte) 198, (byte) 199, (byte) 200, (byte) 201,
			(byte) 202, (byte) 210, (byte) 211, (byte) 212, (byte) 213, (byte) 214, (byte) 215, (byte) 216, (byte) 217,
			(byte) 218, (byte) 226, (byte) 227, (byte) 228, (byte) 229, (byte) 230, (byte) 231, (byte) 232, (byte) 233,
			(byte) 234, (byte) 242, (byte) 243, (byte) 244, (byte) 245, (byte) 246, (byte) 247, (byte) 248, (byte) 249,
			(byte) 250, (byte) 255, (byte) 218, (byte) 0, (byte) 12, (byte) 3, (byte) 1, (byte) 0, (byte) 2, (byte) 17,
			(byte) 3, (byte) 17, (byte) 0, (byte) 63, (byte) 0, (byte) 180, (byte) 48, (byte) 6, (byte) 69, (byte) 84,
			(byte) 212, (byte) 135, (byte) 250, (byte) 49, (byte) 235, (byte) 214, (byte) 174, (byte) 116, (byte) 246,
			(byte) 170, (byte) 154, (byte) 144, (byte) 205, (byte) 153, (byte) 61, (byte) 121, (byte) 21, (byte) 200,
			(byte) 246, (byte) 61, (byte) 8, (byte) 124, (byte) 72, (byte) 202, (byte) 94, (byte) 7, (byte) 29,
			(byte) 233, (byte) 24, (byte) 100, (byte) 127, (byte) 58, (byte) 114, (byte) 99, (byte) 181, (byte) 4,
			(byte) 113, (byte) 222, (byte) 176, (byte) 59, (byte) 8, (byte) 226, (byte) 228, (byte) 103, (byte) 29,
			(byte) 41, (byte) 177, (byte) 227, (byte) 127, (byte) 20, (byte) 228, (byte) 198, (byte) 211, (byte) 206,
			(byte) 41, (byte) 163, (byte) 137, (byte) 61, (byte) 168, (byte) 1, (byte) 95, (byte) 168, (byte) 232,
			(byte) 42, (byte) 212, (byte) 24, (byte) 242, (byte) 151, (byte) 57, (byte) 224, (byte) 246, (byte) 170,
			(byte) 210, (byte) 18, (byte) 112, (byte) 42, (byte) 205, (byte) 191, (byte) 250, (byte) 159, (byte) 188,
			(byte) 122, (byte) 211, (byte) 20, (byte) 182, (byte) 36, (byte) 7, (byte) 23, (byte) 72, (byte) 115,
			(byte) 220, (byte) 113, (byte) 90, (byte) 135, (byte) 238, (byte) 140, (byte) 117, (byte) 205, (byte) 100,
			(byte) 183, (byte) 19, (byte) 171, (byte) 99, (byte) 161, (byte) 28, (byte) 85, (byte) 249, (byte) 174,
			(byte) 226, (byte) 133, (byte) 112, (byte) 207, (byte) 207, (byte) 160, (byte) 173, (byte) 32, (byte) 99,
			(byte) 81, (byte) 55, (byte) 107, (byte) 22, (byte) 20, (byte) 117, (byte) 233, (byte) 214, (byte) 148,
			(byte) 116, (byte) 108, (byte) 250, (byte) 214, (byte) 83, (byte) 106, (byte) 172, (byte) 14, (byte) 35,
			(byte) 143, (byte) 143, (byte) 122, (byte) 106, (byte) 106, (byte) 115, (byte) 110, (byte) 249, (byte) 163,
			(byte) 4, (byte) 103, (byte) 165, (byte) 105, (byte) 116, (byte) 79, (byte) 179, (byte) 145, (byte) 179,
			(byte) 143, (byte) 148, (byte) 99, (byte) 138, (byte) 204, (byte) 214, (byte) 135, (byte) 203, (byte) 23,
			(byte) 63, (byte) 197, (byte) 82, (byte) 195, (byte) 168, (byte) 71, (byte) 39, (byte) 13, (byte) 149,
			(byte) 57, (byte) 232, (byte) 106, (byte) 13, (byte) 93, (byte) 131, (byte) 197, (byte) 27, (byte) 43,
			(byte) 103, (byte) 230, (byte) 235, (byte) 69, (byte) 197, (byte) 20, (byte) 212, (byte) 181, (byte) 28,
			(byte) 169, (byte) 38, (byte) 6, (byte) 8, (byte) 198, (byte) 61, (byte) 41, (byte) 205, (byte) 20,
			(byte) 196, (byte) 13, (byte) 178, (byte) 1, (byte) 248, (byte) 81, (byte) 24, (byte) 118, (byte) 81,
			(byte) 243, (byte) 246, (byte) 167, (byte) 180, (byte) 76, (byte) 64, (byte) 196, (byte) 173, (byte) 159,
			(byte) 76, (byte) 212, (byte) 131, (byte) 96, (byte) 177, (byte) 200, (byte) 62, (byte) 83, (byte) 43,
			(byte) 123, (byte) 226, (byte) 164, (byte) 17, (byte) 156, (byte) 99, (byte) 123, (byte) 126, (byte) 117,
			(byte) 24, (byte) 140, (byte) 175, (byte) 222, (byte) 118, (byte) 231, (byte) 190, (byte) 105, (byte) 193,
			(byte) 71, (byte) 77, (byte) 199, (byte) 241, (byte) 52, (byte) 196, (byte) 72, (byte) 34, (byte) 83,
			(byte) 213, (byte) 155, (byte) 39, (byte) 142, (byte) 180, (byte) 162, (byte) 36, (byte) 201, (byte) 201,
			(byte) 63, (byte) 157, (byte) 51, (byte) 9, (byte) 158, (byte) 88, (byte) 115, (byte) 239, (byte) 78,
			(byte) 81, (byte) 24, (byte) 39, (byte) 230, (byte) 20, (byte) 132, (byte) 39, (byte) 151, (byte) 31,
			(byte) 181, (byte) 20, (byte) 252, (byte) 197, (byte) 234, (byte) 40, (byte) 160, (byte) 46, (byte) 76,
			(byte) 7, (byte) 168, (byte) 170, (byte) 218, (byte) 128, (byte) 255, (byte) 0, (byte) 67, (byte) 127,
			(byte) 194, (byte) 174, (byte) 1, (byte) 199, (byte) 61, (byte) 42, (byte) 190, (byte) 160, (byte) 191,
			(byte) 232, (byte) 114, (byte) 96, (byte) 83, (byte) 123, (byte) 10, (byte) 59, (byte) 163, (byte) 13,
			(byte) 6, (byte) 6, (byte) 41, (byte) 89, (byte) 72, (byte) 247, (byte) 52, (byte) 168, (byte) 164,
			(byte) 119, (byte) 193, (byte) 167, (byte) 50, (byte) 156, (byte) 26, (byte) 231, (byte) 108, (byte) 236,
			(byte) 34, (byte) 140, (byte) 114, (byte) 221, (byte) 51, (byte) 81, (byte) 168, (byte) 30, (byte) 103,
			(byte) 34, (byte) 165, (byte) 139, (byte) 134, (byte) 61, (byte) 233, (byte) 164, (byte) 226, (byte) 83,
			(byte) 199, (byte) 90, (byte) 10, (byte) 65, (byte) 42, (byte) 227, (byte) 233, (byte) 86, (byte) 109,
			(byte) 128, (byte) 48, (byte) 231, (byte) 112, (byte) 94, (byte) 123, (byte) 213, (byte) 121, (byte) 58,
			(byte) 116, (byte) 233, (byte) 79, (byte) 50, (byte) 8, (byte) 236, (byte) 152, (byte) 224, (byte) 18,
			(byte) 125, (byte) 104, (byte) 90, (byte) 147, (byte) 45, (byte) 136, (byte) 238, (byte) 231, (byte) 253,
			(byte) 246, (byte) 200, (byte) 136, (byte) 220, (byte) 58, (byte) 181, (byte) 49, (byte) 85, (byte) 187,
			(byte) 245, (byte) 61, (byte) 205, (byte) 71, (byte) 2, (byte) 28, (byte) 100, (byte) 247, (byte) 171,
			(byte) 208, (byte) 71, (byte) 131, (byte) 156, (byte) 113, (byte) 90, (byte) 109, (byte) 161, (byte) 73,
			(byte) 13, (byte) 138, (byte) 14, (byte) 121, (byte) 228, (byte) 85, (byte) 143, (byte) 32, (byte) 40,
			(byte) 199, (byte) 74, (byte) 181, (byte) 111, (byte) 16, (byte) 61, (byte) 112, (byte) 125, (byte) 51,
			(byte) 83, (byte) 203, (byte) 26, (byte) 4, (byte) 31, (byte) 39, (byte) 228, (byte) 105, (byte) 1,
			(byte) 143, (byte) 44, (byte) 56, (byte) 233, (byte) 154, (byte) 169, (byte) 59, (byte) 72, (byte) 19,
			(byte) 110, (byte) 114, (byte) 1, (byte) 200, (byte) 21, (byte) 181, (byte) 34, (byte) 140, (byte) 30,
			(byte) 0, (byte) 250, (byte) 86, (byte) 124, (byte) 241, (byte) 130, (byte) 50, (byte) 71, (byte) 229,
			(byte) 66, (byte) 97, (byte) 107, (byte) 133, (byte) 165, (byte) 202, (byte) 200, (byte) 161, (byte) 93,
			(byte) 176, (byte) 122, (byte) 114, (byte) 106, (byte) 198, (byte) 3, (byte) 30, (byte) 95, (byte) 233,
			(byte) 205, (byte) 99, (byte) 74, (byte) 165, (byte) 36, (byte) 201, (byte) 30, (byte) 226, (byte) 181,
			(byte) 45, (byte) 36, (byte) 134, (byte) 120, (byte) 182, (byte) 202, (byte) 16, (byte) 17, (byte) 222,
			(byte) 180, (byte) 49, (byte) 156, (byte) 109, (byte) 169, (byte) 58, (byte) 136, (byte) 192, (byte) 193,
			(byte) 97, (byte) 249, (byte) 212, (byte) 170, (byte) 177, (byte) 131, (byte) 156, (byte) 138, (byte) 114,
			(byte) 8, (byte) 66, (byte) 241, (byte) 183, (byte) 138, (byte) 148, (byte) 24, (byte) 123, (byte) 117,
			(byte) 250, (byte) 80, (byte) 101, (byte) 113, (byte) 170, (byte) 98, (byte) 235, (byte) 149, (byte) 247,
			(byte) 226, (byte) 156, (byte) 101, (byte) 128, (byte) 1, (byte) 128, (byte) 51, (byte) 158, (byte) 160,
			(byte) 82, (byte) 230, (byte) 48, (byte) 49, (byte) 131, (byte) 207, (byte) 160, (byte) 163, (byte) 204,
			(byte) 78, (byte) 56, (byte) 63, (byte) 247, (byte) 205, (byte) 50, (byte) 70, (byte) 249, (byte) 145,
			(byte) 255, (byte) 0, (byte) 181, (byte) 255, (byte) 0, (byte) 124, (byte) 209, (byte) 78, (byte) 243,
			(byte) 71, (byte) 247, (byte) 91, (byte) 242, (byte) 162, (byte) 150, (byte) 131, (byte) 47, (byte) 99,
			(byte) 143, (byte) 173, (byte) 87, (byte) 191, (byte) 81, (byte) 246, (byte) 73, (byte) 51, (byte) 233,
			(byte) 87, (byte) 48, (byte) 14, (byte) 122, (byte) 213, (byte) 123, (byte) 209, (byte) 254, (byte) 137,
			(byte) 39, (byte) 166, (byte) 41, (byte) 189, (byte) 136, (byte) 142, (byte) 232, (byte) 231, (byte) 208,
			(byte) 102, (byte) 158, (byte) 220, (byte) 28, (byte) 10, (byte) 106, (byte) 227, (byte) 138, (byte) 151,
			(byte) 4, (byte) 142, (byte) 245, (byte) 204, (byte) 118, (byte) 144, (byte) 32, (byte) 204, (byte) 135,
			(byte) 53, (byte) 27, (byte) 224, (byte) 75, (byte) 199, (byte) 173, (byte) 74, (byte) 56, (byte) 147,
			(byte) 6, (byte) 152, (byte) 227, (byte) 247, (byte) 216, (byte) 52, (byte) 13, (byte) 4, (byte) 131,
			(byte) 229, (byte) 206, (byte) 42, (byte) 180, (byte) 161, (byte) 152, (byte) 172, (byte) 125, (byte) 135,
			(byte) 38, (byte) 174, (byte) 74, (byte) 0, (byte) 66, (byte) 7, (byte) 165, (byte) 64, (byte) 235,
			(byte) 209, (byte) 179, (byte) 205, (byte) 84, (byte) 55, (byte) 6, (byte) 75, (byte) 12, (byte) 125,
			(byte) 1, (byte) 206, (byte) 49, (byte) 82, (byte) 77, (byte) 113, (byte) 228, (byte) 97, (byte) 81,
			(byte) 55, (byte) 53, (byte) 62, (byte) 220, (byte) 101, (byte) 50, (byte) 8, (byte) 233, (byte) 82,
			(byte) 89, (byte) 36, (byte) 127, (byte) 104, (byte) 221, (byte) 112, (byte) 192, (byte) 51, (byte) 30,
			(byte) 51, (byte) 218, (byte) 173, (byte) 110, (byte) 13, (byte) 216, (byte) 91, (byte) 75, (byte) 240,
			(byte) 236, (byte) 18, (byte) 96, (byte) 87, (byte) 60, (byte) 100, (byte) 138, (byte) 213, (byte) 16,
			(byte) 130, (byte) 157, (byte) 141, (byte) 23, (byte) 246, (byte) 118, (byte) 182, (byte) 234, (byte) 140,
			(byte) 204, (byte) 24, (byte) 48, (byte) 4, (byte) 48, (byte) 82, (byte) 42, (byte) 72, (byte) 118,
			(byte) 199, (byte) 18, (byte) 146, (byte) 202, (byte) 85, (byte) 135, (byte) 20, (byte) 89, (byte) 18,
			(byte) 165, (byte) 116, (byte) 103, (byte) 94, (byte) 98, (byte) 221, (byte) 119, (byte) 177, (byte) 206,
			(byte) 122, (byte) 10, (byte) 200, (byte) 123, (byte) 208, (byte) 220, (byte) 52, (byte) 100, (byte) 15,
			(byte) 90, (byte) 222, (byte) 146, (byte) 15, (byte) 58, (byte) 38, (byte) 157, (byte) 212, (byte) 178,
			(byte) 47, (byte) 124, (byte) 116, (byte) 172, (byte) 233, (byte) 252, (byte) 150, (byte) 66, (byte) 172,
			(byte) 128, (byte) 46, (byte) 113, (byte) 144, (byte) 59, (byte) 209, (byte) 161, (byte) 92, (byte) 198,
			(byte) 93, (byte) 202, (byte) 228, (byte) 43, (byte) 1, (byte) 197, (byte) 45, (byte) 140, (byte) 209,
			(byte) 37, (byte) 198, (byte) 37, (byte) 25, (byte) 83, (byte) 198, (byte) 49, (byte) 82, (byte) 204,
			(byte) 134, (byte) 37, (byte) 10, (byte) 72, (byte) 62, (byte) 135, (byte) 218, (byte) 170, (byte) 194,
			(byte) 234, (byte) 147, (byte) 169, (byte) 35, (byte) 60, (byte) 250, (byte) 85, (byte) 33, (byte) 61,
			(byte) 81, (byte) 209, (byte) 164, (byte) 208, (byte) 237, (byte) 218, (byte) 128, (byte) 227, (byte) 217,
			(byte) 77, (byte) 60, (byte) 72, (byte) 167, (byte) 162, (byte) 177, (byte) 255, (byte) 0, (byte) 128,
			(byte) 154, (byte) 72, (byte) 231, (byte) 27, (byte) 70, (byte) 17, (byte) 200, (byte) 199, (byte) 101,
			(byte) 169, (byte) 145, (byte) 201, (byte) 255, (byte) 0, (byte) 150, (byte) 111, (byte) 84, (byte) 114,
			(byte) 49, (byte) 155, (byte) 207, (byte) 64, (byte) 173, (byte) 255, (byte) 0, (byte) 124, (byte) 208,
			(byte) 95, (byte) 166, (byte) 99, (byte) 111, (byte) 202, (byte) 165, (byte) 220, (byte) 199, (byte) 164,
			(byte) 109, (byte) 75, (byte) 185, (byte) 207, (byte) 30, (byte) 89, (byte) 252, (byte) 233, (byte) 8,
			(byte) 103, (byte) 154, (byte) 223, (byte) 243, (byte) 204, (byte) 254, (byte) 84, (byte) 83, (byte) 240,
			(byte) 255, (byte) 0, (byte) 243, (byte) 207, (byte) 245, (byte) 162, (byte) 139, (byte) 129, (byte) 108,
			(byte) 142, (byte) 106, (byte) 43, (byte) 197, (byte) 205, (byte) 164, (byte) 131, (byte) 167, (byte) 202,
			(byte) 106, (byte) 206, (byte) 61, (byte) 122, (byte) 212, (byte) 87, (byte) 35, (byte) 54, (byte) 210,
			(byte) 103, (byte) 251, (byte) 166, (byte) 155, (byte) 216, (byte) 148, (byte) 245, (byte) 57, (byte) 164,
			(byte) 83, (byte) 145, (byte) 215, (byte) 138, (byte) 148, (byte) 1, (byte) 154, (byte) 106, (byte) 14,
			(byte) 112, (byte) 106, (byte) 80, (byte) 56, (byte) 237, (byte) 154, (byte) 229, (byte) 103, (byte) 109,
			(byte) 202, (byte) 216, (byte) 253, (byte) 233, (byte) 166, (byte) 203, (byte) 254, (byte) 180, (byte) 26,
			(byte) 148, (byte) 2, (byte) 37, (byte) 250, (byte) 211, (byte) 102, (byte) 95, (byte) 222, (byte) 140,
			(byte) 154, (byte) 16, (byte) 192, (byte) 168, (byte) 96, (byte) 42, (byte) 213, (byte) 212, (byte) 17,
			(byte) 164, (byte) 33, (byte) 112, (byte) 8, (byte) 198, (byte) 106, (byte) 187, (byte) 175, (byte) 203,
			(byte) 212, (byte) 226, (byte) 172, (byte) 135, (byte) 13, (byte) 102, (byte) 197, (byte) 190, (byte) 241,
			(byte) 92, (byte) 125, (byte) 106, (byte) 224, (byte) 50, (byte) 188, (byte) 16, (byte) 70, (byte) 223,
			(byte) 42, (byte) 147, (byte) 130, (byte) 123, (byte) 28, (byte) 84, (byte) 255, (byte) 0, (byte) 101,
			(byte) 119, (byte) 159, (byte) 129, (byte) 140, (byte) 112, (byte) 42, (byte) 27, (byte) 77, (byte) 194,
			(byte) 69, (byte) 29, (byte) 171, (byte) 106, (byte) 37, (byte) 87, (byte) 56, (byte) 12, (byte) 1,
			(byte) 199, (byte) 122, (byte) 173, (byte) 68, (byte) 244, (byte) 27, (byte) 112, (byte) 215, (byte) 18,
			(byte) 90, (byte) 195, (byte) 28, (byte) 164, (byte) 58, (byte) 196, (byte) 48, (byte) 6, (byte) 59,
			(byte) 85, (byte) 41, (byte) 110, (byte) 30, (byte) 17, (byte) 177, (byte) 85, (byte) 126, (byte) 113,
			(byte) 223, (byte) 156, (byte) 86, (byte) 156, (byte) 144, (byte) 21, (byte) 66, (byte) 210, (byte) 55,
			(byte) 3, (byte) 144, (byte) 23, (byte) 189, (byte) 101, (byte) 198, (byte) 130, (byte) 123, (byte) 163,
			(byte) 185, (byte) 130, (byte) 250, (byte) 3, (byte) 218, (byte) 158, (byte) 251, (byte) 132, (byte) 82,
			(byte) 38, (byte) 180, (byte) 184, (byte) 157, (byte) 108, (byte) 165, (byte) 137, (byte) 27, (byte) 247,
			(byte) 114, (byte) 12, (byte) 50, (byte) 213, (byte) 9, (byte) 163, (byte) 85, (byte) 93, (byte) 170,
			(byte) 8, (byte) 238, (byte) 125, (byte) 234, (byte) 218, (byte) 68, (byte) 233, (byte) 118, (byte) 99,
			(byte) 71, (byte) 24, (byte) 60, (byte) 143, (byte) 66, (byte) 105, (byte) 243, (byte) 71, (byte) 34,
			(byte) 130, (byte) 90, (byte) 62, (byte) 71, (byte) 189, (byte) 14, (byte) 227, (byte) 178, (byte) 76,
			(byte) 206, (byte) 146, (byte) 47, (byte) 58, (byte) 84, (byte) 80, (byte) 6, (byte) 91, (byte) 214,
			(byte) 179, (byte) 221, (byte) 190, (byte) 207, (byte) 119, (byte) 141, (byte) 155, (byte) 130, (byte) 55,
			(byte) 74, (byte) 189, (byte) 44, (byte) 158, (byte) 84, (byte) 193, (byte) 240, (byte) 1, (byte) 0,
			(byte) 145, (byte) 143, (byte) 90, (byte) 207, (byte) 11, (byte) 52, (byte) 243, (byte) 240, (byte) 187,
			(byte) 153, (byte) 143, (byte) 65, (byte) 84, (byte) 132, (byte) 246, (byte) 58, (byte) 107, (byte) 121,
			(byte) 76, (byte) 145, (byte) 171, (byte) 136, (byte) 142, (byte) 8, (byte) 233, (byte) 154, (byte) 156,
			(byte) 23, (byte) 255, (byte) 0, (byte) 158, (byte) 120, (byte) 252, (byte) 106, (byte) 59, (byte) 88,
			(byte) 231, (byte) 72, (byte) 85, (byte) 10, (byte) 32, (byte) 192, (byte) 171, (byte) 24, (byte) 151,
			(byte) 31, (byte) 193, (byte) 154, (byte) 163, (byte) 141, (byte) 140, (byte) 5, (byte) 241, (byte) 247,
			(byte) 7, (byte) 231, (byte) 74, (byte) 60, (byte) 206, (byte) 161, (byte) 71, (byte) 231, (byte) 78,
			(byte) 219, (byte) 47, (byte) 251, (byte) 20, (byte) 170, (byte) 178, (byte) 231, (byte) 239, (byte) 47,
			(byte) 229, (byte) 64, (byte) 134, (byte) 252, (byte) 222, (byte) 139, (byte) 249, (byte) 209, (byte) 82,
			(byte) 121, (byte) 114, (byte) 255, (byte) 0, (byte) 121, (byte) 104, (byte) 164, (byte) 23, (byte) 44,
			(byte) 149, (byte) 39, (byte) 165, (byte) 71, (byte) 58, (byte) 159, (byte) 33, (byte) 198, (byte) 63,
			(byte) 132, (byte) 212, (byte) 248, (byte) 7, (byte) 138, (byte) 108, (byte) 171, (byte) 251, (byte) 166,
			(byte) 250, (byte) 98, (byte) 171, (byte) 161, (byte) 11, (byte) 115, (byte) 150, (byte) 140, (byte) 124,
			(byte) 220, (byte) 84, (byte) 219, (byte) 121, (byte) 166, (byte) 42, (byte) 146, (byte) 216, (byte) 29,
			(byte) 141, (byte) 78, (byte) 163, (byte) 154, (byte) 228, (byte) 123, (byte) 157, (byte) 197, (byte) 82,
			(byte) 15, (byte) 152, (byte) 41, (byte) 179, (byte) 175, (byte) 206, (byte) 9, (byte) 169, (byte) 153,
			(byte) 79, (byte) 154, (byte) 41, (byte) 147, (byte) 174, (byte) 25, (byte) 73, (byte) 206, (byte) 40,
			(byte) 24, (byte) 99, (byte) 9, (byte) 142, (byte) 105, (byte) 208, (byte) 32, (byte) 101, (byte) 37,
			(byte) 219, (byte) 104, (byte) 3, (byte) 34, (byte) 159, (byte) 183, (byte) 40, (byte) 112, (byte) 57,
			(byte) 237, (byte) 87, (byte) 108, (byte) 236, (byte) 100, (byte) 40, (byte) 124, (byte) 193, (byte) 141,
			(byte) 195, (byte) 165, (byte) 105, (byte) 74, (byte) 46, (byte) 79, (byte) 66, (byte) 39, (byte) 53,
			(byte) 21, (byte) 118, (byte) 80, (byte) 183, (byte) 31, (byte) 48, (byte) 30, (byte) 245, (byte) 173,
			(byte) 110, (byte) 152, (byte) 249, (byte) 143, (byte) 65, (byte) 84, (byte) 163, (byte) 128, (byte) 195,
			(byte) 115, (byte) 176, (byte) 245, (byte) 7, (byte) 154, (byte) 211, (byte) 141, (byte) 51, (byte) 149,
			(byte) 238, (byte) 106, (byte) 154, (byte) 177, (byte) 92, (byte) 221, (byte) 72, (byte) 46, (byte) 238,
			(byte) 55, (byte) 28, (byte) 100, (byte) 243, (byte) 85, (byte) 226, (byte) 183, (byte) 93, (byte) 187,
			(byte) 152, (byte) 114, (byte) 122, (byte) 123, (byte) 83, (byte) 117, (byte) 8, (byte) 38, (byte) 102,
			(byte) 45, (byte) 19, (byte) 99, (byte) 29, (byte) 133, (byte) 85, (byte) 84, (byte) 185, (byte) 85,
			(byte) 206, (byte) 72, (byte) 230, (byte) 132, (byte) 104, (byte) 147, (byte) 104, (byte) 89, (byte) 3,
			(byte) 67, (byte) 117, (byte) 149, (byte) 99, (byte) 128, (byte) 106, (byte) 244, (byte) 178, (byte) 9,
			(byte) 34, (byte) 221, (byte) 237, (byte) 89, (byte) 66, (byte) 41, (byte) 158, (byte) 113, (byte) 184,
			(byte) 182, (byte) 220, (byte) 242, (byte) 107, (byte) 96, (byte) 162, (byte) 139, (byte) 124, (byte) 142,
			(byte) 167, (byte) 138, (byte) 24, (byte) 164, (byte) 236, (byte) 97, (byte) 94, (byte) 129, (byte) 146,
			(byte) 9, (byte) 193, (byte) 199, (byte) 21, (byte) 14, (byte) 155, (byte) 230, (byte) 11, (byte) 164,
			(byte) 219, (byte) 183, (byte) 35, (byte) 212, (byte) 113, (byte) 79, (byte) 212, (byte) 207, (byte) 250,
			(byte) 70, (byte) 220, (byte) 227, (byte) 2, (byte) 173, (byte) 104, (byte) 214, (byte) 141, (byte) 33,
			(byte) 105, (byte) 3, (byte) 225, (byte) 151, (byte) 167, (byte) 28, (byte) 85, (byte) 35, (byte) 57,
			(byte) 187, (byte) 35, (byte) 106, (byte) 53, (byte) 159, (byte) 3, (byte) 123, (byte) 174, (byte) 125,
			(byte) 133, (byte) 60, (byte) 71, (byte) 38, (byte) 57, (byte) 113, (byte) 159, (byte) 165, (byte) 34,
			(byte) 197, (byte) 40, (byte) 31, (byte) 52, (byte) 205, (byte) 159, (byte) 165, (byte) 56, (byte) 198,
			(byte) 248, (byte) 229, (byte) 216, (byte) 211, (byte) 57, (byte) 67, (byte) 99, (byte) 224, (byte) 130,
			(byte) 252, (byte) 253, (byte) 41, (byte) 66, (byte) 201, (byte) 221, (byte) 248, (byte) 250, (byte) 81,
			(byte) 229, (byte) 147, (byte) 140, (byte) 187, (byte) 82, (byte) 136, (byte) 207, (byte) 247, (byte) 219,
			(byte) 31, (byte) 90, (byte) 96, (byte) 46, (byte) 195, (byte) 255, (byte) 0, (byte) 61, (byte) 13,
			(byte) 20, (byte) 121, (byte) 63, (byte) 237, (byte) 55, (byte) 231, (byte) 69, (byte) 23, (byte) 21,
			(byte) 139, (byte) 152, (byte) 201, (byte) 247, (byte) 164, (byte) 117, (byte) 249, (byte) 79, (byte) 166,
			(byte) 41, (byte) 195, (byte) 20, (byte) 132, (byte) 124, (byte) 164, (byte) 85, (byte) 16, (byte) 115,
			(byte) 32, (byte) 126, (byte) 240, (byte) 129, (byte) 235, (byte) 82, (byte) 117, (byte) 224, (byte) 83,
			(byte) 74, (byte) 226, (byte) 118, (byte) 235, (byte) 212, (byte) 212, (byte) 161, (byte) 79, (byte) 28,
			(byte) 243, (byte) 92, (byte) 143, (byte) 115, (byte) 182, (byte) 229, (byte) 121, (byte) 0, (byte) 14,
			(byte) 50, (byte) 41, (byte) 207, (byte) 3, (byte) 202, (byte) 192, (byte) 39, (byte) 62, (byte) 167,
			(byte) 210, (byte) 174, (byte) 195, (byte) 100, (byte) 101, (byte) 96, (byte) 207, (byte) 144, (byte) 61,
			(byte) 43, (byte) 78, (byte) 27, (byte) 116, (byte) 81, (byte) 133, (byte) 0, (byte) 98, (byte) 183,
			(byte) 167, (byte) 69, (byte) 189, (byte) 89, (byte) 140, (byte) 235, (byte) 168, (byte) 236, (byte) 83,
			(byte) 176, (byte) 177, (byte) 0, (byte) 130, (byte) 252, (byte) 227, (byte) 214, (byte) 180, (byte) 210,
			(byte) 60, (byte) 47, (byte) 35, (byte) 156, (byte) 212, (byte) 144, (byte) 198, (byte) 20, (byte) 142,
			(byte) 42, (byte) 114, (byte) 163, (byte) 56, (byte) 29, (byte) 235, (byte) 174, (byte) 49, (byte) 81,
			(byte) 86, (byte) 71, (byte) 28, (byte) 230, (byte) 228, (byte) 238, (byte) 204, (byte) 125, (byte) 86,
			(byte) 216, (byte) 161, (byte) 89, (byte) 194, (byte) 251, (byte) 54, (byte) 59, (byte) 84, (byte) 105,
			(byte) 40, (byte) 40, (byte) 174, (byte) 189, (byte) 71, (byte) 167, (byte) 90, (byte) 223, (byte) 186,
			(byte) 183, (byte) 89, (byte) 160, (byte) 42, (byte) 195, (byte) 130, (byte) 48, (byte) 107, (byte) 154,
			(byte) 8, (byte) 246, (byte) 183, (byte) 13, (byte) 4, (byte) 199, (byte) 31, (byte) 221, (byte) 61,
			(byte) 136, (byte) 172, (byte) 106, (byte) 199, (byte) 170, (byte) 58, (byte) 104, (byte) 78, (byte) 234,
			(byte) 204, (byte) 149, (byte) 8, (byte) 147, (byte) 118, (byte) 72, (byte) 235, (byte) 154, (byte) 99,
			(byte) 40, (byte) 42, (byte) 113, (byte) 131, (byte) 138, (byte) 35, (byte) 27, (byte) 36, (byte) 200,
			(byte) 28, (byte) 19, (byte) 79, (byte) 102, (byte) 11, (byte) 209, (byte) 125, (byte) 248, (byte) 172,
			(byte) 108, (byte) 117, (byte) 166, (byte) 85, (byte) 224, (byte) 238, (byte) 35, (byte) 29, (byte) 42,
			(byte) 1, (byte) 57, (byte) 84, (byte) 62, (byte) 220, (byte) 1, (byte) 235, (byte) 83, (byte) 207,
			(byte) 242, (byte) 41, (byte) 218, (byte) 58, (byte) 251, (byte) 209, (byte) 97, (byte) 109, (byte) 188,
			(byte) 153, (byte) 88, (byte) 12, (byte) 3, (byte) 133, (byte) 250, (byte) 250, (byte) 213, (byte) 70,
			(byte) 23, (byte) 100, (byte) 212, (byte) 154, (byte) 138, (byte) 185, (byte) 3, (byte) 104, (byte) 13,
			(byte) 112, (byte) 158, (byte) 107, (byte) 204, (byte) 86, (byte) 86, (byte) 228, (byte) 140, (byte) 112,
			(byte) 42, (byte) 75, (byte) 125, (byte) 60, (byte) 89, (byte) 194, (byte) 82, (byte) 89, (byte) 93,
			(byte) 73, (byte) 63, (byte) 120, (byte) 30, (byte) 43, (byte) 105, (byte) 27, (byte) 229, (byte) 7,
			(byte) 20, (byte) 247, (byte) 69, (byte) 117, (byte) 33, (byte) 128, (byte) 32, (byte) 245, (byte) 205,
			(byte) 110, (byte) 233, (byte) 163, (byte) 139, (byte) 219, (byte) 73, (byte) 238, (byte) 103, (byte) 36,
			(byte) 42, (byte) 121, (byte) 243, (byte) 73, (byte) 255, (byte) 0, (byte) 129, (byte) 84, (byte) 130,
			(byte) 20, (byte) 35, (byte) 33, (byte) 219, (byte) 254, (byte) 250, (byte) 172, (byte) 251, (byte) 203,
			(byte) 83, (byte) 167, (byte) 75, (byte) 231, (byte) 196, (byte) 187, (byte) 161, (byte) 39, (byte) 230,
			(byte) 83, (byte) 218, (byte) 174, (byte) 91, (byte) 205, (byte) 105, (byte) 60, (byte) 97, (byte) 208,
			(byte) 168, (byte) 227, (byte) 167, (byte) 165, (byte) 100, (byte) 213, (byte) 141, (byte) 58, (byte) 93,
			(byte) 18, (byte) 249, (byte) 49, (byte) 255, (byte) 0, (byte) 120, (byte) 243, (byte) 254, (byte) 213,
			(byte) 30, (byte) 84, (byte) 93, (byte) 50, (byte) 127, (byte) 239, (byte) 170, (byte) 81, (byte) 228,
			(byte) 15, (byte) 238, (byte) 81, (byte) 254, (byte) 142, (byte) 23, (byte) 248, (byte) 41, (byte) 8,
			(byte) 60, (byte) 168, (byte) 127, (byte) 188, (byte) 127, (byte) 239, (byte) 170, (byte) 41, (byte) 63,
			(byte) 209, (byte) 255, (byte) 0, (byte) 216, (byte) 162, (byte) 141, (byte) 7, (byte) 115, (byte) 67,
			(byte) 29, (byte) 232, (byte) 199, (byte) 81, (byte) 235, (byte) 79, (byte) 160, (byte) 138, (byte) 163,
			(byte) 51, (byte) 152, (byte) 117, (byte) 38, (byte) 233, (byte) 148, (byte) 15, (byte) 226, (byte) 34,
			(byte) 180, (byte) 173, (byte) 237, (byte) 48, (byte) 1, (byte) 97, (byte) 255, (byte) 0, (byte) 214,
			(byte) 169, (byte) 160, (byte) 176, (byte) 219, (byte) 44, (byte) 147, (byte) 75, (byte) 221, (byte) 142,
			(byte) 209, (byte) 87, (byte) 226, (byte) 139, (byte) 208, (byte) 126, (byte) 116, (byte) 233, (byte) 210,
			(byte) 183, (byte) 188, (byte) 199, (byte) 86, (byte) 173, (byte) 215, (byte) 42, (byte) 35, (byte) 142,
			(byte) 53, (byte) 94, (byte) 49, (byte) 83, (byte) 249, (byte) 67, (byte) 32, (byte) 140, (byte) 84,
			(byte) 137, (byte) 22, (byte) 57, (byte) 53, (byte) 32, (byte) 94, (byte) 245, (byte) 185, (byte) 206,
			(byte) 49, (byte) 83, (byte) 10, (byte) 24, (byte) 118, (byte) 52, (byte) 253, (byte) 185, (byte) 96,
			(byte) 77, (byte) 57, (byte) 70, (byte) 65, (byte) 200, (byte) 169, (byte) 54, (byte) 240, (byte) 41,
			(byte) 12, (byte) 16, (byte) 238, (byte) 5, (byte) 79, (byte) 81, (byte) 84, (byte) 53, (byte) 61,
			(byte) 60, (byte) 93, (byte) 33, (byte) 3, (byte) 135, (byte) 31, (byte) 116, (byte) 214, (byte) 134,
			(byte) 222, (byte) 132, (byte) 83, (byte) 156, (byte) 110, (byte) 92, (byte) 142, (byte) 162, (byte) 144,
			(byte) 211, (byte) 105, (byte) 221, (byte) 28, (byte) 121, (byte) 183, (byte) 185, (byte) 137, (byte) 138,
			(byte) 156, (byte) 241, (byte) 237, (byte) 71, (byte) 152, (byte) 234, (byte) 62, (byte) 97, (byte) 205,
			(byte) 116, (byte) 146, (byte) 91, (byte) 164, (byte) 255, (byte) 0, (byte) 49, (byte) 24, (byte) 35,
			(byte) 140, (byte) 138, (byte) 161, (byte) 45, (byte) 178, (byte) 36, (byte) 152, (byte) 35, (byte) 154,
			(byte) 194, (byte) 81, (byte) 104, (byte) 236, (byte) 133, (byte) 85, (byte) 35, (byte) 13, (byte) 163,
			(byte) 121, (byte) 166, (byte) 85, (byte) 60, (byte) 100, (byte) 214, (byte) 196, (byte) 80, (byte) 4,
			(byte) 9, (byte) 26, (byte) 224, (byte) 119, (byte) 168, (byte) 4, (byte) 5, (byte) 175, (byte) 14,
			(byte) 209, (byte) 192, (byte) 239, (byte) 90, (byte) 98, (byte) 220, (byte) 44, (byte) 91, (byte) 137,
			(byte) 201, (byte) 29, (byte) 42, (byte) 233, (byte) 167, (byte) 185, (byte) 157, (byte) 121, (byte) 105,
			(byte) 98, (byte) 154, (byte) 41, (byte) 85, (byte) 83, (byte) 219, (byte) 161, (byte) 169, (byte) 151,
			(byte) 142, (byte) 61, (byte) 63, (byte) 149, (byte) 72, (byte) 209, (byte) 0, (byte) 132, (byte) 122,
			(byte) 115, (byte) 81, (byte) 99, (byte) 24, (byte) 173, (byte) 142, (byte) 113, (byte) 110, (byte) 33,
			(byte) 73, (byte) 237, (byte) 200, (byte) 97, (byte) 199, (byte) 67, (byte) 244, (byte) 174, (byte) 94,
			(byte) 214, (byte) 69, (byte) 176, (byte) 212, (byte) 222, (byte) 7, (byte) 251, (byte) 164, (byte) 227,
			(byte) 165, (byte) 117, (byte) 145, (byte) 99, (byte) 12, (byte) 167, (byte) 21, (byte) 205, (byte) 248,
			(byte) 146, (byte) 31, (byte) 42, (byte) 120, (byte) 231, (byte) 69, (byte) 231, (byte) 161, (byte) 250,
			(byte) 138, (byte) 206, (byte) 162, (byte) 208, (byte) 214, (byte) 139, (byte) 215, (byte) 151, (byte) 185,
			(byte) 171, (byte) 190, (byte) 35, (byte) 252, (byte) 56, (byte) 252, (byte) 40, (byte) 221, (byte) 22,
			(byte) 51, (byte) 183, (byte) 255, (byte) 0, (byte) 29, (byte) 168, (byte) 236, (byte) 174, (byte) 60,
			(byte) 251, (byte) 84, (byte) 125, (byte) 135, (byte) 36, (byte) 96, (byte) 212, (byte) 197, (byte) 184,
			(byte) 255, (byte) 0, (byte) 86, (byte) 213, (byte) 137, (byte) 67, (byte) 124, (byte) 216, (byte) 191,
			(byte) 186, (byte) 127, (byte) 239, (byte) 154, (byte) 41, (byte) 124, (byte) 207, (byte) 250, (byte) 100,
			(byte) 223, (byte) 149, (byte) 20, (byte) 12, (byte) 209, (byte) 198, (byte) 120, (byte) 169, (byte) 35,
			(byte) 143, (byte) 113, (byte) 246, (byte) 20, (byte) 152, (byte) 61, (byte) 106, (byte) 196, (byte) 73,
			(byte) 182, (byte) 44, (byte) 250, (byte) 214, (byte) 145, (byte) 87, (byte) 102, (byte) 82, (byte) 118,
			(byte) 34, (byte) 88, (byte) 183, (byte) 201, (byte) 147, (byte) 208, (byte) 118, (byte) 171, (byte) 11,
			(byte) 31, (byte) 124, (byte) 83, (byte) 97, (byte) 24, (byte) 82, (byte) 125, (byte) 77, (byte) 90,
			(byte) 3, (byte) 3, (byte) 21, (byte) 165, (byte) 204, (byte) 200, (byte) 74, (byte) 99, (byte) 154,
			(byte) 66, (byte) 184, (byte) 197, (byte) 78, (byte) 87, (byte) 62, (byte) 244, (byte) 214, (byte) 92,
			(byte) 227, (byte) 210, (byte) 139, (byte) 136, (byte) 131, (byte) 163, (byte) 98, (byte) 165, (byte) 2,
			(byte) 145, (byte) 151, (byte) 231, (byte) 252, (byte) 42, (byte) 80, (byte) 56, (byte) 161, (byte) 140,
			(byte) 140, (byte) 112, (byte) 219, (byte) 79, (byte) 122, (byte) 90, (byte) 71, (byte) 24, (byte) 193,
			(byte) 29, (byte) 169, (byte) 123, (byte) 231, (byte) 214, (byte) 129, (byte) 145, (byte) 178, (byte) 158,
			(byte) 171, (byte) 138, (byte) 169, (byte) 120, (byte) 141, (byte) 144, (byte) 248, (byte) 233, (byte) 214,
			(byte) 175, (byte) 16, (byte) 122, (byte) 83, (byte) 72, (byte) 12, (byte) 118, (byte) 176, (byte) 224,
			(byte) 138, (byte) 26, (byte) 186, (byte) 176, (byte) 70, (byte) 78, (byte) 46, (byte) 230, (byte) 84,
			(byte) 17, (byte) 147, (byte) 48, (byte) 53, (byte) 121, (byte) 211, (byte) 10, (byte) 5, (byte) 71,
			(byte) 111, (byte) 9, (byte) 75, (byte) 135, (byte) 95, (byte) 238, (byte) 244, (byte) 171, (byte) 18,
			(byte) 142, (byte) 71, (byte) 214, (byte) 148, (byte) 21, (byte) 145, (byte) 85, (byte) 37, (byte) 205,
			(byte) 34, (byte) 177, (byte) 4, (byte) 231, (byte) 138, (byte) 132, (byte) 175, (byte) 31, (byte) 90,
			(byte) 182, (byte) 87, (byte) 4, (byte) 212, (byte) 44, (byte) 160, (byte) 49, (byte) 21, (byte) 68,
			(byte) 21, (byte) 199, (byte) 202, (byte) 224, (byte) 254, (byte) 117, (byte) 71, (byte) 196, (byte) 16,
			(byte) 249, (byte) 154, (byte) 116, (byte) 140, (byte) 163, (byte) 37, (byte) 112, (byte) 195, (byte) 250,
			(byte) 214, (byte) 139, (byte) 167, (byte) 28, (byte) 117, (byte) 166, (byte) 74, (byte) 162, (byte) 88,
			(byte) 25, (byte) 8, (byte) 206, (byte) 84, (byte) 138, (byte) 77, (byte) 93, (byte) 21, (byte) 23,
			(byte) 102, (byte) 153, (byte) 207, (byte) 104, (byte) 55, (byte) 18, (byte) 24, (byte) 90, (byte) 61,
			(byte) 160, (byte) 224, (byte) 250, (byte) 214, (byte) 182, (byte) 249, (byte) 127, (byte) 184, (byte) 63,
			(byte) 58, (byte) 231, (byte) 52, (byte) 150, (byte) 150, (byte) 45, (byte) 65, (byte) 145, (byte) 72,
			(byte) 29, (byte) 143, (byte) 21, (byte) 209, (byte) 126, (byte) 255, (byte) 0, (byte) 213, (byte) 115,
			(byte) 88, (byte) 29, (byte) 19, (byte) 90, (byte) 134, (byte) 249, (byte) 255, (byte) 0, (byte) 184,
			(byte) 159, (byte) 157, (byte) 20, (byte) 126, (byte) 251, (byte) 213, (byte) 127, (byte) 42, (byte) 40,
			(byte) 185, (byte) 54, (byte) 53, (byte) 209, (byte) 114, (byte) 192, (byte) 26, (byte) 179, (byte) 39,
			(byte) 220, (byte) 0, (byte) 81, (byte) 69, (byte) 109, (byte) 29, (byte) 140, (byte) 36, (byte) 245,
			(byte) 31, (byte) 18, (byte) 141, (byte) 192, (byte) 122, (byte) 10, (byte) 177, (byte) 138, (byte) 40,
			(byte) 160, (byte) 66, (byte) 116, (byte) 56, (byte) 161, (byte) 128, (byte) 234, (byte) 40, (byte) 162,
			(byte) 129, (byte) 12, (byte) 97, (byte) 215, (byte) 233, (byte) 73, (byte) 25, (byte) 36, (byte) 81,
			(byte) 69, (byte) 3, (byte) 7, (byte) 25, (byte) 28, (byte) 83, (byte) 87, (byte) 238, (byte) 224,
			(byte) 209, (byte) 69, (byte) 48, (byte) 23, (byte) 60, (byte) 82, (byte) 48, (byte) 252, (byte) 232,
			(byte) 162, (byte) 128, (byte) 19, (byte) 0, (byte) 190, (byte) 238, (byte) 132, (byte) 140, (byte) 26,
			(byte) 107, (byte) 140, (byte) 156, (byte) 122, (byte) 81, (byte) 69, (byte) 2, (byte) 68, (byte) 108,
			(byte) 188, (byte) 212, (byte) 110, (byte) 189, (byte) 40, (byte) 162, (byte) 129, (byte) 145, (byte) 55,
			(byte) 99, (byte) 81, (byte) 72, (byte) 187, (byte) 64, (byte) 97, (byte) 235, (byte) 69, (byte) 20,
			(byte) 193, (byte) 28, (byte) 124, (byte) 136, (byte) 209, (byte) 235, (byte) 178, (byte) 5, (byte) 114,
			(byte) 191, (byte) 188, (byte) 61, (byte) 43, (byte) 162, (byte) 242, (byte) 164, (byte) 219, (byte) 254,
			(byte) 185, (byte) 191, (byte) 33, (byte) 69, (byte) 21, (byte) 206, (byte) 247, (byte) 58, (byte) 166,
			(byte) 244, (byte) 66, (byte) 249, (byte) 18, (byte) 255, (byte) 0, (byte) 207, (byte) 193, (byte) 255,
			(byte) 0, (byte) 190, (byte) 104, (byte) 162, (byte) 138, (byte) 68, (byte) 159, (byte) 255, (byte) 217

	};

}
