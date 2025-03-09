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

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.atmp.CucaDiagram;

/**
 * Generic class for managing a custom XMI transformation, using services
 * provided by external providers via dependency injection.
 *
 * @param <S> the class of the service type.
 */
public final class XmiCucaDiagramCustom<S> implements XmlDiagramTransformer {

    private static final String DIAGRAM_TO_XMI_METHOD_NAME = "diagramToXmi";
    private static final String TRANSFORMER_XML_METHOD_NAME = "transformerXml";

    private final Class<S> serviceType;
    private S service;

    /**
     * Transform a diagram into a custom XMI format, using dependency injection to
     * load the external class responsible for the custom transformation.
     *
     * @param serviceType the class of the service type.
     * @param diagram     the diagram to convert.
     * @throws ParserConfigurationException if the converting fails or the custom
     *                                      provider for the service type is not
     *                                      found.
     */
    public XmiCucaDiagramCustom(final Class<S> serviceType, final CucaDiagram diagram)
            throws ParserConfigurationException {
        this.serviceType = serviceType;

        try {
            final ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceType);

            for (final S s : serviceLoader) {
                if (this.service == null) {
                    this.service = s;
                } else {
                    throw new ParserConfigurationException(
                            "Multiple providers for service type " + serviceType.getName());
                }
            }
        } catch (ServiceConfigurationError e) {
            throw new ParserConfigurationException(e.getMessage());
        }

        if (this.service == null) {
            throw new ParserConfigurationException("No provider for service type " + serviceType.getName());
        }

        try {
            final Method builder = serviceType.getMethod(DIAGRAM_TO_XMI_METHOD_NAME, diagram.getClass());
            builder.invoke(this.service, diagram);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

    @Override
    public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {
        try {
            final Method builder = this.serviceType.getMethod(TRANSFORMER_XML_METHOD_NAME, OutputStream.class);
            builder.invoke(this.service, os);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

}
