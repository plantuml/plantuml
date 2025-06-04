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
package net.sourceforge.plantuml.ditaa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.PSystemBasicFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class PSystemDitaaFactory extends PSystemBasicFactory<PSystemDitaa> {
	// ::remove folder when __HAXE__

	// private StringBuilder data;
	// // -E,--no-separation
	// private boolean performSeparationOfCommonEdges;
	//
	// // -S,--no-shadows
	// private boolean dropShadows;

	public PSystemDitaaFactory() {
		super(DiagramType.DITAA);
	}

	@Override
	public PSystemDitaa initDiagram(UmlSource source, String startLine, PreprocessingArtifact preprocessing) {
		boolean performSeparationOfCommonEdges = true;
		if (startLine != null && (startLine.contains("-E") || startLine.contains("--no-separation")))
			performSeparationOfCommonEdges = false;

		boolean dropShadows = true;
		if (startLine != null && (startLine.contains("-S") || startLine.contains("--no-shadows")))
			dropShadows = false;

		boolean allCornersAreRound = false;
		if (startLine != null && (startLine.contains("-r") || startLine.contains("--round-corners")))
			allCornersAreRound = true;

		boolean transparentBackground = false;
		if (startLine != null && (startLine.contains("-T") || startLine.contains("--transparent")))
			transparentBackground = true;

//		boolean forceFontSize = false;
//		if (startLine != null && startLine.contains("--font-size"))
//			forceFontSize = true;

		final float scale = extractScale(startLine);
		// final Font font = extractFont(startLine);

		return new PSystemDitaa(source, performSeparationOfCommonEdges, dropShadows, allCornersAreRound,
				transparentBackground, scale, preprocessing);

	}

	@Override
	public PSystemDitaa executeLine(UmlSource source, PSystemDitaa system, String line,
			PreprocessingArtifact preprocessing) {
		if (system == null && (line.equals("ditaa") || line.startsWith("ditaa("))) {
			boolean performSeparationOfCommonEdges = true;
			if (line.contains("-E") || line.contains("--no-separation"))
				performSeparationOfCommonEdges = false;

			boolean dropShadows = true;
			if (line.contains("-S") || line.contains("--no-shadows"))
				dropShadows = false;

			boolean allCornersAreRound = false;
			if (line.contains("-r") || line.contains("--round-corners"))
				allCornersAreRound = true;

			boolean transparentBackground = false;
			if (line.contains("-T") || line.contains("--transparent"))
				transparentBackground = true;

//			boolean forceFontSize = false;
//			if (line.contains("--font-size"))
//				forceFontSize = true;

			final float scale = extractScale(line);
			// final Font font = extractFont(line);
			return new PSystemDitaa(source, performSeparationOfCommonEdges, dropShadows, allCornersAreRound,
					transparentBackground, scale, preprocessing);
		}
		if (system == null)
			return null;

		system.add(line);
		return system;
	}

	private static final Pattern SCALE = Pattern.compile("scale=([\\d.]+)");

	private float extractScale(String line) {
		if (line == null)
			return 1;

		final Matcher m = SCALE.matcher(line);
		if (m.find()) {
			final String number = m.group(1);
			return Float.parseFloat(number);
		}
		return 1;
	}

//	private Font extractFont(String line) {
//		if (line == null)
//			return new Font("Dialog", Font.BOLD, 12);
//
//		final Pattern pName = Pattern.compile("font-family=([a-zA-Z0-0 ]+)");
//		final Matcher mName = pName.matcher(line);
//		String fontName = "Dialog";
//		if (mName.find()) {
//			fontName = mName.group(1);
//		}
//
//		final Pattern pVariant = Pattern.compile("font-variant=(BOLD|ITALIC|PLAIN)");
//		final Matcher mVariant = pVariant.matcher(line);
//		int fontVariant = Font.BOLD;
//		if (mVariant.find()) {
//			switch (mVariant.group(1)) {
//			case "BOLD":
//				fontVariant = Font.BOLD;
//				break;
//			case "ITALIC":
//				fontVariant = Font.ITALIC;
//				break;
//			case "PLAIN":
//				fontVariant = Font.PLAIN;
//				break;
//			}
//		}
//
//		final Pattern pSize = Pattern.compile("font-size=([\\d]+)");
//		final Matcher mSize = pSize.matcher(line);
//		int fontSize = 12;
//		if (mSize.find())
//			fontSize = Integer.parseInt(mSize.group(1));
//
//		return new Font(fontName, fontVariant, fontSize);
//	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return null;
	}

}
