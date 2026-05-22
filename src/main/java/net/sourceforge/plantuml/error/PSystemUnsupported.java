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
package net.sourceforge.plantuml.error;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.geom.GraphicPosition;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

public class PSystemUnsupported extends UgDiagram {

	private final List<String> strings = new ArrayList<>();

	public PSystemUnsupported(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		final String directive = source.iterator2().next().getString();

		strings.add("<b>Diagram not supported by this release of PlantUML");
		strings.add(" ");
		strings.add("Sorry, but the following directive \"\"" + directive + "\"\" is not recognized.");
		strings.add(" ");
		strings.add("Possible causes:");
		strings.add("- Typo in the directive or incorrect syntax.");
		strings.add("- The directive was added in a newer PlantUML release.");
		strings.add(" ");
		strings.add("Suggested actions:");
		strings.add("- Check the directive spelling and syntax.");
		strings.add("- Upgrade PlantUML to the latest version.");
		strings.add("- Consult the documentation: https://plantuml.com");
		strings.add(" ");
		
		if (!TeaVM.isTeaVM()) {
			strings.add("Running on " + Version.fullDescription());
			strings.add("(License " + new License().toString() + ")");
		}
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormatOption) throws Exception {
		return GraphicStrings.createBlackOnWhite(strings, PSystemVersion.getPlantumlImage(),
				GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Unsupported)");
	}

}
