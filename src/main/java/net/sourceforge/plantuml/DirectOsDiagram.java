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
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.teavm.TeaVM;

public abstract class DirectOsDiagram extends Diagram {

	public DirectOsDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	abstract protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException;

	@Override
	final public ImageData exportDiagram(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final long now = System.currentTimeMillis();
		try {
//			if (this instanceof TitledDiagram) {
//				final TitledDiagram titledDiagram = (TitledDiagram) this;
//				final StyleBuilder styleBuilder = titledDiagram.getCurrentStyleBuilder();
//				if (styleBuilder != null) {
//					styleBuilder.printMe();
//				}
//			}
			return exportDiagramNow(os, index, fileFormatOption);
		} finally {
			if (!TeaVM.isTeaVM()) {
				if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.ENABLE_STATS))
					StatsUtilsIncrement.onceMoreGenerate(System.currentTimeMillis() - now, getClass(),
							fileFormatOption.getFileFormat());

			}
		}
	}

}
