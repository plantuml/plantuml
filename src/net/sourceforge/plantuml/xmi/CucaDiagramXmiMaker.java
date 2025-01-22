/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.xmi;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.utils.Log;

public final class CucaDiagramXmiMaker {
	// ::remove folder when __CORE__

	private final CucaDiagram diagram;
	private final FileFormat fileFormat;

	public CucaDiagramXmiMaker(CucaDiagram diagram, FileFormat fileFormat) throws IOException {
		this.diagram = diagram;
		this.fileFormat = fileFormat;
	}

	public static String getModel(UmlDiagram classDiagram) {
		return "model1";
	}

	public void createFiles(OutputStream fos) throws IOException {
		try {
			final XmlDiagramTransformer xmi;
			if (diagram instanceof StateDiagram)
				xmi = createStateDiagram();
			else if (diagram instanceof DescriptionDiagram)
				xmi = createDescriptionDiagram();
			else if (diagram instanceof ClassDiagram)
				xmi = createClassDiagram();
			else
				throw new UnsupportedOperationException(
						"Diagram type " + diagram.getUmlDiagramType() + " is not supported in XMI");

			xmi.transformerXml(fos);
		} catch (ParserConfigurationException e) {
			Log.error(e.toString());
			Logme.error(e);
			throw new IOException(e.toString());
		} catch (TransformerException e) {
			Log.error(e.toString());
			Logme.error(e);
			throw new IOException(e.toString());
		}
	}

	private XmlDiagramTransformer createStateDiagram() throws ParserConfigurationException {
		if (fileFormat == FileFormat.XMI_CUSTOM) {
			return new XmiCucaDiagramCustom<>(XmiStateDiagramCustom.class, diagram);
		} else {
			return new XmiStateDiagram((StateDiagram) diagram);
		}
	}

	private XmlDiagramTransformer createClassDiagram() throws ParserConfigurationException {
		if (fileFormat == FileFormat.XMI_STANDARD)
			return new XmiClassDiagramStandard((ClassDiagram) diagram);
		else if (fileFormat == FileFormat.XMI_ARGO)
			return new XmiClassDiagramArgo((ClassDiagram) diagram);
		else if (fileFormat == FileFormat.XMI_SCRIPT)
			return new XmiClassDiagramScript((ClassDiagram) diagram);
		else if (fileFormat == FileFormat.XMI_STAR)
			return new XmiClassDiagramStar((ClassDiagram) diagram);
		else if (fileFormat == FileFormat.XMI_CUSTOM)
			return new XmiCucaDiagramCustom<>(XmiClassDiagramCustom.class, diagram);
		else
			throw new UnsupportedOperationException();
	}

	private XmlDiagramTransformer createDescriptionDiagram() throws ParserConfigurationException {
		if (fileFormat == FileFormat.XMI_SCRIPT) {
			return new XmiDescriptionDiagramScript((DescriptionDiagram) diagram);
		} else if (fileFormat == FileFormat.XMI_CUSTOM) {
			return new XmiCucaDiagramCustom<>(XmiDescriptionDiagramCustom.class, diagram);
		} else {
			// dont care about which file format is specified, to keep backwards
			// compatibility
			return new XmiDescriptionDiagramStandard((DescriptionDiagram) diagram);
		}
	}

}
