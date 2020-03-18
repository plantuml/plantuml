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
 */
package net.sourceforge.plantuml.directdot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.CounterOutputStream;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.dot.ExeState;
import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.cucadiagram.dot.ProcessState;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphicUtils;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PSystemDot extends AbstractPSystem {

	private final String data;

	public PSystemDot(String data) {
		this.data = data;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Dot)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final Graphviz graphviz = GraphvizUtils.create(null, data,
				StringUtils.goLowerCase(fileFormat.getFileFormat().name()));
		if (graphviz.getExeState() != ExeState.OK) {
			final TextBlock result = GraphicStrings.createForError(
					Arrays.asList("There is an issue with your Dot/Graphviz installation"), false);
			UGraphicUtils.writeImage(os, null, fileFormat, seed(), new ColorMapperIdentity(), HColorUtils.WHITE,
					result);
			return ImageDataSimple.error();
		}
		final CounterOutputStream counter = new CounterOutputStream(os);
		final ProcessState state = graphviz.createFile3(counter);
		// if (state.differs(ProcessState.TERMINATED_OK())) {
		// throw new IllegalStateException("Timeout1 " + state);
		// }
		if (counter.getLength() == 0 || state.differs(ProcessState.TERMINATED_OK())) {
			final TextBlock result = GraphicStrings.createForError(Arrays.asList("GraphViz has crashed"), false);
			UGraphicUtils.writeImage(os, null, fileFormat, seed(), new ColorMapperIdentity(), HColorUtils.WHITE,
					result);
			return ImageDataSimple.error();
		}

		return ImageDataSimple.ok();
	}
}
