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

/**
 * Contains classes for processing PlantUML source files.
 *
 * <p>
 * The following is a typical control flow for PlantUML:
 *
 * <ul>
 * <li>
 * Arguments are parsed ({@link Run} constructs {@link CliOptions}),
 * </li>
 * <li>
 * Files are read ({@link Run} constructs {@link SourceFileReaderAbstract}),
 * </li>
 * <li>
 * Files are split into blocks ({@link SourceFileReaderAbstract} constructs
 * {@link BlockUmlBuilder}),
 * <li>
 * Blocks are pre-processed ({@link BlockUml} uses
 * {@link net.sourceforge.plantuml.tim.TimLoader}),
 * </li>
 * <li>
 * Blocks are processed and the output images saved ({@link Run} calls
 * {@link SourceFileReaderAbstract#getGeneratedImages()} which calls
 * {@link BlockUml#getDiagram()} which uses
 * {@link PSystemBuilder}),
 * </li>
 * </ul>
 */
package net.sourceforge.plantuml;
