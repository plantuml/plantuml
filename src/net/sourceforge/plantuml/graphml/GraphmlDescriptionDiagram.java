
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 *
 */
package net.sourceforge.plantuml.graphml;

import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.SuggestedFile;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.impl.Cuca2GenericConverter;
import net.sourceforge.plantuml.genericdiagram.cucaprocessing.impl.CucaDiagramWrapper;
import net.sourceforge.plantuml.genericdiagram.data.SimpleGenericModel;
import net.sourceforge.plantuml.xmi.XmlDiagramTransformer;

public class GraphmlDescriptionDiagram implements XmlDiagramTransformer {

	private final DescriptionDiagram diagram;
 	private final Cuca2GenericConverter converter;
  private final GraphMLExporter exporter;


	public GraphmlDescriptionDiagram(DescriptionDiagram diagram, SuggestedFile suggestedFile, String graphmlRootDir) throws ParserConfigurationException {

		String parentDir = suggestedFile.getParentFile().toURI().getPath();
		String sourceFileName = suggestedFile.getName();
		Integer blockCount = Integer.valueOf(suggestedFile.toString().replaceAll("(\\[|\\])", ""));

		this.diagram = diagram;
		this.converter = new Cuca2GenericConverter(parentDir + sourceFileName, blockCount, graphmlRootDir);
		this.exporter = new GraphMLExporter();

		converter.visitCucaDiagram(new CucaDiagramWrapper(diagram));
		SimpleGenericModel genericModel = converter.getModel();
		genericModel.acceptVisitor(exporter);
		exporter.finish();
	}


	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
		this.exporter.transformerXml(os);
	}

}
