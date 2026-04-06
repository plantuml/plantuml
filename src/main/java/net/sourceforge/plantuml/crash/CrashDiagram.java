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
package net.sourceforge.plantuml.crash;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class CrashDiagram extends TitledDiagram {

	public DiagramDescription getDescription() {
		return new DiagramDescription("Crash Diagram");
	}

	public CrashDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, DiagramType.BPM, null, preprocessing);
	}

	@Override
	public TextBlock getTextBlock12026(int num, FileFormatOption fileFormatOption) throws Exception {

		final Display display = Display.create(
				"<b>Crash Diagram</b>",
				"",
				"This diagram is only used to test crash handling.",
				"",
				"There are three types of crash:",
				"  \u2022 During <b>parsing</b>",
				"  \u2022 During <b>finalizing</b>",
				"  \u2022 During <b>drawing</b>",
				"",
				"The purpose is to verify that PlantUML error recovery",
				"mechanisms work correctly, using a diagram whose",
				"behavior is predictable <i>(it crashes)</i>."
		);

		return display.create(FontConfiguration.blackBlueTrue(UFontFactory.sansSerif(14)), HorizontalAlignment.LEFT,
				getSkinParam());
	}

}
