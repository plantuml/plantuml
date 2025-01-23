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
 * Original Author:  Valentin Vasiu
 * 
 *
 */
package net.sourceforge.plantuml.xmi;

import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;

/**
 * Custom interface for managing a XMI transformation of a class diagram.
 * The scope of this interface is to be implemented by an external service,
 * which is loaded by PlantUML at runtime via dependency injection.
 */
public interface XmiClassDiagramCustom extends XmlDiagramTransformer {

    /**
     * Convert a class diagram to a XMI object, which will be kept internal and
     * finally transformed by the XmlDiagramTransformer implementation.
     *
     * @param diagram the class diagram to convert.
     * @throws ParserConfigurationException if the class diagram cannot be parsed.
     */
    void diagramToXmi(final ClassDiagram diagram) throws ParserConfigurationException;

}
